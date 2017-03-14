package com.sunitdeswal.spycam;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.utils.SpyCamUtility;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;
import com.sunitdeswal.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

import java.util.List;

/**
 * create the camera preview and start video recording
 * <p>
 * Created by Sunit Deswal on 12/12/2016.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    public static String sLastFilePath;
    private MediaRecorder mMediaRecorder;


    public CameraPreview(Context context, Camera camera) {
        super(context);

        MyLog.e("TAG", "cam preview constructor" + "Thread:" + Thread.currentThread().getName());

        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        getHolder().addCallback(this);
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
            mCamera.release();
        }

        //unlock the camera for mediaRecorder
        mCamera.unlock();

        //set the video recorder
        mMediaRecorder = new MediaRecorder();

        SpyCamPreferenceUtility preferenceUtility = new SpyCamPreferenceUtility(getContext());
        int camId = preferenceUtility.getPreferredCamera();

        MyLog.e("quality check 720", " :" + CamcorderProfile.hasProfile(camId, CamcorderProfile.QUALITY_1080P));

        int cameraQuality = camId==0? getCameraQuality(preferenceUtility, SpyCamConstants.BACK_CAMERA_SUPPORTED_QUALITY)
                :getCameraQuality(preferenceUtility,SpyCamConstants.FRONT_CAMERA_SUPPORTED_QUALITY);

        try {
            sLastFilePath = SpyCamUtility.getOutputMediaFile(getContext()).toString();

            //configure media recorder
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setProfile(CamcorderProfile.get(camId, cameraQuality));
            mMediaRecorder.setOutputFile(sLastFilePath);

            if(preferenceUtility.getMaxDuration()!=0)
            mMediaRecorder.setMaxDuration(getMaxDuration(preferenceUtility.getMaxDuration()));

            if(preferenceUtility.getMaxSize()!=0)
            mMediaRecorder.setMaxFileSize(getMaxSize(preferenceUtility.getMaxSize()));

            mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            //start video recording
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private long getMaxSize(int maxSize) {
        int sizeInGB = 1024*1024*1024*1024;
        switch (maxSize){
            case 1:
                return sizeInGB/2;
            case 2:
            return sizeInGB;
            case 3:
                return 2*sizeInGB;
            case 4:
                return 3*sizeInGB;
            case 5:
                return 5*sizeInGB;
            default:
                return sizeInGB;
        }
    }

    private int getMaxDuration(int maxDuration) {

        switch (maxDuration){
            case 1:
                return 15*60*1000;
            case 2:
                return 30*60*1000;
            case 3:
                return 45*60*1000;
            case 4:
                return 60*60*1000;
            default:
                return 15*60*1000;
        }
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
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
        }

    }


    public int getCameraQuality(SpyCamPreferenceUtility preferenceUtility, List<String> qualityList) {

        int cameraQuality = preferenceUtility.getPreferredCameraQuality();

        switch (qualityList.get(cameraQuality)){

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
}
