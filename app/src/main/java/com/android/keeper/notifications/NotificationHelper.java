package com.android.keeper.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.android.keeper.MainActivity;
import com.android.keeper.R;

public class NotificationHelper extends ContextWrapper {

    public static String channelTaskID="channelTasks";
    public static String channelTasksName="Task Alarm";
    public String channelRemindersID="channelReminders";
    public String channelRemindersName="Channel Reminders";

    private static NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            //createChannels();
        }
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel tasksChannel=new NotificationChannel(channelTaskID,channelTasksName, NotificationManager.IMPORTANCE_DEFAULT);
        tasksChannel.enableLights(true);
        tasksChannel.enableVibration(true);
        tasksChannel.setLightColor(R.color.primaryColor);
        tasksChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        //getManager().createNotificationChannel(tasksChannel);
    }*/

    public NotificationManager getManager(){
        if(manager==null){
            manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification getTaskNotification(String title, String content) {
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("fragment","tasks");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channelTaskID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_check_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent);
        return builder.build();
    }
}
