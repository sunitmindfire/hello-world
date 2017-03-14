package com.sunitdeswal.spycam.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sunitdeswal.spycam.DialogChooseDirectory;
import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.fragments.NavigationDrawerFragment;
import com.sunitdeswal.spycam.utils.DialogUtility;
import com.sunitdeswal.spycam.utils.SpyCamUtility;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;
import com.sunitdeswal.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

/**
 * user can change app setting from this screen
 */
public class SettingActivity extends MyBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,DialogChooseDirectory.OnFolderSelectedListener,
        CompoundButton.OnCheckedChangeListener,NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private SpyCamPreferenceUtility mPreferenceUtility;
    private AppCompatSpinner mPreviewSizeSpinner;
    private SwitchCompat mCameraPreviewSwitch;
    private AppCompatSpinner mQualitySpinner;
    private AppCompatSpinner mCameraSpinner;
    private AppCompatSpinner mMaxDurationSpinner;
    private AppCompatSpinner mMaxSizeSpinner;
    private TextView mStoragePathTextView;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.setting_drawer);

        //set toolbar title
        ((TextView)findViewById(R.id.toolbar_textView)).setText(R.string.settings);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);


        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(),getSupportFragmentManager(),R.id.navigation_drawer_container,
                false,false,getString(R.string.navigation_fragment_tag));

        mPreferenceUtility = new SpyCamPreferenceUtility(this);

        mStoragePathTextView = (TextView) findViewById(R.id.storageTextView);
        mStoragePathTextView.setText(mPreferenceUtility.getStorageLocation());
        mStoragePathTextView.setOnClickListener(this);

        mCameraPreviewSwitch = (SwitchCompat) findViewById(R.id.cameraPreviewSwitch);
        mCameraPreviewSwitch.setOnCheckedChangeListener(this);

        mCameraSpinner = (AppCompatSpinner) findViewById(R.id.cameraSpinner);
        setSpinnerArrayData(R.array.array_camera_type, mCameraSpinner);

        mQualitySpinner = (AppCompatSpinner) findViewById(R.id.qualitySpinner);
//        setSpinnerArrayData(R.array.camera_quality, mQualitySpinner);
        if(mPreferenceUtility.getPreferredCamera() == 0)
        mQualitySpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,SpyCamConstants.BACK_CAMERA_SUPPORTED_QUALITY));
        else
            mQualitySpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,SpyCamConstants.FRONT_CAMERA_SUPPORTED_QUALITY));
        mQualitySpinner.setOnItemSelectedListener(this);

        mPreviewSizeSpinner = (AppCompatSpinner) findViewById(R.id.previewSizeSpinner);
        setSpinnerArrayData(R.array.preview_size,mPreviewSizeSpinner);

        mMaxDurationSpinner = (AppCompatSpinner) findViewById(R.id.timeSpinner);
        setSpinnerArrayData(R.array.max_duration,mMaxDurationSpinner);

        mMaxSizeSpinner = (AppCompatSpinner)findViewById(R.id.videoSizeSpinner);
        setSpinnerArrayData(R.array.max_video_size,mMaxSizeSpinner);

        (findViewById(R.id.resetButton)).setOnClickListener(this);

        restoreCurrentStateOfView();
    }


    /**
     * This method restore the current state setting screen.
     */
    private void restoreCurrentStateOfView() {

        mCameraSpinner.setSelection(mPreferenceUtility.getPreferredCamera());

        mQualitySpinner.setSelection(mPreferenceUtility.getPreferredCameraQuality());

        mCameraPreviewSwitch.setChecked(mPreferenceUtility.getIsCameraPreviewEnabled());

        if(mPreferenceUtility.getIsCameraPreviewEnabled())
            mPreviewSizeSpinner.setVisibility(View.VISIBLE);
        else
            mPreviewSizeSpinner.setVisibility(View.INVISIBLE);

        mPreviewSizeSpinner.setSelection(mPreferenceUtility.getPreviewSizeSpinnerSelection());

        mMaxSizeSpinner.setSelection(mPreferenceUtility.getMaxSize());

        mMaxDurationSpinner.setSelection(mPreferenceUtility.getMaxDuration());

        mStoragePathTextView.setText(mPreferenceUtility.getStorageLocation());
    }


    /**
     * set the data into spinner
     *
     * @param array   :array for spinner
     * @param spinner :required spinner
     */
    private void setSpinnerArrayData(int array, Spinner spinner) {
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int iPosition, long l) {

        switch (adapterView.getId()) {

            case R.id.cameraSpinner:
                mPreferenceUtility.setPreferredCamera(iPosition);
                break;

            case R.id.qualitySpinner:
                mPreferenceUtility.setPreferredCameraQuality(iPosition);
                break;

            case R.id.previewSizeSpinner:
                if(mCameraPreviewSwitch.isChecked()){
                int size = getPreviewSize(iPosition);
                mPreferenceUtility.setPreviewWidth(size);
                mPreferenceUtility.setPreviewHeight(size);
                mPreferenceUtility.setPreviewSizeSpinnerState(iPosition);
                    MyLog.e("spinner","" + mPreferenceUtility.getPreviewSizeSpinnerSelection());
                }
                break;

            case R.id.videoSizeSpinner:
                mPreferenceUtility.setMaxSize(iPosition);
                break;

            case R.id.timeSpinner:
                mPreferenceUtility.setMaxDuration(iPosition);
        }
    }

    private int getPreviewSize(int iPosition) {
        int previewSize =1;

        switch (iPosition){
            case 0:
                previewSize = SpyCamConstants.PREVIEW_HEIGHT_SMALL;
                break;
            case 1:
                previewSize = SpyCamConstants.PREVIEW_HEIGHT_MEDIUM;
                break;
            case 2:
                previewSize = SpyCamConstants.PREVIEW_HEIGHT_LARGE;
                break;
        }
        return previewSize;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        Log.e("switch", "" + isChecked);

        if (!isChecked) {
            setPreviewDimension(SpyCamConstants.NO_PREVIEW_WIDTH, SpyCamConstants.NO_PREVIEW_HEIGHT);
            mPreviewSizeSpinner.setVisibility(View.INVISIBLE);
        }
        else {
            setPreviewDimension(SpyCamConstants.PREVIEW_WIDTH_SMALL, SpyCamConstants.PREVIEW_HEIGHT_SMALL);
            mPreviewSizeSpinner.setVisibility(View.VISIBLE);
        }


        mPreferenceUtility.setIsCameraPreviewEnabled(isChecked);
        Log.e("switch", "" + mPreferenceUtility.getIsCameraPreviewEnabled());
    }


    private void setPreviewDimension(int width, int height) {
        mPreferenceUtility.setPreviewWidth(width);
        mPreferenceUtility.setPreviewHeight(height);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.storageTextView:
                chooseStoragePath();
                break;

            case R.id.resetButton:
                showResetDialog();
                break;
        }
    }

    private void showResetDialog() {
        DialogUtility.showDialog(this,R.string.reset_message,R.string.reset,R.string.cancel,null,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int iWhich) {
                switch (iWhich){
                    case DialogInterface.BUTTON_POSITIVE:
                        resetValues();
                        break;
                }
            }
        });
    }


    private void resetValues() {
        mStoragePathTextView.setText(SpyCamConstants.sFileStoragePath);
        mPreferenceUtility.setStorageLocation(SpyCamConstants.sStoragePath);

        mCameraSpinner.setSelection(0);
        mQualitySpinner.setSelection(0);

        mCameraPreviewSwitch.setChecked(false);

        mPreviewSizeSpinner.setSelection(0);
        mMaxDurationSpinner.setSelection(0);
        mMaxSizeSpinner.setSelection(0);

    }

    private void chooseStoragePath() {
        new DialogChooseDirectory(this,this,mStoragePathTextView.getText().toString());
    }


    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onChooseDirectory(String dir) {
        MyLog.e("folder path",dir);
        mStoragePathTextView.setText(dir);
        mPreferenceUtility.setStorageLocation(dir);
    }
}
