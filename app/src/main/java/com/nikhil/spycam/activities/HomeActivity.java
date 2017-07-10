package com.nikhil.spycam.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.enums.ServiceType;
import com.nikhil.spycam.fragments.NavigationDrawerFragment;
import com.nikhil.spycam.services.BackGroundVideoRecorderService;
import com.nikhil.spycam.utils.AlarmManagerUtility;
import com.nikhil.spycam.utils.CameraUtility;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.toastUtility.MyToast;

import java.util.Calendar;


/**
 * Home Screen of SpyCam from where user can start and stop video
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    //variable to know if recording is in progress or not
    private static boolean sIsRecordingStart;

    //request code for run time permissions
    private final int PERMISSION_REQUEST_CODE = 1;

    private DrawerLayout mDrawerLayout;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disable Action Bar default Title
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        initializeViews();

        initializeAd();

        MyLog.e("permission", Build.VERSION.SDK_INT + " " + Build.VERSION_CODES.M);

        //check permissions for marshmallow and above at run time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkAndRequestPermissions();

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);

        //add Navigation View
        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(), getSupportFragmentManager(),
                R.id.navigation_view_container, false, false, getString(R.string.navigation_fragment_tag));
    }

    /**
     * initialize ad
     */
    private void initializeAd() {
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    /**
     * initialize views of activity
     */
    private void initializeViews() {

        findViewById(R.id.imageView_start).setOnClickListener(this);
        findViewById(R.id.imageView_stop).setOnClickListener(this);

        //set Text of Toolbar Tittle Text
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.app_name);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageView_start:
                //check if recording is in progress or not
                if (!sIsRecordingStart) {
                    //start a service to record video in background
                    if (CameraUtility.hasCamera(this)) {
                        Intent intent = new Intent(this, BackGroundVideoRecorderService.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(SpyCamConstants.KEY_SERVICE_TYPE,ServiceType.NORMAL);
                        intent.putExtra(SpyCamConstants.KEY_RECORDER_SERVICE,bundle);
                        startService(intent);
                        sIsRecordingStart = true;
                    }
                }
                break;

            case R.id.imageView_stop:
                //stop the background service
                Intent i = new Intent(this, BackGroundVideoRecorderService.class);
                stopService(i);
                sIsRecordingStart = false;
                break;

            default:
                break;
        }
    }


    /**
     * Request run time permission for devices having Marshmallow or above
     */
    private void checkAndRequestPermissions() {
        MyLog.e("permissions", "in permission check");

        //Required permissions
        String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                MyLog.e("permissions", "permission required");
                break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MyLog.e("permission result", "" + requestCode + ":" + grantResults.length);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < grantResults.length; ++i) {
                    MyLog.e("permission result ", "permission" + permissions[i] + ":" + grantResults[i]);

                    //close the application of any permission is not granted
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        //show custom Toast
                        View layout = getLayoutInflater().inflate(R.layout.custom_toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));
                        MyToast.makeText(this, layout, R.string.permissionDenied, Toast.LENGTH_LONG);

                        //close the application
                        finish();
                        break;
                    }
                }
        }
    }


    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }
}
