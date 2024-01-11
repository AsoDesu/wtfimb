package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel

class Subscription(
    val discordUserId: String,
    val service: ServiceDetail,

    private val jda: JDA,
    val stagecoach: StagecoachService,
) {
    var timetable: ServiceTimetableResponse? = null
    var assignedVehicleRef: String? = null

    private val queries = SubscriptionQuery(this)
    var realTimeUpdater: RealTimeUpdater? = null

    fun query() = queries.query()

    fun getCallFromMonitor(monitor: StopMonitorResponse): MonitoredCall? {
        val stops = monitor.stopMonitors.stopMonitor?.firstOrNull()
        return stops?.monitoredCalls?.monitoredCall?.firstOrNull { call ->
            call.lineRef == service.number
                    && call.direction.equals(service.direction.name, true)
                    && parseSeconds(call.aimedArrivalTime) == service.epochSeconds
        }
    }

    fun getVehicleDescription(vehicleInfo: Service): String {
        val lastUpdateMillis = vehicleInfo.updateTime.toLongOrNull() ?: 0

        val timetable = getTimetable(vehicleInfo)
        val nextStop = getStopFromTimetable(timetable, vehicleInfo.nextStopOnRoute)

        return buildString {
            appendLine(":bus: **Assigned Vehicle: ** `${vehicleInfo.fleetNumber}`")
            appendLine("> :hourglass_flowing_sand: **Last Update: ** <t:${lastUpdateMillis / 1000}:R>")
            appendLine("> :round_pushpin: **On route: ** ${vehicleInfo.serviceNumber} | ${vehicleInfo.serviceDescription}")
            appendLine("> :busstop: **Next Stop: ** `${nextStop?.name ?: "Unknown"}`")
        }
    }

    fun getTimetable(vehicleInfo: Service): ServiceTimetableResponse {
        if (timetable == null || timetable?.requestId != vehicleInfo.serviceId) {
            timetable = stagecoach.serviceTimetable(vehicleInfo)
        }
        return timetable!!
    }

    fun getStopFromTimetable(timetable: ServiceTimetableResponse, stopLabel: String): TimetableRow? {
        return timetable.timetableRows?.timetableRow?.firstOrNull { row ->
            row.stop.stopLabel == stopLabel
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Subscription
        return discordUserId == other.discordUserId
    }
    override fun hashCode(): Int {
        return discordUserId.hashCode()
    }
}