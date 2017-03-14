package com.sunitdeswal.spycam;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sunit deswal on 3/6/2017.
 */

public class SpyCamApplication extends Application {

    public   static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

    }
}
