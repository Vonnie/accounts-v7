package com.kinsey.passwords;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountActivityFragment extends Fragment {
    private static final String TAG = "AccountActivityFragment";

    public enum FragmentEditMode { EDIT, ADD };
    private FragmentEditMode mMode;

    private EditText mCorpNameTextView;
    private EditText mUserNameTextView;
    private EditText mUserEmailTextView;
    private EditText mCorpWebsiteTextView;
    private EditText mNoteTextView;
    private TextView mOpenDateTextView;
    private TextView mAccountIdTextView;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;
    private Button mSaveButton;

    private OnSaveClicked mSaveListener = null;

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    interface OnSaveClicked {
        void onSaveClicked();
    }

    public AccountActivityFragment() {
        Log.d(TAG, "AccountActivityFragment: constructor called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mCorpNameTextView = (EditText) view.findViewById(R.id.acc_corp_name);
        mUserNameTextView = (EditText) view.findViewById(R.id.acc_user_name);
        mUserEmailTextView = (EditText) view.findViewById(R.id.acc_user_email);
        mCorpWebsiteTextView = (EditText) view.findViewById(R.id.acc_corp_website);
        mNoteTextView = (EditText) view.findViewById(R.id.acc_notes);
        mOpenDateTextView = (TextView) view.findViewById(R.id.acc_open_date);
        mDtePickOpen = (DatePicker) view.findViewById(R.id.acc_datePicker);
        mAccountIdTextView = (TextView) view.findViewById(R.id.acc_account_id);
        mSaveButton = (Button) view.findViewById(R.id.acc_save);

//        Bundle arguments = getArguments();
        Bundle arguments = getActivity().getIntent().getExtras();  // The line we'll change later

        final Account account;
        if (arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details.");

            account = (Account) arguments.getSerializable(Account.class.getSimpleName());
            if (account != null) {
                Log.d(TAG, "onCreateView: Account found, editing..." + account.getId());
                mCorpNameTextView.setText(account.getCorpName());
                mUserNameTextView.setText(account.getUserName());
                mUserEmailTextView.setText(account.getUserEmail());
                mCorpWebsiteTextView.setText(account.getCorpWebsite());
                mNoteTextView.setText(account.getNote());
                if (account.getOpenLong() != 0) {
                    Log.d(TAG, "onCreateView: openLong " + account.getOpenLong());
                    mOpenDateTextView.setText("Open: " + format_ymdtimehm.format(account.getOpenLong()));
                    mDtePickOpen.setMaxDate(new Date().getTime());
                    mDtePickOpen.setMinDate(0);
                    Date dte = new Date(account.getOpenLong());
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(dte);
                    lngOpenDate = c1.getTimeInMillis();
                    mDtePickOpen.init(c1.get(Calendar.YEAR),
                            c1.get(Calendar.MONTH),
                            c1.get(Calendar.DAY_OF_MONTH),
                            new DatePicker.OnDateChangedListener(){
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Calendar c2 = Calendar.getInstance();
                                    c2.set(year, monthOfYear, dayOfMonth);
                                    lngOpenDate = c2.getTimeInMillis();
                                }
                            });
                } else {
                    Log.d(TAG, "onCreateView: zero open date");
                }

                Log.d(TAG, "onCreateView: passwordId " + account.getPassportId());
//                mAccountIdTextView.setText("see this");
//                Log.d(TAG, "onCreateView: textView " + mAccountIdTextView.getText().toString());
                mAccountIdTextView.setText(String.valueOf(account.getPassportId()));


//                mSortOrderTextView.setText(Integer.toString(account.getSortOrder()));
                mMode = FragmentEditMode.EDIT;
            } else {
                // No task, so we must be adding a new task, and not editing an existing
                mMode = FragmentEditMode.ADD;
            }
        } else {
            account = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mMode = FragmentEditMode.ADD;
        }


        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Update the database if at least one field has changed.
                // -- There's no need to hit the database unless this has happened.
//                int so; // to save repeated conversions to init.
//                if (mSortOrderTextView.length() > 0) {
//                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
//                } else {
//                    so = 0;
//                }

                if (!verifiedAccount()) {
                    return;
                }
                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (!mCorpNameTextView.getText().toString().equals((account.getCorpName()))) {
                            values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                        }
                        if (!mUserNameTextView.getText().toString().equals((account.getUserName()))) {
                            values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                        }
                        if (!mUserEmailTextView.getText().toString().equals((account.getUserEmail()))) {
                            values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
                        }
                        if (!mCorpWebsiteTextView.getText().toString().equals((account.getCorpWebsite()))) {
                            values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                        }
                        if (!mNoteTextView.getText().toString().equals((account.getNote()))) {
                            values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());
                        }
                        if (lngOpenDate != account.getOpenLong()) {
                            values.put(AccountsContract.Columns.OPEN_DATE_COL, lngOpenDate);
                        }
//                        if (so != task.getSortOrder()) {
//                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
//                        }
                        if (values.size() != 0) {
                            Log.d(TAG, "onClick: updating accountId " + AccountsContract.buildIdUri(account.getId()));
                            contentResolver.update(AccountsContract.buildIdUri(account.getId()), values, null, null);
                            Toast.makeText(getActivity(),
                                    values.size() + " changed columns",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    " no changes detected",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ADD:
                        if (mCorpNameTextView.length()>0) {
                            Log.d(TAG, "onClick: adding new account");
                            values.put(AccountsContract.Columns.PASSPORT_ID_COL,
                                    String.valueOf(getMaxValue(AccountsContract.Columns.PASSPORT_ID_COL)));
                            values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                            values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                            values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
                            values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                            values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());
                            values.put(AccountsContract.Columns.OPEN_DATE_COL, String.valueOf(System.currentTimeMillis()));
                            Log.d(TAG, "onClick: open date entered "  + mOpenDateTextView.getText().toString());
                            Log.d(TAG, "onClick: open date millis " + System.currentTimeMillis());
//                            values.put(AccountsContract.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(AccountsContract.CONTENT_URI, values);
                        }
                        break;
                }

                Log.d(TAG, "onClick: Done editing");

                if (mSaveListener != null) {
                    mSaveListener.onSaveClicked();
                }
            }
        });
        return view;
//        return inflater.inflate(R.layout.fragment_account, container, false);
    }



    private boolean verifiedAccount() {
        if (mCorpNameTextView.getText().toString().equals("")) {
            mCorpNameTextView.setError("Corporation name is required");
//            Toast.makeText(getActivity(),
//                    "Corporation name is required",
//                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (mUserNameTextView.getText().toString().equals("")) {
            mUserNameTextView.setError("User name is required");
            return false;
        }
        if (mUserEmailTextView.getText().toString().equals("")) {
            mUserEmailTextView.setError("User email is required");
            return false;
        }
        if (!isEmailValid(mUserEmailTextView.getText().toString())) {
            mUserEmailTextView.setError("User email is invalid format");
            return false;
        }
        if (!mCorpWebsiteTextView.getText().toString().equals("")) {
            if (!mCorpWebsiteTextView.getText().toString().toLowerCase().startsWith("http://")) {
                mCorpWebsiteTextView.setText("http://" + mCorpWebsiteTextView.getText().toString());
            }
        }

        return true;
    }
    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    private int getMaxValue(String col) {
        int iId = 0;
        Cursor cursor = getActivity().getContentResolver().query(
                AccountsContract.CONTENT_MAX_VALUE_URI, null, null, null, col);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int iIndex = cursor.getColumnIndex(col);
                iId = cursor.getInt(iIndex) + 1;
                Log.d(TAG, "getMaxValue: " + iId);
            }
            cursor.close();
        }

        Log.d(TAG, "getMaxValue: " + iId);
        return iId;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof OnSaveClicked)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditActivityFragment interface");
        }
        mSaveListener = (OnSaveClicked) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mSaveListener = null;
    }

}
