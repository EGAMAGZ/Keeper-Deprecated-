package com.android.keeper.dialog;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.RemindersUtilities;
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.notifications.AlertReceiver;
import com.android.keeper.util.CalendarUtil;
import com.android.keeper.util.CursorUtil;

import java.text.DateFormat;
import java.util.Calendar;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private EditTaskBottomSheetListener bottomSheetListener;
    private View bottomSheetView;
    private EditText taskTitleEditText,taskDetailsEditText;
    private ImageButton saveTaskButton,deleteTaskButton, addTaskDateButton,deleteTaskDateButton;
    private Button changeTaskDateButton,changeTaskTimeButton;
    private SQLiteConnection conn;

    private String old_task_title,old_task_details;
    private int old_task_id,old_task_position;
    private Integer selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_edit_task,container,false);
        conn = new SQLiteConnection(getContext(), "keeper_db", null, 1);

        changeTaskDateButton=bottomSheetView.findViewById(R.id.task_date);
        changeTaskTimeButton=bottomSheetView.findViewById(R.id.task_time);
        addTaskDateButton = bottomSheetView.findViewById(R.id.task_add_date);
        deleteTaskDateButton=bottomSheetView.findViewById(R.id.task_delete_date);

        taskTitleEditText=bottomSheetView.findViewById(R.id.task_title);
        taskDetailsEditText=bottomSheetView.findViewById(R.id.task_details);
        saveTaskButton=bottomSheetView.findViewById(R.id.task_save);
        deleteTaskButton=bottomSheetView.findViewById(R.id.task_delete);

        loadDate(old_task_id);
        setTaskDate();
        taskTitleEditText.setText(old_task_title);
        if(!old_task_details.isEmpty()){
            taskDetailsEditText.setText(old_task_details);
        }

        changeTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_year!=null && selected_month!=null && selected_dayOfMonth!=null){

                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onChangeDateListener);
                    datePicker.show(getFragmentManager(),"date_picker");
                }else{
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onDateSetListener);
                    datePicker.setOnDismissListener(onDateDismissListener);
                    datePicker.show(getFragmentManager(),"date_picker");
                }
            }
        });

        changeTaskTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_year!=null && selected_month!=null && selected_dayOfMonth!=null){
                    TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
                    timePicker.setCallBack(onChangeTimeListener);
                    timePicker.show(getFragmentManager(),"time_picker");
                }else{
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onDateSetListener);
                    datePicker.setOnDismissListener(onDateDismissListener);
                    datePicker.show(getFragmentManager(),"date_picker");
                }
            }
        });

        addTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onDateSetListener);
                datePicker.setOnDismissListener(onDateDismissListener);
                datePicker.show(getFragmentManager(),"date picker");
            }
        });

        deleteTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_year=null;
                selected_month=null;
                selected_dayOfMonth=null;
                selected_hourOfDay=null;
                selected_minute=null;
                changeTaskDateButton.setText("FECHAS");
                changeTaskTimeButton.setText("00:00");
            }
        });

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskTitleEditText.getText().toString().isEmpty()){
                    bottomSheetListener.onEmptyTaskTitle();
                }else{
                    saveEditedTask();
                    String new_task_title=taskTitleEditText.getText().toString();
                    String new_task_details=taskDetailsEditText.getText().toString();
                    //TODO: ADD A METHOD TO CHANGE ALARM AND/OR CANCEL THE PREVIOUS VERSION OF IT
                    bottomSheetListener.onSaveEditedTask(old_task_position,new_task_title,new_task_details);
                    dismiss();
                }
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: MAKE SOME TEST, TO PROOF THAT IS CANCEL ONLY THE TASK SELECTED
                deleteTask();
                bottomSheetListener.onDeleteSavedTask(old_task_position);
                cancelNotificationAlarm();//TODO: CHECK WHEN A TASK HAS A NOTIFICATION ALARM
                dismiss();
            }
        });
        return bottomSheetView;
    }

    private void loadDate(int task_id){
        String[] columns={TasksUtilities.COLUMN_TASK_YEAR,TasksUtilities.COLUMN_TASK_MONTH,TasksUtilities.COLUMN_TASK_DAY,TasksUtilities.COLUMN_TASK_HOUR,TasksUtilities.COLUMN_TASK_MINUTE};

        SQLiteDatabase database=conn.getReadableDatabase();
        Cursor cursor=database.query(TasksUtilities.TABLE_NAME,columns,TasksUtilities.COLUMN_TASK_ID+" = "+task_id,null,null,null,null);
        cursor.moveToFirst();//TODO: CHECK WHY IS NEEDED TO CALL THIS METHOD TO READ THE ELEMENTS FROM QUERY, AND HOW TO DELETE IT

        CursorUtil cursorUtil=new CursorUtil(cursor);

        selected_year=cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_YEAR);
        selected_month=cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_MONTH);
        selected_dayOfMonth=cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_DAY);
        selected_hourOfDay=cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_HOUR);
        selected_minute=cursorUtil.checkNullInteger(TasksUtilities.COLUMN_TASK_MINUTE);

        cursor.close();
        database.close();
    }

    private void setTaskDate(){
        if(selected_year!=null && selected_month!=null && selected_dayOfMonth!=null){
            CalendarUtil calendarUtil=new CalendarUtil(getContext(),selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute);
            String date=calendarUtil.getDateFormat(DateFormat.FULL);
            String time=calendarUtil.getTimeFormat(DateFormat.SHORT);
            changeTaskDateButton.setText(date);
            changeTaskTimeButton.setText(time);
        }
    }

    public void setContent(int position, int task_id, String task_title, String task_details){
        old_task_position=position;
        old_task_id=task_id;
        old_task_title=task_title;
        old_task_details=task_details;
    }

    private void saveEditedTask(){
        SQLiteDatabase database=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_TITLE,taskTitleEditText.getText().toString());
        values.put(TasksUtilities.COLUMN_TASK_DETAILS,taskDetailsEditText.getText().toString());

        values.put(TasksUtilities.COLUMN_TASK_YEAR,selected_year);
        values.put(TasksUtilities.COLUMN_TASK_MONTH,selected_month);
        values.put(TasksUtilities.COLUMN_TASK_DAY,selected_dayOfMonth);

        values.put(TasksUtilities.COLUMN_TASK_HOUR,selected_hourOfDay);
        values.put(TasksUtilities.COLUMN_TASK_MINUTE,selected_minute);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+old_task_id,null);
        database.close();
    }

    private void deleteTask(){
        SQLiteDatabase database=conn.getWritableDatabase();
        database.delete(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID+"="+old_task_id,null);
        database.close();
    }

    private void cancelNotificationAlarm(){
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(), 1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /* ----- Listeners when time and date are selected ----- */

    private DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selected_year=year;
            selected_month=month;
            selected_dayOfMonth=dayOfMonth;

            TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
            timePicker.setCallBack(onTimeSetListener);
            timePicker.setOnDismissListener(onTimeDismissListener);
            timePicker.show(getFragmentManager(),"time picker");
        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            selected_hourOfDay=hourOfDay;
            selected_minute=minute;
            setTaskDate();
        }
    };

    /* ----- Listeners when time or date in not selected */

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

    private DialogInterface.OnDismissListener onTimeDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            /* In case of dismiss, the variables related with date will set a value
             * of 0 and will not continue with the next step (display Time Picker Dialog and set time)
             * */
            selected_year=null;
            selected_month=null;
            selected_dayOfMonth=null;
            selected_minute=null;
            selected_hourOfDay=null;
        }
    };

    /* ----- Listeners when time or date are changed (individually)----- */

    private DatePickerDialog.OnDateSetListener onChangeDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selected_year=year;
            selected_month=month;
            selected_dayOfMonth=dayOfMonth;

            setTaskDate();
        }
    };

    private TimePickerDialog.OnTimeSetListener onChangeTimeListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            selected_hourOfDay=hourOfDay;
            selected_minute=minute;
            setTaskDate();
        }
    };

    public interface EditTaskBottomSheetListener{
        void onSaveEditedTask(int task_position,String task_title,String task_details);
        void onDeleteSavedTask(int task_position);
        void onEmptyTaskTitle();
    }

    public void setEditTaskBottomSheetListener(EditTaskBottomSheetListener listener){
        bottomSheetListener=listener;
    }
}
