package dev.asodesu.wherebus.stagecoach

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.asodesu.wherebus.stagecoach.schema.*
import jakarta.annotation.PostConstruct
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Color
import java.time.Instant

/**
 * A service for fetching data from stagecoach's API
 */
@Service
class StagecoachService(val objectMapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json".toMediaType()
    val colors = arrayOf("039b78", "f9ae00", "0079c1")

    var adcUrl = ""
    var tisUrl = ""
    var authHeaders: Map<String, String> = mapOf()

    @PostConstruct
    fun postConstruct() {
        logger.info("Requesting tokens from stagecoach...")
        val request = Request.Builder()
            .get()
            .url("https://www.stagecoachbus.com/config/config.json")
            .build()
        val response = client.newCall(request).execute()
        val text = response.body?.string() ?: "{}"
        val config = objectMapper.readValue<StagecoachConfig>(text)

        adcUrl = config.avl.url
        tisUrl = config.tis.url
        authHeaders = config.tis.headers!!
    }

    fun getVehicle(fleetNo: String): VehiclesResponse {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.stagecoach-technology.net")
            .addPathSegments("vehicle-tracking/v1/vehicles")
            .addQueryParameter("fleetno", fleetNo)
            .addQueryParameter("descriptive_fields", "true")
            .build()

        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        val response = makeRequest(request)
        val text = response.body?.string() ?: "{}"
        return objectMapper.readValue(text)
    }

    fun searchStops(query: String): SearchResults {
        val payload = SearchQuery(
            searchText = query,
            searchAlgorithm = "approximateMatch",
            maxNumberOfResults = 10,
            requestId = "stagecoach-you-have-a-nice-api",
            categories = Categories(
                category = listOf("busStop")
            )
        )
        return stagecoachPostRequest("https://api.stagecoachbus.com/tis/v3/location-query", payload)
    }

    fun monitorStops(stop: String, lookAhead: Int = 720, serviceFilter: List<String>): StopMonitorResponse {
        val payload = StopMonitorRequestPayload(
            stopMonitorRequest = StopMonitorRequest(
                lookAheadMinutes = lookAhead.toLong(),
                stopMonitorQueries = StopMonitorQueries(
                    stopMonitorQuery = listOf(
                        StopMonitorQuery(
                            stopPointLabel = stop,
                            servicesFilters = ServicesFilters(
                                servicesFilter = serviceFilter.map { ServicesFilter(it) }
                            )
                        )
                    )
                )
            )
        )
        return stagecoachPostRequest("https://api.stagecoachbus.com/adc/stop-monitor", payload)
    }

    fun stopEvents(stop: String, maxEvents: Int = 60): StopEvents {
        val currentTime = Instant.now().minusSeconds(900).toString()
        val payload = StopEventsQuery(
            stops = Stops(
                stopLabel = listOf(stop)
            ),
            departure = Departure(
                targetDepartureTime = Value(currentTime),
                earliestDepartureLeeway = "PT0M",
                latestDepartureLeeway = "PT240M"
            ),
            responseCharacteristics = ResponseCharacteristics(
                maxLaterEvents = Value(maxEvents),
                stopCoordinates = false,
                includeSituations = false,
                generateKml = false
            ),
            requestId = "stagecoach-you-have-a-nice-api"
        )
        return stagecoachPostRequest("https://api.stagecoachbus.com/tis/v3/stop-event-query", payload)
    }

    fun serviceTimetable(service: dev.asodesu.wherebus.stagecoach.schema.Service): ServiceTimetableResponse {
        val departureTime = service.aimedOriginStopDepartureTime.toLongOrNull() ?: 0
        return serviceTimetable(
            service.serviceId,
            departureTime,
            service.originStopReference
        )
    }

    fun serviceTimetable(serviceId: String, departureTime: Long, originStop: String): ServiceTimetableResponse {
        val targetDepartureTime = Instant.ofEpochMilli(departureTime).toString()
        val payload = ServiceTimetableQuery(
            serviceId = serviceId,
            genericDayPattern = "targetDateOnly",
            departure = Departure(
                targetDepartureTime = Value(targetDepartureTime),
                departureStopLabel = originStop
            ),
            responseCharacteristics = ResponseCharacteristics(
                maxLaterTimetableColumns = Value(1),
                tripEvents = TripEvents(
                    timingInformationPoints = true,
                    nonTimingInformationPoints = true
                ),
                vehicleLegPaths = false,
                stopCoordinates = false,
                includeSituations = false,
                generateKml = false
            ),
            requestId = "stagecoach-you-have-a-nice-api"
        )
        return stagecoachPostRequest("https://api.stagecoachbus.com/tis/v3/service-timetable-query", payload)
    }

    private inline fun <reified T> stagecoachPostRequest(url: String, payload: Any): T {
        val payloadText = objectMapper.writeValueAsString(payload)

        val request = Request.Builder()
            .url(url)
            .post(payloadText.toRequestBody(jsonMediaType))
            .build()
        val response = makeRequest(request)
        val text = response.body?.string() ?: "{}"
        return objectMapper.readValue(text)
    }

    private fun makeRequest(request: Request): Response {
        val builder = request.newBuilder()
        authHeaders.forEach { (key, value) ->
            builder.header(key, value)
        }
        val req = builder.build()
        return client.newCall(req).execute()
    }

    fun getStagecoachColor() = Color(colors.random().toInt(16))

}