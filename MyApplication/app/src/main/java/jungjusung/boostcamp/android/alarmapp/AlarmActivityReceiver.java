package jungjusung.boostcamp.android.alarmapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmActivityReceiver extends BroadcastReceiver {
    String TAG;
    @Override
    public void onReceive(Context context, Intent intent) {
        TAG=this.getClass().getName();
        Log.d(TAG,"리시버 액티비티 호출");
        intent=new Intent(context,AlarmDetail.class);
        PendingIntent pi=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
