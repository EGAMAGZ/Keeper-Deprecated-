package com.android.keeper.viewholders

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.keeper.R
import com.android.keeper.adapters.TasksAdapter
import org.junit.runner.RunWith

class TasksViewHolder(itemView: View, listener: TasksAdapter.OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
    /*This class will get all the elements from layout, and allow us to acces to them(in this case define them)
     * and also add them listeners
     * */
    var taskTitleTextView: TextView?
    var taskDetailsTextView: TextView?
    var taskImage: ImageView?

    init { //Because this class is static, we need to pass the interface OnItemClickListener
        /*At cause the adapter is getting the elements from the arraylist, the
         * getAdapterPosition() [position returned by interface] will be the same
         * position as the arraylist.
         * */itemView.setOnLongClickListener {
            if (listener != null) {
                val position = adapterPosition //Get Adapter Position (ItemPosition)
                if (position != RecyclerView.NO_POSITION) {
                    listener.OnEditTask(position)
                }
            }
            true
        }
        taskTitleTextView = itemView.findViewById<TextView?>(R.id.task_item_title)
        taskDetailsTextView = itemView.findViewById<TextView?>(R.id.task_item_details)
        taskImage = itemView.findViewById<ImageView?>(R.id.task_item_image)
        taskImage.setOnClickListener(View.OnClickListener {
            if (listener != null) {
                val position = adapterPosition //Get Adapter Position (ItemPosition)
                if (position != RecyclerView.NO_POSITION) {
                    listener.OnTaskDoneClick(position)
                }
            }
        })
    }
}