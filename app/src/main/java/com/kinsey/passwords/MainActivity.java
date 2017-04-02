package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;
import com.kinsey.passwords.tools.AppDialog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements
        AccountRecyclerViewAdapter.OnAccountClickListener,
        MainActivityFragment.OnActionListener,
        AppDialog.DialogEvents {
    public static final String TAG = "MainActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private static final String ACCOUNT_FRAGMENT = "AccountFragment";
    public static String DEFAULT_APP_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
            + "/Passport";


    public static final int REQUEST_ACCOUNTS_LIST = 1;
    public static final int REQUEST_SUGGESTS_LIST = 2;
    public static final int REQUEST_ACCOUNT_EDIT = 3;
    public static final int REQUEST_ACCOUNT_SEARCH = 4;

    private static String pattern_ymdtime = "yyyy-MM-dd HH:mm:ss.0";
    public static SimpleDateFormat format_ymdtime = new SimpleDateFormat(
            pattern_ymdtime, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        resetPreferences();

//        String[] projection = {AccountsContract.Columns._ID_COL,
//                AccountsContract.Columns.PASSPORT_ID_COL,
//                AccountsContract.Columns.SEQUENCE_COL,
//                AccountsContract.Columns.OPEN_DATE_COL,
//                AccountsContract.Columns.ACTVY_DATE_COL,
//                AccountsContract.Columns.CORP_NAME_COL,
//                AccountsContract.Columns.USER_NAME_COL,
//                AccountsContract.Columns.USER_EMAIL_COL,
//                AccountsContract.Columns.CORP_WEBSITE_COL,
//                AccountsContract.Columns.REF_FROM_COL,
//                AccountsContract.Columns.REF_TO_COL,
//                AccountsContract.Columns.NOTE_COL
//        };
//
//        Log.d(TAG, "onCreate: projection " + Arrays.toString(projection));
//        ContentResolver contentResolver = getContentResolver();
//
//////        Cursor cursor = contentResolver.query(AccountsContract.buildTaskUrl(2),
//        Cursor cursor = contentResolver.query(AccountsContract.CONTENT_URI,
//                projection,
//                null,
//                null,
//                AccountsContract.Columns.SEQUENCE_COL);
//
//        if (cursor == null) {
//            Log.d(TAG, "onCreate: null cursor");
//        } else {
//            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                for (int i=0; i<cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: ===========================================");
//            }
//            cursor.close();
//        }
//
//        AccountDatabase accountDatabase = AccountDatabase.getInstance(this);
//        final SQLiteDatabase dbAccount = accountDatabase.getReadableDatabase();

//
//        Log.d(TAG, "onCreate: projectionSuggest " + Arrays.toString(projection));
//        String[] projectionSuggests = {SuggestsContract.Columns._ID_COL,
//                SuggestsContract.Columns.PASSWORD_COL,
//                SuggestsContract.Columns.SEQUENCE_COL,
//                SuggestsContract.Columns.ACTVY_DATE_COL};
//
//        Log.d(TAG, "onCreate: projectionSuggest " + Arrays.toString(projectionSuggests));
////        ContentResolver contentResolver = getContentResolver();
//
//////        Cursor cursor = contentResolver.query(TasksContract.buildTaskUrl(2),
//        Log.d(TAG, "onCreate: suggest uri " + SuggestsContract.CONTENT_URI);
//        cursor = contentResolver.query(SuggestsContract.CONTENT_URI,
//                projectionSuggests,
//                null,
//                null,
//                SuggestsContract.Columns.SEQUENCE_COL);
//
//        if (cursor != null) {
//            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                for (int i=0; i<cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: ===========================================");
//            }
//            cursor.close();
//        }
//
////        AccountDatabase accountDatabase = AccountDatabase.getInstance(this);
////        final SQLiteDatabase db = accountDatabase.getReadableDatabase();
//
//        SuggestDatabase suggestDatabase = SuggestDatabase.getInstance(this);
//        final SQLiteDatabase dbSuggest = suggestDatabase.getReadableDatabase();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.menumain_addAccount:
                editAccountRequest(null);
                break;
            case R.id.menumain_showAccounts:
                accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
                break;
            case R.id.menumain_showSuggests:
                suggestsListRequest();
                break;
            case R.id.menumain_search:
                Intent detailIntent = new Intent(this, SearchActivity.class);
//        detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
                startActivity(detailIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editAccountRequest(Account account) {
//        Log.d(TAG, "addAccountRequest: starts");
        if (mTwoPane) {
//            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
        } else {
//            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
            // in single-pane mode, start the detail activity for the selected item Id.
            Intent detailIntent = new Intent(this, AccountActivity.class);

            if (account != null) { // editing an account
                detailIntent.putExtra(Account.class.getSimpleName(), account);
//                detailIntent.putExtra(Account.class.getSimpleName(), AccountsContract.ACCOUNT_ACTION_CHG);
                startActivityForResult(detailIntent, REQUEST_ACCOUNT_EDIT);
            } else { // adding an account
//                detailIntent.putExtra(Account.class.getSimpleName(), AccountsContract.ACCOUNT_ACTION_ADD);
                startActivityForResult(detailIntent, REQUEST_ACCOUNT_EDIT);
            }
        }
    }

    private void accountsListRequest(int sortorder) {
//        Log.d(TAG, "accountsListRequest: starts");
        if (mTwoPane) {
        } else {
        }

        Intent detailIntent = new Intent(this, AccountListActivity.class);
        detailIntent.putExtra(Account.class.getSimpleName(), sortorder);
        startActivityForResult(detailIntent, REQUEST_ACCOUNTS_LIST);

//        AccountListActivityFragment fragment = new AccountListActivityFragment();
//
////        fragment.LOADER_ID = 1;
//        Bundle arguments = new Bundle();
//        arguments.putSerializable(Account.class.getSimpleName(), AccountsContract.TABLE_NAME);
//        fragment.setArguments(arguments);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment, fragment)
//                .commit();
    }

    private void suggestsListRequest() {
//        Log.d(TAG, "suggestsListRequest: starts");
        if (mTwoPane) {
        } else {
        }

        Intent detailIntent = new Intent(this, SuggestListActivity.class);
        detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
        startActivity(detailIntent);

//        MainActivityFragment fragment = new MainActivityFragment();
//
////        fragment.LOADER_ID = 0;
//        Bundle arguments = new Bundle();
//        arguments.putSerializable(MainActivityFragment.BUNDLE_TABLE_ID, SuggestsContract.TABLE_NAME);
//        fragment.setArguments(arguments);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentMain, fragment)
//                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult: starts");
        if (resultCode == RESULT_CANCELED) {
            return;
        }
//        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
//        Log.d(TAG, "onActivityResult: resultCode " + resultCode);
        // Check which request we're responding to
        switch (requestCode) {
            case REQUEST_ACCOUNTS_LIST: {

                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
//                    Log.d(TAG, "onActivityResult: success");
                    int resultWhich = data.getIntExtra("which", 0);
//                    Log.d(TAG, "onActivityResult: which " + resultWhich);
                    switch (resultWhich) {
                        case 1:
                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
                            break;
                        case 2:
                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE);
                            break;
                        case 3:
                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_SEQUENCE);
                            break;
                        case 4:
                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID);
                            break;
                        case 8:
                            suggestsListRequest();
                            break;
                        default:
                            break;
                    }
                    // The user picked a contact.
                    // The Intent's data Uri identifies which contact was selected.

                    // Do something with the contact here (bigger example below)
                }
                break;
            }
            case REQUEST_SUGGESTS_LIST:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("result");
//                    Log.d(TAG, "onActivityResult: result " + result);
                    suggestsListRequest();
                }
                break;
            case REQUEST_ACCOUNT_EDIT:
                accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
                break;
//            case REQUEST_ACCOUNT_SEARCH:
//                Log.d(TAG, "onActivityResult: return from search");
//                break;
            default:
                break;
        }
    }

//    @Override
//    protected void onResume() {
//        Log.d(TAG, "onResume: starts");
//        super.onResume();
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String queryResult = sharedPreferences.getString(SEARCH_QUERY, "");
//
//        if (queryResult.length() > 0) {
//            Log.d(TAG, "onResume: return a value " + queryResult);
//
//            int queryResultId = sharedPreferences.getInt(SearchActivity.SEARCH_ACCOUNT, -1);
//            Log.d(TAG, "onResume: queryResultsId " + queryResultId);
//            if (queryResultId == -1) {
//                Intent detailIntent = new Intent(this, SearchListActivity.class);
//                startActivity(detailIntent);
//            } else {
//                resetPreferences();
//                Cursor cursor = getContentResolver().query(
//                        AccountsContract.buildIdUri(queryResultId), null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    Intent detailIntent = new Intent(this, AccountActivity.class);
//                    Account account = AccountsContract.getAccountFromCursor(cursor);
//                    detailIntent.putExtra(Account.class.getSimpleName(), account);
//                    Log.d(TAG, "showAccount: account " + account.toString());
//                }
//            }
//
//
//
//
////            Intent detailIntent = new Intent(this, AccountActivity.class);
//
////            detailIntent.putExtra(Account.class.getSimpleName(), account);
//////            startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
////            startActivity(detailIntent);
//
//
//        }
//
//
////        onSearchRequested();
//    }


//    private void resetPreferences() {
//        Log.d(TAG, "resetPreferences: starts");
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sharedPreferences.edit().putString(SEARCH_QUERY, "").apply();
//        sharedPreferences.edit().putInt(SEARCH_ACCOUNT, -1).apply();
//    }
    @Override
    public boolean onSearchRequested() {
//        Log.d(TAG, "onSearchRequested: started");
        return super.onSearchRequested();
    }

    @Override
    public void onAccountDeleteClick(Account account) {
        getContentResolver().delete(AccountsContract.buildIdUri(account.getId()), null, null);
    }

    @Override
    public void onAccountEditClick(Account account) {
        editAccountRequest(account);
    }

    @Override
    public void onAccountsClicked() {
        accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
    }

    @Override
    public void onSuggestsClicked() {
        suggestsListRequest();
    }

    @Override
    public void onSearchClicked() {
        Intent detailIntent = new Intent(this, SearchActivity.class);
//        detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
        startActivity(detailIntent);
    }

    @Override
    public void onAddAccountClicked() {
        editAccountRequest(null);
    }

//    @Override
//    public void onAccountsExportClicked() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        AppDialog newFragment = AppDialog.newInstance(AppDialog.DIALOG_ACCOUNT_FILE_OPTIONS,
//                "Select action");
//        newFragment.show(fragmentManager, "dialog");
//    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
//        Log.d(TAG, "onActionRequestDialogResult: starts which " + which);
        switch (which) {
            case 0:
                break;
            case 1:
            case 2:
            case 4:
                break;
            default:
        }

    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }
}
