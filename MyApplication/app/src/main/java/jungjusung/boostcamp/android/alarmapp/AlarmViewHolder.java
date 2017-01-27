package jungjusung.boostcamp.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 22..
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView mAlarmPmAm;
    TextView mAlarmTime;
    TextView mAlarmDay;
    TextView mAlarmMemo;
    TextView mAlarmSoundName;
    Switch mUse;
    ImageView mAlarmImage;
    ImageView mEditImage;
    private Realm realm;
    String TAG;
    Context context;
    private AlarmAdapter.ListItemClickListener mOnClickListener;

    String[] mDays = new String[]{"", "일", "월", "화", "수", "목", "금", "토"};

    public AlarmViewHolder(View itemView, Context context, AlarmAdapter.ListItemClickListener mOnClickListener) {
        super(itemView);
        this.context = context;
        this.mOnClickListener = mOnClickListener;
        itemView.setOnClickListener(this);

        TAG = this.getClass().getName();
        realm = Realm.getDefaultInstance();
        mAlarmPmAm = (TextView) itemView.findViewById(R.id.tv_am_pm);
        mAlarmTime = (TextView) itemView.findViewById(R.id.tv_time);
        mAlarmDay = (TextView) itemView.findViewById(R.id.tv_day);
        mAlarmMemo = (TextView) itemView.findViewById(R.id.tv_memo);
        mAlarmSoundName = (TextView) itemView.findViewById(R.id.tv_sound_name);
        mAlarmImage = (ImageView) itemView.findViewById(R.id.iv_useAlarm);
        mEditImage = (ImageView) itemView.findViewById(R.id.iv_edit_icon);
        mUse = (Switch) itemView.findViewById(R.id.sw_use);
    }

    void bind(int listIndex, boolean isEditing) {
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
            if (isEditing) {
                mEditImage.setVisibility(View.VISIBLE);
            } else {
                mEditImage.setVisibility(View.GONE);
            }
            startAlarm(alarm.isAlarm_replay(), alarm);
        }
    }

    public void startAlarm(boolean isReapeating, Alarm alarm) {
        boolean settingFlag = alarm.isAlarm_setting_receiver();
        if (settingFlag)
            return;

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
                //반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        } else {
            if (!isReapeating) {
                //반복하지 않음
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingNotiIntent);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + addTime, pendingActivityIntent);
            } else {
                //반복함
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingNotiIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3 * 60 * 1000, pendingActivityIntent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        final int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}
