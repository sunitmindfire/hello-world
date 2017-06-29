package com.nikhil.spycam.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;

/**
 * This class show draw over app require permission.
 *
 * Created by Sunit deswal on 1/30/2017.
 */

public class DrawOverAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_over_app);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestDrawOverPermission(View view) {
        Intent drawOverAppIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        drawOverAppIntent.setData(Uri.parse(SpyCamConstants.PACKAGE_LABEL + getPackageName()));
        startActivityForResult(drawOverAppIntent,1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(Settings.canDrawOverlays(this)){
            startActivity(new Intent(this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
