package dev.asodesu.wherebus

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.math.log

@SpringBootApplication
class WherebusApplication

object WherebusBootstrap {
    val logger = LoggerFactory.getLogger("StartBootstrap")
    val serverPortRegex = Pattern.compile("(server\\.port=)(\\d+)")

    @JvmStatic
    fun main(args: Array<String>) {
        val propertiesFile = File("./application.properties")
        if (!propertiesFile.exists()) {
            val resource = ClassPathResource("default.properties")
            val stream = resource.url.openStream()
            if (stream == null) {
                logger.error("Uh oh.. couldn't find default properties resource!")
                return
            } else {
                Files.copy(stream, propertiesFile.toPath())
                stream.close()
            }

            logger.error("Properties file was not found, so the default one has been created!")
            logger.error("Please configure the bot in the 'app.properties' file and start up again!")
            return
        }
        val app = SpringApplication(WherebusApplication::class.java)

        val content = propertiesFile.readText()
        val hasServerPort = serverPortRegex.matcher(content).find()
        if (!hasServerPort) {
            logger.info("Server Port has not been set, Web App disabled!")
            app.webApplicationType = WebApplicationType.NONE
        }

        app.run(*args)
    }
}
