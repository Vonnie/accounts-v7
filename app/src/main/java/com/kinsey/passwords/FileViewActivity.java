package com.kinsey.passwords;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.tools.AppDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.DEFAULT_APP_DIRECTORY;
import static com.kinsey.passwords.MainActivity.format_ymdtime;

public class FileViewActivity extends AppCompatActivity
    implements FileViewActivityFragment.OnFileViewClickListener,
        AppDialog.DialogEvents {

    private static final String TAG = "FileViewActivity";

    FileViewActivityFragment fvFragment;
    boolean importRefreshReq = false;
    ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private ShareActionProvider myShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_suggest_list);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar  = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Share Exported file", Snackbar.LENGTH_LONG)
//                        .setAction("Send",
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        shareExport();
//
//                                    }
//                                }
//                                ).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fvFragment = (FileViewActivityFragment) fragmentManager.findFragmentById(R.id.json_fragment);
//        if (fvFragment != null) {
//            Log.d(TAG, "onCreate: found the fragment");
//            fvFragment.setInfoMessage("Able to connect");
//        }

        //
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager.findFragmentById(R.id.fragment) == null) {
//
//        }


//        new CountDownTimer(3000, 3000) {
//
//            public void onTick(long millisUntilFinished) {
////                    holder.corp_name.setText("checking db, seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//                progressBar  = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.GONE);
//            }
//        }.start();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("IMPORT", importRefreshReq);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.putExtra("IMPORT", importRefreshReq);
        setResult(RESULT_OK, intent);
        finish();
//        return super.onSupportNavigateUp();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_view, menu);


//        getMenuInflater().inflate(R.menu.share_menu, menu);

//        // Locate MenuItem with ShareActionProvider
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//
//        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

//        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
//        myShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

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

            case R.id.vw_export:
                Log.d(TAG, "onOptionsItemSelected: Export");
                ExportAccountDB();
                break;

            case R.id.vw_import:
                Log.d(TAG, "onOptionsItemSelected: Import");
                ImportAccountDB();
                break;

            case R.id.vw_filename:
                Log.d(TAG, "onOptionsItemSelected: View filename");
                showFilename();
                break;

            case R.id.menu_item_share:
                Log.d(TAG, "onOptionsItemSelected: View share");
                break;

            case android.R.id.home:
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentByTag("acctlistfrag");
//
//                if (listFragment != null) {
//                    Log.d(TAG, "onOptionsItemSelected: found account list fragment");
//                }

                Log.d(TAG, "onOptionsItemSelected: home button pressed");

                Intent intent = new Intent();
                intent.putExtra("IMPORT", importRefreshReq);
                setResult(RESULT_OK, intent);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (myShareActionProvider != null) {
            myShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void showFilename() {
        Log.d(TAG, "showFilename: " + MainActivity.DEFAULT_APP_DIRECTORY + "/accounts.json");
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_EXPORT_FILENAME);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_OK);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_export_filename));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, MainActivity.DEFAULT_APP_DIRECTORY + "/accounts.json");
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.ok);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }

    private void shareExport() {

        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
//        ArrayList<Uri> uris = new ArrayList<Uri>();
        emailintent.setType("text/html");


        File file = new File(MainActivity.DEFAULT_APP_DIRECTORY,
                "/accounts.json");
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
        startActivity(Intent.createChooser(emailintent, "Send Email..."));

        Toast.makeText(FileViewActivity.this,
                "Exported file shared",
                Toast.LENGTH_SHORT).show();

    }


    public String ExportAccountDB() {
        String msgError = "";
        int count = -1;


////            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
//
//            String jsonDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//
//            File folder = new File(jsonDownload);
//            if (!folder.exists()) {
////                Log.d(TAG, "ExportAccountDB: create download " + jsonDownload);
////                 create directory
//                folder.mkdirs();
//                Log.v(TAG, "dirCreated " + DEFAULT_APP_DIRECTORY);
//            }
//
////            Log.v(TAG, "default dir " + DEFAULT_APP_DIRECTORY);

//            File folder = new File(DEFAULT_APP_DIRECTORY);
//            if (!folder.exists()) {
////                Log.d(TAG, "ExportAccountDB: create download passport " + DEFAULT_APP_DIRECTORY);
//                // create directory
//                folder.mkdirs();
//                Log.v(TAG, "dirCreated " + DEFAULT_APP_DIRECTORY);
//            }


//            String jsonFile = DEFAULT_APP_DIRECTORY
//                    + "/accounts.json";
//
////            Log.d(TAG, "ExportAccountDB: accounts.json " + jsonFile);
//
////			FileWriter fileWriter = new FileWriter(jsonFilePath);
////            File file = new File(jsonFile);
//            File fileDir = new File(DEFAULT_APP_DIRECTORY);
//
//            if (fileDir.exists()) {
//                Log.d(TAG, "ExportAccountDB: dir exists " + fileDir.getAbsoluteFile());
//            } else {
//                Log.d(TAG, "ExportAccountDB: dir not exists " + fileDir.getAbsoluteFile());
//            }
//            File dirAccounts;
//            try {
//                dirAccounts = new File(DEFAULT_APP_DIRECTORY);
//                File file = new File(DEFAULT_APP_DIRECTORY, "accounts.json");
//                if (file.exists()) {
//                    Log.d(TAG, "ExportAccountDB: dir found");
//                }
//            } catch (IOException ioexc) {
//                if (!dirAccounts.exists()) {
//                    dirAccounts.mkdirs();
//                }
//            }

        File file = new File(DEFAULT_APP_DIRECTORY, "accounts.json");

        if (file.exists()) {
//                Log.d(TAG, "ExportAccountDB: file exists");
        } else {

            Log.d(TAG, "ExportAccountDB: got file " + file.getAbsoluteFile());
            Log.d(TAG, "ExportAccountDB: got dir " + file.getParentFile().getParentFile().getParentFile().getAbsoluteFile());
            if (file.getParentFile().getParentFile().getParentFile().exists()) {
                Log.d(TAG, "ExportAccountDB: parent dir found " + file.getParentFile().getParentFile().getParentFile().getAbsoluteFile());
            } else {
                if (file.getParentFile().getParentFile().getParentFile().mkdirs()) {
                    Log.d(TAG, "ExportAccountDB: dirs made " + file.getParentFile().getParentFile().getParentFile().getAbsoluteFile());
                }
            }

            if (file.getParentFile().getParentFile().exists()) {
                Log.d(TAG, "ExportAccountDB: parent dir found " + file.getParentFile().getParentFile().getAbsoluteFile());
            } else {
                if (file.getParentFile().getParentFile().mkdirs()) {
                    Log.d(TAG, "ExportAccountDB: dirs made " + file.getParentFile().getParentFile().getAbsoluteFile());
                }
            }

            if (file.getParentFile().exists()) {
                Log.d(TAG, "ExportAccountDB: parent dir found " + file.getParentFile().getAbsoluteFile());
            } else {
                if (file.getParentFile().mkdirs()) {
                    Log.d(TAG, "ExportAccountDB: dirs made " + file.getParentFile().getAbsoluteFile());
                }
            }

            //                Log.d(TAG, "ExportAccountDB: file does not exists");
//                if (file.isDirectory()) {
//                    Log.d(TAG, "ExportAccountDB: file is dir");
//                }
            Log.d(TAG, "ExportAccountDB: file " + file.getAbsoluteFile());

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
        ;
//                Log.d(TAG, "ExportAccountDB: file created");

//                OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
//                Log.d(TAG, "ExportAccountDB: fos created");
//                writeJson(fos);
//                Log.d(TAG, "ExportAccountDB: fos written & closed");

//
//            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
//            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

//            FileWriter fos = new FileWriter(jsonFile);
//            fos.write(response);


        try {
//			JsonWriter writer = new JsonWriter(fileWriter);
//            JsonWriter writer = new JsonWriter(out);
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            count = writeMessagesArray(writer);
            writer.flush();
            writer.close();
            fvFragment.reportJson();
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

//        Log.d(TAG, "ExportAccountDB: return msg " + msgError);
        if (count != -1) {

            Toast.makeText(this,
                    count + " Exported accounts",
                    Toast.LENGTH_LONG).show();
        }
        return msgError;
    }


    private void ImportAccountDB() {
        Log.d(TAG, "ImportAccountDB: starts");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


//        final String msg = "";
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                List<Account> listAccounts = new ArrayList<Account>();
//
//                mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d(TAG, "Runnable: post");
//                }
//            });
//        };


        new Thread(new Runnable() {
            @Override
            public void run() {

                String msg = "";
                boolean error = false;
                try {
                    List<Account> listAccounts = new ArrayList<Account>();
                    final JsonReader reader = new JsonReader(new FileReader(
                            MainActivity.DEFAULT_APP_DIRECTORY + "/accounts.json"));
                    // reader.beginObject();


                    //                Toast.makeText(this,
//                        " File of exported accounts not found",
//                        Toast.LENGTH_LONG).show();
//                fvFragment.setInfoMessage("Not able to find external file of exported accounts");
//                return;


//                    Log.d(TAG, "run: begin to read from file");
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Account account = readMessage(reader);
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

                    ContentResolver contentResolver = getContentResolver();
                    int deleteCount = getContentResolver().delete(AccountsContract.CONTENT_URI, null, null);
                    Log.d(TAG, "run: delete count " + deleteCount);

                    int itemCount = 0;
                    for (Account item : listAccounts) {
//                Log.d(TAG, "ImportAccountDB: acc " + item.getPassportId()
//                        + " " + item.getCorpName());
                        addAccountToDB(contentResolver, item);
                        itemCount++;
                        int remCount = itemCount % 80;
                        if (remCount == 0 || itemCount == 1) {
                            Log.d(TAG, "run: count " + itemCount);
                            final int runCount = itemCount;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    String notifyMsg;
                                    if (runCount == 1) {
                                        notifyMsg = "progress: db replace begins";
                                    } else {
                                        notifyMsg = String.format("progress: %s rows replaced", runCount);
                                    }
                                    Toast.makeText(getApplicationContext(),
                                            notifyMsg, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }

                    Log.d(TAG, "run: import count " + itemCount);

                    msg = listAccounts.size() + " Accounts Imported";

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    msg = "import file not found";
                    error = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = "import file error " + e.getMessage();
                    error = true;
//			savePassports();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    msg = "import exception";
                    error = true;
                }

                final String notifyMsg = msg;
                final boolean notifyError = error;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (notifyError) {

                            Toast.makeText(getApplicationContext(),
                                    notifyMsg, Toast.LENGTH_LONG).show();
                        } else {
                            importRefreshReq = true;
                            Intent intent = new Intent();
                            intent.putExtra("IMPORT", importRefreshReq);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }
                });
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

            final private Account readMessage(JsonReader reader) {
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

                    writer.beginArray();
                    for (Account item : listAccounts) {
                        writeMessage(writer, item);
                        count++;
                    }
                    writer.endArray();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                    Log.v(TAG, "writeMessageArrayError: " + e2.getMessage());
                }
                return count;
            }


            public void writeMessage(JsonWriter writer, Account item)
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
                        Account item = new Account(
                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));

                        if (cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
                                item.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
                            }
                        }
                        if (cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
                                item.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
                            }
                        }
                        if (cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL) != -1) {
                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL))) {
                                item.setActvyLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL)));
                            }
                        }
                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL) != -1) {
                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL))) {
                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL)));
                            }
                        }
                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL) != -1) {
                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL))) {
                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL)));
                            }
                        }
                        listAccounts.add(item);
                    }
                    cursor.close();
                }

                return listAccounts;
            }


//    private void loadSearchDB() {
//        deleteAllSearchItems();
//        AccountSearchLoaderCallbacks loaderAcctCallbacks = new AccountSearchLoaderCallbacks(this);
//        getLoaderManager().restartLoader(MainActivity.SEARCH_LOADER_ID, null, loaderAcctCallbacks);
//        Toast.makeText(this,
//                "Search Dictionary DB built",
//                Toast.LENGTH_LONG).show();
//
//    }
//
//
//    private void deleteAllSearchItems() {
////		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
////		String[] selectionArgs = { "account" };
////        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
//        getContentResolver().delete(
//                SearchesContract.CONTENT_URI_TRUNCATE,
//                null, null);
//
//    }


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

    @Override
    public void onFileViewShown() {
        progressBar  = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

}
