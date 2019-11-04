package com.kinsey.passwords.tools;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kinsey.passwords.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private onDatePickerListener mListener;
    public int day_;
    public int month_;
    public int year_;
    private DatePickerDialog datePickerDialog;


    public DatePickerFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.MaterialAlertDialog_MaterialComponents_Title_Text_CenterStacked,
                this, year_, month_, day_);
//        R.style.AppTheme, this, year_, month_, day_);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        // Create a TextView programmatically.
        TextView tv = new TextView(getActivity());

        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.setText("This is a custom title.");
        tv.setTextColor(Color.parseColor("#ff0000"));
        tv.setBackgroundColor(Color.parseColor("#FFD2DAA7"));


        datePickerDialog.setTitle("   Date this account was opened.");

        return datePickerDialog;

//        // Use the current date as the default date in the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        callListener(year, month, dayOfMonth);
    }


    public DialogFragment setCallbackListener(onDatePickerListener listener) {
        mListener = listener;
        return null;
    }


    private void callListener(int year, int month, int day) {
        if (mListener != null) mListener.onDataSet(year, month, day);
    }

    public interface onDatePickerListener {
        void onDataSet(int year, int month, int day);
    }
}
