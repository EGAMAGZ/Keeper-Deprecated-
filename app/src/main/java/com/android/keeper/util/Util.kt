package com.android.keeper.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

object Util {
    fun isInSplitScreen(context: Context): Boolean {
        var state = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if ((context as Activity).isInMultiWindowMode) {
                state = true
            }
        }
        return state
    }
}