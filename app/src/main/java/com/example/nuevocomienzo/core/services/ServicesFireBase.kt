package com.example.nuevocomienzo.core.services

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.installations.FirebaseInstallations

object FirebaseMessagingServices {

    fun initialize(context: Context) {
        FirebaseApp.initializeApp(context) ?: FirebaseApp.getInstance()

    }
    private val firebaseApp: FirebaseApp by lazy {
        FirebaseApp.getInstance()
    }

    private val firebaseMessaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }

    private val firebaseInAppMessaging: FirebaseInAppMessaging by lazy {
        FirebaseInAppMessaging.getInstance()
    }

    fun getFCMToken(onTokenReceived: (String) -> Unit) {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                onTokenReceived(token)
            } else {
                println("Error al obtener el token: ${task.exception}")
            }
        }
    }

    fun getIAPIId(onIdReceived: (String) -> Unit) {
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = task.result
                onIdReceived(id)
            } else {
                println("Error al obtener el ID de instalación: ${task.exception}")
            }
        }
    }

    fun subscribeToTopic(topic: String) {
        firebaseMessaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Suscrito al tema: $topic")
                } else {
                    println("Error al suscribirse al tema: ${task.exception}")
                }
            }
    }

    fun unsubscribeFromTopic(topic: String) {
        firebaseMessaging.unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Desuscrito del tema: $topic")
                } else {
                    println("Error al desuscribirse del tema: ${task.exception}")
                }
            }
    }

    fun setInAppMessagingEnabled(enabled: Boolean) {
        firebaseInAppMessaging.setMessagesSuppressed(!enabled)
    }

    fun setMessageHandler(onMessageReceived: (String) -> Unit) {
        firebaseMessaging.token.addOnSuccessListener { token ->
            println("Token de FCM: $token")
        }

        firebaseMessaging.isAutoInitEnabled = true
    }
}