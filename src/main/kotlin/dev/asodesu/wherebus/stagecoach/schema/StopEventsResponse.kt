package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.IllegalStateException

data class StopEvents(
    @JsonProperty("RequestId")
    val requestId: String
) {
    @JsonProperty("Stops")
    val stops: StopList? = null
    @JsonProperty("Events")
    val events: Events? = null
}

data class StopList(
    @JsonProperty("Stop")
    val stop: List<Stop>,
)

data class Stop(
    @JsonProperty("StopLabel")
    val stopLabel: String,
    @JsonProperty("SMSCode")
    val smscode: String,
    @JsonProperty("CompassPoint")
    val compassPoint: String,
    @JsonProperty("Name")
    val name: String,
)

data class Events(
    @JsonProperty("Event")
    val event: List<Event>,
)

data class Event(
    @JsonProperty("StopLabel")
    val stopLabel: String,
    @JsonProperty("ScheduledArrivalTime")
    val scheduledArrivalTime: Value<String>? = null,
    @JsonProperty("ScheduledDepartureTime")
    val scheduledDepartureTime: Value<String>? = null,
    @JsonProperty("Trip")
    val trip: Trip,
) {
    val scheduledTime = scheduledArrivalTime ?: scheduledDepartureTime ?: throw IllegalStateException("No scheduled times on this stop-event")
}

data class Trip(
    @JsonProperty("TripId")
    val tripId: String,
    @JsonProperty("Trunk")
    val trunk: Boolean,
    @JsonProperty("Service")
    val service: TripService,
    @JsonProperty("DestinationBoard")
    val destinationBoard: String,
)

data class TripService(
    @JsonProperty("ServiceId")
    val serviceId: String,
    @JsonProperty("Mode")
    val mode: String,
    @JsonProperty("Operator")
    val operator: Operator,
    @JsonProperty("ServiceNumber")
    val serviceNumber: String,
    @JsonProperty("Description")
    val description: String,
    @JsonProperty("Direction")
    val direction: String,
)

data class Operator(
    @JsonProperty("OperatorCode")
    val operatorCode: String,
    @JsonProperty("PublicName")
    val publicName: String,
)
