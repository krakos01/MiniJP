package com.krakos.minijp.network

import com.krakos.minijp.model.Items
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("words")
    suspend fun getAllWords(@Query("keyword") query: String): Items
}