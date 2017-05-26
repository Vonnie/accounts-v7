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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import static com.kinsey.passwords.AccountActivity.account;

/**
 * Created by Yvonne on 4/25/2017.
 */

public class AccountPlaceholderFrag1 extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "AccountPlaceholderFrag1";
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_ACCOUNT = "account";
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

    private String acctCorpName = "";
    private String acctCorpWebsite = "";
    private String acctUserName = "";
    private String acctUserEmail = "";
    private boolean isReadyForUpdates = false;
    private int numTries = 0;

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    private OnAccountListener mListener;

    public interface OnAccountListener {
//        void onAccount1Instance();
//        void onWebsiteRequest(String website);
    }


    public AccountPlaceholderFrag1() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AccountPlaceholderFrag1 newInstance() {
        AccountPlaceholderFrag1 fragment = new AccountPlaceholderFrag1();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        args.putSerializable(ARG_ACCOUNT, account);
//        args.putInt(ARG_ACCOUNT_ROWID, rowId);
//        Log.d(TAG, "newInstance: acct " + rowId);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        this.sectNumber = getArguments().getInt(ARG_SECTION_NUMBER);
//        Account account = (Account)getArguments().getSerializable(ARG_ACCOUNT);
//        this.rowId = getArguments().getInt(ARG_ACCOUNT_ROWID);
//        Log.d(TAG, "onCreateView: sectNumber " + this.sectNumber);
//        Log.d(TAG, "onCreateView: sectNumber " + this.sectNumber);
//        Log.d(TAG, "onCreateView: rowid " + this.rowId);
//        this.account = getAccount(accountRowId);
//        Log.d(TAG, "onCreateView: acctId " + this.account);

        View rootView;
        TextView tvPage;
//        switch (sectNumber) {
//            case 1:
        rootView = inflater.inflate(R.layout.fragment_acct_page_1, container, false);
        tvPage = (TextView) rootView.findViewById(R.id.section_label);
        tvPage.setText(getPageTitle(1));
        setupPage1(rootView);
        isReadyForUpdates = true;
        fillPage();

//                break;
//            case 2:
//                rootView = inflater.inflate(R.layout.fragment_acct_page_2, container, false);
//                tvPage = (TextView) rootView.findViewById(R.id.section_label);
//                tvPage.setText(getPageTitle(2));
//                setupPage2(rootView);
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



//        mImgWebView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mListener.onWebsiteRequest(mCorpWebsiteTextView.getText().toString());
//
////                Intent detailIntent = new Intent(getActivity(), WebViewActivity.class);
////                detailIntent.putExtra(WebViewActivity.class.getSimpleName(), mCorpWebsiteTextView.getText().toString());
//////                Log.d(TAG, "onClick: website " + account.getCorpWebsite());
//////                Log.d(TAG, "onClick: wv class " + WebViewActivity.class.getSimpleName());
////                mListener.onWebpage();
////                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_WEBPAGE);
//
//            }
//        });
        return rootView;
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void setupPage1(View view) {

        mCorpNameTextView = (EditText) view.findViewById(R.id.acc_corp_name);
        mCorpWebsiteTextView = (EditText) view.findViewById(R.id.acc_corp_website);
        mImgWebView = (ImageButton) view.findViewById(R.id.acc_img_website);
        mUserNameTextView = (EditText) view.findViewById(R.id.acc_user_name);
        mUserEmailTextView = (EditText) view.findViewById(R.id.acc_user_email);
        mAccountIdTextView = (TextView) view.findViewById(R.id.acc_account_id);
        mActvyDtTextView = (TextView) view.findViewById(R.id.acc_actvy_date);
    }

    public void fillPage() {

//        if (account == null) {
//            Log.d(TAG, "setupPage1: account is null");
//            return;
//        }
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
////            mListener.onAccount1Instance();
//        }

        Log.d(TAG, "fillPage1: corpname " + AccountListActivity.account.getCorpName());
        Log.d(TAG, "fillPage1: account " + AccountListActivity.account);
        rowId = AccountListActivity.account.getId();

        if (mCorpNameTextView == null) {
            return;
        }
        mCorpNameTextView.setText(AccountListActivity.account.getCorpName());
        mCorpNameTextView.setError(null);
        mCorpWebsiteTextView.setText(AccountListActivity.account.getCorpWebsite());
        mCorpWebsiteTextView.setError(null);
        mUserNameTextView.setText(AccountListActivity.account.getUserName());
        mUserNameTextView.setError(null);
        mUserEmailTextView.setText(AccountListActivity.account.getUserEmail());
        mUserEmailTextView.setError(null);

        mAccountIdTextView.setText("Account Id: " + String.valueOf(AccountListActivity.account.getPassportId()));

        if (AccountListActivity.account.getActvyLong() == 0) {
            mActvyDtTextView.setText("no activity date");
        } else {
            mActvyDtTextView.setText("ActvyDate: " + format_ymdtimehm.format(AccountListActivity.account.getActvyLong()));
        }

    }

    private void loadFromMap() {
        acctCorpName = mCorpNameTextView.getText().toString();
        acctCorpWebsite = mCorpWebsiteTextView.getText().toString();
        acctUserName = mUserNameTextView.getText().toString();
        Log.d(TAG, "loadFromMap: username " + acctUserName);
        acctUserEmail = mUserEmailTextView.getText().toString();
    }


    public boolean collectChgs() {

        if (mCorpNameTextView == null) {
            return false;
        }

        boolean chgsMade = false;

        Log.d(TAG, "collectChgs: id " + AccountListActivity.account.getId());
        Log.d(TAG, "collectChgs: id " + rowId);
//        if (this.account.getId() == currentAccount.getId()) {

        loadFromMap();

        if (!acctCorpName.equals(AccountListActivity.account.getCorpName())) {
            AccountListActivity.account.setCorpName(acctCorpName);
            chgsMade = true;
        }

        if (!acctCorpWebsite.equals(AccountListActivity.account.getCorpWebsite())) {
            AccountListActivity.account.setCorpWebsite(acctCorpWebsite);
            chgsMade = true;
        }

//        Log.d(TAG, "collectChgs: " + acctUserName);
//        Log.d(TAG, "collectChgs: " + currentAccount.getUserName());

        if (!acctUserName.equals(AccountListActivity.account.getUserName())) {
            AccountListActivity.account.setUserName(acctUserName);
            chgsMade = true;
        }

        if (!acctUserEmail.equals(AccountListActivity.account.getUserEmail())) {
            AccountListActivity.account.setUserEmail(acctUserEmail);
            chgsMade = true;
        }

        return chgsMade;
//            return false;
//        }
//        return false;
    }

//    private void setupPage2(View view) {
//
//        mDtePickOpen = (DatePicker) view.findViewById(R.id.acc_datePicker);
//
//        mDtePickOpen.setMaxDate(new Date().getTime());
//        mDtePickOpen.setMinDate(0);
//        Date dte;
//        if (account.getOpenLong() != 0) {
//            dte = new Date(account.getOpenLong());
////                    Log.d(TAG, "onCreateView: db openLong " + account.getOpenLong());
//        } else {
//            dte = new Date();
//        }
//        Calendar c1 = Calendar.getInstance();
//        c1.setTime(dte);
//        lngOpenDate = c1.getTimeInMillis();
////                Log.d(TAG, "onDateChanged: DB lngOpenDate " + lngOpenDate);
//        mDtePickOpen.init(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH),
//                c1.get(Calendar.DAY_OF_MONTH),
//                new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                                Log.d(TAG, "onDateChanged: clicked ");
//                        Calendar c2 = Calendar.getInstance();
//                        c2.set(year, monthOfYear, dayOfMonth);
//                        lngOpenDate = c2.getTimeInMillis();
////                                Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
//                    }
//                });
//
//
//    }
//
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


    public void checkUI() {
        if (mCorpNameTextView == null) {
            Log.d(TAG, "checkUI: corpnameTV is null");
        } else {
            Log.d(TAG, "checkUI: have corpnameTV");
        }
    }
    public boolean validatePageErrors() {

        boolean hasErrors = false;
        if (mCorpNameTextView.getText().toString().equals("")) {
            mCorpNameTextView.setError("Corporation name is required");
            mCorpNameTextView.requestFocus();
//            Toast.makeText(getActivity(),
//                    "Corporation name is required",
//                    Toast.LENGTH_LONG).show();
            hasErrors = true;
        }

        if (!mCorpWebsiteTextView.getText().toString().equals("")
        && !mCorpWebsiteTextView.getText().toString().toLowerCase().startsWith("http://")) {
            mCorpWebsiteTextView.setText("http://" + mCorpWebsiteTextView.getText().toString());
        }

        if (mUserNameTextView.getText().toString().equals("")) {
            mUserNameTextView.setError("User name is required");
            if (!hasErrors) {
                mUserNameTextView.requestFocus();
            }
            hasErrors = true;
        }
        if (mUserEmailTextView.getText().toString().equals("")) {
            mUserEmailTextView.setError("Email is required");
            if (!hasErrors) {
                mUserEmailTextView.requestFocus();
            }
            hasErrors = true;
        } else if (!isEmailValid(mUserEmailTextView.getText().toString())) {
            mUserEmailTextView.setError("Email is invalid format");
            if (!hasErrors) {
                mUserEmailTextView.requestFocus();
            }
            hasErrors = true;
        }

        return hasErrors;
    }

//    public Account collectPage(Account account) {
//
//        account.setCorpName(mCorpNameTextView.getText().toString());
//        account.setCorpWebsite(mCorpWebsiteTextView.getText().toString());
//        account.setUserName(mUserNameTextView.getText().toString());
//        account.setUserEmail(mUserEmailTextView.getText().toString());
//        return account;
//    }

    private boolean validatePage2() {
        return true;
    }

    private boolean validatePage3() {
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

//    private Account getAccount(int id) {
//        int iId = 0;
//        Cursor cursor = getActivity().getContentResolver()
//                .query(AccountsContract.buildIdUri(id), null, null, null, null);
//        if (cursor == null) {
//            return null;
//        } else {
//            if (cursor.moveToFirst()) {
//                Account account = AccountsContract.getAccountFromCursor(cursor);
//                cursor.close();
//                return account;
//            }
//            cursor.close();
//            return null;
//        }
//    }


//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }


    public void setCorpNameFocus() {
        mCorpNameTextView.requestFocus();
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
        if (!(activity instanceof AccountPlaceholderFrag1.OnAccountListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AccountPlaceholderFrag1 interface");
        }
        mListener = (AccountPlaceholderFrag1.OnAccountListener) activity;
    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
