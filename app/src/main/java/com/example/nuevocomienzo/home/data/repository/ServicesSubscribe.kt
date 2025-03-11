package com.example.nuevocomienzo.home.data.repository

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class ServicesSubscribe {
    suspend fun subscribe(topic: String, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(topic).await()
            Log.d(TAG, "Suscripción exitosa al topic: $topic")
            onSuccess()
        } catch (e: Exception) {
            Log.e(TAG, "Error al suscribirse al topic: $topic", e)
            onFailure(e.localizedMessage ?: "Error desconocido al suscribirse")
        }
    }

    suspend fun desubscribe(topic: String, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).await()
            Log.d(TAG, "Desuscripción exitosa del topic: $topic")
            onSuccess()
        } catch (e: Exception) {
            Log.e(TAG, "Error al desuscribirse del topic: $topic", e)
            onFailure(e.localizedMessage ?: "Error desconocido al desuscribirse")
        }
    }

    companion object {
        private const val TAG = "ServicesSubscribe"
    }
}
