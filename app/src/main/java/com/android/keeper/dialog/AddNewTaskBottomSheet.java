package com.android.keeper.dialog;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.notifications.AlertReceiver;
import com.android.keeper.notifications.NotificationHelper;
import com.android.keeper.util.CalendarUtil;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNewTaskBottomSheet extends BottomSheetDialogFragment {

    public AddNewTaskBottomSheetListener bottomSheetListener;
    private View fragmentView;
    private ImageButton addDetailsButton,addDateButton,saveTaskButton,deleteTaskDateButton;
    private Button changeTaskDateButton,changeTaskTimeButton;
    private EditText titleEditText,descriptionEditText;
    private SQLiteConnection conn;
    private NotificationHelper notificationHelper;
    private RelativeLayout taskDateContainer;

    private String task_title,task_details;
    private int task_id;
    private Integer selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_add_new_task, container, false);
        conn = new SQLiteConnection(getContext(), "keeper_db", null, 1);
        notificationHelper=new NotificationHelper(getContext());

        addDetailsButton= fragmentView.findViewById(R.id.task_add_details);
        addDateButton= fragmentView.findViewById(R.id.task_add_date);
        saveTaskButton= fragmentView.findViewById(R.id.task_save);

        deleteTaskDateButton= fragmentView.findViewById(R.id.task_delete_date);
        changeTaskDateButton= fragmentView.findViewById(R.id.task_date);
        changeTaskTimeButton= fragmentView.findViewById(R.id.task_time);
        //EditTexts
        descriptionEditText= fragmentView.findViewById(R.id.task_details);
        titleEditText= fragmentView.findViewById(R.id.task_title);
        //Layouts
        taskDateContainer=fragmentView.findViewById(R.id.task_date_container);

        changeTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onChangeDateListener);
                datePicker.show(getFragmentManager(),"date picker");
            }
        });

        changeTaskTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogFragment timePicker=new TimePickerDialogFragment();
                timePicker.setCallBack(onChangeTimeListener);
                timePicker.show(getFragmentManager(),"time picker");
            }
        });

        deleteTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDateFields();
            }
        });

        addDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(descriptionEditText.getVisibility()==View.GONE){
                    descriptionEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setCallBack(onDateSetListener);
                datePicker.setOnDismissListener(onDateDismissListener);
                datePicker.show(getFragmentManager(),"date picker");

            }
        });
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: MAKE SOME TEST, TO CHECK IF THE NOTIFICATION ALARMS DO NOT OVERLAP
                task_title=titleEditText.getText().toString();
                if(descriptionEditText.getVisibility()== View.VISIBLE){
                    task_details=descriptionEditText.getText().toString();
                }else{ task_details="";}

                if(task_title.isEmpty()){
                    bottomSheetListener.onEmptyTaskTitle();
                }else{
                    task_id=saveTask();
                    setNotificationAlarm(notificationHelper.getTaskNotification(task_title,task_details),task_id);
                    bottomSheetListener.onAddTask(task_id,task_title,task_details);
                    dismiss();
                }
            }
        });
        deleteTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDateFields();
            }
        });

        return fragmentView;
    }

    private int saveTask(){
        int id;
        long returnedId;
        SQLiteDatabase database=conn.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(TasksUtilities.COLUMN_TASK_TITLE,task_title);
        values.put(TasksUtilities.COLUMN_TASK_DETAILS,task_details);
        values.put(TasksUtilities.COLUMN_TASK_YEAR,selected_year);
        values.put(TasksUtilities.COLUMN_TASK_MONTH,selected_month);
        values.put(TasksUtilities.COLUMN_TASK_DAY,selected_dayOfMonth);
        values.put(TasksUtilities.COLUMN_TASK_HOUR,selected_hourOfDay);
        values.put(TasksUtilities.COLUMN_TASK_MINUTE,selected_minute);

        returnedId=database.insert(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID,values);
        id=(int) returnedId;

        database.close();
        return id;
    }

    public void setTaskDate(){
        if(selected_year!=null && selected_month!=null && selected_dayOfMonth!=null){
            if(deleteTaskDateButton.getVisibility()==View.GONE || changeTaskDateButton.getVisibility()==View.GONE){
                showDateFields();
            }

            CalendarUtil calendarUtil=new CalendarUtil(selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute);
            String date= calendarUtil.getDateFormat(DateFormat.FULL);
            String time=calendarUtil.getTimeFormat(DateFormat.SHORT);
            changeTaskDateButton.setText(date);
            changeTaskTimeButton.setText(time);
            bottomSheetListener.onTaskDateSelected();
        }
    }

    private void setNotificationAlarm(Notification notification,int task_id){
        //TODO:FIX ERROR THAT SHOW THE SAME CONTENT TO EVERY NOTIFICATION(SOMETIMES)
        //TODO:ADD METHOD TO DELETE/CANCEL NOTIFICAION
        if(selected_year==null && selected_month==null && selected_dayOfMonth==null){
            return ;
        }else{
            calendar=Calendar.getInstance();
            calendar.set(Calendar.YEAR,selected_year);
            calendar.set(Calendar.MONTH,selected_month);
            calendar.set(Calendar.DAY_OF_MONTH,selected_dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY,selected_hourOfDay);
            calendar.set(Calendar.MINUTE,selected_minute);
            calendar.set(Calendar.SECOND,0);

            AlarmManager alarmManager=(AlarmManager) fragmentView.getContext().getSystemService(Context.ALARM_SERVICE);

            String channelTaskID=NotificationHelper.channelTaskID;
            String channelTaskName=NotificationHelper.channelTasksName;

            Intent intent=new Intent(getContext(), AlertReceiver.class);
            intent.putExtra(AlertReceiver.NOTIFICATION_ID , task_id ) ;
            intent.putExtra(AlertReceiver.NOTIFICATION,notification);
            intent.putExtra(AlertReceiver.NOTIFICATION_CHANNEL_ID,channelTaskID);
            intent.putExtra(AlertReceiver.NOTIFICATION_CHANNEL_NAME,channelTaskName);

            PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),1,intent,0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }

    private void showDateFields(){
        deleteTaskDateButton.setVisibility(View.VISIBLE);
        changeTaskDateButton.setVisibility(View.VISIBLE);
        changeTaskTimeButton.setVisibility(View.VISIBLE);
            taskDateContainer.setVisibility(View.VISIBLE);
    }

    private void deleteDateFields(){
        changeTaskDateButton.setText("");
        changeTaskTimeButton.setText("");
        deleteTaskDateButton.setVisibility(View.GONE);
        changeTaskDateButton.setVisibility(View.GONE);
        changeTaskTimeButton.setVisibility(View.GONE);

        taskDateContainer.setVisibility(View.GONE);

        selected_year=0;selected_month=0;selected_dayOfMonth=0;selected_hourOfDay=0;selected_minute=0;
    }

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

    private DialogInterface.OnDismissListener onDateDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            selected_year=null;
            selected_month=null;
            selected_dayOfMonth=null;
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
            selected_year=null;
            selected_month=null;
            selected_dayOfMonth=null;
            selected_minute=null;
            selected_hourOfDay=null;
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

    /*
    * Methods with the porpouse to create communication
    * between fragments
    * */
    public interface AddNewTaskBottomSheetListener{
        void onAddTask(int task_id,String task_title,String task_details);
        void onEmptyTaskTitle();
        void onTaskDateSelected();
    }

    public void setBottomSheetListener(AddNewTaskBottomSheetListener listener){
        bottomSheetListener=listener;
    }
}
