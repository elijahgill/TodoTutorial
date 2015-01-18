package com.elijahgill.todotutorial;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Elijah on 1/17/2015.
 */
public class DatePickerFragment extends DialogFragment
 {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use current date as default
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // create new instance
        return new DatePickerDialog(getActivity(), (TodoDetailActivity)getActivity(), year, month, day);

    }

}
