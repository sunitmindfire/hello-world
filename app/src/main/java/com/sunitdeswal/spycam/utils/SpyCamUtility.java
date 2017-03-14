package com.sunitdeswal.spycam.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaScannerConnection;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.constants.SpyCamConstants;
import com.sunitdeswal.spycam.utils.logUtility.MyLog;
import com.sunitdeswal.spycam.utils.preferenceUtility.SpyCamPreferenceUtility;

import java.io.File;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class contain the methods which are common to application
 * <p>
 * Created by Sunit Deswal on 12/12/2016.
 */

public class SpyCamUtility {



    /**
     * @param iFragment            :fragment to be inflated
     * @param iFragmentManager     :fragmentManager to begin transaction
     * @param iFragmentContainerId :fragment container
     * @param isAddToBackStack     :to add fragment transaction to backStack
     * @param isReplace            :replace fragment
     * @param tag                  :fragment tag
     * @return :instance of inflated fragment
     */
    public static Fragment inflateFragment(Fragment iFragment, FragmentManager iFragmentManager, int iFragmentContainerId, boolean isAddToBackStack, boolean isReplace, String tag) {
        if (iFragment != null && iFragmentManager != null) {
            FragmentTransaction transaction = iFragmentManager.beginTransaction();

            //replace or add Fragment
            if (isReplace)
                transaction.replace(iFragmentContainerId, iFragment, tag);
            else
                transaction.add(iFragmentContainerId, iFragment, tag);

            //add fragment to backStack
            if (isAddToBackStack)
                transaction.addToBackStack(tag);

            transaction.commit();
        }
        return iFragment;
    }



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
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(Context context) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(new SpyCamPreferenceUtility(context).getStorageLocation(),context.getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                MyLog.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat(SpyCamConstants.DATE_FORMAT, Locale.getDefault()).format(new Date());

        MyLog.e("file", new File(mediaStorageDir.getPath()).list().toString());

        return new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");

    }


    /**
     * This method add the files to MediaStore
     *
     * @param iContext     :application context
     * @param iFilesToScan :files which need to be added into MediaStore
     * @param iMimiTypes   :required Mimi type of files
     * @param iListener    :listener for when file is added to mediaStore
     */
    public static void startMediaScanner(Context iContext, String[] iFilesToScan, String[] iMimiTypes,
                                         MediaScannerConnection.OnScanCompletedListener iListener) {
        MediaScannerConnection.scanFile(iContext, iFilesToScan, iMimiTypes, iListener);
    }

    public static String getTimeFromMilliSecond(int videoDuration) {
        int minute;
        int second;

        second = videoDuration/1000;
        minute = second/60;
        second = second % 60;

        return String.format(Locale.getDefault(),"%02d",minute) + " : " + String.format(Locale.getDefault(),"%02d",second);
    }


    /**
     * This method convert bytes into mega Bytes (MB)
     * @param iBytes : required input
     * @return       : string value of MegaByte
     */
    public static String convertByteToMegaByte(int iBytes) {

        return String.format(Locale.US,"%.2f",iBytes/(1024.0 * 1024.0))  ;
    }


//    public static void checkAndRequestPermission(Context iContext,String [] permissions){
//        for(String permission : permissions){
//            if(ContextCompat.checkSelfPermission(iContext,permission) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(iContext,permissions,1);
//            }
//        }
//    }


//
//    public static ArrayList<Integer> getCameraSupportedResolution(Camera camera){
//        ArrayList<Integer> resolutions=new ArrayList<>();
//        if(camera!=null){
//            List<Camera.Size> list = CamcorderProfile.get(1);
//            for(int i=0;i<list.size();++i){
//               resolutions.add(list.get(i).height);
//            }
//        }
//
//
//        return removeDuplicateElement(resolutions);
//    }
//
//
//    public static ArrayList<Integer> removeDuplicateElement(ArrayList<Integer> list){
//        if (list.size()<2){
//            return list;
//        }
//
//        Log.e("list before",list.size() + ":" + list.toString());
//
//        int previous = list.get(0);
//
//        for(int i=1;i<list.size();++i){
//            Log.e("list in",previous + ":" + list.get(i));
//            if (previous == list.get(i)){
//                list.remove(i);
//                --i;
//                Log.e("list sze",i + ":" + list.size() + ":" + list.toString());
//
//            }
//            previous = list.get(i);
//        }
//
//        Log.e("list after",list.size() + ":" + list.toString());
//        return list;
//    }
}
