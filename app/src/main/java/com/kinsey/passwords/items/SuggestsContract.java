package com.kinsey.passwords.items;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.kinsey.passwords.provider.SuggestDatabase;
import com.kinsey.passwords.provider.SuggestProvider;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class SuggestsContract {

    public static final String TABLE_NAME = "password_item";

    public static class Columns {

        public static final String _ID_COL = BaseColumns._ID;

        public static final String PASSWORD_COL = "password_text";

        public static final String ACTVY_DATE_COL = "actvy_date";

        public static final String SEQUENCE_COL = "sequence";

        public static final String NOTE_COL = "notes";

        private Columns() {
            // private constructor to prevent instantiation
        }
    }

    /**
     *  The URI to access the Tasks table
     */
//    public static final Uri CONTENT_URI = Uri.withAppendedPath(SuggestProvider.CONTENT_AUTHORITY_URI, TABLE_NAME);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(SuggestProvider.CONTENT_AUTHORITY_URI, SuggestDatabase.DATABASE_NAME);

    //    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kinsey.passport";

//    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
//    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;


    public static Uri buildIdUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


    public static long getId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
