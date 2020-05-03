package com.android.keeper.util;

import java.text.DateFormat;
import java.util.Calendar;

public class CalendarUtil {

    private Calendar calendar;
    private int timeStyle;
    /** Here is use Integer instead of int because with it you can set is as null(also default value).
     * The default of int is 0*/
    private Integer year,month,day,hour,minute;

    public CalendarUtil(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        calendar= Calendar.getInstance();
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getDateFormat(int timeStyle){
        String date="";
        if(year!=null || month!=null || day!=null){
            calendar.set(year,month,day);
            date= DateFormat.getDateInstance(timeStyle).format(calendar.getTime());
        }
        return date;
    }

    public String getTimeFormat(int timeStyle){
        String time="";
        if(year!=null || month!=null || day!=null){
            calendar.set(year,month,day,hour,minute);
            //Will change automatically between 12 and 24 format
            time= DateFormat.getTimeInstance(timeStyle).format(calendar.getTime());
        }
        return time;
    }
}
