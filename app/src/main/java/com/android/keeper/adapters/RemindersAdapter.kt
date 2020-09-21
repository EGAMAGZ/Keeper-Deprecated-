package com.android.keeper.adapters

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.android.keeper.R
import com.android.keeper.recycle_items.ReminderItem
import com.android.keeper.viewholders.RemindersViewHolder
import org.junit.runner.RunWith
import java.util.*

class RemindersAdapter(private val remindersList: ArrayList<ReminderItem?>?) : RecyclerView.Adapter<RemindersViewHolder?>(), Filterable {
    private val remindersListFull: ArrayList<ReminderItem?>?
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        open fun onEditReminder(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RemindersViewHolder {
        /*This method will directly access to the layout
        *   viewgroup -> parent*/
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.reminder_item, viewGroup, false)
        return RemindersViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(remindersViewHolder: RemindersViewHolder, i: Int) {
        /*At the moment that each item is inserted, this method will be executed
         *
         * i=position from the item
         * so, i is the same position to the array list and adapter( getAdapterPosition() )
         * */
        val currentItem = remindersList.get(i)
        remindersViewHolder.reminderTitleTextView.text = currentItem.getReminderTitle()
        if (currentItem.getReminderDate().isEmpty() || currentItem.getReminderTime().isEmpty()) {
            remindersViewHolder.reminderDateTextView.text = "No Date Assigned"
        } else {
            remindersViewHolder.reminderDateTextView.text = currentItem.getReminderDate() + " " + currentItem.getReminderTime()
        }
    }

    override fun getItemCount(): Int {
        //Length of items that will be added to the recyclerview
        return remindersList.size
    }

    override fun getFilter(): Filter? {
        return reminderListFilter
    }

    private val reminderListFilter: Filter? = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults? {
            val filteredList = ArrayList<ReminderItem?>()
            if (charSequence == null || charSequence.length == 0) {
                filteredList.addAll(remindersListFull)
            } else {
                val filterpattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                for (item in remindersListFull) {
                    if (item.getReminderTitle().toLowerCase().contains(filterpattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            remindersList.clear()
            remindersList.addAll(filterResults.values as ArrayList<*>)
            notifyDataSetChanged()
        }
    }

    fun removeItem(position: Int) {
        remindersListFull.removeAt(position)
    }

    fun addItem(index: Int, reminderItem: ReminderItem?) {
        remindersListFull.add(index, reminderItem)
        notifyItemInserted(index)
    }

    fun editItem(reminder_position: Int, reminder_title: String?) {
        remindersListFull.get(reminder_position).setReminderTitle(reminder_title)
        notifyItemChanged(reminder_position)
    }

    init {
        remindersListFull = ArrayList(remindersList)
    }
}