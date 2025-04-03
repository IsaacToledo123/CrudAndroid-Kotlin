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
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks
import com.google.firebase.inappmessaging.model.InAppMessage
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
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
        setupInAppMessagingDisplay()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Token de FCM obtenido: $token")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el token de FCM", it)
            }
        getFirebaseInstallationId()
    }

    private fun setupInAppMessagingDisplay() {
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent { inAppMessage, callbacks ->
            try {
                Log.d(TAG, "Mensaje in-app recibido: ${inAppMessage.campaignMetadata?.campaignName}")

                com.google.firebase.inappmessaging.display.FirebaseInAppMessagingDisplay.getInstance().displayMessage(inAppMessage, callbacks)

                // Si prefieres implementación personalizada, descomenta esto y adapta según tus necesidades:
                /*
                when (inAppMessage.messageType) {
                    InAppMessage.MessageType.MODAL -> showCustomModal(inAppMessage, callbacks)
                    InAppMessage.MessageType.BANNER -> showCustomBanner(inAppMessage, callbacks)
                    InAppMessage.MessageType.CARD -> showCustomCard(inAppMessage, callbacks)
                    InAppMessage.MessageType.IMAGE_ONLY -> showCustomImage(inAppMessage, callbacks)
                    else -> callbacks.displayErrorEncountered(
                        FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.UNSPECIFIED_RENDER_ERROR
                    )
                }
                */
            } catch (e: Exception) {
                Log.e(TAG, "Error al mostrar mensaje in-app", e)
                callbacks.displayErrorEncountered(
                    FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.UNSPECIFIED_RENDER_ERROR
                )
            }
        }
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

        logFCMMessageDetails(remoteMessage)

        val data = remoteMessage.data

        // Activar eventos de in-app messaging si se indica
        if (data["triggerIAM"] == "true") {
            val eventName = data["eventName"] ?: "activo_add"
            triggerInAppEvent(eventName)
        }

        // Mostrar notificación
        remoteMessage.notification?.let {
            showNotification(
                it.title ?: "Título predeterminado",
                it.body ?: "Mensaje vacío",
                data
            )
        }
    }

    // Método para registrar todos los detalles del mensaje FCM
    private fun logFCMMessageDetails(remoteMessage: RemoteMessage) {
        Log.d(TAG, "=== Mensaje FCM Recibido ===")
        Log.d(TAG, "Mensaje ID: ${remoteMessage.messageId}")
        Log.d(TAG, "Remitente: ${remoteMessage.from}")
        Log.d(TAG, "Tipo: ${remoteMessage.messageType}")
        Log.d(TAG, "TTL: ${remoteMessage.ttl}")

        // Datos
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Datos del mensaje:")
            for ((key, value) in remoteMessage.data) {
                Log.d(TAG, "   $key: $value")
            }
        }

        // Notificación
        remoteMessage.notification?.let {
            Log.d(TAG, "Notificación:")
            Log.d(TAG, "   Título: ${it.title}")
            Log.d(TAG, "   Cuerpo: ${it.body}")
            Log.d(TAG, "   Canal: ${it.channelId}")
            Log.d(TAG, "   Sonido: ${it.sound}")
            Log.d(TAG, "   Icono: ${it.icon}")
            Log.d(TAG, "   Color: ${it.color}")
        }

        Log.d(TAG, "==========================")
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
    private fun showNotification(title: String, message: String, data: Map<String, String> = emptyMap()) {
        val channelId = "mi_canal_notificacion"
        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            for ((key, value) in data) {
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificación para Android 8.0+
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

        if (message.length > 50) {
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}