package dev.asodesu.wherebus.commands

import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.subscription.SubscriptionManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId

@Component
class UnsubscribeCommand(val subscriptions: SubscriptionManager) : Command {
    override val name = "unsubscribe"

    override fun create() = SubcommandData(name, "Unsubscribes from your current bus")

    override fun handle(evt: SlashCommandInteractionEvent) {
        val userId = evt.user.id
        val previousSubscription = subscriptions.removeSubscriptions(userId)
        if (previousSubscription == null) {
            evt.reply("You don't have an active subscription!").setEphemeral(true).queue()
        } else {
            val service = previousSubscription.service
            val time = getTime(Instant.ofEpochSecond(service.epochSeconds).atZone(ZoneId.systemDefault()))
            evt.reply("Unsubscribed from bus trip: **${service.number}** at $time").setEphemeral(true).queue()
        }
    }
}