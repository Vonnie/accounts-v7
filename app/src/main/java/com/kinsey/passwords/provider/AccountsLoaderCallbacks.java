package com.kinsey.passwords.provider;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.kinsey.passwords.items.AccountsContract;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class AccountsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "AccountsLoaderCallbacks";

    Context mContext;

    public AccountsLoaderCallbacks(Context context) {
        Log.d(TAG, "AccountsLoaderCallbacks: starts");
        mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts");
        return new CursorLoader(
                mContext,
                AccountsContract.CONTENT_URI, null, null, null,
                AccountsContract.Columns.CORP_NAME_COL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            SearchProvider.listSearches.clear();
            data.moveToFirst();
            do {

            } while (data.moveToNext());
            data.close();
        } catch (Exception e) {
            Log.e(TAG, "onLoadFinished() error: " + e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
