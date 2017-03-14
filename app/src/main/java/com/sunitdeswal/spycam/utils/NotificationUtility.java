package com.sunitdeswal.spycam.utils;

import android.app.Notification;
import android.content.Context;

/**
 * Created by Sunit deswal on 2/21/2017.
 */

public class NotificationUtility {


    /**
     * give an instance of notification builder
     *
     * @param context :application context
     * @return :instance of notification builder
     */
    private static Notification.Builder getNotificationBuilder(Context context) {
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
    public static Notification generateNotification(Context context, String titleMessage, String textMessage, int icon) {

        return getNotificationBuilder(context)
                .setContentTitle(titleMessage)
                .setContentText(textMessage)
                .setSmallIcon(icon)
                .build();
    }
}
