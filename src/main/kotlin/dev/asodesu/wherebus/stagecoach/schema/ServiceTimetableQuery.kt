package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

data class ServiceTimetableQuery(
    @JsonProperty("ServiceId")
    val serviceId: String,
    @JsonProperty("GenericDayPattern")
    val genericDayPattern: String,
    @JsonProperty("Departure")
    val departure: Departure,
    @JsonProperty("ResponseCharacteristics")
    val responseCharacteristics: ResponseCharacteristics,
    @JsonProperty("RequestId")
    val requestId: String,
)

data class TripEvents(
    @JsonProperty("TimingInformationPoints")
    val timingInformationPoints: Boolean,
    @JsonProperty("NonTimingInformationPoints")
    val nonTimingInformationPoints: Boolean,
)
