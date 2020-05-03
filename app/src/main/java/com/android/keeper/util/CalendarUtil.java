package com.android.keeper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    private Calendar calendar;
    private int timeStyle;
    private Context context;
    private SharedPreferences sharedPreferences;

    /** Here is use Integer instead of int because with it you can set is as null(also default value).
     * The default of int is 0*/
    private Integer year,month,day,hour,minute;

    public CalendarUtil(@NonNull final Context context, Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        sharedPreferences=context.getSharedPreferences("keeper_settings", Context.MODE_PRIVATE);
        calendar= Calendar.getInstance();
        this.context=context;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    private String getTimeSettings(){
        String option=sharedPreferences.getString("clock_format","auto");
        return option;
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
        DateFormat dateFormat;
        if(year!=null || month!=null || day!=null){
            calendar.set(year,month,day,hour,minute);
            //Will change automatically between 12 and 24 format
            switch(getTimeSettings()){
                case "12hr":
                    dateFormat=new SimpleDateFormat("KK:mm a");
                    time=dateFormat.format(calendar.getTime());
                    break;
                case "24hr":
                    dateFormat=new SimpleDateFormat("HH:mm");
                    time=dateFormat.format(calendar.getTime());
                    break;
                case "auto":
                    time=DateFormat.getTimeInstance(timeStyle).format(calendar.getTime());
                    break;
            }
            //time= DateFormat.getTimeInstance(timeStyle).format(calendar.getTime());
        }
        return time;
    }
}
