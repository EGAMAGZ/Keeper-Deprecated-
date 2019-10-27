package com.android.keeper.dialog;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.notifications.AlertReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private EditTaskBottomSheetListener bottomSheetListener;
    private View fragmentView;
    private EditText taskTitleEditText,taskDetailsEditText;
    private ImageButton saveTaskButton,deleteTaskButton, addTaskDateButton,deleteTaskDateButton;
    private Button changeTaskDateButton,changeTaskTimeButton;
    private SQLiteConnection conn;

    private String old_task_title,old_task_details;
    private int old_task_id,old_task_position,selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute;;
    private boolean saveTaskButtonClicked=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.bottom_sheet_edit_task,container,false);
        //selected_year=0;selected_month=0;selected_dayOfMonth=0;selected_hourOfDay=0;selected_minute=0;
        conn = new SQLiteConnection(getContext(), "keeper_db", null, 1);

        changeTaskDateButton=fragmentView.findViewById(R.id.task_date);
        changeTaskTimeButton=fragmentView.findViewById(R.id.task_time);
        addTaskDateButton = fragmentView.findViewById(R.id.task_add_date);
        deleteTaskDateButton=fragmentView.findViewById(R.id.task_delete_date);

        taskTitleEditText=fragmentView.findViewById(R.id.task_title);
        taskDetailsEditText=fragmentView.findViewById(R.id.task_details);
        saveTaskButton=fragmentView.findViewById(R.id.task_save);
        deleteTaskButton=fragmentView.findViewById(R.id.task_delete);

        loadDate(old_task_id);
        setTaskDate();
        taskTitleEditText.setText(old_task_title);
        if(!old_task_details.isEmpty()){
            taskDetailsEditText.setText(old_task_details);
        }

        changeTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_year!=0 && selected_month!=0 && selected_dayOfMonth!=0){

                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onChangeDateListener);
                    datePicker.show(getFragmentManager(),"date picker");
                }else{
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onDateSetListener);
                    datePicker.setOnDismissListener(onDateDismissListener);//TODO: ADD ONDISMISSLISTENER
                    datePicker.show(getFragmentManager(),"date picker");
                }
            }
        });

        changeTaskTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_year!=0 && selected_month!=0 && selected_dayOfMonth!=0){

                    TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
                    timePicker.setCallBack(onChangeTimeListener);
                    timePicker.show(getFragmentManager(),"time picker");
                }else{
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.setCallBack(onDateSetListener);
                    datePicker.setOnDismissListener(onDateDismissListener);//TODO: ADD ONDISMISSLISTENER
                    datePicker.show(getFragmentManager(),"date picker");
                }
            }
        });

        addTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onDateSetListener);
                datePicker.setOnDismissListener(onDateDismissListener);//TODO: ADD ONDISMISSLISTENER
                datePicker.show(getFragmentManager(),"date picker");

            }
        });

        deleteTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_year=0;selected_month=0;selected_dayOfMonth=0;selected_hourOfDay=0;selected_minute=0;
                changeTaskDateButton.setText("FECHAS");
                changeTaskTimeButton.setText("00:00");
            }
        });

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTaskButtonClicked=true;
                if(taskTitleEditText.getText().toString().isEmpty()){
                    bottomSheetListener.OnEmptyTaskTitle();
                }else{
                    saveEditedTask();
                    //TODO: ADD A METHOD TO CHANGE ALARM AND/OR CANCEL THE PREVIOUS VERSION OF IT
                    bottomSheetListener.OnSaveEditedTask(old_task_position,old_task_id,taskTitleEditText.getText().toString(),taskDetailsEditText.getText().toString());
                    dismiss();
                }
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: MAKE SOME TEST, TO PROOF THAT IS CANCEL ONLY THE TASK SELECTED
                bottomSheetListener.OnDeleteSavedTask(old_task_position,old_task_id);
                cancelNotificationAlarm();//TODO: CHECK WHEN A TASK HAS A NOTIFICATION ALARM
                dismiss();
            }
        });
        return fragmentView;
    }


    private DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selected_year=year;
            selected_month=month;
            selected_dayOfMonth=dayOfMonth;

            TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
            timePicker.setCallBack(onTimeSetListener);
            timePicker.setOnDismissListener(onTimeDismissListener);//TODO: ADD ONDISMISSLISTENER
            timePicker.show(getFragmentManager(),"time picker");
        }
    };

    private DialogInterface.OnDismissListener onDateDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            selected_year=0;selected_month=0;selected_dayOfMonth=0;
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

    private DialogInterface.OnDismissListener onTimeDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            selected_year=0;selected_month=0;selected_dayOfMonth=0;selected_minute=0;selected_hourOfDay=0;
        }
    };

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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void loadDate(int task_id){
        String[] columns={TasksUtilities.COLUMN_TASK_YEAR,TasksUtilities.COLUMN_TASK_MONTH,TasksUtilities.COLUMN_TASK_DAY,TasksUtilities.COLUMN_TASK_HOUR,TasksUtilities.COLUMN_TASK_MINUTE};

        SQLiteDatabase database=conn.getReadableDatabase();
        Cursor cursor=database.query(TasksUtilities.TABLE_NAME,columns,TasksUtilities.COLUMN_TASK_ID+" = "+task_id,null,null,null,null);
        cursor.moveToFirst();//TODO: CHECK WHY IS NEEDED TO CALL THIS METHOD TO READ THE ELEMENTS FROM QUERY, AND HOW TO DELETE IT

        selected_year=cursor.getInt(0);
        selected_month=cursor.getInt(1);
        selected_dayOfMonth=cursor.getInt(2);
        selected_hourOfDay=cursor.getInt(3);
        selected_minute=cursor.getInt(4);

        database.close();
    }

    private void setTaskDate(){
        if(selected_year!=0 && selected_month!=0 && selected_dayOfMonth!=0){

            Calendar calendar= Calendar.getInstance();
            calendar.set(Calendar.YEAR,selected_year);
            calendar.set(Calendar.MONTH,selected_month);
            calendar.set(Calendar.DAY_OF_MONTH,selected_dayOfMonth);

            String date= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            changeTaskDateButton.setText(date);
            changeTaskTimeButton.setText(selected_hourOfDay+":"+selected_minute);//TODO:ADD A FORMAT FOR TIME
        }
    }

    public void setContent(int position, int task_id, String task_title, String task_details){
        old_task_position=position;
        old_task_id=task_id;
        old_task_title=task_title;
        old_task_details=task_details;
    }

    private void saveEditedTask(){
        SQLiteDatabase database=conn.getReadableDatabase();

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

    private void cancelNotificationAlarm(){
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(), 1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public interface EditTaskBottomSheetListener{
        void OnSaveEditedTask(int task_position,int task_id,String task_title,String task_details);
        void OnDeleteSavedTask(int task_position,int task_id);
        void OnEmptyTaskTitle();
    }

    public void setEditTaskBottomSheetListener(EditTaskBottomSheetListener listener){
        bottomSheetListener=listener;
    }
}
