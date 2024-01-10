import type { Service } from "./stagecoachTypes"

const BASE_URL = "/api/stagecoach"

export function fetchVehicleInfo(fleetNo: string): Promise<Service> {
    return fetch(`${BASE_URL}/vehicle/${fleetNo}`)
        .then(res => res.json())
}