package com.android.keeper.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

public class Util {

    public static boolean isInSplitScreen(@NonNull final Context context){
        boolean state=false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if(((Activity)context).isInMultiWindowMode()){
                state=true;
            }
        }
        return state;
    }
}
