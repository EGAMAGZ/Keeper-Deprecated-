package com.android.keeper.recycle_items

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class NoteItem(private val note_id: Int?, private var note_title: String?, private var note_description: String?) {
    fun getNote_id(): Int? {
        return note_id
    }

    fun getNote_title(): String? {
        return note_title
    }

    fun setNote_title(note_title: String?) {
        this.note_title = note_title
    }

    fun getNote_description(): String? {
        return note_description
    }

    fun setNote_description(note_description: String?) {
        this.note_description = note_description
    }

}