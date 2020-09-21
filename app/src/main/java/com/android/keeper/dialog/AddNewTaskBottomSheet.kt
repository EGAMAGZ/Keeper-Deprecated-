package com.android.keeper.dialogimport

import android.app.Notification
import android.content.Context
import android.view.View
import android.widget.Button
import java.text.DateFormat
import java.util.*

org.junit.runner.RunWithimport android.support.test.runner.AndroidJUnit4import android.support.test.InstrumentationRegistryimport android.os.Buildimport android.app.Activityimport android.database.sqlite.SQLiteDatabaseimport com.android.keeper.util.PreferenceUtilimport android.content.SharedPreferencesimport android.content.SharedPreferences.Editorimport android.support.design.widget.BottomSheetDialogFragmentimport android.widget.TextViewimport android.widget.ImageButtonimport android.view.LayoutInflaterimport android.view.ViewGroupimport android.os.Bundleimport com.android.keeper.Rimport com.android.keeper.dialog.EditTaskBottomSheet.EditTaskBottomSheetListenerimport android.widget.EditTextimport com.android.keeper.localdb.SQLiteConnectionimport android.app.DatePickerDialog.OnDateSetListenerimport android.widget.DatePickerimport com.android.keeper.dialog.TimePickerDialogFragmentimport android.app.TimePickerDialog.OnTimeSetListenerimport android.widget.TimePickerimport android.content.DialogInterfaceimport com.android.keeper.dialog.DatePickerDialogFragmentimport com.android.keeper.localdb.utilities.TasksUtilitiesimport com.android.keeper.util.CursorUtilimport com.android.keeper.util.CalendarUtilimport android.content.ContentValuesimport android.app.AlarmManagerimport android.content.Intentimport com.android.keeper.notifications.AlertReceiverimport android.app.PendingIntentimport com.android.keeper.dialog.AddNewTaskBottomSheet.AddNewTaskBottomSheetListenerimport com.android.keeper.notifications.NotificationHelperimport android.widget.RelativeLayoutimport android.app.DatePickerDialogimport android.app.TimePickerDialogimport com.android.keeper.dialog.AddNewReminderBottomSheet.AddNewReminderBottomSheetListenerimport android.widget.LinearLayoutimport com.android.keeper.localdb.utilities.RemindersUtilitiesimport com.android.keeper.localdb.utilities.NotesUtilitiesimport android.database.sqlite.SQLiteDatabase.CursorFactoryimport android.database.sqlite.SQLiteOpenHelperimport com.android.keeper.recycle_items.NoteItemimport com.android.keeper.recycle_items.TaskItemimport android.support.v7.widget.RecyclerViewimport com.android.keeper.viewholders.TasksViewHolderimport android.widget.Filter.FilterResultsimport com.android.keeper.recycle_items.ReminderItemimport com.android.keeper.viewholders.RemindersViewHolderimport android.widget.ScrollViewimport android.support.design.bottomappbar.BottomAppBarimport android.widget.Toastimport android.support.design.widget.CoordinatorLayoutimport android.support.design.widget.FloatingActionButtonimport android.widget.ProgressBarimport com.android.keeper.adapters.TasksAdapterimport android.view.animation.Animationimport com.android.keeper.dialog.EditTaskBottomSheetimport com.android.keeper.dialog.AddNewTaskBottomSheetimport android.support.v7.widget.LinearLayoutManagerimport android.annotation .SuppressLintimport android.view.animation.Animation.AnimationListenerimport com.android.keeper.customelements.IconToastimport com.android.keeper.dialog.MessageBottomSheetimport com.android.keeper.adapters.RemindersAdapterimport com.android.keeper.dialog.AddNewReminderBottomSheetimport android.view.View.OnLongClickListenerimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.support.annotation .RequiresApiimport android.app.NotificationChannelimport android.content.ContextWrapperimport com.android.keeper.MainActivityimport android.support.v4.app.NotificationCompatimport android.support.v7.app.AppCompatActivityimport android.support.design.widget.NavigationViewimport com.android.keeper.fragments.TasksFragmentimport com.android.keeper.fragments.RemindersFragmentimport android.support.v4.widget.DrawerLayoutimport com.android.keeper.fragments.NotesFragmentimport android.support.v4.content.ContextCompatimport android.support.v4.view.GravityCompatimport android.support.design.internal .NavigationMenuItemViewimport com.android.keeper.fragments.ScheduleFragmentimport com.android.keeper.SettingsActivityimport android.view.inputmethod.EditorInfoimport android.view.WindowManagerimport android.widget.Spinnerimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport android.widget.CompoundButtonimport android.widget.ArrayAdapter
/**
 * Represents a Bottom Sheet that will be displayed when a new task will be added
 *
 * @author Gamaliel Garcia
 */
class AddNewTaskBottomSheet : BottomSheetDialogFragment() {
    var bottomSheetListener: AddNewTaskBottomSheetListener? = null
    private var bottomSheetView: View? = null
    private var addDetailsButton: ImageButton? = null
    private var addDateButton: ImageButton? = null
    private var saveTaskButton: ImageButton? = null
    private var deleteTaskDateButton: ImageButton? = null
    private var changeTaskDateButton: Button? = null
    private var changeTaskTimeButton: Button? = null
    private var titleEditText: EditText? = null
    private var descriptionEditText: EditText? = null
    private var conn: SQLiteConnection? = null
    private var notificationHelper: NotificationHelper? = null
    private var taskDateContainer: RelativeLayout? = null
    private var task_title: String? = null
    private var task_details: String? = null
    private var task_id = 0
    private var selected_year: Int? = null
    private var selected_month: Int? = null
    private var selected_dayOfMonth: Int? = null
    private var selected_hourOfDay: Int? = null
    private var selected_minute: Int? = null
    private var calendar: Calendar? = null

    interface AddNewTaskBottomSheetListener {
        /**
         * Adds new task to the list
         * @param task_id Id of the task when it's stored
         * @param task_title Title of the task
         * @param task_details Details of the task
         */
        open fun onAddTask(task_id: Int, task_title: String?, task_details: String?)

        /**
         * Advices the user when the title of the task is empty
         */
        open fun onEmptyTaskTitle()

        /**
         * Advices the user when the date of the task is selected
         */
        open fun onTaskDateSelected()
    }

    /**
     * Sets interface for the bottom sheet
     * @param listener Interface of AddNewTaskBottomSheet
     */
    fun setBottomSheetListener(listener: AddNewTaskBottomSheetListener?) {
        bottomSheetListener = listener
    }
    /* ----- Listeners when time and date are selected ----- */
    /**
     * Listener when the user selects a date to the task
     */
    private val onDateSetListener: OnDateSetListener? = object : OnDateSetListener {
        override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            selected_year = year
            selected_month = month
            selected_dayOfMonth = dayOfMonth
            /* Once the user selects date, will display the time picker dialog */
            val timePicker = TimePickerDialogFragment()
            timePicker.setCallBack(onTimeSetListener)
            timePicker.setOnDismissListener(onTimeDismissListener)
            timePicker.show(getFragmentManager(), "time_picker")
        }
    }

    /**
     * Listener when the user selects the time to the task
     */
    private val onTimeSetListener: OnTimeSetListener? = object : OnTimeSetListener {
        override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
            selected_hourOfDay = hourOfDay
            selected_minute = minute
            /* Once the user selects the time, date and time will be displayed in the views related with them */setTaskDate()
        }
    }
    /* ----- Listeners when time or date in not selected */
    /**
     * Listener when the user dismiss the date picker
     */
    private val onDateDismissListener: DialogInterface.OnDismissListener? = object : DialogInterface.OnDismissListener {
        override fun onDismiss(dialogInterface: DialogInterface?) {
            /* In case of dismiss, the variables related with date and time will set a value
             * of 0 and will not continue with the next step (display date and time)
             * */
            selected_year = null
            selected_month = null
            selected_dayOfMonth = null
        }
    }

    /**
     * Listener when the user dismiss the time picker
     */
    private val onTimeDismissListener: DialogInterface.OnDismissListener? = object : DialogInterface.OnDismissListener {
        override fun onDismiss(dialogInterface: DialogInterface?) {
            /* In case of dismiss, the variables related with date and time will set a value
             * of 0 and will not continue with the next step (display date and time)
             * */
            selected_year = null
            selected_month = null
            selected_dayOfMonth = null
            selected_minute = null
            selected_hourOfDay = null
        }
    }
    /* ----- Listeners when time or date are changed (individually)----- */
    /**
     * Listener when the user updates the date
     */
    private val onChangeDateListener: OnDateSetListener? = object : OnDateSetListener {
        override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            selected_year = year
            selected_month = month
            selected_dayOfMonth = dayOfMonth
            setTaskDate()
        }
    }

    /**
     * Listener when the user updates the time
     */
    private val onChangeTimeListener: OnTimeSetListener? = object : OnTimeSetListener {
        override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
            selected_hourOfDay = hourOfDay
            selected_minute = minute
            setTaskDate()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_add_new_task, container, false)
        conn = SQLiteConnection(getContext(), "keeper_db", null, 1)
        notificationHelper = NotificationHelper(getContext())

        // ----- Layouts ------
        taskDateContainer = bottomSheetView.findViewById<RelativeLayout?>(R.id.task_date_container)
        // ----- Buttons -----
        addDetailsButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_add_details)
        addDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_add_date)
        saveTaskButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_save)
        deleteTaskDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.task_delete_date)
        changeTaskDateButton = bottomSheetView.findViewById<Button?>(R.id.task_date)
        changeTaskTimeButton = bottomSheetView.findViewById<Button?>(R.id.task_time)
        // ----- EditTexts ------
        descriptionEditText = bottomSheetView.findViewById<EditText?>(R.id.task_details)
        titleEditText = bottomSheetView.findViewById<EditText?>(R.id.task_title)
        // ----- setListeners -----
        changeTaskDateButton.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setCallBack(onChangeDateListener)
            datePicker.show(getFragmentManager(), "date_picker")
        })
        changeTaskTimeButton.setOnClickListener(View.OnClickListener {
            val timePicker = TimePickerDialogFragment()
            timePicker.setCallBack(onChangeTimeListener)
            timePicker.show(getFragmentManager(), "time_picker")
        })
        deleteTaskDateButton.setOnClickListener(View.OnClickListener { deleteDateFields() })
        addDetailsButton.setOnClickListener(View.OnClickListener {
            if (descriptionEditText.getVisibility() == View.GONE) {
                descriptionEditText.setVisibility(View.VISIBLE)
            }
        })
        addDateButton.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setCallBack(onDateSetListener)
            datePicker.setOnDismissListener(onDateDismissListener)
            datePicker.show(getFragmentManager(), "date_picker")
        })
        saveTaskButton.setOnClickListener(View.OnClickListener { //TODO: MAKE SOME TEST, TO CHECK IF THE NOTIFICATION ALARMS DO NOT OVERLAP
            task_title = titleEditText.getText().toString()
            task_details = if (descriptionEditText.getVisibility() == View.VISIBLE) {
                descriptionEditText.getText().toString()
            } else {
                ""
            }
            if (task_title.isEmpty()) {
                bottomSheetListener.onEmptyTaskTitle()
            } else {
                task_id = saveTask()
                setNotificationAlarm(notificationHelper.getTaskNotification(task_title, task_details), task_id)
                bottomSheetListener.onAddTask(task_id, task_title, task_details)
                dismiss()
            }
        })
        deleteTaskDateButton.setOnClickListener(View.OnClickListener { deleteDateFields() })
        return bottomSheetView
    }

    /**
     * Stores the title,details,date and time in the database
     */
    private fun saveTask(): Int {
        val id: Int
        val returnedId: Long
        val database: SQLiteDatabase = conn.getWritableDatabase()
        val values = ContentValues()
        values.put(TasksUtilities.COLUMN_TASK_TITLE, task_title)
        values.put(TasksUtilities.COLUMN_TASK_DETAILS, task_details)
        // DATE
        values.put(TasksUtilities.COLUMN_TASK_YEAR, selected_year)
        values.put(TasksUtilities.COLUMN_TASK_MONTH, selected_month)
        values.put(TasksUtilities.COLUMN_TASK_DAY, selected_dayOfMonth)
        //TIME
        values.put(TasksUtilities.COLUMN_TASK_HOUR, selected_hourOfDay)
        values.put(TasksUtilities.COLUMN_TASK_MINUTE, selected_minute)
        returnedId = database.insert(TasksUtilities.TABLE_NAME, TasksUtilities.COLUMN_TASK_ID, values)
        id = returnedId as Int // Gets id
        database.close()
        return id
    }

    /**
     * Sets time and date of the reminder in the bottom sheet
     */
    fun setTaskDate() {
        if (selected_year != null && selected_month != null && selected_dayOfMonth != null) {
            if (deleteTaskDateButton.getVisibility() == View.GONE || changeTaskDateButton.getVisibility() == View.GONE) {
                // Checks if the views related with date and time are hidden
                showDateFields()
            }
            val calendarUtil = CalendarUtil(getContext(), selected_year, selected_month, selected_dayOfMonth, selected_hourOfDay, selected_minute)
            val date: String = calendarUtil.getDateFormat(DateFormat.FULL)
            val time: String = calendarUtil.getTimeFormat(DateFormat.SHORT)
            //Sets time and date
            changeTaskDateButton.setText(date)
            changeTaskTimeButton.setText(time)
            bottomSheetListener.onTaskDateSelected()
        }
    }

    private fun setNotificationAlarm(notification: Notification?, task_id: Int) {
        //TODO:FIX ERROR THAT SHOW THE SAME CONTENT TO EVERY NOTIFICATION(SOMETIMES)
        //TODO:ADD METHOD TO DELETE/CANCEL NOTIFICAION
        if (selected_year != null && selected_month != null && selected_dayOfMonth != null) {
            calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, selected_year)
            calendar.set(Calendar.MONTH, selected_month)
            calendar.set(Calendar.DAY_OF_MONTH, selected_dayOfMonth)
            calendar.set(Calendar.HOUR_OF_DAY, selected_hourOfDay)
            calendar.set(Calendar.MINUTE, selected_minute)
            calendar.set(Calendar.SECOND, 0)
            val alarmManager: AlarmManager = bottomSheetView.getContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val channelTaskID: String = NotificationHelper.Companion.channelTaskID
            val channelTaskName: String = NotificationHelper.Companion.channelTasksName
            val intent = Intent(getContext(), AlertReceiver::class.java)
            intent.putExtra(AlertReceiver.Companion.NOTIFICATION_ID, task_id)
            intent.putExtra(AlertReceiver.Companion.NOTIFICATION, notification)
            intent.putExtra(AlertReceiver.Companion.NOTIFICATION_CHANNEL_ID, channelTaskID)
            intent.putExtra(AlertReceiver.Companion.NOTIFICATION_CHANNEL_NAME, channelTaskName)
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }
    }

    /**
     * Sets views related with time and date visible
     */
    private fun showDateFields() {
        deleteTaskDateButton.setVisibility(View.VISIBLE)
        changeTaskDateButton.setVisibility(View.VISIBLE)
        changeTaskTimeButton.setVisibility(View.VISIBLE)
        taskDateContainer.setVisibility(View.VISIBLE)
    }

    /**
     * Sets views related with time and date gone
     */
    private fun deleteDateFields() {
        deleteTaskDateButton.setVisibility(View.GONE)
        changeTaskDateButton.setVisibility(View.GONE)
        changeTaskTimeButton.setVisibility(View.GONE)
        taskDateContainer.setVisibility(View.GONE)
        selected_year = null
        selected_month = null
        selected_dayOfMonth = null
        selected_hourOfDay = null
        selected_minute = null
    }
}