package jungjusung.boostcamp.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 22..
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    TextView mAlarmPmAm;
    TextView mAlarmTime;
    TextView mAlarmDay;
    TextView mAlarmMemo;
    TextView mAlarmSoundName;
    Switch mUse;
    ImageView mAlarmImage;
    private Realm realm;
    String TAG;
    Context context;
    String[] mDays=new String[]{"","일","월","화","수","목","금","토"};
    public AlarmViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        TAG = this.getClass().getName();
        realm = Realm.getDefaultInstance();
        mAlarmPmAm = (TextView) itemView.findViewById(R.id.tv_am_pm);
        mAlarmTime = (TextView) itemView.findViewById(R.id.tv_time);
        mAlarmDay = (TextView) itemView.findViewById(R.id.tv_day);
        mAlarmMemo = (TextView) itemView.findViewById(R.id.tv_memo);
        mAlarmSoundName = (TextView) itemView.findViewById(R.id.tv_sound_name);
        mAlarmImage = (ImageView) itemView.findViewById(R.id.iv_useAlarm);
        mUse = (Switch) itemView.findViewById(R.id.sw_use);

    }

    void bind(int listIndex) {
        RealmResults<Alarm> list = realm.where(Alarm.class).findAll();
        if (list.size() > 0) {
            Alarm alarm = list.get(listIndex);
            if (Integer.parseInt(alarm.getAlarm_hour()) > 12) {
                mAlarmPmAm.setText(context.getResources().getString(R.string.tv_pm));
            } else {
                mAlarmPmAm.setText(context.getResources().getString(R.string.tv_am));
            }
            String time = Integer.parseInt(alarm.getAlarm_hour()) % 12 + ":" + Integer.parseInt(alarm.getAlarm_minute());
            mAlarmTime.setText(time);
            RealmList<RealmString> iList = alarm.getIterList();
            StringBuffer sb = new StringBuffer();
            if (alarm.isAlarm_replay())
                sb.append("반복 ");
            for (RealmString item : iList) {
                sb.append(item.getValue() + " ");
            }
            mAlarmDay.setText(sb.toString());
            if (alarm.isAlarm_isDoing()) {
                mAlarmImage.setBackgroundResource(R.drawable.ic_alarm_on);
                mUse.setChecked(alarm.isAlarm_isDoing());
            } else {
                mAlarmImage.setBackgroundResource(R.drawable.ic_alarm_off);
                mUse.setChecked(alarm.isAlarm_isDoing());
            }
            mAlarmSoundName.setText(alarm.getAlarm_sound_name());
            mAlarmMemo.setText(alarm.getAlarm_memo());
            startAlarm(alarm.isAlarm_replay(),alarm);
        }
    }

    public void startAlarm(boolean isReapeating, Alarm alarm) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent activityIntent = new Intent(context, AlarmActivityReceiver.class);
        Intent notiIntent = new Intent(context, AlarmNotificationReceiver.class);
        notiIntent.putExtra("sound_uri",alarm.getAlarm_sound_uri());
        notiIntent.putExtra("id",alarm.getAlarm_id());
        PendingIntent pendingNotiIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id(), notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingActivityIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();
        boolean findDay=false;
        RealmList<RealmString> days=alarm.getIterList();
        for(RealmString day:days){
            if(day.getValue().equals(mDays[calendar.get(Calendar.DAY_OF_WEEK)])){
                findDay=true;
                break;
            }
        }
        if(!findDay) {
            return;
        }else{
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getAlarm_hour()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getAlarm_minute()));
            calendar.set(Calendar.SECOND, 0);
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (!isReapeating) {
                //반복하지 않음
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingNotiIntent);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingActivityIntent);
            } else {
                //반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        } else {
            if (!isReapeating) {
                //반복하지 않음
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingNotiIntent);
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingActivityIntent);
            } else {
                //반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        }


    }
}
