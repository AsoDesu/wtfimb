package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

data class ServiceTimetableResponse(
    @JsonProperty("RequestId")
    val requestId: String,
    @JsonProperty("Services")
    val services: Services? = null,
    @JsonProperty("TimetableRows")
    val timetableRows: TimetableRows? = null,
    @JsonProperty("TimetableColumns")
    val timetableColumns: TimetableColumns? = null,
)

data class Services(
    @JsonProperty("Service")
    val service: List<TimetableService>,
)

data class TimetableService(
    @JsonProperty("ServiceNumber")
    val serviceNumber: String,
    @JsonProperty("Description")
    val description: String,
    @JsonProperty("Direction")
    val direction: String,
)

data class TimetableRows(
    @JsonProperty("TimetableRow")
    val timetableRow: List<TimetableRow>,
)

data class TimetableRow(
    @JsonProperty("Name")
    val name: String,
    @JsonProperty("Stop")
    val stop: StopLabel,
)

data class StopLabel(
    @JsonProperty("StopLabel")
    val stopLabel: String,
)

data class TimetableColumns(
    @JsonProperty("TimetableColumn")
    val timetableColumn: List<TimetableColumn>,
)

data class TimetableColumn(
    @JsonProperty("Events")
    val events: TimetableEvents,
)

data class TimetableEvents(
    @JsonProperty("Event")
    val event: List<TimetableEvent>,
)

data class TimetableEvent(
    @JsonProperty("RowOrdinal")
    val rowOrdinal: Long,
    @JsonProperty("ScheduledEventTime")
    val scheduledEventTime: Value<String>,
)