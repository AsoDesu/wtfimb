package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.stagecoach.StagecoachService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class SubscriptionManager {
    @Autowired
    private lateinit var context: ApplicationContext
    @Autowired
    private lateinit var stagecoach: StagecoachService

    private val subscriptions = mutableSetOf<Subscription>()

    fun newSubscription(member: Member, channel: GuildMessageChannel, service: ServiceDetail): Subscription {
        val guild = channel.guild
        val jda = context.getBean(JDA::class.java)
        val subscription = Subscription(guild.id, member.id, channel.id, service, jda, stagecoach)
        subscriptions.add(subscription)

        return subscription
    }

    fun getSubscriptionByUser(userId: String) = subscriptions.firstOrNull { it.discordUserId == userId }

}