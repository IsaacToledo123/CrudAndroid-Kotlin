package com.example.nuevocomienzo.login.data.repository

import android.util.Log
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    suspend fun getFirebaseToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener el token FCM", e)
            null
        }
    }

    suspend fun getFirebaseInstallationId(): String? {
        return try {
            FirebaseInstallations.getInstance().id.await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener el ID de instalación", e)
            null
        }
    }

    companion object {
        private const val TAG = "FirebaseRepository"
    }
}