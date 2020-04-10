package com.android.keeper.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.android.keeper.localdb.utilities.NotesUtilities;
import com.android.keeper.localdb.utilities.RemindersUtilities;
import com.android.keeper.localdb.utilities.TasksUtilities;

public class SQLiteConnection extends SQLiteOpenHelper {

    public SQLiteConnection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TasksUtilities.CREATE_TASKS_TABLE);
        sqLiteDatabase.execSQL(NotesUtilities.CREATE_NOTES_TABLE);
        sqLiteDatabase.execSQL(RemindersUtilities.CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TasksUtilities.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NotesUtilities.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+RemindersUtilities.TABLE_NAME);
    }
}
