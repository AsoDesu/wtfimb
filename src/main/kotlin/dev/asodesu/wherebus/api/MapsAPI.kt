package dev.asodesu.wherebus.api

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.exitProcess

@RestController
@RequestMapping("/api/maps/")
class MapsAPI {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${wtfimb.maps.type}")
    lateinit var type: String

    @Value("\${wtfimb.maps.api-key}")
    lateinit var apiKey: String

    @Value("\${wtfimb.maps.map-id}")
    lateinit var mapId: String

    @PostConstruct
    fun postConstruct() {
        if (type != "osm" && type != "google") {
            logger.error("'$type' is not a valid map type! Your available options are 'google' and 'osm'")
            exitProcess(0)
        }
    }

    @GetMapping("/info")
    fun getMapInfo(): MapKeys {
        return MapKeys(type, apiKey, mapId)
    }

    class MapKeys(val type: String, val apiKey: String, val mapId: String)

}