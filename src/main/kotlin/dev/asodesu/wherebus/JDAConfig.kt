package dev.asodesu.wherebus

import dev.asodesu.wherebus.commands.Command
import dev.asodesu.wherebus.commands.CommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.math.log
import kotlin.system.exitProcess

@Configuration
class JDAConfig {
    val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${discord.bot-token}")
    lateinit var token: String

    @Autowired
    lateinit var commands: List<Command>
    @Bean
    fun jda(): JDA {
        if (token.isBlank()) {
            logger.error("")
            logger.error("Discord Bot Token has not been set!")
            logger.error("You must set the 'discord.bot-token' config value to a bot token from the " +
                    "Discord Developer Portal (https://discord.com/developers/applications)")
            exitProcess(0)
        }

        val jda = JDABuilder.createDefault(token)
            .setEventManager(AnnotatedEventManager())
            .build()
        jda.awaitReady()

        val commandHandler = CommandHandler(jda, commands)
        jda.addEventListener(commandHandler)
        commandHandler.create()
        return jda
    }

}