package com.nikhil.spycam.utils;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by Sunit deswal on 04/07/2017.
 * <p>
 * Utility class for alarm manager
 */
public class AlarmManagerUtility {

    public static AlarmManager getAlarmManager(Context context){
        return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

}
