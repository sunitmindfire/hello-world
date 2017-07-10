package com.nikhil.spycam.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nikhil.spycam.R;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;

import java.io.File;

/**
 * Created by Sunit deswal on 2/13/2017.
 */

public class VideoAdapter extends CursorAdapter {

    private Context mContext;
    private final Cursor mMediaCursor;
    private LayoutInflater mLayoutInflater;
    private SparseBooleanArray mSelection = new SparseBooleanArray();

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

        int bgColor =  (mSelection.get(cursor.getPosition())) ? ContextCompat.getColor(mContext,R.color.colorGreySelection)
                :ContextCompat.getColor(mContext,R.color.colorWhite);

        view.setBackgroundColor(bgColor);

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

        String videoSize = mMediaCursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        try {
            videoDuration = SpyCamUtility.getTimeFromMilliSecond(Integer.parseInt(videoDuration));
            videoSize = SpyCamUtility.convertByteToMegaByte(Integer.parseInt(videoSize)) + mContext.getString(R.string.unit_mb);

        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }

        videoTitleTextView.setText(title);
        durationTextView.setText(videoDuration);
        sizeTextView.setText(videoSize);
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        MyLog.e("selection","" + mSelection.size());
        notifyDataSetChanged();
    }

    public void removeSelection(int position) {
        mSelection.delete(position);
        MyLog.e("selection","" + mSelection.size());
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new SparseBooleanArray();
        MyLog.e("selection","" + mSelection.size());
        notifyDataSetChanged();
    }

    public void deleteSelectedItems(){
        String path[] = new String[mSelection.size()];
        for(int i=0;i<mSelection.size();++i){
            int key = mSelection.keyAt(i);
                 if(mSelection.get(key)) {
                     mMediaCursor.moveToPosition(key);
                     path[i] = mMediaCursor.getString(mMediaCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                     MyLog.e("path delete", path[i]);
                     new File(path[i]).delete();
                 }
        }

        SpyCamUtility.startMediaScanner(mContext,path,null,new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                MyLog.v("scanner", "file " + path + " was scanned seccessfully: " + uri);
            }
        });
        MyLog.e("selection","" + mSelection.size());
        notifyDataSetChanged();
    }

}



