package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        WebViewActivityFragment fragment = (WebViewActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentWebView);
        if (fragment.canBackOut()) {
            super.onBackPressed();
        }

    }
}
