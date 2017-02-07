package com.sunitdeswal.spycam.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;

import com.sunitdeswal.spycam.CameraPreview;
import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.utility.SpyCamUtility;
import com.sunitdeswal.spycam.utility.logUtility.MyLog;

/**
 * Start video recording in background
 * Created by Sunit deswal on 12/12/2016.
 */
public class BackGroundVideoRecorderService extends Service {

    private Camera mCamera;
    private WindowManager mWindowManager;
    private CameraPreview mCameraPreview;
    private android.view.WindowManager.LayoutParams mLayoutParams;


    @Override
    public void onCreate() {
        super.onCreate();

        MyLog.e("tag camera", "onCreate" + Thread.currentThread().getName());

        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        //set layout parameters for window
        mLayoutParams = new WindowManager.LayoutParams(0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;

        //show notification which indicate that service is started
        Notification notification = SpyCamUtility.genrateNotifiacation(this, getString(R.string.notification_tittle),
                getString(R.string.notification_text), R.mipmap.spycam_app_icon);

        //change the state of service to foreground so that system will not kill it
        startForeground(SpyCamConstants.NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.e("tag camera", "onStartCommand" + Thread.currentThread().getName());

        mCamera = SpyCamUtility.getCameraInstance();

        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            //set camera preview
            mCameraPreview = new CameraPreview(this, mCamera);
            //add camera preview to view
            mWindowManager.addView(mCameraPreview, mLayoutParams);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyLog.e("tag camera", "onBind");
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.e("tag", "onDestroy" + Thread.currentThread().getName());

        //release the camera
        if (mCamera != null) {
            mCamera.lock();
            mCamera.release();
            mWindowManager.removeView(mCameraPreview);
        }
    }

}
