package com.nikhil.spycam.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhil.spycam.R;
import com.nikhil.spycam.adapters.VideoAdapter;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.fragments.NavigationDrawerFragment;
import com.nikhil.spycam.utils.DialogUtility;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.toastUtility.MyToast;

/**
 * This displays the all videos recorded by the user
 */
public class MyVideosActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private Cursor mMediaCursor;
    private DrawerLayout mDrawerLayout;
    private VideoAdapter mVideoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        //inflate Navigation Drawer
        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(), getSupportFragmentManager(), R.id.navigation_drawer_container,
                false, false, getString(R.string.navigation_fragment_tag));

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        //set title of toolbar
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.my_videos);

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.myVideo_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);

        //required media columns
        String[] mediaColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.MIME_TYPE};


        //<------- query to show video from specific folder("SpyCam") ------------------>
        String selectionMedia = String.format(getString(R.string.selection_query),MediaStore.Video.Media.DATA);
        String[] selectionArgs = new String[]{getString(R.string.query_argument)};


        //<------------ get video data  stored in external storage in specific folder("SpyCam") ----------------------->
        mMediaCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, selectionMedia, selectionArgs, null);


        MyLog.e("adapter", "" + MediaStore.Video.Media.EXTERNAL_CONTENT_URI + "and" + MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        MyLog.e("adapter cursor", "" + mMediaCursor.getCount());

        initializeListView();

    }

    private void initializeListView() {

        ListView listView = (ListView) findViewById(R.id.videoListView);
        listView.setOnItemClickListener(this);

        mVideoAdapter = new VideoAdapter(this, mMediaCursor, getLayoutInflater(), true);
        listView.setAdapter(mVideoAdapter);

        //set listView in multiple item selection mode
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            //to track of number of item selected in listView
            int itemSelected = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int iPosition, long l, boolean isChecked) {
                if (isChecked) {
                    itemSelected++;
                    mVideoAdapter.setNewSelection(iPosition, true);
                } else {
                    itemSelected--;
                    mVideoAdapter.removeSelection(iPosition);
                }
                MyLog.e("selected", itemSelected + getString(R.string.slash) + mMediaCursor.getCount() + getString(R.string.selected));
                actionMode.setTitle(itemSelected + getString(R.string.slash) + mMediaCursor.getCount() + getString(R.string.selected));

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.toolbar_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        showDeleteDialog();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mVideoAdapter.clearSelection();
                itemSelected = 0;
            }
        });
    }

    /**
     * This method show the alert dialog with delete message
     */
    private void showDeleteDialog() {
        DialogUtility.showDialog(this, R.string.deleteDialogMessage, R.string.yes, R.string.no, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mVideoAdapter.deleteSelectedItems();
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mMediaCursor.close();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
        //get the path of selected video
        mMediaCursor.moveToPosition(iPosition);
        String path = mMediaCursor.getString(mMediaCursor.getColumnIndex(MediaStore.Video.Media.DATA));
        MyLog.e("adapter click pos ", "" + mMediaCursor.getPosition() + iPosition);
        MyLog.e("adapter click path", path);

        //start video in any available player
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), SpyCamConstants.DATA_TYPE);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
            MyToast.makeText(this,"No app found to play video.Install a video player from play store.",Toast.LENGTH_LONG);
        }

    }


    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}

