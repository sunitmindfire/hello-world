package com.sunitdeswal.spycam.utils.preferenceUtility;

import android.content.Context;
import android.content.SharedPreferences;

import com.sunitdeswal.spycam.constants.SpyCamConstants;

/**
 * Created by Sunit deswal on 2/9/2017.
 */

public class SpyCamPreferenceUtility {


    private static final String SIZE_SPINNER_STATE ="SIZE_SPINNER_SELECTION" ;
    private static final String MAX_DURATION = "MAX DURATION" ;
    private static final String MAX_SIZE = "MAX_SIZE";
    private Context mContext;

    // Shared Preferences
    private final SharedPreferences mPref;

    // Editor for Shared preferences
    private final SharedPreferences.Editor mEditor;

    // Shared preferences file name
    private static final String PREF_NAME = "SPYCAM SETTING PREFERENCE";

    private static final String STORAGE_PATH = "STORAGE PATH";
    private static final String PREFERRED_CAMERA = "PREFERRED CAMERA";
    private static final String CAMERA_PREVIEW = "CAMERA PREVIEW";
    private static final String PREFERRED_QUALITY = "CAMERA QUALITY" ;
    private static final String PREVIEW_WIDTH = "PREVIEW WIDTH";
    private static final String PREVIEW_HEIGHT = "PREVIEW HEIGHT";


    public SpyCamPreferenceUtility(Context context) {
        int PRIVATE_MODE = 0;
        mContext = context;
        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
        mEditor.commit();
    }


    /**
     * @return : return storage path
     */
    public String getStorageLocation() {
        return mPref.getString(STORAGE_PATH, SpyCamConstants.sStoragePath);
    }



    /**
     * set the path where to store the video
     *
     * @param path : storage path
     */
    public void setStorageLocation(String path) {
        mEditor.putString(STORAGE_PATH, path);
        mEditor.commit();
    }



    /**
     * @return : return preferred camera
     */
    public int getPreferredCamera() {
        return mPref.getInt(PREFERRED_CAMERA,0);
    }


    /**
     * set the preferred camera to record video
     *
     * @param camera : storage path
     */
    public void setPreferredCamera(int camera) {
        mEditor.putInt(PREFERRED_CAMERA, camera);
        mEditor.commit();
    }


    public boolean getIsCameraPreviewEnabled(){
        return mPref.getBoolean(CAMERA_PREVIEW,false);
    }


    public void setIsCameraPreviewEnabled(boolean enabled){
        mEditor.putBoolean(CAMERA_PREVIEW,enabled);
        mEditor.commit();
    }

    public int getPreferredCameraQuality(){
        return mPref.getInt(PREFERRED_QUALITY,0);
    }

    public void setPreferredCameraQuality(int quality) {
        mEditor.putInt(PREFERRED_QUALITY, quality);
        mEditor.commit();
    }


    public int getMaxDuration(){
        return mPref.getInt(MAX_DURATION,0);
    }

    public void setMaxDuration(int quality) {
        mEditor.putInt(MAX_DURATION, quality);
        mEditor.commit();
    }

    public int getMaxSize(){
        return mPref.getInt(MAX_SIZE,0);
    }

    public void setMaxSize(int quality) {
        mEditor.putInt(MAX_SIZE, quality);
        mEditor.commit();
    }


    public int getPreviewWidth(){
        return mPref.getInt(PREVIEW_WIDTH,1);
    }

    public void setPreviewWidth(int width) {
        mEditor.putInt(PREVIEW_WIDTH, width);
        mEditor.commit();
    }

    public int getPreviewHeight(){
        return mPref.getInt(PREVIEW_HEIGHT,1);
    }

    public void setPreviewHeight(int height) {
        mEditor.putInt(PREVIEW_HEIGHT, height);
        mEditor.commit();
    }

    public int getPreviewSizeSpinnerSelection() {
        return mPref.getInt(SIZE_SPINNER_STATE,0);
    }

    public void setPreviewSizeSpinnerState(int iPosition){
        mEditor.putInt(SIZE_SPINNER_STATE,iPosition);
        mEditor.commit();
    }







}
