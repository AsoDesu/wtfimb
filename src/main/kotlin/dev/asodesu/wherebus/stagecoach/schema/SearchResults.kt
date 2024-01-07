package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

class SearchResults(
    @JsonProperty("RequestId")
    val requestId: String,
    @JsonProperty("ResponseMessages")
    val responseMessages: ResponseMessages,
    @JsonProperty("Locations")
    val locations: Locations,
)

class ResponseMessages(
    @JsonProperty("ResponseMessage")
    val responseMessage: List<ResponseMessage>,
)

class ResponseMessage(
    @JsonProperty("Success")
    val success: Boolean,
    @JsonProperty("MessageText")
    val messageText: String,
)

class Locations(
    @JsonProperty("Location")
    val location: List<Location>,
)

class Location(
    @JsonProperty("Category")
    val category: String,
    @JsonProperty("Geocode")
    val geocode: Geocode,
    @JsonProperty("FullText")
    val fullText: String,
    @JsonProperty("StopData")
    val stopData: StopData,
)

class Geocode(
    @JsonProperty("Grid")
    val grid: Value<String>,
    @JsonProperty("Longitude")
    val longitude: Double,
    @JsonProperty("Latitude")
    val latitude: Double,
)

class StopData(
    @JsonProperty("StopLabel")
    val stopLabel: String,
)
