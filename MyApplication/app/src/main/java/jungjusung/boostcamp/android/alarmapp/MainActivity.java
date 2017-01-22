package jungjusung.boostcamp.android.alarmapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
}
