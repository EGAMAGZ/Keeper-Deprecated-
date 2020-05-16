package com.android.keeper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class PreferenceUtil {

    public static String KEEPER_SETTINGS = "keeper_settings";

    public static String CLOCK_FORMAT = "clock_format";
    public static String CHANGE_LAST_FRAGMENT="change_last_fragment";
    public static String KEEP_SCREEN_ON = "keep_screen_on";
    public static String LAST_FRAGMENT = "last_fragment";

    private static PreferenceUtil sInstance;
    private final SharedPreferences mSharedPreferences;

    public PreferenceUtil(@NonNull final Context context) {
        mSharedPreferences= context.getSharedPreferences(KEEPER_SETTINGS,Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(@NonNull final Context context) {
        if(sInstance==null){
            sInstance= new PreferenceUtil(context);
        }
        return sInstance;
    }

    public void setClockFormat(String format){
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CLOCK_FORMAT,format);
        editor.apply();
    }

    public String getClockFormat(){
        return mSharedPreferences.getString(CLOCK_FORMAT,"auto");
    }

    public void setChangeLastFragment(boolean change){
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(CHANGE_LAST_FRAGMENT,change);
        editor.apply();
    }

    public boolean getChangeLastFragment() {
        return mSharedPreferences.getBoolean(CHANGE_LAST_FRAGMENT,false);
    }

    public void setKeepScreenOn(boolean keepScreenOn){
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEEP_SCREEN_ON,keepScreenOn);
        editor.apply();
    }
    public boolean getKeepScreenOn(){
        return mSharedPreferences.getBoolean(KEEP_SCREEN_ON,false);
    }

    public void setLastFragment(String keyword){
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LAST_FRAGMENT,keyword);
        editor.apply();
    }

    public String getLastFragment() {
        return mSharedPreferences.getString(LAST_FRAGMENT,"notes");
    }
}
