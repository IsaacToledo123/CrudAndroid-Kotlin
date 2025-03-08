package com.example.nuevocomienzo

import android.app.Application
import com.example.nuevocomienzo.core.services.FirebaseMessagingServices

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseMessagingServices.initialize(this)
        FirebaseMessagingServices.setInAppMessagingEnabled(true)
    }

}