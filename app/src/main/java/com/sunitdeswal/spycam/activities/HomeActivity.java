package com.sunitdeswal.spycam.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.fragments.NavigationDrawerFragment;
import com.sunitdeswal.spycam.services.BackGroundVideoRecorderService;
import com.sunitdeswal.spycam.utils.CameraUtility;
import com.sunitdeswal.spycam.utils.DialogUtility;
import com.sunitdeswal.spycam.utils.SpyCamUtility;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;

import java.security.Permission;


/**
 * Home Screen of SpyCam from where user can start and stop video
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener,NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        initializeViews();

        Log.e("permission",Build.VERSION.SDK_INT + " " + Build.VERSION_CODES.M );
        //check permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        checkAndRequestPermissions();

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);

        //add Navigation View
        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(), getSupportFragmentManager(), R.id.navigation_view_container, false, false,getString(R.string.navigation_fragment_tag));

//         Check if Android M or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            if (!Settings.canDrawOverlays(this)) {
                MyLog.e("draw over other","false"  );
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                myIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(myIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Request run time permission for devices having Marshmallow or above
     */
    private void checkAndRequestPermissions() {
        String permissions[]= {Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_CODE);
                MyLog.e("permissions","in permission check");
            }
        }

    }


    /**
     * initialize view of activity
     */
    private void initializeViews() {
        findViewById(R.id.imageView_start).setOnClickListener(this);
        findViewById(R.id.imageView_stop).setOnClickListener(this);
//        ((NavigationView)findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(this);
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.app_name);
    }




    @Override
    public void onClick(View view) {

        View rootView = findViewById(R.id.rootView);
        switch (view.getId()) {

            case R.id.imageView_start:
                //start a service to record video in background
                if (CameraUtility.hasCamera(this)) {
                    changeBackgroundColor(R.color.colorWhite, rootView);
                    Intent intent = new Intent(this, BackGroundVideoRecorderService.class);
                    startService(intent);
                }
                break;

            case R.id.imageView_stop:
                changeBackgroundColor(R.color.colorBlack, rootView);
                //stop the background service
                Intent i = new Intent(this, BackGroundVideoRecorderService.class);
                stopService(i);
                break;

            default:
                break;
        }

    }


    private void changeBackgroundColor(int colorId, View rootView) {
        rootView.setBackgroundColor(ContextCompat.getColor(this, colorId));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}
