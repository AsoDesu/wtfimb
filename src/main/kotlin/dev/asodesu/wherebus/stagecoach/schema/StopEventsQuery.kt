package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

class StopEventsQuery(
    @JsonProperty("Stops")
    val stops: Stops,
    @JsonProperty("Departure")
    val departure: Departure,
    @JsonProperty("ResponseCharacteristics")
    val responseCharacteristics: ResponseCharacteristics,
    @JsonProperty("RequestId")
    val requestId: String,
)

class Stops(
    @JsonProperty("StopLabel")
    val stopLabel: List<String>,
)

class Departure(
    @JsonProperty("TargetDepartureTime")
    val targetDepartureTime: Value<String>,
    @JsonProperty("EarliestDepartureLeeway")
    val earliestDepartureLeeway: String,
    @JsonProperty("LatestDepartureLeeway")
    val latestDepartureLeeway: String,
)

class ResponseCharacteristics(
    @JsonProperty("MaxLaterEvents")
    val maxLaterEvents: Value<Int>,
    @JsonProperty("StopCoordinates")
    val stopCoordinates: Boolean,
    @JsonProperty("IncludeSituations")
    val includeSituations: Boolean,
    @JsonProperty("GenerateKML")
    val generateKml: Boolean,
)
