package jungjusung.boostcamp.android.alarmapp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.TransitionDrawable;
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

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class AddAlarmActivity extends AppCompatActivity implements View.OnClickListener
        , CompoundButton.OnCheckedChangeListener, TimePicker.OnTimeChangedListener {


    private static final int REQUEST_SOUND = 1;
    private static final int REQUEST_ITERATION = 2;
    private static final int REQUEST_OPTIONAL = 3;

    LinearLayout mSound;
    LinearLayout mIteration;
    LinearLayout mReplay;
    LinearLayout mOptional;
    TimePicker mTimePicker;
    Resources system;
    Animation mFadeIn, mFadeOut;
    String TAG;


    EditText mEditMemo;
    TextView mTextSound, mTextIteration;
    Switch mSwReplay;
    ArrayList<String> rList;
    RealmList<RealmString> iList = new RealmList<>();
    Realm realm;
    String mSoundName;
    String mSoundUri;

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
        mSwReplay = (Switch) findViewById(R.id.sw_replay);

        mSound.setOnClickListener(this);
        mIteration.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mOptional.setOnClickListener(this);
        mSwReplay.setOnCheckedChangeListener(this);
        mTimePicker.setOnTimeChangedListener(this);

        set_timepicker_text_colour();

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
            //디비 저장!!!
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
        } else if (requestCode == REQUEST_OPTIONAL && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                // 추가 액티비티에서 결과 넘어옴
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
                ((EditText) child).setTextSize(20);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecking) {
        if (isChecking) {
            Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public void insertIntoRealmDB() {

        RealmResults<Alarm> result=realm.where(Alarm.class).findAll();
        Toast.makeText(this, "DB 갯수 : "+result.size(), Toast.LENGTH_SHORT).show();
        RealmInit init = (RealmInit)getApplication();
        int sequnceNumber = init.getREALM_INDEX()+1;
        init.setREALM_INDEX(sequnceNumber);
        Alarm alarm = new Alarm();
        realm.beginTransaction();

        alarm.setAlarm_id(sequnceNumber);
        alarm.setAlarm_isDoing(true);
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

        realm.insert(alarm);
        realm.commitTransaction();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                RealmResults<Alarm> result=realm.where(Alarm.class).findAll();
                Toast.makeText(AddAlarmActivity.this, "성공 : "+result.size(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
