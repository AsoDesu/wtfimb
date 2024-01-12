import type { Service } from "./stagecoachTypes"

const BASE_URL = ""

export function fetchVehicleInfo(fleetNo: string): Promise<Service> {
    return fetch(`${BASE_URL}/api/stagecoach/vehicle/${fleetNo}`)
        .then(res => res.json())
}

export function fetchMapInfo(): Promise<any> {
    return fetch(`${BASE_URL}/api/maps/info`)
        .then(res => res.json())
}