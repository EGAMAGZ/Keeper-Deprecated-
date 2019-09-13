package com.android.keeper.dialog;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;

public class AddNewTaskBottomSheet extends BottomSheetDialogFragment {

    private View bottomSheetView;
    private ImageButton addDetailsButton,saveTaskButton;
    private EditText titleEditText,descriptionEditText;
    private SQLiteConnection conn;

    private String task_title,task_details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_add_new_task,container,false);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);

        addDetailsButton=bottomSheetView.findViewById(R.id.task_add_description);
        saveTaskButton=bottomSheetView.findViewById(R.id.task_save);

        descriptionEditText=bottomSheetView.findViewById(R.id.task_details);
        titleEditText=bottomSheetView.findViewById(R.id.task_title);

        addDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(descriptionEditText.getVisibility()==View.INVISIBLE){
                    descriptionEditText.setVisibility(View.VISIBLE);
                }else if(descriptionEditText.getVisibility()==View.VISIBLE){
                    descriptionEditText.setText("");
                    descriptionEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task_title=titleEditText.getText().toString();
                if(descriptionEditText.getVisibility()== View.VISIBLE){
                    task_details=descriptionEditText.getText().toString();
                }else{ task_details="";}

                if(task_title.isEmpty()){
                    Toast.makeText(getContext(),"Task Title is Empty",Toast.LENGTH_SHORT).show();
                }else{
                    saveTask();
                    Toast.makeText(getContext(),"Task will be saved",Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });


        return bottomSheetView;
    }
    private void saveTask(){
        SQLiteDatabase database=conn.getWritableDatabase();
        String sql="INSERT INTO "+ TasksUtilities.TABLE_NAME +"("+TasksUtilities.COLUMN_TASK_TITLE+","+TasksUtilities.COLUMN_TASK_DETAILS+") VALUES ('"+task_title+"','"+task_details+"')";

        database.execSQL(sql);
        database.close();
    }
}
