package jungjusung.boostcamp.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {


    TextView mAlarmPmAm;
    TextView mAlarmTime;
    TextView mAlarmDay;
    TextView mAlarmMemo;
    TextView mAlarmSoundName;
    Switch mUse;
    ImageView mAlarmImage;
    ImageView mEditImage;
    LinearLayout mItemLayout,mAlarmIconLayout;
    private Realm realm;
    String TAG;
    Animation mFadeIn, mFadeOut;
    Context context;
    private AlarmAdapter.ListItemClickListener mOnClickListener;

    public AlarmViewHolder(View itemView, Context context, AlarmAdapter.ListItemClickListener mOnClickListener) {
        super(itemView);
        this.context = context;
        this.mOnClickListener = mOnClickListener;

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
        mItemLayout=(LinearLayout)itemView.findViewById(R.id.ll_item_detail);
        mAlarmIconLayout=(LinearLayout)itemView.findViewById(R.id.ll_alarm_icon);
        itemView.setOnClickListener(this);
        mUse.setOnCheckedChangeListener(this);
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
            String minite=alarm.getAlarm_minute();
            if(Integer.parseInt(alarm.getAlarm_minute())<10) {
                minite = "0"+alarm.getAlarm_minute();
            }
            String time = Integer.parseInt(alarm.getAlarm_hour()) % 12 + ":" + minite;
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
                mFadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
                mAlarmIconLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                mEditImage.startAnimation(mFadeIn);
                mItemLayout.startAnimation(mFadeOut);
                mEditImage.setVisibility(View.VISIBLE);
                mItemLayout.setVisibility(View.GONE);
            } else {
                mFadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
                mAlarmIconLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
                mItemLayout.startAnimation(mFadeIn);
                mEditImage.startAnimation(mFadeOut);
                mEditImage.setVisibility(View.GONE);
                mItemLayout.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onClick(View view) {
        final int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int index=getAdapterPosition();
        Realm realm=Realm.getDefaultInstance();
        RealmResults<Alarm> list=realm.where(Alarm.class).findAll();
        Alarm alarm=list.get(index);
        if(isChecked){
            mAlarmImage.setBackgroundResource(R.drawable.ic_alarm_on);
            realm.beginTransaction();
            alarm.setAlarm_isDoing(true);
            realm.insertOrUpdate(alarm);
            realm.commitTransaction();
        }else{
            mAlarmImage.setBackgroundResource(R.drawable.ic_alarm_off);
            realm.beginTransaction();
            alarm.setAlarm_isDoing(false);
            realm.insertOrUpdate(alarm);
            realm.commitTransaction();
        }
    }
}
