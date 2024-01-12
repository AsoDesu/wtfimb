<script>
// @ts-nocheck
  import { LeafletMapsAdapter } from "$lib/maps/LeaftletMapsAdapter";
  import { createEventDispatcher, onMount } from "svelte";
  const dispatch = createEventDispatcher();

  /**
   * @type {HTMLDivElement}
   */
  let container;
  /**
   * @type {MapAdapter}
   */
  export let mapAdapter = null;

  /**
   * @type {number[]}
   */
   export let center;

  const ready = () => {
    mapAdapter = new LeafletMapsAdapter(container, center, 17)
    dispatch("ready", mapAdapter);
  };
</script>

<svelte:head>
  <link
    rel="stylesheet"
    href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
    integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
    crossorigin=""
  />
  <script
    src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
    integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
    on:load={ready}
    crossorigin=""
  ></script>
</svelte:head>

<style>
    .full-screen {
      width: 100vw;
      height: 100vh;
    }
  </style>

<div class="full-screen" bind:this={container}></div>
