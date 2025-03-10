package com.example.nuevocomienzo.core.services

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ServicesFireBase : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        initializeService()
    }

    private fun initializeService() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Token de FCM obtenido: $token")
                sendRegistrationToServer(token)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el token de FCM", it)
            }

        getFirebaseInstallationId()
    }

    private fun getFirebaseInstallationId(): Task<String> {
        return FirebaseInstallations.getInstance().id
            .addOnSuccessListener { id ->
                Log.d(TAG, "ID de instalación de Firebase: $id")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el ID de instalación", it)
            }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // Aquí puedes enviar el token a tu backend si es necesario
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.containsKey("triggerIAM")) {
            val triggerValue = remoteMessage.data["triggerIAM"]
            if ("true" == triggerValue) {
                triggerInAppEvent("mensajeEspecial")
            }
        }
    }

    fun triggerInAppEvent(eventName: String) {
        Log.d(TAG, "Activando evento In-App Messaging: $eventName")
        FirebaseInAppMessaging.getInstance().triggerEvent(eventName)
    }

    companion object {
        private const val TAG = "ServicesFireBase"

        fun initializeFirebaseMessaging() {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    Log.d(TAG, "Token de FCM obtenido: $token")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error al obtener el token de FCM", it)
                }

            FirebaseInstallations.getInstance().id
                .addOnSuccessListener { id ->
                    Log.d(TAG, "ID de instalación de Firebase: $id")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error al obtener el ID de instalación", it)
                }
        }
    }
}