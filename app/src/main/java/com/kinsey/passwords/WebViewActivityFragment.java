package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewActivityFragment extends Fragment {
    private static final String TAG = "WebViewActivityFragment";

    private WebView wv = null;
    private String webAddr;

    public WebViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        Bundle arguments = getActivity().getIntent().getExtras();

        webAddr = (String) arguments.getSerializable(WebViewActivity.class.getSimpleName());

        Log.d(TAG, "onCreateView: webAddr " + webAddr);
        if (webAddr == null) {
            return view;
        }
        wv = (WebView) view.findViewById(R.id.wv_page);
        wv.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = wv.getSettings();

        webSettings.setJavaScriptEnabled(true);

        if (webAddr.toLowerCase().startsWith("http")) {
            wv.loadUrl(webAddr);
        } else {
            if (!webAddr.equals("")) {
                wv.loadUrl(webAddr);
            }
        }


        return view;

    }

    public boolean canBackOut() {
        if (wv.canGoBack()) {
            wv.goBack();
            return false;
        } else {
            return true;
        }
    }


    private class MyWebViewClient extends WebViewClient {
        //		http://www.androidaspect.com/2012/09/android-webview-tutorial.html
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
