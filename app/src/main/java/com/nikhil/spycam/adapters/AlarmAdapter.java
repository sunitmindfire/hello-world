package com.nikhil.spycam.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nikhil.spycam.R;
import com.nikhil.spycam.database.SpyCamDBHelper;
import com.nikhil.spycam.services.BackGroundVideoRecorderService;
import com.nikhil.spycam.utils.AlarmManagerUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;

/**
 * Adapter class for alarm list
 */

public class AlarmAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AlarmAdapter(Context context, Cursor c, LayoutInflater layoutInflater) {
        super(context, c, false);
        mLayoutInflater = layoutInflater;
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        MyLog.e("alrm_adapter:", "newView  :" + cursor.getPosition());
        View rootView = mLayoutInflater.inflate(R.layout.alarm_list_layout, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.mDateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        holder.mTimeTextView = (TextView) rootView.findViewById(R.id.timeTextView);
        holder.mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        holder.mCancelAlarm = (Button) rootView.findViewById(R.id.cancel_alarm);
        rootView.setTag(holder);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        MyLog.e("alrm_adapter:", "total rows :" + cursor.getCount());
        MyLog.e("alrm_adapter:", "bindView :" + cursor.getPosition());
        viewHolder.mCancelAlarm.setTag(cursor.getPosition());
        viewHolder.mCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e("alrm_adapter:", "deleted row :" + cursor.getPosition());
                cursor.moveToPosition((int)v.getTag());
                cancelAlarm((int) cursor.getLong(4));
                deleteEntryFromDB(cursor.getLong(4));
            }
        });

        viewHolder.mDateTextView.setText(cursor.getString(1));
        viewHolder.mTimeTextView.setText(cursor.getString(2));
        viewHolder.mDurationTextView.setText(cursor.getInt(3) + "");
    }

    private void deleteEntryFromDB(long aLong) {
        SpyCamDBHelper.deleteAlarm(mContext, aLong);
        notifyDataSetChanged();
    }

    private void cancelAlarm(int aLong) {
        PendingIntent pi = PendingIntent.getService(mContext, aLong, new Intent(mContext, BackGroundVideoRecorderService.class), 0);
        AlarmManagerUtility.getAlarmManager(mContext).cancel(pi);
    }

    private class ViewHolder {
        TextView mDateTextView;
        TextView mTimeTextView;
        TextView mDurationTextView;
        Button mCancelAlarm;
    }
}
