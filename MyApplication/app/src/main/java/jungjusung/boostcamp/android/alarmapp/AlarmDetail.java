package jungjusung.boostcamp.android.alarmapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 27..
 */
public class AlarmDetail extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    private static final int REQUEST_SOUND = 1;
    private static final int REQUEST_ITERATION = 2;
    Animation mFadeIn, mFadeOut;
    Resources system;

    LinearLayout mSound;
    LinearLayout mIteration;
    LinearLayout mReplay;
    LinearLayout mOptional;
    TimePicker mTimePicker;
    EditText mEditMemo;
    TextView mTextSound, mTextIteration,mNowLocation;
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
    GpsInfo gps;

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
        mNowLocation=(TextView)findViewById(R.id.tv_now_location);
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

        gps = new GpsInfo(AlarmDetail.this);
        String nowLocation=findLocation(gps);
        mNowLocation.setText(nowLocation);

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
            NotificationManager notificationManager
                    = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(alarm_id);
            updateAlarmSetRealmDB(alarm.getAlarm_id());
        } else if (item.getItemId() == android.R.id.home) {
            NotificationManager notificationManager
                    = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(alarm_id);
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
            case R.id.btn_delete:
                Toast.makeText(this, "디비 삭제 : "+alarm_id, Toast.LENGTH_SHORT).show();

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
        startAlarm(result.isAlarm_replay(),result);
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
        alarm.setAlarm_isDoing(false);
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
            alarm.setAlarm_isDoing(true);
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
    public void startAlarm(boolean isReapeating, Alarm alarm) {
        boolean settingFlag = alarm.isAlarm_setting_receiver();
        if (settingFlag)
            return;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm temp = alarm;
        temp.setAlarm_isDoing(true);
        temp.setAlarm_setting_receiver(true);
        realm.insertOrUpdate(temp);
        realm.commitTransaction();
        Context context=getApplicationContext();
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
    public String findLocation(GpsInfo gps){
        String nowAddress =getResources().getString(R.string.not_find_location);
        if (gps.isGetLocation()) {

        }
        Context context=this.getApplicationContext();
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {

                address = geocoder.getFromLocation(latitude, longitude, 1);
                //세번째 파라미터는 좌표에 대해 주소를 리턴 갯수로 하나만 표시

                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0);
                    Log.d(TAG,currentLocationAddress);
                    nowAddress  = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress;
    }
    private void set_numberpicker_text_colour(NumberPicker number_picker) {
        final int count = number_picker.getChildCount();
        final NumberPicker c = number_picker;
        for (int i = 0; i < count; i++) {
            final View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
                ((Paint) wheelpaint_field.get(number_picker)).setColor(selectedColor);
                ((EditText) child).setTextColor(selectedColor);
                number_picker.invalidate();
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            }
        }
    }


    private void set_timepicker_text_colour() {
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");
        NumberPicker hour_numberpicker = (NumberPicker) mTimePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) mTimePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) mTimePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);
    }
}
