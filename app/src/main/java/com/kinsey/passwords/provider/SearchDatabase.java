package com.kinsey.passwords.provider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.kinsey.passwords.items.SearchItem;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchDatabase {
    private static final String TAG = "SearchDatabase";

    //The columns we'll include in the dictionary table
    public static final String KEY_CORP_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String CORP_NAME_TEXT_2 = SearchManager.SUGGEST_COLUMN_TEXT_2;
    public static final String ID_CORP_NAME = SearchManager.SUGGEST_COLUMN_FLAGS;
    public static final String ICON_CORP_NAME = SearchManager.SUGGEST_COLUMN_ICON_1;
    public static final String SEARCH_DB_ID = "search_alt_db_id";
    public static final String LOOKUP_KEY = ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY;

    public static final String DATABASE_NAME = "corpnamebank";
    public static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final int DATABASE_VERSION = 7;


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
                        KEY_CORP_NAME + "," +
                        CORP_NAME_TEXT_2 + "," +
                        ID_CORP_NAME + "," +
                        ICON_CORP_NAME + "," +
                        SEARCH_DB_ID + "," +
                        LOOKUP_KEY + ");";


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

        /**
         * Starts a thread to load the database table with words
         */
        private void loadDictionary() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadWords() throws IOException {
//            Log.d(TAG, "Loading accounts...");

//            List<SearchItem> listCorpNames = new ArrayList<SearchItem>();

//            for (AccountItem item : PassportDBProvider.listAccounts) {
//                SearchItem suggestItem = new SearchItem();
//                suggestItem.setIdCorpName("account");
//                suggestItem.setCorpName(item.getCorpName());
//                listCorpNames.add(suggestItem);
//            }


            Collections.sort(SearchProvider.listSearches,
                    new SearchCorpNameComparator());

        }



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
