package com.kinsey.passwords.provider;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SearchesContract;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class AccountSuggestsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "AccountsLoaderCallbacks";

    Context mContext;

    public AccountSuggestsLoaderCallbacks(Context context) {
        Log.d(TAG, "AccountsLoaderCallbacks: starts");
        mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts id " + id);
        return new CursorLoader(
                mContext,
                AccountsContract.CONTENT_URI, null, null, null,
                AccountsContract.Columns.CORP_NAME_COL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts data count " + data.getCount());
        try {
            SearchesContract.listSearches.clear();
            data.moveToFirst();
            do {
                loadAccountDictionary(data);
            } while (data.moveToNext());
            data.close();
        } catch (Exception e) {
            Log.e(TAG, "onLoadFinished() error: " + e.getMessage());
        }
    }

    private void loadAccountDictionary(Cursor data) {
//        String myUrlStr = "android.resource://com.kinsey.passwords/drawable/ic_action_user";
        ContentValues cvs = new ContentValues();
        cvs.put(SearchDatabase.KEY_CORP_NAME,
                data.getString(data.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)));
        cvs.put(SearchDatabase.ID_CORP_NAME, "account");
        cvs.put(SearchDatabase.CORP_NAME_TEXT_2,
                data.getString(data.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)));
//        cvs.put(SearchDatabase.ICON_CORP_NAME, myUrlStr);
        cvs.put(SearchDatabase.SEARCH_DB_ID,
                data.getString(data.getColumnIndex(AccountsContract.Columns._ID_COL)));
        cvs.put(SearchDatabase.LOOKUP_KEY, "");

//            Log.v(TAG, "accSuggest " + item.getCorpName() + ":" + item.getPassportId());
        mContext.getContentResolver().insert(
                SearchesContract.CONTENT_URI, cvs);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
