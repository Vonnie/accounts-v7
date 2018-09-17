package com.kinsey.passwords.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kinsey.passwords.items.AccountsContract;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class AccountDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AccountDatabase";

    public static final String DATABASE_NAME = "Passport";
//    private static final String DATABASE_TABLE_NAME = "passport_detail";
    public static final int DATABASE_VERSION = 1;

    // Implement AccountDatabase as a Singleton
    private static AccountDatabase instance = null;

    private AccountDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * Get an instance of the app's singleton database helper object
     * @param context the content providers context.
     * @return a SQLite database helper object
     */
    public static AccountDatabase getInstance(Context context) {
        if (instance == null) {
//            Log.d(TAG, "getInstance: creating new instance");
            instance = new AccountDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG, "onCreate: creating table " + AccountsContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + AccountsContract.TABLE_NAME);
        String SQL_CREATE_PASSPORT = "create table "
                + AccountsContract.TABLE_NAME
                + " ("
                + AccountsContract.Columns._ID_COL
                + " integer primary key autoincrement, "
                // +
                // "subject_id integer, notify_date datetime, actvy_date datetime, "
                + AccountsContract.Columns.PASSPORT_ID_COL + " integer, "
                + AccountsContract.Columns.SEQUENCE_COL + " integer, "
                + AccountsContract.Columns.OPEN_DATE_COL + " long, "
                + AccountsContract.Columns.ACTVY_DATE_COL + " long, "
                + AccountsContract.Columns.CORP_NAME_COL + " TEXT NOT NULL, "
                + AccountsContract.Columns.USER_NAME_COL + " TEXT NOT NULL, "
                + AccountsContract.Columns.USER_EMAIL_COL + " TEXT NOT NULL, "
                + AccountsContract.Columns.CORP_WEBSITE_COL + " TEXT NOT NULL, "
                + AccountsContract.Columns.REF_FROM_COL + " INTEGER, "
                + AccountsContract.Columns.REF_TO_COL + " INTEGER, "
                + AccountsContract.Columns.NOTE_COL + " TEXT NOT NULL); ";
//                + "passport_id integer, sequence integer, open_date long, actvy_date long, "
//                + "corporation_name text not null, "
//                + "user_name text not null, "
//                + "user_email text not null, "
//                + "corporation_website text not null, "
//                + "ref_from integer, ref_to integer, "
//                + "passport_note text not null);";

        sqLiteDatabase.execSQL(SQL_CREATE_PASSPORT);
        Log.v(TAG, "createSQL " + SQL_CREATE_PASSPORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                // upgrade logic from version
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
    }



}
