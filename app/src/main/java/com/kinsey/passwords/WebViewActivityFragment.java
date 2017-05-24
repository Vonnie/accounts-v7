package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewActivityFragment extends Fragment {
    private static final String TAG = "WebViewActivityFragment";

    private WebView webview = null;
    private String webAddr;

    public WebViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        Bundle arguments = getActivity().getIntent().getExtras();

        webAddr = (String) arguments.getSerializable(WebViewActivity.class.getSimpleName());

//        Log.d(TAG, "onCreateView: webAddr " + webAddr);
        if (webAddr == null) {
            return view;
        }
        webview = (WebView) view.findViewById(R.id.wv_page);
//        webview.setWebViewClient(new MyWebViewClient());

//        setContentView(webview);


        WebSettings webSettings = webview.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

//        webview.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                getActivity().setProgress(progress * 1000);
//            }
//
//            @Override
//            public void onCloseWindow(WebView window) {
//                Log.d(TAG, "onCloseWindow: ");
//                super.onCloseWindow(window);
//            }
//
//
//        });
//
//        webview.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//            @Override
//            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                return null;
//            }
//        });


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
            webview.loadUrl(webAddr);
        }

        
        

        return view;

    }

    public boolean canBackOut() {
        if (webview.canGoBack()) {
            webview.goBack();
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
