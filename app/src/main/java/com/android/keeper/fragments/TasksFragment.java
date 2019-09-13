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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;

public class TasksFragment extends Fragment {

    private View FragmentView;
    private FloatingActionButton AddTaskBtn;
    private SQLiteConnection conn;
    private ProgressBar TaskProgressBar;
    private TextView TaskPercentage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentView=inflater.inflate(R.layout.fragment_tasks,container,false);
        TaskProgressBar=(ProgressBar) FragmentView.findViewById(R.id.task_progress_bar);
        TaskPercentage=(TextView) FragmentView.findViewById(R.id.task_progress_percentage);

        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);

        AddTaskBtn =(FloatingActionButton) FragmentView.findViewById(R.id.addtask_flt_btn);
        AddTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return ;
            }
        });

        return FragmentView;
    }

}
