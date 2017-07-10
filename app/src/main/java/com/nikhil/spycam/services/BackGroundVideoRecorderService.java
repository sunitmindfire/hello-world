package com.nikhil.spycam.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nikhil.spycam.CameraPreview;
import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.enums.ServiceType;
import com.nikhil.spycam.utils.CameraUtility;
import com.nikhil.spycam.utils.NotificationUtility;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;
import com.nikhil.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;
import com.nikhil.spycam.utils.toastUtility.MyToast;

/**
 * Created by Sunit deswal on 12/12/2016.
 * <p>
 * Start video recording in background
 */
public class BackGroundVideoRecorderService extends Service {

    private Camera mCamera;
    private WindowManager mWindowManager;
    private CameraPreview mCameraPreview;
    private android.view.WindowManager.LayoutParams mLayoutParams;


    @Override
    public void onCreate() {
        super.onCreate();


        SpyCamPreferenceUtility preferenceUtility = new SpyCamPreferenceUtility(this);

        MyLog.e("tag camera", "onCreate" + Thread.currentThread().getName() + preferenceUtility.getPreviewHeight()+"---width ---"+ preferenceUtility.getPreviewWidth());

        // Create new window to display camera preview
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        //set layout parameters for window
        mLayoutParams = new WindowManager.LayoutParams(preferenceUtility.getPreviewWidth(),
                preferenceUtility.getPreviewHeight(),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_SCALED|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;

        //show notification which indicate that service is started
        Notification notification = NotificationUtility.generateNotification(this, getString(R.string.notification_tittle),
                getString(R.string.notification_text), R.mipmap.spycam_app_icon);

        //change the state of service to foreground so that system will not kill it
        startForeground(SpyCamConstants.NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.e("tag camera", "onStartCommand" + Thread.currentThread().getName());
        Bundle bundle = intent.getBundleExtra(SpyCamConstants.KEY_RECORDER_SERVICE);
        if((ServiceType)bundle.getSerializable(SpyCamConstants.KEY_SERVICE_TYPE) == ServiceType.ALARM){
            int duration = bundle.getInt(SpyCamConstants.KEY_DURATION,2);
            new CountDownTimer(duration*60*1000,1000){

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    stopSelf();
                }
            }.start();
        }

        int camId = new SpyCamPreferenceUtility(this).getPreferredCamera();
        mCamera = (camId==0)
                ? CameraUtility.getBackCamera():CameraUtility.getFrontFacingCamera();

        if (mCamera != null) {

            //change camera orientation
            mCamera.setDisplayOrientation(90);

            //disable camera sound
            mCamera.enableShutterSound(false);

            //set camera preview
            mCameraPreview = new CameraPreview(this, mCamera);

            //add camera preview to view
            mWindowManager.addView(mCameraPreview, mLayoutParams);
            
            mCameraPreview.setOnTouchListener(new View.OnTouchListener() {

                private WindowManager.LayoutParams paramsF = mLayoutParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = motionEvent.getRawX();
                            initialTouchY = motionEvent.getRawY();
                            MyLog.e("touch","initial x:" + initialX + " initial y:" + initialY);
                            MyLog.e("touch 2","initialTouchX:" + initialTouchX + " initialTouchY:" + initialTouchY);
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                            MyLog.e("touch 3","getRaw x and y :" +motionEvent.getRawX() + " " + (motionEvent.getRawY() ));
                            MyLog.e("touch 4","param last = " + paramsF.x + " " + paramsF.y);
                            mWindowManager.updateViewLayout(mCameraPreview, paramsF);
                            break;
                    }
                    return false;
                }
            });
          MyToast.makeText(this,"Recording Start",Toast.LENGTH_SHORT);
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
        MyLog.e("tag camera", "onDestroy" + Thread.currentThread().getName() + mCamera);

        try {

            if (mCameraPreview != null)
                mCameraPreview.stopMediaRecorder();

            //release the camera
            if (mCamera != null) {
                mCamera.lock();
                mCamera.stopPreview();
                mCamera.release();
            }

            //remove camera preview
            mWindowManager.removeView(mCameraPreview);

            MyToast.makeText(this, "Recoding Stop", Toast.LENGTH_SHORT);


//<------------Start MediaScanner so that latest files are added in content provider----------->
            if (CameraPreview.sLastFilePath != null) {
                MyLog.e("last video ", CameraPreview.sLastFilePath);

                SpyCamUtility.startMediaScanner(
                        getApplicationContext(),
                        new String[]{CameraPreview.sLastFilePath},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                MyLog.v("scanner", "file " + path + " was scanned seccessfully: " + uri);
                            }
                        });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
