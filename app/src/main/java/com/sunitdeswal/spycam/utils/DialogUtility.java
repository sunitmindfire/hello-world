package com.sunitdeswal.spycam.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * This is utility class for various alert dialogs
 * <p>
 * Created by Sunit Deswal on 3/6/2017.
 */

public class DialogUtility {

    /**
     * This method create the dialog builder
     *
     * @param iContext : application context
     * @return : alert dialog builder
     */
    private static AlertDialog.Builder createAlertDialogBuilder(Context iContext) {
        return new AlertDialog.Builder(iContext);
    }


    /**
     * This method shows the alert dialog with a message
     *
     * @param iContext                      :application context
     * @param iDialogMessage                :dialog message
     * @param iPositiveButtonText           :positive button text
     * @param iNegativeButtonText           :negative button text
     * @param iDialogOnClickListener:dialog on click listener
     */
    public static void showDialog(Context iContext, int iDialogMessage, int iPositiveButtonText, int iNegativeButtonText,
                                  View iDialogView, DialogInterface.OnClickListener iDialogOnClickListener) {

        AlertDialog.Builder builder = createAlertDialogBuilder(iContext);

        builder.setMessage(iContext.getString(iDialogMessage))
                .setPositiveButton(iPositiveButtonText, iDialogOnClickListener)
                .setNegativeButton(iNegativeButtonText, iDialogOnClickListener);

        if (iDialogView != null)
            builder.setView(iDialogView);

        builder.create().show();
    }
}
