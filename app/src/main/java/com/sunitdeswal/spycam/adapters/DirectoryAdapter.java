package com.sunitdeswal.spycam.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunit deswal on 3/6/2017.
 */

public class DirectoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<File> mEntries;

    public DirectoryAdapter(Context iContext,ArrayList<File> iEntries){
        mContext = iContext;
        mEntries = iEntries;
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int i) {
        return mEntries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int iPosition, View iConvertView, ViewGroup parent)
    {   ViewHolder viewHolder;
        if(iConvertView==null){
            MyLog.e("getviw","convet view null");
            viewHolder = new ViewHolder();
            iConvertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_list_layout,null);
            viewHolder.mDirectoryTextView = (TextView)iConvertView.findViewById(R.id.directoryTextView);
            iConvertView.setTag(viewHolder);
        }
        else {
            MyLog.e("getviw","convet view not null");
            viewHolder = (ViewHolder)iConvertView.getTag();
        }

       if (mEntries.get( iPosition ).getName().equals( mContext.getString(R.string.empty_directory) ) ) {
            viewHolder.mDirectoryTextView.setText(mContext.getString(R.string.empty_directory));
            viewHolder.mDirectoryTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext,R.drawable.folderback), null, null, null );
        }
        else
        {
            viewHolder.mDirectoryTextView.setText( mEntries.get(iPosition).getName() );
            viewHolder.mDirectoryTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext,R.drawable.folder ), null, null, null );
        }

        return iConvertView;
    }

    private class ViewHolder{
        TextView mDirectoryTextView;
    }

}
