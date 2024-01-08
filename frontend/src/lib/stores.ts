import { writable } from "svelte/store";
import type { Service } from "./stagecoachTypes";

export let busService = writable<Service>()