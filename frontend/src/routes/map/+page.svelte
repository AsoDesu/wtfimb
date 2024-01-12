<script lang="ts">
  import { browser } from "$app/environment";
  import { page } from "$app/stores";
  import type { MapAdapter } from "$lib/maps/MapAdapter";
  import { fetchMapInfo, fetchVehicleInfo } from "$lib/stagecoach";
  import type { Service } from "$lib/stagecoachTypes";
  import { busService } from "$lib/stores";
  import { lerp } from "$lib/utils";
  import GoogleMaps from '$lib/components/GoogleMaps.svelte'
  import LeaftletMaps from '$lib/components/LeafletMaps.svelte'

  let mapInfo: any = null
  let map: MapAdapter;
  let bus = $page.url.searchParams.get("bus") as string;

  async function fetchBusInfo() {
    fetchMapInfo().then(data => mapInfo = data)
    fetchVehicleInfo(bus).then(data => $busService = data)
  }
  fetchBusInfo()

  async function ready(event: CustomEvent) {
    let adapter = event.detail as MapAdapter;

    let div = document.createElement("div");
    div.innerHTML =
      '<div style="position:relative; height: 48px;width: 38px;"><img src="https://www.stagecoachbus.com/assets/img/bus_position_icon.svg" style="position:relative; left: 0;  top: 0;"/><div style="position:absolute; bottom: 0; left: 0; text-align: center; width:100%; padding-bottom: 2px;"><span class="text-white" style="font-size: x-small;">' +
      $busService.serviceNumber +
      "</span></div></div>";

    let markerPos = getServiceLatLng($busService)
    adapter.setupMarker(markerPos, div);
  }

  let interval = setInterval(updateMarker, 5000);
  async function updateMarker() {
    if (map == null) return;
    let newServiceData = await fetchVehicleInfo(bus);
    let lastPos = getServiceLatLng($busService);
    let newPos = getServiceLatLng(newServiceData);

    if (!document.hidden) {
      // interpolation time!
      let interpolationTime = 750;
      let frame = 0;
      let interval: number;
      interval = setInterval(() => {
        if (frame >= interpolationTime) {
          clearInterval(interval);
          return;
        }
        let t = frame / interpolationTime;
        let lng = lerp(lastPos[1], newPos[1], t);
        let lat = lerp(lastPos[0], newPos[0], t);
        map.setMarkerPosition([lat, lng]);

        frame += 10;
      }, 10);
    } else {
      map.setMarkerPosition(newPos);
    }

    $busService = newServiceData;
  }

  function getServiceLatLng(service: Service): number[] {
    let lat = +service.latitude;
    let lng = +service.longitude;
    return [lat, lng];
  }
</script>

<div class="w-screen h-screen absolute">
  {#if (mapInfo == null || $busService == null)}
    Loading map...
  {:else if (mapInfo.type == "google")}
    <GoogleMaps on:ready={ready} bind:mapAdapter={map} mapInfo={mapInfo} center={getServiceLatLng($busService)} />
  {:else if (mapInfo.type == "osm")}
    <LeaftletMaps on:ready={ready} bind:mapAdapter={map} center={getServiceLatLng($busService)} />
  {/if}
</div>
