package com.example.database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
            super.onMessageReceived(remoteMessage);
            Log.v("testremote", "" + remoteMessage);

           // Log.i(getString(R.string.DEBUG_TAG), "Remote Message received");

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",IMPORTANCE_HIGH);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");

                //     notificationChannel.setLockscreenVisibility(1);

                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);

            }


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

            notificationBuilder.setAutoCancel(true)
                    //.setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.alert)
                    .setTicker("Hearty365")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Tank Level Notification")
                    .setContentText("The Level of water is low.")
                    .setContentInfo("Info")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            notificationManager.notify(/*notification id*/1, notificationBuilder.build());


    }
}