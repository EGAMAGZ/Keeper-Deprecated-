package com.android.keeper.dialog;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;

import java.text.DateFormat;
import java.util.Calendar;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private EditTaskBottomSheetListener bottomSheetListener;
    private View fragmentView;
    private EditText taskTitleEditText,taskDetailsEditText;
    private ImageButton saveTaskButton,deleteTaskButton;
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


        taskTitleEditText=fragmentView.findViewById(R.id.task_title);
        taskDetailsEditText=fragmentView.findViewById(R.id.task_details);
        saveTaskButton=fragmentView.findViewById(R.id.task_save);
        deleteTaskButton=fragmentView.findViewById(R.id.task_delete);

        loadDate(old_task_id);
        setDate();
        taskTitleEditText.setText(old_task_title);
        if(!old_task_details.isEmpty()){
            taskDetailsEditText.setText(old_task_details);
        }

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTaskButtonClicked=true;
                if(taskTitleEditText.getText().toString().isEmpty()){
                    bottomSheetListener.OnEmptyTaskTitle();
                }else{
                    bottomSheetListener.OnSaveEditedTask(old_task_position,old_task_id,taskTitleEditText.getText().toString(),taskDetailsEditText.getText().toString());
                    dismiss();
                }
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.OnDeleteSavedTask(old_task_position,old_task_id);
                dismiss();
            }
        });
        return fragmentView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //TODO: MAKE MORE TEST TO THIS METHOD
        super.onDismiss(dialog);
        /*if(taskTitleEditText.getText().toString().isEmpty() || saveTaskButtonClicked){
            return ;
        }else{
            bottomSheetListener.OnSaveEditedTask(old_task_position,old_task_id,taskTitleEditText.getText().toString(),taskDetailsEditText.getText().toString());
        }*/
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

    private void setDate(){
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

    public interface EditTaskBottomSheetListener{
        void OnSaveEditedTask(int task_position,int task_id,String task_title,String task_details);
        void OnDeleteSavedTask(int task_position,int task_id);
        void OnEmptyTaskTitle();
    }

    public void setEditTaskBottomSheetListener(EditTaskBottomSheetListener listener){
        bottomSheetListener=listener;
    }
}
