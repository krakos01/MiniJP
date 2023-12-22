package com.krakos.minijp

import android.app.Application
import com.krakos.minijp.data.AppContainer
import com.krakos.minijp.data.DefaultAppContainer

class MiniJpApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}