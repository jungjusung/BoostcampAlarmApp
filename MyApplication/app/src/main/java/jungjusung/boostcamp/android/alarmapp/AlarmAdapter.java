package jungjusung.boostcamp.android.alarmapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jusung on 2017. 1. 22..
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder>{

    private int mAlarmItems;
    Context context;
    String TAG;

    public AlarmAdapter(int mAlarmItems,Context context) {
        this.mAlarmItems = mAlarmItems;
        this.context=context;
        TAG=this.getClass().getName();
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int listItem = R.layout.alarmlist_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(listItem, viewGroup, shouldAttachToParentImmediately);
        AlarmViewHolder alarmViewHolder = new AlarmViewHolder(view, context);

        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mAlarmItems;
    }
}
