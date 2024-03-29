package com.krakos.minijp.data

import com.krakos.minijp.model.Items
import com.krakos.minijp.network.ApiService

interface DataRepository {
    suspend fun allWordsData(query: String): Items
}

class NetworkRepository(private val apiService: ApiService): DataRepository {
    override suspend fun allWordsData(query: String): Items = apiService.getAllWords(query)
}
