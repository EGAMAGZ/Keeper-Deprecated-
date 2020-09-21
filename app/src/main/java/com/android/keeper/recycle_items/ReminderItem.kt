package com.android.keeper.recycle_items

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class ReminderItem(private val reminderId: Int, private var reminderTitle: String?, private var reminderDate: String?, private var reminderTime: String?, private var reminderDone: Boolean) {
    fun getReminderId(): Int {
        return reminderId
    }

    fun getReminderTitle(): String? {
        return reminderTitle
    }

    fun setReminderTitle(reminderTitle: String?) {
        this.reminderTitle = reminderTitle
    }

    fun getReminderDate(): String? {
        return reminderDate
    }

    fun setReminderDate(reminderDate: String?) {
        this.reminderDate = reminderDate
    }

    fun getReminderTime(): String? {
        return reminderTime
    }

    fun setReminderTime(reminderTime: String?) {
        this.reminderTime = reminderTime
    }

    fun isReminderDone(): Boolean {
        return reminderDone
    }

    fun setReminderDone(reminderDone: Boolean) {
        this.reminderDone = reminderDone
    }

}