package com.android.keeper.localdb.utilities

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

object RemindersUtilities {
    val TABLE_NAME: String? = "REMINDERS_LIST"
    val COLUMN_REMINDER_ID: String? = "reminder_id"
    val COLUMN_REMINDER_TITLE: String? = "reminder_title"
    val COLUMN_REMINDER_YEAR: String? = "reminder_year"
    val COLUMN_REMINDER_MONTH: String? = "reminder_month"
    val COLUMN_REMINDER_DAY: String? = "reminder_day"
    val COLUMN_REMINDER_HOUR: String? = "reminder_hour"
    val COLUMN_REMINDER_MINUTE: String? = "reminder_minute"
    val COLUMN_REMINDER_DONE: String? = "reminder_done"
    val CREATE_REMINDER_TABLE: String? = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NULL," +
            COLUMN_REMINDER_TITLE + " TEXT NOT NULL," + COLUMN_REMINDER_YEAR + " INTEGER NULL," + COLUMN_REMINDER_MONTH + " INTEGER NULL," +
            COLUMN_REMINDER_DAY + " INTEGER NULL," + COLUMN_REMINDER_HOUR + " INTEGER NULL," + COLUMN_REMINDER_MINUTE + " INTEGER NULL," +
            COLUMN_REMINDER_DONE + " INTEGER NULL DEFAULT 0)"
}