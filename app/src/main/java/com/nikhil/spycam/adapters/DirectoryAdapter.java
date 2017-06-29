package com.nikhil.spycam.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nikhil.spycam.R;
import com.nikhil.spycam.utils.logUtility.MyLog;

import java.io.File;
import java.util.ArrayList;

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
    public View getView(int iPosition, View iConvertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(iConvertView==null){
            MyLog.e("getviw","convet view null");
            viewHolder = new ViewHolder();
            iConvertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.dialog_list_layout,null);
            viewHolder.mDirectoryTextView = (TextView)iConvertView.findViewById(R.id.directoryTextView);
            iConvertView.setTag(viewHolder);
        }
        else {
            MyLog.e("getviw","convet view not null");
            viewHolder = (ViewHolder)iConvertView.getTag();
        }


        if (mEntries.get( iPosition ).getName().equals( mContext.getString(R.string.empty_directory) ) ) {
              setViewHolder(viewHolder,mContext.getString(R.string.empty_directory),R.drawable.folderback);
       } else
        {
            setViewHolder(viewHolder,mEntries.get(iPosition).getName(),R.drawable.folder);
        }

        return iConvertView;
    }

    private void setViewHolder(ViewHolder viewHolder,String iDirectoryName,int iDirectoryImage){
        viewHolder.mDirectoryTextView.setText(iDirectoryName);
        viewHolder.mDirectoryTextView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(mContext,iDirectoryImage), null, null, null );
    }

    private class ViewHolder{
        TextView mDirectoryTextView;
    }

}
