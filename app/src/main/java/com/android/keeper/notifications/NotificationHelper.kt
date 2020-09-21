package com.android.keeper.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.NotificationCompat
import com.android.keeper.MainActivity
import com.android.keeper.R
import org.junit.runner.RunWith

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    var channelRemindersID: String? = "channelReminders"
    var channelRemindersName: String? = "Channel Reminders"
    private val context: Context? = null

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel tasksChannel=new NotificationChannel(channelTaskID,channelTasksName, NotificationManager.IMPORTANCE_DEFAULT);
        tasksChannel.enableLights(true);
        tasksChannel.enableVibration(true);
        tasksChannel.setLightColor(R.color.primaryColor);
        tasksChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        //getManager().createNotificationChannel(tasksChannel);
    }*/
    fun getManager(): NotificationManager? {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager
    }

    fun getTaskNotification(title: String?, content: String?): Notification? {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fragment", "tasks")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext, channelTaskID)
        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_check_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
        //TODO:ADD ACTION, COMPLETE TASK
        return builder.build()
    }

    companion object {
        var channelTaskID: String? = "channelTasks"
        var channelTasksName: String? = "Task Alarm"
        private var manager: NotificationManager? = null
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context = base
        }
    }
}