package com.example.nuevocomienzo

import android.app.Application
import android.util.Log
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks
import com.google.firebase.inappmessaging.model.InAppMessage

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
        println("se activo")
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent { inAppMessage, callbacks ->
            Log.d(
                "InAppMessaging",
                "Mensaje recibido: ${inAppMessage.campaignMetadata?.campaignName}"
            )

            callbacks.messageDismissed(FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.AUTO)
        }
    }

}