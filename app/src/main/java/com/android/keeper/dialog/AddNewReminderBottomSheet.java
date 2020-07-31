package com.android.keeper.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.RemindersUtilities;
import com.android.keeper.util.CalendarUtil;

import java.text.DateFormat;

/**
 * Represents a BottomSheet that will be displayed when a new reminder will be added
 *
 * @author Gamaliel Garcia
 * */
public class AddNewReminderBottomSheet extends BottomSheetDialogFragment {

    public AddNewReminderBottomSheetListener bottomSheetListener;
    private View bottomSheetView;
    private SQLiteConnection sqLiteConnection;
    private ImageButton saveReminderButton,addDateButton,deleteDateButton;
    private EditText titleEditText;
    private TextView dateTextView, timeTextView;
    private LinearLayout dateLayoutContainer,timeLayoutContainer;

    private String reminder_title;
    private int reminder_id;
    private Integer selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute;

    public interface AddNewReminderBottomSheetListener{
        /**
         * Adds new reminder to the list
         * @param reminder_id Id of the reminder when it's stored
         * @param reminder_title Title of reminder
         * @param year Year of the reminder
         * @param month Month of the reminder
         * @param day Day of the reminder
         * @param hour Hour of the reminder
         * @param minute Minute of rhe reminder
         * */
        void onAddReminder(int reminder_id,String reminder_title,Integer year,Integer month,Integer day,Integer hour,Integer minute);
        /**
         * Advices to the user when the title of reminder is empty
         * */
        void onEmptyReminderTitle();
        /**
         * Advices the user when date and time is selected
         * */
        void onReminderDateSelected();
    }

    /**
     * Sets interface for this bottom sheet
     * @param listener Interface of AddNewReminderBottomSheet
     * */
    public void setBottomSheetListener(AddNewReminderBottomSheetListener listener){
        bottomSheetListener=listener;
    }

    /* ----- Listeners when time and date are selected ----- */

    /**
     * Listener when the user selects a date to the reminder
     * */
    private DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selected_year=year;
            selected_month=month;
            selected_dayOfMonth=dayOfMonth;
            /* Once the user selects date, will be display the time picker dialog */
            TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
            timePicker.setCallBack(onTimeSetListener);
            timePicker.setOnDismissListener(onTimeDismissListener);
            timePicker.show(getFragmentManager(),"time_picker");
        }
    };

    /**
     * Listener when the user selects the time to the reminder
     * */
    private TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            selected_hourOfDay=hourOfDay;
            selected_minute=minute;
            /* Once the user selects the time, date and time will be displayed in the views related with them*/
            setReminderDate();
        }
    };

    /* ----- Listeners when time or date in not selected ----- */

    /**
     * Listener when the user dismiss the date picker
     * */
    private DialogInterface.OnDismissListener onDateDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            /* In case of dismiss, the variables related with date will set a value
             * of 0 and will not continue with the next step (display Time Picker Dialog and set time)
             * */
            selected_year=null;
            selected_month=null;
            selected_dayOfMonth=null;
        }
    };

    /**
     * Listener when the user dismiss the time picker
     * */
    private DialogInterface.OnDismissListener onTimeDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            /* In case of dismiss, the variables related with date and time will set a value
             * of 0 and will not continue with the next step (display date and time)
             * */
            selected_year=null;
            selected_month=null;
            selected_dayOfMonth=null;
            selected_minute=null;
            selected_hourOfDay=null;
        }
    };

    /* ----- Listeners when time or date are changed (individually)----- */

    /**
     * Listener when the user updates the date
     * */
    private DatePickerDialog.OnDateSetListener onChangeDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selected_year=year;
            selected_month=month;
            selected_dayOfMonth=dayOfMonth;

            setReminderDate();
        }
    };

    /**
     * Listener when the user updates the time
     * */
    private TimePickerDialog.OnTimeSetListener onChangeTimeListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            selected_hourOfDay=hourOfDay;
            selected_minute=minute;

            setReminderDate();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView =inflater.inflate(R.layout.bottom_sheet_add_new_reminder,container,false); // Gets View
        sqLiteConnection=new SQLiteConnection(getContext(),"keeper_db",null,1); // SQL Connection
        // ----- Layout -----
        dateLayoutContainer= bottomSheetView.findViewById(R.id.reminder_date_container);
        timeLayoutContainer= bottomSheetView.findViewById(R.id.reminder_time_container);
        // ----- Buttons -----
        saveReminderButton= bottomSheetView.findViewById(R.id.reminder_save);
        addDateButton= bottomSheetView.findViewById(R.id.reminder_add_date);
        deleteDateButton= bottomSheetView.findViewById(R.id.reminder_delete_date);
        // ----- EditTexts -----
        titleEditText= bottomSheetView.findViewById(R.id.reminder_title);
        // ----- TextViews -----
        dateTextView= bottomSheetView.findViewById(R.id.reminder_date);
        timeTextView= bottomSheetView.findViewById(R.id.reminder_time);
        // ----- setListeners -----
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onChangeDateListener);
                datePicker.show(getFragmentManager(),"date_picker");
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
                timePicker.setCallBack(onChangeTimeListener);
                timePicker.show(getFragmentManager(),"time_picker");
            }
        });

        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_title=titleEditText.getText().toString();
                if(reminder_title.isEmpty()){
                    bottomSheetListener.onEmptyReminderTitle();
                }else{
                    reminder_id=saveReminder();
                    bottomSheetListener.onAddReminder(reminder_id,reminder_title,selected_year,selected_month,
                            selected_dayOfMonth,selected_hourOfDay,selected_minute);
                    dismiss();
                }
            }
        });

        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onDateSetListener);
                datePicker.setOnDismissListener(onDateDismissListener);
                datePicker.show(getFragmentManager(),"date_picker");
            }
        });

        deleteDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDateFields();
            }
        });

        return bottomSheetView;
    }

    /**
     * Stores the title,date and time in the database
     * */
    private int saveReminder(){
        int id;
        long returnedId;
        SQLiteDatabase database=sqLiteConnection.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(RemindersUtilities.COLUMN_REMINDER_TITLE,reminder_title);
        // DATE
        values.put(RemindersUtilities.COLUMN_REMINDER_YEAR,selected_year);
        values.put(RemindersUtilities.COLUMN_REMINDER_MONTH,selected_month);
        values.put(RemindersUtilities.COLUMN_REMINDER_DAY,selected_dayOfMonth);
        // TIME
        values.put(RemindersUtilities.COLUMN_REMINDER_HOUR,selected_hourOfDay);
        values.put(RemindersUtilities.COLUMN_REMINDER_MINUTE,selected_minute);

        returnedId=database.insert(RemindersUtilities.TABLE_NAME,RemindersUtilities.COLUMN_REMINDER_ID,values);
        id=(int) returnedId; // Gets id

        database.close();
        return id;
    }

    /**
     * Sets time and date of the reminder in the bottom sheet
     * */
    private void setReminderDate(){
        if(selected_year!=null && selected_month!=null && selected_dayOfMonth!=null){ //Checks if date is empty
            if(dateLayoutContainer.getVisibility()==View.GONE || timeLayoutContainer.getVisibility()==View.GONE){
                //Checks if the views related with date and time are hidden
                showDateFields();
            }
            CalendarUtil calendarUtil=new CalendarUtil(getContext(),selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute);
            String date= calendarUtil.getDateFormat(DateFormat.LONG);
            String time=calendarUtil.getTimeFormat(DateFormat.SHORT);
            // Sets time and date
            dateTextView.setText(date);
            timeTextView.setText(time);
            bottomSheetListener.onReminderDateSelected();
        }
    }
    /**
     * Sets views related with time and date visible
     * */
    private void showDateFields(){
        dateLayoutContainer.setVisibility(View.VISIBLE);
        timeLayoutContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Sets views related with time and date gone
     * */
    private void deleteDateFields(){
        dateLayoutContainer.setVisibility(View.GONE);
        timeLayoutContainer.setVisibility(View.GONE);

        selected_year=null;
        selected_month=null;
        selected_dayOfMonth=null;
        selected_hourOfDay=null;
        selected_minute=null;
    }

}
