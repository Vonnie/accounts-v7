package com.kinsey.passwords.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.kinsey.passwords.items.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchProvider extends ContentProvider {
    private static final String TAG = "SearchProvider";

    public static List<SearchItem> listSearches = new ArrayList<SearchItem>();


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
