package com.android.keeper.recycle_items

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class TaskItem(private var imageResource: Int, private val taskId: Int, private var taskTitle: String?, private var taskDetails: String?, private var taskDone: Boolean) {
    fun getImageResource(): Int {
        return imageResource
    }

    fun getTaskTitle(): String? {
        return taskTitle
    }

    fun getTaskDetails(): String? {
        return taskDetails
    }

    fun getTaskId(): Int {
        return taskId
    }

    fun setTaskTitle(taskTitle: String?) {
        this.taskTitle = taskTitle
    }

    fun setTaskDetails(taskDetails: String?) {
        this.taskDetails = taskDetails
    }

    fun setImageResource(imageResource: Int) {
        this.imageResource = imageResource
    }

    fun isTaskDone(): Boolean {
        return taskDone
    }

    fun setTaskDone(taskDone: Boolean) {
        this.taskDone = taskDone
    }

}