package dev.asodesu.wherebus.subscription.manager

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.subscription.ServiceDetail
import dev.asodesu.wherebus.subscription.Subscription
import jakarta.annotation.PostConstruct
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executors
import kotlin.math.log

@Component
class SubscriptionManager : TimerTask() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Autowired
    private lateinit var context: ApplicationContext
    @Autowired
    private lateinit var stagecoach: StagecoachService

    private val subscriptions = mutableMapOf<String, Subscription>()
    private val timer = Timer("Subscription Updater")
    private val threadPool = Executors.newCachedThreadPool()

    @PostConstruct
    fun postConstruct() {
        timer.scheduleAtFixedRate(this, 0L, 5000L)
    }

    fun newSubscription(user: User, service: ServiceDetail): Subscription {
        val jda = context.getBean(JDA::class.java)
        val subscription = Subscription(user.id,  service, jda, stagecoach)
        subscriptions[user.id] = subscription

        return subscription
    }

    override fun run() {
        subscriptions.forEach { (id, subscription) ->
            threadPool.execute {
                try {
                    val updater = subscription.realTimeUpdater ?: return@execute
                    val updates = updater.runUpdates()
                    updates.send(updater.channel)
                    logger.info("Updated subscription $id")
                } catch (e: Exception) {
                    logger.error("Failed updating subscription $id", e)
                }
            }
        }
    }

    fun removeSubscriptions(userId: String) = subscriptions.remove(userId)

    fun getSubscriptionByUser(userId: String) = subscriptions[userId]

}