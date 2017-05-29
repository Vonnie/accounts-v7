package com.kinsey.passwords;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity
//        extends WebViewClient {
        extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    //    WebViewActivityFragment wvFragment;
    private WebView webview = null;
    private String webAddr;

    private boolean blnWebLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_web_view);
//        setContentView(R.layout.fragment_web_view);

//        View fragView = findViewById(R.id.fragmentWebView);
//
//        if (fragView == null) {
//            Log.d(TAG, "onCreate: fragView null");
//        } else {
//            Log.d(TAG, "onCreate: fragView not null");
//        }

//        WebView webview = new WebView(this);
//        setContentView(webview);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle arguments = getIntent().getExtras();
        webAddr = (String) arguments.getSerializable(WebViewActivity.class.getSimpleName());
        webview = (WebView) findViewById(R.id.wv_page);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        Log.d(TAG, "onCreate: loadUrl returned");
        blnWebLoaded = true;

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Log.d(TAG, "onJsBeforeUnload: ");
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });


        if (!webAddr.equals("")) {
            webview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webview.loadUrl(webAddr);
                }
            }, 500);
        }


//        wvFragment = (WebViewActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentWebView);

//        getWindow().requestFeature(Window.FEATURE_PROGRESS);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume: " + blnWebLoaded);
        Log.d(TAG, "onPostResume: " + blnWebLoaded);
//        if (blnWebLoaded) {
//            setResult(Activity.RESULT_OK);
//            finish();
//        } else {
//            super.onPostResume();
//        }
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: ");
        Log.d(TAG, "onBackPressed: " + webview.canGoBack());

        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            setResult(Activity.RESULT_OK);
            finish();
            super.onBackPressed();
        }

//        if (wvFragment.canBackOut()) {
//            Log.d(TAG, "onBackPressed: canbackout");
////            super.onBackPressed();
//            setResult(Activity.RESULT_OK);
//            finish();
//        } else {
//            Log.d(TAG, "onBackPressed: finished");
//            setResult(Activity.RESULT_OK);
//            finish();
//        }

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        Log.d(TAG, "onKeyDown: key back " + (keyCode == KeyEvent.KEYCODE_BACK));
//        // Check if the key event was the Back button and if there's history
////        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack())
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (wvFragment.canBackOut()) {
////            myWebView.goBack();
//                return true;
//            }
//        }
//        // If it wasn't the Back key or there's no web page history, bubble up to the default
//        // system behavior (probably exit the activity)
//        return super.onKeyDown(keyCode, event);
//    }
}
