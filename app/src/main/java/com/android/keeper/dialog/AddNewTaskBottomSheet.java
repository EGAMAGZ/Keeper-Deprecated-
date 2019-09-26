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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;

public class AddNewTaskBottomSheet extends BottomSheetDialogFragment {

    public AddNewTaskBottomSheetListener bottomSheetListener;
    private View bottomSheetView;
    private ImageButton addDetailsButton,addDateButton,saveTaskButton;
    private EditText titleEditText,descriptionEditText;
    private SQLiteConnection conn;

    private String task_title,task_details;
    private int task_id;
    private boolean saveTaskButtonClicked=false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_add_new_task,container,false);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);

        addDetailsButton=bottomSheetView.findViewById(R.id.task_add_details);
        addDateButton=bottomSheetView.findViewById(R.id.task_add_date);
        saveTaskButton=bottomSheetView.findViewById(R.id.task_save);

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
                    bottomSheetListener.OnAddTask(task_id,task_title,task_details);
                    dismiss();
                }
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
