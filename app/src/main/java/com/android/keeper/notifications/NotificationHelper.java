package com.android.keeper.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.android.keeper.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelTaskID="channelTasks";
    public static final String channelTasksName="Task Alarm";
    public static final String channelRemindersID="channelReminders";
    public static final String channelRemindersName="Channel Reminders";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel tasksChannel=new NotificationChannel(channelTaskID,channelTasksName, NotificationManager.IMPORTANCE_DEFAULT);
        tasksChannel.enableLights(true);
        tasksChannel.enableVibration(true);
        tasksChannel.setLightColor(R.color.primaryColor);
        tasksChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(tasksChannel);
    }

    public NotificationManager getManager(){
        if(manager==null){
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getTaskChannelNotification(String title,String message){
        return new NotificationCompat.Builder(getApplicationContext(),channelTaskID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_check_black_24dp);
    }
}
