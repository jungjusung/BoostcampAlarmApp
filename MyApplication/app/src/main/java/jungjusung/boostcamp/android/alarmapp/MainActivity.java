package jungjusung.boostcamp.android.alarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements AlarmAdapter.ListItemClickListener {

    private RecyclerView mAlarmList;
    private AlarmAdapter alarmAdapter;
    private boolean isEditing = false;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_main);
        Context context = this.getApplicationContext();
        mAlarmList = (RecyclerView) findViewById(R.id.rv_alarmlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAlarmList.setLayoutManager(layoutManager);
        mAlarmList.setHasFixedSize(true);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Alarm> list = realm.where(Alarm.class).findAll();
        alarmAdapter = new AlarmAdapter(list.size(), context, isEditing, this);
        mAlarmList.setAdapter(alarmAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu, menu);
        //Menu 생성
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        switch (selectedMenuItem) {
            case R.id.action_setting:
                if (!isEditing)
                    isEditing = true;
                else
                    isEditing = false;
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Alarm> list = realm.where(Alarm.class).findAll();
                alarmAdapter = new AlarmAdapter(list.size(), this.getApplicationContext(), isEditing, this);
                mAlarmList.setAdapter(alarmAdapter);
                break;
            case R.id.action_add_alarm:
                Intent intent = new Intent(this.getApplicationContext(), AddAlarmActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Alarm> list = realm.where(Alarm.class).findAll();
        alarmAdapter.setItem(list.size());
        alarmAdapter.notifyDataSetChanged();
        Log.d(TAG, list.size() + " onResume");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (isEditing) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Alarm> alarmList=realm.where(Alarm.class).findAll();
            Alarm alarm=alarmList.get(clickedItemIndex);
            Context context = this.getApplicationContext();

            Toast.makeText(context, "id : " + alarm.getAlarm_id() + " 현재 시간 : " + alarm.getAlarm_hour() + " : " + alarm.getAlarm_minute() + "", Toast.LENGTH_SHORT).show();
            Intent detailIntent=new Intent(context,AlarmDetail.class);
            detailIntent.putExtra("alarm_id",alarm.getAlarm_id());
            startActivity(detailIntent);
        }
    }
}
