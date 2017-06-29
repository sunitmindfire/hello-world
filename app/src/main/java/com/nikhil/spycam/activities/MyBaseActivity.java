package com.nikhil.spycam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * This class is used as a parent activity to override back button functionality
 *
 * Created by Sunit deswal on 3/2/2017.
 */
public class MyBaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
