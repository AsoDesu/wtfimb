package dev.asodesu.wherebus.commands

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

interface Command {
    val name: String

    fun create(): SubcommandData
    fun handle(evt: SlashCommandInteractionEvent)
    fun complete(evt: CommandAutoCompleteInteractionEvent) {}
}