package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.Values
import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.schema.Service
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import java.time.Instant
import java.time.ZoneId

class SubscriptionQuery(private val subscription: Subscription) {
    private val stagecoach by subscription::stagecoach
    private val service by subscription::service
    private var timetable by subscription::timetable
    private var assignedVehicleRef by subscription::assignedVehicleRef

    fun query(): MessageCreateData {
        val event = subscription.service.getEvent(stagecoach)
        val date = Instant.ofEpochSecond(service.epochSeconds).atZone(ZoneId.systemDefault())

        val monitor = stagecoach.monitorStops(service.stopId, serviceFilter = listOf(service.number))
        val call = subscription.getCallFromMonitor(monitor)
        // get the assigned vehicle, or the one we stored, just in case
        // stagecoach forgot about our trip
        val vehicle = call?.vehicleRef ?: assignedVehicleRef
        if (!vehicle.isNullOrBlank()) {
            assignedVehicleRef = vehicle
        }

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

        if (!vehicle.isNullOrBlank() && Values.baseUrl.isNotBlank()) {
            val (_, fleetNo) = vehicle.split("-")
            val button = Button.link("${Values.baseUrl}/map?bus=$fleetNo", "View on map")
                .withEmoji(Emoji.fromUnicode("U+1F30D"))
            message.addActionRow(button)
        }

        return message.build()
    }

    private fun getVehicleDescription(vehicleRef: String?): String {
        val noBusDesc = ":bus: **No assigned bus, no additional data**"
        if (vehicleRef.isNullOrBlank()) return noBusDesc

        val (_, fleetNo) = vehicleRef.split("-")
        val vehicleInfo = stagecoach.getVehicle(fleetNo).services.firstOrNull() ?: return noBusDesc
        val desc = subscription.getVehicleDescription(vehicleInfo)
        return "\n" + desc
    }
}