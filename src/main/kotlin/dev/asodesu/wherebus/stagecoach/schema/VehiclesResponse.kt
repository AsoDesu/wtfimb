package dev.asodesu.wherebus.stagecoach.schema

class VehiclesResponse(
    val services: List<Service>
)

data class Service(
    val fleetNumber: String,
    val updateTime: String,
    val operatingCompany: String,
    val serviceNumber: String,
    val direction: String,
    val serviceId: String,
    val shortOpco: String,
    val serviceDescription: String,
    val cancelled: String,
    val latitude: String,
    val longitude: String,
    val heading: String,
    val calculatedHeading: String,
    val destinationDisplay: String,
    val originStopReference: String,
    val originStopName: String,
    val nextStopReference: String,
    val nextStopName: String,
    val finalStopReference: String,
    val finalStopName: String,
    val aimedOriginStopDepartureTime: String,
    val expectedOriginStopDepartureTime: String,
    val aimedNextStopArrivalTime: String,
    val expectedNextStopArrivalTime: String,
    val aimedNextStopDepartureTime: String,
    val expectedNextStopDepartureTime: String,
    val aimedFinalStopArrivalTime: String,
    val expectedFinalStopArrivalTime: String,
    val kmlUrl: String,
    val tripId: String,
    val previousStopOnRoute: String,
    val currentStopOnRoute: String,
    val nextStopOnRoute: String,
    val isJourneyCompletedHeuristic: String,
    val metresFromRoute: String,
    val snapLongitude: String,
    val snapLatitude: String,
    val secondsOffRoute: String,
    val rag: String?,
)