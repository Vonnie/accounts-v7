package com.kinsey.passwords.provider;

import android.app.LoaderManager;
import android.app.SearchManager;
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

public class AccountSearchLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "AccountsLoaderCallbacks";

    Context mContext;

    public AccountSearchLoaderCallbacks(Context context) {
//        Log.d(TAG, "AccountsLoaderCallbacks: starts");
        mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.d(TAG, "onCreateLoader: starts id " + id);
        return new CursorLoader(
                mContext,
                AccountsContract.CONTENT_URI, null, null, null,
                AccountsContract.Columns.CORP_NAME_COL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.d(TAG, "onLoadFinished: starts data count " + data.getCount());
        try {
//            SearchesContract.listSearches.clear();
            data.moveToFirst();
            do {
                loadAccountDictionary(data);
            } while (data.moveToNext());
            data.close();

//            Cursor dict = mContext.getContentResolver().query(
//                    SearchesContract.CONTENT_URI, null, null, null, null);

            // ??????????????????????????????
//            String[] selectionArgs = {"s"};
//            Cursor dict = mContext.getContentResolver().query(
//                    Uri.withAppendedPath(SearchProvider.CONTENT_AUTHORITY_URI, SearchManager.SUGGEST_URI_PATH_QUERY),
//                    null, null, selectionArgs, null);
            // ??????????????????

//            Log.d(TAG, "onLoadFinished: dictCount " + dict.getCount());

        } catch (Exception e) {
            Log.e(TAG, "onLoadFinished() error: " + e.getMessage());
        }
    }

    public void loadAccountDictionary(Cursor data) {
//        String myUrlStr = "android.resource://com.kinsey.passwords/drawable/ic_action_user";
        ContentValues cvs = new ContentValues();
        cvs.put(SearchManager.SUGGEST_COLUMN_FORMAT, "account");
        cvs.put(SearchManager.SUGGEST_COLUMN_TEXT_1,
                data.getString(data.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)));
        cvs.put(SearchManager.SUGGEST_COLUMN_TEXT_2,
                data.getString(data.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)));
        Log.d(TAG, "loadAccountDictionary: dictCorpName "
                + data.getString(data.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL))
        );
        if (data.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL) == -1) {
            cvs.put(SearchManager.SUGGEST_COLUMN_TEXT_2_URL, "");
        } else {
            cvs.put(SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
                    data.getString(data.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)));
        }
//        cvs.put(SearchDatabase.ICON_CORP_NAME, myUrlStr);
        cvs.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                String.valueOf(data.getInt(data.getColumnIndex(AccountsContract.Columns._ID_COL))));
        cvs.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                String.valueOf(data.getInt(data.getColumnIndex(AccountsContract.Columns._ID_COL))));
        cvs.put(SearchManager.SUGGEST_COLUMN_QUERY, "");

        Log.d(TAG, "loadAccountDictionary: id "
                + data.getString(data.getColumnIndex(AccountsContract.Columns._ID_COL)));
//            Log.v(TAG, "accSuggest " + item.getCorpName() + ":" + item.getPassportId());
//        Log.d(TAG, "loadAccountDictionary: cvs " + cvs);
        mContext.getContentResolver().insert(
                SearchesContract.CONTENT_URI, cvs);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
