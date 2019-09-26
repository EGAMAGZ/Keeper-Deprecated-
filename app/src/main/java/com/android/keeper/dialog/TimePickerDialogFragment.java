package com.android.keeper.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {

    private SharedPreferences sharedPreferences;
    private TimePickerDialog timePickerDialog;

    private String clockFormat;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        sharedPreferences=getContext().getSharedPreferences("keeper_settings", Context.MODE_PRIVATE);

        clockFormat=sharedPreferences.getString("clock_format","auto");
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        //TODO: ADD SETTINGS OPTION THAT WILL ENABLE OR DISABLE MANUALLY(OR AUTOMATIC FROM SYSTEM) THE 24 FORMAT
        if(clockFormat.equals("auto")){
            timePickerDialog= new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hour,minute,android.text.format.DateFormat.is24HourFormat(getActivity()));
        }else if(clockFormat.equals("24hr")){
            timePickerDialog= new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hour,minute,true);
        }else if(clockFormat.equals("12hr")){
            timePickerDialog= new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hour,minute,false);
        }
        return timePickerDialog;
    }
}
