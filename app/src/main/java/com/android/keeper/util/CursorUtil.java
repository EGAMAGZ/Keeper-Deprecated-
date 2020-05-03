package com.android.keeper.util;

import android.database.Cursor;

public class CursorUtil {

    public static Integer checkNullInteger(int pos, Cursor cursor){
        Integer value=null;
        if(!cursor.isNull(pos)){
            value=cursor.getInt(pos);
        }
        return value;
    }
}
