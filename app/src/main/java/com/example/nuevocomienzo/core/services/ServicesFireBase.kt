package com.example.nuevocomienzo.core.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nuevocomienzo.MainActivity
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

public class ServicesFireBase : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        initializeService()
    }

    private fun initializeService() {

        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Token de FCM obtenido: $token")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el token de FCM", it)
            }
        getFirebaseInstallationId()
    }

    private fun getFirebaseInstallationId() {
        FirebaseInstallations.getInstance().id
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
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("Mensaje recibido")

        if (remoteMessage.data["triggerIAM"] == "true") {
            FirebaseInAppMessaging.getInstance().triggerEvent("activo_add")
            println("Evento 'campaña' disparado desde la notificación")
        }

        remoteMessage.notification?.let {
            showNotification(it.title ?: "Título predeterminado", it.body ?: "Mensaje vacío")
        }
    }

    fun triggerInAppEvent(eventName: String) {
        Log.d(TAG, "Intentando activar evento In-App Messaging: $eventName")
        try {
            FirebaseInAppMessaging.getInstance().triggerEvent(eventName)
            Log.d(TAG, "Evento activado correctamente: $eventName")
        } catch (e: Exception) {
            Log.e(TAG, "Error al activar evento: ${e.message}", e)
        }
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

            FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
            FirebaseInstallations.getInstance().id
                .addOnSuccessListener { id ->
                    Log.d(TAG, "ID de instalación de Firebase: $id")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error al obtener el ID de instalación", it)
                }
        }


        fun subscribeToTopic(topic: String, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnSuccessListener {
                    Log.d(TAG, "Suscripción exitosa al topic: $topic")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error al suscribirse al topic: $topic", exception)
                    onFailure(exception.message ?: "Error desconocido al suscribirse")
                }
        }

        fun unsubscribeFromTopic(topic: String, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnSuccessListener {
                    Log.d(TAG, "Desuscripción exitosa del topic: $topic")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error al desuscribirse del topic: $topic", exception)
                    onFailure(exception.message ?: "Error desconocido al desuscribirse")
                }
        }
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(title: String, message: String) {
        val channelId = "mi_canal_notificacion"
        val notificationId = 1

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal de Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal de notificaciones para Firebase"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}