package com.android.keeper.adapters

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.android.keeper.R
import com.android.keeper.recycle_items.TaskItem
import com.android.keeper.viewholders.TasksViewHolder
import org.junit.runner.RunWith
import java.util.*

class TasksAdapter(private val tasksList: ArrayList<TaskItem?>?) : RecyclerView.Adapter<TasksViewHolder?>(), Filterable {
    private val taskListFull: ArrayList<TaskItem?>?
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        open fun OnEditTask(position: Int)
        open fun OnTaskDoneClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TasksViewHolder {
        //This method will directly access to the layout
        //viewgroup -> parent
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.task_item, viewGroup, false)
        return TasksViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(tasksViewHolder: TasksViewHolder, i: Int) {
        /*At the moment that each item is inserted, this method will be executed
        *
        * i=position from the item
        * */
        val currentItem = tasksList.get(i)
        tasksViewHolder.taskTitleTextView.text = currentItem.getTaskTitle()
        tasksViewHolder.taskDetailsTextView.text = currentItem.getTaskDetails()
        tasksViewHolder.taskImage.setImageResource(currentItem.getImageResource())
    }

    override fun getItemCount(): Int {
        //Length of items that will be added to the recyclerview
        return tasksList.size
    }

    override fun getFilter(): Filter? {
        return taskListFilter
    }

    private val taskListFilter: Filter? = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList = ArrayList<TaskItem?>()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(taskListFull)
            } else {
                val filterpattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in taskListFull) {
                    if (item.getTaskTitle().toLowerCase().contains(filterpattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            tasksList.clear()
            tasksList.addAll(results.values as ArrayList<*>)
            notifyDataSetChanged()
        }
    }

    fun removeItem(position: Int) {
        taskListFull.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(index: Int, taskItem: TaskItem?) {
        taskListFull.add(index, taskItem)
        notifyItemInserted(index)
    }

    fun editItem(task_position: Int, task_title: String?, task_details: String?) {
        taskListFull.get(task_position).setTaskTitle(task_title)
        taskListFull.get(task_position).setTaskDetails(task_details)
        //Here was a for before to change searching by id
        notifyItemChanged(task_position)
    }

    init {
        taskListFull = ArrayList(tasksList)
    }
}