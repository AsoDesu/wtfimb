package dev.asodesu.wherebus.stagecoach

import dev.asodesu.wherebus.stagecoach.schema.*
import java.awt.Color

interface StagecoachService {
    fun getVehicle(fleetNo: String): VehiclesResponse
    fun getVehicles(params: Map<String, String>): String
    fun searchStops(query: String): SearchResults
    fun monitorStops(stop: String, lookAhead: Int = 720, serviceFilter: List<String>): StopMonitorResponse
    fun stopEvents(stop: String, maxEvents: Int = 60): StopEvents
    fun serviceTimetable(service: Service): ServiceTimetableResponse
    fun serviceTimetable(serviceId: String, departureTime: Long, originStop: String): ServiceTimetableResponse

    fun getStagecoachColor(): Color
}