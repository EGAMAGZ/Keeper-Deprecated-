package com.android.keeper.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    public static String NOTIFICATION_CHANNEL_ID="notification_id";
    public static String NOTIFICATION_CHANNEL_NAME="notification-name";

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onReceive(Context context, Intent intent) {
        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        String channelId=intent.getParcelableExtra(NOTIFICATION_CHANNEL_ID);
        String channelName=intent.getParcelableExtra(NOTIFICATION_CHANNEL_NAME);
        /*NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getTaskChannelNotification();
        notificationHelper.getManager().notify(1,nb.build());*/

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            NotificationChannel notificationChannel = new NotificationChannel( channelId , channelName , NotificationManager.IMPORTANCE_DEFAULT) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;
    }
}
