package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.stagecoach.getTime
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Service
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import java.awt.Color
import java.time.Instant
import java.time.ZoneId
import kotlin.math.abs

class RealTimeUpdater(private val subscription: Subscription, val channel: MessageChannel) {
    private val service by subscription::service
    private val stagecoach by subscription::stagecoach
    private var assignedVehicleRef by subscription::assignedVehicleRef

    private val timeNotifyDeltaSeconds = 120
    var lastExpectedTime: Long = -1
    var lastVehicleInfo: Service? = null

    // Embed Colors
    val expectedTimeChangeColour = Color(255, 0, 0)
    val vehicleAssignedColour = Color(102, 0, 255)
    val busRouteStartedColour = Color(0, 255, 0)
    val nextStopColour = Color(255, 255, 0)

    fun runUpdates(): Updates {
        val updates = Updates(subscription)

        val monitor = stagecoach.monitorStops(service.stopId, serviceFilter = listOf(service.number))
        val call = subscription.getCallFromMonitor(monitor)

        val currentExpectedIsoTime = call?.expectedArrivalTime
        if (!currentExpectedIsoTime.isNullOrBlank()) {
            val currentExpectedTime = parseSeconds(currentExpectedIsoTime)
            val notifyDistance = abs(lastExpectedTime - currentExpectedTime)
            if (notifyDistance >= timeNotifyDeltaSeconds || lastExpectedTime == -1L) {
                lastExpectedTime = currentExpectedTime
                val time = getTime(Instant.ofEpochSecond(currentExpectedTime).atZone(ZoneId.systemDefault()))
                updates.addUpdate(
                    EmbedBuilder()
                        .setTitle("⏰ Expected Time Changed")
                        .setDescription("Your bus is now expected to arrive at **${time}** (<t:$currentExpectedTime:R>)")
                        .setColor(expectedTimeChangeColour)
                        .build()
                )
            }
        }

        // get the assigned vehicle, or the one we stored, just in case
        // stagecoach forgot about our trip
        val vehicle = call?.vehicleRef ?: assignedVehicleRef
        if (!vehicle.isNullOrBlank()) {
            assignedVehicleRef = vehicle
        }
        if (assignedVehicleRef.isNullOrBlank()) return updates

        val (_, fleetNo) = assignedVehicleRef!!.split("-")
        val vehicleInfo = stagecoach.getVehicle(fleetNo).services.firstOrNull() ?: return updates
        val isSameService = vehicleInfo.serviceNumber == service.number
        val isSameDirection = vehicleInfo.direction.equals(service.direction.name, true)
        val isOnRoute = isSameService && isSameDirection

        if (lastVehicleInfo == null) {
            val description = subscription.getVehicleDescription(vehicleInfo)
            updates.addUpdate(
                EmbedBuilder()
                    .setTitle("\uD83D\uDCD6 Bus information available!")
                    .setDescription(description)
                    .setColor(vehicleAssignedColour)
                    .build()
            )
        } else if (lastVehicleInfo?.serviceId != vehicleInfo.serviceId) {
            val routeDescription = "${vehicleInfo.serviceNumber} | ${vehicleInfo.serviceDescription}"
            if (isSameService && isSameDirection) {
                updates.addUpdate(
                    EmbedBuilder()
                        .setTitle("� Bus route started")
                        .setDescription("Your bus has started route **${routeDescription}**")
                        .setColor(busRouteStartedColour)
                        .build()
                )
            }
        }

        val timetable = subscription.getTimetable(vehicleInfo)
        if (isOnRoute && lastVehicleInfo?.nextStopOnRoute != vehicleInfo.nextStopOnRoute) {
            if (vehicleInfo.nextStopOnRoute == service.stopId) {
                val stopInfo = subscription.getStopFromTimetable(timetable, vehicleInfo.nextStopOnRoute)
                updates.addUpdate(
                    EmbedBuilder()
                        .setTitle("\uD83D\uDE8F You're stop is next!")
                        .setDescription("Your bus will be at **${stopInfo?.name ?: "Unknown?"}** next!")
                        .setColor(nextStopColour)
                        .build()
                )
            }
        }

        updates.vehicleRef = assignedVehicleRef
        lastVehicleInfo = vehicleInfo

        return updates
    }

}