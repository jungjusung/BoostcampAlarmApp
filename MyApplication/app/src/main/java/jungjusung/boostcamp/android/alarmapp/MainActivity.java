package jungjusung.boostcamp.android.alarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mAlarmList;
    private AlarmAdapter alarmAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context=this.getApplicationContext();
        mAlarmList=(RecyclerView)findViewById(R.id.rv_alarmlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAlarmList.setLayoutManager(layoutManager);
        mAlarmList.setHasFixedSize(true);

        alarmAdapter = new AlarmAdapter(100,context);
        mAlarmList.setAdapter(alarmAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu,menu);
        //Menu 생성
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem=item.getItemId();
        switch (selectedMenuItem){
            case R.id.action_setting:
                Toast.makeText(this,"세팅 메뉴", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_alarm:
                Intent intent=new Intent(this.getApplicationContext(),AddAlarmActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
