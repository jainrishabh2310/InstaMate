package com.example.instamate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCM extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Aagya", "onMessageReceived: ");

        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload here
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            // Display a local notification
//            showNotifi/cation(title, body);
        }


    }
    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("786", "Channel Name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "786")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.add_profile)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(786, notificationBuilder.build());
    }
}
