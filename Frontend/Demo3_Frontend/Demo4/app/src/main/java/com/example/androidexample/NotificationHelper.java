package com.example.androidexample;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class NotificationHelper {

    private static final String CHANNEL_ID = MainActivity.getID();
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;


    public static void showNotification(Context context, String title, String message) {
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Create an Intent for the notification action
        Intent acceptIntent = new Intent(context, LevelEditor.class);
        Intent ignoreIntent = new Intent(context, LevelEditor.class);

        // Set the mutability flag for PendingIntent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;

// Create separate PendingIntents for each action with the mutability flag
        PendingIntent acceptPendingIntent = PendingIntent.getActivity(context, 0, acceptIntent, flags | PendingIntent.FLAG_MUTABLE);
        PendingIntent ignorePendingIntent = PendingIntent.getActivity(context, 1, ignoreIntent, flags | PendingIntent.FLAG_MUTABLE);

        // Add accept and ignore actions
        builder.addAction(R.drawable.ic_accept, "Accept", acceptPendingIntent)
                .addAction(R.drawable.ic_ignore, "Ignore", ignorePendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = (int) System.currentTimeMillis();
        if (ActivityCompat.checkSelfPermission(context, "android.permission.SEND_NOTIFICATION") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.SEND_NOTIFICATION"}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            return;
        }
        System.out.println("Permission to post notifications");
        notificationManager.notify(notificationId, builder.build());
    }


    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "testUser",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}