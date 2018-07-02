package com.kinsey.passwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    private enum FragmentEditMode { EDIT, ADD }
    private FragmentEditMode mMode;

    private EditText mCorpNameTextView;
    private EditText mCorpWebsiteTextView;
    private EditText mUserNameTextView;
    private EditText mUserEmailTextView;
    private EditText mNoteTextView;
    private OnSaveClicked mSaveListener = null;
    private Account account;

    interface OnSaveClicked {
        void onSaveClicked();
    }

    public AddEditActivityFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
    }

    public boolean canClose() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks.
        Activity activity = getActivity();
        if(!(activity instanceof OnSaveClicked)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditActivityFragment.OnSaveClicked interface");
        }
        mSaveListener = (OnSaveClicked) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mSaveListener = null;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
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
        Button saveButton = view.findViewById(R.id.addedit_save);

        Bundle arguments = getArguments();

        if(arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details.");

            account = (Account) arguments.getSerializable(Account.class.getSimpleName());
            Log.d(TAG, "onCreateView: " + account);
            if(account != null) {
                Log.d(TAG, "onCreateView: Task details found, editing...");
                mCorpNameTextView.setText(account.getCorpName());
                mCorpWebsiteTextView.setText(account.getCorpWebsite());
                mUserNameTextView.setText(account.getUserName());
                mUserEmailTextView.setText(account.getUserEmail());
                mNoteTextView.setText(account.getNote());
                mMode = FragmentEditMode.EDIT;
            } else {
                // No task, so we must be adding a new task, and not editing an  existing one
                mMode = FragmentEditMode.ADD;
            }
        } else {
            account = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mMode = FragmentEditMode.ADD;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the database if at least one field has changed.
                // - There's no need to hit the database unless this has happened.
//                int so;     // to save repeated conversions to int.
//                if(mSortOrderTextView.length()>0) {
//                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
//                } else {
//                    so = 0;
//                }

                Log.d(TAG, "onClick: ");

                if (!verifiedAccount()) {
                    return;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if(account == null) {
                            // remove lint warnings, will never execute
                            break;
                        }
                        Log.d(TAG, "onClick: " + account);
                        if(!mCorpNameTextView.getText().toString().equals(account.getCorpName())) {
                            values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                        }
                        if(!mCorpWebsiteTextView.getText().toString().equals(account.getCorpWebsite())) {
                            values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                        }
                        if(!mUserNameTextView.getText().toString().equals(account.getUserName())) {
                            values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                        }
                        if(!mUserEmailTextView.getText().toString().equals(account.getUserEmail())) {
                            values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
                        }
                        if(!mNoteTextView.getText().toString().equals(account.getNote())) {
                            values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());
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
                            contentResolver.update(AccountsContract.buildIdUri(account.getId()), values, null, null);
                            account = getAccount(account.getId());
                            Toast.makeText(getActivity(),
                                    values.size() - 1 + " changed columns",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "no changes detected",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ADD:
                        if(mCorpNameTextView.length()>0) {
                            Log.d(TAG, "onClick: adding new task");
                            values.put(AccountsContract.Columns.PASSPORT_ID_COL,
                                    String.valueOf(getMaxValue(AccountsContract.Columns.PASSPORT_ID_COL)));
                            values.put(AccountsContract.Columns.CORP_NAME_COL, mCorpNameTextView.getText().toString());
                            values.put(AccountsContract.Columns.CORP_WEBSITE_COL, mCorpWebsiteTextView.getText().toString());
                            values.put(AccountsContract.Columns.USER_NAME_COL, mUserNameTextView.getText().toString());
                            values.put(AccountsContract.Columns.USER_EMAIL_COL, mUserEmailTextView.getText().toString());
                            values.put(AccountsContract.Columns.NOTE_COL, mNoteTextView.getText().toString());
                            Uri uri = contentResolver.insert(AccountsContract.CONTENT_URI, values);
                            long id = AccountsContract.getId(uri);
                            Log.d(TAG, "onClick: " + id);
                            account = getAccount((int)id);
                            Log.d(TAG, "onClick: " + account);
                            Toast.makeText(getActivity(),
                                    "New account added",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done editing");

                if(mSaveListener != null) {
                    mSaveListener.onSaveClicked();
                }
            }
        });
        Log.d(TAG, "onCreateView: Exiting...");

        return view;
    }

    private Account getAccount(int id) {
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
//            Toast.makeText(getActivity(),
//                    "Corporation name is required",
//                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mCorpWebsiteTextView.getText().toString().equals("")) {
            if (!mCorpWebsiteTextView.getText().toString().toLowerCase().startsWith("http://")) {
                mCorpWebsiteTextView.setText("http://" + mCorpWebsiteTextView.getText().toString());
            }
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
}
