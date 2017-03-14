package com.sunitdeswal.spycam.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.SpyCamApplication;
import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;

import java.util.ArrayList;

/**
 * Utility class for Camera
 * Created by Sunit deswal on 2/21/2017.
 */

public class CameraUtility {



    /**
     * check if camera is supported or not
     *
     * @param context : application context
     * @return : true if camera is supported else false
     */
    public static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    /**
     * get an instance of the Camera object
     *
     * @return : instance of camera
     */
    public static Camera getBackCamera() {
        Camera camera = null;
        try {
            // attempt to get a Camera instance
            camera = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        // returns null if camera is unavailable
        return camera;
    }


    /**
     * This method give access to front facing camera if available
     * @return : instance of front camera
     */
    public static Camera getFrontFacingCamera() {
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int camId = 0; camId < Camera.getNumberOfCameras(); camId++) {
            Camera.getCameraInfo(camId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(camId);
                } catch (RuntimeException e) {
                    MyLog.e("TAG", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return camera;
    }

    public static ArrayList<String> getSupportedCameraQuality(int camID) {
        ArrayList<String> cameraQuality = new ArrayList<>();

        if(CamcorderProfile.hasProfile(camID,CamcorderProfile.QUALITY_1080P))
            cameraQuality.add(SpyCamConstants.QUALITY_1080);

        if(CamcorderProfile.hasProfile(camID,CamcorderProfile.QUALITY_720P))
            cameraQuality.add(SpyCamConstants.QUALITY_720);

        if(CamcorderProfile.hasProfile(camID,CamcorderProfile.QUALITY_480P))
            cameraQuality.add(SpyCamConstants.QUALITY_480);

        if(CamcorderProfile.hasProfile(camID,CamcorderProfile.QUALITY_CIF))
            cameraQuality.add(SpyCamConstants.QUALITY_360);

        if(CamcorderProfile.hasProfile(camID,CamcorderProfile.QUALITY_QVGA))
            cameraQuality.add(SpyCamConstants.QUALITY_240);

        for (int i=0;i<cameraQuality.size();++i){
            MyLog.e("quality of",camID + ":" + cameraQuality.get(i) );
        }

        return cameraQuality;
    }
}
