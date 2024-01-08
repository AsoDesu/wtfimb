<script lang="ts">
  import { browser } from "$app/environment";
  import { page } from "$app/stores";
  import Map from "$lib/components/Map.svelte";
  import { fetchVehicleInfo } from "$lib/stagecoach";
  import type { Service } from "$lib/stagecoachTypes";
  import { busService } from "$lib/stores";
  import { lerp } from "$lib/utils";
  import RouteDisplay from "./components/RouteDisplay.svelte";

  let map: google.maps.Map;
  let marker: any;

  let bus = $page.url.searchParams.get("bus") as string;

  let ready = false;
  let centre: google.maps.LatLng;
  //@ts-ignore
  window.initMap = async () => {
    if (!browser) return
    let service = await fetchVehicleInfo(bus);
    $busService = service;
    let lat = +service.latitude;
    let lng = +service.longitude;
    centre = new google.maps.LatLng(lat, lng);
    ready = true;
  };

  $: ready && createMarker(map);
  let interval = setInterval(updateMarker, 5000)

  async function createMarker(bindMap: google.maps.Map) {
    //@ts-ignore
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    let div = document.createElement("div")
    div.innerHTML = '<div style="position:relative; height: 48px;width: 38px;"><img src="https://www.stagecoachbus.com/assets/img/bus_position_icon.svg" style="position:relative; left: 0;  top: 0;"/><div style="position:absolute; bottom: 0; left: 0; text-align: center; width:100%; padding-bottom: 2px;"><span class="text-white" style="font-size: x-small;">' + $busService.serviceNumber + '</span></div></div>'
    marker = new AdvancedMarkerElement({
      position: getServiceLatLng($busService),
      map: bindMap,
      content: div,
    });
  }

  async function updateMarker() {
    if (marker == null || !ready) return
    let newServiceData = await fetchVehicleInfo(bus);
    let lastPos = getServiceLatLng($busService)
    let newPos = getServiceLatLng(newServiceData)

    console.log({ lat: lastPos.lat(), lng: lastPos.lng() }, { lat: newPos.lat(), lng: newPos.lng() })

    if (lastPos == null || isNaN(lastPos.lng()) || isNaN(lastPos.lat())) {
      marker.position = newPos
      return
    }

    // interpolation time!
    let interpolationTime = 750
    let frame = 0
    let interval: number
    interval = setInterval(() => {
      if (frame >= interpolationTime) {
        clearInterval(interval)
        return
      }
      let t = frame / interpolationTime
      let lng = lerp(lastPos.lng(), newPos.lng(), t)
      let lat = lerp(lastPos.lat(), newPos.lat(), t)
      marker.position = new google.maps.LatLng(lat, lng)

      frame += 10
    }, 10)

    $busService = newServiceData
  }

  function getServiceLatLng(service: Service): google.maps.LatLng {
    let lat = +service.latitude;
    let lng = +service.longitude;
    return new google.maps.LatLng(lat, lng);
  }
</script>

<svelte:head>
  <script
    defer
    async
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCY8515BQmgpPTeKBOj109D2X0ZHHLxBM0&callback=initMap"
  >
  </script>
</svelte:head>

{#if ready}
  <div class="w-screen h-screen absolute">
    <Map center={centre} bind:map />
  </div>
{/if}
