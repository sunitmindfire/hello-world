package com.sunitdeswal.spycam.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.sunitdeswal.spycam.R;


/**
 * Splash screen of application
 */
public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private Handler mHandler;
    private int mProgressStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //change progressBar color
        mProgressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorSkyBlue),
                PorterDuff.Mode.SRC_IN);

        mHandler = new Handler();
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (mProgressStatus < 100) {

                    // Update the progress status
                    ++mProgressStatus;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
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

                //Launch Home Screen
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
