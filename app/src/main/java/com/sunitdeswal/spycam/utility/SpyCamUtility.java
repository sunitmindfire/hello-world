package com.sunitdeswal.spycam.utility;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

/**
 * Created by Sunit deswal on 12/12/2016.
 */

public class SpyCamUtility {


    /**
     * attach navigation drawer to action bar
     *
     * @param drawerLayout          :instance of navigation drawer
     * @param actionBarDrawerToggle : instance of actionBarDrawerToggle
     */
    public static void setNavigationDrawerToActionBar(DrawerLayout drawerLayout, ActionBarDrawerToggle actionBarDrawerToggle) {
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

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
    public static Camera getCameraInstance() {
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
     * give an instance of notification builder
     *
     * @param context :application context
     * @return :instance of notification builder
     */
    public static Notification.Builder getNotificationBuilder(Context context) {
        return new Notification.Builder(context);
    }


    /**
     * generate notification
     *
     * @param context      :application context
     * @param titleMessage :title message
     * @param textMessage  :text message
     * @param icon         :notification icon
     * @return instance of notification
     */
    public static Notification genrateNotifiacation(Context context, String titleMessage, String textMessage, int icon) {

        Notification.Builder notificationBuilder = getNotificationBuilder(context);
        return notificationBuilder
                .setContentTitle(titleMessage)
                .setContentText(textMessage)
                .setSmallIcon(icon)
                .build();

    }


}
