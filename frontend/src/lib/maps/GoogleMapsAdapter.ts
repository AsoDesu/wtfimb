import type { Service } from "$lib/stagecoachTypes";
import { MapAdapter } from "./MapAdapter";

export class GoogleMapsAdapter extends MapAdapter {
  map: google.maps.Map;
  marker: any;

  constructor(
    container: HTMLDivElement,
    mapOptions: any,
    startPos: number[],
    scale: number
  ) {
    super();

    this.map = new google.maps.Map(container, {
      zoom: scale,
      center: this.adaptLatLng(startPos),
      mapId: mapOptions.mapId,
    });
  }

  async setupMarker(pos: number[], markerElement: HTMLElement) {
    //@ts-ignore
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    this.marker = new AdvancedMarkerElement({
      position: this.adaptLatLng(pos),
      map: this.map,
      content: markerElement,
    });
  }

  setMarkerPosition(pos: number[]) {
    this.marker.position = this.adaptLatLng(pos)
  }

  adaptLatLng([lat, lng]: number[]): google.maps.LatLng {
    return new google.maps.LatLng(lat, lng);
  }
}
