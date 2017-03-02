package com.kinsey.passwords;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class FileViewActivityFragment extends Fragment {

    WebView webView;

    public FileViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_view, container, false);

        webView = (WebView) view.findViewById(R.id.wv_page);
        reportJson();

        return view;
    }

    private void reportJson() {
        boolean retSuccess = true;
        File file = new File(MainActivity.DEFAULT_APP_DIRECTORY
                + "/accounts.json");
        if (!file.exists()) {
//            jsonViewListener.onSetTitle("Export from Home...");
//			tvContent.setText("json export file not found");
            retSuccess = false;
        } else {
            try {

                webView.loadUrl("file://" + file.getAbsolutePath());

            } catch (Exception e) {
                Log.e(TAG, "inputStream error " + e.getMessage());
                retSuccess = false;
            }
        }
    }
}
