package com.android.keeper.fragments;

import java.util.ArrayList;

import android.content.ContentValues;
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

    public void loadTasks(){
        long count;
        String[] columns={TasksUtilities.COLUMN_TASK_ID,TasksUtilities.COLUMN_TASK_TITLE,TasksUtilities.COLUMN_TASK_DETAILS,TasksUtilities.COLUMN_TASK_DONE};
        String selection=null; //This will select all rows

        SQLiteDatabase database=conn.getReadableDatabase();

        try {
            Cursor cursor = database.query(TasksUtilities.TABLE_NAME, columns, selection, null, null, null, TasksUtilities.COLUMN_TASK_ID+" DESC", null);
            count= DatabaseUtils.queryNumEntries(database,TasksUtilities.TABLE_NAME);

            while(cursor.moveToNext()){
                if(cursor.getInt(3)==0){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
                }else if(cursor.getInt(3)==1){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
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
                    editTaskBottomSheet.setContent(position,tasksList.get(position).getTaskId(),tasksList.get(position).getTaskTitle(),tasksList.get(position).getTaskDetails());
                    editTaskBottomSheet.show(getFragmentManager(),"ediTaskBottomSheet");
                }
            });
            percentageTasks();
            database.close();
        }
    }

    private void percentageTasks(){
        //TODO: Make test when someone of them are done
        SQLiteDatabase database=conn.getReadableDatabase();
        int percentage;
        try{
            int tasksTotal=(int) numberTasksTotal(database);
            int tasksDoneCount=(int) numberTasksDone(database);
            percentage=(int) ((100*tasksDoneCount)/tasksTotal);
        }catch(ArithmeticException e){
            percentage=0;
        }
        TaskProgressBar.setProgress(percentage);
        TaskPercentage.setText(percentage+"%");
        database.close();
    }

    private long numberTasksTotal(SQLiteDatabase database){
        long count=0;
        try {
            Cursor cursor=database.rawQuery("SELECT "+TasksUtilities.COLUMN_TASK_ID+" FROM "+ TasksUtilities.TABLE_NAME,null);
            count=cursor.getCount();
            return count;
        }catch (Exception e){
            return count;
        }
    }

    private long numberTasksDone(SQLiteDatabase database){
        long count=0;
        try{
            Cursor cursor=database.rawQuery("SELECT "+TasksUtilities.COLUMN_TASK_ID+" FROM "+TasksUtilities.TABLE_NAME+" WHERE "+TasksUtilities.COLUMN_TASK_DONE+" = 1",null);
            count=cursor.getCount();
            return count;
        }catch(Exception e){
            return count;
        }
    }

    private void deleteTask(int task_id){
        SQLiteDatabase database=conn.getReadableDatabase();
        database.delete(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);
        for(int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                tasksList.remove(i);
            }
        }
        database.close();
    }
    private void editTask(int task_id,String task_title,String task_details){
        SQLiteDatabase database=conn.getReadableDatabase();

        String sql="UPDATE "+TasksUtilities.TABLE_NAME+" SET "+TasksUtilities.COLUMN_TASK_TITLE+"='"+task_title+"',"+TasksUtilities.COLUMN_TASK_DETAILS+"= '"+task_details+
                "' WHERE "+TasksUtilities.COLUMN_TASK_ID+" = "+task_id;
        database.execSQL(sql);

        for (int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                tasksList.get(i).setTaskTitle(task_title);
                tasksList.get(i).setTaskDetails(task_details);
            }
        }

        database.close();
    }

    public void OnAddTask(int task_id,String task_title, String task_details){
        tasksList.add(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_id,task_title,task_details));
        tasksRecAdapter.notifyItemInserted(0);
        //Toast.makeText(getContext(),"ID"+task_id,Toast.LENGTH_SHORT).show();
        percentageTasks();
        Snackbar.make(coordinatorLayout,"Task Saved",Snackbar.LENGTH_LONG).show();
    }

    public void OnSaveEditedTask(int task_position, int task_id,String task_title,String task_details){
        editTask(task_id,task_title,task_details);
        tasksRecAdapter.notifyItemChanged(task_position);
        Snackbar.make(coordinatorLayout,"Task Saved",Snackbar.LENGTH_SHORT).show();
    }

    public void OnDeleteSavedTask(int task_position, int task_id){
        deleteTask(task_id);
        tasksRecAdapter.notifyItemRemoved(task_position);
        percentageTasks();
        Snackbar.make(coordinatorLayout,"Task Deleted",Snackbar.LENGTH_SHORT).show();
    }
}
