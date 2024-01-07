package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Service
import dev.asodesu.wherebus.stagecoach.schema.ServiceTimetableResponse
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import java.time.Instant
import java.time.ZoneId

class SubscriptionQueries(val subscription: Subscription) {
    private val stagecoach by subscription::stagecoach
    private val service by subscription::service
    private var timetable by subscription::timetable

    fun query(): MessageCreateData {
        val event = subscription.service.getEvent(stagecoach)
        val date = Instant.ofEpochSecond(service.epochSeconds).atZone(ZoneId.systemDefault())

        val monitor = stagecoach.monitorStops(service.stopId, serviceFilter = listOf(service.number))
        val stops = monitor.stopMonitors.stopMonitor?.firstOrNull()
        val call = stops?.monitoredCalls?.monitoredCall?.firstOrNull { call ->
            call.lineRef == service.number
                    && call.direction.equals(service.direction.name, true)
                    && parseSeconds(call.aimedArrivalTime) == service.epochSeconds
        }
        val vehicle = call?.vehicleRef

        var description: String
        val emote: String
        if (call?.expectedArrivalTime == null) {
            emote = ":calendar_spiral:"
            description = ":alarm_clock: **Scheduled Arrival: ** ${getTime(date)}"
        } else {
            val expectedDate = Instant.parse(call.expectedArrivalTime).atZone(ZoneId.systemDefault())
            description = ":alarm_clock: **Expected Arrival: ** ${getTime(expectedDate)}"
            emote = if (vehicle.isNullOrBlank()) {
                ":infinity:"
            } else {
                ":busstop:"
            }
        }

        description += "\n"
        description += getVehicleDescription(vehicle)

        val embed = EmbedBuilder()
            .setTitle("$emote [${getTime(date)}] **${service.number}** | ${event.trip.service.description}")
            .setColor(stagecoach.getStagecoachColor())
            .setDescription(description)
            .setFooter("Data sourced directly from Stagecoach", "https://i.imgur.com/DOAvG3Y.png")
            .setTimestamp(Instant.now())
            .build()

        val message = MessageCreateBuilder()
            .addEmbeds(embed)

        if (!vehicle.isNullOrBlank()) {
            val (_, fleetNo) = vehicle.split("-")
            val button = Button.link("https://wtfimb.asodesu.dev/map?bus=$fleetNo", "View on map")
                .withEmoji(Emoji.fromUnicode("U+1F30D"))
            message.addActionRow(button)
        }

        return message.build()
    }

    private fun getVehicleDescription(vehicleRef: String?): String {
        val noBusDesc = ":bus: **No assigned bus, no additional data**"
        if (vehicleRef.isNullOrBlank()) return noBusDesc

        val (_, fleetNo) = vehicleRef.split("-")
        val vehicleInfo = getVehicleInfo(fleetNo) ?: return noBusDesc
        val lastUpdateMillis = vehicleInfo.updateTime.toLongOrNull() ?: 0

        if (timetable == null) {
            timetable = stagecoach.serviceTimetable(vehicleInfo)
        }
        val timetable = timetable!!
        val nextStop = timetable.timetableRows?.timetableRow?.firstOrNull { row ->
            row.stop.stopLabel == vehicleInfo.nextStopOnRoute
        }

        return buildString {
            appendLine()
            appendLine(":bus: **Assigned Vehicle: ** `$vehicleRef`")
            appendLine("> :hourglass_flowing_sand: **Last Update: ** <t:${lastUpdateMillis / 1000}:R>")
            appendLine("> :round_pushpin: **On route: ** ${vehicleInfo.serviceNumber} | ${vehicleInfo.serviceDescription}")
            appendLine("> :busstop: **Next Stop: ** `${nextStop?.name ?: ""}`")
        }
    }

    private fun getVehicleInfo(fleet: String): Service? {
        val info = stagecoach.getVehicle(fleet)
        return info.services.firstOrNull()
    }

}