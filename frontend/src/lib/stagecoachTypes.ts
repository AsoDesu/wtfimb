export interface Service {
  fleetNumber: string;
  updateTime: string;
  operatingCompany: string;
  serviceNumber: string;
  direction: string;
  serviceId: string;
  shortOpco: string;
  serviceDescription: string;
  cancelled: string;
  latitude: string;
  longitude: string;
  heading: string;
  calculatedHeading: string;
  destinationDisplay: string;
  originStopReference: string;
  originStopName: string;
  nextStopReference: string;
  nextStopName: string;
  finalStopReference: string;
  finalStopName: string;
  aimedOriginStopDepartureTime: string;
  expectedOriginStopDepartureTime: string;
  aimedNextStopArrivalTime: string;
  expectedNextStopArrivalTime: string;
  aimedNextStopDepartureTime: string;
  expectedNextStopDepartureTime: string;
  aimedFinalStopArrivalTime: string;
  expectedFinalStopArrivalTime: string;
  kmlUrl: string;
  tripId: string;
  previousStopOnRoute: string;
  currentStopOnRoute: string;
  nextStopOnRoute: string;
  isJourneyCompletedHeuristic: string;
  metresFromRoute: string;
  snapLongitude: string;
  snapLatitude: string;
  secondsOffRoute: string;
  rag: string;
}