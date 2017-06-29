package com.nikhil.spycam;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nikhil.spycam.activities.HomeActivity;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

import java.util.List;

/**
 * create the camera preview and start video recording
 * <p>
 * Created by Sunit Deswal on 12/12/2016.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private Context mContext;
    public static String sLastFilePath;
    private MediaRecorder mMediaRecorder;
    private GestureDetector mGestureDetector;
    private final String mMaxAllDurations[] = getResources().getStringArray(R.array.max_duration);
    private final String mAllVideoSize[] = getResources().getStringArray(R.array.max_video_size);

    public CameraPreview(Context context, Camera camera) {
        super(context);

        MyLog.e("TAG", "cam preview constructor" + "Thread:" + Thread.currentThread().getName());

        mContext = context;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        getHolder().addCallback(this);

        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        MyLog.e("TAG", "surface created" + "Thread:" + Thread.currentThread().getName());

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setParameters(mCamera.getParameters());
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            MyLog.e("TAG", "Error setting camera preview: " + e.getMessage());
        }

        //unlock the camera for mediaRecorder
        mCamera.unlock();

        //set the video recorder
        mMediaRecorder = new MediaRecorder();

        SpyCamPreferenceUtility preferenceUtility = new SpyCamPreferenceUtility(mContext);

        //get preferred camera that is selected
        int camId = preferenceUtility.getPreferredCamera();

        MyLog.e("quality check 720", " :" + CamcorderProfile.hasProfile(camId, CamcorderProfile.QUALITY_1080P));

        int cameraQuality = (camId == 0) ? getCameraQuality(preferenceUtility, SpyCamConstants.BACK_CAMERA_SUPPORTED_QUALITY)
                : getCameraQuality(preferenceUtility, SpyCamConstants.FRONT_CAMERA_SUPPORTED_QUALITY);

        try {
            sLastFilePath = SpyCamUtility.getOutputMediaFile(getContext()).toString();

            //configure media recorder
            SpyCamUtility.configureMediaRecorder(mMediaRecorder, mCamera, MediaRecorder.AudioSource.CAMCORDER, MediaRecorder.VideoSource.CAMERA,
                    CamcorderProfile.get(camId, cameraQuality), sLastFilePath);

            //set maximum video recording time
            if (!mMaxAllDurations[preferenceUtility.getMaxDuration()].equals(getResources().getString(R.string.no_limit)))
                mMediaRecorder.setMaxDuration(getMaxDuration(preferenceUtility.getMaxDuration()));

            //set maximum video size
            if (!mAllVideoSize[preferenceUtility.getMaxSize()].equals(getResources().getString(R.string.no_limit))) {
                mMediaRecorder.setMaxFileSize(getMaxSize(preferenceUtility.getMaxSize()));
            }

            mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            //start video recording
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * This method return the maximum size in GB of video file to be created.
     *
     * @param maxSize : required size value selected by user
     * @return : file size
     */
    private long getMaxSize(int maxSize) {
        long sizeInGB =1024*1024*1024;

        MyLog.e("ssds","" +  sizeInGB);
        Resources resources = getResources();
        MyLog.e("ssds2", mAllVideoSize[maxSize]);
        if (mAllVideoSize[maxSize].equals(resources.getString(R.string.size_0_5))) {
            MyLog.e("ssds3","" + sizeInGB/2);
            return sizeInGB/2;
        } else if (mAllVideoSize[maxSize].equals(resources.getString(R.string.size_1))) {
            return sizeInGB;
        } else if (mAllVideoSize[maxSize].equals(resources.getString(R.string.size_2))) {
            return 2 * sizeInGB;
        } else if (mAllVideoSize[maxSize].equals(resources.getString(R.string.size_3))) {
            return 3 * sizeInGB;
        } else if (mAllVideoSize[maxSize].equals(resources.getString(R.string.size_5))) {
            return 5 * sizeInGB;
        }

        MyLog.e("ssd2","" + sizeInGB);
        return sizeInGB;
    }

    /**
     * This method return the maximum duration in minute of video file to be created.
     *
     * @param maxDuration :required duration value selected by user
     * @return : file duration in minute
     */
    private int getMaxDuration(int maxDuration) {

        int milliSecondInOneMinute = 60 * 1000;
        Resources resources = getResources();

        MyLog.e("ssd", mMaxAllDurations[maxDuration] + maxDuration + resources.getString(R.string.min_15));

        if (mMaxAllDurations[maxDuration].equals(resources.getString(R.string.min_15))) {
            return 15 * milliSecondInOneMinute;
        } else if (mMaxAllDurations[maxDuration].equals(resources.getString(R.string.min_30))) {
            return 30 * milliSecondInOneMinute;
        } else if (mMaxAllDurations[maxDuration].equals(resources.getString(R.string.min_45))) {
            return 45 * milliSecondInOneMinute;
        } else if (mMaxAllDurations[maxDuration].equals(resources.getString(R.string.min_60))) {
            return 60 * milliSecondInOneMinute;
        }

        return milliSecondInOneMinute;

    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MyLog.e("TAG", "surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        MyLog.e("TAG", "surface destroyed");
    }


    /**
     * stop and release the media recorder
     */
    public void stopMediaRecorder() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * This method return the required camera quality parameter based on user selection
     *
     * @param preferenceUtility : instance of Spycam sharedPreference
     * @param qualityList       : all available qualities
     * @return : camera quality value
     */
    public int getCameraQuality(SpyCamPreferenceUtility preferenceUtility, List<String> qualityList) {

        int cameraQuality = preferenceUtility.getPreferredCameraQuality();

        switch (qualityList.get(cameraQuality)) {

            case SpyCamConstants.QUALITY_1080:
                return CamcorderProfile.QUALITY_1080P;
            case SpyCamConstants.QUALITY_720:
                return CamcorderProfile.QUALITY_720P;
            case SpyCamConstants.QUALITY_480:
                return CamcorderProfile.QUALITY_480P;
            case SpyCamConstants.QUALITY_360:
                return CamcorderProfile.QUALITY_QVGA;
            case SpyCamConstants.QUALITY_240:
                return CamcorderProfile.QUALITY_LOW;
            default:
                return CamcorderProfile.QUALITY_LOW;
        }
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //take the user to app
            try {
                mContext.startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (RuntimeException rpe) {
                rpe.printStackTrace();
            }
            return true;
        }
    }
}
