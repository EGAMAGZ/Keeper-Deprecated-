package com.android.keeper.fragments

import android.os.Bundle
import android.support.design.bottomappbar.BottomAppBar
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import com.android.keeper.R
import com.android.keeper.localdb.SQLiteConnection
import com.android.keeper.localdb.utilities.NotesUtilities
import org.junit.runner.RunWith

class NotesFragment : Fragment() {
    private var conn: SQLiteConnection? = null
    private var scrollView: ScrollView? = null
    private var fragmentView: View? = null
    private var bottomAppBar: BottomAppBar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_notes, container, false)
        conn = SQLiteConnection(context, "keeper_db", null, 1)
        scrollView = fragmentView.findViewById<ScrollView?>(R.id.notes_scrollview)
        bottomAppBar = fragmentView.findViewById(R.id.bottombar)
        bottomAppBar.replaceMenu(R.menu.notes_bottomappbar_menu)
        bottomAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
            }
            true
        })
        loadNotes()
        return fragmentView
    }

    private fun loadNotes() {
        val colums = arrayOf<String?>(NotesUtilities.COLUMN_NOTE_ID, NotesUtilities.COLUMN_NOTE_TITLE, NotesUtilities.COLUMN_NOTE_DESCRIPTION)
        val database = conn.getReadableDatabase()
        val cursor = database.query(NotesUtilities.TABLE_NAME, colums, null, null, null,
                null, NotesUtilities.COLUMN_NOTE_ID + " DESC", null)
        /*while(cursor.moveToNext()){

        }*/Toast.makeText(context, "TOTAL:" + cursor.count, Toast.LENGTH_SHORT).show()
    }
}