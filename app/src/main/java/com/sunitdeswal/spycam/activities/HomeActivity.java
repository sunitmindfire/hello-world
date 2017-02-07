package com.sunitdeswal.spycam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.services.BackGroundVideoRecorderService;
import com.sunitdeswal.spycam.utility.SpyCamUtility;


/**
 * Home Screen of SpyCam from where user can start and stop video
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        initializeViews();

        //attach Navigation Drawer to action bar
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_home);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(drawerLayout, actionBarDrawerToggle);

    }

    private void initializeViews() {
        ImageView startRecordingImage = (ImageView) findViewById(R.id.imageView_start);
        ImageView stopRecordingImage = (ImageView) findViewById(R.id.imageView_stop);

        startRecordingImage.setOnClickListener(this);
        stopRecordingImage.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageView_start:
                //start a service to record video in background
                if (SpyCamUtility.hasCamera(this)) {
                    Intent intent = new Intent(this, BackGroundVideoRecorderService.class);
                    startService(intent);
                }
                break;

            case R.id.imageView_stop:
                //stop the background service
                Intent i = new Intent(this, BackGroundVideoRecorderService.class);
                stopService(i);
                break;

            default:
                break;
        }

    }

}
