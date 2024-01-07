package dev.asodesu.wherebus

import dev.asodesu.wherebus.commands.Command
import dev.asodesu.wherebus.commands.CommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class Config : WebMvcConfigurer {
    @Value("\${discord.bot-token}")
    lateinit var token: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("*")
    }

    @Autowired
    lateinit var commands: List<Command>
    @Bean
    fun jda(): JDA {
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