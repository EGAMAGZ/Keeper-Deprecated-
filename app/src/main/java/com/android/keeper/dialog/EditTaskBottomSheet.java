package com.android.keeper.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.keeper.R;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private EditTaskBottomSheetListener bottomSheetListener;
    private View fragmentView;
    private EditText taskTitleEditText,taskDetailsEditText;
    private ImageButton saveTaskButton,deleteTaskButton;

    private String old_task_title,old_task_details;
    private int old_task_id,old_task_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.bottom_sheet_edit_task,container,false);

        taskTitleEditText=fragmentView.findViewById(R.id.task_title);
        taskDetailsEditText=fragmentView.findViewById(R.id.task_details);
        saveTaskButton=fragmentView.findViewById(R.id.task_save);
        deleteTaskButton=fragmentView.findViewById(R.id.task_delete);

        taskTitleEditText.setText(old_task_title);
        if(!old_task_details.isEmpty()){
            taskDetailsEditText.setText(old_task_details);
        }

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.OnSaveEditedTask(old_task_id);
                dismiss();
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

    public void setContent(int position,int task_id,String task_title,String task_details){
        old_task_position=position;
        old_task_id=task_id;
        old_task_title=task_title;
        old_task_details=task_details;
    }

    public interface EditTaskBottomSheetListener{
        void OnSaveEditedTask(int task_id);
        void OnDeleteSavedTask(int task_position,int task_id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        bottomSheetListener=(EditTaskBottomSheetListener) context;
    }
}
