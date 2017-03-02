package com.kinsey.passwords;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;
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

public class AccountListActivity extends AppCompatActivity
        implements AccountRecyclerViewAdapter.OnAccountClickListener,
        AppDialog.DialogEvents {

    private static final String TAG = "AccountListActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private OnListClickListener mListener;

    public interface OnListClickListener {
        void onListSuggestsClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Request an action", Snackbar.LENGTH_LONG)
                        .setAction("See list",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Toast.makeText(AccountListActivity.this,
//                                                "Snackbar action clicked",
//                                                Toast.LENGTH_SHORT).show();
//                                        editAccountRequest(null);
//                                        mListener.onListSuggestsClick();

                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        AppDialog newFragment = AppDialog.newInstance(AppDialog.DIALOG_ACCOUNT_LIST_OPTIONS,
                                                getString(R.string.listdiag_acc_message));
                                        Bundle args = new Bundle();
                                        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ACCOUNT_LIST_OPTIONS);
                                        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.listdiag_acc_message));

                                        newFragment.setArguments(args);

                                        newFragment.show(fragmentManager, "dialog");

//                                        Intent returnIntent = new Intent();
//                                        returnIntent.putExtra("result", "open_date");
//                                        setResult(Activity.RESULT_OK, returnIntent);
//                                        finish();
                                    }
                                }
                        ).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        AccountListActivityFragment fragment = new AccountListActivityFragment();
//
//        Bundle arguments = getIntent().getExtras();
////        arguments.putSerializable(Task.class.getSimpleName(), task);
//        fragment.setArguments(arguments);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
////        android.support.v4.app.Fragment frag = fragmentManager.findFragmentById(R.id.fragmentEdit);
////        Log.d(TAG, "onCreate: frag " + frag);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.remove(frag);
////        fragmentTransaction.commit();
//        fragmentTransaction.replace(R.id.fragmentAccount, fragment);
//        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: starts");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: starts");
        super.onStop();
        Log.d(TAG, "onStop: ends");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: starts");
        super.onDestroy();
        Log.d(TAG, "onDestroy: ends");
    }

    public String ExportAccountDB() {
        String msgError = "";
        int count = -1;
        try {

            String jsonDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            File folder = new File(jsonDownload);
            if (!folder.exists()) {
                Log.d(TAG, "ExportAccountDB: create download " + jsonDownload);
//                 create directory
                folder.mkdirs();
                Log.v(TAG, "dirCreated " + DEFAULT_APP_DIRECTORY);
            }


            folder = new File(DEFAULT_APP_DIRECTORY);
            if (!folder.exists()) {
                Log.d(TAG, "ExportAccountDB: create download passport " + DEFAULT_APP_DIRECTORY);
                // create directory
                folder.mkdirs();
                Log.v(TAG, "dirCreated " + DEFAULT_APP_DIRECTORY);
            }


            String jsonFile = DEFAULT_APP_DIRECTORY
                    + "/accounts.json";

////			FileWriter fileWriter = new FileWriter(jsonFilePath);
//            File file = new File(jsonFilePath);
//
            Log.d(TAG, "ExportAccountDB: accounts.json " + jsonFile);
//
////            if (file.exists()) {
////                file.delete();
////            }
//
//            if (file.exists()) {
//                Log.d(TAG, "ExportAccountDB: file exists");
//            } else {
//                Log.d(TAG, "ExportAccountDB: file not exists");
//            }
//
//            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
//            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");


//			JsonWriter writer = new JsonWriter(fileWriter);
//            JsonWriter writer = new JsonWriter(out);
            JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
            writer.setIndent("  ");
            count = writeMessagesArray(writer);
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            msgError = e1.getMessage();
            Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            msgError = "jsonError: " + e2.getMessage();
            Log.v(TAG, msgError);
        }
        Log.d(TAG, "ExportAccountDB: return msg " + msgError);
        if (count != -1) {
            Toast.makeText(AccountListActivity.this,
                    count + " Exported accounts",
                    Toast.LENGTH_LONG).show();
        }
        return msgError;
    }

    public int writeMessagesArray(JsonWriter writer) throws IOException {
        int count = -1;
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
        Log.d(TAG, "loadAccounts: starts ");
        Cursor cursor = getContentResolver().query(
                AccountsContract.CONTENT_URI, null, null, null, AccountsContract.Columns.CORP_NAME_COL);

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

    private void ImportAccountDB() {
        Log.d(TAG, "ImportAccountDB: starts");
        String msg = "";
        try {
            List<Account> listAccounts = new ArrayList<Account>();
            JsonReader reader = new JsonReader(new FileReader(
                    MainActivity.DEFAULT_APP_DIRECTORY + "/accounts.json"));
            // reader.beginObject();

            reader.beginArray();
            while (reader.hasNext()) {
                Account account = readMessage(reader);
                if (account == null) {
                    break;
                } else {
                    listAccounts.add(account);
                }
            }
            reader.endArray();
            reader.close();

            ContentResolver contentResolver = getContentResolver();
            getContentResolver().delete(AccountsContract.CONTENT_URI, null, null);

            for (Account item : listAccounts) {
                Log.d(TAG, "ImportAccountDB: acc " + item.getPassportId()
                        + " " + item.getCorpName());
                addAccountToDB(contentResolver, item);
            }

            msg = listAccounts.size() + " Accounts Imported";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            msg = "import file not found";
        } catch (IOException e) {
            e.printStackTrace();
            msg = "import file error " + e.getMessage();
//			savePassports();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg = "import exception";
        }
        Toast.makeText(AccountListActivity.this,
                msg, Toast.LENGTH_LONG).show();

    }

    private void addAccountToDB(ContentResolver contentResolver, Account account) {

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

    private Account readMessage(JsonReader reader) {
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

    private void viewAccountsFile() {
        Intent detailIntent = new Intent(this, FileViewActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onAccountDeleteClick(Account account) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance(AppDialog.DIALOG_YES_NO, "Confirm account delete", account.getId());
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, 1, account.getCorpName()));
        args.putInt(AppDialog.DIALOG_ACCOUNT_ID, account.getId());
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        AppDialog newFragment = new AppDialog();
//        Bundle args = new Bundle();
//        args.putInt("id", 1);
//        args.putString("message", "hi from dialog");
//        newFragment.setArguments(args);
//        newFragment.show(fragmentManager, "dialog");


////        newFragment.show(getSupportFragmentManager(), "NoticeDialogFragment");
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
////        transaction.add(android.R.id.content, newFragment)
////                .addToBackStack(null).commit();

//        FragmentManager fm = getFragmentManager();
//        AppDialog dialogFragment = new AppDialog ();
//        dialogFragment.show(fm, "Sample Fragment");

//        Intent detailIntent = new Intent(this, AppDialog.class);
//        detailIntent.putExtra("id", 1);
//        detailIntent.putExtra("message", "hi from dialog");
//        startActivity(detailIntent);

//        AppDialog dialog = new AppDialog();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        dialog.show(fragmentManager, "NoticeDialogFragment");
//        getContentResolver().delete(AccountsContract.buildIdUri(account.getId()), null, null);
    }

    @Override
    public void onAccountEditClick(Account account) {
        editAccountRequest(account);
    }

    private void editAccountRequest(Account account) {
        Log.d(TAG, "addAccountRequest: starts");
        if (mTwoPane) {
            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
        } else {
            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
            // in single-pane mode, start the detail activity for the selected item Id.
            Intent detailIntent = new Intent(this, AccountActivity.class);

            if (account != null) { // editing an account
                detailIntent.putExtra(Account.class.getSimpleName(), account);
                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
            } else { // adding an account
                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_ADD);
            }
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: starts");
        Intent detailIntent = new Intent(this, FileViewActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: confirmed to delete");
        Log.d(TAG, "onPositiveDialogResult: acctid " + args.getInt(AppDialog.DIALOG_ACCOUNT_ID));
        getContentResolver().delete(AccountsContract.buildIdUri(args.getInt(AppDialog.DIALOG_ACCOUNT_ID)), null, null);
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: delete cancel");
    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
        Log.d(TAG, "onActionRequestDialogResult: starts");

        Intent returnIntent;
        switch (which) {
            case 0:
                editAccountRequest(null);
                break;
            case 1:
            case 2:
                Log.d(TAG, "onActionRequestDialogResult: request list");
                returnIntent = new Intent();
                returnIntent.putExtra("which", which);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case 4:
                ExportAccountDB();
                break;
            case 5:
                ImportAccountDB();
                break;
            case 6:
                viewAccountsFile();
                break;
            case 7:
                returnIntent = new Intent();
                returnIntent.putExtra("which", which);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: starts");
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        // Check which request we're responding to
        switch (requestCode) {
            case AccountsContract.ACCOUNT_ACTION_CHG: {
                Log.d(TAG, "onActivityResult: returned from edit change");
                break;
            }
            case AccountsContract.ACCOUNT_ACTION_ADD: {
                Log.d(TAG, "onActivityResult: returned from edit add");
                break;
            }
        }
    }
}