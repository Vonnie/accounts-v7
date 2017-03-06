package com.kinsey.passwords.provider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.SearchItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchDatabase {
    private static final String TAG = "SearchDatabase";

    //The columns we'll include in the dictionary table
//    public static final String KEY_CORP_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
//    public static final String CORP_NAME_TEXT_2 = SearchManager.SUGGEST_COLUMN_TEXT_2;
//    public static final String ID_CORP_NAME = SearchManager.SUGGEST_COLUMN_FLAGS;
//    public static final String ICON_CORP_NAME = SearchManager.SUGGEST_COLUMN_ICON_1;
//    public static final String SEARCH_DB_ID = "search_alt_db_id";
//    public static final String LOOKUP_KEY = ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY;

    public static final String DATABASE_NAME = "corpnamebank";
    public static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final int DATABASE_VERSION = 10;

    private DictionaryOpenHelper mDatabaseOpenHelper;
//    public static SQLiteDatabase dictionaryDB;
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    List<Account> listAccounts = new ArrayList<Account>();

    Context context;

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public SearchDatabase(Context context) {
        this.context = context;
//        if (resetDB) {
//            removeDB(4);
//        }
        mDatabaseOpenHelper = new DictionaryOpenHelper(context, DATABASE_VERSION);

//        mDatabaseOpenHelper.getWritableDatabase().setVersion(1);
    }

    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(SearchManager.SUGGEST_COLUMN_FORMAT, SearchManager.SUGGEST_COLUMN_FORMAT);
        map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_1);
        map.put(SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_TEXT_2);
        map.put(SearchManager.SUGGEST_COLUMN_TEXT_2_URL, SearchManager.SUGGEST_COLUMN_TEXT_2_URL);
//        map.put(SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_ICON_1);
//        map.put(SearchManager.SUGGEST_COLUMN_ICON_2, SearchManager.SUGGEST_COLUMN_ICON_2);
//        map.put(SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE, SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE);
//        map.put(SearchManager.SUGGEST_COLUMN_INTENT_ACTION, SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA, SearchManager.SUGGEST_COLUMN_INTENT_DATA);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
        map.put(SearchManager.SUGGEST_COLUMN_QUERY, SearchManager.SUGGEST_COLUMN_QUERY);
        map.put(SearchManager.SUGGEST_COLUMN_FLAGS, SearchManager.SUGGEST_COLUMN_FLAGS);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }


    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getWord(String rowId, String[] columns) {
        String selection = "rowid = ? ";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
    }


    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
        String selection = SearchManager.SUGGEST_COLUMN_TEXT_1 + " MATCH ? ";
        String[] selectionArgs = new String[] {query+"*"};

        Log.d(TAG, "getWordMatches: selection " + selection);
        Log.d(TAG, "getWordMatches: selectionArgs " + selectionArgs);
        Log.d(TAG, "getWordMatches: columns " + columns);
        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_WORD> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the word column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the DictionaryProvider when queries are made.
         * - This can be revised to also search the definition text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_WORD (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }

    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */

        try {
//            Log.d(TAG, "corpNameQuery " + selection + selectionArgs[0]);
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(FTS_VIRTUAL_TABLE);
            builder.setProjectionMap(mColumnMap);

//            if (columns != null) {
//                Log.d(TAG, "columns " + columns[0]);
//            }
//            if (selection != null) {
//                Log.d(TAG, "selection " + selection + selectionArgs[0]);
//            }

            Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                    null, selection, selectionArgs, null, null, null);
//            Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
//                    columns, selection, selectionArgs, null, null, null);
//            Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
//                    null, null, null, null, null, null);

            if (cursor == null) {
                Log.e(TAG, "query cursor returns null ");
                return null;
            } else if (!cursor.moveToFirst()) {
                Log.e(TAG, "query cursor no first ");
                cursor.close();
                return null;
            }
            return cursor;
        } catch (Exception e) {
            Log.e(TAG, "queryError " + e.getMessage());
            return null;
        }
    }

    public void delete(String selectionClause, String[] selectionArgs) {
        mDatabaseOpenHelper.getWritableDatabase().delete(FTS_VIRTUAL_TABLE, selectionClause, selectionArgs);
    }

    public void deleteRows() {

        mDatabaseOpenHelper.getWritableDatabase().delete(FTS_VIRTUAL_TABLE, null, null);
//    	mDatabaseOpenHelper = new DictionaryOpenHelper(context);
        if (mDatabaseOpenHelper == null) {
            Log.v(TAG, "mDatabaseOpenHelper null");
        }


        mDatabaseOpenHelper.recreateDB(mDatabaseOpenHelper.getWritableDatabase());
    }


    public long addSuggestion(ContentValues values) {
        return mDatabaseOpenHelper.addSuggestion(values);
    }

    /**
     * This creates/opens the database.
     */
    private static class DictionaryOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        private int dbVers;

        /* Note that FTS3 does not support column constraints and thus, you cannot
         * declare a primary key. However, "rowid" is automatically used as a unique
         * identifier, so when making requests, we will use "_id" as an alias for "rowid"
         */
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        SearchManager.SUGGEST_COLUMN_FORMAT + "," +
                        SearchManager.SUGGEST_COLUMN_TEXT_1 + "," +
                        SearchManager.SUGGEST_COLUMN_TEXT_2 + "," +
                        SearchManager.SUGGEST_COLUMN_TEXT_2_URL + "," +
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA + "," +
                        SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA + "," +
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + "," +
                        SearchManager.SUGGEST_COLUMN_SHORTCUT_ID + "," +
                        SearchManager.SUGGEST_COLUMN_QUERY + "," +
                        SearchManager.SUGGEST_COLUMN_FLAGS + ");";


        DictionaryOpenHelper(Context context, int dbVers) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            this.dbVers = dbVers;
            Log.v(TAG, "dbVers " + dbVers);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Log.v(TAG, "onCreate db " + dbVers);
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
                Log.v(TAG, "dbVers " + dbVers);
                Log.v(TAG, "suggestTbl created " + FTS_TABLE_CREATE);


        }

        public void recreateDB(SQLiteDatabase db) {
            mDatabase = db;
            if (mDatabase == null) {
                Log.v(TAG, "database null");
            }
//            loadDictionary();
        }
//        /**
//         * Starts a thread to load the database table with words
//         */
//        private void loadDictionary() {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        loadWords();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }).start();
//        }

//        private void loadWords() throws IOException {
////            Log.d(TAG, "Loading accounts...");
//
//            List<SearchItem> listCorpNames = new ArrayList<SearchItem>();
//
//            for (Account item : listAccounts) {
//                SearchItem suggestItem = new SearchItem();
//                suggestItem.setIdCorpName("account");
//                suggestItem.setCorpName(item.getCorpName());
//                listCorpNames.add(suggestItem);
//            }
//
//
//            Collections.sort(listCorpNames,
//                    new SearchCorpNameComparator());
//
//        }



        /**
         * Add a word to the dictionary.
         * @return rowId or -1 if failed
         */
        public long addSuggestion(ContentValues values) {


            if (mDatabase == null) {
                Log.e(TAG, "mDatabase is null");
            }

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, values);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }



    protected static class SearchCorpNameComparator implements
            Comparator<SearchItem> {
        public int compare(SearchItem left, SearchItem right) {
            String corpName1 = (String) left.getCorpName();
            String corpName2 = (String) right.getCorpName();

            if (corpName1.compareToIgnoreCase(corpName2) < 0) {
                return -1;
            } else {
                if (corpName1.compareToIgnoreCase(corpName2) > 0) {
                    return +1;
                } else {
                    return +0;
                }
            }

        }
    }

}
