package com.android.keeper.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CursorUtil {

    public static Integer checkNullInteger(int pos, Cursor cursor){
        Integer value=null;
        if(!cursor.isNull(pos)){
            value=cursor.getInt(pos);
        }
        return value;
    }

    public static long getCount(String sql, SQLiteDatabase database){
        long count=0;
        try{
            Cursor cursor=database.rawQuery(sql,null);
            count=cursor.getCount();
        }catch (Exception e){
            count=0;
        }finally {
           return  count;
        }
    }
}
