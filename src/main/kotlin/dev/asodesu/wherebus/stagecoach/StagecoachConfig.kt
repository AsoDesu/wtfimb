package dev.asodesu.wherebus.stagecoach

import com.fasterxml.jackson.annotation.JsonProperty

class StagecoachConfig(
    @JsonProperty("AVL")
    val avl: UrlConfig,
    @JsonProperty("TIS")
    val tis: UrlConfig
) {

    class UrlConfig(
        @JsonProperty("URL")
        val url: String
    ) {
        @JsonProperty("HEADERS")
        val headers: Map<String, String>? = null
    }

}