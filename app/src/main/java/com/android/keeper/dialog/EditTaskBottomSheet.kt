package com.android.keeper.dialog

import android.app.AlarmManager
import android.app.DatePickerDialog.OnDateSetListener
import android.app.PendingIntent
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.android.keeper.R
import com.android.keeper.localdb.SQLiteConnection
import com.android.keeper.localdb.utilities.TasksUtilities
import com.android.keeper.notifications.AlertReceiver
import com.android.keeper.util.CalendarUtil
import com.android.keeper.util.CursorUtil
import org.junit.runner.RunWith
import java.text.DateFormat

/**
 * Represents a bottom sheet to edit a task
 */
class EditTaskBottomSheet : BottomSheetDialogFragment() {
    private var bottomSheetListener: EditTaskBottomSheetListener? = null
    private var bottomSheetView: View? = null
    private var taskTitleEditText: EditText? = null
    private var taskDetailsEditText: EditText? = null
    private var saveTaskButton: ImageButton? = null
    private var deleteTaskButton: ImageButton? = null
    private var addTaskDateButton: ImageButton? = null
    private var deleteTaskDateButton: ImageButton? = null
    private var changeTaskDateButton: Button? = null
    private var changeTaskTimeButton: Button? = null
    private var conn: SQLiteConnection? = null
    private var old_task_title: String? = null
    private var old_task_details: String? = null
    private var old_task_id = 0
    private var old_task_position = 0
    private var selected_year: Int? = null
    private var selected_month: Int? = null
    private var selected_dayOfMonth: Int? = null
    private var selected_hourOfDay: Int? = null
    private var selected_minute: Int? = null

    interface EditTaskBottomSheetListener {
        /**
         * Updates the task that is edited
         * @param task_position Position of the task in RecycleView
         * @param task_title Title of the task
         * @param task_details Details of the task
         */
        open fun onSaveEditedTask(task_position: Int, task_title: String?, task_details: String?)

        /**
         * Deletes task from the RecycleView
         * @param task_position Position of task in RecycleView
         */
        open fun onDeleteSavedTask(task_position: Int)

        /**
         * Advices the user when the title of the task is empty
         */
        open fun onEmptyTaskTitle()
    }

    /**
     * Sets interface fro this bottom sheet
     * @param listener Interface of EditTaskBottomSheet
     */
    fun setEditTaskBottomSheetListener(listener: EditTaskBottomSheetListener?) {
        bottomSheetListener = listener
    }
    /* ----- Listeners when time and date are selected ----- */
    /**
     * Listener when the user selects a date to the task
     */
    private val onDateSetListener: OnDateSetListener? = OnDateSetListener { datePicker, year, month, dayOfMonth ->
        selected_year = year
        selected_month = month
        selected_dayOfMonth = dayOfMonth
        val timePicker = TimePickerDialogFragment()
        timePicker.setCallBack(onTimeSetListener)
        timePicker.setOnDismissListener(onTimeDismissListener)
        timePicker.show(fragmentManager, "time picker")
    }

    /**
     * Listener when the user selects a time to the task
     */
    private val onTimeSetListener: OnTimeSetListener? = OnTimeSetListener { timePicker, hourOfDay, minute ->
        selected_hourOfDay = hourOfDay
        selected_minute = minute
        setTaskDate()
    }
    /* ----- Listeners when time or date in not selected */
    /**
     * Listener when the user dismiss the date picker
     */
    private val onDateDismissListener: DialogInterface.OnDismissListener? = DialogInterface.OnDismissListener { /* In case of dismiss, the variables related with date will set a value
             * of 0 and will not continue with the next step (display Time Picker Dialog and set time)
             * */
        selected_year = null
        selected_month = null
        selected_dayOfMonth = null
    }

    /**
     * Listener when the user dismiss the time picker
     */
    private val onTimeDismissListener: DialogInterface.OnDismissListener? = DialogInterface.OnDismissListener { /* In case of dismiss, the variables related with date will set a value
             * of 0 and will not continue with the next step (display Time Picker Dialog and set time)
             * */
        selected_year = null
        selected_month = null
        selected_dayOfMonth = null
        selected_minute = null
        selected_hourOfDay = null
    }
    /* ----- Listeners when time or date are changed (individually)----- */
    /**
     * Listener when the user updates the date
     */
    private val onChangeDateListener: OnDateSetListener? = OnDateSetListener { datePicker, year, month, dayOfMonth ->
        selected_year = year
        selected_month = month
        selected_dayOfMonth = dayOfMonth
        setTaskDate()
    }

    /**
     * Listener when the user updates the time
     */
    private val onChangeTimeListener: OnTimeSetListener? = OnTimeSetListener { timePicker, hourOfDay, minute ->
        selected_hourOfDay = hourOfDay
        selected_minute = minute
        setTaskDate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_edit_task, container, false)
        conn = SQLiteConnection(context, "keeper_db", null, 1)
        // ----- Buttons -----
        changeTaskDateButton = bottomSheetView.findViewById<Button?>(R.id.task_date)
        changeTaskTimeButton = bottomSheetView.findViewById<Button?>(R.id.task_time)
        addTaskDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_add_date)
        deleteTaskDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_delete_date)
        saveTaskButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_save)
        deleteTaskButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_delete)
        // ----- EditTexts -----
        taskTitleEditText = bottomSheetView.findViewById<EditText?>(R.id.task_title)
        taskDetailsEditText = bottomSheetView.findViewById<EditText?>(R.id.task_details)
        // ----- Sets Initial State -----
        loadDate(old_task_id)
        setTaskDate()
        taskTitleEditText.setText(old_task_title)
        if (!old_task_details.isEmpty()) {
            taskDetailsEditText.setText(old_task_details)
        }
        // ----- setListeners ------
        changeTaskDateButton.setOnClickListener(View.OnClickListener {
            if (selected_year != null && selected_month != null && selected_dayOfMonth != null) {
                val datePicker = DatePickerDialogFragment()
                datePicker.setCallBack(onChangeDateListener)
                datePicker.show(fragmentManager, "date_picker")
            } else {
                val datePicker = DatePickerDialogFragment()
                datePicker.setCallBack(onDateSetListener)
                datePicker.setOnDismissListener(onDateDismissListener)
                datePicker.show(fragmentManager, "date_picker")
            }
        })
        changeTaskTimeButton.setOnClickListener(View.OnClickListener {
            if (selected_year != null && selected_month != null && selected_dayOfMonth != null) {
                val timePicker = TimePickerDialogFragment()
                timePicker.setCallBack(onChangeTimeListener)
                timePicker.show(fragmentManager, "time_picker")
            } else {
                val datePicker = DatePickerDialogFragment()
                datePicker.setCallBack(onDateSetListener)
                datePicker.setOnDismissListener(onDateDismissListener)
                datePicker.show(fragmentManager, "date_picker")
            }
        })
        addTaskDateButton.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setCallBack(onDateSetListener)
            datePicker.setOnDismissListener(onDateDismissListener)
            datePicker.show(fragmentManager, "date_picker")
        })
        deleteTaskDateButton.setOnClickListener(View.OnClickListener {
            selected_year = null
            selected_month = null
            selected_dayOfMonth = null
            selected_hourOfDay = null
            selected_minute = null
            changeTaskDateButton.setText("FECHAS")
            changeTaskTimeButton.setText("00:00")
        })
        saveTaskButton.setOnClickListener(View.OnClickListener {
            if (taskTitleEditText.getText().toString().isEmpty()) {
                bottomSheetListener.onEmptyTaskTitle()
            } else {
                saveEditedTask()
                val new_task_title = taskTitleEditText.getText().toString()
                val new_task_details = taskDetailsEditText.getText().toString()
                bottomSheetListener.onSaveEditedTask(old_task_position, new_task_title, new_task_details)
                dismiss()
            }
        })
        deleteTaskButton.setOnClickListener(View.OnClickListener { //TODO: MAKE SOME TEST, TO PROOF THAT IS CANCEL ONLY THE TASK SELECTED
            deleteTask()
            bottomSheetListener.onDeleteSavedTask(old_task_position)
            cancelNotificationAlarm() //TODO: CHECK WHEN A TASK HAS A NOTIFICATION ALARM
            dismiss()
        })
        return bottomSheetView
    }

    /**
     * Sets the main content of the task selected
     */
    fun setContent(position: Int, task_id: Int, task_title: String?, task_details: String?) {
        old_task_position = position
        old_task_id = task_id
        old_task_title = task_title
        old_task_details = task_details
    }

    /**
     * Loads date and time of the taks by searching it by id
     * @param task_id Id of the task
     */
    private fun loadDate(task_id: Int) {
        val columns = arrayOf<String?>(TasksUtilities.COLUMN_TASK_YEAR, TasksUtilities.COLUMN_TASK_MONTH, TasksUtilities.COLUMN_TASK_DAY, TasksUtilities.COLUMN_TASK_HOUR, TasksUtilities.COLUMN_TASK_MINUTE)
        val database = conn.getReadableDatabase()
        val cursor = database.query(TasksUtilities.TABLE_NAME, columns, TasksUtilities.COLUMN_TASK_ID + " = " + task_id, null, null, null, null)
        cursor.moveToFirst() //TODO: CHECK WHY IS NEEDED TO CALL THIS METHOD TO READ THE ELEMENTS FROM QUERY, AND HOW TO DELETE IT
        val cursorUtil = CursorUtil(cursor)
        selected_year = cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_YEAR)
        selected_month = cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_MONTH)
        selected_dayOfMonth = cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_DAY)
        selected_hourOfDay = cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_HOUR)
        selected_minute = cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_MINUTE)
        cursor.close()
        database.close()
    }

    /**
     * Sets time and date of the task in the bottom sheet
     */
    private fun setTaskDate() {
        if (selected_year != null && selected_month != null && selected_dayOfMonth != null) {
            val calendarUtil = CalendarUtil(context, selected_year, selected_month, selected_dayOfMonth, selected_hourOfDay, selected_minute)
            val date = calendarUtil.getDateFormat(DateFormat.FULL)
            val time = calendarUtil.getTimeFormat(DateFormat.SHORT)
            changeTaskDateButton.setText(date)
            changeTaskTimeButton.setText(time)
        }
    }

    /**
     * Stores the edited title,details, date and time in the database
     */
    private fun saveEditedTask() {
        val database = conn.getWritableDatabase()
        val values = ContentValues()
        values.put(TasksUtilities.COLUMN_TASK_TITLE, taskTitleEditText.getText().toString())
        values.put(TasksUtilities.COLUMN_TASK_DETAILS, taskDetailsEditText.getText().toString())
        // DATE
        values.put(TasksUtilities.COLUMN_TASK_YEAR, selected_year)
        values.put(TasksUtilities.COLUMN_TASK_MONTH, selected_month)
        values.put(TasksUtilities.COLUMN_TASK_DAY, selected_dayOfMonth)
        // TIME
        values.put(TasksUtilities.COLUMN_TASK_HOUR, selected_hourOfDay)
        values.put(TasksUtilities.COLUMN_TASK_MINUTE, selected_minute)
        database.update(TasksUtilities.TABLE_NAME, values, TasksUtilities.COLUMN_TASK_ID + "=" + old_task_id, null)
        database.close()
    }

    /**
     * Deletes the task from the database
     */
    private fun deleteTask() {
        val database = conn.getWritableDatabase()
        database.delete(TasksUtilities.TABLE_NAME, TasksUtilities.COLUMN_TASK_ID + "=" + old_task_id, null)
        database.close()
    }

    private fun cancelNotificationAlarm() {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }
}