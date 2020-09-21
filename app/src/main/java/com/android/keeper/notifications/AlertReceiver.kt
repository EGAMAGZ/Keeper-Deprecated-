package com.android.keeper.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.android.keeper.R
import org.junit.runner.RunWith

class AlertReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null
    private var notification: Notification? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification = intent.getParcelableExtra<Notification?>(NOTIFICATION)
        val channelId = intent.getStringExtra(NOTIFICATION_CHANNEL_ID)
        val channelName = intent.getStringExtra(NOTIFICATION_CHANNEL_NAME)
        /*NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getTaskChannelNotification();
        notificationHelper.getManager().notify(1,nb.build());*/if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = R.color.primaryColor
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, notification)
    }

    companion object {
        var NOTIFICATION_ID: String? = "notification-id"
        var NOTIFICATION: String? = "notification"
        var NOTIFICATION_CHANNEL_ID: String? = "notification_id"
        var NOTIFICATION_CHANNEL_NAME: String? = "notification-name"
    }
}