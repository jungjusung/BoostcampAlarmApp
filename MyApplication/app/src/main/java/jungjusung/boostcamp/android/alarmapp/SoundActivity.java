package jungjusung.boostcamp.android.alarmapp;


import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jusung on 2017. 1. 24..
 */

public class SoundActivity extends AppCompatActivity implements View.OnClickListener {

    private Ringtone r;
    private LinearLayout mSoundDefault, mSoundWakeUp, mSoundMinions, mSoundJarvis, mSoundCheer, mSoundEnergy, mSoundMirotic;
    private ImageView mImageDefaut, mImageWakeUp, mImageMinions, mImageJarvis, mImageCheer, mImageEnergy, mImageMirotic;
    private List<LinearLayout> mSoundList = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();
    private Uri notification;
    private String sound_result, sound_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        sound_result = getString(R.string.tv_default);
        mSoundDefault = (LinearLayout) findViewById(R.id.ll_sound_default);
        mSoundWakeUp = (LinearLayout) findViewById(R.id.ll_sound_wake_up);
        mSoundMinions = (LinearLayout) findViewById(R.id.ll_sound_minions);
        mSoundJarvis = (LinearLayout) findViewById(R.id.ll_sound_jarvis);
        mSoundCheer = (LinearLayout) findViewById(R.id.ll_sound_cheer);
        mSoundEnergy = (LinearLayout) findViewById(R.id.ll_sound_energy);
        mSoundMirotic = (LinearLayout) findViewById(R.id.ll_sound_mirotic);
        mImageDefaut = (ImageView) findViewById(R.id.iv_sound_default);
        mImageWakeUp = (ImageView) findViewById(R.id.iv_sound_wake_up);
        mImageMinions = (ImageView) findViewById(R.id.iv_sound_minions);
        mImageJarvis = (ImageView) findViewById(R.id.iv_sound_jarvis);
        mImageCheer = (ImageView) findViewById(R.id.iv_sound_cheer);
        mImageEnergy = (ImageView) findViewById(R.id.iv_sound_energy);
        mImageMirotic = (ImageView) findViewById(R.id.iv_sound_mirotic);

        mSoundList.add(mSoundDefault);
        mSoundList.add(mSoundWakeUp);
        mSoundList.add(mSoundMinions);
        mSoundList.add(mSoundJarvis);
        mSoundList.add(mSoundCheer);
        mSoundList.add(mSoundEnergy);
        mSoundList.add(mSoundMirotic);

        mImageList.add(mImageDefaut);
        mImageList.add(mImageWakeUp);
        mImageList.add(mImageMinions);
        mImageList.add(mImageJarvis);
        mImageList.add(mImageCheer);
        mImageList.add(mImageEnergy);
        mImageList.add(mImageMirotic);

        for (int i = 0; i < mSoundList.size(); i++) {
            mSoundList.get(i).setOnClickListener(this);
        }

        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("sound_name", sound_result);
        intent.putExtra("sound_uri", sound_uri);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (r != null) {
            if (r.isPlaying())
                r.stop();
        }

    }

    @Override
    public void onClick(View view) {
        // 클릭했을때 알람의 소리를 미리 들려준다.
        if (r != null) {
            if (r.isPlaying())
                r.stop();
        }
        int selectedViewId = view.getId();
        switch (selectedViewId) {
            case R.id.ll_sound_default:
                setBackgroundImageView(R.id.iv_sound_default);
                break;
            case R.id.ll_sound_wake_up:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.best_wake_up_sound;
                    setBackgroundImageView(R.id.iv_sound_wake_up);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sound_minions:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.minion_ring_ring;
                    setBackgroundImageView(R.id.iv_sound_minions);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sound_jarvis:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.jarvis_alarm;
                    setBackgroundImageView(R.id.iv_sound_jarvis);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sound_cheer:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.awesomemorning_alarm;
                    setBackgroundImageView(R.id.iv_sound_cheer);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sound_energy:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.elegant_ringtone;
                    setBackgroundImageView(R.id.iv_sound_energy);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sound_mirotic:
                try {
                    sound_uri = "android.resource://" + getPackageName() + "/raw/" + R.raw.very_nice_alarm;
                    setBackgroundImageView(R.id.iv_sound_mirotic);
                    notification = Uri.parse(sound_uri);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

    public void setBackgroundImageView(int id) {
        // 알람 체크된 효과를 주기 위한 로직
        for (int i = 0; i < mImageList.size(); i++) {

            if (mImageList.get(i).getId() == id) {
                //아이디가 같으면 체크 아니면 디폴트
                mImageList.get(i).setBackgroundResource(R.drawable.ic_check);
                sound_result = ((TextView) mSoundList.get(i).getChildAt(0)).getText().toString();
            } else {
                mImageList.get(i).setBackgroundResource(0);
            }
        }
    }
}
