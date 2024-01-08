<script lang="ts">
    import { busService } from "$lib/stores";
</script>

<style>
    .translate {
        transform: translate(-50%);
    }
</style>

<div class="bg-white rounded-lg shadow-lg absolute w-[98%] h-[10%] z-20 left-[50%] right-[50%] bottom-[10px] translate">
    <div class="flex w-full h-full items-center justify-center flex-col text-black">
        <h2 class="font-bold size">{$busService?.serviceNumber} | {$busService?.serviceDescription}</h2>
        <h2 class="font-normal">Next Stop: {$busService?.nextStopOnRoute}</h2>
    </div>
</div>