package dev.asodesu.wherebus.stagecoach.schema

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
    val aimedArrivalTime: String,
    val aimedDepartureTime: String,
) {

    var expectedArrivalTime: String? = null
    var expectedDepartureTime: String? = null

}
