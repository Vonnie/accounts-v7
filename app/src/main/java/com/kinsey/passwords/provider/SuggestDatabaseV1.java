package com.kinsey.passwords.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kinsey.passwords.items.SuggestsContract;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class SuggestDatabaseV1 extends SQLiteOpenHelper {
    private static final String TAG = "SuggestDatabaseV1";

    public static final String DATABASE_NAME = "PasswordBook";
    public static final int DATABASE_VERSION = 1;

    // Implement AppDatabase as a Singleton
    private static SuggestDatabaseV1 instance = null;

    private SuggestDatabaseV1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        Log.d(TAG, "SuggestDatabaseV1: constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     * @param context the content providers context.
     * @return a SQLite database helper object
     */
    public static SuggestDatabaseV1 getInstance(Context context) {
        if (instance == null) {
//            Log.d(TAG, "getInstance: creating new instance");
            instance = new SuggestDatabaseV1(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + SuggestsContract.TABLE_NAME);
        String SQL_CREATE_PASSWORD = "create table "
                + SuggestsContract.TABLE_NAME + " ("
                + SuggestsContract.Columns._ID_COL + " integer primary key autoincrement, "
                + SuggestsContract.Columns.PASSWORD_COL + " TEXT NOT NULL, "
                + SuggestsContract.Columns.ACTVY_DATE_COL + " datetime, "
                + SuggestsContract.Columns.SEQUENCE_COL + " integer, "
                + SuggestsContract.Columns.NOTE_COL + " TEXT NOT NULL); ";
//                + " (_id integer primary key autoincrement, "
//                + "password_text text not null, " + "actvy_date datetime, "
//                + "sequence integer, " + "notes text);";

        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORD);
        Log.v(TAG, "createSQL " + SQL_CREATE_PASSWORD);
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
