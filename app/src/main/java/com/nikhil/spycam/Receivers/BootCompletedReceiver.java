package com.nikhil.spycam.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nikhil.spycam.utils.toastUtility.MyToast;

/**
 * Copyright MDLive.  All rights reserved.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            MyToast.makeText(context,"Boot Completed",Toast.LENGTH_SHORT);
        }
    }
}
