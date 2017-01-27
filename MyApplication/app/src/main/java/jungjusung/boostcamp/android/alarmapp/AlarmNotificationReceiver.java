package jungjusung.boostcamp.android.alarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {
    String TAG;
    @Override
    public void onReceive(Context context, Intent intent) {
        TAG=this.getClass().getName();
        String uri=intent.getStringExtra("sound_uri");
        int id=intent.getIntExtra("id",-1);
        if(id==-1)
            return;
        PushWakeLock.acquireCpuWakeLock(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_alarm_noti)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                .setSound(Uri.parse(uri))
                .setOngoing(true)
                .setContentInfo("Info");
        Notification noti=builder.build();
        noti.flags=Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,noti);

        PushWakeLock.releaseCpuLock();
    }
}
