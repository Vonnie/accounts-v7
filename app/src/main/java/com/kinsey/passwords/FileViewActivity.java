package com.kinsey.passwords;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileDao;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.tools.AppDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.format_ymdtime;

public class FileViewActivity extends AppCompatActivity
        implements AppDialog.DialogEvents {
    private static final String TAG = "FileViewActivity";

    ProgressBar progressBar;
    WebView webView;
//    private ProfileAdapter adapter;
//    private ProfileViewModel profileViewModel;

    private Handler mHandler = new Handler();
    boolean importRefreshReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String filename = getExternalFilesDir("passport/") + MainActivity.BACKUP_FILENAME;
//                Snackbar.make(view, filename, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Backup / Restore");


//        this.adapter = new ProfileAdapter();
//
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//
//        profileViewModel.getAllProfiles().observe(this, new Observer<List<Profile>>() {
//            @Override
//            public void onChanged(List<Profile> profiles) {
//                adapter.submitList(profiles);
//            }
//        });


        webView = (WebView) findViewById(R.id.wv_page);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '1px'})();");
                Log.d(TAG, "onPageFinished: ");
            }

        });
        introPage();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_file_view, menu);

        MenuItem item;
        File dirStorage = getExternalFilesDir("passport/");
        Log.d(TAG, "can read: " + dirStorage.canRead());
        Log.d(TAG, "storage dir: " + dirStorage.getAbsolutePath());
        if (dirStorage.canRead()) {
            item = menu.findItem(R.id.vw_show_file);
            item.setEnabled(true);
            item = menu.findItem(R.id.vw_export);
            item.setEnabled(true);
            item = menu.findItem(R.id.vw_import);
            item.setEnabled(true);
            item = menu.findItem(R.id.vw_shared);
            item.setEnabled(true);
        } else {
            item = menu.findItem(R.id.vw_show_file);
            item.setEnabled(false);
            item = menu.findItem(R.id.vw_export);
            item.setEnabled(false);
            item = menu.findItem(R.id.vw_import);
            item.setEnabled(false);
            item = menu.findItem(R.id.vw_shared);
            item.setEnabled(false);
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Log.d(TAG, "onOptionsItemSelected: ");
                break;

            case R.id.vw_show_file:
                Log.d(TAG, "onOptionsItemSelected: Export");
                reportFile();
                break;

            case R.id.vw_export:
                Log.d(TAG, "onOptionsItemSelected: Export");
                ExportAccountDB();
                break;

            case R.id.vw_import:
                Log.d(TAG, "onOptionsItemSelected: Import");
                ImportAccountDB();
                break;

            case R.id.vw_filename:
                Log.d(TAG, "onOptionsItemSelected: Share View filename");
                showFilename();
                break;

            case R.id.vw_shared:
                shareExport();
                Log.d(TAG, "onOptionsItemSelected: View share");
                break;

        }

        return super.onOptionsItemSelected(item);
    }


//    void createExternalStoragePrivateFile() {
//        // Create a path where we will place our private file on external
//        // storage.
//        File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
//
//        try {
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = getResources().openRawResource(R.drawable.ic_action_add);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
//    }

    void deleteExternalStoragePrivateFile() {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
        file.delete();
    }

    boolean hasExternalStoragePrivateFile() {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
        return file.exists();
    }

    private void introPage() {
        try {
//            Log.d(TAG, "reportJson: " + MainActivity.DEFAULT_APP_DIRECTORY);
            Log.d(TAG, "reportJson: ");
            boolean retSuccess = true;

            webView.getSettings().setJavaScriptEnabled(true);
            String htmlString;

//            String filename = getExternalFilesDir("passport/");
//            File dirStorage = new File(Environment.getDataDirectory() + "/passport");
            File dirStorage = getExternalFilesDir("passport");

            File fileExternal = new File(dirStorage, MainActivity.BACKUP_FILENAME);

//            Log.d(TAG, "reportJson: state " + Environment.getExternalStorageState());
//            Log.d(TAG, "reportJson: system " + System.getenv());

            if (dirStorage.exists()) {
                Log.d(TAG, "reportJson dirExternal " + dirStorage.getAbsolutePath());
                Log.d(TAG, "reportJson dirExternal canRead " + dirStorage.canRead());
                Log.d(TAG, "reportJson dirInternal free space " + dirStorage.getFreeSpace());

                if (dirStorage.canRead()) {
                    if (fileExternal.exists()) {
                        htmlString = greetMsg() +
                                "<h5>" + accountJsonProperties(fileExternal) + "</h5>" +
                                "<h5>" + MainActivity.adapter.getItemCount() + " Account Profile items on db<h5>";

                    } else {
                        htmlString = notfyMsg() +
                                "<h4>Use menu to export data.</h4>" +
                                "<h5>" + MainActivity.adapter.getItemCount() + " Account Profile items currently on db<h5>";
                    }
                } else {
                    htmlString = notfyMsg() +
                            "<h4>Path storage dir does not exists " + dirStorage.getAbsoluteFile() + "</h4>" +
                            "<h5>Use menu to export data to this file.</h5>" +
                            "<h5>" + MainActivity.adapter.getItemCount() + " Account Profile items to backup<h5>";
                }
            } else {
                htmlString = permissionMsg() +
                        "<h5>" + MainActivity.adapter.getItemCount() + " Account Profile items to backup once permission is granted<h5>";
            }

            webView.loadData(htmlString, "text/html", null);

            Log.d(TAG, "db count: " + MainActivity.adapter.getItemCount());
        } catch (
                Exception ex) {
            Log.d(TAG, "reportJson: " + ex.getMessage());
        }

    }


    private String warningMsg() {
        String htmlString = "<h1>Warning</h1>\n" +
                "<h2>Unable to acquire Exported Accounts</h2>\n" +
                "<h3>Either not previously exported or file storage issue</h3>";
        return htmlString;
    }

    private String notfyMsg() {
        String htmlString = "<h1>Notification</h1>" +
                "<h2>App storage available, export file not yet created</h2>";
        return htmlString;
    }

    private String greetMsg() {
        String htmlString = "<h1>Welcome</h1>" +
                "<h2>File Offload Properties</h2>" +
                "<h3>App storage available and export file exists.</h3>" +
                "<h4>See menu for file location</h4>";
        return htmlString;
    }
    private String permissionMsg() {
        String htmlString = "<h1>Permission Issue</h1>" +
                "<h2>Unable to access App storage for backup file.</h2>" +
                "<h3>Grant access for this App from Settings</h3>" +
                "<h3>Select Accounts app in apps list. Select permissions. Then, set on Storage.</h3>" +
                "<h4>Since version 10, backup storage has moved. See Filename from menu</h4>" +
                "<h4>Once permission is granted, return to backup the db.</h4>";
        return htmlString;
    }


    private String accountJsonProperties(File file) {

        String returnValue = "";
        try {

            List<Profile> listAccounts = new ArrayList<Profile>();

            final JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));

            int listCount = 0;
            reader.beginArray();
            while (reader.hasNext()) {
                Profile account = readMessage(reader);
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

            returnValue = listCount + " Accounts Items on backup file";
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
//        File fileExternal = new File(Environment.getDataDirectory() + "/passport/"
//                + MainActivity.BACKUP_FILENAME);
        File fileExternal = new File(getExternalFilesDir(null) + "/passport/"
                + MainActivity.BACKUP_FILENAME);


        String loc = "file://" + fileExternal.getAbsolutePath();
        Log.d(TAG, "reportJson: " + loc);
        webView.loadUrl(loc);
    }


    public void ExportAccountDB() {
        String msgError = "";
        int count = -1;

//        File path = null;
        try {

            File dirStorage = getExternalFilesDir("passport");

            Log.d(TAG, "ExportAccountDB: path found " + dirStorage.getPath());
            // Make sure the Pictures directory exists.
            if (!dirStorage.exists()) {
                dirStorage.mkdirs();
            }


            File file = new File(dirStorage, MainActivity.BACKUP_FILENAME);
            Log.d(TAG, "ExportAccountDB export filename: " + file.getAbsoluteFile());

            if (file.exists()) {
                Log.d(TAG, "ExportAccountDB: file exists " + file.getAbsoluteFile());
            } else {

                Log.d(TAG, "ExportAccountDB: file " + file.getAbsoluteFile());
                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getAbsoluteFile());
                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getParentFile().getAbsoluteFile());
                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getParentFile().getParentFile().getAbsoluteFile());
                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getPath());
                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getParentFile().getPath());
                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getParentFile().getParentFile().getPath());


                try {
                    if (file.createNewFile()) {
                        Log.d(TAG, "ExportAccountDB: file created " + file.getAbsoluteFile());
                    }
                } catch (IOException e1) {
                    msgError = e1.getMessage();
                    Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
                    Toast.makeText(this,
                            " Exported directory not exists",
                            Toast.LENGTH_LONG).show();
//                    fvFragment.setInfoMessage("Exported directory not exists");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                    msgError = "jsonError: " + e2.getMessage();
                    Log.v(TAG, msgError);
                }

            }

            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            count = writeMessagesArray(writer);
            writer.flush();
            writer.close();
            introPage();
            Toast.makeText(this,
                    count + " Exported accounts",
                    Toast.LENGTH_LONG).show();

        } catch (IOException e1) {
            msgError = e1.getMessage();
            Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
            Toast.makeText(this,
                    " Exported file directory issues",
                    Toast.LENGTH_LONG).show();
//                    fvFragment.setInfoMessage("Exported directory not exists");
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            msgError = "jsonError: " + e2.getMessage();
            Log.v(TAG, msgError);
            Toast.makeText(this, "Export has errors", Toast.LENGTH_SHORT).show();
        }
    }


    private void ImportAccountDB() {
        Log.d(TAG, "ImportAccountDB: starts");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        new Thread(new Runnable() {
            @Override
            public void run() {

//                String msg = "";
                boolean error = false;
                try {
                    List<Profile> listAccounts = new ArrayList<Profile>();

                    File dirStorage = getExternalFilesDir("passport");

                    Log.d(TAG, "run: path " + dirStorage.getAbsoluteFile());


                    final JsonReader reader = new JsonReader(new FileReader(
                            dirStorage.getAbsoluteFile() + "/" + MainActivity.BACKUP_FILENAME));

                    reader.beginArray();
                    while (reader.hasNext()) {
                        Profile account = readMessage(reader);

                        if (account == null) {
                            break;
                        } else {
                            listAccounts.add(account);
                            int listCount = listAccounts.size();
                        }
                    }
//                    Log.d(TAG, "run: count " + listAccounts.size());
                    reader.endArray();
                    reader.close();




                    MainActivity.profileViewModel.deleteAllProfiles();

                    MainActivity.profileViewModel.insertMulti(listAccounts);

//                    for (Profile item : listAccounts) {
//                        MainActivity.profileViewModel.insert(item);
//                    }
//
//                    String msg = MainActivity.adapter.getItemCount() + " Items uploaded";
//                    Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();

//                    ContentResolver contentResolver = getContentResolver();
//                    int deleteCount = getContentResolver().delete(AccountsContract.CONTENT_URI, null, null);
//                    Log.d(TAG, "run: delete count " + deleteCount);
//
//                    int itemCount = 0;
//                    for (Profile item : listAccounts) {
////                Log.d(TAG, "ImportAccountDB: acc " + item.getPassportId()
////                        + " " + item.getCorpName());
//                        addAccountToDB(contentResolver, item);
//                        itemCount++;
//                        int remCount = itemCount % 50;
//                        if (remCount == 0 || itemCount == 1) {
//                            Log.d(TAG, "run: count " + itemCount);
//                            final int runCount = itemCount;
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    String notifyMsg;
//                                    if (runCount == 1) {
//                                        notifyMsg = "progress: db replace begins";
//                                    } else {
//                                        notifyMsg = String.format("progress: %s rows replaced", runCount);
//                                    }
//                                    Toast.makeText(getApplicationContext(),
//                                            notifyMsg, Toast.LENGTH_LONG).show();
//
//                                }
//                            });
//                        }
//                    }
//
//                    Log.d(TAG, "run: import count " + itemCount);
//
//                    msg = listAccounts.size() + " Accounts Imported";
//
//                    importRefreshReq = true;
//                    Intent intent = new Intent();
//                    intent.putExtra("IMPORT", importRefreshReq);
//                    setResult(RESULT_OK, intent);
//                    finish();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
//                    msg = "import file not found";
                    error = true;
                } catch (IOException e) {
                    e.printStackTrace();
//                    msg = "import file error " + e.getMessage();
                    error = true;
//			savePassports();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    msg = "import exception";
                    error = true;
                }

//                final String notifyMsg = msg;
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Toast.makeText(getApplicationContext(),
//                                notifyMsg, Toast.LENGTH_LONG).show();
//
//
//                    }
//                });
            }
        }).start();

//        loadSearchDB();

//        fvFragment.setImportRefreshReq(true);


        //        FragmentManager fragmentManager = getSupportFragmentManager();
//        AppDialog newFragment = AppDialog.newInstance();
//        Bundle args = new Bundle();
//        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ASK_IF_NEED_DICTIONARY_REBUILD);
//        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
//        args.putString(AppDialog.DIALOG_MESSAGE, "Ask to rebuild search dictionary");
//        args.putString(AppDialog.DIALOG_SUB_MESSAGE, "Accounts changed, rebuild dictionary to sync up?");
//
//        newFragment.setArguments(args);
//        newFragment.show(fragmentManager, "dialog");

    }


    final private void addAccountToDB(ContentResolver contentResolver, Account account) {

        ContentValues values = new ContentValues();
        values.put(AccountsContract.Columns.PASSPORT_ID_COL, account.getPassportId());
        values.put(AccountsContract.Columns.CORP_NAME_COL, account.getCorpName());
        values.put(AccountsContract.Columns.USER_NAME_COL, account.getUserName());
        values.put(AccountsContract.Columns.USER_EMAIL_COL, account.getUserEmail());
        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, account.getCorpWebsite());
        values.put(AccountsContract.Columns.NOTE_COL, account.getNote());
        values.put(AccountsContract.Columns.OPEN_DATE_COL, account.getOpenLong());
        values.put(AccountsContract.Columns.ACTVY_DATE_COL, account.getActvyLong());
        values.put(AccountsContract.Columns.REF_FROM_COL, account.getRefFrom());
        values.put(AccountsContract.Columns.REF_TO_COL, account.getRefTo());
        values.put(AccountsContract.Columns.SEQUENCE_COL, account.getSequence());
        contentResolver.insert(AccountsContract.CONTENT_URI, values);

    }


    private void showFilename() {
        File fileExternal = new File(getExternalFilesDir(null) + "/passport/"
                + MainActivity.BACKUP_FILENAME);

        Log.d(TAG, "showFilename: " + Environment.getDataDirectory() + "/passport/" + MainActivity.BACKUP_FILENAME);
        Log.d(TAG, "showFilename: " + getExternalFilesDir(null) + "/passport/" + MainActivity.BACKUP_FILENAME);
        Log.d(TAG, "showFilename: " + getExternalFilesDir("passport/") + MainActivity.BACKUP_FILENAME);
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_EXPORT_FILENAME);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_OK);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_export_filename));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getExternalFilesDir(null) + "/passport/" + MainActivity.BACKUP_FILENAME);
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.ok);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }

    private void shareExport() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        dlg.setTitle(getResources().getString(R.string.app_name))
                .setMessage("Is the exported file up-to-date for this share.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        shareIntent();
                        // finish dialog
                        dialog.dismiss();
                        return;
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // finish dialog
                        dialog.dismiss();
                        return;
                    }

                })
                .show();
        dlg = null;

    }

    private void shareIntent() {

        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
//        ArrayList<Uri> uris = new ArrayList<Uri>();
        emailintent.setType("text/html");


        File file = new File(Environment.getDataDirectory() + "/passport",
                MainActivity.BACKUP_FILENAME);
        if (!file.exists()) {
            Toast.makeText(FileViewActivity.this,
                    "File not exported to email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App - import/export file");
//                Uri u = Uri.fromFile(file);
//                uris.add(u);
        emailintent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        emailintent.putExtra(Intent.EXTRA_TEXT, "Exported JSON file");

        emailintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        emailintent.putExtra(Intent.EXTRA_TEXT, "My Accounts Attachments");

        try {
            startActivity(Intent.createChooser(emailintent, "Send your accounts.json..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(FileViewActivity.this,
                    "Unable to get the shared menu",
                    Toast.LENGTH_LONG).show();
        }

//        Toast.makeText(FileViewActivityV1.this,
//                "Exported file shared sent",
//                Toast.LENGTH_SHORT).show();

    }


    final public Profile readMessage(JsonReader reader) {
        Profile item = new Profile();
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


    public int writeMessagesArray(JsonWriter writer) throws IOException {
        int count = 0;
        try {

            List<Account> listAccounts = loadAccounts();

            Log.d(TAG, "writeMessagesArray: " + listAccounts);
            writer.beginArray();
            for (Profile item : MainActivity.adapter.getCurrentList()) {
//            for (Account item : listAccounts) {
                writeMessage(writer, item);
                count++;
            }
            writer.endArray();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            Log.e(TAG, "writeMessageArrayError: " + e2.getMessage());
        }
        return count;
    }


    public void writeMessage(JsonWriter writer, Profile item)
            throws IOException {
        try {
            writer.beginObject();
            writer.name("corpName").value(item.getCorpName());
            writer.name("accountId").value(item.getPassportId());
            writer.name("seq").value(item.getSequence());
            writer.name("userName").value(item.getUserName());
            writer.name("userEmail").value(item.getUserEmail());
            writer.name("refFrom").value(item.getRefFrom());
            writer.name("refTo").value(item.getRefTo());
            if (item.getCorpWebsite() == null) {
                writer.name("website").nullValue();
            } else {
                writer.name("website").value(item.getCorpWebsite().toString());
            }
            if (item.getOpenLong() == 0) {
                writer.name("openDt").nullValue();
            } else {
                writer.name("openDt").value(
                        format_ymdtime.format(item.getOpenLong()));
            }
            if (item.getActvyLong() == 0) {
                writer.name("actvyDt").nullValue();
            } else {
                writer.name("actvyDt").value(
                        format_ymdtime.format(item.getActvyLong()));
            }
            Log.d(TAG, "writeMessage: note " + item.getNote());
            writer.name("note").value(item.getNote());

            writer.endObject();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            Log.v(TAG, "writeMessageError: " + e2.getMessage());
        }
    }


    List<Account> loadAccounts() {
//        Log.d(TAG, "loadAccounts: starts ");
        Cursor cursor = getContentResolver().query(
                AccountsContract.CONTENT_URI, null, null, null,
                String.format("%s COLLATE NOCASE ASC, %s COLLATE NOCASE ASC", AccountsContract.Columns.CORP_NAME_COL, AccountsContract.Columns.SEQUENCE_COL));
//                        AccountsContract.Columns.CORP_NAME_COL);

        List<Account> listAccounts = new ArrayList<Account>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Account item = AccountsContract.getAccountFromCursor(cursor);
//                        Account item = new Account(
//                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
//                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
//                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
//                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
//                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
//                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
//
//                        if (cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
//                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
//                                item.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
//                            }
//                        }
//                        if (cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
//                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
//                                item.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
//                            }
//                        }
//                        if (cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL) != -1) {
//                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL))) {
//                                item.setActvyLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL)));
//                            }
//                        }
//                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL) != -1) {
//                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL))) {
//                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL)));
//                            }
//                        }
//                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL) != -1) {
//                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL))) {
//                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL)));
//                            }
//                        }
                listAccounts.add(item);
            }
            cursor.close();
        }

        return listAccounts;
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }



    private static class UploadProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private UploadProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.update(profiles[0]);
            return null;
        }
    }


}
