package jungjusung.boostcamp.android.alarmapp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class AddAlarmActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int REQUEST_SOUND=1;
    private static final int REQUEST_ITERATION=2;
    private static final int REQUEST_OPTIONAL=3;
    LinearLayout mSound;
    LinearLayout mIteration;
    LinearLayout mReplay;
    LinearLayout mOptional;
    Animation mFadeIn,mFadeOut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        mSound=(LinearLayout)findViewById(R.id.ll_sound);
        mIteration=(LinearLayout)findViewById(R.id.ll_iteration);
        mReplay=(LinearLayout)findViewById(R.id.ll_replay);
        mOptional=(LinearLayout)findViewById(R.id.ll_optional);

        mSound.setOnClickListener(this);
        mIteration.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mOptional.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_save_alarm){
            Toast.makeText(this,"알람을 저장합니다.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int clickedView=view.getId();
        Intent intent=null;
        switch (clickedView){
            case R.id.ll_sound:
                Toast.makeText(this,"사운드 변경 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent=new Intent(getApplicationContext(),SoundActivity.class);
                startActivityForResult(intent,REQUEST_SOUND);
                break;
            case R.id.ll_iteration:
                Toast.makeText(this,"반복 변경 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent=new Intent(getApplicationContext(),IterationActivity.class);
                startActivityForResult(intent,REQUEST_ITERATION);
                break;
            case R.id.ll_replay:
                Toast.makeText(this,"다시 알림 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                break;
            case R.id.ll_optional:
                Toast.makeText(this,"추가 기능 클릭", Toast.LENGTH_SHORT).show();
                mFadeOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mFadeIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(mFadeOut);
                view.startAnimation(mFadeIn);
                intent=new Intent(getApplicationContext(),OptionalActivity.class);
                startActivityForResult(intent,REQUEST_OPTIONAL);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_SOUND&&data!=null){
            if(resultCode!= Activity.RESULT_OK){
                // 사운드 액티비티에서 결과 넘어옴
            }
        }else if(requestCode==REQUEST_ITERATION&&data!=null){
            if(resultCode!= Activity.RESULT_OK){
                // 반복 액티비티에서 결과 넘어옴
            }
        }else if(requestCode==REQUEST_OPTIONAL&&data!=null){
            if(resultCode!= Activity.RESULT_OK){
                // 추가 액티비티에서 결과 넘어옴
            }
        }

    }
}
