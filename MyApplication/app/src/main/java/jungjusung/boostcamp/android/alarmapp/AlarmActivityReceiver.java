package jungjusung.boostcamp.android.alarmapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmActivityReceiver extends BroadcastReceiver {
    String TAG;
    @Override
    public void onReceive(Context context, Intent intent) {
        TAG=this.getClass().getName();
        int id=intent.getIntExtra("id",-1);
        Realm realm=Realm.getDefaultInstance();
        Alarm alarm=realm.where(Alarm.class).equalTo("alarm_id",id).findFirst();
        if(!alarm.isAlarm_isDoing())
            return;
        if(id==-1)
            return;

        Log.d(TAG,"리시버 액티비티 호출");
        Intent detailIntent=new Intent(context,AlarmDetail.class);
        detailIntent.putExtra("alarm_repeat",true);
        detailIntent.putExtra("alarm_id",id);
        PendingIntent pi=PendingIntent.getActivity(context,id,detailIntent,PendingIntent.FLAG_ONE_SHOT);

        try {
            pi.send();

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
