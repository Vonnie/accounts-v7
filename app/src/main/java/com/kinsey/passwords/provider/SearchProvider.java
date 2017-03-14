package com.kinsey.passwords.provider;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchProvider extends SearchRecentSuggestionsProvider {
    private static final String TAG = "SearchProvider";

    private SearchDatabase mDictionary;

    public final static String AUTHORITY = "com.kinsey.passwords.provider.SearchProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SearchDatabase.DATABASE_NAME);

    // MIME types used for searching words or looking up a single definition
    public static final String WORDS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.kinsey.passwords";
    public static final String DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.kinsey.passwords";


    // UriMatcher stuff
    private static final int SEARCH_WORDS = 0;
    private static final int GET_WORD = 1;
    private static final int TRUNCATE = 2;
    private static final int LOADCONTACT = 3;
    private static final int SEARCH_SUGGEST = 4;
    private static final int REFRESH_SHORTCUT = 6;

//    private static final int DELETE_SUGGEST_ROWS = 5;

    private static final int CONTACTCOUNT = 7;
    private static final UriMatcher sURIMatcher = buildUriMatcher();


//    public static final Uri CONTENT_URI_TRUNCATE = Uri.parse("content://"
//            + AUTHORITY + "/" + SearchDatabase.DATABASE_NAME + "/truncate");
//    public static final Uri CONTENT_URI_DELETE_SUGGEST_ROWS = Uri.parse("content://"
//            + AUTHORITY + "/" + SearchDatabase.DATABASE_NAME + "/delete");
//    public static final Uri CONTENT_URI_LOAD_CONTACTS = Uri.parse("content://"
//            + AUTHORITY + "/" + SearchDatabase.DATABASE_NAME + "/contact");
//    public static final Uri CONTENT_URI_GET_SUGGESTIONS = Uri.parse("content://"
//            + AUTHORITY + "/" + SearchDatabase.DATABASE_NAME + "/#");
////    public static final Uri CONTENT_URI_GET_SUGGESTIONS = Uri.parse("content://"
////            + AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/*");
//
////    public static List<SuggestItem> listContacts = new ArrayList<SuggestItem>();

    /**
     * Builds up a UriMatcher for search suggestion and shortcut refresh queries.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions...
        matcher.addURI(AUTHORITY, SearchDatabase.DATABASE_NAME, SEARCH_WORDS);
        matcher.addURI(AUTHORITY, SearchDatabase.DATABASE_NAME + "/#", GET_WORD);
        matcher.addURI(AUTHORITY, SearchDatabase.DATABASE_NAME + "/truncate", TRUNCATE);
        matcher.addURI(AUTHORITY, SearchDatabase.DATABASE_NAME + "/contact", LOADCONTACT);
        // to get suggestions...
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
//        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
//        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        /* The following are unused in this implementation, but if we include
         * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
         * could expect to receive refresh queries when a shortcutted suggestion is displayed in
         * Quick Search Box, in which case, the following Uris would be provided and we
         * would return a cursor with a single item representing the refreshed suggestion data.
         */
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
        return matcher;
    }


    public SearchProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate() {
        mDictionary = new SearchDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        Log.d(TAG, "query: starts");
//        if (selection != null) {
//            Log.d(TAG, "query " + selection + selectionArgs[0]);
//        }

        // Use the UriMatcher to see what kind of query we have and format the db query accordingly
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
//                if (selectionArgs == null) {
//                    throw new IllegalArgumentException(
//                            "selectionArgs must be provided for the Uri: " + uri);
//                }
//                Log.v(TAG, "getSuggest " + selectionArgs[0]);
                return getSuggestions(selectionArgs[0]);
            case SEARCH_WORDS:
                if (selectionArgs == null) {
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the Uri: " + uri);
                }
                return search(selectionArgs[0]);
            case GET_WORD:
                return getWord(uri);
            case REFRESH_SHORTCUT:
                return refreshShortcut(uri);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    private Cursor getSuggestions(String query) {
//        Log.v(TAG, "getSuggestions " + query);
        try {
            query = query.toLowerCase();
            String[] columns = new String[]{
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_TEXT_2,
                    SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                    SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
       /* SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                        (only if you want to refresh shortcuts) */
                    SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                    SearchManager.SUGGEST_COLUMN_QUERY,
                    SearchManager.SUGGEST_COLUMN_FLAGS};

//            Log.v(TAG, "getSuggestions " + query);

            return mDictionary.getWordMatches(query, columns);
        } catch (Exception e) {
            Log.e(TAG, "getSuggestions error " + e.getMessage());
            return null;
        }
    }


    private Cursor search(String query) {
//        Log.v(TAG, "search " + query);
        query = query.toLowerCase();
        String[] columns = new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                SearchManager.SUGGEST_COLUMN_QUERY,
                SearchManager.SUGGEST_COLUMN_FLAGS};

//        for (String item : columns) {
//            Log.v(TAG, "col " + item);
//        }

        return mDictionary.getWordMatches(query, columns);
    }

    private Cursor getWord(Uri uri) {
//        Log.v(TAG, "getWord " + uri);
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                SearchManager.SUGGEST_COLUMN_QUERY,
                SearchManager.SUGGEST_COLUMN_FLAGS};
//        String[] columns = new String[]{
//                SearchDatabase.KEY_CORP_NAME,
//                SearchDatabase.CORP_NAME_TEXT_2,
//                SearchDatabase.ID_CORP_NAME,
//                SearchDatabase.ICON_CORP_NAME,
//                SearchDatabase.SEARCH_DB_ID,
//                SearchDatabase.LOOKUP_KEY
//        };

        return mDictionary.getWord(rowId, columns);

    }

    private Cursor refreshShortcut(Uri uri) {
      /* This won't be called with the current implementation, but if we include
       * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
       * could expect to receive refresh queries when a shortcutted suggestion is displayed in
       * Quick Search Box. In which case, this method will query the table for the specific
       * word, using the given item Uri and provide all the columns originally provided with the
       * suggestion query.
       */
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                SearchManager.SUGGEST_COLUMN_QUERY,
                SearchManager.SUGGEST_COLUMN_FLAGS};
//        String[] columns = new String[]{
//                BaseColumns._ID,
//                SearchDatabase.KEY_CORP_NAME,
//                SearchDatabase.CORP_NAME_TEXT_2,
//                SearchDatabase.ID_CORP_NAME,
//                SearchDatabase.ICON_CORP_NAME,
//                SearchDatabase.SEARCH_DB_ID,
//                SearchDatabase.LOOKUP_KEY,
//                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
//                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

        return mDictionary.getWord(rowId, columns);

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_WORDS:
                return WORDS_MIME_TYPE;
            case GET_WORD:
                return DEFINITION_MIME_TYPE;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //        throw new UnsupportedOperationException();
//        values.get(AccountItem.)
        // ---add a new stats---
//        long rowID = mDictionary.addWord(item);

        long rowID = mDictionary.addSuggestion(values);

//        Log.d(TAG, "insert: rowId " + rowID);
//        Log.d(TAG, "insert: values " + values);
        // ---if added successfully---
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        Log.d(TAG, "delete: uri " + uri);
//        Log.d(TAG, "delete: match " + sURIMatcher.match(uri));
        int count;

        switch (sURIMatcher.match(uri)) {
            case SEARCH_WORDS:
                mDictionary.delete(selection, selectionArgs);
                break;
            case TRUNCATE:
                mDictionary.deleteRows();
                break;
            default:
                Log.v(TAG, "Unknown " + uri);
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return 0;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
