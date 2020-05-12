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
import com.android.keeper.dialog.MessageBottomSheet;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;
import com.android.keeper.recycle_items.TaskItem;
import com.android.keeper.util.CursorUtil;
import com.android.keeper.util.Util;

public class TasksFragment extends Fragment {

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
    private RecyclerView.LayoutManager tasksLayoutManager;
    private EditTaskBottomSheet editTaskBottomSheet;
    private AddNewTaskBottomSheet addNewTaskBottomSheet;

    private ArrayList<TaskItem> tasksList;
    private int percentage=0;
    private boolean wasShownBottomSheet=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_tasks,container,false);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);
        tasksList=new ArrayList<TaskItem>();

        risefrombottom= AnimationUtils.loadAnimation(getContext(),R.anim.rise_from_bottom);
        risefrombottom.setDuration(200);
        hidetobottom=AnimationUtils.loadAnimation(getContext(),R.anim.hide_to_bottom);
        hidetobottom.setDuration(200);

        coordinatorLayout= fragmentView.findViewById(R.id.task_fragment_container);
        bottomAppBar= fragmentView.findViewById(R.id.bottombar);
        scrollView= fragmentView.findViewById(R.id.task_scrollview);

        tasksRecyclerView= fragmentView.findViewById(R.id.tasks_recyclerview);
        tasksLayoutManager=new LinearLayoutManager(getContext());

        taskProgressBar =(ProgressBar) fragmentView.findViewById(R.id.task_progress_bar);
        taskPercentage =(TextView) fragmentView.findViewById(R.id.task_progress_percentage);

        addTaskBtn =(FloatingActionButton) fragmentView.findViewById(R.id.addtask_flt_btn);

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
                    public void onAddTask(int task_id, String task_title, String task_details) {
                        AddTask(task_id,task_title,task_details);
                    }

                    @Override
                    public void onEmptyTaskTitle() {
                        Toast.makeText(getContext(),"Task Title Empty",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTaskDateSelected() {
                        Toast.makeText(getContext(),"Task Date Selected",Toast.LENGTH_SHORT).show();
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
                    TasksUtilities.COLUMN_TASK_ID+" DESC", null);
            while(cursor.moveToNext()){
                if(cursor.getInt(3)==0){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2),false));
                }else if(cursor.getInt(3)==1){
                    tasksList.add(new TaskItem(R.drawable.ic_check_box_black_24dp,cursor.getInt(0),cursor.getString(1),cursor.getString(2),true));
                }
            }
        }
        catch(Exception e){
            Log.e("Keeper Error Logger","ERROR:",e);
            taskProgressBar.setProgress(0);
            taskPercentage.setText(0+"%");
        }
        finally {
            /* At cause the adapter is getting the elements from the arraylist, the
            * getAdapterPosition() [position returned by interface] will be the same
            * position as the arraylist. That's the reason there isn't needed to pass
            * the id from other interface [in this case, from the Bottomsheet, especially
            * because almost of SQL CRUD is done in the same bottomsheets]
            * */
            tasksRecAdapter=new TasksAdapter(tasksList);
            tasksRecyclerView.setLayoutManager(tasksLayoutManager);
            tasksRecyclerView.setAdapter(tasksRecAdapter);
            tasksRecAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
                @Override
                public void OnEditTask(int position) {
                    editTaskBottomSheet=new EditTaskBottomSheet();
                    editTaskBottomSheet.setContent(position,tasksList.get(position).getTaskId(),tasksList.get(position).getTaskTitle(),tasksList.get(position).getTaskDetails());
                    editTaskBottomSheet.setEditTaskBottomSheetListener(new EditTaskBottomSheet.EditTaskBottomSheetListener() {
                        @Override
                        public void onSaveEditedTask(int task_position, String task_title, String task_details) {
                            /*Here isn't needed to pass the id, because it isn't used and the
                            * position returned is the same position for the arraylist. As you can see,
                            * its posible to edit the task without the use of the id to look for it to modify it
                            * */
                            SaveEditedTask(task_position,task_title,task_details);
                        }

                        @Override
                        public void onDeleteSavedTask(int task_position) {
                            /*Here isn't not needed the id,beacuse whe are only deleting an element
                            * from the array and the adapter using the same position, and it is deleted
                            * from db in the bottomsheet (where it is needed the id)*/
                            DeleteSavedTask(task_position);
                        }

                        @Override
                        public void onEmptyTaskTitle() {
                            Toast.makeText(getContext(),"Task Title Empty",Toast.LENGTH_SHORT).show();
                        }
                    });
                    editTaskBottomSheet.show(getFragmentManager(),"ediTaskBottomSheet");
                }

                @Override
                public void OnTaskDoneClick(int position) {
                    /*Example of why the position returned by the interface of the Adapter (getAdapterPosition())
                    * is the same position for the arraylist*/
                    if(tasksList.get(position).isTaskDone()){
                        setTaskUndone(tasksList.get(position).getTaskId(),position);
                        tasksRecAdapter.notifyItemChanged(position);
                    }else{
                        setTaskDone(tasksList.get(position).getTaskId(),position);
                        tasksRecAdapter.notifyItemChanged(position);
                    }
                }
            });
            percentageTasks();
            sortTaskArrayList();
            database.close();
        }
    }

    private void percentageTasks(){
        SQLiteDatabase database=conn.getReadableDatabase();

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
        String sql="SELECT "+TasksUtilities.COLUMN_TASK_ID+" FROM "+ TasksUtilities.TABLE_NAME;
        long count=CursorUtil.getCount(sql,database);

        return count;
    }

    private long numberTasksDone(SQLiteDatabase database){
        String sql="SELECT "+TasksUtilities.COLUMN_TASK_ID+" FROM "+TasksUtilities.TABLE_NAME+" WHERE "+TasksUtilities.COLUMN_TASK_DONE+" = 1";
        long count= CursorUtil.getCount(sql,database);

        return count;
    }

    /*
    *   Methods related with the interaction with tasks
    * */

    private void setTaskDone(int task_id,int task_position){
        SQLiteDatabase database=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_DONE,1);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);

        tasksList.get(task_position).setImageResource(R.drawable.ic_check_box_black_24dp);
        tasksList.get(task_position).setTaskDone(true);
        //Here was a for before
        sortTaskArrayList();
        database.close();
        percentageTasks();
        if(!Util.isInSplitScreen(getContext())){
            if(percentage==100 && !wasShownBottomSheet){
                wasShownBottomSheet=true;
                MessageBottomSheet messageBottomSheet=new MessageBottomSheet();
                messageBottomSheet.setTitle("Awesome!","#062639");
                messageBottomSheet.setSubtitle("You completed all your tasks");
                messageBottomSheet.show(getFragmentManager(),"messageBottomSheet");
            }
        }else{
            Toast.makeText(getContext(),"You completed all your tasks",Toast.LENGTH_SHORT).show();
        }
    }

    private void setTaskUndone(int task_id,int task_position){
        SQLiteDatabase database=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_DONE,0);

        database.update(TasksUtilities.TABLE_NAME,values,TasksUtilities.COLUMN_TASK_ID+"="+task_id,null);

        tasksList.get(task_position).setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        tasksList.get(task_position).setTaskDone(false);
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

    /*
    *
    * Action like Add,Save,Delete tasks
    *
    * */

    private void AddTask(int task_id,String task_title, String task_details){
        //The SQL PART IS AUTO EXECUTED ON ADDNEWTASKBOTTOMSHEET
        /** It is important to add a new item in the adapter and in the array list because if it is not added it will throw
         * an IndexOutOfBoundsException when is notified to the adapter that a new item is inserted */
        //It is related with the adapter for the elements that are shown
        tasksList.add(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_id,task_title,task_details,false));

        //It is related with the adapter with the elements for filter
        tasksRecAdapter.addItem(0,new TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp,task_id,task_title,task_details,false));
        Toast.makeText(getContext(),"Task added",Toast.LENGTH_SHORT).show();
        percentageTasks();
    }

    private void SaveEditedTask(int task_position,String task_title,String task_details){
        //It is related with the adapter for the elements that are shown
        tasksList.get(task_position).setTaskTitle(task_title);
        tasksList.get(task_position).setTaskDetails(task_details);
        tasksRecAdapter.editItem(task_position,task_title,task_details);//It is related with the adapter with the elements for filter
        Toast.makeText(getContext(),"Task saved",Toast.LENGTH_SHORT).show();
    }

    private void DeleteSavedTask(int task_position){
        tasksList.remove(task_position); //It is related with the adapter for the elements that are shown
        tasksRecAdapter.removeItem(task_position);//It is related with the adapter with the elements for filter
        Toast.makeText(getContext(),"Task deleted",Toast.LENGTH_SHORT).show();
        percentageTasks();
    }

    public void FilterTask(String text){
        tasksRecAdapter.getFilter().filter(text);
    }

}
