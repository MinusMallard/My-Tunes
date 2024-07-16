package com.example.mytunes

import android.app.Application
import com.example.mytunes.data.AppContainer
import com.example.mytunes.data.DefaultAppContainer

class MyTunesApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}