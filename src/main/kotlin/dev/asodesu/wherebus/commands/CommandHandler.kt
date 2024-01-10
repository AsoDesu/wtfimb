package dev.asodesu.wherebus.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import java.util.concurrent.Executors

class CommandHandler(val jda: JDA, val commands: List<Command>) {
    private val threadPool = Executors.newCachedThreadPool()

    fun create() {
        val groupedCommands = commands.groupBy { it.parentCommand }
        val commands = groupedCommands.map { (parent, subCommands) ->
            val subCommandsData = subCommands.map { it.create() }
            parent.addSubcommands(subCommandsData)
        }

        jda.guilds.forEach {
            it.updateCommands()
                .addCommands(commands)
                .queue()
        }
    }

    @SubscribeEvent
    fun handleInteraction(evt: SlashCommandInteractionEvent) {
        if (evt.name != "stagecoach") return
        val subCommand = evt.subcommandName
        if (subCommand == null) {
            evt.reply("Hey!").setEphemeral(true).queue()
            return
        }

        val find = commands.find { it.name == subCommand }
        if (find != null) {
            threadPool.execute {
                find.handle(evt)
            }
        } else {
            evt.reply("Subcommand not found!")
                .setEphemeral(true)
                .queue()
        }
    }

    @SubscribeEvent
    fun handleAutoComplete(evt: CommandAutoCompleteInteractionEvent) {
        val name = evt.subcommandName ?: return
        val find = commands.find { it.name == name }
        if (find != null) {
            threadPool.execute {
                find.complete(evt)
            }
        } else {
            evt.replyChoices().queue()
        }
    }

}