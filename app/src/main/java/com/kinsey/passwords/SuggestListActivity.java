package com.kinsey.passwords;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.items.SuggestsContract;
import com.kinsey.passwords.provider.CursorRecyclerViewAdapter;
import com.kinsey.passwords.tools.AppDialog;
import com.kinsey.passwords.tools.PasswordFormula;

import java.util.ArrayList;
import java.util.List;

public class SuggestListActivity extends AppCompatActivity
        implements CursorRecyclerViewAdapter.OnSuggestClickListener,
        AppDialog.DialogEvents {
    private static final String TAG = "SuggestListActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;
    private PasswordFormula passwordFormula = new PasswordFormula();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a generated password", Snackbar.LENGTH_LONG)
                        .setAction("Add",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        requestPassword();
//                                        generatePasswords();
                                    }
                                }

                        ).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void generatePasswords(int passwordLen) {
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();

        int iSeq = getMaxValue(SuggestsContract.Columns.SEQUENCE_COL);
        Log.d(TAG, "generatePasswords: " + iSeq);
//        strUUID = java.util.UUID.randomUUID().toString();
//        createPassword(++iSeq);

        values.put(SuggestsContract.Columns.PASSWORD_COL, passwordFormula.createPassword(passwordLen));
        values.put(SuggestsContract.Columns.SEQUENCE_COL, ++iSeq);
        contentResolver.insert(SuggestsContract.CONTENT_URI, values);
        Toast.makeText(SuggestListActivity.this,
                "Password generated",
                Toast.LENGTH_SHORT).show();
    }

    private int getMaxValue(String col) {
        int iId = 0;
        Cursor cursor = getContentResolver().query(
                SuggestsContract.CONTENT_MAX_VALUE_URI, null, null, null, col);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int iIndex = cursor.getColumnIndex(col);
                iId = cursor.getInt(iIndex);
                Log.d(TAG, "getMaxValue: " + iId);
            }
            cursor.close();
        }

        return iId;
    }

    private void requestPassword() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_REQUEST_GEN_PASSWORD_LENGTH);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, "Password at 8 or 10 length");
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, "choose from buttons");
//        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, 1, account.getCorpName()));
//        args.putInt(AppDialog.DIALOG_ACCOUNT_ID, account.getId());
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.suggdiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.suggdiag_negative_caption);

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }

    List<Suggest> loadPasswords() {
        Log.d(TAG, "loadPasswords: starts ");
        Cursor cursor = getContentResolver().query(
                SuggestsContract.CONTENT_URI, null, null, null, SuggestsContract.Columns.SEQUENCE_COL);

        List<Suggest> listSuggests = new ArrayList<Suggest>();
        if (cursor != null) {
            while(cursor.moveToNext()) {
                Log.d(TAG, "loadPasswords: seq " + cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL))
                        + ":" + cursor.getString(cursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)));
                Suggest item = new Suggest(
                        cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns._ID_COL)),
                        cursor.getString(cursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)),
                        cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));
                item.setNewSequence(cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));
                listSuggests.add(item);
            }
            cursor.close();
        }

        return listSuggests;
    }


    @Override
    public void onSuggestDeleteClick(Suggest suggest) {
        getContentResolver().delete(SuggestsContract.buildIdUri(suggest.getId()), null, null);
    }

    @Override
    public void onSuggestDownClick(Suggest suggest) {
        List<Suggest> listSuggests = loadPasswords();
        int nextId = -1;
        int iLimit = listSuggests.size();
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (nextId != -1) {
                nextId = item.getId();
                break;
            }
            if (suggest.getId() == item.getId()) {
                nextId = item.getId();
            }
        }
        Log.d(TAG, "onSuggestDownClick: nextId " + nextId);

        int reseq = 0;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() != item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
                if (nextId == item.getId()) {
                    break;
                }
            }
        }

        boolean found = false;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() == item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
            } else {
                if (nextId == item.getId()) {
                    found = true;
                } else {
                    if (found) {
                        reseq++;
                        item.setNewSequence(reseq);
                    }
                }
            }
        }

        ContentResolver contentResolver = getContentResolver();

        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (item.getSequence() != item.getNewSequence()) {
                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
                ContentValues values = new ContentValues();
                values.put(SuggestsContract.Columns.SEQUENCE_COL, item.getNewSequence());
                contentResolver.update(SuggestsContract.buildIdUri(item.getId()), values, null, null);
            }
        }
    }

    @Override
    public void onSuggestUpClick(Suggest suggest) {
        List<Suggest> listSuggests = loadPasswords();
        int priorId = -1;
        int iLimit = listSuggests.size();
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() == item.getId()) {
                break;
            }
            priorId = item.getId();
        }
        Log.d(TAG, "onSuggestDownClick: priorId " + priorId);

        int reseq = 0;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (priorId != item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
                if (suggest.getId() == item.getId()) {
                    break;
                }
            }
        }

        boolean found = false;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (priorId == item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
            } else {
                if (suggest.getId() == item.getId()) {
                    found = true;
                } else {
                    if (found) {
                        reseq++;
                        item.setNewSequence(reseq);
                    }
                }
            }
        }

        ContentResolver contentResolver = getContentResolver();

        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (item.getSequence() != item.getNewSequence()) {
                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
                ContentValues values = new ContentValues();
                values.put(SuggestsContract.Columns.SEQUENCE_COL, item.getNewSequence());
                contentResolver.update(SuggestsContract.buildIdUri(item.getId()), values, null, null);
            }
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        if (dialogId == AppDialog.DIALOG_ID_REQUEST_GEN_PASSWORD_LENGTH) {
            generatePasswords(10);
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        if (dialogId == AppDialog.DIALOG_ID_REQUEST_GEN_PASSWORD_LENGTH) {
            generatePasswords(8);
        }
    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }
}
