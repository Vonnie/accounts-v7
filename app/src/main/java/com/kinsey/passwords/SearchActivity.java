package com.kinsey.passwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.provider.AccountSuggestsLoaderCallbacks;

public class SearchActivity extends AppCompatActivity
        implements SearchActivityFragment.OnActionListener {
    private static final String TAG = "SearchActivity";

    public static final int CONTACT_QUERY_LOADER = 1;

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
                "Search DB built",
                Toast.LENGTH_LONG).show();

    }


    private void deleteAllSuggestions() {
//		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
//		String[] selectionArgs = { "account" };
        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
        getContentResolver().delete(
                SearchesContract.CONTENT_URI_TRUNCATE,
                null, null);

    }

    @Override
    public void onSearchRequestClicked(String searchValue) {

    }
}
