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

import com.android.keeper.R;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private EditTaskBottomSheetListener bottomSheetListener;
    private View fragmentView;
    private EditText taskTitleEditText,taskDetailsEditText;
    private ImageButton addDetailsButton;

    private String old_task_title,old_task_details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.bottom_sheet_edit_task,container,false);

        taskTitleEditText=fragmentView.findViewById(R.id.task_title);
        taskDetailsEditText=fragmentView.findViewById(R.id.task_details);

        taskTitleEditText.setText(old_task_title);

        if(!old_task_details.isEmpty()){
            taskDetailsEditText.setVisibility(View.VISIBLE);
            taskDetailsEditText.setText(old_task_details);
        }

        return fragmentView;
    }

    public void setContent(String task_title,String task_details){
        old_task_title=task_title;
        old_task_details=task_details;
    }

    public interface EditTaskBottomSheetListener{
        void OnSaveEditedTask();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        bottomSheetListener=(EditTaskBottomSheetListener) context;
    }
}
