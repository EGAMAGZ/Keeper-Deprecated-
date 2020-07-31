package com.android.keeper.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Represents date picker dialog fragment
 *
 * @author Gamaliel Garcia
 * */
public class DatePickerDialogFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener onDateSetListener;
    DialogInterface.OnDismissListener onDismissListener;

    public DatePickerDialogFragment() {}

    /**
     * Sets callback when the date is selected by the user
     * */
    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        onDateSetListener = ondate;
    }

    /**
     * Sets callback when the user dismiss date picker dialog
     * */
    public void setOnDismissListener(DialogInterface.OnDismissListener ondismiss){
        //TODO: Check if this is executed
        onDismissListener=ondismiss;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),onDateSetListener, year,month,day);
    }
}
