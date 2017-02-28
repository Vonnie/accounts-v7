package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements
        AccountRecyclerViewAdapter.OnAccountClickListener,
        MainActivityFragment.OnActionClicked {
    public static final String TAG = "MainActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private static final String ACCOUNT_FRAGMENT = "AccountFragment";

    public static final int REQUEST_ACCOUNTS_LIST = 1;
    public static final int REQUEST_SUGGESTS_LIST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                accountsListRequest();
                break;
            case R.id.menumain_showSuggests:
                suggestsListRequest();
                break;
            case R.id.menumain_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
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
                startActivity(detailIntent);
            } else { // adding an account
                startActivity(detailIntent);
            }
        }
    }

    private void accountsListRequest() {
        Log.d(TAG, "accountsListRequest: starts");
        if (mTwoPane) {
        } else {
        }

        Intent detailIntent = new Intent(this, AccountListActivity.class);
        detailIntent.putExtra(Account.class.getSimpleName(), "sortorder");
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
        Log.d(TAG, "suggestsListRequest: starts");
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
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        // Check which request we're responding to
        switch (requestCode) {
            case REQUEST_ACCOUNTS_LIST: {
//                Log.d(TAG, "onActivityResult: you're back");
                // Make sure the request was successful

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: success");
                    int resultWhich = data.getIntExtra("which", 0);
                    Log.d(TAG, "onActivityResult: which " + resultWhich);
                    if (resultWhich == 4) {
                        suggestsListRequest();
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
                    Log.d(TAG, "onActivityResult: result " + result);
                    suggestsListRequest();
                }
                break;
            default:
                break;
        }
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
        accountsListRequest();
    }

    @Override
    public void onAccountsByOpenClicked() {
        accountsListRequest();
    }

    @Override
    public void onSuggestsClicked() {
        suggestsListRequest();
    }

    @Override
    public void onAddAccountClicked() {
        editAccountRequest(null);
    }



}
