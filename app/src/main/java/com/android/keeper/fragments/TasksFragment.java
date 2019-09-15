package com.android.keeper.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.dialog.AddNewTaskBottomSheet;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;

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
                AddNewTaskBottomSheet addNewTaskBottomSheet=new AddNewTaskBottomSheet();
                addNewTaskBottomSheet.show(getFragmentManager(),"addNewTaskBottomSheet");
            }
        });

        loadTasks();

        return FragmentView;
    }
    //Remember: All the methods that will be called from MainActivity must be public
    public void loadTasks(){
        SQLiteDatabase database=conn.getReadableDatabase();
        //String[] parameters=[];
        String[] columns={TasksUtilities.COLUMN_TASK_TITLE,TasksUtilities.COLUMN_TASK_DETAILS};
        String selection=null; //This will select all rows
        try {
            Cursor cursor = database.query(TasksUtilities.TABLE_NAME, columns, selection, null, null, null, null, null);
            cursor.moveToFirst();
            long count= DatabaseUtils.queryNumEntries(database,TasksUtilities.TABLE_NAME);
            percentageTasks(count,database);
            Toast.makeText(getContext(),"Number:"+ count,Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getContext(),"Error Consulta",Toast.LENGTH_SHORT).show();
        }
        finally {
            database.close();
        }
    }

    private void percentageTasks(long count,SQLiteDatabase database){
        //TODO: Make test when someone of them are done
        int tasksTotal=(int) count;
        int tasksDoneCount=(int) tasksDone(database);
        int percentage=(int) ((100*tasksDoneCount)/tasksTotal);
        TaskProgressBar.setProgress(percentage);
        TaskPercentage.setText(percentage+"%");
    }
    private long tasksDone(SQLiteDatabase database){
        long count=0;
        try{
            Cursor cursor=database.rawQuery("SELECT * FROM "+TasksUtilities.TABLE_NAME+" WHERE "+TasksUtilities.COLUMN_TASK_DONE+" = 1",null);
            cursor.moveToFirst();
            count=cursor.getCount();
            return count;
        }catch(Exception e){
            return count;
        }

    }
    public void OnSavedTask(){
    }

}
