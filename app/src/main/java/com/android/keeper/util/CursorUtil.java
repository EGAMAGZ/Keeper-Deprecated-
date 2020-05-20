package com.android.keeper.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

public class CursorUtil {

    private Cursor cursor;

    public CursorUtil(@NonNull Cursor cursor) {
        this.cursor = cursor;
    }

    public int getInt(String column){
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public String getString(String column){
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public Integer checkNullInteger(String column){
        Integer value=null;
        if(!cursor.isNull(cursor.getColumnIndex(column))){
            value=cursor.getInt(cursor.getColumnIndex(column));
        }
        return value;
    }

    public static Integer checkNullInteger(String column, Cursor cursor){
        Integer value=null;
        if(!cursor.isNull(cursor.getColumnIndex(column))){
            value=cursor.getInt(cursor.getColumnIndex(column));
        }
        return value;
    }

    public static int getInt(String column,@NonNull Cursor cursor){
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public static String getString(String column, @NonNull Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public static long getCount(String sql, SQLiteDatabase database){
        long count=0;
        try{
            Cursor cursor=database.rawQuery(sql,null);
            count=cursor.getCount();
            cursor.close();
        }catch (Exception e){
            count=0;
        }finally {
           return count;
        }
    }
}
