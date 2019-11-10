package com.kinsey.passwords;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.tools.ProfileJsonListIO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;
import static androidx.core.content.FileProvider.getUriForFile;

public class FileViewActivity extends AppCompatActivity {
//        implements AppDialog.DialogEvents {
    private static final String TAG = "FileViewActivity";

    private ProgressBar progressBar;
    private WebView webView;
    private ProfileAdapter adapter;
//    private ProfileViewModel profileViewModel;

//    private Handler mHandler = new Handler();
//    boolean importRefreshReq = false;
//    public static File dirStorage;
//    View fileViewActivityView;
//    ShareActionProvider myShareActionProvider;
    boolean onFirstReported = true;
    private static final int BACKUP_FILE_REQUESTED = 1;


    private static String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        progressBar = findViewById(R.id.progressBar);

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


        webView = findViewById(R.id.wv_page);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '1px'})();");
                Log.d(TAG, "onPageFinished: ");
            }

        });


        adapter = new ProfileAdapter();
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        MainActivity.profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                adapter.submitList(profiles);
                if (onFirstReported) {
                    onFirstReported = false;
                    infoPage();
                }
                Log.d(TAG, "viewModel db count " + adapter.getItemCount());
            }
        });


        progressBar.setVisibility(View.INVISIBLE);
//        new CountDownTimer(30000, 100) {
//
//            public void onTick(long millisUntilFinished) {
////                tvListTitle.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
////                tvListTitle.setText("done!");
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        }.start();


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
        if (checkPermission()) {
//        if (dirStorage.canRead()) {
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

//        MenuItem shareItem = menu.findItem(R.id.vw_shared);
//        myShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        switch (id) {
//            case R.id.action_settings:
//                Log.d(TAG, "onOptionsItemSelected: ");
//                break;

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
                composeEmail();
//                shareExport();
                Log.d(TAG, "onOptionsItemSelected: View share");
                break;


            default:
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        setResult(RESULT_CANCELED);
        finish();
        return false;
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

    public void infoPage() {
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

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE))
//            {
//            }

//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    STORAGE_PERMISSION_CODE);


            if (checkPermission()) {
                if (!dirStorage.exists()) {
                    dirStorage.mkdirs();
                }
//                Log.d(TAG, "reportJson dirExternal " + dirStorage.getAbsolutePath());
//                Log.d(TAG, "reportJson dirExternal canRead " + dirStorage.canRead());
//                Log.d(TAG, "reportJson dirInternal free space " + dirStorage.getFreeSpace());

                if (dirStorage.canRead()) {
                    if (fileExternal.exists()) {
                        htmlString = greetMsg() +
                                "<hr><h5>" + adapter.getItemCount() + " Account Profile items currently on db<h5>" +
                                "<h5>" + accountJsonProperties(fileExternal.getAbsoluteFile().toString()) + "</h5><hr>" +
                                "<h5>Notify: If uninstall app, the exported / backup file also is deleted.</h5>" +
                                "<h5>Preserve file with a file copy or share from menu</h5><hr>" +
"<h5>Information: To setup this app to another device, use this Backup/Restore feature to populate data onto the other device.</h5>" +
                        "<h5>Steps: 1. Backup 2. Install app to other device 3. Copy to device backup file, see loc from menu item filename 4. Restore</h5>";
                    } else {
                        htmlString = notfyMsg() +
                                "<h4>Use menu to export data.</h4>" +
                                "<h5>" + adapter.getItemCount() + " Account Profile items currently on db<h5>" +
                        "<h4>Notify</h4>" +
                                "<h4>As of version name 11, backup storage has moved. See Filename from menu</h4>";
                    }
                } else {
                    htmlString = notfyMsg() +
                            "<h4>Path storage dir does not exists " + dirStorage.getAbsoluteFile() + "</h4>" +
                            "<h5>Use menu to export data to this file.</h5>" +
                            "<h5>" + adapter.getItemCount() + " Account Profile items to backup<h5>";
                }

            } else {
                htmlString = permissionMsg() +
                        "<h5>" + adapter.getItemCount() + " Account Profile items to backup once permission is granted<h5>";
            }

            webView.loadData(htmlString, "text/html", null);

            Log.d(TAG, "db count: " + adapter.getItemCount());

        } catch (Exception ex) {
            Log.d(TAG, "reportJson: " + ex.getMessage());
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private String warningMsg() {
        String htmlString = "<h1>Warning</h1>" +
                "<h2>Unable to acquire Exported Accounts</h2>" +
                "<h3>Either not previously exported or file storage issue</h3>";
        return htmlString;
    }

    private String notfyMsg() {
        String htmlString = "<h1>Notification</h1>" +
                "<h2>App storage available and with permissions,</h2>" +
                "<h2>Export file not yet created</h2>";
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
                "<h2>Unable to access App storage for Backup / Restore</h2>" +
                "<h3>Grant Permission Instruction</h3>" +
                "<h4>Grant access for this App from Settings</h4>" +
                "<h4>Select Accounts app in apps list. Select permissions. Then, set on Storage.</h4>" +
                "<h4>Once permission is granted, return to backup the db.</h4>" +
                "<h3>Notify</h3>" +
                "<h4>As of version name 11, backup storage has moved. See Filename from menu</h4>";
        return htmlString;
    }


    private String accountJsonProperties(String filename) {

        String returnValue = "";
        ProfileJsonListIO profileJsonListIO = new ProfileJsonListIO();
        try {

            List<Profile> listAccounts = new ArrayList<Profile>();

            listAccounts = profileJsonListIO.readProfileJson(filename);

            int listCount = listAccounts.size();

            returnValue = listCount + " Items on Accounts backup file";
//                    + " created " + format_mdy.format(file.lastModified());


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


        if (fileExternal.exists()) {
            String loc = "file://" + fileExternal.getAbsolutePath();
            Log.d(TAG, "reportJson: " + loc);
            webView.loadUrl(loc);
            return;
        }



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Unable to show export file. File has errors.");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void ExportAccountDB() {
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
                alertMsg("Backup Temporaruly Unavailable");
//                alertBackup(file, "Backup file exists. Want to over-write it? Created: "
//                        + format_mdy.format(file.lastModified()));
            } else {

//                Log.d(TAG, "ExportAccountDB: file " + file.getAbsoluteFile());
//                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getAbsoluteFile());
//                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getParentFile().getAbsoluteFile());
//                Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getParentFile().getParentFile().getAbsoluteFile());
//                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getPath());
//                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getParentFile().getPath());
//                Log.d(TAG, "ExportAccountDB: got path " + file.getParentFile().getParentFile().getParentFile().getPath());


                try {
                    if (file.createNewFile()) {
                        Log.d(TAG, "ExportAccountDB: file created " + file.getAbsoluteFile());
                    }
                } catch (IOException e1) {
                    msgError = e1.getMessage();
                    Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
                    Toast.makeText(this,
                            " unable to create Exported file",
                            Toast.LENGTH_LONG).show();
                    return;
//                    fvFragment.setInfoMessage("Exported directory not exists");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                    msgError = "jsonError: " + e2.getMessage();
                    Log.v(TAG, msgError);
                }


//                alertBackup(file, "Confirm backup");
                alertMsg("Backup Temporaruly Unavailable");
            }
//            JsonWriter writer = new JsonWriter(new FileWriter(file));
//            writer.setIndent("  ");
//            count = writeMessagesArray(writer);
//            writer.flush();
//            writer.close();
//
//            infoPage("Account Profiles exported");
//
//            Toast.makeText(this,
//                    count + " Exported accounts",
//                    Toast.LENGTH_LONG).show();

//        } catch (IOException e1) {
//            msgError = e1.getMessage();
//            Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
//            Toast.makeText(this,
//                    " Exported file directory issues",
//                    Toast.LENGTH_LONG).show();
////                    fvFragment.setInfoMessage("Exported directory not exists");
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            msgError = "jsonError: " + e2.getMessage();
            Log.v(TAG, msgError);
            Toast.makeText(this, "Export has errors", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertMsg(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
//                        new DownloadProfileAsyncTask(getApplicationContext(),
//                                file, adapter, progressBar, webView).execute();
//
                        //                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
                    }
                });
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void alertBackup(File file, String msg) {


//        new DownloadProfileAsyncTask(getApplicationContext(),
//                file, adapter, progressBar, webView).execute();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        new DownloadProfileAsyncTask(getApplicationContext(),
                                file, adapter, progressBar, webView).execute();

                        //                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void ImportAccountDB() {
        Log.d(TAG, "ImportAccountDB: starts");

        try {
            File dirStorage = getExternalFilesDir("passport");
            String storageFilename = dirStorage.getAbsolutePath() + "/" + MainActivity.BACKUP_FILENAME;

//            Log.d(TAG, "call asyncTask");
//            new UploadProfileAsyncTask(getApplicationContext(),
//                    this.webView, this.progressBar).execute(storageFilename);

//            new UploadProfileAsyncTask(getApplicationContext(),
//                    webView, progressBar).execute(storageFilename);

            alertMsg("Restore Temporaruly Unavailable");

//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("Confirm Restore of backup file onto data DB");
//            alertDialogBuilder.setPositiveButton("yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            new UploadProfileAsyncTask(getApplicationContext(),
//                                    webView, progressBar).execute(storageFilename);
//                        }
//                    })
//                    .setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            });
//
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



//    final private void addAccountToDB(ContentResolver contentResolver, Account account) {
//
//        ContentValues values = new ContentValues();
//        values.put(AccountsContract.Columns.PASSPORT_ID_COL, account.getPassportId());
//        values.put(AccountsContract.Columns.CORP_NAME_COL, account.getCorpName());
//        values.put(AccountsContract.Columns.USER_NAME_COL, account.getUserName());
//        values.put(AccountsContract.Columns.USER_EMAIL_COL, account.getUserEmail());
//        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, account.getCorpWebsite());
//        values.put(AccountsContract.Columns.NOTE_COL, account.getNote());
//        values.put(AccountsContract.Columns.OPEN_DATE_COL, account.getOpenLong());
//        values.put(AccountsContract.Columns.ACTVY_DATE_COL, account.getActvyLong());
//        values.put(AccountsContract.Columns.REF_FROM_COL, account.getRefFrom());
//        values.put(AccountsContract.Columns.REF_TO_COL, account.getRefTo());
//        values.put(AccountsContract.Columns.SEQUENCE_COL, account.getSequence());
//        contentResolver.insert(AccountsContract.CONTENT_URI, values);
//
//    }


    private void showFilename() {
        File fileExternal = new File(getExternalFilesDir("passport")
                + "/" + MainActivity.BACKUP_FILENAME);

//        Log.d(TAG, "showFilename: " + Environment.getDataDirectory() + "/passport/" + MainActivity.BACKUP_FILENAME);
//        Log.d(TAG, "showFilename: " + getExternalFilesDir(null) + "/passport/" + MainActivity.BACKUP_FILENAME);
//        Log.d(TAG, "showFilename: " + getExternalFilesDir("passport/") + MainActivity.BACKUP_FILENAME);
        String displayMsg = getExternalFilesDir("passport") + "/" + MainActivity.BACKUP_FILENAME;
        if (fileExternal.exists()) {
            displayMsg += " \ncreated " + format_mdy.format(fileExternal.lastModified());
        }


        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(displayMsg);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
//                        profileViewModel.delete(profile);
                    }
                });
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                refreshDeletePos(position);
//                            }
//                        });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();




//        AppDialog dialog = new AppDialog();
//        Bundle args = new Bundle();
//        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_EXPORT_FILENAME);
//        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_OK);
//        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_export_filename));
//        args.putString(AppDialog.DIALOG_SUB_MESSAGE, displayMsg);
//        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.ok);
//
//        dialog.setArguments(args);
//        dialog.show(getSupportFragmentManager(), null);

    }


    public void composeEmail() {

        File dirStorage = getExternalFilesDir("passport/");
        File file = new File(dirStorage, MainActivity.BACKUP_FILENAME);
        if (!file.exists()) {
            Toast.makeText(FileViewActivity.this,
                    "No Exported File to share",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String[] addresses = {"vonniekinsey@gmail.com"};
        String subject = "You Account Json";
//        Uri uri = Uri.fromFile(file);
//        Uri uri = Uri.parse(file.getAbsolutePath());
//        FileProvider fp = new FileProvider();
        Uri uri = getUriForFile(getApplicationContext(),
                "com.kinsey.passwords.fileprovider", file);
//                Uri.parse(file.getAbsolutePath());

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.setType("*/*");
//        intent.setType("message/rfc822")
//        intent.setType("text/json");
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Keep Accounts backup in secure place for any future restores.");
        try {
            startActivityForResult(intent.createChooser(intent, "Share accounts data"),
                    BACKUP_FILE_REQUESTED);
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "error: " + ex.getMessage());
        }
    }
//    private void shareExport() {
//        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
//        dlg.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
//        dlg.setTitle(getResources().getString(R.string.app_name))
//                .setMessage("Is the exported file up-to-date for this share.")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        shareIntent();
//                        // finish dialog
//                        dialog.dismiss();
//                        return;
//                    }
//
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        // finish dialog
//                        dialog.dismiss();
//                        return;
//                    }
//
//                })
//                .show();
//        dlg = null;
//
//    }

//    private void shareIntent() {
//
//        Intent emailintent = new Intent(Intent.ACTION_SEND);
//        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App");
////        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
////        ArrayList<Uri> uris = new ArrayList<Uri>();
//        emailintent.setType("text/html");
//
//        File dirStorage = getExternalFilesDir("passport/");
//        File file = new File(dirStorage, MainActivity.BACKUP_FILENAME);
//        if (!file.exists()) {
//            Toast.makeText(FileViewActivity.this,
//                    "No Exported File to share",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App - import/export file");
////                Uri u = Uri.fromFile(file);
////                uris.add(u);
//        emailintent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        emailintent.putExtra(Intent.EXTRA_TEXT, "Exported JSON file");
//
//        emailintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////        emailintent.putExtra(Intent.EXTRA_TEXT, "My Accounts Attachments");
//
//
//        myShareActionProvider.setShareIntent(emailintent);
////        try {
////            startActivity(Intent.createChooser(emailintent, "Send your accounts.json..."));
////        } catch (ActivityNotFoundException e) {
////            Toast.makeText(FileViewActivity.this,
////                    "Unable to get the shared menu",
////                    Toast.LENGTH_LONG).show();
////        }
//
////        Toast.makeText(FileViewActivityV1.this,
////                "Exported file shared sent",
////                Toast.LENGTH_SHORT).show();
//
//    }


//
//    public int writeMessagesArray(JsonWriter writer) throws IOException {
//        int count = 0;
//        try {
//
////            List<Account> listAccounts = loadAccounts();
//
////            Log.d(TAG, "writeMessagesArray: " + listAccounts);
//
//            writer.beginArray();
//            for (Profile item : MainActivity.adapter.getCurrentList()) {
////            for (Account item : listAccounts) {
//                writeMessage(writer, item);
//                count++;
//            }
//            writer.endArray();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//            System.out.println(e2.getMessage());
//            Log.e(TAG, "writeMessageArrayError: " + e2.getMessage());
//        }
//        return count;
//    }


//    public void writeMessage(JsonWriter writer, Profile item)
//            throws IOException {
//        try {
//            writer.beginObject();
//            writer.name("corpName").value(item.getCorpName());
//            writer.name("accountId").value(item.getPassportId());
//            writer.name("seq").value(item.getSequence());
//            writer.name("userName").value(item.getUserName());
//            writer.name("userEmail").value(item.getUserEmail());
//            writer.name("refFrom").value(item.getRefFrom());
//            writer.name("refTo").value(item.getRefTo());
//            if (item.getCorpWebsite() == null) {
//                writer.name("website").nullValue();
//            } else {
//                writer.name("website").value(item.getCorpWebsite().toString());
//            }
//            if (item.getOpenLong() == 0) {
//                writer.name("openDt").nullValue();
//            } else {
//                writer.name("openDt").value(
//                        format_ymdtime.format(item.getOpenLong()));
//            }
//            if (item.getActvyLong() == 0) {
//                writer.name("actvyDt").nullValue();
//            } else {
//                writer.name("actvyDt").value(
//                        format_ymdtime.format(item.getActvyLong()));
//            }
//            Log.d(TAG, "writeMessage: note " + item.getNote());
//            writer.name("note").value(item.getNote());
//
//            writer.endObject();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//            System.out.println(e2.getMessage());
//            Log.v(TAG, "writeMessageError: " + e2.getMessage());
//        }
//    }


//    List<Account> loadAccounts() {
////        Log.d(TAG, "loadAccounts: starts ");
//        Cursor cursor = getContentResolver().query(
//                AccountsContract.CONTENT_URI, null, null, null,
//                String.format("%s COLLATE NOCASE ASC, %s COLLATE NOCASE ASC", AccountsContract.Columns.CORP_NAME_COL, AccountsContract.Columns.SEQUENCE_COL));
////                        AccountsContract.Columns.CORP_NAME_COL);
//
//        List<Account> listAccounts = new ArrayList<Account>();
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Account item = AccountsContract.getAccountFromCursor(cursor);
////                        Account item = new Account(
////                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
////                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
////
////                        if (cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
////                                item.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
////                                item.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL))) {
////                                item.setActvyLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL))) {
////                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL))) {
////                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL)));
////                            }
////                        }
//                listAccounts.add(item);
//            }
//            cursor.close();
//        }
//
//        return listAccounts;
//    }

//    @Override
//    public void onPositiveDialogResult(int dialogId, Bundle args) {
//
//    }
//
//    @Override
//    public void onNegativeDialogResult(int dialogId, Bundle args) {
//
//    }
//
//    @Override
//    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
//
//    }
//
//    @Override
//    public void onDialogCancelled(int dialogId) {
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BACKUP_FILE_REQUESTED) {
            if (resultCode == RESULT_OK) {
                // success
                Log.d(TAG, "file share was successful");
            }
            else if (resultCode == RESULT_CANCELLED) {
                // cancelled
                Log.d(TAG, "file share cancelled");
            }
        }
    }

    private static class UploadProfileAsyncTask extends AsyncTask<String, Void, String> {
//        private ProfileDao profileDao;
        ProfileJsonListIO profileJsonListIO = new ProfileJsonListIO();
        private List<Profile> listAccounts = new ArrayList<Profile>();
        Context context;
        private WebView webView;
        private ProgressBar progressBar;

        private UploadProfileAsyncTask(Context context,
                                       WebView webView,
                                       ProgressBar progressBar) {
            this.context = context;
            this.webView = webView;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {

            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... filename) {
//            profileDao.update(profiles[0]);
//            File dirStorage = getExternalFilesDir("passport");

            String msgDisplay = "No upload / updates";
            try {

                Log.d(TAG, "upload file " + filename[0]);

                listAccounts = profileJsonListIO.readProfileJson(filename[0]);

                Log.d(TAG, "run: upload size " + listAccounts.size());

                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb, Locale.US);

                msgDisplay = formatter.format("<h2>%3d Account Profiles Upload</h2>",
                        listAccounts.size()).toString() +
                        "<h3>App DB Updated, restored from backup file</h3>";
//                publishProgress(msgDisplay);
//
//                publishProgress("<h2>%3d Account Profile uploaded</h2>" );
//
//                publishProgress(msgDisplay + "<h3>App DB Updated</h3>");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return msgDisplay;
        }

//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
////            WebView webView = (WebView)FileViewActivity.this.findViewById(R.id.wv_page);
////            FileViewActivity.infoPage("Account Profiles exported");
//            FileViewActivity.webView.loadData(values[0], "text/html", null);
//        }

        @Override
        protected void onPostExecute(String msgDisplay) {
            super.onPostExecute(msgDisplay);

            this.webView.loadData(msgDisplay, "text/html", null);

            MainActivity.profileViewModel.deleteAllProfiles();
            Log.d(TAG, "run: delete all complete ");

            MainActivity.profileViewModel.insertAll(listAccounts);
            Log.d(TAG, "run: upload complete ");


            Toast.makeText(context, "Upload restore complete", Toast.LENGTH_SHORT).show();

            this.progressBar.setVisibility(View.INVISIBLE);

        }

    }


    private static class DownloadProfileAsyncTask extends AsyncTask<Void, String, Integer> {
        //        private ProfileDao profileDao;
        ProfileJsonListIO profileJsonListIO = new ProfileJsonListIO();
        Context context;
        File storageFile;
        ProfileAdapter adapter;
        String msgError;
        private ProgressBar progressBar;
        private WebView webView;

        private DownloadProfileAsyncTask(Context context,
                                         File file,
                                         ProfileAdapter adapter,
                                         ProgressBar progressBar,
                                         WebView webView) {
            this.context = context;
            this.storageFile = file;
            this.adapter = adapter;
            this.progressBar = progressBar;
            this.webView = webView;

//            File dirStorage = context.getExternalFilesDir("passport");
        }

        @Override
        protected void onPreExecute() {

            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int count = 0;
//            profileDao.update(profiles[0]);
//            File dirStorage = getExternalFilesDir("passport");

            try {

//                Log.d(TAG, "ExportAccountDB: path found " + storageFile.getPath());
//                // Make sure the Pictures directory exists.
//                if (!dirStorage.exists()) {
//                    dirStorage.mkdirs();
//                }
//
//                File file = new File(dirStorage, MainActivity.BACKUP_FILENAME);
//                Log.d(TAG, "ExportAccountDB export filename: " + file.getAbsoluteFile());
//
//
//                if (file.exists()) {
//                    Log.d(TAG, "ExportAccountDB: file exists " + file.getAbsoluteFile());
//                } else {
//
//                    Log.d(TAG, "ExportAccountDB: file " + file.getAbsolutePath());
//
//
//                    if (file.createNewFile()) {
//                        Log.d(TAG, "ExportAccountDB: file created " + file.getAbsoluteFile());
//                    }
//
//                }

                Log.d(TAG, "upload file " + this.storageFile.getAbsolutePath());


//                count = profileJsonListIO.writeProfileJson(this.storageFile);
//
//                JsonWriter writer = new JsonWriter(new FileWriter(file));
//                writer.setIndent("  ");
//                count = writeMessagesArray(writer);
//                writer.flush();
//                writer.close();


                List<Profile> profiles = adapter.getCurrentList();
                count = profileJsonListIO.writeProfileJson(this.storageFile, profiles);


                Log.d(TAG, "run: upload size " + count);

                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb, Locale.US);

                publishProgress(formatter.format("<h2>%3d Account Profiles Download to backup file</h2>" +
                                "<h3>See menu for export filename</h3>" +
                                "<h3>See menu to view exported file</h3>",
                        count).toString());
//            } catch (IOException e1) {
//                msgError = "jsonFile Error: " + e1.getMessage();
//                Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
////                    fvFragment.setInfoMessage("Exported directory not exists");
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println(e2.getMessage());
                msgError = "jsonError: " + e2.getMessage();
                Log.v(TAG, msgError);
            }

            return count;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            FileViewActivity.webView.loadData(values[0], "text/html", null);
        }


        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);

            if (msgError != null) {
                Toast.makeText(context,
                        msgError,
                        Toast.LENGTH_LONG).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            String msgDisplay = formatter.format("<h2>%3d Account Profiles onto Backup file</h2>",
                    count).toString();

            this.webView.loadData(msgDisplay, "text/html", null);

//            FileViewActivity.infoPage("Account Profiles exported");

            Toast.makeText(context,
                    count + " Exported accounts to backup file",
                    Toast.LENGTH_LONG).show();

            this.progressBar.setVisibility(View.INVISIBLE);

        }

    }


}
