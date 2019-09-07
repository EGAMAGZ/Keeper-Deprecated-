package com.android.keeper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.keeper.AddTaskActivity;
import com.android.keeper.R;

public class TasksFragment extends Fragment {

    private View fragmentView;
    private FloatingActionButton addTaskBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.fragment_tasks,container,false);

        addTaskBtn =(FloatingActionButton) fragmentView.findViewById(R.id.addtask_flt_btn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        return fragmentView;
    }
}
