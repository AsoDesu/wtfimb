package dev.asodesu.wherebus.commands

import net.dv8tion.jda.api.interactions.commands.build.Commands

object Commands {
    val stagecoach = Commands.slash("stagecoach", "The stagecoach bus command")
    val test = Commands.slash("test", "wtfimb test commands")
}