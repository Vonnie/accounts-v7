package com.kinsey.passwords.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SuggestsContract;

import static com.kinsey.passwords.items.SuggestsContract.Columns.SEQUENCE_COL;
import static com.kinsey.passwords.provider.SuggestDatabaseV1.DATABASE_NAME;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class SuggestProvider extends ContentProvider {
    private static final String TAG = "SuggestProvider";

    private SuggestDatabaseV1 mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final String AUTHORITY = "com.kinsey.passwords.provider.PasswordDBProvider";
    public static final String CONTENT_AUTHORITY = AUTHORITY + "/" + DATABASE_NAME;
//    public static final String AUTHORITY = "com.kinsey.passwords.provider.PasswordDBProvider";
//    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private static final int BOOK = 1;
    private static final int ROW_ID = 2;
    private static final int MAX_SEQUENCE_VALUE = 3;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, SuggestDatabaseV1.DATABASE_NAME, BOOK);
        matcher.addURI(AUTHORITY, SuggestDatabaseV1.DATABASE_NAME + "/#", ROW_ID);
        matcher.addURI(AUTHORITY, SuggestDatabaseV1.DATABASE_NAME + "/maxvalue", MAX_SEQUENCE_VALUE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
//        Log.d(TAG, "onCreate: starts");
        mOpenHelper = SuggestDatabaseV1.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
//        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
//        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case ROW_ID:
//                queryBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
//                selection = SuggestsContract.Columns._ID_COL;
//                selectionArgs[0] = uri.getPathSegments().get(1);
                long suggestId = SuggestsContract.getId(uri);
                queryBuilder.appendWhere(SuggestsContract.Columns._ID_COL + " = " + suggestId);
                break;
            case MAX_SEQUENCE_VALUE:
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
//                sortOrder = sortOrder + " DESC";
//                sortOrder = SuggestsContract.Columns.SEQUENCE_COL + " DESC";
                sortOrder = sortOrder + " DESC";
//                c = queryBuilder.query(mOpenHelper, null, selection,
//                        selectionArgs, null, null, sortOrder, "1");
                break;
            default:
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
                if (sortOrder == null || sortOrder == "")
                    sortOrder = SEQUENCE_COL;
                sortOrder = sortOrder + " ASC";

//                c = queryBuilder.query(passwordDB, projection, selection,
//                        selectionArgs, null, null, sortOrder);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

//        Log.d(TAG, "query: cursor " + cursor.getCount());


//        Log.d(TAG, "query: called with URI " + uri);
//        Log.d(TAG, "query: called with context " + getContext().getContentResolver());
        // ---register to watch a content URI for changes---
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
//        Log.d(TAG, "Entering insert, called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
//        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch(match) {
            case BOOK:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(SuggestsContract.TABLE_NAME, null, contentValues);
                if(recordId >=0) {
                    returnUri = SuggestsContract.buildIdUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (recordId >= 0) {
            // something was inserted
//            Log.d(TAG, "insert: Setting notifyChanged with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
//            Log.d(TAG, "insert: nothing inserted");
        }
//        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
//        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case BOOK:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(SuggestsContract.TABLE_NAME, selection, selectionArgs);
                break;

            case ROW_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = AccountsContract.getId(uri);
                selectionCriteria = SuggestsContract.Columns._ID_COL + " = " + taskId;

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(SuggestsContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            // something was deleted
//            Log.d(TAG, "delete: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
//            Log.d(TAG, "delete: nothing deleted");
        }

//        Log.d(TAG, "Exiting delete, returning " + count);
        return count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
//        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
//        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case BOOK:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(SuggestsContract.TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case ROW_ID:
                db = mOpenHelper.getWritableDatabase();
                long suggestId = SuggestsContract.getId(uri);
                selectionCriteria = SuggestsContract.Columns._ID_COL + " = " + suggestId;
//                Log.d(TAG, "update: selectionCriteria " + selectionCriteria);
//                Log.d(TAG, "update: contentValues " + contentValues);

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(SuggestsContract.TABLE_NAME, contentValues, selectionCriteria, selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            // something was deleted
//            Log.d(TAG, "updatead: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
//            Log.d(TAG, "update: nothing updated");
        }

//        Log.d(TAG, "Exiting update, returning " + count);
        return count;

    }
}


//ls -alh
//
//        sqlite3 Passport
//        sqlite3 PasswordBook
//
//
