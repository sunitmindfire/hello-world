package com.nikhil.spycam.utils.toastUtility;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhil.spycam.R;

/**
 * This class is the utility class for Toast.
 * <p>
 * Created by Sunit deswal on 2/21/2017.
 */

public class MyToast {

    /**
     * This method creates simple toast
     *
     * @param iContext   : application context
     * @param iMessage   : toast message
     * @param iDuration: toast duration
     */
    public static void makeText(Context iContext, String iMessage, int iDuration) {
        Toast.makeText(iContext, iMessage, iDuration).show();
    }


    /**
     * This method creates custom toast.
     * Custom Layout must contain textView with id 'toastTextView'
     *
     * @param iContext   : application context
     * @param layout     : required layout for toast
     * @param iMessage   : required toast message
     * @param iDuration: toast duration
     */
    public static void makeText(Context iContext, View layout, int iMessage, int iDuration) {
        Toast toast = getToast(iContext);
        TextView text = (TextView) layout.findViewById(R.id.toastTextView);
        text.setText(iMessage);
        toast.setDuration(iDuration);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 20);
        toast.show();
    }


    /**
     * This method create an instance of Toast
     *
     * @param iContext : application context
     * @return : instance of Toast
     */
    private static Toast getToast(Context iContext) {
        return new Toast(iContext);
    }

}
