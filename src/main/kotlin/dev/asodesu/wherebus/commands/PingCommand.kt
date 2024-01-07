package dev.asodesu.wherebus.commands

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.springframework.stereotype.Component

@Component
class PingCommand : Command {
    override val name = "ping"

    override fun create(): SubcommandData {
        return SubcommandData("ping", "Pings the bot")
            .addOption(OptionType.STRING, "response", "The message i should respond with", true, true)
    }

    override fun handle(evt: SlashCommandInteractionEvent) {
        val option = evt.getOption("response")?.asString ?: return
        evt.reply("Reply with: $option").queue()
    }

    override fun complete(evt: CommandAutoCompleteInteractionEvent) {
        evt.replyChoice("Hello (opt1)", "option_1", )
            .addChoice("Hello (opt2)", "option_2", )
            .addChoice("Nano: ${System.nanoTime()}", "option_3")
            .queue()
    }
}