package com.kinsey.passwords.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SuggestsContract;

import static com.kinsey.passwords.items.SuggestsContract.Columns.SEQUENCE_COL;
import static com.kinsey.passwords.provider.SuggestDatabase.DATABASE_NAME;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class SuggestProvider extends ContentProvider {
    private static final String TAG = "SuggestProvider";

    private SuggestDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final String AUTHORITY = "com.kinsey.passwords.provider.PasswordDBProvider";
    public static final String CONTENT_AUTHORITY = AUTHORITY + "/" + DATABASE_NAME;
//    public static final String AUTHORITY = "com.kinsey.passwords.provider.PasswordDBProvider";
//    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private static final int BOOK = 1;
    private static final int ROW_ID = 2;
    private static final int MAX_VALUE = 3;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, SuggestDatabase.DATABASE_NAME, BOOK);
        matcher.addURI(AUTHORITY, SuggestDatabase.DATABASE_NAME + "/#", ROW_ID);
        matcher.addURI(AUTHORITY, SuggestDatabase.DATABASE_NAME + "/maxvalue", MAX_VALUE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: starts");
        mOpenHelper = SuggestDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case ROW_ID:
//                queryBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
//                selection = SuggestsContract.Columns._ID_COL;
//                selectionArgs[0] = uri.getPathSegments().get(1);
                long taskId = SuggestsContract.getId(uri);
                queryBuilder.appendWhere(SuggestsContract.Columns._ID_COL + " = " + taskId);
                break;
            case MAX_VALUE:
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
//                sortOrder = sortOrder + " DESC";
                sortOrder = SuggestsContract.Columns._ID_COL + " DESC";
//                c = queryBuilder.query(mOpenHelper, null, selection,
//                        selectionArgs, null, null, sortOrder, "1");
                break;
            default:
                queryBuilder.setTables(SuggestsContract.TABLE_NAME);
                if (sortOrder == null || sortOrder == "")
                    sortOrder = SEQUENCE_COL + " ASC";

//                c = queryBuilder.query(passwordDB, projection, selection,
//                        selectionArgs, null, null, sortOrder);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        Log.d(TAG, "query: cursor " + cursor.getCount());


        Log.d(TAG, "query: called with URI " + uri);
        Log.d(TAG, "query: called with context " + getContext().getContentResolver());
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
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

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
            Log.d(TAG, "delete: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "delete: nothing deleted");
        }

        Log.d(TAG, "Exiting delete, returning " + count);
        return count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}


//ls -alh
//
//        sqlite3 Passport.db
//        sqlite3 PasswordBook.db
//
//
//        INSERT INTO password_item
//        (sequence, password_text, notes) VALUES(1,'aaa',''),
//        (password_text) VALUES('bbb'),
//        (password_text) VALUES('ccc');
//
//        INSERT INTO passport_detail
//        (passport_id, sequence, corporation_name, user_name, user_email)
//        VALUES(1, 1, "My Business", "Vonnie", "vonniekinsey@gmail.com");





//generic_x86:/data/data/com.kinsey.passwords/databases # sqlite3 Passport
//        SQLite version 3.9.2 2015-11-02 18:31:45
//        Enter ".help" for usage hints.
//        sqlite> .schema
//        CREATE TABLE android_metadata (locale TEXT);
//        CREATE TABLE passport_detail (_id integer primary key autoincrement, passport_id integer, seque
//        nce integer, open_date long, actvy_date long, corporation_name text not null, user_name text no
//        t null, user_email text not null, corporation_website text not null, ref_from integer, ref_to i
//        nteger, passport_note text not null);
//        =====================================================================
//        createSQL create table passport_detail (_id integer primary key autoincrement, passport_id integer, sequence integer, open_date long, actvy_date long, corporation_name TEXT NOT NULL, user_name TEXT NOT NULL, user_email TEXT NOT NULL, corporation_website TEXT NOT NULL, ref_from INTEGER, ref_to INTEGER, passport_note TEXT NOT NULL);
//        ==========================================================================
//        sqlite> .tables
//        android_metadata  passport_detail
//        sqlite> SELECT * FROM passport_detail;
//        1|1|1|1487515159961|1487515159961|Abc Corp|vonniekinsey|vonniekinsey@gmail.com|http://abc.com|0
//        |0|Djdjdjdjdjdjjdjdjskskksksksk
//        2|2|2|1487515231573|1487515231573|Xyz Corp|vonniekinsey|vonniekinsey@gmail.com|http://xyz.com|0
//        |0|Nxjsjdkjls
//        sqlite> .header on
//        sqlite> select * from passport_detail;
//        _id|passport_id|sequence|open_date|actvy_date|corporation_name|user_name|user_email|corporation
//        _website|ref_from|ref_to|passport_note
//        1|1|1|1487515159961|1487515159961|Abc Corp|vonniekinsey|vonniekinsey@gmail.com|http://abc.com|0
//        |0|Djdjdjdjdjdjjdjdjskskksksksk
//        2|2|2|1487515231573|1487515231573|Xyz Corp|vonniekinsey|vonniekinsey@gmail.com|http://xyz.com|0
//        |0|Nxjsjdkjls
//        sqlite> .quit
//        generic_x86:/data/data/com.kinsey.passwords/databases # sqlite3 PasswordBook
//        SQLite version 3.9.2 2015-11-02 18:31:45
//        Enter ".help" for usage hints.
//        sqlite> .schema
//        CREATE TABLE android_metadata (locale TEXT);
//        CREATE TABLE password_item (_id integer primary key autoincrement, password_text text not null,
//        actvy_date datetime, sequence integer, notes text);
//        ====================================================
//        : createSQL create table password_item (_id integer primary key autoincrement, password_text TEXT NOT NULL, aactvy_date datetime, sequence integer, notes TEXT NOT NULL);
//        ===============================================
//
//        sqlite> select * from password_item
//        ...> ;
//        1|edwZa3aarftt|2017-02-19 08:43:16.0|1|
//        2|5edrGvfffdvd|2017-02-19 08:43:16.0|2|
//        3|hby9yhhhijUo|2017-02-19 08:43:17.0|3|
//        4|dccxdz8sxdCc|2017-02-19 08:43:17.0|4|
//        5|jMm8kmmlkijm|2017-02-19 08:43:18.0|5|
//        6|df3cgtftrFfe|2017-02-19 08:43:18.0|6|
//        7|eSda3swqrtff|2017-02-19 08:43:18.0|7|
//        8|fgvGbvf2rtee|2017-02-19 08:43:19.0|8|
//        9|huy2yhhhjhKu|2017-02-19 08:43:19.0|9|
//        10|d2scqawaaWqz|2017-02-19 08:43:19.0|10|
//        11|tfgXdz7zgbvf|2017-02-19 08:43:19.0|11|
//        12|aw3wazwSgvvf|2017-02-19 08:43:20.0|12|
//        13|3Fdvswadewss|2017-02-19 08:43:20.0|13|
//        14|hbbhbj8Bklil|2017-02-19 08:43:20.0|14|
//        15|cxVzaaa9wsqq|2017-02-19 08:43:21.0|15|
//        16|frgfDgd1rftt|2017-02-19 08:43:21.0|16|
//        17|tfyes8rrwSqs|2017-02-19 08:43:22.0|17|
//        18|dc8crTefesws|2017-02-19 08:43:22.0|18|
//        19|rtt4aqqWxzdc|2017-02-19 08:43:23.0|19|
//        20|aq6sSwwxsaax|2017-02-19 08:43:23.0|20|
//        sqlite>
//
//
//        MainActivity: onCreate: suggest uri content://com.kinsey.passwords.provider.PassportDBProvider/password_item

