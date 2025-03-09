package com.example.nuevocomienzo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().containsKey("triggerIAM")) {
            String triggerValue = remoteMessage.getData().get("triggerIAM");
            if ("true".equals(triggerValue)) {
                Log.d(TAG, "Activando In-App Messaging...");
                FirebaseInAppMessaging.getInstance().triggerEvent("mensajeEspecial");
            }
        }
    }
}
