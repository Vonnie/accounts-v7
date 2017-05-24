package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kinsey.passwords.AccountListActivity.account;

/**
 * Created by Yvonne on 4/25/2017.
 */

public class AccountPlaceholderFrag2 extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "AccountPlaceholderFrag2";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ACCOUNT = "account";
    public static final String ARG_ACCOUNT_ROWID = "account_rowid";
    private int sectNumber;
//    Account account = new Account();
//    private int accountId = -1;
    private int rowId = -1;

    private EditText mCorpNameTextView;
    private EditText mUserNameTextView;
    private EditText mUserEmailTextView;
    private EditText mCorpWebsiteTextView;
    private EditText mNoteTextView;
    private EditText mSeqTextView;
    //    private TextView mOpenDateTextView;
    private TextView mAccountIdTextView;
    private EditText mRefIdFromTextView;
    private EditText mRefIdToTextView;
    private TextView mActvyDtTextView;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;
    private ImageButton mImgWebView;

    private long acctOpenDate = 0;
    private boolean isReadyForUpdates = false;
    private int numTries = 0;

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    private OnAccountListener mListener;
    public interface OnAccountListener {
        void onAccount2Instance();
    }


    public AccountPlaceholderFrag2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AccountPlaceholderFrag2 newInstance() {
        AccountPlaceholderFrag2 fragment = new AccountPlaceholderFrag2();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
////        args.putSerializable(ARG_ACCOUNT, account);
////        args.putInt(ARG_ACCOUNT_ROWID, rowId);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        this.sectNumber = getArguments().getInt(ARG_SECTION_NUMBER);
////        this.rowId = getArguments().getInt(ARG_ACCOUNT_ROWID);
//        Log.d(TAG, "onCreateView: sectNumber " + sectNumber);
//        Log.d(TAG, "onCreateView: sectNumber " + sectNumber);
////        Account account = (Account)getArguments().getSerializable(ARG_ACCOUNT);
////        Log.d(TAG, "onCreateView: rowid " + this.rowId);
////        this.account = account;


        View rootView;
        TextView tvPage;
//        switch (sectNumber) {
//            case 1:
//                rootView = inflater.inflate(R.layout.fragment_acct_page_1, container, false);
//                tvPage = (TextView) rootView.findViewById(R.id.section_label);
//                tvPage.setText(getPageTitle(1));
//                setupPage1(rootView);
//                break;
//            case 2:
                rootView = inflater.inflate(R.layout.fragment_acct_page_2, container, false);
                tvPage = (TextView) rootView.findViewById(R.id.section_label);
                tvPage.setText(getPageTitle(2));
                setupPage2(rootView);
        isReadyForUpdates = true;
        fillPage();
//                break;
//            default:
//                rootView = inflater.inflate(R.layout.fragment_acct_page_3, container, false);
//                tvPage = (TextView) rootView.findViewById(R.id.section_label);
//                tvPage.setText(getPageTitle(3));
//                setupPage3(rootView);
//                break;
//        }

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, sectNumber));
        return rootView;
    }

//    private void setupPage1(View view) {
//
//        mCorpNameTextView = (EditText) view.findViewById(R.id.acc_corp_name);
//        mCorpWebsiteTextView = (EditText) view.findViewById(R.id.acc_corp_website);
//        mImgWebView = (ImageButton) view.findViewById(R.id.acc_img_website);
//        mUserNameTextView = (EditText) view.findViewById(R.id.acc_user_name);
//        mUserEmailTextView = (EditText) view.findViewById(R.id.acc_user_email);
//        mSeqTextView = (EditText) view.findViewById(R.id.acc_seq);
//        mAccountIdTextView = (TextView) view.findViewById(R.id.acc_account_id);
//        mActvyDtTextView = (TextView) view.findViewById(R.id.acc_actvy_date);
//
//
//        if (account == null) {
//            Log.d(TAG, "setupPage1: account is null");
//            return;
//        }
//        Log.d(TAG, "setupPage1: corpname " + account.getCorpName());
//        mCorpNameTextView.setText(account.getCorpName());
//        mCorpWebsiteTextView.setText(account.getCorpWebsite());
//        mUserNameTextView.setText(account.getUserName());
//        mUserEmailTextView.setText(account.getUserEmail());
//
//        if (account.getSequence() == 0) {
//            mSeqTextView.setText("");
//        } else {
//            mSeqTextView.setText(String.valueOf(account.getSequence()));
//        }
//
//        mAccountIdTextView.setText("Account Id: " + String.valueOf(account.getPassportId()));
//
//        if (account.getActvyLong() == 0) {
//            mActvyDtTextView.setText("no activity date");
//        } else {
//            mActvyDtTextView.setText("ActvyDate: " + format_ymdtimehm.format(account.getActvyLong()));
//        }
//
//
//    }

    private void setupPage2(View view) {

        mDtePickOpen = (DatePicker) view.findViewById(R.id.acc_datePicker);

    }

    public void fillPage() {

//        if (!isReadyForUpdates) {
//            new CountDownTimer(10000, 1000) {
//
//                public void onTick(long millisUntilFinished) {
////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                }
//
//                public void onFinish() {
////                    mTextField.setText("done!");
//                }
//            }.start();
//
//            if (!isReadyForUpdates) {
//                Log.d(TAG, "fillPage: unable to update");
//                return;
//            }
////            mListener.onAccount2Instance();
//        }
//        Log.d(TAG, "fillPage2: corpname " + account.getCorpName());
        rowId = account.getId();
//        this.account = account;

        if (mDtePickOpen == null) {
            return;
        }
        mDtePickOpen.setMaxDate(new Date().getTime());
        mDtePickOpen.setMinDate(0);
        Date dte;
        if (account.getOpenLong() != 0) {
            dte = new Date(account.getOpenLong());
//                    Log.d(TAG, "onCreateView: db openLong " + account.getOpenLong());
        } else {
            dte = new Date();
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dte);
        lngOpenDate = c1.getTimeInMillis();
//                Log.d(TAG, "onDateChanged: DB lngOpenDate " + lngOpenDate);
        mDtePickOpen.init(c1.get(Calendar.YEAR),
                c1.get(Calendar.MONTH),
                c1.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                Log.d(TAG, "onDateChanged: clicked ");
                        Calendar c2 = Calendar.getInstance();
                        c2.set(year, monthOfYear, dayOfMonth);
                        lngOpenDate = c2.getTimeInMillis();
//                                Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
                    }
                });

    }

    public void checkUI() {
        if (mCorpNameTextView == null) {
            Log.d(TAG, "checkUI: corpnameTV is null");
        } else {
            Log.d(TAG, "checkUI: have corpnameTV");
        }
    }

    private void loadFromMap() {
        if (mDtePickOpen != null) {
            Calendar c2 = Calendar.getInstance();
            c2.set(mDtePickOpen.getYear(), mDtePickOpen.getMonth(), mDtePickOpen.getDayOfMonth());
            long lngDatePickerOpenDate = c2.getTimeInMillis();
            acctOpenDate = lngDatePickerOpenDate;
        }
    }


    public boolean collectChgs() {

        if (mDtePickOpen == null) {
            return false;
        }

        boolean chgsMade = false;

//        if (this.account.getId() == account.getId()) {
            loadFromMap();

            if (acctOpenDate != account.getOpenLong()) {
                account.setOpenLong(acctOpenDate);
                chgsMade = true;
            }

            return chgsMade;
//        }
//        return false;
    }
//    private void setupPage3(View view) {
//
//        mNoteTextView = (EditText) view.findViewById(R.id.acc_notes);
//        mRefIdFromTextView = (EditText) view.findViewById(R.id.acc_ref_from);
//        mRefIdToTextView = (EditText) view.findViewById(R.id.acc_ref_to);
//
//        mNoteTextView.setText(account.getNote());
//
//        if (account.getRefFrom() == 0) {
//            mRefIdFromTextView.setText("");
//        } else {
//            mRefIdFromTextView.setText(String.valueOf(account.getRefFrom()));
//        }
//        if (account.getRefTo() == 0) {
//            mRefIdToTextView.setText("");
//        } else {
//            mRefIdToTextView.setText(String.valueOf(account.getRefTo()));
//        }
//
//    }

//    public boolean verifyAcctPages(int sectNumber) {
//        switch (sectNumber) {
//            case 1:
//                return validatePage1();
//            case 2:
//                return validatePage2();
//            case 3:
//                return validatePage3();
//        }
//        return true;
//    }

//    private boolean validatePage1() {
//        if (mCorpNameTextView.getText().toString().equals("")) {
//            mCorpNameTextView.setError("Corporation name is required");
////            Toast.makeText(getActivity(),
////                    "Corporation name is required",
////                    Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (mUserNameTextView.getText().toString().equals("")) {
//            mUserNameTextView.setError("User name is required");
//            return false;
//        }
//        if (mUserEmailTextView.getText().toString().equals("")) {
//            mUserEmailTextView.setError("User email is required");
//            return false;
//        }
//        if (!isEmailValid(mUserEmailTextView.getText().toString())) {
//            mUserEmailTextView.setError("User email is invalid format");
//            return false;
//        }
//
//        return true;
//    }

    public boolean validatePageErrors() {
        return false;
    }

//    private boolean validatePage3() {
//        return true;
//    }


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


//    public int getSection() {
//        return sectNumber;
//    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return "Page 1 of 3";
            case 2:
                return "Page 2 of 3";
            case 3:
                return "Page 3 of 3";
        }
        return null;
    }
    public int getNumTries() {
        return numTries;
    }

    public void setNumTries(int numTries) {
        this.numTries = numTries;
    }

    @Override
    public void onAttach(Context context) {
//        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof AccountPlaceholderFrag2.OnAccountListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditActivityFragment interface");
        }
        mListener = (AccountPlaceholderFrag2.OnAccountListener) activity;
    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }
}
