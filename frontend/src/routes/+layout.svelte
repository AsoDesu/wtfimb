<script>
  import "tailwindcss/tailwind.css";
  import "../app.css";

  import { browser } from '$app/environment'; 
</script>

{#if (browser)}
  <slot />
{/if}
