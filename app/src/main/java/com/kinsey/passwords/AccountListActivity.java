package com.kinsey.passwords;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;
import com.kinsey.passwords.provider.AccountSearchLoaderCallbacks;
import com.kinsey.passwords.provider.SectionsPagerAdapter;
import com.kinsey.passwords.tools.AppDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.DEFAULT_APP_DIRECTORY;
import static com.kinsey.passwords.MainActivity.format_ymdtime;
import static com.kinsey.passwords.MainActivityFragment.ACCOUNT_LOADER_ID;
import static com.kinsey.passwords.SearchActivity.CONTACT_QUERY_LOADER;

public class AccountListActivity extends AppCompatActivity
        implements AccountRecyclerViewAdapter.OnAccountClickListener,
        AccountActivityFragment.OnActionListener,
        ViewPager.OnPageChangeListener,
        AppDialog.DialogEvents,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AccountListActivity";

    private boolean mTwoPane = false;
    int mSortorder = AccountsContract.ACCOUNT_LIST_BY_CORP_NAME;
    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference
    RecyclerView mRecyclerView;
//    Loader<Cursor> loader;
//    int LOADER_ID = 1;

    private Account account;

    AccountActivityFragment accountActivityFragment = new AccountActivityFragment();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AccountPlaceholderFrag1 frag1;
    private AccountPlaceholderFrag2 frag2;
    private AccountPlaceholderFrag3 frag3;


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


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

//            List<Fragment> fragments = new Vector<Fragment>();
////            frag1 = AccountPlaceholderFrag1.newInstance(0);
//            fragments.add(frag1);
////            frag2 = AccountPlaceholderFrag2.newInstance(1);
//            fragments.add(frag2);
////            frag3 = AccountPlaceholderFrag3.newInstance(2);
//            fragments.add(frag3);

            // primary sections of the activity.
//            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), new Account());
//            mSectionsPagerAdapter.setAccount(new Account());

            // Create the adapter that will return a fragment for each of the three

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.item_detail_container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

// Attach the page change listener inside the activity
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // This method will be invoked when a new page becomes selected.
                @Override
                public void onPageSelected(int position) {
                    mSectionsPagerAdapter.refreshPage(position);
                    String msg = "";
                    switch (position) {
                        case 0:
                            msg = "Account Id info";
                            break;
                        case 1:
                            msg = "Account Open Date calendar";
                            break;
                        case 2:
                            msg = "Account Notes & ref";
                            break;
                    }
                    Toast.makeText(AccountListActivity.this,
                            msg, Toast.LENGTH_SHORT).show();
                }

                // This method will be invoked when the current page is scrolled
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Code goes here
                }

                // Called when the scroll state changes:
                // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                @Override
                public void onPageScrollStateChanged(int state) {
                    // Code goes here
                    Log.d(TAG, "onPageScrollStateChanged: state " + state);
                }
            });

//
//            mViewPager.setCurrentItem(2);
////            mSectionsPagerAdapter.getItem(2);
////            mViewPager.setAdapter(mSectionsPagerAdapter);
//            mViewPager.setCurrentItem(1);
////            mSectionsPagerAdapter.getItem(1);
////            mViewPager.setAdapter(mSectionsPagerAdapter);
//            mViewPager.setCurrentItem(0);
////            mSectionsPagerAdapter.getItem(0);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.account_items_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setupAdapter();
//
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putBoolean(AccountsContract.ACCOUNT_TWO_PANE, false);
//            arguments.putInt(Account.class.getSimpleName(),
//                    AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//            AccountListActivityFragment fragment = new AccountListActivityFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.frameLayout, fragment)
//                    .commit();
//        }


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
                                        AppDialog newFragment = AppDialog.newInstance();
                                        Bundle args = new Bundle();
                                        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ACCOUNT_ACTIONS_LIST);
                                        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_ACCOUNT_LIST_OPTIONS);
                                        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.listdiag_acc_message));
                                        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.listdiag_acc_sub_message));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            switch (menuItem.getItemId()) {
                case R.id.menuacct_add:
                    menuItem.setVisible(true);
                    Log.d(TAG, "onMenuOpened: set off add");
                    break;
                default:
                    if (mTwoPane) {
                        menuItem.setVisible(true);
                    } else {
                        menuItem.setVisible(false);
                    }
                    break;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menuacct_add:
                editAccountRequest(null);
                break;
            case R.id.menuacct_save:
                saveAccount();
//                accountActivityFragment.save();
//                setupAdapter();
                break;
            case R.id.menuacct_delete:
                deleteAccount();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {
//        Log.d(TAG, "deleteAccount: ");
        if (account == null) {
            Toast.makeText(this,
                    "No Account selected to delete",
                    Toast.LENGTH_LONG).show();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.deldiag_sub_message, account.getCorpName(), account.getPassportId()));
        args.putInt(AppDialog.DIALOG_ACCOUNT_ID, account.getId());
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }

    private void saveAccount() {

//        if (mSectionsPagerAdapter == null) {
//            return;
//        }
//        boolean updateReady = true;
//        int limit = mSectionsPagerAdapter.getCountBuilt();
//        for (int i = 0; i < limit; i++) {
//            if (!mSectionsPagerAdapter.getItemUpdates(i)) {
//                updateReady = false;
//            }
//        }

//        if (!mSectionsPagerAdapter.validateFrags()) {
//            Toast.makeText(AccountListActivity.this,
//                                                "Error on account entry",
//                                                Toast.LENGTH_LONG).show();
//            return;
//        };
//        int currentItem = mViewPager.getCurrentItem();
//        Fragment item = mSectionsPagerAdapter.getItem(currentItem);
//        if (null != item && item.isVisible()) {
//            Fragment childFragment = (Fragment) mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
//
//        }

        int currentItem = mViewPager.getCurrentItem();
        if (!mSectionsPagerAdapter.validateFrags(currentItem)) {
            Toast.makeText(AccountListActivity.this,
                                                "Error on account entry",
                                                Toast.LENGTH_LONG).show();
            return;
        };

        mSectionsPagerAdapter.collectChgs();
    }


    private void setupAdapter() {
//        Cursor cursor = createCursor();
        Cursor cursor = null;
        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, this);

        mAccountAdapter = new AccountRecyclerViewAdapter(mTwoPane, mSortorder, cursor,
                (AccountRecyclerViewAdapter.OnAccountClickListener) this);
        mRecyclerView.setAdapter(mAccountAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//    public Cursor createCursor() {
//        Log.d(TAG, "createCursor: starts");
        Log.d(TAG, "onCreateLoader: id " + String.valueOf(id));

        String[] projectionAcct =
                {AccountsContract.Columns._ID_COL,
                        AccountsContract.Columns.PASSPORT_ID_COL,
                        AccountsContract.Columns.CORP_NAME_COL,
                        AccountsContract.Columns.USER_NAME_COL,
                        AccountsContract.Columns.USER_EMAIL_COL,
                        AccountsContract.Columns.CORP_WEBSITE_COL,
                        AccountsContract.Columns.NOTE_COL,
                        AccountsContract.Columns.OPEN_DATE_COL,
                        AccountsContract.Columns.ACTVY_DATE_COL,
                        AccountsContract.Columns.SEQUENCE_COL,
                        AccountsContract.Columns.REF_FROM_COL,
                        AccountsContract.Columns.REF_TO_COL};
//        , SuggestsContract.Columns.ACTVY_DATE_COL,
//                SuggestsContract.Columns.NOTE_COL};{
        // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
//        String sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE";
        String sortOrder;
        switch (mSortorder) {
            case AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE:
                sortOrder = AccountsContract.Columns.OPEN_DATE_COL + " DESC," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
                break;
            case AccountsContract.ACCOUNT_LIST_BY_SEQUENCE:
                sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
                break;
            case AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID:
                sortOrder = AccountsContract.Columns.PASSPORT_ID_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
                break;
            default:
                sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE ASC";
                break;
        }
//        String sortOrder = AccountsContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
        return new CursorLoader(this,
                AccountsContract.CONTENT_URI,
                projectionAcct,
//                null,
                null,
                null,
                sortOrder);
//        Cursor cursor = getContentResolver().query(
//                AccountsContract.CONTENT_URI, null, null, null, sortOrder);
////        Log.d(TAG, "onCreateLoader: cursor " + cursor.toString());
//        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor > loader, Cursor data) {
        //        Log.d(TAG, "onLoadFinished: starts");
//        this.loader = loader;
        int count = 0;
        mAccountAdapter.swapCursor(data);
        count = mAccountAdapter.getItemCount();
//        if (count == 0) {
//            twCurrentTitle.setText("No accounts, + to add");
//        } else {
//            twCurrentTitle.setText("Accounts");
//        }
//        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        Log.d(TAG, "onLoaderReset: starts");
        mAccountAdapter.swapCursor(null);
    }


//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }

    @Override
    public void onAccountRetreived(Account account) {

    }


    public String ExportAccountDB() {
        String msgError = "";
        int count = -1;

        try {


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
            File file = new File(DEFAULT_APP_DIRECTORY, "accounts.json");
//
////            if (file.exists()) {
////                file.delete();
////            }
//
            if (file.exists()) {
//                Log.d(TAG, "ExportAccountDB: file exists");
            } else {
//                Log.d(TAG, "ExportAccountDB: file does not exists");
//                if (file.isDirectory()) {
//                    Log.d(TAG, "ExportAccountDB: file is dir");
//                }
                if (file.getParentFile().mkdirs()) {
                    Log.d(TAG, "ExportAccountDB: dirs made");
                } else {
//                    Log.d(TAG, "ExportAccountDB: dirs already exists");
                }
                Log.d(TAG, "ExportAccountDB: file " + file.getAbsoluteFile());
                if (file.createNewFile()) {
                    Log.d(TAG, "ExportAccountDB: file created " + file.getAbsoluteFile());
                }
                ;
//                Log.d(TAG, "ExportAccountDB: file created");

//                OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
//                Log.d(TAG, "ExportAccountDB: fos created");
//                writeJson(fos);
//                Log.d(TAG, "ExportAccountDB: fos written & closed");
            }
//
//            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
//            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

//            FileWriter fos = new FileWriter(jsonFile);
//            fos.write(response);

//			JsonWriter writer = new JsonWriter(fileWriter);
//            JsonWriter writer = new JsonWriter(out);
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            count = writeMessagesArray(writer);
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            msgError = e1.getMessage();
            Log.e(TAG, "ExportAccountDB error: " + e1.getMessage());
            Toast.makeText(AccountListActivity.this,
                    " Exported directory not exists",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            msgError = "jsonError: " + e2.getMessage();
            Log.v(TAG, msgError);
        }
//        Log.d(TAG, "ExportAccountDB: return msg " + msgError);
        if (count != -1) {
            Toast.makeText(AccountListActivity.this,
                    count + " Exported accounts",
                    Toast.LENGTH_LONG).show();
        }
        return msgError;
    }

    public void writeJson(OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("    ");
        jsonFinal(writer);
    }

    public void jsonFinal(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("status").value("OK");
        writer.name("num_results").value("");
        writer.endObject();
        writer.close();
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
//        Log.d(TAG, "loadAccounts: starts ");
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
//        Log.d(TAG, "ImportAccountDB: starts");
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
//                Log.d(TAG, "ImportAccountDB: acc " + item.getPassportId()
//                        + " " + item.getCorpName());
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ASK_IF_NEED_DICTIONARY_REBUILD);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, "Ask to rebuild search dictionary");
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, "Accounts changed, rebuild dictionary to sync up?");

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");

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
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.deldiag_sub_message, account.getCorpName(), account.getPassportId()));
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
//        Log.d(TAG, "addAccountRequest: starts");
//        boolean mTwoPane = false;
        if (mTwoPane) {
            mAccountAdapter.resetSelection();
            this.account = new Account();
            mSectionsPagerAdapter.setAccount(this.account);
            mViewPager.setCurrentItem(2);
//            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(1);
//            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
            mSectionsPagerAdapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);

////            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            AppDialog newFragment = AppDialog.newInstance();
//            Bundle args = new Bundle();
//            args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ACCOUNT_ACTIONS_LIST);
//            args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_ACCOUNT_LIST_OPTIONS);
//            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.listdiag_acc_message));
//            args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.listdiag_acc_sub_message));
//
//            newFragment.setArguments(args);
//
//            newFragment.show(fragmentManager, "dialog");

        } else {
//            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
            // in single-pane mode, start the detail activity for the selected item Id.
            Intent detailIntent = new Intent(this, AccountActivity.class);

            if (account != null) { // editing an account
                detailIntent.putExtra(Account.class.getSimpleName(), account.getId());
                detailIntent.putExtra(AccountsContract.ACCOUNT_TWO_PANE, false);
                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
            } else { // adding an account
                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_ADD);
            }
        }
    }


    @Override
    public void onAccountListSelect(Account account) {
//        Context context = getContext();
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(Account.class.getSimpleName(), account.getId());

        startActivityForResult(intent, AccountsContract.ACCOUNT_ACTION_CHG);

    }

    @Override
    public void onAccountLandListSelect(Account account) {
        Log.d(TAG, "onAccountLandListSelect: id " + account.getId());
//        accountActivityFragment = new AccountActivityFragment();
//        this.account = account;
//        Bundle arguments = new Bundle();
//        arguments.putInt(Account.class.getSimpleName(), account.getId());
//        accountActivityFragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.item_detail_container, accountActivityFragment)
//                .commit();

//        Log.d(TAG, "onAccountLandListSelect: account " + account);
        mSectionsPagerAdapter.setAccount(account);
        mSectionsPagerAdapter.notifyDataSetChanged();
//        mSectionsPagerAdapter.getItem(0);
//        mSectionsPagerAdapter.getItem(1);
//        mSectionsPagerAdapter.getItem(2);
////        mViewPager.setCurrentItem(2);
////        mViewPager.setCurrentItem(1);
//        mViewPager.setCurrentItem(0);
//        mViewPager.setCurrentItem(2);
//        mViewPager.setCurrentItem(1);
//        mViewPager.setCurrentItem(0);
//        frag1.fillPage(account);
//        frag2.fillPage(account);
//        frag3.fillPage(account);
//        if (mSectionsPagerAdapter != null) {
//            mSectionsPagerAdapter.setAccount(account);
//            mViewPager.setCurrentItem(2);
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(1);
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(0);
//            mSectionsPagerAdapter.notifyDataSetChanged();
////            mSectionsPagerAdapter.fillPages();
////            int limit = mSectionsPagerAdapter.getCountBuilt();
////            for (int i = 0; i < limit; i++) {
////                mSectionsPagerAdapter.fillPages;
//////                mSectionsPagerAdapter.removeFrag(0);
//
////            if (mSectionsPagerAdapter.getCountBuilt() > 0) {
//////        getSupportFragmentManager().beginTransaction().remove(frag).commit();
////                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.item_detail_container)).commit();
////
////            }
//        }


//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//
//        // Create the adapter that will return a fragment for each of the three
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.item_detail_container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    private void clearFrag() {
        this.account = null;
        Bundle arguments = new Bundle();
        arguments.putInt(Account.class.getSimpleName(), -1);
        accountActivityFragment = new AccountActivityFragment();
        accountActivityFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, accountActivityFragment)
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        if (mViewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
//        }
//    }

    @Override
    public void onDialogCancelled(int dialogId) {
//        Log.d(TAG, "onDialogCancelled: starts");
        Intent detailIntent = new Intent(this, FileViewActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        switch (dialogId) {
            case AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT:
//            Log.d(TAG, "onPositiveDialogResult: confirmed to delete");
//            Log.d(TAG, "onPositiveDialogResult: acctid " + args.getInt(AppDialog.DIALOG_ACCOUNT_ID));
                getContentResolver().delete(AccountsContract.buildIdUri(args.getInt(AppDialog.DIALOG_ACCOUNT_ID)), null, null);
                setupAdapter();
                clearFrag();
                break;
            case AppDialog.DIALOG_ID_ASK_IF_NEED_DICTIONARY_REBUILD:
//                Log.d(TAG, "onPositiveDialogResult: rebuild req");
                loadSearchDB();
                break;
            case AppDialog.DIALOG_ID_CONFIRM_TO_IMPORT:
                ImportAccountDB();
                break;
            case AppDialog.DIALOG_ID_CONFIRM_TO_EXPORT:
                ExportAccountDB();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
//        Log.d(TAG, "onNegativeDialogResult: delete cancel");
    }

    private void loadSearchDB() {
        deleteAllSuggestions();
        AccountSearchLoaderCallbacks loaderAcctCallbacks = new AccountSearchLoaderCallbacks(this);
        getLoaderManager().restartLoader(CONTACT_QUERY_LOADER, null, loaderAcctCallbacks);
        Toast.makeText(this,
                "Search Dictionary DB built",
                Toast.LENGTH_LONG).show();

    }

    private void deleteAllSuggestions() {
//		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
//		String[] selectionArgs = { "account" };
//        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
        getContentResolver().delete(
                SearchesContract.CONTENT_URI_TRUNCATE,
                null, null);
        getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, null);
    }

    private void confirmImport() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_CONFIRM_TO_IMPORT);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, "Confirmation");
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, "Import?");

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }

    private void confirmExport() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_CONFIRM_TO_EXPORT);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, "Confirmation");
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, "Export?");

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
//        Log.d(TAG, "onActionRequestDialogResult: starts");

        if (dialogId != AppDialog.DIALOG_ID_ACCOUNT_ACTIONS_LIST) {
            return;
        }
        Intent returnIntent;
        switch (which) {
            case AppDialog.DIALOG_ACCT_LIST_CORP_NAME:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_CORP_NAME;
                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_OPEN_DATE:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE;
                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_ACCT_ID:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID;
                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_USER_SEQ:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_SEQUENCE;
                setupAdapter();
                break;
//            case 4:
////                Log.d(TAG, "onActionRequestDialogResult: request list");
//                returnIntent = new Intent();
//                returnIntent.putExtra("which", which);
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//                break;
            case AppDialog.DIALOG_ACCT_LIST_EXPORT:
                confirmExport();
                break;
            case AppDialog.DIALOG_ACCT_LIST_IMPORT:
                confirmImport();
                break;
            case AppDialog.DIALOG_ACCT_LIST_VIEW_EXPORT_FILE:
                viewAccountsFile();
                break;
            case AppDialog.DIALOG_ACCT_LIST_VIEW_SUGGESTIONS:
                suggestPasswordList();
//                returnIntent = new Intent();
//                returnIntent.putExtra("which", which);
//                Log.d(TAG, "onActionRequestDialogResult: which " + which);
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
                break;
            default:
                Log.d(TAG, "onActionRequestDialogResult: default " + which);
        }
    }

    private void suggestPasswordList() {
        Intent detailIntent = new Intent(this, SuggestListActivity.class);
        detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
        startActivity(detailIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult: starts");
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        // Check which request we're responding to
        switch (requestCode) {
            case AccountsContract.ACCOUNT_ACTION_CHG: {
                Log.d(TAG, "onActivityResult: returned from edit change");
                setupAdapter();
                break;
            }
            case AccountsContract.ACCOUNT_ACTION_ADD: {
                Log.d(TAG, "onActivityResult: returned from edit add");
                setupAdapter();
                break;
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: " + position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged: " + state);
    }


}