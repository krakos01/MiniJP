package com.krakos.minijp.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// In API it's called "senses"
@Serializable
data class Translation(
    @SerializedName("english_definitions") val englishDefinitions: List<String>,
    @SerializedName("parts_of_speech") val partsOfSpeech: List<String>,
    val tags: List<String>
)