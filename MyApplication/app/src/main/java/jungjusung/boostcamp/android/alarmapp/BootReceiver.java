package jungjusung.boostcamp.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 27..
 */

public class BootReceiver extends BroadcastReceiver {
    private String[] mDays = new String[]{"", "일", "월", "화", "수", "목", "금", "토"};

    @Override
    public void onReceive(Context context, Intent intent) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Alarm> list = realm.where(Alarm.class).findAll();
        for (Alarm alarm : list) {
            if(alarm.isAlarm_isDoing())
                startAlarm(alarm.isAlarm_replay(), alarm, context);
        }
    }
    public void startAlarm(boolean isReapeating, Alarm alarm,Context context) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm temp = alarm;
        temp.setAlarm_setting_receiver(true);
        realm.insertOrUpdate(temp);
        realm.commitTransaction();

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent activityIntent = new Intent(context, AlarmActivityReceiver.class);
        activityIntent.putExtra("id", alarm.getAlarm_id());
        Intent notiIntent = new Intent(context, AlarmNotificationReceiver.class);
        notiIntent.putExtra("sound_uri", alarm.getAlarm_sound_uri());
        notiIntent.putExtra("id", alarm.getAlarm_id());
        PendingIntent pendingNotiIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id(), notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingActivityIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();
        boolean findDay = false;
        int nowDay = calendar.get(Calendar.DAY_OF_WEEK);
        int diffDay = 0;
        RealmList<RealmString> days = alarm.getIterList();

        for (int i = nowDay; i <= 7; i++) {
            for (int j = 0; j < days.size(); j++) {
                if (mDays[i].equals(days.get(j).getValue())) {
                    findDay = true;
                    diffDay = i - nowDay;
                    break;
                }
            }
            if (findDay)
                break;
        }
        if (!findDay) {
            for (int i = 1; i <= nowDay; i++) {
                if (mDays[i].equals(days.get(0).getValue())) {
                    diffDay = nowDay - i;
                    break;
                }
            }
        }
        int addTime = diffDay * 24 * 60 * 60 * 1000;
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getAlarm_hour()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getAlarm_minute()));
        calendar.set(Calendar.SECOND, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            manager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis() + addTime, pendingNotiIntent), pendingNotiIntent);
            manager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis() + addTime, pendingActivityIntent), pendingActivityIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isReapeating) {
                //반복하지 않음
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingNotiIntent);
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingActivityIntent);
            } else {
                //3분씩 반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        } else {
            if (!isReapeating) {
                //반복하지 않음
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingNotiIntent);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingActivityIntent);
            } else {
                //3분씩 반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        }
    }
}
