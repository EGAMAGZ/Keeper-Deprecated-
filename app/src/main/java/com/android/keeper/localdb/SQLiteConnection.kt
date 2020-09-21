package com.android.keeper.localdb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.android.keeper.localdb.utilities.NotesUtilities
import com.android.keeper.localdb.utilities.RemindersUtilities
import com.android.keeper.localdb.utilities.TasksUtilities
import org.junit.runner.RunWith

class SQLiteConnection(context: Context?, name: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase.execSQL(TasksUtilities.CREATE_TASKS_TABLE)
        sqLiteDatabase.execSQL(NotesUtilities.CREATE_NOTES_TABLE)
        sqLiteDatabase.execSQL(RemindersUtilities.CREATE_REMINDER_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TasksUtilities.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesUtilities.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RemindersUtilities.TABLE_NAME)
    }
}