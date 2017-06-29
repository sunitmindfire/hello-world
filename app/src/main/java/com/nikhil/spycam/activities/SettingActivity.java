package com.nikhil.spycam.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nikhil.spycam.DialogChooseDirectory;
import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.fragments.NavigationDrawerFragment;
import com.nikhil.spycam.utils.DialogUtility;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

/**
 * user can change app setting from this screen
 */
public class SettingActivity extends MyBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogChooseDirectory.OnFolderSelectedListener,
        CompoundButton.OnCheckedChangeListener, NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private SpyCamPreferenceUtility mPreferenceUtility;
    private AppCompatSpinner mPreviewSizeSpinner;
    private AppCompatSpinner mMaxDurationSpinner;
    private SwitchCompat mCameraPreviewSwitch;
    private AppCompatSpinner mMaxSizeSpinner;
    private AppCompatSpinner mQualitySpinner;
    private AppCompatSpinner mCameraSpinner;
    private InterstitialAd mInterstitialAd;
    private TextView mStoragePathTextView;
    private DrawerLayout mDrawerLayout;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        //set toolbar title
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.settings);

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.setting_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);

        //inflate Navigation Drawer
        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(), getSupportFragmentManager(), R.id.navigation_drawer_container,
                false, false, getString(R.string.navigation_fragment_tag));

        mPreferenceUtility = new SpyCamPreferenceUtility(this);

        initializeAds();

        initializeViews();

        restoreCurrentStateOfView();
    }


    /**
     * This method initialize all views.
     */
    private void initializeViews() {

        //initialize directory chooser textView
        mStoragePathTextView = (TextView) findViewById(R.id.storageTextView);
        mStoragePathTextView.setText(mPreferenceUtility.getStorageLocation());
        mStoragePathTextView.setOnClickListener(this);

        //initialize camera preview toggle button
        mCameraPreviewSwitch = (SwitchCompat) findViewById(R.id.cameraPreviewSwitch);
        mCameraPreviewSwitch.setOnCheckedChangeListener(this);

        //initialize all spinners
        initializeSpinners();

        //initialize reset button
        (findViewById(R.id.resetButton)).setOnClickListener(this);

    }


    /**
     * This method initialize spinners
     */
    private void initializeSpinners() {

        //initialize preferred camera spinner
        mCameraSpinner = (AppCompatSpinner) findViewById(R.id.cameraSpinner);
        setSpinnerArrayData(R.array.array_camera_type, mCameraSpinner);

        //initialize camera quality spinner
        mQualitySpinner = (AppCompatSpinner) findViewById(R.id.qualitySpinner);
        mQualitySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                (mPreferenceUtility.getPreferredCamera() == 0) ? SpyCamConstants.FRONT_CAMERA_SUPPORTED_QUALITY : SpyCamConstants.BACK_CAMERA_SUPPORTED_QUALITY));
        mQualitySpinner.setOnItemSelectedListener(this);

        //initialize camera preview size spinner
        mPreviewSizeSpinner = (AppCompatSpinner) findViewById(R.id.previewSizeSpinner);
        setSpinnerArrayData(R.array.preview_size, mPreviewSizeSpinner);

        //initialize video time duration spinner
        mMaxDurationSpinner = (AppCompatSpinner) findViewById(R.id.timeSpinner);
        setSpinnerArrayData(R.array.max_duration, mMaxDurationSpinner);

        //initialize video size spinner
        mMaxSizeSpinner = (AppCompatSpinner) findViewById(R.id.videoSizeSpinner);
        setSpinnerArrayData(R.array.max_video_size, mMaxSizeSpinner);

    }

    /**
     * initialize Ads
     */
    private void initializeAds() {
        //initialize banner ad
        mAdView = (AdView) findViewById(R.id.adSettingView);
        mAdView.loadAd(SpyCamUtility.getAdRequest());

        //initialize full screen ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
        mInterstitialAd.loadAd(SpyCamUtility.getAdRequest());
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }


    /**
     * This method restore the current state of setting screen.
     */
    private void restoreCurrentStateOfView() {

        mCameraSpinner.setSelection(mPreferenceUtility.getPreferredCamera());
        mQualitySpinner.setSelection(mPreferenceUtility.getPreferredCameraQuality());
        mCameraPreviewSwitch.setChecked(mPreferenceUtility.getIsCameraPreviewEnabled());

        mPreviewSizeSpinner.setVisibility(mPreferenceUtility.getIsCameraPreviewEnabled() ? View.VISIBLE : View.INVISIBLE);
        mPreviewSizeSpinner.setSelection(mPreferenceUtility.getPreviewSizeSpinnerSelection());

        mMaxSizeSpinner.setSelection(mPreferenceUtility.getMaxSize());
        mMaxDurationSpinner.setSelection(mPreferenceUtility.getMaxDuration());

        mStoragePathTextView.setText(mPreferenceUtility.getStorageLocation());
    }


    /**
     * This method set the data into spinner
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
                if (mCameraPreviewSwitch.isChecked()) {
                    int size = getPreviewSize(getResources().getStringArray(R.array.preview_size)[iPosition]);
                    MyLog.e("ssd preview size","" + size);
                    mPreferenceUtility.setPreviewWidth(size);
                    mPreferenceUtility.setPreviewHeight(size);
                    mPreferenceUtility.setPreviewSizeSpinnerState(iPosition);
                    MyLog.e("spinner", "" + mPreferenceUtility.getPreviewSizeSpinnerSelection());
                }
                break;

            case R.id.videoSizeSpinner:
                mPreferenceUtility.setMaxSize(iPosition);
                break;

            case R.id.timeSpinner:
                mPreferenceUtility.setMaxDuration(iPosition);
                break;

            default:
                break;
        }
    }

    private int getPreviewSize(String size) {

        int previewSize = 1;

        MyLog.e("ssd",size);
        if (size.equals(getString(R.string.small))) {
            return SpyCamConstants.PREVIEW_HEIGHT_MEDIUM;
        }else if (size.equals(getString(R.string.medium))) {
            return  SpyCamConstants.PREVIEW_HEIGHT_MEDIUM;
        }else if (size.equals(getString(R.string.large))) {
            return  SpyCamConstants.PREVIEW_HEIGHT_LARGE;
        }

        return previewSize;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        MyLog.e("switch", "" + isChecked);

        if (!isChecked) {
            setPreviewDimension(SpyCamConstants.NO_PREVIEW_WIDTH, SpyCamConstants.NO_PREVIEW_HEIGHT);
            mPreviewSizeSpinner.setVisibility(View.INVISIBLE);
        } else {
            setPreviewDimension(SpyCamConstants.PREVIEW_WIDTH_SMALL, SpyCamConstants.PREVIEW_HEIGHT_SMALL);
            mPreviewSizeSpinner.setSelection(0);
            mPreviewSizeSpinner.setVisibility(View.VISIBLE);
        }

        mPreferenceUtility.setIsCameraPreviewEnabled(isChecked);
        MyLog.e("switch", "" + mPreferenceUtility.getIsCameraPreviewEnabled());
    }


    /**
     * This method set the width and height of camera preview
     *
     * @param width  : width of camera preview
     * @param height : height of camera preview
     */
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

    /**
     * This method generate an dialog for reset button
     */
    private void showResetDialog() {
        DialogUtility.showDialog(this, R.string.reset_message, R.string.reset, R.string.cancel, null, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int iWhich) {
                switch (iWhich) {
                    case DialogInterface.BUTTON_POSITIVE:
                        resetValues();
                        break;
                }
            }
        });
    }


    /**
     * This method restore the default values of setting screen
     */
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


    /**
     * This method creates the dialog to choose the directory
     */
    private void chooseStoragePath() {
        new DialogChooseDirectory(this, this, mStoragePathTextView.getText().toString());
    }


    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onChooseDirectory(String dir) {
        MyLog.e("folder path", dir);
        mStoragePathTextView.setText(dir);
        mPreferenceUtility.setStorageLocation(dir);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }
}
