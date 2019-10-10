package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kinsey.passwords.items.Account;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.format_ymdtime;

/**
 * A placeholder fragment containing a simple view.
 */
public class FileViewActivityFragment extends Fragment {
    private static final String TAG = "FileViewActivityFrag";

    WebView webView;
//    TextView tvLocation;

//    boolean importRefreshReq = false;


    private OnFileViewClickListener mListener;

    public interface OnFileViewClickListener {

        void onFileViewShown();
    }


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
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '5px'})();");
                Log.d(TAG, "onPageFinished: ");
                if (mListener != null) {
                    mListener.onFileViewShown();
                }
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
        try {
//            Log.d(TAG, "reportJson: " + MainActivity.DEFAULT_APP_DIRECTORY);
            Log.d(TAG, "reportJson: ");
            boolean retSuccess = true;

            webView.getSettings().setJavaScriptEnabled(true);
            String htmlString;

            File dirStorage = new File(MainActivity.DEFAULT_APP_DIRECTORY);

            File pathExternal = new File(MainActivity.DEFAULT_APP_DIRECTORY_DATA);

            File fileExternal = new File(pathExternal, MainActivity.BACKUP_FILENAME);

//            Log.d(TAG, "reportJson: state " + Environment.getExternalStorageState());
//            Log.d(TAG, "reportJson: system " + System.getenv());

            if (dirStorage.exists()) {
                Log.d(TAG, "reportJson dirExternal " + dirStorage.getAbsolutePath());
                Log.d(TAG, "reportJson dirExternal canRead " + dirStorage.canRead());
                Log.d(TAG, "reportJson dirInternal free space " + dirStorage.getFreeSpace());

                if (dirStorage.canRead()) {

                    if (pathExternal.exists()) {
                        if (fileExternal.exists()) {
                            htmlString = notfyMsg() +
                                    "<h4>Have storage file "  + "</h4>" +
                                    "<h5>" + fileExternal.getAbsoluteFile() + "</h5>" +
                                    "<h5>" + accountJsonProperties(fileExternal) + "</h5>";

                            //                File path2 = new File(, "passport");
                            //
                            //                Log.d(TAG, "run: path2 " + path2.getAbsoluteFile());
                            //
                            //                final JsonReader reader = new JsonReader(new FileReader(
                            //                        path2.getAbsoluteFile() + "/accounts.json"));
                        } else {
                            htmlString = notfyMsg() +
                                    "<h4>But presently no account export file on " + pathExternal.getAbsoluteFile() + "</h4>" +
                                    "<h5>Use menu to export data to this file.</h5>";
//                            try {
//                                fileExternal.createNewFile();
//                                Log.d(TAG, "reportJson: new file create on " + fileExternal.getAbsoluteFile());
//                            } catch (Exception ex) {
//                                Log.d(TAG, "reportJson: " + ex.getMessage());
//                            }
                        }
                    } else {
                        htmlString = notfyMsg() +
                                "<h4>Path external does not exists " + pathExternal.getAbsoluteFile() + "</h4>" +
                                "<h5>Use menu to export data to this file.</h5>";
                    }
                } else {
                    htmlString = permissionMsg();
                }
            } else {
                htmlString = warningMsg() +
                        "<h5>Have no external storage for file " + dirStorage.getAbsoluteFile() + "</h5>";
            }

            webView.loadData(htmlString, "text/html", null);

        } catch (Exception ex) {
            Log.d(TAG, "reportJson: " + ex.getMessage());
        }

    }

    private String warningMsg() {
        String htmlString = "<br><br><h1>Warning</h1>\n" +
                "<h2>Unable to acquire Exported Accounts</h2>\n" +
                "<h3>Either not previously exported or file storage issue</h3>";
        return htmlString;
    }

    private String notfyMsg() {
        String htmlString = "<br><br><h1>Notification</h1>\n" +
                "<h2>App storage available</h2>\n" ;
        return htmlString;
    }

    private String permissionMsg() {
        String htmlString = "<br><br><h1>Permission</h1>\n" +
                "<h2>Unable to access App storage for backup file.</h2>\n" +
                "<h3>App may need permission from Settings for Apps</h3>" +
                "<h3>Select Accounts app in apps list, then select permissions.</h3>" +
                "<h3>Set on Storage.</h3>";
        return htmlString;
    }

    private String accountJsonProperties(File file) {

        String returnValue = "";
        try {

            List<Account> listAccounts = new ArrayList<Account>();

            final JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));

            int listCount = 0;
            reader.beginArray();
            while (reader.hasNext()) {
                Account account = readMessage(reader);
                if (account == null) {
                    break;
                } else {
                    listAccounts.add(account);
                    listCount = listAccounts.size();
                }
            }
//                    Log.d(TAG, "run: count " + listAccounts.size());
            reader.endArray();
            reader.close();

            returnValue = listCount + " #Accounts on backup file";
        } catch (IOException e) {
            e.printStackTrace();
            returnValue = "error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = "error: " + e.getMessage();
        } finally {
            return returnValue;
        }

    }

    public void reportFile() {
        File fileExternal = new File(MainActivity.DEFAULT_APP_DIRECTORY_DATA
                + "/" + MainActivity.BACKUP_FILENAME);
        String loc = "file://" + fileExternal.getAbsolutePath();
        Log.d(TAG, "reportJson: " + loc);
        webView.loadUrl(loc);
    }

    public void reportFile2() {
        boolean retSuccess = true;
        File file = new File(MainActivity.DEFAULT_APP_DIRECTORY_DATA
                + "/" + MainActivity.BACKUP_FILENAME);
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


//                        "<h3>Not located on external storage. </h3>" +
//                        "<h3>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "<h3>" +
//                        "<h3>" +MainActivity.DEFAULT_APP_DIRECTORY + "/accounts.json" + "<h3>";
//                webView.loadData(htmlString, "text/html", null);

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


    public void exportJson() {
        try {
            File fileExternal = new File(MainActivity.DEFAULT_APP_DIRECTORY_DATA
                    + "/" + MainActivity.BACKUP_FILENAME);
            String loc = "file://" + fileExternal.getAbsolutePath();
            webView.loadUrl(loc);
        } catch (Exception ex) {
            Log.d(TAG, "reportJson: " + ex.getMessage());
        }
    }
//    public boolean isImportRefreshReq() {
//        return importRefreshReq;
//    }
//
//    public void setImportRefreshReq(boolean importRefreshReq) {
//        this.importRefreshReq = importRefreshReq;
//    }



    final public Account readMessage(JsonReader reader) {
        Account item = new Account();
        boolean retSuccess = true;
        try {
            reader.beginObject();
            Calendar c1 = Calendar.getInstance();
            while (reader.hasNext()) {
                String name = reader.nextName();
                String value = "";
                int iValue = 0;
                if (name.equals("corpName")) {
                    // System.out.println(reader.nextString());
                    value = reader.nextString();
//					Log.v(TAG, "json corpName " + value);
                    item.setCorpName(value);
                } else if (name.equals("accountId")) {
                    // System.out.println(reader.nextInt());
                    iValue = reader.nextInt();
                    Log.v(TAG, "json id " + iValue);
                    item.setPassportId(iValue);
                } else if (name.equals("seq")) {
                    // System.out.println(reader.nextInt());
                    iValue = reader.nextInt();
//					Log.v(TAG, "json seq " + iValue);
                    item.setSequence(iValue);
                } else if (name.equals("userName")) {
                    value = reader.nextString();
//					Log.v(TAG, "json userName " + value);
                    item.setUserName(value);
                } else if (name.equals("userEmail")) {
                    value = reader.nextString();
//					Log.v(TAG, "json userEmail " + value);
                    item.setUserEmail(value);
                } else if (name.equals("refFrom")) {
                    iValue = reader.nextInt();
//					Log.v(TAG, "json refFrom " + iValue);
                    item.setRefFrom(iValue);
                } else if (name.equals("refTo")) {
                    iValue = reader.nextInt();
//					Log.v(TAG, "json refTo " + iValue);
                    item.setRefTo(iValue);
                } else if (name.equals("website")) {
                    value = reader.nextString();
//					Log.v(TAG, "json website " + value);
//                    URL urlValue = new URL(value);
//                    item.setCorpWebsite(urlValue);
                    item.setCorpWebsite(value);
                } else if (name.equals("openDt")) {
                    value = reader.nextString();
//					Log.v(TAG, "json openDt " + value);
                    Date dte = format_ymdtime.parse(value);
                    c1.setTime(dte);
                    item.setOpenLong(c1.getTimeInMillis());
                } else if (name.equals("actvyDt")) {
//					Log.v(TAG, "actvyDt reader " + reader);
                    Date dte;
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                        dte = new Date();
                    } else {
                        value = reader.nextString();
//						Log.v(TAG, "json actvyDt " + value);
                        dte = format_ymdtime.parse(value);
                    }
                    c1.setTime(dte);
                    item.setActvyLong(c1.getTimeInMillis());
                } else if (name.equals("note")) {
                    value = reader.nextString();
                    item.setNote(value);
                } else {
                    reader.skipValue(); // avoid some unhandle events
                }
            }

            reader.endObject();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            item = null;
        }
        return item;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof FileViewActivityFragment.OnFileViewClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement FileViewActivityFragment interface");
        }
        mListener = (FileViewActivityFragment.OnFileViewClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
