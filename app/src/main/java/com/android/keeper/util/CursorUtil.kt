package com.android.keeper.util

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class CursorUtil(cursor: Cursor) {
    private val cursor: Cursor?
    fun getInt(column: String?): Int {
        return cursor.getInt(cursor.getColumnIndex(column))
    }

    fun getString(column: String?): String? {
        return cursor.getString(cursor.getColumnIndex(column))
    }

    fun checkNullInteger(column: String?): Int? {
        var value: Int? = null
        if (!cursor.isNull(cursor.getColumnIndex(column))) {
            value = cursor.getInt(cursor.getColumnIndex(column))
        }
        return value
    }

    companion object {
        fun checkNullInteger(column: String?, cursor: Cursor?): Int? {
            var value: Int? = null
            if (!cursor.isNull(cursor.getColumnIndex(column))) {
                value = cursor.getInt(cursor.getColumnIndex(column))
            }
            return value
        }

        fun getInt(column: String?, cursor: Cursor): Int {
            return cursor.getInt(cursor.getColumnIndex(column))
        }

        fun getString(column: String?, cursor: Cursor): String? {
            return cursor.getString(cursor.getColumnIndex(column))
        }

        fun getCount(sql: String?, database: SQLiteDatabase?): Long {
            var count: Long = 0
            try {
                val cursor = database.rawQuery(sql, null)
                count = cursor.count.toLong()
                cursor.close()
            } catch (e: Exception) {
                count = 0
            } finally {
                return count
            }
        }
    }

    init {
        this.cursor = cursor
    }
}