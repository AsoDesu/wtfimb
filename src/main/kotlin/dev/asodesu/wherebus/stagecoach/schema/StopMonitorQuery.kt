package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

data class StopMonitorRequestPayload(
    @JsonProperty("StopMonitorRequest")
    val stopMonitorRequest: StopMonitorRequest,
)

data class StopMonitorRequest(
    val header: Header = Header("", "", ""),
    val lookAheadMinutes: Long,
    val stopMonitorQueries: StopMonitorQueries,
)

data class Header(
    val retailOperation: String,
    val channel: String,
    val ipAddress: String,
)

data class StopMonitorQueries(
    val stopMonitorQuery: List<StopMonitorQuery>,
)

data class StopMonitorQuery(
    val stopPointLabel: String,
    val servicesFilters: ServicesFilters,
)

data class ServicesFilters(
    val servicesFilter: List<ServicesFilter>,
)

data class ServicesFilter(
    val filter: String,
)
