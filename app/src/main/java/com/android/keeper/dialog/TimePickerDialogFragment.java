package com.android.keeper.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.android.keeper.util.PreferenceUtil;

import java.text.DateFormat;
import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {

    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DialogInterface.OnDismissListener onDismissListener;

    public TimePickerDialogFragment() {}

    /**
     * Sets callback when the time is selected by the user
     * */
    public void setCallBack(TimePickerDialog.OnTimeSetListener onTimeSet){
        onTimeSetListener=onTimeSet;
    }

    /**
     * Sets callback when the user dismiss time picker dialog
     * */
    public void setOnDismissListener(DialogInterface.OnDismissListener ondismiss){
        onDismissListener=ondismiss;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        switch(PreferenceUtil.getInstance(getContext()).getClockFormat()){
            case "auto":
                timePickerDialog= new TimePickerDialog(getActivity(),onTimeSetListener,hour,minute,android.text.format.DateFormat.is24HourFormat(getActivity()));
                break;
            case "24hr":
                timePickerDialog= new TimePickerDialog(getActivity(),onTimeSetListener,hour,minute,true);
                break;
            case "12hr":
                timePickerDialog= new TimePickerDialog(getActivity(),onTimeSetListener,hour,minute,false);
                break;
        }
        return timePickerDialog;
    }
}
