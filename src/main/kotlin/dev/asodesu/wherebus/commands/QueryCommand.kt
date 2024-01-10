package dev.asodesu.wherebus.commands

import dev.asodesu.wherebus.subscription.SubscriptionManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class QueryCommand : Command {
    @Autowired
    private lateinit var subscriptions: SubscriptionManager

    override val name = "query"
    override fun create() = SubcommandData(name, "Query the current information about your subscribed trip")

    override fun handle(evt: SlashCommandInteractionEvent) {
        val subscription = subscriptions.getSubscriptionByUser(evt.user.id)
            ?: return evt.reply("You don't have an active subscription!").setEphemeral(true).queue()

        val hook = evt.deferReply(true).complete()
        val message = subscription.query()
        hook.sendMessage(message).queue()
    }

    override val parentCommand = Commands.stagecoach
}