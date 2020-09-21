package com.android.keeper.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import com.android.keeper.R
import com.android.keeper.adapters.RemindersAdapter
import com.android.keeper.customelements.IconToast
import com.android.keeper.dialog.AddNewReminderBottomSheet
import com.android.keeper.dialog.AddNewReminderBottomSheet.AddNewReminderBottomSheetListener
import com.android.keeper.localdb.SQLiteConnection
import com.android.keeper.localdb.utilities.RemindersUtilities
import com.android.keeper.recycle_items.ReminderItem
import com.android.keeper.util.CalendarUtil
import com.android.keeper.util.CursorUtil
import org.junit.runner.RunWith
import java.text.DateFormat
import java.util.*

class RemindersFragment : Fragment() {
    private var fragmentView: View? = null
    private var sqLiteConnection: SQLiteConnection? = null
    private var remindersRecyclerView: RecyclerView? = null
    private var remindersRecAdapter: RemindersAdapter? = null
    private var remindersLayoutManager: RecyclerView.LayoutManager? = null
    private var addReminderBtn: FloatingActionButton? = null
    private var scrollView: ScrollView? = null
    private var addNewReminderBottomSheet: AddNewReminderBottomSheet? = null
    private var remindersList: ArrayList<ReminderItem?>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_reminders, container, false)
        sqLiteConnection = SQLiteConnection(context, "keeper_db", null, 1)
        remindersList = ArrayList()
        addReminderBtn = fragmentView.findViewById(R.id.addreminder_flt_btn)
        remindersRecyclerView = fragmentView.findViewById(R.id.reminders_recyclerview)
        remindersLayoutManager = LinearLayoutManager(context)
        scrollView = fragmentView.findViewById<ScrollView?>(R.id.reminder_scrollview)
        addReminderBtn.setOnClickListener(View.OnClickListener {
            addNewReminderBottomSheet = AddNewReminderBottomSheet()
            addNewReminderBottomSheet.setBottomSheetListener(object : AddNewReminderBottomSheetListener {
                override fun onAddReminder(reminder_id: Int, reminder_title: String?, year: Int?, month: Int?, day: Int?, hour: Int?, minute: Int?) {
                    AddReminder(reminder_id, reminder_title, year, month, day, hour, minute)
                }

                override fun onEmptyReminderTitle() {
                    IconToast.Companion.makeContent(context, "You need to add a title", Toast.LENGTH_SHORT, R.drawable.ic_close_veish_24dp).show()
                }

                override fun onReminderDateSelected() {
                    IconToast.Companion.makeContent(context, "Reminder's Date Selected", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
                }
            })
            addNewReminderBottomSheet.show(fragmentManager, "addReminderBottomSheet")
        })
        scrollView.setOnScrollChangeListener(View.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY <= oldScrollY) {
                //scrollView up
                addReminderBtn.show()
            } else {
                //ScrollView down
                addReminderBtn.hide()
            }
        })
        loadReminders()
        return fragmentView
    }

    private fun loadReminders() {
        val columns = arrayOf<String?>(RemindersUtilities.COLUMN_REMINDER_ID, RemindersUtilities.COLUMN_REMINDER_TITLE,
                RemindersUtilities.COLUMN_REMINDER_YEAR, RemindersUtilities.COLUMN_REMINDER_MONTH,
                RemindersUtilities.COLUMN_REMINDER_DAY, RemindersUtilities.COLUMN_REMINDER_HOUR,
                RemindersUtilities.COLUMN_REMINDER_MINUTE)
        val database = sqLiteConnection.getReadableDatabase()
        try {
            val cursor = database.query(RemindersUtilities.TABLE_NAME, columns, null, null, null, null,
                    RemindersUtilities.COLUMN_REMINDER_ID + " DESC", null)
            while (cursor.moveToNext()) {
                val cursorUtil = CursorUtil(cursor)
                val id = cursorUtil.getInt(RemindersUtilities.COLUMN_REMINDER_ID)
                val title = cursorUtil.getString(RemindersUtilities.COLUMN_REMINDER_TITLE)
                val calendarUtil = CalendarUtil(context, cursorUtil.checkNullInteger(RemindersUtilities.COLUMN_REMINDER_YEAR),
                        cursorUtil.checkNullInteger(RemindersUtilities.COLUMN_REMINDER_MONTH),
                        cursorUtil.checkNullInteger(RemindersUtilities.COLUMN_REMINDER_DAY),
                        cursorUtil.checkNullInteger(RemindersUtilities.COLUMN_REMINDER_HOUR),
                        cursorUtil.checkNullInteger(RemindersUtilities.COLUMN_REMINDER_MINUTE))
                val date = calendarUtil.getDateFormat(DateFormat.LONG)
                val time = calendarUtil.getTimeFormat(DateFormat.SHORT)
                remindersList.add(ReminderItem(id, title, date, time, false))
            }
            cursor.close()
        } catch (e: Exception) {
        } finally {
            remindersRecAdapter = RemindersAdapter(remindersList)
            remindersRecyclerView.setLayoutManager(remindersLayoutManager)
            remindersRecyclerView.setAdapter(remindersRecAdapter)
            remindersRecAdapter.setOnItemClickListener(RemindersAdapter.OnItemClickListener { })
        }
        database.close()
    }

    private fun AddReminder(reminder_id: Int, reminder_title: String?, year: Int?, month: Int?, day: Int?, hour: Int?, minute: Int?) {
        //The SQL PART IS AUTO EXECUTED ON AddNewReminderBottomSheet
        val calendarUtil = CalendarUtil(context, year, month, day, hour, minute)
        val date = calendarUtil.getDateFormat(DateFormat.LONG)
        val time = calendarUtil.getTimeFormat(DateFormat.SHORT)
        /** It is important to add a new item in the adapter and in the array list because if it is not added it will throw
         * an IndexOutOfBoundsException when is notified to the adapter that a new item is inserted  */
        remindersList.add(0, ReminderItem(reminder_id, reminder_title, date, time, false))
        remindersRecAdapter.addItem(0, ReminderItem(reminder_id, reminder_title, date, time, false))
        IconToast.Companion.makeContent(context, "Reminder Added", Toast.LENGTH_SHORT, R.drawable.ic_done_veish_24dp).show()
    }

    fun FilterReminder(text: String?) {
        remindersRecAdapter.getFilter().filter(text)
    }
}