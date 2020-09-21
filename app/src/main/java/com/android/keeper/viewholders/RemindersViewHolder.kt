package com.android.keeper.viewholders

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.android.keeper.R
import com.android.keeper.adapters.RemindersAdapter
import org.junit.runner.RunWith

class RemindersViewHolder(itemView: View, listener: RemindersAdapter.OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
    var reminderTitleTextView: TextView?
    var reminderDateTextView: TextView?

    init {
        itemView.setOnLongClickListener {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditReminder(position)
                }
            }
            true
        }
        reminderTitleTextView = itemView.findViewById<TextView?>(R.id.reminder_item_title)
        reminderDateTextView = itemView.findViewById<TextView?>(R.id.reminder_item_date)
    }
}