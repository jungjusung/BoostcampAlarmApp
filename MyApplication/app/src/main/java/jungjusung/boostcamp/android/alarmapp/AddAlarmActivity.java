package jungjusung.boostcamp.android.alarmapp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.TransitionDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class AddAlarmActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int REQUEST_SOUND = 1;
    private static final int REQUEST_ITERATION = 2;

    LinearLayout mSound;
    LinearLayout mIteration;
    LinearLayout mReplay;
    LinearLayout mOptional;
    TimePicker mTimePicker;
    Resources system;
    Animation mFadeIn, mFadeOut;
    String TAG;


    EditText mEditMemo;
    TextView mTextSound, mTextIteration,mNowLocation;
    Switch mSwReplay;
    ArrayList<String> rList;
    RealmList<RealmString> iList = new RealmList<>();
    Realm realm;
    String mSoundName;
    String mSoundUri;
    String[] mDays = new String[]{"", "일", "월", "화", "수", "목", "금", "토"};
    GpsInfo gps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        realm = Realm.getDefaultInstance();
        TAG = this.getClass().getName();
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

        mSound.setOnClickListener(this);
        mIteration.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mOptional.setOnClickListener(this);

        set_timepicker_text_colour();
        gps = new GpsInfo(AddAlarmActivity.this);
        String nowLocation=findLocation(gps);
        mNowLocation.setText(nowLocation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_alarm) {
            if(mTextIteration.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"요일 반복을 설정해주세요.", Toast.LENGTH_SHORT).show();
            }else
                insertIntoRealmDB();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int clickedView = view.getId();
        Intent intent = null;
        switch (clickedView) {
            case R.id.ll_sound:
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent = new Intent(getApplicationContext(), SoundActivity.class);
                startActivityForResult(intent, REQUEST_SOUND);
                break;
            case R.id.ll_iteration:

                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent = new Intent(getApplicationContext(), IterationActivity.class);
                startActivityForResult(intent, REQUEST_ITERATION);
                break;
            case R.id.ll_replay:
                mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                break;
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
                Bundle rBundle = data.getExtras();
                rList = rBundle.getStringArrayList("iteration_list");
                StringBuffer sb = new StringBuffer();
                for (String item : rList) {
                    sb.append(item + " ");
                    RealmString realmStr = new RealmString();
                    realmStr.setValue(item);
                    iList.add(realmStr);
                }
                mTextIteration.setText(sb.toString());

            }
        }
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

    public void insertIntoRealmDB() {

        RealmResults<Alarm> result=realm.where(Alarm.class).findAll();
        RealmInit init = (RealmInit)getApplication();
        int sequnceNumber=0;
        try {
            if(realm.where(Alarm.class).findAll().size()>0)
                sequnceNumber=realm.where(Alarm.class).max("alarm_id").intValue()+1;
        } catch (ArrayIndexOutOfBoundsException e) {
            sequnceNumber=0;
        }
        Alarm alarm = new Alarm();
        realm.beginTransaction();

        alarm.setAlarm_id(sequnceNumber);
        alarm.setAlarm_isDoing(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarm.setAlarm_hour(String.valueOf(mTimePicker.getCurrentHour()));
            alarm.setAlarm_minute(String.valueOf(mTimePicker.getCurrentMinute()));
        } else {
            alarm.setAlarm_hour(String.valueOf(mTimePicker.getHour()));
            alarm.setAlarm_minute(String.valueOf(mTimePicker.getMinute()));
        }
        alarm.setIterList(iList);
        alarm.setAlarm_memo(mEditMemo.getText().toString());
        alarm.setAlarm_sound_name(mSoundName);
        alarm.setAlarm_sound_uri(mSoundUri);
        alarm.setAlarm_optional(null);
        alarm.setAlarm_replay(mSwReplay.isChecked());
        alarm.setAlarm_setting_receiver(false);
        realm.insert(alarm);
        realm.commitTransaction();
        startAlarm(alarm.isAlarm_replay(),alarm);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                RealmResults<Alarm> result=realm.where(Alarm.class).findAll();
                onBackPressed();
            }
        });

    }
    public void startAlarm(boolean isReapeating, Alarm alarm) {
        boolean settingFlag = alarm.isAlarm_setting_receiver();
        Log.d(TAG,settingFlag+"");
        if (settingFlag)
            return;
        Log.d(TAG,"그래서안옴");
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
}
