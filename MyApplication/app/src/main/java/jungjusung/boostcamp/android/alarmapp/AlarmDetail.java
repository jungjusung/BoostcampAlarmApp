package jungjusung.boostcamp.android.alarmapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmDetail extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    private static final int REQUEST_SOUND = 1;
    private static final int REQUEST_ITERATION = 2;
    private static final int REQUEST_OPTIONAL = 3;
    Animation mFadeIn, mFadeOut;

    LinearLayout mSound;
    LinearLayout mIteration;
    LinearLayout mReplay;
    LinearLayout mOptional;
    TimePicker mTimePicker;
    EditText mEditMemo;
    TextView mTextSound, mTextIteration;
    Switch mSwReplay;
    Button mDeleteAlarm;
    ArrayList<String> rList;
    RealmList<RealmString> iList;
    RealmList<RealmString> tempList;
    String mSoundName;
    String mSoundUri;
    LinearLayout mLayout;
    Alarm alarm = null;
    String TAG;
    int alarm_id;
    boolean alarm_repeat;
    private String[] mDays = new String[]{"", "일", "월", "화", "수", "목", "금", "토"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TAG = this.getClass().getName();
        Intent intent = getIntent();
        alarm_id = intent.getIntExtra("alarm_id", -1);
        alarm_repeat=intent.getBooleanExtra("alarm_repeat",false);
        Realm realm = Realm.getDefaultInstance();
        if (alarm_id != -1) {
            alarm = realm.where(Alarm.class).equalTo("alarm_id", alarm_id).findFirst();
        }
        mLayout = (LinearLayout) findViewById(R.id.ll_detail);
        mSound = (LinearLayout) findViewById(R.id.ll_sound);
        mIteration = (LinearLayout) findViewById(R.id.ll_iteration);
        mReplay = (LinearLayout) findViewById(R.id.ll_replay);
        mOptional = (LinearLayout) findViewById(R.id.ll_optional);
        mTimePicker = (TimePicker) findViewById(R.id.tp_time);

        //DB 넘길 자료들
        mEditMemo = (EditText) findViewById(R.id.et_memo);
        mTextSound = (TextView) findViewById(R.id.tv_sound);
        mTextIteration = (TextView) findViewById(R.id.tv_iteration);
        mSwReplay = (Switch) findViewById(R.id.sw_replay);
        mDeleteAlarm = (Button) findViewById(R.id.btn_delete);
        repeatAlarm();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mTimePicker.setCurrentHour(Integer.parseInt(alarm.getAlarm_hour()));
            mTimePicker.setCurrentMinute(Integer.parseInt(alarm.getAlarm_minute()));
        } else {
            mTimePicker.setHour(Integer.parseInt(alarm.getAlarm_hour()));
            mTimePicker.setMinute(Integer.parseInt(alarm.getAlarm_minute()));
        }
        mEditMemo.setText(alarm.getAlarm_memo());
        mTextSound.setText(alarm.getAlarm_sound_name());
        mSoundName = alarm.getAlarm_sound_name();
        mSoundUri = alarm.getAlarm_sound_uri();
        iList = alarm.getIterList();

        StringBuffer sb = new StringBuffer();
        for (RealmString str : iList) {
            sb.append(str.getValue() + " ");
        }
        mTextIteration.setText(sb.toString());
        mSwReplay.setChecked(alarm.isAlarm_replay());

        mSound.setOnClickListener(this);
        mIteration.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mOptional.setOnClickListener(this);
        mDeleteAlarm.setOnClickListener(this);
        mLayout.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_alarm) {
            //디비 저장!!!
            Toast.makeText(this, "알람수정", Toast.LENGTH_SHORT).show();
            updateAlarmSetRealmDB(alarm.getAlarm_id());
        } else if (item.getItemId() == android.R.id.home) {
            NotificationManager notificationManager
                    = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(alarm_id);
            //repeatAlarm();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        int clickedView = view.getId();
        Intent intent = null;
        switch (clickedView) {
            case R.id.ll_sound:
                Toast.makeText(this, "사운드 변경 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent = new Intent(getApplicationContext(), SoundActivity.class);
                startActivityForResult(intent, REQUEST_SOUND);
                break;
            case R.id.ll_iteration:
                Toast.makeText(this, "반복 변경 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent = new Intent(getApplicationContext(), IterationActivity.class);
                startActivityForResult(intent, REQUEST_ITERATION);
                break;
            case R.id.ll_replay:
                Toast.makeText(this, "다시 알림 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                break;
            case R.id.ll_optional:
                Toast.makeText(this, "추가 기능 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent = new Intent(getApplicationContext(), OptionalActivity.class);
                startActivityForResult(intent, REQUEST_OPTIONAL);
                break;
            case R.id.btn_delete:
                Toast.makeText(this, "디비 삭제", Toast.LENGTH_SHORT).show();
                deleteAlarmFromRealmDB(alarm_id);
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SOUND && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                mSoundName = data.getStringExtra("sound_name");
                mSoundUri = data.getStringExtra("sound_uri");
                mTextSound.setText(mSoundName);
                // 사운드 액티비티에서 결과 넘어옴
            }
        } else if (requestCode == REQUEST_ITERATION && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                final Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Bundle rBundle = data.getExtras();
                rList = rBundle.getStringArrayList("iteration_list");
                StringBuffer sb = new StringBuffer();
                tempList = new RealmList<>();

                for (String item : rList) {
                    sb.append(item + " ");
                    RealmString realmStr = realm.createObject(RealmString.class);
                    realmStr.setValue(item);
                    tempList.add(realmStr);
                    mTextIteration.setText(sb.toString());
                }
                realm.commitTransaction();
            }
        } else if (requestCode == REQUEST_OPTIONAL && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                // 추가 액티비티에서 결과 넘어옴
            }
        }
    }

    public void updateAlarmSetRealmDB(int index) {

        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm result = realm.where(Alarm.class).equalTo("alarm_id", index).findFirst();
        result.setAlarm_isDoing(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            result.setAlarm_hour(String.valueOf(mTimePicker.getCurrentHour()));
            result.setAlarm_minute(String.valueOf(mTimePicker.getCurrentMinute()));
        } else {
            result.setAlarm_hour(String.valueOf(mTimePicker.getHour()));
            result.setAlarm_minute(String.valueOf(mTimePicker.getMinute()));
        }
        if (tempList != null)
            result.setIterList(tempList);
        result.setAlarm_memo(mEditMemo.getText().toString());
        result.setAlarm_sound_name(mSoundName);
        result.setAlarm_sound_uri(mSoundUri);
        result.setAlarm_replay(mSwReplay.isChecked());
        result.setAlarm_setting_receiver(false);
        realm.insert(result);
        realm.commitTransaction();

        deleteBroadCast(index);
        onBackPressed();
    }

    public void deleteAlarmFromRealmDB(int index) {
        deleteBroadCast(index);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm result = realm.where(Alarm.class).equalTo("alarm_id", index).findFirst();
        result.deleteFromRealm();
        realm.commitTransaction();
        //리시버도 같이 지워줘야 한다.
        onBackPressed();
    }

    public void deleteBroadCast(int index) {
        Context context = this.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notiIntent = new Intent(context, AlarmNotificationReceiver.class);
        Intent activityIntent = new Intent(context, AlarmActivityReceiver.class);
        Intent detailIntent = new Intent(context, AlarmDetail.class);
        PendingIntent notiPendingIntent = PendingIntent.getBroadcast(context, index, notiIntent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent activityPendingIntent = PendingIntent.getBroadcast(context, index, activityIntent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent detailPendingIntent = PendingIntent.getActivity(context, index, detailIntent, PendingIntent.FLAG_NO_CREATE);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm alarm = realm.where(Alarm.class).equalTo("alarm_id", index).findFirst();
        alarm.setAlarm_setting_receiver(false);
        realm.insertOrUpdate(alarm);
        realm.commitTransaction();

        if (notiPendingIntent != null) {
            alarmManager.cancel(notiPendingIntent);
            notiPendingIntent.cancel();
        }
        if (activityPendingIntent != null) {
            alarmManager.cancel(activityPendingIntent);
            activityPendingIntent.cancel();
        }
        if (detailPendingIntent != null) {
            alarmManager.cancel(detailPendingIntent);
            detailPendingIntent.cancel();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.ll_detail) {
            NotificationManager notificationManager
                    = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(alarm_id);
            //repeatAlarm();
            return true;
        }
        return false;
    }

    public void repeatAlarm() {
        if (alarm_repeat&&alarm.isAlarm_isDoing() && alarm.isAlarm_replay() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            deleteBroadCast(alarm_id);
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            Alarm alarm=realm.where(Alarm.class).equalTo("alarm_id",alarm_id).findFirst();
            alarm.setAlarm_setting_receiver(true);
            realm.insertOrUpdate(alarm);
            realm.commitTransaction();
            
            Context context = getApplicationContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notiIntent = new Intent(context, AlarmNotificationReceiver.class);
            notiIntent.putExtra("sound_uri", alarm.getAlarm_sound_uri());
            notiIntent.putExtra("id", alarm.getAlarm_id());
            Intent activityIntent = new Intent(context, AlarmActivityReceiver.class);
            activityIntent.putExtra("id", alarm.getAlarm_id());
            PendingIntent notiPendingIntent = PendingIntent.getBroadcast(context, alarm_id, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent activityPendingIntent = PendingIntent.getBroadcast(context, alarm_id, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c = Calendar.getInstance();

            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis() + 3 * 60 * 1000, notiPendingIntent), notiPendingIntent);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis() + 3 * 60 * 1000, activityPendingIntent), activityPendingIntent);
        }
    }
}
