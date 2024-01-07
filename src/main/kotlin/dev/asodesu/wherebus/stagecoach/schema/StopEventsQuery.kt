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
