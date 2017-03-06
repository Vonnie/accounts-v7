package com.kinsey.passwords.items;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import com.kinsey.passwords.provider.SearchDatabase;
import com.kinsey.passwords.provider.SearchProvider;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchesContract {

    public static Cursor cursorSearch;

//    public static final class Columns {
//
//        public static final String _ID_COL = "_id";
//
//        public static final String SUGGEST_ID_COL = "suggest_id";
//
//        public static final String SUGGEST_FLAGS_COL = "suggest_flags";
//        public static final String SUGGEST_TEXT_1_COL = "suggest_text_1";
//        public static final String SUGGEST_TEXT_2_COL = "suggest_text_2";
//        public static final String SUGGEST_INTENT_DATA_ID_COL = "suggest_intent_data_id";
//        public static final String SUGGEST_ALT_DB_ID_COL = "suggest_alt_db_id";
//
//        private Columns() {
//        }
//    }


//    public static List<SearchItem> listSearches = new ArrayList<SearchItem>();

    /**
     *  The URI to access the Tasks table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(SearchProvider.CONTENT_AUTHORITY_URI, SearchDatabase.DATABASE_NAME);
    public static final Uri CONTENT_URI_TRUNCATE = Uri.withAppendedPath(CONTENT_URI, "truncate");

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
