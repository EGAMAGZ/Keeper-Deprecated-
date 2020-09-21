package com.android.keeper.dialogimport

import android.view.View
import java.text.DateFormat

/**
 * Represents a BottomSheet that will be displayed when a new reminder will be added
 *
 * @author Gamaliel Garcia
 */
class AddNewReminderBottomSheet : BottomSheetDialogFragment() {
    var bottomSheetListener: AddNewReminderBottomSheetListener? = null
    private var bottomSheetView: View? = null
    private var sqLiteConnection: SQLiteConnection? = null
    private var saveReminderButton: ImageButton? = null
    private var addDateButton: ImageButton? = null
    private var deleteDateButton: ImageButton? = null
    private var titleEditText: EditText? = null
    private var dateTextView: TextView? = null
    private var timeTextView: TextView? = null
    private var dateLayoutContainer: LinearLayout? = null
    private var timeLayoutContainer: LinearLayout? = null
    private var reminder_title: String? = null
    private var reminder_id = 0
    private var selected_year: Int? = null
    private var selected_month: Int? = null
    private var selected_dayOfMonth: Int? = null
    private var selected_hourOfDay: Int? = null
    private var selected_minute: Int? = null

    interface AddNewReminderBottomSheetListener {
        /**
         * Adds new reminder to the list
         * @param reminder_id Id of the reminder when it's stored
         * @param reminder_title Title of reminder
         * @param year Year of the reminder
         * @param month Month of the reminder
         * @param day Day of the reminder
         * @param hour Hour of the reminder
         * @param minute Minute of rhe reminder
         */
        open fun onAddReminder(reminder_id: Int, reminder_title: String?, year: Int?, month: Int?, day: Int?, hour: Int?, minute: Int?)

        /**
         * Advices to the user when the title of reminder is empty
         */
        open fun onEmptyReminderTitle()

        /**
         * Advices the user when date and time is selected
         */
        open fun onReminderDateSelected()
    }

    /**
     * Sets interface for this bottom sheet
     * @param listener Interface of AddNewReminderBottomSheet
     */
    fun setBottomSheetListener(listener: AddNewReminderBottomSheetListener?) {
        bottomSheetListener = listener
    }
    /* ----- Listeners when time and date are selected ----- */
    /**
     * Listener when the user selects a date to the reminder
     */
    private val onDateSetListener: OnDateSetListener? = object : OnDateSetListener {
        override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            selected_year = year
            selected_month = month
            selected_dayOfMonth = dayOfMonth
            /* Once the user selects date, will be display the time picker dialog */
            val timePicker = TimePickerDialogFragment()
            timePicker.setCallBack(onTimeSetListener)
            timePicker.setOnDismissListener(onTimeDismissListener)
            timePicker.show(getFragmentManager(), "time_picker")
        }
    }

    /**
     * Listener when the user selects the time to the reminder
     */
    private val onTimeSetListener: OnTimeSetListener? = object : OnTimeSetListener {
        override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
            selected_hourOfDay = hourOfDay
            selected_minute = minute
            /* Once the user selects the time, date and time will be displayed in the views related with them*/setReminderDate()
        }
    }
    /* ----- Listeners when time or date in not selected ----- */
    /**
     * Listener when the user dismiss the date picker
     */
    private val onDateDismissListener: DialogInterface.OnDismissListener? = object : DialogInterface.OnDismissListener {
        override fun onDismiss(dialogInterface: DialogInterface?) {
            /* In case of dismiss, the variables related with date will set a value
             * of 0 and will not continue with the next step (display Time Picker Dialog and set time)
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
            setReminderDate()
        }
    }

    /**
     * Listener when the user updates the time
     */
    private val onChangeTimeListener: OnTimeSetListener? = object : OnTimeSetListener {
        override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
            selected_hourOfDay = hourOfDay
            selected_minute = minute
            setReminderDate()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_add_new_reminder, container, false) // Gets View
        sqLiteConnection = SQLiteConnection(getContext(), "keeper_db", null, 1) // SQL Connection
        // ----- Layout -----
        dateLayoutContainer = bottomSheetView.findViewById<LinearLayout?>(R.id.reminder_date_container)
        timeLayoutContainer = bottomSheetView.findViewById<LinearLayout?>(R.id.reminder_time_container)
        // ----- Buttons -----
        saveReminderButton = bottomSheetView.findViewById<ImageButton?>(R.id.reminder_save)
        addDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.reminder_add_date)
        deleteDateButton = bottomSheetView.findViewById<ImageButton?>(R.id.reminder_delete_date)
        // ----- EditTexts -----
        titleEditText = bottomSheetView.findViewById<EditText?>(R.id.reminder_title)
        // ----- TextViews -----
        dateTextView = bottomSheetView.findViewById<TextView?>(R.id.reminder_date)
        timeTextView = bottomSheetView.findViewById<TextView?>(R.id.reminder_time)
        // ----- setListeners -----
        dateTextView.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setCallBack(onChangeDateListener)
            datePicker.show(getFragmentManager(), "date_picker")
        })
        timeTextView.setOnClickListener(View.OnClickListener {
            val timePicker = TimePickerDialogFragment()
            timePicker.setCallBack(onChangeTimeListener)
            timePicker.show(getFragmentManager(), "time_picker")
        })
        saveReminderButton.setOnClickListener(View.OnClickListener {
            reminder_title = titleEditText.getText().toString()
            if (reminder_title.isEmpty()) {
                bottomSheetListener.onEmptyReminderTitle()
            } else {
                reminder_id = saveReminder()
                bottomSheetListener.onAddReminder(reminder_id, reminder_title, selected_year, selected_month,
                        selected_dayOfMonth, selected_hourOfDay, selected_minute)
                dismiss()
            }
        })
        addDateButton.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setCallBack(onDateSetListener)
            datePicker.setOnDismissListener(onDateDismissListener)
            datePicker.show(getFragmentManager(), "date_picker")
        })
        deleteDateButton.setOnClickListener(View.OnClickListener { deleteDateFields() })
        return bottomSheetView
    }

    /**
     * Stores the title,date and time in the database
     */
    private fun saveReminder(): Int {
        val id: Int
        val returnedId: Long
        val database: SQLiteDatabase = sqLiteConnection.getWritableDatabase()
        val values = ContentValues()
        values.put(RemindersUtilities.COLUMN_REMINDER_TITLE, reminder_title)
        // DATE
        values.put(RemindersUtilities.COLUMN_REMINDER_YEAR, selected_year)
        values.put(RemindersUtilities.COLUMN_REMINDER_MONTH, selected_month)
        values.put(RemindersUtilities.COLUMN_REMINDER_DAY, selected_dayOfMonth)
        // TIME
        values.put(RemindersUtilities.COLUMN_REMINDER_HOUR, selected_hourOfDay)
        values.put(RemindersUtilities.COLUMN_REMINDER_MINUTE, selected_minute)
        returnedId = database.insert(RemindersUtilities.TABLE_NAME, RemindersUtilities.COLUMN_REMINDER_ID, values)
        id = returnedId as Int // Gets id
        database.close()
        return id
    }

    /**
     * Sets time and date of the reminder in the bottom sheet
     */
    private fun setReminderDate() {
        if (selected_year != null && selected_month != null && selected_dayOfMonth != null) { //Checks if date is empty
            if (dateLayoutContainer.getVisibility() == View.GONE || timeLayoutContainer.getVisibility() == View.GONE) {
                //Checks if the views related with date and time are hidden
                showDateFields()
            }
            val calendarUtil = CalendarUtil(getContext(), selected_year, selected_month, selected_dayOfMonth, selected_hourOfDay, selected_minute)
            val date: String = calendarUtil.getDateFormat(DateFormat.LONG)
            val time: String = calendarUtil.getTimeFormat(DateFormat.SHORT)
            // Sets time and date
            dateTextView.setText(date)
            timeTextView.setText(time)
            bottomSheetListener.onReminderDateSelected()
        }
    }

    /**
     * Sets views related with time and date visible
     */
    private fun showDateFields() {
        dateLayoutContainer.setVisibility(View.VISIBLE)
        timeLayoutContainer.setVisibility(View.VISIBLE)
    }

    /**
     * Sets views related with time and date gone
     */
    private fun deleteDateFields() {
        dateLayoutContainer.setVisibility(View.GONE)
        timeLayoutContainer.setVisibility(View.GONE)
        selected_year = null
        selected_month = null
        selected_dayOfMonth = null
        selected_hourOfDay = null
        selected_minute = null
    }
}