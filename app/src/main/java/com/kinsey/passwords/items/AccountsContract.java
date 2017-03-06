package com.kinsey.passwords.items;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.kinsey.passwords.provider.AccountDatabase;

import static com.kinsey.passwords.provider.AccountProvider.CONTENT_AUTHORITY_URI;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class AccountsContract {

    public static final String TABLE_NAME = "passport_detail";

    // Tasks fields
    public static class Columns {
        public static final String _ID_COL = BaseColumns._ID;

        public static final String PASSPORT_ID_COL = "passport_id";

        public static final String SEQUENCE_COL = "sequence";

        public static final String CORP_NAME_COL = "corporation_name";

        public static final String USER_NAME_COL = "user_name";

        public static final String USER_EMAIL_COL = "user_email";

        public static final String CORP_WEBSITE_COL = "corporation_website";

        public static final String NOTE_COL = "passport_note";

        public static final String REF_FROM_COL = "ref_from";

        public static final String REF_TO_COL = "ref_to";

        public static final String OPEN_DATE_COL = "open_date";

        public static final String ACTVY_DATE_COL = "actvy_date";

        private Columns() {
            // private constructor to prevent instantiation
        }
    }

    public static final int ACCOUNT_LIST_BY_CORP_NAME = 1;
    public static final int ACCOUNT_LIST_BY_OPEN_DATE = 2;
    public static final int ACCOUNT_LIST_BY_SEQUENCE = 3;
    public static final int ACCOUNT_EDIT = 3;
    public static final int ACCOUNT_SEARCH = 4;

    public static final int ACCOUNT_ACTION_ADD = 1;
    public static final int ACCOUNT_ACTION_CHG = 2;
    /**
     *  The URI to access the Tasks table
     */
//    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, AccountDatabase.DATABASE_NAME);
    public static final Uri CONTENT_MAX_VALUE_URI = Uri.withAppendedPath(CONTENT_URI, "maxvalue");
    public static final Uri CONTENT_ACCOUNT_ID_URI = Uri.withAppendedPath(CONTENT_URI, "accountid");
//    public static final Uri CONTENT_ROW_ID_URI = Uri.withAppendedPath(CONTENT_URI, String.valueOf(AccountProvider.ROW_ID));

//    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kinsey.passport";
//    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
//    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static Uri buildIdUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static Uri buildAcctIdUri(long id) {
        return ContentUris.withAppendedId(CONTENT_ACCOUNT_ID_URI, id);
    }

    public static long getId(Uri uri) {
        return ContentUris.parseId(uri);
    }

    public static Account getAccountFromCursor(Cursor cursor) {
        final Account account = new Account(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));

        if (cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
                account.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
//                    holder.account_id.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))));
            }
        }

        if (cursor.getColumnIndex(AccountsContract.Columns.NOTE_COL) != -1) {
            account.setNote(cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.NOTE_COL)));
        }
//            Log.d(TAG, "onBindViewHolder: dte col " + mCursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL));
        if (cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
            if (cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
            } else {
                account.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
            }
        }

        if (cursor.getColumnIndex(Columns.REF_FROM_COL) != -1) {
            if (cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL))) {
            } else {
                account.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL)));
            }
        }

        if (cursor.getColumnIndex(Columns.REF_TO_COL) != -1) {
            if (cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL))) {
            } else {
                account.setRefTo(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL)));
            }
        }

        return account;
    }
}
