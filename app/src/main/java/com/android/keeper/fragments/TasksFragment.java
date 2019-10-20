package com.android.keeper.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
    private View fragmentView;
    private FloatingActionButton addTaskBtn;
    private BottomAppBar bottomAppBar;
    private ProgressBar taskProgressBar;
    private TextView taskPercentage;
    private RecyclerView tasksRecyclerView;
    private ScrollView scrollView;
    private TasksAdapter tasksRecAdapter;
    private Animation risefrombottom,hidetobottom;
    //private RecyclerView.Adapter tasksRecAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;
    private EditTaskBottomSheet editTaskBottomSheet;
    private AddNewTaskBottomSheet addNewTaskBottomSheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_tasks,container,false);

        risefrombottom= AnimationUtils.loadAnimation(getContext(),R.anim.rise_from_bottom);
        risefrombottom.setDuration(200);
        hidetobottom=AnimationUtils.loadAnimation(getContext(),R.anim.hide_to_bottom);
        hidetobottom.setDuration(200);

        coordinatorLayout= fragmentView.findViewById(R.id.task_fragment_container);
        bottomAppBar= fragmentView.findViewById(R.id.bottombar);
        scrollView= fragmentView.findViewById(R.id.task_scrollview);

        tasksRecyclerView= fragmentView.findViewById(R.id.tasks_recyclerview);
        tasksLayoutManager=new LinearLayoutManager(getContext());

        tasksList=new ArrayList<TaskItem>();
        taskProgressBar =(ProgressBar) fragmentView.findViewById(R.id.task_progress_bar);
        taskPercentage =(TextView) fragmentView.findViewById(R.id.task_progress_percentage);

        addTaskBtn =(FloatingActionButton) fragmentView.findViewById(R.id.addtask_flt_btn);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //if(scrollY<=oldScrollY){
                if(scrollY<=oldScrollY){
                    //scrollView up
                    risefrombottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            bottomAppBar.setVisibility(View.VISIBLE);
                            addTaskBtn.show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if(bottomAppBar.getVisibility()!=View.VISIBLE){
                        bottomAppBar.startAnimation(risefrombottom);
                    }
                }else{
                    hidetobottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            bottomAppBar.setVisibility(View.GONE);
                            addTaskBtn.hide();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if(bottomAppBar.getVisibility()!=View.GONE){
                        bottomAppBar.startAnimation(hidetobottom);
                    }
                }
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTaskBottomSheet=new AddNewTaskBottomSheet();
                addNewTaskBottomSheet.setBottomSheetListener(new AddNewTaskBottomSheet.AddNewTaskBottomSheetListener() {
                    @Override
                    public void OnAddTask(int task_id, String task_title, String task_details) {
                        AddTask(task_id,task_title,task_details);
                    }

                    @Override
                    public void OnEmptyTaskTitle() {
                        CustomToast("Task Title Empty",R.drawable.ic_close_white_24dp);
                    }
                });
                addNewTaskBottomSheet.show(getFragmentManager(),"addNewTaskBottomSheet");
            }
        });

        loadTasks();

        return fragmentView;
    }

    public void loadTasks(){
        String[] columns={TasksUtilities.COLUMN_TASK_ID,TasksUtilities.COLUMN_TASK_TITLE,TasksUtilities.COLUMN_TASK_DETAILS,TasksUtilities.COLUMN_TASK_DONE};
        String selection=null; //This will select all rows

        SQLiteDatabase database=conn.getReadableDatabase();

        try {
            Cursor cursor = database.query(TasksUtilities.TABLE_NAME, columns, selection, null, null, null,
                    TasksUtilities.COLUMN_TASK_ID+" DESC, "+TasksUtilities.COLUMN_TASK_ID+" ASC", null);
            while(cursor.moveToNext()){
                if(cursor.getInt(3)==0){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2),false));
                }else if(cursor.getInt(3)==1){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2),true));
                }
            }

        }
        catch(IllegalStateException e){
            taskProgressBar.setProgress(0);
            taskPercentage.setText(0+"%");
            CustomToast("App Error",R.drawable.ic_close_white_24dp);
        }
        catch(Exception e){
            Log.e("Keeper Error Logger","ERROR:",e);
            taskProgressBar.setProgress(0);
            taskPercentage.setText(0+"%");
            CustomToast("Internal App Error",R.drawable.ic_close_white_24dp);
        }
        finally {
            tasksRecAdapter=new TasksAdapter(tasksList);
            tasksRecyclerView.setLayoutManager(tasksLayoutManager);
            tasksRecyclerView.setAdapter(tasksRecAdapter);
            tasksRecAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    editTaskBottomSheet=new EditTaskBottomSheet();
                    editTaskBottomSheet.setContent(position,tasksList.get(position).getTaskId(),tasksList.get(position).getTaskTitle(),tasksList.get(position).getTaskDetails());
                    editTaskBottomSheet.setEditTaskBottomSheetListener(new EditTaskBottomSheet.EditTaskBottomSheetListener() {
                        @Override
                        public void OnSaveEditedTask(int task_position, int task_id, String task_title, String task_details) {
                            SaveEditedTask(task_position,task_id,task_title,task_details);
                        }

                        @Override
                        public void OnDeleteSavedTask(int task_position, int task_id) {
                            DeleteSavedTask(task_position,task_id);
                        }

                        @Override
                        public void OnEmptyTaskTitle() {
                            CustomToast("Task Title Empty",R.drawable.ic_close_white_24dp);
                        }
                    });
                    editTaskBottomSheet.show(getFragmentManager(),"ediTaskBottomSheet");
                }

                @Override
                public void onDoneTaskClick(int position) {
                    //TODO: Show snackbar with undo action for the both status that the task could have(EX: setTaskDone -> setTaskUndone)
                    if(tasksList.get(position).isTaskDone()){
                        setTaskUndone(tasksList.get(position).getTaskId());
                        tasksRecAdapter.notifyItemChanged(position);
                    }else{
                        setTaskDone(tasksList.get(position).getTaskId());
                        tasksRecAdapter.notifyItemChanged(position);
                    }
                }

                /*@Override
                public void onItemLongClick(int position) {
                    Toast.makeText(getContext(),"LONG CLICKED "+position,Toast.LENGTH_SHORT).show();
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                }*/
            });
            percentageTasks();
            sortTaskArrayList();
            database.close();
        }
    }

    private void percentageTasks(){
        SQLiteDatabase database=conn.getReadableDatabase();
        int percentage;
        try{
            int tasksTotal=(int) numberTasksTotal(database);
            int tasksDoneCount=(int) numberTasksDone(database);
            percentage=(int) ((100*tasksDoneCount)/tasksTotal);
        }catch(ArithmeticException e){
            percentage=0;
        }
        taskProgressBar.setProgress(percentage);
        taskPercentage.setText(percentage+"%");
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

    /*
    *   Functions related with the interaction with tasks
    * */

    private void addTask(int task_id,String task_title, String task_details){
        //The SQL PART IS AUTO EXECUTED ON ADDNEWTASKBOTTOMSHEET

        //It is related with the adapter for the elements that are shown
        tasksList.add(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_id,task_title,task_details,false));

        //It is related with the adapter with the elements for filter
        tasksRecAdapter.addItem(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_id,task_title,task_details,false));
    }

    private void deleteTask(int task_id){
        SQLiteDatabase database=conn.getReadableDatabase();
        database.delete(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);
        for(int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                tasksList.remove(i); //It is related with the adapter for the elements that are shown

                tasksRecAdapter.removeItem(i);//It is related with the adapter with the elements for filter
            }
        }
        database.close();
    }
    private void editTask(int task_id,String task_title,String task_details){
        //TODO: PASS THE SQLITE SECTION TO THE BOTTOMSHEET THAT BELONGS
        SQLiteDatabase database=conn.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_TITLE,task_title);
        values.put(TasksUtilities.COLUMN_TASK_DETAILS,task_details);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);

        for (int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                //It is related with the adapter for the elements that are shown
                tasksList.get(i).setTaskTitle(task_title);
                tasksList.get(i).setTaskDetails(task_details);

                tasksRecAdapter.editItem(task_id,task_title,task_details);//It is related with the adapter with the elements for filter
            }
        }
        database.close();
    }
    private void setTaskDone(int task_id){
        SQLiteDatabase database=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_DONE,1);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);

        for(int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                tasksList.get(i).setImageResource(R.drawable.ic_check_box_black_24dp);
                tasksList.get(i).setTaskDone(true);
            }
        }
        sortTaskArrayList();
        database.close();
        percentageTasks();
    }

    private void setTaskUndone(int task_id){
        SQLiteDatabase database=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_DONE,0);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);

        for(int i=0;i<tasksList.size();++i){
            if(tasksList.get(i).getTaskId()==task_id){
                tasksList.get(i).setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                tasksList.get(i).setTaskDone(false);
            }
        }
        sortTaskArrayList();
        database.close();
        percentageTasks();
    }

    private void sortTaskArrayList(){
        Collections.sort(tasksList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem taskItem, TaskItem taskItem2) {
                return Boolean.compare(taskItem.isTaskDone(),taskItem2.isTaskDone());
            }
        });
        tasksRecAdapter.notifyDataSetChanged();
    }

    public void setTaskDate(int selected_year,int selected_month,int selected_dayOfMonth,int selected_hourOfDay,int selected_minute){
        if(selected_year==0 && selected_month==0 && selected_dayOfMonth==0){
            return ;
        }else{
            addNewTaskBottomSheet.setTaskDate(selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute);
        }
    }

    /*
    *
    * Action like Add,Save,Delete tasks
    *
    * */

    private void AddTask(int task_id,String task_title, String task_details){
        addTask(task_id, task_title, task_details);
        tasksRecAdapter.notifyItemInserted(0);
        CustomToast("Task added",R.drawable.ic_done_white_24dp);
        /*if(selected_year==0 && selected_month==0 && selected_dayOfMonth==0){
            Toast.makeText(getContext(),"Year:"+selected_year,Toast.LENGTH_SHORT).show();
        }else{

        }*/
        percentageTasks();
    }

    private void SaveEditedTask(int task_position, int task_id,String task_title,String task_details){
        editTask(task_id,task_title,task_details);
        tasksRecAdapter.notifyItemChanged(task_position);
        //CustomToast("Task saved",R.drawable.ic_done_white_24dp);
    }

    private void DeleteSavedTask(int task_position, int task_id){
        deleteTask(task_id);
        tasksRecAdapter.notifyItemRemoved(task_position);
        CustomToast("Task deleted",R.drawable.ic_done_white_24dp);
        percentageTasks();
    }

    public void FilterTask(String text){
        tasksRecAdapter.getFilter().filter(text);
    }

    private void CustomToast(String text,int imageResource){
        LayoutInflater inflater=getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) fragmentView.findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(text);
        toastImage.setImageResource(imageResource);

        Toast toast = new Toast(getContext());
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        toast.show();
    }

}
