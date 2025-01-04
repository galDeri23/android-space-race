package com.example.hw1_obstacleracinggame

import android.app.Application
import com.example.hw1_obstacleracinggame.utilites.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)

    }
}