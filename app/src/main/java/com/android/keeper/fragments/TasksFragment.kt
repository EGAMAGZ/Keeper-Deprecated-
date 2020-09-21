package com.android.keeper.fragments

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.bottomappbar.BottomAppBar
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.android.keeper.R
import com.android.keeper.adapters.TasksAdapter
import com.android.keeper.customelements.IconToast
import com.android.keeper.dialog.AddNewTaskBottomSheet
import com.android.keeper.dialog.AddNewTaskBottomSheet.AddNewTaskBottomSheetListener
import com.android.keeper.dialog.EditTaskBottomSheet
import com.android.keeper.dialog.EditTaskBottomSheet.EditTaskBottomSheetListener
import com.android.keeper.dialog.MessageBottomSheet
import com.android.keeper.localdb.SQLiteConnection
import com.android.keeper.localdb.utilities.TasksUtilities
import com.android.keeper.recycle_items.TaskItem
import com.android.keeper.util.CursorUtil
import com.android.keeper.util.Util
import org.junit.runner.RunWith
import java.lang.Boolean
import java.util.*

class TasksFragment : Fragment() {
    private var coordinatorLayout: CoordinatorLayout? = null
    private var conn: SQLiteConnection? = null
    private var fragmentView: View? = null
    private var addTaskBtn: FloatingActionButton? = null
    private var bottomAppBar: BottomAppBar? = null
    private var taskProgressBar: ProgressBar? = null
    private var taskPercentage: TextView? = null
    private var tasksRecyclerView: RecyclerView? = null
    private var scrollView: ScrollView? = null
    private var tasksRecAdapter: TasksAdapter? = null
    private var risefrombottom: Animation? = null
    private var hidetobottom: Animation? = null
    private var tasksLayoutManager: RecyclerView.LayoutManager? = null
    private var editTaskBottomSheet: EditTaskBottomSheet? = null
    private var addNewTaskBottomSheet: AddNewTaskBottomSheet? = null
    private var tasksList: ArrayList<TaskItem?>? = null
    private var percentage = 0
    private var wasShownBottomSheet = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_tasks, container, false)
        conn = SQLiteConnection(context, "keeper_db", null, 1)
        tasksList = ArrayList()
        risefrombottom = AnimationUtils.loadAnimation(context, R.anim.rise_from_bottom)
        risefrombottom.setDuration(200)
        hidetobottom = AnimationUtils.loadAnimation(context, R.anim.hide_to_bottom)
        hidetobottom.setDuration(200)
        coordinatorLayout = fragmentView.findViewById(R.id.task_fragment_container)
        bottomAppBar = fragmentView.findViewById(R.id.bottombar)
        scrollView = fragmentView.findViewById<ScrollView?>(R.id.task_scrollview)
        tasksRecyclerView = fragmentView.findViewById(R.id.tasks_recyclerview)
        tasksLayoutManager = LinearLayoutManager(context)
        taskProgressBar = fragmentView.findViewById<View?>(R.id.task_progress_bar) as ProgressBar?
        taskPercentage = fragmentView.findViewById<View?>(R.id.task_progress_percentage) as TextView?
        addTaskBtn = fragmentView.findViewById<View?>(R.id.addtask_flt_btn) as FloatingActionButton?
        scrollView.setOnScrollChangeListener(View.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY <= oldScrollY) {
                //scrollView up
                risefrombottom.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        bottomAppBar.setVisibility(View.VISIBLE)
                        addTaskBtn.show()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                if (bottomAppBar.getVisibility() != View.VISIBLE) {
                    bottomAppBar.startAnimation(risefrombottom)
                }
            } else {
                hidetobottom.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        bottomAppBar.setVisibility(View.GONE)
                        addTaskBtn.hide()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                if (bottomAppBar.getVisibility() != View.GONE) {
                    bottomAppBar.startAnimation(hidetobottom)
                }
            }
        })
        addTaskBtn.setOnClickListener(View.OnClickListener {
            addNewTaskBottomSheet = AddNewTaskBottomSheet()
            addNewTaskBottomSheet.setBottomSheetListener(object : AddNewTaskBottomSheetListener {
                override fun onAddTask(task_id: Int, task_title: String?, task_details: String?) {
                    AddTask(task_id, task_title, task_details)
                }

                override fun onEmptyTaskTitle() {
                    IconToast.Companion.makeContent(context, "You need to add a title", Toast.LENGTH_SHORT, R.drawable.ic_close_veish_24dp).show()
                }

                override fun onTaskDateSelected() {
                    IconToast.Companion.makeContent(context, "Task's Date Selected", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
                }
            })
            addNewTaskBottomSheet.show(fragmentManager, "addNewTaskBottomSheet")
        })
        loadTasks()
        return fragmentView
    }

    fun loadTasks() {
        val columns = arrayOf<String?>(TasksUtilities.COLUMN_TASK_ID, TasksUtilities.COLUMN_TASK_TITLE, TasksUtilities.COLUMN_TASK_DETAILS, TasksUtilities.COLUMN_TASK_DONE)
        val selection: String? = null //This will select all rows
        val database = conn.getReadableDatabase()
        try {
            val cursor = database.query(TasksUtilities.TABLE_NAME, columns, selection, null, null, null,
                    TasksUtilities.COLUMN_TASK_ID + " DESC", null)
            while (cursor.moveToNext()) {
                if (cursor.getInt(3) == 0) {
                    tasksList.add(TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp, cursor.getInt(0), cursor.getString(1), cursor.getString(2), false))
                } else if (cursor.getInt(3) == 1) {
                    tasksList.add(TaskItem(R.drawable.ic_check_box_black_24dp, cursor.getInt(0), cursor.getString(1), cursor.getString(2), true))
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("Keeper Error Logger", "ERROR:", e)
            taskProgressBar.setProgress(0)
            taskPercentage.setText(0.toString() + "%")
        } finally {
            /* At cause the adapter is getting the elements from the arraylist, the
            * getAdapterPosition() [position returned by interface] will be the same
            * position as the arraylist. That's the reason there isn't needed to pass
            * the id from other interface [in this case, from the Bottomsheet, especially
            * because almost of SQL CRUD is done in the same bottomsheets]
            * */
            tasksRecAdapter = TasksAdapter(tasksList)
            tasksRecyclerView.setLayoutManager(tasksLayoutManager)
            tasksRecyclerView.setAdapter(tasksRecAdapter)
            tasksRecAdapter.setOnItemClickListener(object : TasksAdapter.OnItemClickListener {
                override fun OnEditTask(position: Int) {
                    editTaskBottomSheet = EditTaskBottomSheet()
                    editTaskBottomSheet.setContent(position, tasksList.get(position).getTaskId(), tasksList.get(position).getTaskTitle(), tasksList.get(position).getTaskDetails())
                    editTaskBottomSheet.setEditTaskBottomSheetListener(object : EditTaskBottomSheetListener {
                        override fun onSaveEditedTask(task_position: Int, task_title: String?, task_details: String?) {
                            /*Here isn't needed to pass the id, because it isn't used and the
                            * position returned is the same position for the arraylist. As you can see,
                            * its posible to edit the task without the use of the id to look for it to modify it
                            * */
                            SaveEditedTask(task_position, task_title, task_details)
                        }

                        override fun onDeleteSavedTask(task_position: Int) {
                            /*Here isn't not needed the id,because whe are only deleting an element
                            * from the array and the adapter using the same position, and it is deleted
                            * from db in the bottomsheet (where it is needed the id)*/
                            DeleteSavedTask(task_position)
                        }

                        override fun onEmptyTaskTitle() {
                            IconToast.Companion.makeContent(context, "You need to add a title", Toast.LENGTH_SHORT, R.drawable.ic_close_veish_24dp).show()
                        }
                    })
                    editTaskBottomSheet.show(fragmentManager, "ediTaskBottomSheet")
                }

                override fun OnTaskDoneClick(position: Int) {
                    /*Example of why the position returned by the interface of the Adapter (getAdapterPosition())
                    * is the same position for the arraylist*/
                    if (tasksList.get(position).isTaskDone()) {
                        setTaskUndone(tasksList.get(position).getTaskId(), position)
                    } else {
                        setTaskDone(tasksList.get(position).getTaskId(), position)
                    }
                    tasksRecAdapter.notifyItemChanged(position)
                }
            })
            percentageTasks()
            sortTaskArrayList()
            database.close()
        }
    }

    private fun percentageTasks() {
        val database = conn.getReadableDatabase()
        percentage = try {
            val tasksTotal = numberTasksTotal(database) as Int
            val tasksDoneCount = numberTasksDone(database) as Int
            (100 * tasksDoneCount / tasksTotal) as Int
        } catch (e: ArithmeticException) {
            0
        }
        taskProgressBar.setProgress(percentage)
        taskPercentage.setText("$percentage%")
        database.close()
    }

    private fun numberTasksTotal(database: SQLiteDatabase?): Long {
        val sql = "SELECT " + TasksUtilities.COLUMN_TASK_ID + " FROM " + TasksUtilities.TABLE_NAME
        return CursorUtil.Companion.getCount(sql, database)
    }

    private fun numberTasksDone(database: SQLiteDatabase?): Long {
        val sql = "SELECT " + TasksUtilities.COLUMN_TASK_ID + " FROM " + TasksUtilities.TABLE_NAME + " WHERE " + TasksUtilities.COLUMN_TASK_DONE + " = 1"
        return CursorUtil.Companion.getCount(sql, database)
    }

    /*
    *   Methods related with the interaction with tasks
    * */
    private fun setTaskDone(task_id: Int, task_position: Int) {
        val database = conn.getWritableDatabase()
        val values = ContentValues()
        values.put(TasksUtilities.COLUMN_TASK_DONE, 1)
        database.update(TasksUtilities.TABLE_NAME, values, TasksUtilities.COLUMN_TASK_ID + "=" + task_id, null)
        tasksList.get(task_position).setImageResource(R.drawable.ic_check_box_black_24dp)
        tasksList.get(task_position).setTaskDone(true)
        percentageTasks()
        if (!Util.isInSplitScreen(context)) {
            if (percentage == 100 && !wasShownBottomSheet) {
                wasShownBottomSheet = true
                val messageBottomSheet = MessageBottomSheet()
                messageBottomSheet.setTitle("Awesome!", "#062639")
                messageBottomSheet.setSubtitle("You completed all your tasks")
                messageBottomSheet.show(fragmentManager, "messageBottomSheet")
            }
        } else {
            //TODO: Change icon of this toast with a hornet
            IconToast.Companion.makeContent(context, "You completed all your tasks", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
        }
        sortTaskArrayList()
        database.close()
    }

    private fun setTaskUndone(task_id: Int, task_position: Int) {
        val database = conn.getWritableDatabase()
        val values = ContentValues()
        values.put(TasksUtilities.COLUMN_TASK_DONE, 0)
        database.update(TasksUtilities.TABLE_NAME, values, TasksUtilities.COLUMN_TASK_ID + "=" + task_id, null)
        tasksList.get(task_position).setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp)
        tasksList.get(task_position).setTaskDone(false)
        sortTaskArrayList()
        percentageTasks()
        database.close()
    }

    private fun sortTaskArrayList() {
        Collections.sort(tasksList) { taskItem, taskItem2 -> Boolean.compare(taskItem.isTaskDone(), taskItem2.isTaskDone()) }
        tasksRecAdapter.notifyDataSetChanged()
    }

    /*
    *
    * Action like Add,Save,Delete tasks
    *
    * */
    private fun AddTask(task_id: Int, task_title: String?, task_details: String?) {
        //The SQL PART IS AUTO EXECUTED ON ADDNEWTASKBOTTOMSHEET
        /** It is important to add a new item in the adapter and in the array list because if it is not added it will throw
         * an IndexOutOfBoundsException when is notified to the adapter that a new item is inserted  */
        //It is related with the adapter for the elements that are shown
        tasksList.add(0, TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp, task_id, task_title, task_details, false))

        //It is related with the adapter with the elements for filter
        tasksRecAdapter.addItem(0, TaskItem(R.drawable.ic_check_box_outline_blank_black_24dp, task_id, task_title, task_details, false))
        IconToast.Companion.makeContent(context, "Task added", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
        percentageTasks()
    }

    private fun SaveEditedTask(task_position: Int, task_title: String?, task_details: String?) {
        //It is related with the adapter for the elements that are shown
        tasksList.get(task_position).setTaskTitle(task_title)
        tasksList.get(task_position).setTaskDetails(task_details)
        tasksRecAdapter.editItem(task_position, task_title, task_details) //It is related with the adapter with the elements for filter
        IconToast.Companion.makeContent(context, "Task saved", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
    }

    private fun DeleteSavedTask(task_position: Int) {
        tasksList.removeAt(task_position) //It is related with the adapter for the elements that are shown
        tasksRecAdapter.removeItem(task_position) //It is related with the adapter with the elements for filter
        IconToast.Companion.makeContent(context, "Task deleted", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
        percentageTasks()
    }

    fun FilterTask(text: String?) {
        tasksRecAdapter.getFilter().filter(text)
    }
}