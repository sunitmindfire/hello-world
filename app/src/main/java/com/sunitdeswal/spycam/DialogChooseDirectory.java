package com.sunitdeswal.spycam;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sunitdeswal.spycam.adapters.DirectoryAdapter;
import com.sunitdeswal.spycam.utils.DialogUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class show a dialog to select directory for storage.
 * <p>
 * Created by Sunit deswal on 3/6/2017.
 */
public class DialogChooseDirectory implements AdapterView.OnItemClickListener {


    private ArrayList<File> mEntries = new ArrayList<File>();
    private File mCurrentDir;
    private Context mContext;
    private DirectoryAdapter mAdapter;
    private OnFolderSelectedListener mFolderListener = null;


    public DialogChooseDirectory(Context iContext, OnFolderSelectedListener iFolderSelectedListener, String iCurrentDirectory) {
        mContext = iContext;
        mFolderListener = iFolderSelectedListener;

        mCurrentDir = (iCurrentDirectory != null) ? new File(iCurrentDirectory) : Environment.getExternalStorageDirectory();

        listDirectories();

        mAdapter = new DirectoryAdapter(iContext, mEntries);

        View dialogView = ((LayoutInflater) iContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_layout, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.dialog_list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        DialogUtility.showDialog(iContext, R.string.select_folder, R.string.ok, R.string.cancel,
                dialogView, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (mFolderListener != null)
                                    mFolderListener.onChooseDirectory(mCurrentDir.getAbsolutePath());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
    }


    private void listDirectories() {

        //delete previous entries
        mEntries.clear();

        // Get all files in Current directory
        File[] files = mCurrentDir.listFiles();

        // show back option for parent folder
        if (mCurrentDir.getParent() != null)
            mEntries.add(new File(mContext.getString(R.string.empty_directory)));

        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory())
                    continue;

                mEntries.add(file);
            }
        }

        //Sort the list in ascending order
        Collections.sort(mEntries, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View list, int iPosition, long id) {

        if (iPosition < 0 || iPosition >= mEntries.size())
            return;

        mCurrentDir = (mEntries.get(iPosition).getName().equals(mContext.getString(R.string.empty_directory))) ?
                mCurrentDir.getParentFile()
                : mEntries.get(iPosition);

        listDirectories();

        mAdapter.notifyDataSetChanged();

    }

   public interface OnFolderSelectedListener {
        void onChooseDirectory(String dir);
    }


}