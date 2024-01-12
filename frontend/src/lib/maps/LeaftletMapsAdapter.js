// @ts-nocheck
import { MapAdapter } from "./MapAdapter";

export class LeafletMapsAdapter extends MapAdapter {
  map
  marker

  /**
   * @param {any} container
   * @param {any} startPos
   * @param {number} scale
   */
  constructor(container, startPos, scale) {
    super();

    this.map = L.map(container).setView(startPos, scale);
    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution:
        '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(this.map);
  }

  setupMarker(pos, markerElement) {
    let marker = L.divIcon({ html: markerElement })
    this.marker = L.marker(pos, { icon: marker }).addTo(this.map);
  }

  setMarkerPosition(pos) {
    let newPos = this.adaptLatLng(pos)
    this.marker.setLatLng(newPos)
  }

  adaptLatLng(pos) {
    return L.latLng(pos[0], pos[1])
  }
}