package dev.asodesu.wherebus.stagecoach.schema

import java.lang.IllegalStateException
import kotlin.math.exp

class StopMonitorResponse(
    val stopMonitors: StopMonitors,
)

class StopMonitors {
    var stopMonitor: List<StopMonitor>? = null
}

class StopMonitor(
    val stopPointLabel: String,
    val monitoredCalls: MonitoredCalls,
)

class MonitoredCalls(
    val monitoredCall: List<MonitoredCall>,
)

class MonitoredCall(
    val operatorRef: String,
    val datedVehicleJourneyRef: String,
    val visitNumber: Long,
    val lineRef: String,
    val stopPointLabel: String,
    val stopPointName: String,
    val destinationDisplay: String,
    val timingPoint: Boolean,
    val direction: String,
    val vehicleRef: String,
    val cancelled: Boolean,
    var aimedArrivalTime: String? = null,
    var aimedDepartureTime: String? = null,
    var expectedArrivalTime: String? = null,
    var expectedDepartureTime: String? = null
) {
    var aimedTime = aimedArrivalTime ?: aimedDepartureTime ?: throw IllegalStateException("No arrival or departure time on call.")
    var expectedTime = expectedArrivalTime ?: expectedDepartureTime
}
