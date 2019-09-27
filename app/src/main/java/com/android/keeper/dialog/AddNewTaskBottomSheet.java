package com.android.keeper.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.notifications.NotificationHelper;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNewTaskBottomSheet extends BottomSheetDialogFragment {

    public AddNewTaskBottomSheetListener bottomSheetListener;
    private View bottomSheetView;
    private ImageButton addDetailsButton,addDateButton,saveTaskButton,deleteTaskDateButton;
    private Button changeTaskDateButton,changeTaskTimeButton;
    private EditText titleEditText,descriptionEditText;
    private SQLiteConnection conn;
    private NotificationHelper notificationHelper;

    private String task_title,task_details;
    private int task_id;
    private boolean saveTaskButtonClicked=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_add_new_task,container,false);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);
        notificationHelper=new NotificationHelper(getContext());

        addDetailsButton=bottomSheetView.findViewById(R.id.task_add_details);
        addDateButton=bottomSheetView.findViewById(R.id.task_add_date);
        saveTaskButton=bottomSheetView.findViewById(R.id.task_save);
        deleteTaskDateButton=bottomSheetView.findViewById(R.id.task_delete_date);

        changeTaskDateButton=bottomSheetView.findViewById(R.id.task_date);
        changeTaskTimeButton=bottomSheetView.findViewById(R.id.task_time);

        descriptionEditText=bottomSheetView.findViewById(R.id.task_details);
        titleEditText=bottomSheetView.findViewById(R.id.task_title);

        addDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(descriptionEditText.getVisibility()==View.INVISIBLE){
                    descriptionEditText.setVisibility(View.VISIBLE);
                }
            }
        });


        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getFragmentManager(),"date picker");
            }
        });
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTaskButtonClicked=true;
                task_title=titleEditText.getText().toString();
                if(descriptionEditText.getVisibility()== View.VISIBLE){
                    task_details=descriptionEditText.getText().toString();
                }else{ task_details="";}

                if(task_title.isEmpty()){
                    Toast.makeText(getContext(),"Task Title is Empty",Toast.LENGTH_SHORT).show();
                }else{
                    task_id=saveTask();
                    showNotifications(task_title,task_details);
                    bottomSheetListener.OnAddTask(task_id,task_title,task_details);
                    dismiss();
                }
            }
        });
        deleteTaskDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTaskDateButton.setText("");
                changeTaskTimeButton.setText("");
                deleteTaskDateButton.setVisibility(View.GONE);
                changeTaskDateButton.setVisibility(View.GONE);
                changeTaskTimeButton.setVisibility(View.GONE);
            }
        });

        //TODO: Add onClickListener when date is added, and also the storage of the date in the database

        return bottomSheetView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //TODO: MAKE MORE TEST TO THIS METHOD
        super.onDismiss(dialog);
        task_title=titleEditText.getText().toString();
        if(task_title.isEmpty() || saveTaskButtonClicked){
            return ;
        }else{
            if(descriptionEditText.getVisibility()== View.VISIBLE){
                task_details=descriptionEditText.getText().toString();
            }else{ task_details="";}
            task_id=saveTask();
            bottomSheetListener.OnAddTask(task_id,task_title,task_details);
            dismiss();
        }
    }

    private int saveTask(){
        int id;
        long returnedId;
        SQLiteDatabase database=conn.getWritableDatabase();
        ContentValues values=new ContentValues();

        //String sql="INSERT INTO "+ TasksUtilities.TABLE_NAME +"("+TasksUtilities.COLUMN_TASK_TITLE+","+TasksUtilities.COLUMN_TASK_DETAILS+") VALUES ('"+task_title+"','"+task_details+"')";
        values.put(TasksUtilities.COLUMN_TASK_TITLE,task_title);
        values.put(TasksUtilities.COLUMN_TASK_DETAILS,task_details);

        returnedId=database.insert(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID,values);
        id=(int) returnedId;

        database.close();
        return id;
    }

    public void showTaskDate(int year,int month,int dayOfMonth,int hourOfDay,int minute){
        //TODO: Add individual change to date and time
        //TODO: Create and Assign different methods for each bottom sheet when date and time is chosen
        if(deleteTaskDateButton.getVisibility()==View.GONE || changeTaskDateButton.getVisibility()==View.GONE){
            deleteTaskDateButton.setVisibility(View.VISIBLE);
            changeTaskDateButton.setVisibility(View.VISIBLE);
            changeTaskTimeButton.setVisibility(View.VISIBLE);
        }

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        changeTaskDateButton.setText(date);
        changeTaskTimeButton.setText(hourOfDay+":"+minute);
        Toast.makeText(getContext(),"Task Date Selected"+dayOfMonth,Toast.LENGTH_SHORT).show();
    }

    public void showNotifications(String title, String message){
        NotificationCompat.Builder nb=notificationHelper.getTaskChannelNotification(title,message);
        notificationHelper.getManager().notify(1,nb.build());
    }

    /*
    * Methods with the porpouse to create communication
    * between fragments
    * */
    public interface AddNewTaskBottomSheetListener{
        void OnAddTask(int task_id,String task_title,String task_details);
    }

    public void setBottomSheetListener(AddNewTaskBottomSheetListener listener){
        bottomSheetListener=listener;
    }
}
