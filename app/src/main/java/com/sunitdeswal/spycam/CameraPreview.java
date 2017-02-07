package com.sunitdeswal.spycam;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.utility.logUtility.MyLog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * create the camera preview and start video recording
 * Created by Sunit Deswal on 12/12/2016.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;
        MyLog.e("TAG", "cam preview");

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        MyLog.e("TAG", "surface created");

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setParameters(mCamera.getParameters());
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            MyLog.e("TAG", "Error setting camera preview: " + e.getMessage());
        }


        //get the current time and date
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat(SpyCamConstants.DATE_FORMAT, Locale.ENGLISH)
                .format(date.getTime());

        //unlock the camera for mediaRecorder
        mCamera.unlock();

        //set the video recorder
        mMediaRecorder = new MediaRecorder();

        try {
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

            //set storage location for video
            mMediaRecorder.setOutputFile(SpyCamConstants.STORAGE_PATH + timeStamp + ".mp4");

            //start video recording
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MyLog.e("TAG", "surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mMediaRecorder.stop();
    }
}
