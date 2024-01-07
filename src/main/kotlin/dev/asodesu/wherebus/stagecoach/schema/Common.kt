package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Value<T>(
    val value: T,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseCharacteristics(
    @JsonProperty("MaxLaterTimetableColumns")
    val maxLaterTimetableColumns: Value<Long>? = null,
    @JsonProperty("MaxLaterEvents")
    val maxLaterEvents: Value<Int>? = null,
    @JsonProperty("TripEvents")
    val tripEvents: TripEvents? = null,
    @JsonProperty("VehicleLegPaths")
    val vehicleLegPaths: Boolean? = null,
    @JsonProperty("StopCoordinates")
    val stopCoordinates: Boolean? = null,
    @JsonProperty("IncludeSituations")
    val includeSituations: Boolean? = null,
    @JsonProperty("GenerateKML")
    val generateKml: Boolean? = null,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class Departure(
    @JsonProperty("TargetDepartureTime")
    val targetDepartureTime: Value<String>,
    @JsonProperty("EarliestDepartureLeeway")
    val earliestDepartureLeeway: String? = null,
    @JsonProperty("LatestDepartureLeeway")
    val latestDepartureLeeway: String? = null,
    @JsonProperty("DepartureStopLabel")
    val departureStopLabel: String? = null
)

enum class Direction {
    INBOUND,
    OUTBOUND;

    companion object {
        fun fromChar(char: Char): Direction? {
            return entries.firstOrNull {
                it.name.startsWith(char.uppercase())
            }
        }
    }
}