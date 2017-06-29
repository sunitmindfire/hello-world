package com.nikhil.spycam.utils.logUtility;

/**
 * Created by Sunit deswal on 1/23/2017.
 */

import android.util.Log;


/**
 * Created by Sunit Deswal on 09-Dec-16.
 *
 * class containing the various log types with the check whether the app is in
 * debug mode and would not display the log if the app is not in debug mode
 */

public class MyLog {

    private static boolean isDebug = com.nikhil.spycam.BuildConfig.DEBUGGABLE;
    /**
     * Following are the methods for logging based upon a boolean that decides
     * for DEBUG/RELEASE mode
     */
    public static void v(String iTag, String iMessage) {
        if (isDebug) {
            Log.v(iTag, iMessage);
        }
    }
    public static void e(String iTag, String iMessage) {
        if (isDebug) {
            Log.e(iTag, iMessage);
        }
    }
    public static void w(String iTag, String iMessage) {
        if (isDebug) {
            Log.w(iTag, iMessage);
        }
    }
    public static void d(String iTag, String iMessage) {
        if (isDebug) {
            Log.d(iTag, iMessage);
        }
    }

    /**
     * method to print the stack trace
     *
     * @param exception exception occurred
     */
    public static void printStack(Exception exception) {
        if (isDebug) {
            exception.printStackTrace();
        } else {
            SpyCamExceptionLogger.getLoggerInstance().writeToLogFile(exception);
        }
    }
}
