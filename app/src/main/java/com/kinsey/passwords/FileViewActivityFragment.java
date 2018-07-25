package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class FileViewActivityFragment extends Fragment {
    private static final String TAG = "FileViewActivityFrag";

    WebView webView;
//    TextView tvLocation;

    boolean importRefreshReq = false;

    public FileViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_view, container, false);

        webView = (WebView) view.findViewById(R.id.wv_page);
//        tvLocation = (TextView) view.findViewById(R.id.wv_location);
        Log.d(TAG, "onCreateView: ");
//        tvLocation.setText("see it");
//        Log.d(TAG, "onCreateView: tvLocation set");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '30px'})();");
            }
        });
        reportJson();


//        String unencodedHtml =
//                "<html><body>'%28' is the code for '('</body></html>";
//        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
//        webView.loadData(encodedHtml, "text/html", "base64");

        return view;
    }


    public void setInfoMessage(String str) {
//        tvLocation.setText(str);
    }

    public void reportJson() {
        Log.d(TAG, "reportJson: " + MainActivity.DEFAULT_APP_DIRECTORY);
        boolean retSuccess = true;

        File file = new File(MainActivity.DEFAULT_APP_DIRECTORY
                + "/accounts.json");
        String loc = "file://" + file.getAbsolutePath();
        Log.d(TAG, "reportJson: " + loc);
//        webView.setVisibility(View.GONE);
//        tvLocation.setText(loc);

        try {
            if (!file.exists()) {
//            jsonViewListener.onSetTitle("Export from Home...");
//			tvContent.setText("json export file not found");
                retSuccess = false;
//            String myHtmlString = "<h1>File not found</h1>";
//            webView.loadData(myHtmlString, "text/html; charset=UTF-8", null);

//                String unencodedHtml =
//                        "<html><body>'%28' is the code for '('</body></html>";
//                String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
//                webView.loadData(encodedHtml, "text/html", "base64");


                webView.getSettings().setJavaScriptEnabled(true);
                String htmlString = "<br><br><h1>Warning</h1>\n" +
                        "<h2>Unable to acquire Exported Accounts</h2>\n" +
                        "<h3>Either not previously exported or</h3>" +
                        "<h3>Not located on storage. See menu for intended filename.</h3>";
                webView.loadData(htmlString, "text/html", null);

//            webView.setVisibility(View.GONE);
//            tvLocation.setText("file not found");

            } else {
                try {


                    webView.getSettings().setJavaScriptEnabled(true);
//                    String htmlString = "<h1>This is header one.</h1>\n" +
//                            "<h2>..waiting for data..</h2>\n" +
//                            "<h3>This is header three.</h3>";
//                    webView.loadData(htmlString, "text/html", null);

                    //                webView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "reportJson: about to load " + loc);
                    webView.loadUrl(loc);


                } catch (Exception e) {
                    Log.e(TAG, "inputStream error " + e.getMessage());
                    retSuccess = false;
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "reportJson: " + ex.getMessage());
        }
    }

    public boolean isImportRefreshReq() {
        return importRefreshReq;
    }

    public void setImportRefreshReq(boolean importRefreshReq) {
        this.importRefreshReq = importRefreshReq;
    }
}
