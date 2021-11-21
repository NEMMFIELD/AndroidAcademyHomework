package com.example.androidacademyhomework

import android.app.Application
import com.example.androidacademyhomework.di.AppModule


class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        container = AppModule(applicationContext)
    }
    companion object
    {
        lateinit var container:AppModule
    }
}

