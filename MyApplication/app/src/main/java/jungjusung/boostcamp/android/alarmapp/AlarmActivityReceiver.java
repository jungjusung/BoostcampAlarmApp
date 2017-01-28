package jungjusung.boostcamp.android.alarmapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.realm.Realm;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmActivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //DB Primary값으로 상세 정보 액티비티를 호출한다.
        int id = intent.getIntExtra("id", -1);
        Realm realm = Realm.getDefaultInstance();
        Alarm alarm = realm.where(Alarm.class).equalTo("alarm_id", id).findFirst();
        //알람이 off면 작동안한다.
        if (!alarm.isAlarm_isDoing())
            return;
        if (id == -1)
            return;


        Intent detailIntent = new Intent(context, AlarmDetail.class);
        detailIntent.putExtra("alarm_repeat", true);
        detailIntent.putExtra("alarm_id", id);
        PendingIntent pi = PendingIntent.getActivity(context, id, detailIntent, PendingIntent.FLAG_ONE_SHOT);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
