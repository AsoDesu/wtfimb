package dev.asodesu.wherebus.stagecoach.schema

import com.fasterxml.jackson.annotation.JsonProperty

class SearchQuery(
    @JsonProperty("SearchText")
    val searchText: String,
    @JsonProperty("SearchAlgorithm")
    val searchAlgorithm: String,
    @JsonProperty("MaxNumberOfResults")
    val maxNumberOfResults: Long,
    @JsonProperty("RequestId")
    val requestId: String,
    @JsonProperty("Categories")
    val categories: Categories,
)

class Categories(
    @JsonProperty("Category")
    val category: List<String>,
)
