package com.nikhil.spycam.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

import com.nikhil.spycam.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


/**
 * Splash screen of application
 *
 */
public class SplashScreenActivity extends AppCompatActivity {

    private RotateAnimation mRotateAnimation;
    private ProgressBar mProgressBar;
    private int mProgressStatus;
    private Handler mHandler;
    private View mSpyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //initialize Crashlytics
        Fabric.with(this, new Crashlytics());

        initializeViews();
        initializeAnimation();
    }

    /**
     * initialize views
     */
    private void initializeViews() {

        mSpyImage = findViewById(R.id.background);
        mHandler = new Handler();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //change progressBar color
        mProgressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorDarkGreen),
                PorterDuff.Mode.SRC_IN);
    }


    /**
     * initialize animation
     */
    private void initializeAnimation() {
        mRotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setDuration(1000);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Start animation
        mSpyImage.startAnimation(mRotateAnimation);

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (mProgressStatus < 100) {
                    // Update the progress status
                    ++mProgressStatus;

                    // Try to sleep the thread for 15 milliseconds
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                nextScreen();
            }
        }).start();
    }

    /**
     * This method launch next screen.
     */
    private void nextScreen() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //check draw over app permission
                    if (!Settings.canDrawOverlays(SplashScreenActivity.this)) {
                        intent = new Intent(SplashScreenActivity.this, DrawOverAppActivity.class);
                    }
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
