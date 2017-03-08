package com.kinsey.passwords;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.provider.AccountSuggestsLoaderCallbacks;

public class SearchActivity extends AppCompatActivity
        implements SearchActivityFragment.OnActionListener {
    private static final String TAG = "SearchActivity";

    public static final String SEARCH_QUERY = "SearchActivity";
    public static final String SEARCH_ACCOUNT = "SearchActivityAccount";

    public static final int CONTACT_QUERY_LOADER = 1;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Request Search DB Build", Snackbar.LENGTH_LONG)
                        .setAction("Prepare DB",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadSearchDB();
                                    }
                                }
                                ).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    public void onLoadSearchClicked() {

    }

    private void loadSearchDB() {
        deleteAllSuggestions();
        AccountSuggestsLoaderCallbacks loaderAcctCallbacks = new AccountSuggestsLoaderCallbacks(this);
        getLoaderManager().restartLoader(CONTACT_QUERY_LOADER, null, loaderAcctCallbacks);
        Toast.makeText(this,
                "Search Dictionary DB built",
                Toast.LENGTH_LONG).show();

    }


    private void deleteAllSuggestions() {
//		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
//		String[] selectionArgs = { "account" };
//        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
        getContentResolver().delete(
                SearchesContract.CONTENT_URI_TRUNCATE,
                null, null);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);

        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "onQueryTextSubmit: called");
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SEARCH_QUERY, query).apply();
//                showSuggestions();

                SearchesContract.cursorSearch = mSearchView.getSuggestionsAdapter().getCursor();
                mSearchView.clearFocus();
//                Log.d(TAG, "onQueryTextSubmit: showSearches");

//                showSearches(-1);
//                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG, "onQueryTextChange: adt " + mSearchView.getSuggestionsAdapter().getCount());
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SEARCH_QUERY, newText).apply();
                return true;
            }


        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                Log.d(TAG, "SearchView onClose: starts");
//                showSuggestions();
                finish();

//                Intent detailIntent = new Intent(this, AccountListActivity.class);
//                detailIntent.putExtra(Account.class.getSimpleName(), sortorder);
//                startActivityForResult(detailIntent, REQUEST_ACCOUNTS_LIST);

                return true;
            }
        });


        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
//                Log.d(TAG, "onSuggestionSelect: position " + position);
//                showSuggestions();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
//                Log.d(TAG, "onSuggestionClick: position " + position);
//                showSuggestions();
                int accountId = showAccount(position);
                showSearches(accountId);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putInt(SEARCH_ACCOUNT, accountId).apply();
//                finish();
                return true;
            }
        });

//        Log.d(TAG, "onCreateOptionsMenu: returned " + true);

        return true;
    }

    private void showSuggestions() {
        if (mSearchView == null) {
            return;
        }
        Log.d(TAG, "showSuggestions: adt rows " + mSearchView.getSuggestionsAdapter().getCount());
        if (mSearchView.getSuggestionsAdapter().getCount() > 0) {
            Log.d(TAG, "showSuggestions: adt " + mSearchView.getSuggestionsAdapter().getItem(0));
            Log.d(TAG, "showSuggestions: adt cols " + mSearchView.getSuggestionsAdapter().getCursor().getColumnCount());
            Log.d(TAG, "showSuggestions: adt " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(0));
            if (mSearchView.getSuggestionsAdapter().getCursor().moveToFirst()) {
                do {
                    for (int i = 0; i < mSearchView.getSuggestionsAdapter().getCursor().getColumnCount(); i++) {
                        Log.d(TAG, "showSuggestions: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(i)
                                + ": " + mSearchView.getSuggestionsAdapter().getCursor().getString(i));
                    }
                    Log.d(TAG, "showSuggestions: ==========================");
                } while (mSearchView.getSuggestionsAdapter().getCursor().moveToNext());


                SearchesContract.cursorSearch = mSearchView.getSuggestionsAdapter().getCursor();
//                Intent detailIntent = new Intent(this, SearchListActivity.class);
//                startActivity(detailIntent);

//                if (account != null) { // editing an account
//                    detailIntent.putExtra(Account.class.getSimpleName(), mSearchView.getSuggestionsAdapter().getCursor());
//                    startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
//
//                }
            }
        }

    }

    private int showAccount(int position) {
//        Log.d(TAG, "showAccount: pos " + position);
        mSearchView.getSuggestionsAdapter().getCursor().moveToFirst();
        mSearchView.getSuggestionsAdapter().getCursor().move(position);
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(3));
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getString(3));
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(5));
        int dbId = Integer.valueOf(mSearchView.getSuggestionsAdapter().getCursor()
                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA)));
//        Log.d(TAG, "showAccount: " + dbId);
        String corpName = mSearchView.getSuggestionsAdapter().getCursor()
                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
//        Log.d(TAG, "showAccount: corpName " + corpName);

        return dbId;

    }

    private void showSearches(int accountId) {
//        Log.d(TAG, "showSearches: starts");
        Intent detailIntent = new Intent(this, SearchListActivity.class);
        detailIntent.putExtra(SearchListActivity.class.getSimpleName(), accountId);
        startActivity(detailIntent);
    }

    @Override
    public void onSearchRequestClicked(String searchValue) {

    }
}
