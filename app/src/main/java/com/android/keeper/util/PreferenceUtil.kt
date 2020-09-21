package com.android.keeper.util

import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class PreferenceUtil private constructor(context: Context) {
    private val mSharedPreferences: SharedPreferences?
    fun setClockFormat(format: String?) {
        val editor = mSharedPreferences.edit()
        editor.putString(CLOCK_FORMAT, format)
        editor.apply()
    }

    fun getClockFormat(): String? {
        return mSharedPreferences.getString(CLOCK_FORMAT, "auto")
    }

    fun setChangeLastFragment(change: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(CHANGE_LAST_FRAGMENT, change)
        editor.apply()
    }

    fun getChangeLastFragment(): Boolean {
        return mSharedPreferences.getBoolean(CHANGE_LAST_FRAGMENT, false)
    }

    fun setKeepScreenOn(keepScreenOn: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(KEEP_SCREEN_ON, keepScreenOn)
        editor.apply()
    }

    fun getKeepScreenOn(): Boolean {
        return mSharedPreferences.getBoolean(KEEP_SCREEN_ON, false)
    }

    fun setLastFragment(keyword: String?) {
        val editor = mSharedPreferences.edit()
        editor.putString(LAST_FRAGMENT, keyword)
        editor.apply()
    }

    fun getLastFragment(): String? {
        return mSharedPreferences.getString(LAST_FRAGMENT, "notes")
    }

    companion object {
        var KEEPER_SETTINGS: String? = "keeper_settings"
        var CLOCK_FORMAT: String? = "clock_format"
        var CHANGE_LAST_FRAGMENT: String? = "change_last_fragment"
        var KEEP_SCREEN_ON: String? = "keep_screen_on"
        var LAST_FRAGMENT: String? = "last_fragment"
        private var sInstance: PreferenceUtil? = null
        fun getInstance(context: Context): PreferenceUtil? {
            if (sInstance == null) {
                sInstance = PreferenceUtil(context)
            }
            return sInstance
        }
    }

    init {
        mSharedPreferences = context.getSharedPreferences(KEEPER_SETTINGS, Context.MODE_PRIVATE)
    }
}