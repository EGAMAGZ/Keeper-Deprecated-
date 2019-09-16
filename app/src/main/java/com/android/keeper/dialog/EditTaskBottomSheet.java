package com.android.keeper.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.keeper.R;

public class EditTaskBottomSheet extends BottomSheetDialogFragment {

    private View fragmentView;

    private String old_task_title,old_task_details;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.bottom_sheet_edit_task,container,false);

        return fragmentView;
    }

    public void setContent(String task_title,String task_details){
        old_task_title=task_title;
        old_task_details=task_details;
    }
}
