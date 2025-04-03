package com.example.nuevocomienzo

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.example.nuevocomienzo.core.services.ServicesFireBase
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.display.FirebaseInAppMessagingDisplay
import com.google.firebase.inappmessaging.model.InAppMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable data collection once
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true

        Log.d(TAG, "Firebase In-App Messaging inicializado")

        // Utilizar la implementación predeterminada para mostrar mensajes
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent { inAppMessage, callbacks ->
            try {
                Log.d(TAG, "Mensaje in-app recibido: ${inAppMessage.campaignMetadata?.campaignName}")

                // Usar la implementación de display predeterminada en lugar de descartarlo
                FirebaseInAppMessagingDisplay.getInstance().displayMessage(inAppMessage, callbacks)

                // Registrar información detallada del mensaje para depuración
                logInAppMessageDetails(inAppMessage)

            } catch (e: Exception) {
                Log.e(TAG, "Error al procesar mensaje in-app", e)

            }
        }

        // Obtener token FCM
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Token de FCM obtenido: $token")
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Error al obtener el token de FCM", error)
            }

        // Inicializar el servicio de Firebase
        ServicesFireBase.initializeFirebaseMessaging()
    }

    // Método para registrar detalles completos del mensaje in-app
    private fun logInAppMessageDetails(message: InAppMessage) {
        Log.d(TAG, "=== Detalles del Mensaje In-App ===")
        Log.d(TAG, "Tipo: ${message.messageType}")
        message.campaignMetadata?.let {
            Log.d(TAG, "Campaña: ${it.campaignName}")
            Log.d(TAG, "ID Campaña: ${it.campaignId}")
        }
        message.data?.let {
            Log.d(TAG, "Datos: $it")
        }
        Log.d(TAG, "==============================")
    }
}