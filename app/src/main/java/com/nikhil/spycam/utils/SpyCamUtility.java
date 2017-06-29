package com.nikhil.spycam.utils;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.google.android.gms.ads.AdRequest;
import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class contain the methods which are common to application
 * <p>
 * Created by Sunit Deswal on 12/12/2016.
 */

public class SpyCamUtility {


    /**
     * @param iFragment            :fragment to be inflated
     * @param iFragmentManager     :fragmentManager to begin transaction
     * @param iFragmentContainerId :fragment container
     * @param isAddToBackStack     :to add fragment transaction to backStack
     * @param isReplace            :replace fragment
     * @param tag                  :fragment tag
     * @return :instance of inflated fragment
     */
    public static Fragment inflateFragment(Fragment iFragment, FragmentManager iFragmentManager, int iFragmentContainerId, boolean isAddToBackStack, boolean isReplace, String tag) {
        if (iFragment != null && iFragmentManager != null) {
            FragmentTransaction transaction = iFragmentManager.beginTransaction();

            //replace or add Fragment
            if (isReplace)
                transaction.replace(iFragmentContainerId, iFragment, tag);
            else
                transaction.add(iFragmentContainerId, iFragment, tag);

            //add fragment to backStack
            if (isAddToBackStack)
                transaction.addToBackStack(tag);

            transaction.commit();
        }
        return iFragment;
    }


    /**
     * attach navigation drawer to action bar
     *
     * @param drawerLayout          :instance of navigation drawer
     * @param actionBarDrawerToggle : instance of actionBarDrawerToggle
     */
    public static void setNavigationDrawerToActionBar(DrawerLayout drawerLayout, ActionBarDrawerToggle actionBarDrawerToggle) {
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(Context context) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(new SpyCamPreferenceUtility(context).getStorageLocation(), context.getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                MyLog.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat(SpyCamConstants.DATE_FORMAT, Locale.getDefault()).format(new Date());

        MyLog.e("file", new File(mediaStorageDir.getPath()).list().toString());

        return new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");

    }


    /**
     * This method add the files to MediaStore
     *
     * @param iContext     :application context
     * @param iFilesToScan :files which need to be added into MediaStore
     * @param iMimiTypes   :required Mimi type of files
     * @param iListener    :listener for when file is added to mediaStore
     */
    public static void startMediaScanner(Context iContext, String[] iFilesToScan, String[] iMimiTypes,
                                         MediaScannerConnection.OnScanCompletedListener iListener) {
        MediaScannerConnection.scanFile(iContext, iFilesToScan, iMimiTypes, iListener);
    }


    /**
     * This method convert millisecond int minute and seconds
     *
     * @param videoDuration : milliseceonds
     * @return : time in  minute and seconds
     */
    public static String getTimeFromMilliSecond(int videoDuration) {
        int minute;
        int second;

        second = videoDuration / 1000;
        minute = second / 60;
        second = second % 60;

        return String.format(Locale.getDefault(), "%02d", minute) + " : " + String.format(Locale.getDefault(), "%02d", second);
    }


    /**
     * This method configure the media recorder
     *
     * @param iMediaRecorder    : require media recorder
     * @param iCamera           : device camera to record video
     * @param iAudioSource      : audio source for media recorder
     * @param iVideoSource      : video source for media recorder
     * @param iCamcorderProfile : required profile
     * @param iFilePath         :  path of directory where file will be saved
     */
    public static void configureMediaRecorder(MediaRecorder iMediaRecorder, Camera iCamera, int iAudioSource, int iVideoSource,
                                              CamcorderProfile iCamcorderProfile, String iFilePath) {
        iMediaRecorder.setCamera(iCamera);
        iMediaRecorder.setAudioSource(iAudioSource);
        iMediaRecorder.setVideoSource(iVideoSource);
        iMediaRecorder.setProfile(iCamcorderProfile);
        iMediaRecorder.setOutputFile(iFilePath);
    }


    /**
     * This method convert bytes into mega Bytes (MB)
     *
     * @param iBytes : required input
     * @return : string value of MegaByte
     */
    public static String convertByteToMegaByte(int iBytes) {

        return String.format(Locale.US, "%.2f", iBytes / (1024.0 * 1024.0));
    }


    /**
     * This method return adRequest
     *
     * @return : instance of AdRequest
     */
    public static AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .build();
    }


}
