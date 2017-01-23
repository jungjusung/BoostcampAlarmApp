package jungjusung.boostcamp.android.alarmapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jusung on 2017. 1. 22..
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder{

    TextView mAlarmPmAm;
    TextView mAlarmTime;
    TextView mAlarmUse;
    TextView mAlarmDay;
    ImageView mAlarmImage;
    public AlarmViewHolder(View itemView, Context context) {
        super(itemView);
        mAlarmPmAm=(TextView)itemView.findViewById(R.id.tv_am_pm);
        mAlarmTime=(TextView)itemView.findViewById(R.id.tv_time);
        mAlarmUse=(TextView)itemView.findViewById(R.id.tv_useAlarm);
        mAlarmDay=(TextView)itemView.findViewById(R.id.tv_day);
        mAlarmImage=(ImageView) itemView.findViewById(R.id.iv_useAlarm);

    }
    void bind(int listIndex) {

    }
}
