package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.provider.SearchRecyclerViewAdapter;

public class SearchListActivity extends AppCompatActivity
        implements SearchListActivityFragment.OnActionListener,
        SearchRecyclerViewAdapter.OnAccountClickListener {
    private static final String TAG = "SearchListActivity";

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Search Request", Snackbar.LENGTH_LONG)
//                        .setAction("Search",
//
//
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        onSearchRequested();
//                                    }
//                                }
//
//
//
//                ).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        Log.d(TAG, "onCreate: ends");
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_list, menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
//        mSearchView.setSearchableInfo(searchableInfo);
//
//        mSearchView.setIconified(true);
//
//        Log.d(TAG, "onCreateOptionsMenu: returned " + true);
//
//        return true;
//    }

    @Override
    public void onLoadSearchClicked() {

    }

    @Override
    public void onSearchRequestClicked(String searchValue) {

    }

    @Override
    public void onAccountEditClick(Account account) {

    }

    @Override
    public void onAccountDeleteClick(Account account) {

    }
}
