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

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private int mAlarmItems;
    private final static String TAG = AlarmAdapter.class.getSimpleName();
    private boolean isEditing;
    final private ListItemClickListener mOnClickListener;
    Context context;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public AlarmAdapter(int mAlarmItems, Context context, boolean isEditing, ListItemClickListener listener) {
        this.mAlarmItems = mAlarmItems;
        this.context = context;
        this.isEditing = isEditing;
        mOnClickListener = listener;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int listItem = R.layout.alarmlist_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(listItem, viewGroup, shouldAttachToParentImmediately);
        AlarmViewHolder alarmViewHolder = new AlarmViewHolder(view, context, mOnClickListener);

        return alarmViewHolder;
    }

    public void setItem(int size) {
        mAlarmItems = size;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position, isEditing);
    }

    @Override
    public int getItemCount() {
        return mAlarmItems;
    }


}
