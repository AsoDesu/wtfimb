<script lang="ts">
  import { GoogleMapsAdapter } from "$lib/maps/GoogleMapsAdapter";
  import type { MapAdapter } from "$lib/maps/MapAdapter";
  import { createEventDispatcher, onMount } from "svelte";

  const dispatch = createEventDispatcher();

  // @ts-ignore
  export let mapAdapter: MapAdapter = null;

  let container: HTMLDivElement;
  export let mapInfo: any;
  export let zoom = 17;
  export let center: number[];

  //@ts-ignore
  window.initMap = () => {
    mapAdapter = new GoogleMapsAdapter(container, mapInfo, center, zoom);
    dispatch("ready", mapAdapter);
  };
</script>

<svelte:head>
  <script
    defer
    async
    src="https://maps.googleapis.com/maps/api/js?key={mapInfo.apiKey}&callback=initMap"
  >
  </script>
</svelte:head>

<div class="full-screen" bind:this={container}></div>

<style>
  .full-screen {
    width: 100vw;
    height: 100vh;
  }
</style>
