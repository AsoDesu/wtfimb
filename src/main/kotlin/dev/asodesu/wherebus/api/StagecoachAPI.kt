package dev.asodesu.wherebus.api

import dev.asodesu.wherebus.stagecoach.StagecoachService
import dev.asodesu.wherebus.stagecoach.schema.Service
import dev.asodesu.wherebus.stagecoach.schema.ServiceTimetableResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/stagecoach/")
class StagecoachAPI(val stagecoach: StagecoachService) {

    @GetMapping("/vehicle/{ref}")
    fun getVehicleInfo(@PathVariable ref: String): Service {
        val fleetNo = if (ref.contains("-")) ref.split("-")[1] else ref

        val vehicleInfo = stagecoach.getVehicle(fleetNo)
        if (vehicleInfo.services.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle was not found!")
        return vehicleInfo.services.first()
    }

    @GetMapping("/vehicles", produces = ["application/json"])
    fun getVehicles(@RequestParam params: Map<String,String>): String {
        return stagecoach.getVehicles(params)
    }

    @GetMapping("/timetable/{serviceId}")
    fun vehicleTimetable(
        @PathVariable serviceId: String,
        @RequestParam startingStop: String,
        @RequestParam departureTime: Long
    ): ServiceTimetableResponse {
        val timetable = stagecoach.serviceTimetable(serviceId, departureTime, startingStop)
        if (timetable.timetableRows == null)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Timetable was not found!")
        return timetable
    }

}