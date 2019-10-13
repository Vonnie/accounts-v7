package com.kinsey.passwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    private enum FragmentEditMode {EDIT, ADD}

    private FragmentEditMode mMode;

    private EditText mCorpNameTextView;
    private EditText mCorpWebsiteTextView;
    private EditText mUserNameTextView;
    private EditText mUserEmailTextView;
    private EditText mNoteTextView;
//    private EditText mSeqTextView;
    private EditText mOpenDate;
    private TextView mtvOpenDate;
    private TextView mtvActvyDate;
    private TextView mAccountIdTextView;
//    , mAccountId2TextView;
    private TextView mAccRefFrom;
    private TextView mAccRefTo;
    DatePickerDialog picker;
    private Account account;
    private Calendar mCalendar;
    private final Calendar cldrOpened = Calendar.getInstance();
    private long lngOpenDate = 0l;
    private boolean blnCorpNameChg = false;

    private OnListenerClicked mListener = null;
    interface OnListenerClicked {
        //        void onSaveClicked(int acctId);
//        void onDateClicked(long acctActvyLong);
//        void setSaveIcon(boolean setting);
        void saveComplete();
        void updateAccount(Account account);
        void updateNewAccount(Account account);
//        void updateDictCorpName();
    }

    private static String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);
    private static String pattern_ymd = "yyyy-MM-dd";
    public static SimpleDateFormat format_ymd = new SimpleDateFormat(
            pattern_ymd, Locale.US);
    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    public AddEditActivityFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
    }

    public boolean canClose() {

        Log.d(TAG, "canClose: " + mCorpNameTextView.getText().toString());
        Log.d(TAG, "canClose: " + account.getCorpName());
        if (!mCorpNameTextView.getText().toString().equals(account.getCorpName())) {
            Log.d(TAG, "canClose: chgs on Corp Name");
            return false;
        }
        if (!mCorpWebsiteTextView.getText().toString().equals(account.getCorpWebsite())) {
            Log.d(TAG, "canClose: chgs on Corp Website");
            return false;
        }
        if (!mUserNameTextView.getText().toString().equals(account.getUserName())) {
            Log.d(TAG, "canClose: chgs on User Name");
            return false;
        }
        if (!mUserEmailTextView.getText().toString().equals(account.getUserEmail())) {
            Log.d(TAG, "canClose: chgs on User Email");
            return false;
        }
        if (!mNoteTextView.getText().toString().equals(account.getNote())) {
            Log.d(TAG, "canClose: chgs on Notes");
            return false;
        }
        if (lngOpenDate != account.getOpenLong()) {
            Log.d(TAG, "canClose: open Date " + lngOpenDate + ":" + account.getOpenLong());
            return false;
        }
//        if (mSeqTextView.getText().toString().equals("")) {
//            if (account.getSequence() != 0) {
//                Log.d(TAG, "canClose: " + account.getSequence());
//                return false;
//            }
//        } else {
//            if (!mSeqTextView.getText().toString().equals(String.valueOf(account.getSequence()))) {
//                Log.d(TAG, "canClose: chgs on sequence " + account.getSequence());
//                return false;
//            }
//        }
        if (mAccRefFrom.getText().toString().equals("")) {
            if (account.getRefFrom() != 0) {
                Log.d(TAG, "canClose: " + account.getRefFrom());
                return false;
            }
        } else {
            if (!mAccRefFrom.getText().toString().equals(String.valueOf(account.getRefFrom()))) {
                Log.d(TAG, "canClose: chgs on from seq");
                return false;
            }
        }
        if (mAccRefTo.getText().toString().equals("")) {
            if (account.getRefTo() != 0) {
                Log.d(TAG, "canClose: " + account.getRefTo());
                return false;
            }
        } else {
            if (!mAccRefTo.getText().toString().equals(String.valueOf(account.getRefTo()))) {
                Log.d(TAG, "canClose: chgs on from seq");
                return false;
            }
        }

        Log.d(TAG, "canClose: can close, no unapplied changes");
        return true;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks.
        Activity activity = getActivity();
        if (!(activity instanceof OnListenerClicked)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditActivityFragment.OnListenerClicked interface");
        }
        mListener = (OnListenerClicked) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mListener.setSaveIcon(true);
//    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        mCorpNameTextView = view.findViewById(R.id.addedit_corp_name);
        mCorpWebsiteTextView = view.findViewById(R.id.addedit_corp_website);
        mUserNameTextView = view.findViewById(R.id.addedit_user_name);
        mUserEmailTextView = view.findViewById(R.id.addedit_user_email);
        mNoteTextView = (EditText) view.findViewById(R.id.addedit_notes);
//        mSeqTextView = (EditText) view.findViewById(R.id.acc_seq);
        mOpenDate = view.findViewById(R.id.acc_open_date);
        mtvOpenDate = view.findViewById(R.id.addedit_open_date);
        mtvActvyDate = view.findViewById(R.id.addedit_actvy_date);
        mAccountIdTextView = view.findViewById(R.id.acc_account_id);
//        mAccountId2TextView = view.findViewById(R.id.acc_account_id_2);
        mAccRefFrom = view.findViewById(R.id.acc_ref_from);
        mAccRefTo = view.findViewById(R.id.acc_ref_to);
//        Button saveButton = view.findViewById(R.id.addedit_save);

//        Button dateButton = view.findViewById(R.id.addedit_btn_date);
        mCorpNameTextView.requestFocus();

        Bundle arguments = getArguments();
        mCalendar = new GregorianCalendar();

        if (arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details.");

//            int accountId = (int) arguments.getSerializable(Account.class.getSimpleName());
            int accountId = (int) arguments.getInt(Account.class.getSimpleName(), -1);
            Log.d(TAG, "onCreateView: " + accountId);

            if (accountId != -1) {
                account = getAccount(accountId);
                if (account == null) {
                    Toast.makeText(getActivity(), "edit account not aligned with account database",
                            Toast.LENGTH_SHORT).show();
                    return view;
                }
                Log.d(TAG, "onCreateView: Task details found, editing...");
                mCorpNameTextView.setText(account.getCorpName());
                mCorpWebsiteTextView.setText(account.getCorpWebsite());
                mUserNameTextView.setText(account.getUserName());
                mUserEmailTextView.setText(account.getUserEmail());
                mNoteTextView.setText(account.getNote());
//                mSeqTextView.setText(String.valueOf(account.getSequence()));
                if (account.getRefFrom() == 0) {
                    mAccRefFrom.setText("");
                } else {
                    mAccRefFrom.setText(String.valueOf(account.getRefFrom()));
                }
                if (account.getRefTo() == 0) {
                    mAccRefTo.setText("");
                } else {
                    mAccRefTo.setText(String.valueOf(account.getRefTo()));
                }
//                Date dte = new Date(account.getActvyLong());
//                mCalendar.setTime(dte);
//                Date openDte = new Date(account.getOpenLong());
//                mOpenDate.setText(openDte.toString());
                if (account.getOpenLong() == 0) {
                    mtvOpenDate.setText("Click here for OpenDate");
                    lngOpenDate = 0;
                } else {
                    mtvOpenDate.setText("OPENED " + format_mdy.format(account.getOpenLong()));
                    Date dteOpen = new Date(account.getOpenLong());
                    cldrOpened.setTime(dteOpen);
                    lngOpenDate = account.getOpenLong();
                }
                mAccountIdTextView.setText("Account Id: " + String.valueOf(account.getPassportId()));
//                mAccountId2TextView.setText("Account Id: " + String.valueOf(account.getPassportId()));
                mtvActvyDate.setText("ActvyDate:\n" + format_ymdtimehm.format(account.getActvyLong()));
                mMode = FragmentEditMode.EDIT;
            } else {
                // No task, so we must be adding a new task, and not editing an  existing one
                mMode = FragmentEditMode.ADD;
                Date dte = new Date();
                mCalendar.setTime(dte);
                account = new Account();
            }
        } else {
            account = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mMode = FragmentEditMode.ADD;
            Date dte = new Date();
            mCalendar.setTime(dte);
            account = new Account();
        }

//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                saveEdits();
//            }
//        });
        if (mMode == FragmentEditMode.ADD) {
            Log.d(TAG, "onCreateView: to Add");
        } else {
            Log.d(TAG, "onCreateView: to Chg");
        }


        ImageButton btnWebsite = view.findViewById(R.id.imgbtn_globe);
        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: for edit");
                if (mCorpWebsiteTextView != null) {
                    verifyEmail(mCorpWebsiteTextView);
                    linkToInternet(mCorpWebsiteTextView.getText().toString());
                }
            }
        });

        final ImageButton btnSave = view.findViewById(R.id.imgbtn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                saveEdits();
                btnSave.setEnabled(true);
            }
        });

//        mOpenDate.setInputType(InputType.TYPE_NULL);
        mtvOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDatePickerDialog(account.getCorpName(), 1);

//                if(mListener != null) {
//                    mListener.onDateClicked(account.getActvyLong());
//                }

                int day = cldrOpened.get(Calendar.DAY_OF_MONTH);
                int month = cldrOpened.get(Calendar.MONTH);
                int year = cldrOpened.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mtvOpenDate.setText("Opened " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                                mCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                lngOpenDate = mCalendar.getTimeInMillis();
                                Log.d(TAG, "onDateSet: " + lngOpenDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


//        dateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mtvOpenDate.setText("Selected Date: "+ mOpenDate.getText());
//
//            }
//        });

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: corp restore");
            mCorpNameTextView.setText(
                    savedInstanceState.getString(AccountsContract.Columns.CORP_NAME_COL));
            Log.d(TAG, "onCreateView: " + mCorpNameTextView.getText().toString());

            mCorpWebsiteTextView.setText(
                    savedInstanceState.getString(AccountsContract.Columns.CORP_WEBSITE_COL));

            mUserNameTextView.setText(
                    savedInstanceState.getString(AccountsContract.Columns.USER_NAME_COL));

            mUserEmailTextView.setText(
                    savedInstanceState.getString(AccountsContract.Columns.USER_EMAIL_COL));

            mNoteTextView.setText(
                    savedInstanceState.getString(AccountsContract.Columns.NOTE_COL));

//            mSeqTextView.setText(
//                    savedInstanceState.getString(AccountsContract.Columns.SEQUENCE_COL));

            mAccRefFrom.setText(
                    savedInstanceState.getString(AccountsContract.Columns.REF_FROM_COL));

            mAccRefTo.setText(
                    savedInstanceState.getString(AccountsContract.Columns.REF_TO_COL));


            lngOpenDate =
                    savedInstanceState.getLong(AccountsContract.Columns.OPEN_DATE_COL);
            if (lngOpenDate == 0) {
                mtvOpenDate.setText("Click here for OpenDate");
            } else {
                Date dteOpen = new Date(lngOpenDate);
                cldrOpened.setTime(dteOpen);
                mtvOpenDate.setText("OPENED " + format_mdy.format(lngOpenDate));
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: save b4 switch");
        outState.putString(AccountsContract.Columns.CORP_NAME_COL,
                mCorpNameTextView.getText().toString());
        outState.putString(AccountsContract.Columns.CORP_WEBSITE_COL,
                mCorpWebsiteTextView.getText().toString());
        outState.putString(AccountsContract.Columns.USER_NAME_COL,
                mUserNameTextView.getText().toString());
        outState.putString(AccountsContract.Columns.USER_EMAIL_COL,
                mUserEmailTextView.getText().toString());
        outState.putString(AccountsContract.Columns.NOTE_COL,
                mNoteTextView.getText().toString());
//        outState.putString(AccountsContract.Columns.SEQUENCE_COL,
//                mSeqTextView.getText().toString());
        outState.putString(AccountsContract.Columns.REF_FROM_COL,
                mAccRefFrom.getText().toString());
        outState.putString(AccountsContract.Columns.REF_TO_COL,
                mAccRefTo.getText().toString());
        outState.putLong(AccountsContract.Columns.OPEN_DATE_COL,
                lngOpenDate);

    }

//    @Override
//    public void onDestroyView() {
//        mListener.setSaveIcon(false);
//        super.onDestroyView();
//    }



    private void linkToInternet(String webpage) {
        Log.d(TAG, "linkToInternet: " + account);
        if (webpage.equals("")
                || webpage.toLowerCase().equals("http://")
                || webpage.toLowerCase().equals("https://")) {
            Toast.makeText(getContext(),
                    "Account has no corp website to link to",
                    Toast.LENGTH_LONG).show();
        } else {
            if (!webpage.equals("")) {
                Log.d(TAG, "linkToInternet: " + webpage);
                vewInternet(webpage);
//                webview.loadUrl(account.getCorpWebsite());
            } else {
                Log.d(TAG, "linkToInternet: none");;
            }
        }
    }


    private void vewInternet(String webpage) {

        Log.d(TAG, "vewInternet: webpage " + webpage);
        Uri uri = Uri.parse(webpage);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }


    }



    public int getAcctId() {
        return account.getId();
    }

    public void saveEdits() {
        // Update the database if at least one field has changed.
        // - There's no need to hit the database unless this has happened.
//                int so;     // to save repeated conversions to int.
//                if(mSortOrderTextView.length()>0) {
//                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
//                } else {
//                    so = 0;
//                }

//        Log.d(TAG, "onClick: ");

        if (canClose()) {
            return;
        }
        if (!verifiedAccount()) {
            return;
        }

        new saveAccount().execute("");


//                if(mListener != null) {
//                    mListener.onSaveClicked(account.getId());
//                }

    }

    public Account getAccount(int id) {
//        int iId = 0;
        Cursor cursor = getActivity().getContentResolver()
                .query(AccountsContract.buildIdUri(id), null, null, null, null);
        if (cursor == null) {
            return null;
        } else {
            if (cursor.moveToFirst()) {
                Account account = AccountsContract.getAccountFromCursor(cursor);
                cursor.close();
                return account;
            }
            cursor.close();
            return null;
        }
    }


    private int getMaxValue(String col) {
        int iId = 1;
        Cursor cursor = getActivity().getContentResolver().query(
                AccountsContract.CONTENT_MAX_VALUE_URI, null, null, null, col);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int iIndex = cursor.getColumnIndex(col);
                iId = cursor.getInt(iIndex) + 1;
//                Log.d(TAG, "getMaxValue: " + iId);
            }
            cursor.close();
        }

//        Log.d(TAG, "getMaxValue: " + iId);
        return iId;
    }


    private boolean verifiedAccount() {
        if (mCorpNameTextView.getText().toString().equals("")) {
            mCorpNameTextView.setError("Corporation name is required");
            mCorpNameTextView.requestFocus();
//            Toast.makeText(getActivity(),
//                    "Corporation name is required",
//                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mCorpWebsiteTextView.getText().toString().equals("")) {
            if (mCorpWebsiteTextView.getText().toString().toLowerCase().startsWith("http://")
                    || mCorpWebsiteTextView.getText().toString().toLowerCase().startsWith("https://")) {
            } else {
                mCorpWebsiteTextView.setText("http://" + mCorpWebsiteTextView.getText().toString());
            }
        }
        if (mUserNameTextView.getText().toString().equals("")) {
            mUserNameTextView.setError("User name is required");
            mUserNameTextView.requestFocus();
            return false;
        }
        if (mUserEmailTextView.getText().toString().equals("")) {
            mUserEmailTextView.setError("User email is required");
            mUserEmailTextView.requestFocus();
            return false;
        }
        if (!isEmailValid(mUserEmailTextView.getText().toString())) {
            mUserEmailTextView.setError("User email is invalid format");
            mUserEmailTextView.requestFocus();
            return false;
        }

        if (!mAccRefFrom.getText().toString().equals("")) {
            if (!isNumeric(mAccRefFrom)) {
                mAccRefFrom.setError("If From-Ref is given, must have a numeric value");
                mAccRefFrom.requestFocus();
                return false;
            } else if (!isOnDBByAcctId(mAccRefFrom.getText().toString())) {
                mAccRefFrom.requestFocus();
                mAccRefFrom.setError("From-Ref not an ID with Accounts Database");
                return false;
            }
        }

        if (!mAccRefTo.getText().toString().equals("")) {
            if (!isNumeric(mAccRefTo)) {
                mAccRefTo.setError("If To-Ref is given, must have a numeric value");
                mAccRefTo.requestFocus();
                return false;
            } else if (!isOnDBByAcctId(mAccRefTo.getText().toString())) {
                mAccRefTo.requestFocus();
                mAccRefTo.setError("To-Ref not an ID with Accounts Database for " + mAccRefTo.getText().toString());
                return false;
            }
        }
//        if (!mRefIdFromTextView.getText().toString().equals("")) {
//            if (!isIdOnDB(mRefIdFromTextView.getText().toString())) {
//                mRefIdFromTextView.setError("Reference back id does not exists");
//                return false;
//            };
//        }
//        if (!mRefIdToTextView.getText().toString().equals("")) {
//            if (!isIdOnDB(mRefIdToTextView.getText().toString())) {
//                mRefIdToTextView.setError("Reference to id does not exists");
//                return false;
//            };
//        }

        return true;
    }

    private void verifyEmail(TextView tvWebsite) {
        if (!tvWebsite.getText().toString().equals("")) {
            if (tvWebsite.getText().toString().toLowerCase().startsWith("http://")
                    || tvWebsite.getText().toString().toLowerCase().startsWith("https://")) {
            } else {
                tvWebsite.setText("http://" + mCorpWebsiteTextView.getText().toString());
            }
        }
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

    private boolean isNumeric(TextView tv) {
        String regexStr = "^[0-9]*$";

        if (tv.getText().toString().trim().matches(regexStr)) {
            //write code here for success
            return true;
        } else {
            // write code for failure
            return false;
        }
    }

    private boolean isOnDBByAcctId(String strId) {
        Log.d(TAG, "isOnDB: strId " + strId);
        int id = Integer.parseInt(strId);
        Cursor cursorSearch = getActivity().getContentResolver().query(
                AccountsContract.buildAcctIdUri(id), null, null, null, null);


        if (cursorSearch.getCount() == 0) {

            return false;
        }

        return true;

    }

    private boolean isOnDB(String strId) {
        Log.d(TAG, "isOnDB: strId " + strId);
        int id = Integer.parseInt(strId);
        Cursor cursorSearch = getActivity().getContentResolver().query(
                AccountsContract.buildIdUri(id), null, null, null, null);


        if (cursorSearch.getCount() == 0) {

            return false;
        }

        return true;

    }



    protected class saveAccount extends AsyncTask<String, Integer, String> {

        private static final String TAG = "saveAccount";

        ProgressDialog dialog;

        Handler h = new Handler();


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//			super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
            dialog = new ProgressDialog(getActivity());

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Saving Account");
            CharSequence message = new String("one moment please...");
            dialog.setMessage(message);
            dialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
//			summaryAdapter.loadList();
            try {

                h.post(new Runnable() {
                    public void run() {
                        editChecks();
                    }
                });
            } catch (Exception e1) {
                Log.e(TAG, "doInBackground: error " + e1.getMessage(), e1);
                return "error";
            }
            try {
                Thread.sleep(88);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            dialog.dismiss();

            return "";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            dialog.incrementProgressBy(values[0]);
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
//			summaryAdapter.notifyDataSetChanged();
//			listview.setAdapter(summaryAdapter);
            dialog.dismiss();

            Log.d(TAG, "onPostExecute: result " + result);

            super.onPostExecute(result);
        }


        public void editChecks() {
            ContentResolver contentResolver = getActivity().getContentResolver();
            ContentValues values = new ContentValues();

            switch (mMode) {
                case EDIT:
                    if (account == null) {
                        // remove lint warnings, will never execute
                        break;
                    }
                    Log.d(TAG, "onClick: " + account);
                    if (!mCorpNameTextView.getText().toString().equals(account.getCorpName())) {
                        blnCorpNameChg = true;
                        values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                    }
                    if (!mCorpWebsiteTextView.getText().toString().equals(account.getCorpWebsite())) {
                        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                    }
                    if (!mUserNameTextView.getText().toString().equals(account.getUserName())) {
                        values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                    }
                    if (!mUserEmailTextView.getText().toString().equals(account.getUserEmail())) {
                        values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
                    }
                    if (!mNoteTextView.getText().toString().equals(account.getNote())) {
                        values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());
                    }
//                if (!mSeqTextView.getText().toString().equals(account.getSequence())) {
//                    values.put(AccountsContract.Columns.SEQUENCE_COL, mSeqTextView.getText().toString());
//                }
                    if (mAccRefFrom.getText().toString().equals("")) {
                        if (account.getRefFrom() != 0) {
                            values.put(AccountsContract.Columns.REF_FROM_COL, 0);
                        }
                    } else {
//                    Log.d(TAG, "saveEdits: " + mAccRefFrom.getText().toString());
//                    Log.d(TAG, "saveEdits: " + account.getRefFrom());
                        if (!mAccRefFrom.getText().toString().equals(account.getRefFrom())) {
                            values.put(AccountsContract.Columns.REF_FROM_COL, mAccRefFrom.getText().toString());
                        }
                    }
                    if (mAccRefTo.getText().toString().equals("")) {
                        if (account.getRefTo() != 0) {
                            values.put(AccountsContract.Columns.REF_TO_COL, 0);
                        }
                    } else {
                        if (!mAccRefTo.getText().toString().equals(account.getRefTo())) {
                            values.put(AccountsContract.Columns.REF_TO_COL, mAccRefTo.getText().toString());
                        }
                    }


                    if (lngOpenDate != account.getOpenLong()) {
                        Log.d(TAG, "onClick: " + lngOpenDate + ":" + account.getOpenLong());
                        values.put(AccountsContract.Columns.OPEN_DATE_COL, lngOpenDate);
                    }
//                        if(so != task.getSortOrder()) {
//                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
//                        }
//                        if(values.size() != 0) {
//                            Log.d(TAG, "onClick: updating task");
//                            contentResolver.update(TasksContract.buildTaskUri(task.getId()), values, null, null);
//                        }

                    if (values.size() != 0) {
                        values.put(AccountsContract.Columns.ACTVY_DATE_COL, System.currentTimeMillis());
                        Log.d(TAG, "onClick: " + account);
                        Log.d(TAG, "onClick: " + account.getOpenLong());
                        contentResolver.update(AccountsContract.buildIdUri(account.getId()), values, null, null);
                        account = getAccount(account.getId());
                        Log.d(TAG, "onClick: " + account);
                        Log.d(TAG, "onClick: " + account.getOpenLong());
                        mtvActvyDate.setText("ActvyDate:\n" + format_ymdtimehm.format(account.getActvyLong()));
                        mListener.updateAccount(account);

                        if (blnCorpNameChg) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            sharedPreferences.edit().putBoolean(MainActivity.SEARCH_DICT_REFRESHED, false).apply();
                            //                        mListener.updateDictCorpName();
                            blnCorpNameChg = false;
                        }
                        Toast.makeText(getActivity(),
                                values.size() - 1 + " changed columns for account " + account.getCorpName(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "no changes detected",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADD:
                    if (mCorpNameTextView.length() > 0) {
                        Log.d(TAG, "onClick: adding new task");
                        values.put(AccountsContract.Columns.PASSPORT_ID_COL,
                                String.valueOf(getMaxValue(AccountsContract.Columns.PASSPORT_ID_COL)));
                        values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                        values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                        values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
//                    if (!mSeqTextView.getText().toString().equals("")) {
//                        values.put(AccountsContract.Columns.SEQUENCE_COL, mSeqTextView.getText().toString());
//                    }
                        if (mAccRefFrom.getText().toString().equals("")) {
                            values.put(AccountsContract.Columns.REF_FROM_COL, 0);
                        } else {
                            values.put(AccountsContract.Columns.REF_FROM_COL, mAccRefFrom.getText().toString());
                        }
                        if (mAccRefTo.getText().toString().equals("")) {
                            values.put(AccountsContract.Columns.REF_TO_COL, 0);
                        } else {
                            values.put(AccountsContract.Columns.REF_TO_COL, mAccRefTo.getText().toString());
                        }
                        values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());

                        if (lngOpenDate == 0) {
                            lngOpenDate = System.currentTimeMillis();
                            mtvOpenDate.setText("Opened " + format_mdy.format(lngOpenDate));
                            Date dteOpen = new Date(lngOpenDate);
                            cldrOpened.setTime(dteOpen);
                        }
                        values.put(AccountsContract.Columns.OPEN_DATE_COL, lngOpenDate);

                        values.put(AccountsContract.Columns.ACTVY_DATE_COL, System.currentTimeMillis());

                        Uri uri = contentResolver.insert(AccountsContract.CONTENT_URI, values);
                        long id = AccountsContract.getId(uri);
                        account = getAccount((int) id);
                        Log.d(TAG, "onClick: " + account);
                        Log.d(TAG, "onClick: " + account.getOpenLong());
                        mtvActvyDate.setText("ActvyDate:\n" + format_ymdtimehm.format(account.getActvyLong()));
                        mListener.updateNewAccount(account);

                        mAccountIdTextView.setText("Account Id: " + String.valueOf(account.getPassportId()));
//                    mAccountId2TextView.setText("Account Id: " + String.valueOf(account.getPassportId()));


                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        sharedPreferences.edit().putBoolean(MainActivity.SEARCH_DICT_REFRESHED, false).apply();

                        Toast.makeText(getActivity(),
                                "New account " + account.getCorpName() + " added",
                                Toast.LENGTH_LONG).show();
//                    if(mListener != null) {
//                        mListener.onSaveClicked(account.getId());
//                    }

                    }

                    break;
            }
            if (mListener != null) {
                mListener.saveComplete();
            }
            Log.d(TAG, "onClick: Done editing");

        }


    }
}

