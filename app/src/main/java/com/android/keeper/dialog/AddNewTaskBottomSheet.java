package com.android.keeper.dialog;

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

public class AddNewTaskBottomSheet extends BottomSheetDialogFragment {

    private View bottomSheetView;
    private ImageButton addDescriptionButton;
    private EditText descriptionEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_add_new_task,container,false);

        addDescriptionButton=bottomSheetView.findViewById(R.id.task_add_description);
        descriptionEditText=bottomSheetView.findViewById(R.id.task_description);

        addDescriptionButton.setOnClickListener(new View.OnClickListener() {
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

        return bottomSheetView;
    }
}
