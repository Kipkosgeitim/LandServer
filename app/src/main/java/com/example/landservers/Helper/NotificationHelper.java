package com.example.landservers.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.landservers.R;

public class NotificationHelper extends ContextWrapper {

    private static final String LAND_CHANNEL_ID = "com.example.landservers.AppFood";
    private static final String LAND_CHANNEL_NAME ="Land Purchase";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel appFoodChannel =new NotificationChannel(LAND_CHANNEL_ID,LAND_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        appFoodChannel.enableLights(false);
        appFoodChannel.enableVibration(true);
        appFoodChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(appFoodChannel);

    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        return manager;

    }
    @TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getEatItChannelNotification(String title, String body, PendingIntent contentIntent,
                                                                        Uri soundUri)
    {
        return new android.app.Notification.Builder(getApplicationContext(),LAND_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
    @TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getEatItChannelNotification(String title, String body,
                                                                        Uri soundUri)
    {
        return new android.app.Notification.Builder(getApplicationContext(),LAND_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
