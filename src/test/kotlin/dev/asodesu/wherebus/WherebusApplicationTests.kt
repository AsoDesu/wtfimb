package dev.asodesu.wherebus

import dev.asodesu.wherebus.stagecoach.StagecoachService
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.math.log

@SpringBootTest
class WherebusApplicationTests {
    val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var stagecoachService: StagecoachService

    @Test
    fun testVehicle() {
        val testFleet = "37070"
        val vehicles = stagecoachService.getVehicle(testFleet)
        vehicles.services.forEachIndexed { i, service ->
            logger.info("Service #$i")

            logger.info("Service: ${service.serviceNumber}")
            logger.info("Description: ${service.serviceDescription}")
            logger.info("Latitude: ${service.latitude}")
            logger.info("Longitude: ${service.longitude}")

            logger.info("---------")
        }
    }

}
