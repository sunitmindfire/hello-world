package com.sunitdeswal.spycam.utils.toastUtility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sunit deswal on 2/21/2017.
 */

public class MyToast {

    public static void makeText(Context iContext,String iMessage,int iDuration){
        Toast.makeText(iContext,iMessage,iDuration).show();
    }
}
