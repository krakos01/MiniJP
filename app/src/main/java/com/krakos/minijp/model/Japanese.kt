package com.krakos.minijp.model

import kotlinx.serialization.Serializable

@Serializable
data class Japanese(
    val word: String?,
    val reading: String?
)
