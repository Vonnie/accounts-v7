package com.kinsey.passwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.items.SuggestsContract;
import com.kinsey.passwords.provider.CursorRecyclerViewAdapter;

public class SuggestListActivity extends AppCompatActivity
        implements CursorRecyclerViewAdapter.OnSuggestClickListener {
    private static final String TAG = "SuggestListActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Re-generate passwords", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSuggestDeleteClick(Suggest suggest) {
        getContentResolver().delete(SuggestsContract.buildIdUri(suggest.getId()), null, null);
    }

    @Override
    public void onSuggestEditClick(Suggest suggest) {

    }


}
