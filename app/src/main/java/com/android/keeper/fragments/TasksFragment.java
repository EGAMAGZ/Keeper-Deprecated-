package com.android.keeper.fragments;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.adapters.TasksAdapter;
import com.android.keeper.dialog.AddNewTaskBottomSheet;
import com.android.keeper.dialog.EditTaskBottomSheet;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.recycle_items.TaskItem;

public class TasksFragment extends Fragment {

    private ArrayList<TaskItem> tasksList;

    private CoordinatorLayout coordinatorLayout;
    private SQLiteConnection conn;
    private View FragmentView;
    private FloatingActionButton AddTaskBtn;
    private ProgressBar TaskProgressBar;
    private TextView TaskPercentage;
    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksRecAdapter;
    //private RecyclerView.Adapter tasksRecAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentView=inflater.inflate(R.layout.fragment_tasks,container,false);

        coordinatorLayout=FragmentView.findViewById(R.id.task_fragment_container);

        tasksRecyclerView=FragmentView.findViewById(R.id.tasks_recyclerview);
        //tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager=new LinearLayoutManager(getContext());

        tasksList=new ArrayList<TaskItem>();
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
        long count;
        String[] columns={TasksUtilities.COLUMN_TASK_TITLE,TasksUtilities.COLUMN_TASK_DETAILS,TasksUtilities.COLUMN_TASK_DONE};
        String selection=null; //This will select all rows

        SQLiteDatabase database=conn.getReadableDatabase();

        try {
            Cursor cursor = database.query(TasksUtilities.TABLE_NAME, columns, selection, null, null, null, TasksUtilities.COLUMN_TASK_ID+" DESC", null);
            count= DatabaseUtils.queryNumEntries(database,TasksUtilities.TABLE_NAME);
            percentageTasks(count,database);
            while(cursor.moveToNext()){
                if(cursor.getInt(2)==0){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,cursor.getString(0),cursor.getString(1)));
                }else if(cursor.getInt(2)==1){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_black_24dp,cursor.getString(0),cursor.getString(1)));
                }
            }

        }
        catch(IllegalStateException e){
            TaskProgressBar.setProgress(0);
            TaskPercentage.setText(0+"%");
            Toast.makeText(getContext(),"Error while reading database",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.e("Keeper Error Logger","ERROR:",e);
            TaskProgressBar.setProgress(0);
            TaskPercentage.setText(0+"%");
            Toast.makeText(getContext(),"Internal Error",Toast.LENGTH_SHORT).show();
        }
        finally {
            tasksRecAdapter=new TasksAdapter(tasksList);
            tasksRecyclerView.setLayoutManager(tasksLayoutManager);
            tasksRecyclerView.setAdapter(tasksRecAdapter);
            tasksRecAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    EditTaskBottomSheet editTaskBottomSheet=new EditTaskBottomSheet();
                    editTaskBottomSheet.setContent(tasksList.get(position).getTaskTitle(),tasksList.get(position).getTaskDetails());
                    editTaskBottomSheet.show(getFragmentManager(),"ediTaskBottomSheet");

                }
            });
            database.close();
        }
    }

    private void percentageTasks(long count,SQLiteDatabase database){
        //TODO: Make test when someone of them are done
        int percentage;
        try{
            int tasksTotal=(int) count;
            int tasksDoneCount=(int) tasksDone(database);
            percentage=(int) ((100*tasksDoneCount)/tasksTotal);
        }catch(ArithmeticException e){
            percentage=0;
        }

        TaskProgressBar.setProgress(percentage);
        TaskPercentage.setText(percentage+"%");
    }
    private long tasksDone(SQLiteDatabase database){
        long count=0;

        try{
            Cursor cursor=database.rawQuery("SELECT * FROM "+TasksUtilities.TABLE_NAME+" WHERE "+TasksUtilities.COLUMN_TASK_DONE+" = 1",null);
            count=cursor.getCount();
            return count;
        }catch(Exception e){
            return count;
        }

    }
    public void OnAddTask(String task_title, String task_details){
        tasksList.add(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_title,task_details));
        tasksRecAdapter.notifyItemInserted(0);
        Snackbar.make(coordinatorLayout,"Task Saved",Snackbar.LENGTH_LONG).show();
    }

    public void OnSaveEditedTask(){
        Snackbar.make(coordinatorLayout,"Task Saved",Snackbar.LENGTH_SHORT).show();
    }

    public void OnDeleteSavedTask(){
        Snackbar.make(coordinatorLayout,"Task Deleted",Snackbar.LENGTH_SHORT).show();
    }
}
