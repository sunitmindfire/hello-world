package com.sunitdeswal.spycam.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.utils.SpyCamUtility;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;

import java.io.File;

/**
 * Created by Sunit deswal on 2/13/2017.
 */

public class VideoAdapter extends CursorAdapter {

    private Context mContext;
    private final Cursor mMediaCursor;
    private LayoutInflater mLayoutInflater;

    public VideoAdapter(Context iContext, Cursor iMediaCursor, LayoutInflater iLayoutInflater, boolean iAutoRequery) {
        super(iContext, iMediaCursor, iAutoRequery);
        mContext = iContext;
        mMediaCursor = iMediaCursor;
        mLayoutInflater = iLayoutInflater;
        MyLog.e("adapter", "constructor" + " " + mMediaCursor.getColumnName(0));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        MyLog.e("adapter", "new view");
        return mLayoutInflater.inflate(R.layout.video_list_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MyLog.e("adapter", "bind view" + cursor.getPosition());
        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnailImage);
        TextView videoTitleTextView = (TextView) view.findViewById(R.id.videoTittle);
        TextView durationTextView  =(TextView)view.findViewById(R.id.time_textView);
        TextView sizeTextView = (TextView)view.findViewById(R.id.size_textView);
        CheckBox checkBox = (CheckBox)view.findViewById(R.id.listCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MyLog.e("adapter checked position",":" +mMediaCursor.getPosition());
            }
        });

        try {

            String path = mMediaCursor.getString(mMediaCursor.getColumnIndex(MediaStore.Video.Media.DATA));

            MyLog.e("adapter path video ", path);

            Glide.with(mContext)
                    .load(Uri.fromFile(new File(path)))
                    .asBitmap()
                    .into(thumbnailView);
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        String title = mMediaCursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        Log.e("adapter title", title);

        String videoDuration = mMediaCursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

        videoDuration = SpyCamUtility.getTimeFromMilliSecond(Integer.parseInt(videoDuration));

        String videoSize = mMediaCursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));

        videoSize = SpyCamUtility.convertByteToMegaByte(Integer.parseInt(videoSize)) + mContext.getString(R.string.unit_mb);

        videoTitleTextView.setText(title);

        durationTextView.setText(videoDuration);

        sizeTextView.setText(videoSize);
    }


}



