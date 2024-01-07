package dev.asodesu.wherebus.subscription

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.parseSeconds
import dev.asodesu.wherebus.stagecoach.schema.Direction
import dev.asodesu.wherebus.stagecoach.schema.Event

data class ServiceDetail(val number: String, val direction: Direction, val epochSeconds: Long, val stopId: String) {
    var event: Event? = null

    fun getEvent(stagecoach: StagecoachService): Event {
        if (event != null) return event!!
        val stopEvents = stagecoach.stopEvents(stopId)
        val events = stopEvents.events!!.event
        return events.first { event ->
            val service = event.trip.service
            service.serviceNumber == number
                    && service.direction.equals(direction.name, ignoreCase = true)
                    && parseSeconds(event.scheduledArrivalTime.value) == epochSeconds
        }.also { event = it }
    }


}