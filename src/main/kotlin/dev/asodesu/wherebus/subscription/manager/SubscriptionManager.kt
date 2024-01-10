package dev.asodesu.wherebus.subscription.manager

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.subscription.ServiceDetail
import dev.asodesu.wherebus.subscription.Subscription
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class SubscriptionManager {
    @Autowired
    private lateinit var context: ApplicationContext
    @Autowired
    private lateinit var stagecoach: StagecoachService

    private val subscriptions = mutableMapOf<String, Subscription>()

    fun newSubscription(user: User, service: ServiceDetail): Subscription {
        val jda = context.getBean(JDA::class.java)
        val subscription = Subscription(user.id,  service, jda, stagecoach)
        subscriptions[user.id] = subscription

        return subscription
    }

    fun removeSubscriptions(userId: String) = subscriptions.remove(userId)

    fun getSubscriptionByUser(userId: String) = subscriptions[userId]

}