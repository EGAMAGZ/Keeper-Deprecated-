package com.android.keeper.util

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CalendarUtil(context: Context, year: Int?, month: Int?, day: Int?, hour: Int?, minute: Int?) {
    private val calendar: Calendar?
    private val context: Context?

    /** Here is use Integer instead of int because with it you can set is as null(also default value).
     * The default of int is 0 */
    private val year: Int?
    private val month: Int?
    private val day: Int?
    private val hour: Int?
    private val minute: Int?
    private fun getTimeSettings(): String? {
        return PreferenceUtil.Companion.getInstance(context).getClockFormat()
    }

    fun getDateFormat(timeStyle: Int): String? {
        var date: String? = ""
        if (year != null || month != null || day != null) {
            calendar.set(year, month, day)
            date = DateFormat.getDateInstance(timeStyle).format(calendar.getTime())
        }
        return date
    }

    fun getTimeFormat(timeStyle: Int): String? {
        var time: String? = ""
        val dateFormat: DateFormat
        if (year != null || month != null || day != null) {
            calendar.set(year, month, day, hour, minute)
            when (getTimeSettings()) {
                "12hr" -> {
                    dateFormat = SimpleDateFormat("KK:mm a")
                    time = dateFormat.format(calendar.getTime())
                }
                "24hr" -> {
                    dateFormat = SimpleDateFormat("HH:mm")
                    time = dateFormat.format(calendar.getTime())
                }
                "auto" -> time = DateFormat.getTimeInstance(timeStyle).format(calendar.getTime())
            }
        }
        return time
    }

    init {
        calendar = Calendar.getInstance()
        this.context = context
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
    }
}