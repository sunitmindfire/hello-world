package com.sunitdeswal.spycam.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.adapters.VideoAdapter;
import com.sunitdeswal.spycam.fragments.NavigationDrawerFragment;
import com.sunitdeswal.spycam.utils.SpyCamUtility;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;

public class MyVideosActivity extends MyBaseActivity implements AdapterView.OnItemClickListener,NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private Cursor mMediaCursor;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(),getSupportFragmentManager(),R.id.navigation_drawer_container,
                false,false,getString(R.string.navigation_fragment_tag));

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set title of toolbar
        ((TextView)findViewById(R.id.toolbar_textView)).setText(R.string.my_videos);

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.myVideo_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);


        ListView listView = (ListView) findViewById(R.id.videoListView);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                getMenuInflater().inflate(R.menu.toolbar_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });


        String[] mediaColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.MIME_TYPE};


        //<------- query to show video from specific folder("SpyCam") ------------------>
        String selectionMedia = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%SpyCam%"};


        //<------------ get video data  stored in external storage in specific folder("SpyCam") ----------------------->
        mMediaCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, selectionMedia, selectionArgs, null);


        MyLog.e("adapter", "" + MediaStore.Video.Media.EXTERNAL_CONTENT_URI + "and" + MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        MyLog.e("adapter cursor", "" + mMediaCursor.getCount());

        VideoAdapter videoAdapter = new VideoAdapter(this, mMediaCursor, getLayoutInflater(), true);
        listView.setAdapter(videoAdapter);
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
        mMediaCursor.moveToPosition(iPosition);
        String path = mMediaCursor.getString(mMediaCursor.getColumnIndex(MediaStore.Video.Media.DATA));
        MyLog.e("adapter click pos ", "" + mMediaCursor.getPosition() + iPosition);
        MyLog.e("adapter click path", path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "video/mp4");
        startActivity(intent);
    }


    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


}

