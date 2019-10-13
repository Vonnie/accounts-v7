package com.kinsey.passwords.provider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "DatePickerFragment";

    public static final String DATE_PICKER_ID = "ID";
    public static final String DATE_PICKER_TITLE = "TITLE";
    public static final String DATE_PICKER_DATE = "DATE";

    int mDialogId = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        final GregorianCalendar cal = new GregorianCalendar();
        String title = null;

        Bundle arguments = getArguments();
        if (arguments != null) {
            mDialogId = arguments.getInt(DATE_PICKER_ID);
            title = arguments.getString(DATE_PICKER_TITLE);

            // If we passed a date, use it; otherwise leave cal set to the current date.
            Date givenDate = (Date) arguments.getSerializable(DATE_PICKER_DATE);
            if (givenDate!= null) {
                cal.setTime(givenDate);
                Log.d(TAG, "onCreateDialog, retrieved date = " + givenDate.toString());
            }
        }

        Log.d(TAG, "onCreateDialog: " + cal.toString());
        int year = cal.get(GregorianCalendar.YEAR);
        int month = cal.get(GregorianCalendar.MONTH);
        int day = cal.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(), this, year, month, day);
        if (title != null) {
            dpd.setTitle(title);
        }
        return dpd;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities using this dialog must implement its callbacks.
        if (!(context instanceof DatePickerDialog.OnDateSetListener)) {
            throw new ClassCastException(context.toString() + " must implement DatePickerDialog.OnDateSetListener interface");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: ");
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        if (listener != null) {
            // Notify caller of the user-selected values
            view.setTag(mDialogId); // pass the id back back in the tag, to save the caller storing their own copy.
            listener.onDateSet(view, year, month, dayOfMonth);
        }
        Log.d(TAG, "onDateSet: Exiting onDateSet");
    }
}
