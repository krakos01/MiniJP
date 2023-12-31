package com.krakos.minijp.data

import com.krakos.minijp.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val dataRepository: DataRepository
}

class DefaultAppContainer: AppContainer {
    private val baseUrl = "https://jisho.org/api/v1/search/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val dataRepository: DataRepository by lazy {
        NetworkRepository(retrofitService)
    }
}