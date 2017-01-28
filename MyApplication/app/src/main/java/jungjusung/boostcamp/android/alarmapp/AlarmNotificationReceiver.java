package jungjusung.boostcamp.android.alarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import io.realm.Realm;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //노티와 사운드를 전해주는 리시버
        String uri = intent.getStringExtra("sound_uri");
        int id = intent.getIntExtra("id", -1);
        Realm realm = Realm.getDefaultInstance();
        Alarm alarm = realm.where(Alarm.class).equalTo("alarm_id", id).findFirst();
        //알람이 off면 작동안한다.
        if (!alarm.isAlarm_isDoing())
            return;
        if (id == -1)
            return;
        PushWakeLock.acquireCpuWakeLock(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_alarm_noti)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setSound(Uri.parse(uri))
                .setContentInfo("Info");

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
        PushWakeLock.releaseCpuLock();
    }
}
