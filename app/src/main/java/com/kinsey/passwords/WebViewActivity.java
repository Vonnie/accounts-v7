package com.kinsey.passwords;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
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
            Log.d(TAG, "onBackPressed: canbackout");
//            super.onBackPressed();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Log.d(TAG, "onBackPressed: finished");
            setResult(Activity.RESULT_OK);
            finish();
        }

    }
}
