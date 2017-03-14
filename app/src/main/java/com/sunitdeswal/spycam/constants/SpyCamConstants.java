package com.sunitdeswal.spycam.constants;

import android.os.Environment;

import com.sunitdeswal.spycam.utils.CameraUtility;

import java.util.ArrayList;

/**
 * Created by Sunit deswal on 1/30/2017.
 */

public class SpyCamConstants {

    public final static int NOTIFICATION_ID = 1;
    public final static String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public  static String sStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() ;

    public final static int NO_PREVIEW_WIDTH = 1;
    public final static int NO_PREVIEW_HEIGHT = 1;

    public final static int PREVIEW_WIDTH_SMALL = 100;
    public final static int PREVIEW_HEIGHT_SMALL = 100;

    public final static int PREVIEW_WIDTH_MEDIUM = 200;
    public final static int PREVIEW_HEIGHT_MEDIUM = 200;

    public final static int PREVIEW_WIDTH_LARGE = 300;
    public final static int PREVIEW_HEIGHT_LARGE = 300;

    public final static String QUALITY_1080 = "1080p";
    public final static String QUALITY_720 = "720p";
    public final static String QUALITY_480 = "480p";
    public final static String QUALITY_360 = "360p";
    public final static String QUALITY_240 = "240p";

    public final static ArrayList<String> BACK_CAMERA_SUPPORTED_QUALITY = CameraUtility.getSupportedCameraQuality(0);

    public final static ArrayList<String> FRONT_CAMERA_SUPPORTED_QUALITY = CameraUtility.getSupportedCameraQuality(1);

    public final static String sFileStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()  + "/SpyCam";
}
