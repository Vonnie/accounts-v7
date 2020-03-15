package com.kinsey.passwords;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import com.aditya.filebrowser.Constants;
//import com.aditya.filebrowser.FileBrowser;
//import com.aditya.filebrowser.FileChooser;
//import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.tools.ProfileJsonListIO;
//import com.nbsp.materialfilepicker.MaterialFilePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
//import java.util.regex.Pattern;
//import com.codekidlabs.storagechooser.StorageChooser;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import static androidx.core.content.FileProvider.getUriForFile;

public class FileViewActivity extends AppCompatActivity {
//        implements AppDialog.DialogEvents {
    private static final String TAG = "FileViewActivity";

    public static final String EXTRA_LIST_RESTORED =
            "com.kinsey.passwords.EXTRA_LIST_RESTORED";

    private ProgressBar progressBar;
    private WebView webView;
    private ProfileAdapter adapter;
    private ProfileViewModel profileViewModel;

//    private Handler mHandler = new Handler();
//    boolean importRefreshReq = false;
//    public static File dirStorage;
//    View fileViewActivityView;
//    ShareActionProvider myShareActionProvider;
    boolean onFirstReported = true;
    boolean blnListRestored = false;
    private static final int BACKUP_FILE_REQUESTED = 1;
    private static final int PICK_FILE_SHOW = 2;
    private static final int PICK_FILE_RESTORE = 3;

    private AdView mAdView;


    private static String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

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
        setTitle(R.string.menuacct_backup_restore_accts);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_test2_foreground);

//        Log.d(TAG, "onCreate: permission " +
//                PackageInfo.REQUESTED_PERMISSION_GRANTED
//                );
//        if (PackageInfo.REQUESTED_PERMISSION_GRANTED == PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY) {
//            Log.d(TAG, "onCreate: permission internal only ");
//        }
//        if (PackageInfo.REQUESTED_PERMISSION_GRANTED == PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL) {
//            Log.d(TAG, "onCreate: permission external ");
//        }
//
//        Log.d(TAG, "onCreate: dir " + getFilesDir());
//        Log.d(TAG, "onCreate: dir " + getExternalFilesDir(""));
//
//        Log.d(TAG, "onCreate: uris " + getContentResolver().getPersistedUriPermissions());

//        if (getExternalFilesDir("").exists()) {
//            msgDialog("External Storage exists");
//        }
        if (!checkPermission()) {
            msgWarningDialog("This activity requires App permission.\nSee screen for Grant Permission Instruction");
        }
        webView = findViewById(R.id.wv_page);
        final WebSettings webSettings = webView.getSettings();
        Resources res = getResources();
        float fontSize = res.getInteger(R.integer.font_size);
//        fontSize = res.getDimension(R.dimen.txtSize);
        webSettings.setDefaultFontSize((int)fontSize);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '1px'})();");
                Log.d(TAG, "onPageFinished: ");
            }

        });


        adapter = new ProfileAdapter(-1);
        this.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
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

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);


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
                Log.d(TAG, "onOptionsItemSelected: Report");
//                reportFile();
                showFilename();
                break;

            case R.id.vw_export:
                Log.d(TAG, "onOptionsItemSelected: Export");
                backupFilename();
//                ExportAccountDB();
                break;

            case R.id.vw_import:
                Log.d(TAG, "onOptionsItemSelected: Import");
                restoreByFileChooser();
//                restoreFilename();
//                ImportAccountDB();
                break;

//            case R.id.vw_filename:
//                Log.d(TAG, "onOptionsItemSelected: Share View filename");
//                showFilename();
//                break;

            case R.id.vw_shared:
                email_Dialog();
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
        Intent data = new Intent();
        data.putExtra(EXTRA_LIST_RESTORED, blnListRestored);
        setResult(RESULT_OK, data);
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
                        Log.d(TAG, "infoPage: " + fileExternal.getAbsolutePath());
                        htmlString = greetMsg() +
                                "<hr><h5>" + adapter.getItemCount() + " " + getString(R.string.fv_msg_1) + "<h5>" +
                                "<h5>" + accountJsonProperties(fileExternal.getAbsoluteFile().toString()) + "</h5><hr>" +
                                "<h5>" + getString(R.string.fv_msg_49) + "</h5>" +
                                "<h6>" + getString(R.string.fv_msg_2) + "</h6>" +
                                "<ul>" +
                                        "<li>" + getString(R.string.fv_msg_3) + "</li>" +
                                        "<li>" + getString(R.string.fv_msg_4) + "</li>" +
                                        "<li>" + getString(R.string.fv_msg_5) + " <small>" + fileExternal.getAbsolutePath() + "</small></li>" +
                                        "<li>" + getString(R.string.fv_msg_46) + "</li>" +
                                        "<li>" + getString(R.string.fv_msg_47) + "</li>" +
                                        "<li>" + getString(R.string.fv_msg_48) + "</li>" +
                                        "</ul>" ;
                    } else {
                        htmlString = notfyMsg(dirStorage.getAbsoluteFile().getAbsolutePath()) +
                                "<br><h4>" + adapter.getItemCount() + " " + getString(R.string.fv_msg_7) + "<h4>";
//                                "<h4>" + getString(R.string.fv_msg_6) + "</h4>" +
//                        "<h4>Notify</h4>" +
//                                "<h4>" + getString(R.string.fv_msg_8) + "</h4>";
                    }
                } else {
                    htmlString = notfyMsg(dirStorage.getAbsoluteFile().getAbsolutePath()) +
//                            "<h4>" + getString(R.string.fv_msg_9) + dirStorage.getAbsoluteFile() + "</h4>" +
//                            "<h5>" + getString(R.string.fv_msg_10) + "</h5>" +
                            "<br><h5>" + adapter.getItemCount() + " " + getString(R.string.fv_msg_11) + "<h5>";
                }

            } else {
                htmlString = permissionMsg() +
                        "<h5>" + adapter.getItemCount() + " " + getString(R.string.fv_msg_12) + "<h5>";
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
        String htmlString = "<h1>" + getString(R.string.fv_msg_13) + "</h1>" +
                "<h2>" + getString(R.string.fv_msg_14) + "</h2>" +
                "<h3>" + getString(R.string.fv_msg_15) + "</h3>";
        return htmlString;
    }

    private String notfyMsg(String filename) {
        String htmlString = "<h1>" + getString(R.string.fv_msg_16) + "</h1>" +
                "<h2>" + getString(R.string.fv_msg_17) + "</h2>" +
                "<h5>" + String.format(getString(R.string.fv_msg_18), filename) + "</h5>";
        return htmlString;
    }

    private String greetMsg() {
        String htmlString = "<h1>" + getString(R.string.fv_msg_19) + "</h1>" +
                "<h2>" + getString(R.string.fv_msg_20) + "</h2>" +
                "<h3>" + getString(R.string.fv_msg_21) + "</h3>" ;
//                "<h4>" + getString(R.string.fv_msg_22) + "</h4>";
        return htmlString;
    }

    private String permissionMsg() {
        String htmlString = "<h1>" + getString(R.string.fv_msg_23) + "</h1>" +
                "<h2>" + getString(R.string.fv_msg_24) + "</h2>" +
                "<h3>" + getString(R.string.fv_msg_25) + "</h3>" +
//                "<h3>" + getString(R.string.fv_msg_26) + "</h3>" +
//                "<h3>" + getString(R.string.fv_msg_27) + "</h3>" +

                "<ul><li>" + getString(R.string.gotoSettings) + "</li>" +
                "<li>" + getString(R.string.selectapps) + "</li>" +
                "<li>" + getString(R.string.selectappaccounts) + "</li>" +
                "<li>" + getString(R.string.selectpermissions) + "</li>" +
                "<li>" + getString(R.string.allowstorage) + "</li>" +
                "<li>" + getString(R.string.fv_msg_28) + "</li></ul>" ;


//                "<h4>" + getString(R.string.fv_msg_29) + "</h4>" +
//                "<h4>" + getString(R.string.fv_msg_30) + "</h4>";
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
        }

        return returnValue;

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
        alertDialogBuilder.setMessage(getString(R.string.fv_msg_31) + "\n" + getString(R.string.fv_msg_32));
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


    private void ExportAccountDB(String strFilename) {
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


            File file = new File(dirStorage, strFilename);
            Log.d(TAG, "ExportAccountDB export filename: " + file.getAbsoluteFile());


            if (file.exists()) {
                Log.d(TAG, "ExportAccountDB: file exists " + file.getAbsoluteFile());
//                alertMsg("Backup Temporaruly Unavailable");
                alertBackup(file, getString(R.string.fv_msg_33)
                        + format_mdy.format(file.lastModified()));
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
                            R.string.toast_error_backup_create,
                            Toast.LENGTH_LONG).show();
                    return;
//                    fvFragment.setInfoMessage("Exported directory not exists");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                    msgError = "jsonError: " + e2.getMessage();
                    Log.v(TAG, msgError);
                }


                alertBackup(file, getString(R.string.fv_msg_34));
//                alertMsg("Backup Temporaruly Unavailable");
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
            Toast.makeText(this, R.string.toast_export_error, Toast.LENGTH_SHORT).show();
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


    public void email_Dialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FileViewActivity.this);

        //  Inflate the Layout Resource file you created in Step 1
        View mView = getLayoutInflater().inflate(R.layout.content_share_email, null);

        //  Get View elements from Layout file. Be sure to include inflated view name (mView)
        TextInputLayout textInputEmail = (TextInputLayout) mView.findViewById(R.id.text_input_email);
        Button btnOk = (Button) mView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mView.findViewById(R.id.btn_cancel);

        //  Create the AlertDialog using everything we needed from above
        mBuilder.setView(mView);
        final AlertDialog emailDialog = mBuilder.create();

        //  Set Listener for the OK Button
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String strEmail = textInputEmail.getEditText().getText().toString().trim();

                if (!strEmail.isEmpty()) {
                    composeEmail(strEmail);
                    emailDialog.dismiss();
//                    Toast.makeText(FileViewActivity.this, "You entered a Value!,", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FileViewActivity.this, R.string.toast_enter_email, Toast.LENGTH_LONG).show();
                }
            }
        });

        //  Set Listener for the CANCEL Button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                emailDialog.dismiss();
            }
        });

        //  Finally, SHOW your Dialog!
        emailDialog.show();


        //  END OF buttonClick_DialogTest
    }



    public void backupFilename() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FileViewActivity.this);

        //  Inflate the Layout Resource file you created in Step 1
        View mView = getLayoutInflater().inflate(R.layout.content_req_filename, null);

        //  Get View elements from Layout file. Be sure to include inflated view name (mView)
        TextView tvTitle = mView.findViewById(R.id.title);
        File dirStorage = getExternalFilesDir("passport");
        // Make sure the Pictures directory exists.
        if (dirStorage.exists()) {
            tvTitle.setText(getString(R.string.fv_msg_50) + dirStorage.getAbsolutePath());
        } else {
            tvTitle.setText(getString(R.string.fv_msg_50) + "");
        }
        TextInputLayout textInputFilename = (TextInputLayout) mView.findViewById(R.id.text_input_filename);
        textInputFilename.getEditText().setText(MainActivity.BACKUP_FILENAME);
        Button btnOk = (Button) mView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mView.findViewById(R.id.btn_cancel);

        //  Create the AlertDialog using everything we needed from above
        mBuilder.setView(mView);
        final AlertDialog filenameDialog = mBuilder.create();

        //  Set Listener for the OK Button
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String strFilename = textInputFilename.getEditText().getText().toString().trim();

                if (!strFilename.isEmpty()) {
//                    composeEmail(strFilename);
                    ExportAccountDB(strFilename);
                    filenameDialog.dismiss();
//                    Toast.makeText(FileViewActivity.this, "You entered a Value!,", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FileViewActivity.this, R.string.toast_enter_email, Toast.LENGTH_LONG).show();
                }
            }
        });

        //  Set Listener for the CANCEL Button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                filenameDialog.dismiss();
            }
        });

        //  Finally, SHOW your Dialog!
        filenameDialog.show();


        //  END OF buttonClick_DialogTest
    }



    private void alertBackup(File file, String msg) {


//        new DownloadProfileAsyncTask(getApplicationContext(),
//                file, adapter, progressBar, webView).execute();


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.diamsg_yesno);
        dialog.setTitle(msg);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_help_outline_black);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadProfileAsyncTask(getApplicationContext(),
                        file, adapter, progressBar, webView).execute();
                dialog.dismiss();
            }
        });

        Button dialogButtonNo = (Button) dialog.findViewById(R.id.no);
        // if button is clicked, close the custom dialog
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(msg);
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        new DownloadProfileAsyncTask(getApplicationContext(),
//                                file, adapter, progressBar, webView).execute();
//
//                        //                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .setNegativeButton(R.string.no,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }

    private void restoreByFileChooser() {
        openFileBrowser(PICK_FILE_RESTORE);

    }

    private void restoreFilename() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FileViewActivity.this);

        //  Inflate the Layout Resource file you created in Step 1
        View mView = getLayoutInflater().inflate(R.layout.content_req_filename, null);

        //  Get View elements from Layout file. Be sure to include inflated view name (mView)
        TextView tvTitle = mView.findViewById(R.id.title);
        tvTitle.setText("Restore from file");
        TextInputLayout textInputFilename = (TextInputLayout) mView.findViewById(R.id.text_input_filename);
        textInputFilename.getEditText().setText(MainActivity.BACKUP_FILENAME);
        Button btnOk = (Button) mView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mView.findViewById(R.id.btn_cancel);

        //  Create the AlertDialog using everything we needed from above
        mBuilder.setView(mView);
        final AlertDialog filenameDialog = mBuilder.create();

        //  Set Listener for the OK Button
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String strFilename = textInputFilename.getEditText().getText().toString().trim();

                if (!strFilename.isEmpty()) {
                    ImportAccountDB(strFilename);
//                    composeEmail(strFilename);
//                    ExportAccountDB(strFilename);
                    filenameDialog.dismiss();
//                    Toast.makeText(FileViewActivity.this, "You entered a Value!,", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FileViewActivity.this, R.string.toast_enter_email, Toast.LENGTH_LONG).show();
                }
            }
        });

        //  Set Listener for the CANCEL Button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                filenameDialog.dismiss();
            }
        });

        //  Finally, SHOW your Dialog!
        filenameDialog.show();


        //  END OF buttonClick_DialogTest

    }

    private void ImportAccountDB(String strFilename) {
        Log.d(TAG, "ImportAccountDB: starts");

        try {
//            File dirStorage = getExternalFilesDir("passport");
//            String storageFilename = dirStorage.getAbsolutePath() + "/" + strFilename;

//            Log.d(TAG, "call asyncTask");
//            new UploadProfileAsyncTask(getApplicationContext(),
//                    this.webView, this.progressBar).execute(storageFilename);

//            new UploadProfileAsyncTask(getApplicationContext(),
//                    webView, progressBar).execute(storageFilename);

//            alertMsg("Restore Temporaruly Unavailable");


            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.diamsg_yesno);
            dialog.setTitle(getString(R.string.fv_msg_45)) ;


            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(String.format(getString(R.string.fv_msg_35), strFilename));
            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            image.setImageResource(R.drawable.ic_help_outline_black);


            Button dialogButton = (Button) dialog.findViewById(R.id.yes);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UploadProfileAsyncTask(getApplicationContext(),
                            webView, progressBar, profileViewModel).execute(strFilename);
                    blnListRestored = true;
                    dialog.dismiss();
                }
            });

            Button dialogButtonNo = (Button) dialog.findViewById(R.id.no);
            // if button is clicked, close the custom dialog
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage(R.string.fv_msg_35);
//            alertDialogBuilder.setPositiveButton(R.string.yes,
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
            displayMsg += " \n" + getString(R.string.fv_msg_36) + " " + format_mdy.format(fileExternal.lastModified());
        }


//        msgDialog(displayMsg);


        openFileBrowser(PICK_FILE_SHOW);


//        openFileChooser();

//        showFileChooser();

//        requestFilePicker();

//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(displayMsg);
//        alertDialogBuilder.setPositiveButton("ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
////                        profileViewModel.delete(profile);
//                    }
//                });
////                .setNegativeButton("No",
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int which) {
////                                refreshDeletePos(position);
////                            }
////                        });
//
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//
//
//
////        AppDialog dialog = new AppDialog();
////        Bundle args = new Bundle();
////        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_EXPORT_FILENAME);
////        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_OK);
////        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_export_filename));
////        args.putString(AppDialog.DIALOG_SUB_MESSAGE, displayMsg);
////        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.ok);
////
////        dialog.setArguments(args);
////        dialog.show(getSupportFragmentManager(), null);

    }

    private void openFileBrowser(int requestCode) {
//        UtilsRG.info("Open filechooser to read a file");


        //Selecting the "EXAMES_APP" Folder as default
//        File root = android.os.Environment.getExternalStorageDirectory();
//        File dir = new File(root.getAbsolutePath() + "/Exames-App/");
//        File dir = new File(root.getAbsolutePath());
        File dir = getExternalFilesDir("passport") ;
        String directoryy = dir.toString() + "/";
//        msgDialog(directoryy);

        //Giving the FilePicker a custom Title:
        String title = "Select file";
        String readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
//        if(hasPermission(PERMISSION_REQUEST_CODE, readExternalStoragePermission)) {
            new MaterialFilePicker()
                    .withActivity(FileViewActivity.this)
                    .withRequestCode(requestCode)
                    .withFilter(Pattern.compile(".*\\.json$"))
                    .withFilterDirectories(true) // Set directories filterable (false by default)
                    .withHiddenFiles(false) // Show hidden files and folders
                    .withRootPath(directoryy)
                    .withTitle(title)
                    .start();
//        }
    }


//    private void startOpenDialog() {
//        Intent intent = new Intent(this, FilePickerActivity.class);
//        intent.putExtra(FilePickerActivity.ARG_FILE_FILTER, Pattern.compile(".*\\.txt$"));
//        intent.putExtra(FilePickerActivity.ARG_DIRECTORIES_FILTER, true);
//        intent.putExtra(FilePickerActivity.ARG_SHOW_HIDDEN, true);
//        startActivityForResult(intent, 1);
//    }


//    private void openFileChooser() {
////        Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
////        i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
////        startActivityForResult(i2, PICK_FILE_REQUEST);
//
//        Intent i = new Intent(this, FileBrowser.class); //works for all 3 main classes (i.e FileBrowser, FileChooser, FileBrowserWithCustomHandler)
//        i.putExtra(Constants.INITIAL_DIRECTORY, new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Movies").getAbsolutePath());
//        i.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "json");
//        startActivityForResult(i, PICK_FILE_REQUEST);
//
//    }


//    private void openFileChooser() {
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        // 1. Initialize dialog
//        final StorageChooser chooser = new StorageChooser.Builder()
//                // Specify context of the dialog
//                .withActivity(this)
//                .withFragmentManager(getSupportFragmentManager())
//                .withMemoryBar(true)
//                .allowCustomPath(true)
//                // Define the mode as the FILE CHOOSER
//                .setType(StorageChooser.FILE_PICKER)
//                .build();
//
//// 2. Handle what should happend when the user selects the directory !
//        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
//            @Override
//            public void onSelect(String path) {
//                // e.g /storage/emulated/0/Documents/file.txt
////                Log.i(path);
//                Log.d(TAG, "onSelect: path " + path);
//            }
//        });
//
//// 3. Display File Picker whenever you need to !
//        chooser.show();
//    }


//    public void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//
//        // Update with mime types
//        intent.setType("*/*");
//        String mimeTypes = "text/json";
//        // Update with additional mime types here using a String[].
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//
//        // Only pick openable and local files. Theoretically we could pull files from google drive
//        // or other applications that have networked files, but that's unnecessary for this example.
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//
//        // REQUEST_CODE = <some-integer>
//        startActivityForResult(intent, PICK_FILE_REQUEST);
//    }

//    private void requestFilePicker() {
//        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//        chooseFile.setType("*/*");
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//        startActivityForResult(chooseFile, PICK_FILE_REQUEST);
//    }

    private void msgDialog(String msg) {
        msgDialog(msg, R.drawable.ic_info_black_24dp);
    }

    private void msgWarningDialog(String msg) {
        msgDialog(msg, R.drawable.ic_warning);
    }


    public void msgDialog(String msg, int resid) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_msg_ok);
        dialog.setTitle("Account Modify Info");

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_info_black_24dp);
        image.setImageResource(resid);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void composeEmail(String email) {

        File dirStorage = getExternalFilesDir("passport/");
        File file = new File(dirStorage, MainActivity.BACKUP_FILENAME);
        if (!file.exists()) {
            Toast.makeText(FileViewActivity.this,
                    R.string.toast_no_export_to_share,
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        String[] addresses = {"vonniekinsey@gmail.com"};
        String[] addresses = {email};
        String subject = "Account Backup Json";
//        Uri uri = Uri.fromFile(file);
//        Uri uri = Uri.parse(file.getAbsolutePath());
//        FileProvider fp = new FileProvider();
        Uri uri = getUriForFile(getApplicationContext(),
                getString(R.string.fileprovider_package), file);
//                Uri.parse(file.getAbsolutePath());

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.setType("*/*");
//        intent.setType("message/rfc822")
//        intent.setType("text/json");
        intent.setType("application/octet-stream");
//        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, R.string.fv_msg_37);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivityForResult(intent.createChooser(intent, getString(R.string.fv_msg_38)),
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
            else if (resultCode == Activity.RESULT_CANCELED) {
                // cancelled
                Log.d(TAG, "file share cancelled");
            }
        } else {
            if (requestCode == PICK_FILE_SHOW) {
                if (resultCode == RESULT_OK) {
                    // success
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "file share was successful");
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    Log.d(TAG, "onActivityResult: path " + filePath);
//                    webView.setBackgroundColor(
//                            ContextCompat.getColor(getApplicationContext(), R.color.backgroundTransparent)

                    webView.loadUrl("file://" + filePath);
                    progressBar.setVisibility(View.INVISIBLE);
//                    Uri file = data.getData();
//                    Log.d(TAG, "onActivityResult: " + file.getPath());
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    // cancelled
                    Log.d(TAG, "file share cancelled");
                }
            } else {
                if (requestCode == PICK_FILE_RESTORE) {
                    if (resultCode == RESULT_OK) {
                        String strFilename = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                        Log.d(TAG, "onActivityResult: filename " + strFilename);
                        ImportAccountDB(strFilename);
                    }
                }
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
        private ProfileViewModel profileViewModel;

        private UploadProfileAsyncTask(Context context,
                                       WebView webView,
                                       ProgressBar progressBar,
                                       ProfileViewModel profileViewModel) {
            this.context = context;
            this.webView = webView;
            this.progressBar = progressBar;
            this.profileViewModel = profileViewModel;
        }

        @Override
        protected void onPreExecute() {

            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... filename) {
//            profileDao.update(profiles[0]);
//            File dirStorage = getExternalFilesDir("passport");

            String msgDisplay = context.getString(R.string.fv_msg_39);
            try {

                Log.d(TAG, "upload file " + filename[0]);

                listAccounts = profileJsonListIO.readProfileJson(filename[0]);

                Log.d(TAG, "run: upload size " + listAccounts.size());

                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb, Locale.US);

                msgDisplay = formatter.format("<h2>%3d " + context.getString(R.string.fv_msg_40) + "</h2>",
                        listAccounts.size()).toString() +
                        "<h3>" + context.getString(R.string.fv_msg_41) + "</h3>";
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

            this.profileViewModel.deleteAllProfiles();
            Log.d(TAG, "run: delete all complete ");

            this.profileViewModel.insertAll(listAccounts);
            Log.d(TAG, "run: upload complete ");


            Toast.makeText(context, R.string.toast_restore_complete, Toast.LENGTH_SHORT).show();

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

//                StringBuilder sb = new StringBuilder();
//                Formatter formatter = new Formatter(sb, Locale.US);
//
//                publishProgress(formatter.format("<h2>%3d Account Profiles Download to backup file</h2>" +
//                                "<h3>See menu for export filename</h3>" +
//                                "<h3>See menu to view exported file</h3>",
//                        count).toString());


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

            String msgDisplay = formatter.format("<h2>%3d " + context.getString(R.string.fv_msg_42) + "</h2>" +
                                "<h3>" + context.getString(R.string.fv_msg_43) + "</h3>" +
                                "<h3>" + context.getString(R.string.fv_msg_44) + "</h3>",
                    count).toString();

            this.webView.loadData(msgDisplay, "text/html", null);

//            FileViewActivity.infoPage("Account Profiles exported");

            Toast.makeText(context,
                    count + " " + context.getString(R.string.toast_accounts_backed_up),
                    Toast.LENGTH_LONG).show();

            this.progressBar.setVisibility(View.INVISIBLE);

        }

    }


}
