<script lang="ts">
  import { onMount } from "svelte";

  let container: HTMLDivElement;
  // @ts-ignore
  export let map: google.maps.Map = undefined;
  export let zoom = 17;
  export let center: google.maps.LatLng;

  onMount(async () => {
    map = new google.maps.Map(container, {
      zoom,
      center,
      mapId: "18735ba3295594fd"
    });
  });
</script>

<style>
    .full-screen {
      width: 100vw;
      height: 100vh;
    }
  </style>

<div class="full-screen" bind:this={container}></div>
