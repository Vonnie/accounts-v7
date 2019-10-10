package com.kinsey.passwords;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProvider;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.DatePickerFragment;
import com.kinsey.passwords.tools.AppDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//import static com.kinsey.passwords.SearchActivity.SEARCH_ACCOUNT;
//import static com.kinsey.passwords.SearchActivity.SELECTION_QUERY;
// ====================
// Statement to assist in debugging
// if (BuildConfig.DEBUG && acctId == 0) throw new AssertionError("Account Id is zero");
//

public class MainActivity extends AppCompatActivity
        implements
        AccountListActivityFragment.OnAccountListClickListener,
        AddEditActivityFragment.OnListenerClicked,
        AppDialog.DialogEvents,
        DatePickerDialog.OnDateSetListener{

//    AccountRecyclerViewAdapter.OnAccountClickListener,
//    AccountActivityFragment.OnActionListener,
//    AccountPlaceholderFrag1.OnAccountListener,
//    AccountPlaceholderFrag2.OnAccountListener,
//    AccountPlaceholderFrag3.OnAccountListener,

//    ViewPager.OnPageChangeListener,
//    CursorRecyclerViewAdapter.OnSuggestClickListener,
//        AccountRecyclerViewAdapter.OnAccountClickListener,
//        MainActivityFragment.OnActionListener,

    public static final String TAG = "MainActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;
    private Boolean editing = false;
    private static final String ACCOUNT_FRAGMENT = "AccountFragment";

    public static String DEFAULT_APP_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    public static String DEFAULT_APP_DIRECTORY_DATA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
            + "/passport";

    public static String BACKUP_FILENAME = "accounts.json";

    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";
    private int feedLimit = 10;
    private String feedCachedUrl = "INVALIDATED";
    public static final String STATE_URL = "feedUrl";
    public static final String STATE_LIMIT = "feedLimit";
    private ListView listApps;
//    private ViewPager mViewPager;
    private int fragListPos = -1;
    private int frag1Pos = 0;
    private int frag2Pos = 1;
    private int frag3Pos = 2;

    public static final int ACCOUNT_LOADER_ID = 1;
    public static final int SEARCH_LOADER_ID = 2;
    public static final int SUGGEST_LOADER_ID = 3;

    public static final int REQUEST_ACCOUNTS_LIST = 1;
    public static final int REQUEST_SUGGESTS_LIST = 2;
    public static final int REQUEST_ACCOUNT_EDIT = 3;
    public static final int REQUEST_ACCOUNT_SEARCH = 4;
    public static final int REQUEST_VIEW_EXPORT = 5;

    public static int accountSelectedPos = -1;

    private final String APP_RESUMED = "Resumed";
    private final String SELECTION_QUERY = "Query";
    private final String SELECTION_ONE_ITEM = "SeLectionOneItem";
    private final String SORTED_LIST_ORDER = "SortedListOrder";
    public final static String SEARCH_DICT_REFRESHED = "SearchDictRefreshed";

    Menu menu;
    MenuItem miActionProgressItem;
    ProgressBar progressBar;
    private Handler mHandler = new Handler();
    Runnable mRunnable;
    boolean isUserPaging = true;
    public static Account account = new Account();
    private AlertDialog mDialog = null;
    private GregorianCalendar mCalendar;
    private boolean appMsgSent = false;
    private boolean isResumed = false;

    private int accountMode = AccountsContract.ACCOUNT_ACTION_ADD;
//    View addEditLayout;
//    View addEditLayoutScroll;
    View mainFragment;


    //    private AccountListActivityFragment fragList;
//    private static AccountPlaceholderFrag1 frag1;
//    private static AccountPlaceholderFrag2 frag2;
//    private static AccountPlaceholderFrag3 frag3;

    private SearchView mSearchView;


    private enum ListHomeType {
        LISTACCOUNTS,
        TOP10FREEAPP,
        TOP25FREEAPP,
        TOPTVEPISODE,
        TOPTVSEASONS;
    }
    ListHomeType currList = ListHomeType.LISTACCOUNTS;

    private enum AppFragType {
        ACCOUNTEDIT,
        PASSWORDS;
    }
    AppFragType currFrag = AppFragType.ACCOUNTEDIT;

    private static String pattern_ymdtime = "yyyy-MM-dd HH:mm:ss.0";
    public static SimpleDateFormat format_ymdtime = new SimpleDateFormat(
            pattern_ymdtime, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_account_list);
//        setContentView(R.layout.activity_main_rss_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setTitle(getString(R.string.app_name_corpname));

//        Log.d(TAG, "onCreate: layout activity_main");

        mTwoPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//        Log.d(TAG, "onCreate: twoPane is " + mTwoPane);


//        listApps = (ListView) findViewById(R.id.xmlListView);
//        if (savedInstanceState != null) {
//            feedUrl = savedInstanceState.getString(STATE_URL);
//            feedLimit = savedInstanceState.getInt(STATE_LIMIT);
//        }
//        feedUrl = String.format("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml", feedLimit);

//        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
//        downloadUrl(feedUrl);

        FragmentManager fragmentManager = getSupportFragmentManager();
        // If the AddEditActivity fragment exists, we're editing
        editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
//        Log.d(TAG, "onCreate: editing is " + editing);

        // We need references to the containers, so we can show or hide them as necessary.
        // No need to cast them, as we're only calling a method that's available for all views.
        View addEditLayout = findViewById(R.id.task_details_container);
        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
        mainFragment = findViewById(R.id.fragment);
        progressBar  = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.GONE);

        if(mTwoPane) {
            Log.d(TAG, "onCreate: twoPane mode");
            mainFragment.setVisibility(View.VISIBLE);
            addEditLayout.setVisibility(View.VISIBLE);
            addEditLayoutScroll.setVisibility(View.VISIBLE);
        } else if (editing) {
            Log.d(TAG, "onCreate: single pane, editing");
            // hide the left hand fragment, to make room for editing
            mainFragment.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onCreate: single pane, not editing");
            // Show left hand fragment
            mainFragment.setVisibility(View.VISIBLE);
            // Hide the editing frame
            addEditLayout.setVisibility(View.GONE);
            addEditLayoutScroll.setVisibility(View.GONE);
        }


        if (mSearchView != null) {
            Log.d(TAG, "onCreate: activated " + mSearchView.isActivated());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//
//        Log.d(TAG, "sharedPreferences: return a value " + queryResult);
//
//        AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment);
//        listFragment.setQuery(queryResult);


//        if (queryResult.equals("")) {
//            Toast.makeText(this, "Long click on item for more options", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "List from search on " + queryResult, Toast.LENGTH_LONG).show();
//        }

//        if (appMsgSent) {
//
//        } else {
//            Log.d(TAG, "onCreate: send a msg");
//            appMsgSent = true;
//            searchListRequest();
//        }

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "onConfigurationChanged: landscape");
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "onConfigurationChanged: portrait");
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "onConfigurationChanged: unk");
        }
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // Store instance of the menu item containing progress
//        miActionProgressItem = menu.findItem(R.id.miActionProgress);
//        // Extract the action-view from the menu item
//        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
//        // Return to finish
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu: starts");
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

//        if (currList == ListHomeType.LISTACCOUNTS) {
//            menu.findItem(R.id.menumain_showAccounts).setChecked(true);
//        } else if (currList == ListHomeType.TOP10FREEAPP) {
//            menu.findItem(R.id.menumain_rss_top_free_apps).setChecked(true);
//        } else if (currList == ListHomeType.TOPTVEPISODE) {
//            menu.findItem(R.id.menumain_rss_top_tv_episodes).setChecked(true);
//        } else if (currList == ListHomeType.TOPTVSEASONS) {
//            menu.findItem(R.id.menumain_rss_top_tv_seasons).setChecked(true);
//        }

        Log.d(TAG, "onCreateOptionsMenu: search starting");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menumain_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);

        mSearchView.setIconified(true);

        mSearchView.setOnSearchClickListener(new SearchView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: v " + v.toString());
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean isDictRefreshed = sharedPreferences.getBoolean(SEARCH_DICT_REFRESHED, false);
                if (isDictRefreshed) {
                    Log.d(TAG, "onClick: yes dict refreshed");
                } else {
                    Log.d(TAG, "onClick: no dict refreshed");
                    Toast.makeText(getApplicationContext(), "One moment to populate search dictionary", Toast.LENGTH_LONG).show();
                    loadSearchDB();
                    isDictRefreshed = sharedPreferences.getBoolean(SEARCH_DICT_REFRESHED, false);
                    Log.d(TAG, "onClick: dict refreshed");
                }

            }
        });

        //        mSearchView.clearFocus();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");

//        if (queryResult.equals("")) {
//            setMenuItemChecked(R.id.menumain_ifSearch, false);
//        } else {
//            setMenuItemChecked(R.id.menumain_ifSearch, true);
//        }


//        Log.d(TAG, "onCreateOptionsMenu: activated " + mSearchView.isActivated());

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called " + query);
                mSearchView.clearFocus();
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
                if (cursor == null) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                    sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
                    return false;
                }
                if (cursor.getCount() == 1) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                    int accountId = getSearchAccountId(0);
                    Log.d(TAG, "onQueryTextSubmit: accountId " + accountId);
                    sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, accountId).apply();
//                    listFragment.setQuery("");
//                    listFragment.setAcctId(accountId);
//                    int pos = listFragment.getAccountSelectedPos();
//                    Log.d(TAG, ": pos " + pos);
//                    if (pos == -1) {
//                        searchListRequest();
//                    } else {
//                        acctEditRequest(accountId);
//                    }
                    return false;
                }
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SELECTION_QUERY, query).apply();
                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();

                //                showSuggestions();

//                SearchesContract.cursorSearch = mSearchView.getSuggestionsAdapter().getCursor();

//                Log.d(TAG, "onQueryTextSubmit: #searches " + mSearchView.getSuggestionsAdapter().getCursor().getCount());

                Log.d(TAG, "onQueryTextSubmit: showSearches");

//                showSearches();
//                finish();
//                listFragment.setQuery(query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG, "onQueryTextChange: adt " + mSearchView.getSuggestionsAdapter().getCount());
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, newText).apply();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();

                return false;
            }


        });


        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "SearchView onClose: starts");
//                showSuggestions();
//                finish();
                mSearchView.clearFocus();
//                setMenuItemChecked(R.id.menumain_ifSearch, false);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();

                AccountListActivityFragment listFragment = (AccountListActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);
                listFragment.setQuery("");


//                Intent detailIntent = new Intent(this, AccountListActivity.class);
//                detailIntent.putExtra(Account.class.getSimpleName(), sortorder);
//                startActivityForResult(detailIntent, REQUEST_ACCOUNTS_LIST);

                return false;
            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Log.d(TAG, "onSuggestionSelect: position " + position);
//                showSuggestions();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.d(TAG, "onSuggestionClick: position " + position);
//                showSuggestions();
                mSearchView.clearFocus();
                int accountId = getSearchAccountId(position);
                Log.d(TAG, "onSuggestionClick: accountId " + accountId);
//                showOneSearches(accountId, position);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, accountId).apply();
                Log.d(TAG, "onSuggestionClick: finish");
                AccountListActivityFragment listFragment = (AccountListActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                listFragment.setQuery("");
//                listFragment.setAcctId(accountId);
//                int pos = listFragment.getAccountSelectedPos();
//                Log.d(TAG, "onSuggestionClick: pos " + pos);
//                if (pos == -1) {
//                    searchListRequest();
//                } else {
//                    acctEditRequest(accountId);
//                }
//                //                finish();

                return false;
            }
        });

        Log.d(TAG, "onCreateOptionsMenu: returned " + true);


        return true;
    }


    private int getSearchAccountId(int position) {
//        Log.d(TAG, "showAccount: pos " + position);
        mSearchView.getSuggestionsAdapter().getCursor().moveToFirst();
        mSearchView.getSuggestionsAdapter().getCursor().move(position);
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(3));
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getString(3));
//        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(5));
        int dbId = Integer.valueOf(mSearchView.getSuggestionsAdapter().getCursor()
                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA)));
//        Log.d(TAG, "showAccount: " + dbId);
//        String corpName = mSearchView.getSuggestionsAdapter().getCursor()
//                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
//        Log.d(TAG, "showAccount: corpName " + corpName);

        return dbId;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";

        Log.d(TAG, "onOptionsItemSelected: search cancel");
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
        }
        AddEditActivityFragment editFragment;
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menumain_add:
//                editAccountRequest(null);
//                addAccountRequest();
                showAddConfirmationDialog(AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT);
                break;
//            case R.id.menumain_showAccounts:
//                currList = ListHomeType.LISTACCOUNTS;
//                if (!item.isChecked()) {
//                    item.setChecked(true);
//                }
//                accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//                break;

            case R.id.menuacct_sort_corpname:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                resortList(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
                break;

            case R.id.menuacct_sort_opendate:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                resortList(AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE);
                break;

            case R.id.menuacct_sort_passport:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                resortList(AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID);
                break;

            case R.id.menuacct_sort_custom:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                resortList(AccountsContract.ACCOUNT_LIST_BY_SEQUENCE);
                break;

//            case R.id.menuacct_showdate:
//                suggestsListRequest3();
//                break;

//            case R.id.menuacct_save:
//                saveAccountEdits();
//                break;

            case R.id.menumain_showSuggests:
                suggestsListRequest2();
                break;

            case R.id.menuacct_external_accts:
//                addEditActivityFragment = (AddEditActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
//                if(addEditActivityFragment != null) {
//                    Log.d(TAG, "onOptionsItemSelected: addeditFragment found");
//                    if (addEditActivityFragment.canClose()) {
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
//                        if(editFragment != null) {
//                            // we were editing
//                            getSupportFragmentManager().beginTransaction()
//                                    .remove(editFragment)
//                                    .commit();
////                            addEditLayout.setVisibility(View.GONE);
////                            addEditLayoutScroll.setVisibility(View.GONE);
//                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                            sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                            sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//
////                            AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                                    getSupportFragmentManager().findFragmentById(R.id.fragment);
////                            listFragment.setQuery("");
//
//                        }
//                    } else {
//                        showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT_UP);
//                        break;
//                    }
//                }
                viewAccountsFile();
                break;

            case R.id.menumain_refresh:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
                AccountListActivityFragment listFragment = (AccountListActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);
                if (listFragment.getQueryCorp().equals("")) {
                    listFragment.unselectItem();
                } else {
                    listFragment.setQuery("");
                }

                removeEditFrag();

                Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
                break;

//                Log.d(TAG, "onOptionsItemSelected: ifSearch " + item.isChecked());
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                if (item.isChecked()) {
//                    item.setChecked(false);
//                    sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                    AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                            getSupportFragmentManager().findFragmentById(R.id.fragment);
//                    listFragment.setQuery("");
//                    Toast.makeText(this, "Search selection cleared", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//                if (queryResult.equals("")) {
//                    item.setChecked(false);
//                    AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                            getSupportFragmentManager().findFragmentById(R.id.fragment);
//                    listFragment.setQuery("");
//                } else {
//                    item.setChecked(true);
//                }
//                break;


            case R.id.menumain_search_setting:
                searchListRequest();
                break;


//            case R.id.menumain_search:
//                Log.d(TAG, "onOptionsItemSelected: search request");
//                Log.d(TAG, "onOptionsItemSelected: search request");
////                searchListRequest();
//                requestSearch();
//
//                break;


//            case R.id.menumain_rss_top_free_apps:
//
//                currList = ListHomeType.TOP10FREEAPP;
//                if (!item.isChecked()) {
//                    item.setChecked(true);
//                }
//
//                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
//                downloadUrl(feedUrl);
//
//                break;
//            case R.id.menumain_rss_top_tv_episodes:
//
//                currList = ListHomeType.TOPTVEPISODE;
//                if (!item.isChecked()) {
//                    item.setChecked(true);
//                }
//
//                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";
//                downloadUrl(feedUrl);
//
//                break;
//            case R.id.menumain_rss_top_tv_seasons:
//
//                currList = ListHomeType.TOPTVSEASONS;
//                if (!item.isChecked()) {
//                    item.setChecked(true);
//                }
//
////                feedUrl = "https://www.nasa.gov/rss/dyn/Houston-We-Have-a-Podcast.rss";
////                feedUrl = "https://www.cnet.com/rss/news/";
//                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvSeasons/xml";
//                downloadUrl(feedUrl);
//
//                break;

            case R.id.menumain_about:

                showAboutDialog();

                break;

            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");


                editFragment = (AddEditActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);

                if(editFragment == null) {
                    showConfirmationLeaveApp();
                    break;
                }

                if (!editFragment.canClose()) {
                    showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT_UP);
                    break;
                }



                getSupportFragmentManager().beginTransaction()
                        .remove(editFragment)
                        .commit();
                if (mTwoPane) {
                    return false;
                } else {
                    mainFragment.setVisibility(View.VISIBLE);
                    View addEditLayout = findViewById(R.id.task_details_container);
                    View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
                    addEditLayout.setVisibility(View.GONE);
                    addEditLayoutScroll.setVisibility(View.GONE);
                    Toast.makeText(this,
                            "Long click select for more details",
                            Toast.LENGTH_SHORT).show();

                    return true;
                }

//
//                    if (mTwoPane) {
//                        // in Landscape, so quit only if the back button was used
////                            Log.d(TAG, "onPositiveDialogResult: get list");
////                            AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                                    getSupportFragmentManager().findFragmentById(R.id.fragment);
////                            listFragment.resetSelectItem();
//                    } else {
//                        // hide the edit container in single pane mode
//                        // and make sure the left-hand container is visible
////                        returnToMain();
//                    }
//
//
//
//
//                if (mTwoPane) {
//                } else {
//                    showConfirmationLeaveApp();
//                    break;
//                }
//
//                Log.d(TAG, "onOptionsItemSelected: addeditFragment found");
//
//                return true;  // indicate we are handling this
//
////                    setMenuItemVisible(R.id.menuacct_save, false);
////                FileViewActivityFragment fvFragment = (FileViewActivityFragment)
////                        getSupportFragmentManager().findFragmentByTag("fileview");
//////                        getSu1pportFragmentManager().findFragmentById(R.id.task_details_container);
////                if (fvFragment != null) {
////                    Log.d(TAG, "onOptionsItemSelected: fileview fragment found");
////                    if (fvFragment.isImportRefreshReq()) {
////                        resortList(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
////                    }
////                    return true;
////                }
////                SuggestListActivityFragment suggestFragment = (SuggestListActivityFragment)
////                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
////                if (suggestFragment == null) {
////                    return super.onOptionsItemSelected(item);
////                } else {
////                    showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT_UP);
////                    return true;  // indicate we are handling this
////                }
////                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void removeEditFrag() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);

        if (editFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .remove(editFragment)
                    .commit();

            mainFragment.setVisibility(View.VISIBLE);

            if (!mTwoPane) {
                View addEditLayout = findViewById(R.id.task_details_container);
                View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
                addEditLayout.setVisibility(View.GONE);
                addEditLayoutScroll.setVisibility(View.GONE);
            }

        }
    }

//    private void returnToMain() {
//
//        mainFragment.setVisibility(View.VISIBLE);
//
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
////        if(editFragment != null) {
////            // we were editing
////            getSupportFragmentManager().beginTransaction()
////                    .remove(editFragment)
////                    .commit();
////        }
//
//        // Hide the editing frame
//        addEditLayout.setVisibility(View.GONE);
//        addEditLayoutScroll.setVisibility(View.GONE);
//
////        View addEditLayout = findViewById(R.id.task_details_container);
////        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
////        View mainFragment = findViewById(R.id.fragment);
////        // We're just removed the editing fragment, so hide the frame
////        addEditLayout.setVisibility(View.GONE);
////        addEditLayoutScroll.setVisibility(View.GONE);
////
////        // and make sure the MainActivityFragment is visible
////        mainFragment.setVisibility(View.VISIBLE);
//////                            setMenuItemVisible(R.id.menuacct_save, false);
//    }

    private void setMenuItemEnabled(int id, boolean blnSet) {
        MenuItem item = menu.findItem(id);
        item.setEnabled(blnSet);
    }

    private void setMenuItemVisible(int id, boolean blnSet) {
        Log.d(TAG, "setMenuItemVisible: id " + id);
        if (menu == null) {
            Log.d(TAG, "setMenuItemVisible: ");
            return;
        }
        MenuItem item = menu.findItem(id);
        if (blnSet) {
            item.setVisible(true);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            item.setVisible(false);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
    }


    private void setMenuItemChecked(int id, boolean blnSet) {
        MenuItem item = menu.findItem(id);
        if (blnSet) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }
    }


//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
 //
//    @Override
//    public void onPageSelected(int position) {
//        Log.d(TAG, "onPageSelected: " + position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//        Log.d(TAG, "onPageScrollStateChanged: " + state);
//    }

//    @Override
//    public void onAccountRetreived(Account account) {
//
//    }
//
//    @Override
//    public void onAccount2Instance() {
//        Log.d(TAG, "onAccount2Instance: ");
//    }
//
//    @Override
//    public void onAccount3Instance() {
//        Log.d(TAG, "onAccount3Instance: ");
//    }


    private void viewAccountsFile() {
        Log.d(TAG, "viewAccountsFile: request request view exports");
//        mActivityStart = true;
        Intent detailIntent = new Intent(this, FileViewActivity.class);
//        startActivity(detailIntent);
        startActivityForResult(detailIntent, REQUEST_VIEW_EXPORT);
    }

    @Override
    public void onAccountListSelect(Account account) {
        Log.d(TAG, "onAccountListSelect: " + account.getCorpName());
//        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
//        Log.d(TAG, "onAccountListSelect: selected pos " + selected_position);
//        fragList.setSelected_position(selected_position);
//        accountSelectedPos = selected_position;
//        setMenuItemEnabled(R.id.menuacct_delete, true);
//        setMenuItemEnabled(R.id.menuacct_save, false);
//        setMenuItemVisible(R.id.menuacct_save, false);
//        if (account.getCorpWebsite().equals("")) {
//            setMenuItemEnabled(R.id.menuacct_internet, false);
//        } else {
//            setMenuItemEnabled(R.id.menuacct_internet, true);
//        }
        this.account = account;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, this.account.getId()).apply();


        if (mTwoPane) {
            acctEditRequest(this.account.getId());
        }

        Toast.makeText(this,
                "Long click select for more details",
                Toast.LENGTH_SHORT).show();

//        mSectionsPagerAdapter.destroyItem(mViewPager, frag1Pos, frag1);
//        mSectionsPagerAdapter.destroyItem(mViewPager, frag2Pos, frag2);
//        mSectionsPagerAdapter.destroyItem(mViewPager, frag3Pos, frag3);
//        int currPage = mViewPager.getCurrentItem();

//        updatePages(currPage);
//        updatePages(frag1Pos);

    }

//    private void updatePages(int currPage) {
//        isUserPaging = false;
//
////        int currPage = mViewPager.getCurrentItem();
//
//        if (fragListPos == -1) {
//            mViewPager.setOffscreenPageLimit(2);
//        } else {
//            mViewPager.setOffscreenPageLimit(3);
//        }
//
//        mViewPager.setCurrentItem(frag2Pos);
//
//        frag1.fillPage();
//        Log.d(TAG, "updatePages: page1 filled, setPrimary");
////        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag1Pos, frag1);
//////        mViewPager.setCurrentItem(frag1Pos);
////        mSectionsPagerAdapter.notifyDataSetChanged();
//
////        new CountDownTimer(10000, 1000) {
////
////            public void onTick(long millisUntilFinished) {
//////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
////            }
////
////            public void onFinish() {
//////                    mTextField.setText("done!");
////            }
////        }.start();
//
//        frag2.fillPage();
////        mViewPager.setCurrentItem(frag2Pos);
//        Log.d(TAG, "updatePages: page2 filled, setPrimary");
////        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag2Pos, frag2);
////        mSectionsPagerAdapter.notifyDataSetChanged();
//
////        new CountDownTimer(10000, 1000) {
////
////            public void onTick(long millisUntilFinished) {
//////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
////            }
////
////            public void onFinish() {
//////                    mTextField.setText("done!");
////            }
////        }.start();
//        frag3.fillPage();
//        Log.d(TAG, "updatePages: page3 filled, setPrimary");
////        mViewPager.setCurrentItem(frag3Pos);
////        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag3Pos, frag3);
////        mSectionsPagerAdapter.notifyDataSetChanged();
//
////        new CountDownTimer(10000, 1000) {
////
////            public void onTick(long millisUntilFinished) {
//////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
////            }
////
////            public void onFinish() {
//////                    mTextField.setText("done!");
////            }
////        }.start();
//
//
////        mSectionsPagerAdapter.fillPages();
//
////        isUserPaging = false;
//
////        frag1 = (AccountPlaceholderFrag1)mSectionsPagerAdapter.instantiateItem(mViewPager, frag1Pos);
////        frag2 = (AccountPlaceholderFrag2)mSectionsPagerAdapter.instantiateItem(mViewPager, frag2Pos);
////        frag3 = (AccountPlaceholderFrag3)mSectionsPagerAdapter.instantiateItem(mViewPager, frag3Pos);
//
////        if (frag1 != null) {
////
////            mViewPager.setCurrentItem(frag3Pos);
////            frag1.fillPage();
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mViewPager.setCurrentItem(frag3Pos);
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mSectionsPagerAdapter.getItem(frag1Pos);
////        }
////        if (frag2 != null) {
////            mViewPager.setCurrentItem(frag2Pos);
////            frag2.fillPage();
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mSectionsPagerAdapter.getItem(frag2Pos);
////            mViewPager.setCurrentItem(frag2Pos);
////            mSectionsPagerAdapter.notifyDataSetChanged();
////        }
////        if (frag3 != null) {
////            mViewPager.setCurrentItem(frag1Pos);
////            frag3.fillPage();
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mSectionsPagerAdapter.getItem(frag3Pos);
////            mViewPager.setCurrentItem(frag1Pos);
////            mSectionsPagerAdapter.notifyDataSetChanged();
////        }
//
////        isUserPaging = true;
//
//        //        mRetainedFragment.getData().setAccount(account);
////        mSectionsPagerAdapter.setAccount(account);
////        isUserPaging = false;
////        int currPage = mViewPager.getCurrentItem();
////        frag1 = AccountPlaceholderFrag1.newInstance();
////        frag2 = AccountPlaceholderFrag2.newInstance();
////        frag3 = AccountPlaceholderFrag3.newInstance();
////        mSectionsPagerAdapter.setFrag1(frag1);
////        mSectionsPagerAdapter.setFrag2(frag2);
////        mSectionsPagerAdapter.setFrag3(frag3);
////        if (!mTwoPane && accountSortorder == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
//
////        if (fragListPos == -1) {
////            if (accountMode == AccountsContract.ACCOUNT_ACTION_ADD) {
////                mViewPager.setCurrentItem(frag1Pos);
////                if (!isRotated) {
////                    frag1.setCorpNameFocus();
////                }
////            } else {
////                Log.d(TAG, "updatePages: currPage " + currPage);
////                mViewPager.setCurrentItem(currPage);
////                if (!isRotated) {
////                    if (currPage == frag1Pos) {
////                        frag1.setCorpNameFocus();
////                    }
////                }
////            }
////        } else {
//////            fragList.setViewerPos();
//////            myRecyclerView.findViewHolderForAdapterPosition(pos).itemView;
////            if (accountMode == AccountsContract.ACCOUNT_ACTION_ADD) {
////                mViewPager.setCurrentItem(frag1Pos);
////                if (!isRotated) {
////                    frag1.setCorpNameFocus();
////                }
////            } else {
////                mViewPager.setCurrentItem(fragListPos);
////            }
////        }
//
//        Log.d(TAG, "updatePages: currentItem " + mViewPager.getCurrentItem());
//
//        if (fragListPos == -1) {
////            mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag1Pos, frag1);
//            mViewPager.setCurrentItem(frag1Pos);
//        } else {
////            mSectionsPagerAdapter.setPrimaryItem(mViewPager, fragListPos, fragList);
//            if (accountMode == AccountsContract.ACCOUNT_ACTION_ADD) {
//                mViewPager.setCurrentItem(frag1Pos);
//            } else {
//                mViewPager.setCurrentItem(fragListPos);
//            }
//        }
//
//        Log.d(TAG, "updatePages: currentItem " + mViewPager.getCurrentItem());
//
//        isUserPaging = true;
//
////        mViewPager.setCurrentItem(frag3Pos);
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag3Pos());
////        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag2Pos());
////        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag1Pos());
////        Log.d(TAG, "onAccountListSelect: currPage " + currPage);
////        if (currPage != 0) {
////            mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(currPage);
////        }
////        isUserPaging = true;
//
//
//////        Context context = getContext();
////        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
////        Intent intent = new Intent(this, AccountActivity.class);
////        intent.putExtra(Account.class.getSimpleName(), account.getId());
////
////        startActivityForResult(intent, AccountsContract.ACCOUNT_ACTION_CHG);
//
//    }


//    @Override
//    public void onAccountUpClick(Account account) {
//        Log.d(TAG, "onAccountUpClick: " + account.getId() + " " + account.getCorpWebsite());
//        List<Account> listAccounts = loadAccountsBySeq();
//        int priorId = -1;
//        int iLimit = listAccounts.size();
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (account.getId() == item.getId()) {
//                break;
//            }
//            priorId = item.getId();
//        }
////        Log.d(TAG, "onSuggestDownClick: priorId " + priorId);
//
//        int reseq = 0;
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (priorId != item.getId()) {
//                reseq++;
//                item.setNewSequence(reseq);
//                if (account.getId() == item.getId()) {
//                    break;
//                }
//            }
//        }
//
//        boolean found = false;
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (priorId == item.getId()) {
//                reseq++;
//                item.setNewSequence(reseq);
//            } else {
//                if (account.getId() == item.getId()) {
//                    found = true;
//                } else {
//                    if (found) {
//                        reseq++;
//                        item.setNewSequence(reseq);
//                    }
//                }
//            }
//        }
//
//        ContentResolver contentResolver = getContentResolver();
//
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (item.getSequence() != item.getNewSequence()) {
////                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
//                ContentValues values = new ContentValues();
//                values.put(AccountsContract.Columns.SEQUENCE_COL, item.getNewSequence());
//                contentResolver.update(AccountsContract.buildIdUri(item.getId()), values, null, null);
//            }
//        }
//
//        if (accountSelectedPos == -1) {
//        } else if (accountSelectedPos > 0) {
//            accountSelectedPos -= 1;
//        }
//
//    }

//    @Override
//    public void onAccountDownClick(Account account) {
////        Log.d(TAG, "onAccountDownClick: " + account.getId());
//        List<Account> listAccounts = loadAccountsBySeq();
//        int nextId = -1;
//        int iLimit = listAccounts.size();
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (nextId != -1) {
//                nextId = item.getId();
//                break;
//            }
//            if (account.getId() == item.getId()) {
//                nextId = item.getId();
//            }
//        }
////        Log.d(TAG, "onSuggestDownClick: nextId " + nextId);
//
//        int reseq = 0;
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (account.getId() != item.getId()) {
//                reseq++;
//                item.setNewSequence(reseq);
//                if (nextId == item.getId()) {
//                    break;
//                }
//            }
//        }
//
//        boolean found = false;
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (account.getId() == item.getId()) {
//                reseq++;
//                item.setNewSequence(reseq);
//            } else {
//                if (nextId == item.getId()) {
//                    found = true;
//                } else {
//                    if (found) {
//                        reseq++;
//                        item.setNewSequence(reseq);
//                    }
//                }
//            }
//        }
//
//        ContentResolver contentResolver = getContentResolver();
//
//        for (int i = 0; i < iLimit; i++) {
//            Account item = listAccounts.get(i);
//            if (item.getSequence() != item.getNewSequence()) {
////                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
//                ContentValues values = new ContentValues();
//                values.put(AccountsContract.Columns.SEQUENCE_COL, item.getNewSequence());
//                contentResolver.update(AccountsContract.buildIdUri(item.getId()), values, null, null);
//            }
//        }
//
//        if (accountSelectedPos == -1) {
//        } else if (accountSelectedPos < iLimit) {
//            accountSelectedPos += 1;
//        }
//
//    }


    @Override
    public void onListComplete() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAccountLong(final Account account) {
        Log.d(TAG, "onAccountLong: " + account);
//        showAboutDialog();

            @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.activity_itemview, null, false);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);

            builder.setView(messageView);

//            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
////                Log.d(TAG, "onClick: Entering messageView.onClick, showing = " + mDialog.isShowing());
//                    if(mDialog != null && mDialog.isShowing()) {
//                        mDialog.dismiss();
//                    }
//                }
//            });

            mDialog = builder.create();
            mDialog.setCanceledOnTouchOutside(true);

            final EditText tvName = messageView.findViewById(R.id.txt_corp_name);
            tvName.setText(account.getCorpName());
            TextView tvId = (TextView) messageView.findViewById(R.id.txt_id);
//            tvId.setText(String.valueOf("AcctId " + account.getId()));
        tvId.setText(String.valueOf("AcctId " + account.getPassportId()));
//            final EditText tvWebsite = messageView.findViewById(R.id.addedit_corp_website);
//            tvWebsite.setText(account.getCorpWebsite());

            ImageButton btnEdit = messageView.findViewById(R.id.imgbtn_edit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: for edit");
                    acctEditRequest(account.getId());
                    mDialog.dismiss();
                }
            });

            ImageButton btnSave = messageView.findViewById(R.id.imgbtn_save);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: for edit");
                    String prevCorpName = account.getCorpName();
                    account.setCorpName(tvName.getText().toString());
//                    if (!tvWebsite.getText().toString().startsWith("")) {
//                        if (tvWebsite.getText().toString().startsWith("http://") ||
//                                tvWebsite.getText().toString().startsWith("https://")) {
//                            account.setCorpWebsite(tvWebsite.getText().toString());
//                        } else {
//                            tvWebsite.setError("website must start with http");
//                            return;
//                        }
//                    }
                    updateCorp(account);
                    mDialog.dismiss();
                    if (mTwoPane) {
                        removeEditing();
                    }

//                    if (!prevCorpName.equals(account.getCorpName())) {
//                        Log.d(TAG, "onClick: corpname chg to refresh search");
//                        searchListRequest();
//                    }
                }
            });

        ImageButton btnDelete = messageView.findViewById(R.id.imgbtn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: for edit");
                confirmDeleteAccount(account);
                mDialog.dismiss();
            }
        });

//        ImageButton btnWebsite = messageView.findViewById(R.id.imgbtn_globe);
//        btnWebsite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: for edit");
//                verifyEmail(tvWebsite);
//                linkToInternet(tvWebsite.getText().toString());
//                mDialog.dismiss();
//            }
//        });
//            TextView about_url = (TextView) messageView.findViewById(R.id.about_url);
//            if(about_url != null) {
//                about_url.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        String s = ((TextView) v).getText().toString();
//                        intent.setData(Uri.parse(s));
//                        try {
//                            startActivity(intent);
//                        } catch(ActivityNotFoundException e) {
//                            Toast.makeText(MainActivity.this, "No browser application found, cannot visit world-wide web", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }

            mDialog.show();

//        fragAppDialog = new AppItem();
////        currentFragClass = fragCurrencyCountry.getClass().toString();
//
//        Bundle bundle = new Bundle();
////            mBundle.putLong(DrawerItem.EXTRA_DRAWER_ID, idSelected);
////			Log.v(TAG, "bundle set");
////            getActivity().getActionBar().setTitle(drawerItem.getDescription());
//        setTitle(getResources().getString(R.string.frag_title_feed));
//        mBundle.putString(DrawerItem.EXTRA_RSS_VIEW, getResources().getString(R.string.dailyFxRss));
//        fragDailyFxRss = new DailyFxRssFrag();
//        addFragment(fragDailyFxRss);


    }

//    private void verifyEmail(TextView tvWebsite) {
//        if (!tvWebsite.getText().toString().equals("")) {
//            if (tvWebsite.getText().toString().toLowerCase().startsWith("http://")
//                    || tvWebsite.getText().toString().toLowerCase().startsWith("https://")) {
//            } else {
//                tvWebsite.setText("http://" + tvWebsite.getText().toString());
//            }
//        }
//    }


    private void removeEditing() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if(editFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .remove(editFragment)
                    .commit();
            View addEditLayout = findViewById(R.id.task_details_container);
            View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
            addEditLayout.setVisibility(View.GONE);
            addEditLayoutScroll.setVisibility(View.GONE);
        }

    }

    private void updateCorp(Account account) {

        ContentValues values = new ContentValues();

        values.put(AccountsContract.Columns.CORP_NAME_COL, account.getCorpName());
        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, account.getCorpWebsite());
        getContentResolver().update(AccountsContract.buildIdUri(account.getId()), values, null, null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putBoolean(SEARCH_DICT_REFRESHED, false).apply();

    }

    private void confirmDeleteAccount(Account account) {
//        Log.d(TAG, "deleteAccount: ");
//        if (account == null) {
//            Toast.makeText(this,
//                    "No Account selected to delete",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (accountSelectedPos == -1) {
//            Toast.makeText(this,
//                    "Must select an account to delete",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
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


//    private void linkToInternet(String webpage) {
//        Log.d(TAG, "linkToInternet: " + webpage);
//        if (webpage.equals("")
//                || webpage.toLowerCase().equals("http://")
//                || webpage.toLowerCase().equals("https://")) {
//            Toast.makeText(this,
//                    "Selected account has no corp website to link to",
//                    Toast.LENGTH_LONG).show();
//        } else {
//            if (!webpage.equals("")) {
////                mActivityStart = true;
//                Log.d(TAG, "linkToInternet: " + webpage);
//                vewInternet(webpage);
////                webview.loadUrl(account.getCorpWebsite());
//            } else {
//                Log.d(TAG, "linkToInternet: none");;
//            }
//        }
//    }

    private void vewInternet(String webpage) {
//        Bundle arguments = new Bundle();
//        arguments.putString(WebViewActivity.class.getSimpleName(),
//                webpage);
//        WebViewActivityFragment fragment = new WebViewActivityFragment();
//        fragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.content_account_list, fragment)
//                .commit();
//    }

////            Uri uri = Uri.parse(account.getCorpWebsite());
////            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////            startActivity(intent);
////            startActivityForResult(intent, AccountsContract.ACCOUNT_ACTION_WEBPAGE);

        Log.d(TAG, "vewInternet: webpage " + webpage);
        Uri uri = Uri.parse(webpage);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }

//        Intent detailIntent = new Intent(this, WebViewActivity.class);
//        detailIntent.putExtra(WebViewActivity.class.getSimpleName(), account.getCorpWebsite());
////                Log.d(TAG, "onClick: website " + account.getCorpWebsite());
////                Log.d(TAG, "onClick: wv class " + WebViewActivity.class.getSimpleName());
//        startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_WEBPAGE);
////            startActivity(detailIntent);
    }
//    }
//

//
//    List<Account> loadAccountsBySeq() {
////        Log.d(TAG, "loadAccountsBySeq: starts ");
//        String sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL.toLowerCase() + " COLLATE NOCASE ASC";
//        Cursor cursor = getContentResolver().query(
//                AccountsContract.CONTENT_URI, null, null, null, sortOrder);
//
//        List<Account> listAccounts = new ArrayList<Account>();
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
////                Log.d(TAG, "loadPasswords: seq " + cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL))
////                        + ":" + cursor.getString(cursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)));
//                Account item = new Account(
//                        cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
//                        cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
//                item.setNewSequence(cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));
//                listAccounts.add(item);
//            }
//            cursor.close();
//        }
//
//        return listAccounts;
//    }


    private void requestSearch() {
        Log.d(TAG, "onPositiveDialogResult: request to rebuild search");
        Intent detailIntent = new Intent(this, SearchActivity.class);
        detailIntent.putExtra(SearchActivity.class.getSimpleName(), true);
        startActivity(detailIntent);
    }


    private void searchListRequest() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ASK_REFRESH_SEARCHDB);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.searchdiag_message));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.searchdiag_sub_message));
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.searchdiag_negative_caption);
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.searchdiag_positive_caption);


        Log.d(TAG, "searchListRequest: ask for db copy");
        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }


    private void acctEditRequest(int accountId) {
        Log.d(TAG, "taskEditRequest: starts " + accountId);
        Log.d(TAG, "taskEditRequest: in two-pane mode (tablet) " + mTwoPane);

        try {
            accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
    //        setMenuItemVisible(R.id.menuacct_save, true);
            currFrag = AppFragType.ACCOUNTEDIT;
            AddEditActivityFragment editFragment = new AddEditActivityFragment();
    //        FragmentManager fragmentManager = getSupportFragmentManager();
    //        AddEditActivityFragment editFragment = (AddEditActivityFragment)fragmentManager.findFragmentById(R.id.task_details_container);

    //        if (editFragment == null) {
    //            Log.d(TAG, "acctEditRequest: create add/edit");
    //            editFragment = new AddEditActivityFragment();
    //        }
            Bundle arguments = new Bundle();
    //        arguments.putSerializable(Account.class.getSimpleName(), accountId);
            arguments.putInt(Account.class.getSimpleName(), accountId);
            editFragment.setArguments(arguments);

            Log.d(TAG, "taskEditRequest: twoPaneMode " + mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_details_container, editFragment)
                    .commit();


            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
            // Hide the left hand fragment and show the right hand frame
            View mainFragment = findViewById(R.id.fragment);
            View addEditLayout = findViewById(R.id.task_details_container);
            View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
            addEditLayout.setVisibility(View.VISIBLE);
            addEditLayoutScroll.setVisibility(View.VISIBLE);
            if(!mTwoPane) {
                mainFragment.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Edit build: " + e.getMessage());
        }

        Log.d(TAG, "Exiting taskEditRequest");
    }


//    private void saveAccountEdits() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        AddEditActivityFragment editFragment = (AddEditActivityFragment)fragmentManager.findFragmentById(R.id.task_details_container);
//        if (editFragment == null) {
//            Log.d(TAG, "saveAccountEdits: no edit frame to save");
//            return;
//        }
//        editFragment.saveEdits();
//
//
////        View addEditLayout = findViewById(R.id.task_details_container);
////        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
////        View mainFragment = findViewById(R.id.fragment);
////
////        if(!mTwoPane) {
////            if (editFragment != null) {
////                getSupportFragmentManager().beginTransaction()
////                        .remove(editFragment)
////                        .commit();
////            }
////
////            // We've just removed the editing fragment, so hide the frame
////            addEditLayout.setVisibility(View.GONE);
////            addEditLayoutScroll.setVisibility(View.GONE);
////
////            // and make sure the MainActivityFragment is visible.
////            mainFragment.setVisibility(View.VISIBLE);
////        }
////
////        AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                getSupportFragmentManager().findFragmentById(R.id.fragment);
////        listFragment.setAcctId(editFragment.getAcctId());
////
//////        setMenuItemVisible(R.id.menuacct_save, false);
//
//    }

    @Override
    public void saveComplete() {
        showConfirmationDialogOk(AppDialog.DIALOG_ID_EDITS_APPLIED);
    }

    @Override
    public void updateAccount(Account account) {
        this.account = account;
    }

    @Override
    public void updateNewAccount(Account account) {
        this.account = account;

        AccountListActivityFragment listFragment = (AccountListActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);

        listFragment.setAcctId(account.getId());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, account.getId()).apply();

        listFragment.notifyItemChanged();

//        searchListRequest();
    }

//    @Override
//    public void updateDictCorpName() {
////        AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                getSupportFragmentManager().findFragmentById(R.id.fragment);
//
////        listFragment.unselectItem();
//
////        searchListRequest();
//    }
//
//    //    private void addAccountRequest() {
//////        Log.d(TAG, "addAccountRequest: starts");
//////        if (mTwoPane) {
//////            mAccountAdapter.resetSelection();
////        accountMode = AccountsContract.ACCOUNT_ACTION_ADD;
////        this.account = new Account();
////        accountSelectedPos = -1;
////        setMenuItemEnabled(R.id.menuacct_delete, false);
//////        setMenuItemEnabled(R.id.menuacct_save, true);
////        setMenuItemEnabled(R.id.menuacct_internet, false);
////        updatePages(frag1Pos);
////    }


//    private void editAccountRequest(Account account) {
////        Log.d(TAG, "addAccountRequest: starts");
//        if (mTwoPane) {
////            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
//        } else {
////            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
//            // in single-pane mode, start the detail activity for the selected item Id.
//            Intent detailIntent = new Intent(this, AccountActivity.class);
//
//            if (account != null) { // editing an account
//                detailIntent.putExtra(Account.class.getSimpleName(), account);
////                detailIntent.putExtra(Account.class.getSimpleName(), AccountsContract.ACCOUNT_ACTION_CHG);
//                startActivityForResult(detailIntent, REQUEST_ACCOUNT_EDIT);
//            } else { // adding an account
////                detailIntent.putExtra(Account.class.getSimpleName(), AccountsContract.ACCOUNT_ACTION_ADD);
//                startActivityForResult(detailIntent, REQUEST_ACCOUNT_EDIT);
//            }
//        }
//    }


    private void resortList(final int sortorder) {

        progressBar.setVisibility(View.VISIBLE);

        switch (sortorder) {
            case AccountsContract.ACCOUNT_LIST_BY_CORP_NAME:
                getSupportActionBar().setTitle(getString(R.string.app_name_corpname));
                break;
            case AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE:
                getSupportActionBar().setTitle(getString(R.string.app_name_opendate));
                break;
            case AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID:
                getSupportActionBar().setTitle(getString(R.string.app_name_acctid));
                break;
            case AccountsContract.ACCOUNT_LIST_BY_SEQUENCE:
                getSupportActionBar().setTitle(getString(R.string.app_name_custom));
                break;
            default:
                getSupportActionBar().setTitle(getString(R.string.app_name));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        AccountListActivityFragment listFragment = (AccountListActivityFragment)
                                getSupportFragmentManager().findFragmentById(R.id.fragment);
                        listFragment.resortList(sortorder);

                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

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

    private void suggestsListRequest2() {
        Log.d(TAG, "suggestsListRequest2: starts");
        currFrag = AppFragType.PASSWORDS;
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

    private void suggestsListRequest() {
        Log.d(TAG, "suggestsListRequest: ");
        currFrag = AppFragType.PASSWORDS;
        SuggestListActivityFragment fragment = new SuggestListActivityFragment();

//        Bundle arguments = new Bundle();
//        arguments.putSerializable(Account.class.getSimpleName(), acct);
//        fragment.setArguments(arguments);

        Log.d(TAG, "suggestsListRequest: twoPaneMode " + mTwoPane);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.task_details_container, fragment)
                .commit();

    }


    private void suggestsListRequest3() {
//        onDateClicked(0);
        GregorianCalendar mCalendar = new GregorianCalendar();
//        Date dte = new Date(account.getActvyLong());
        Date dte = new Date();
        mCalendar.setTime(dte);
        Log.d(TAG, "onDateClicked: " + mCalendar.toString());

        showDatePickerDialog("Activity Date", 0, mCalendar);
    }

//    private void downloadUrl(String feedUrl) {
//        if (!feedUrl.equalsIgnoreCase(feedCachedUrl)) {
//            Log.d(TAG, "downloadURL: " + feedUrl);
//            DownloadData downloadData = new DownloadData();
//            downloadData.execute(feedUrl);
//            feedCachedUrl = feedUrl;
//            Log.d(TAG, "downloadURL: done");
//        } else {
//            Log.d(TAG, "downloadUrl: URL not changed");
//        }
//    }

    public void showAboutDialog() {
        @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.activity_about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(messageView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "onClick: Entering messageView.onClick, showing = " + mDialog.isShowing());
                if(mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);

//        messageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: Entering meassageView.onClick, showing = " + mDialog.isShowing());
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//            }
//        });

        TextView tv = (TextView) messageView.findViewById(R.id.about_version);
        tv.setText("v" + BuildConfig.VERSION_NAME);

        TextView about_url = (TextView) messageView.findViewById(R.id.about_url);
        if(about_url != null) {
            about_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String s = ((TextView) v).getText().toString();
                    intent.setData(Uri.parse(s));
                    try {
                        startActivity(intent);
                    } catch(ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "No browser application found, cannot visit world-wide web", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



        mDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL, feedUrl);
        outState.putInt(STATE_LIMIT, feedLimit);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: starts");
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
//                    switch (resultWhich) {
//                        case 1:
//                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//                            break;
//                        case 2:
//                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE);
//                            break;
//                        case 3:
//                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_SEQUENCE);
//                            break;
//                        case 4:
//                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID);
//                            break;
//                        case 8:
//                            suggestsListRequest();
//                            break;
//                        default:
//                            break;
//                    }
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
//            case REQUEST_ACCOUNT_EDIT:
//                accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//                break;
//            case REQUEST_ACCOUNT_SEARCH:
//                Log.d(TAG, "onActivityResult: return from search");
//                break;
            case REQUEST_VIEW_EXPORT:
                if (resultCode == RESULT_OK) {
                    boolean blnImported = data.getBooleanExtra("IMPORT", false);
                    Log.d(TAG, "onActivityResult: fileview imported? "  + blnImported);
//                    returnToMain();
                    if (blnImported) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
                        final AccountListActivityFragment listFragment = (AccountListActivityFragment)
                                getSupportFragmentManager().findFragmentById(R.id.fragment);
                        listFragment.setAccountSelectedPos(-1);
                        listFragment.setAcctId(-1);
                        loadSearchDB();
                        setMenuItemChecked(R.id.menuacct_sort_corpname, true);
                        resortList(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);

                        mainFragment.setVisibility(View.VISIBLE);
                        View addEditLayout = findViewById(R.id.task_details_container);
                        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
                        addEditLayout.setVisibility(View.GONE);
                        addEditLayoutScroll.setVisibility(View.GONE);

                        Log.d(TAG, "onActivityResult: refreshed");
                    }
                }
                break;


            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
        Log.d(TAG, "onResume: starts " + editing);


        if (editing) {
            super.onResume();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
//        mainFragment.setVisibility(View.GONE);
        isResumed = true;
//        Log.d(TAG, "onResume: isResumed " + isResumed);

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
                final int accountId = sharedPreferences.getInt(SELECTION_ONE_ITEM, -1);

//        Log.d(TAG, "onResume: qry/id " + queryResult + "/" + accountId);

                final AccountListActivityFragment listFragment = (AccountListActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (!queryResult.equals("")) {
                            listFragment.setQuery(queryResult);
                        }


                        Log.d(TAG, "run: the acctId " + accountId);

                        if (accountId == -1) {
                            listFragment.setAccountSelectedPos(-1);
                        } else {
                            if (listFragment.isOnDBById(accountId)) {


                                Log.d(TAG, "run: on selectAccount " + accountId);
                                listFragment.setAcctId(accountId);
    //                            Cursor cursorSearch = getContentResolver().query(
    //                                    AccountsContract.buildIdUri(accountId), null, null, null, null);


    //                            if (cursorSearch.getCount() == 0) {
    //                                searchListRequest();
    //                            } else {
                                acctEditRequest(accountId);
                            } else {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
                                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
                            }
    //                            }
                        }
                    //        sharedPreferences.edit().putBoolean(APP_RESUMED, true).apply();
                        View mainFragment = findViewById(R.id.fragment);
                        if (mainFragment.getVisibility() == View.VISIBLE) {

                            Toast.makeText(MainActivity.this,
                                    "Long click on item for more options",
                                    Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
    //                        mainFragment.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();;

//        String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//
//        Log.d(TAG, "onResume: return a value " + queryResult);

//        AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment);
//        listFragment.setQuery(queryResult);


//        if (queryResult.length() > 0) {
//
//            int queryResultId = sharedPreferences.getInt(SEARCH_ACCOUNT, -1);
//            Log.d(TAG, "onResume: queryResultsId " + queryResultId);
//            sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();;
//            if (queryResultId == -1) {
//                Intent detailIntent = new Intent(this, SearchListActivity.class);
//                detailIntent.putExtra(SearchListActivity.class.getSimpleName(), (int)-1);
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




//            Intent detailIntent = new Intent(this, AccountActivity.class);

//            detailIntent.putExtra(Account.class.getSimpleName(), account);
////            startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
//            startActivity(detailIntent);


//        }


//        onSearchRequested();

        super.onResume();
    }


    private void resetPreferences() {
        Log.d(TAG, "resetPreferences: starts");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
    }


    private void loadSearchDB() {

        progressBar.setVisibility(View.VISIBLE);

//        mHandler = new Handler();
//        mRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this,
//                        "Search Dictionary DB built",
//                        Toast.LENGTH_LONG).show();
//            }
//        };



        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteAllSearchItems();
                AccountListActivityFragment listFragment = (AccountListActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);
                listFragment.rebuildDict();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putBoolean(SEARCH_DICT_REFRESHED, true).apply();


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        AccountSearchLoaderCallbacks loaderAcctCallbacks = new AccountSearchLoaderCallbacks(MainActivity.this);
//                        getLoaderManager().restartLoader(SEARCH_LOADER_ID, null, loaderAcctCallbacks);
//                        loaderAcctCallbacks.loadAccountDictionary();


                        Toast.makeText(MainActivity.this,
                                "Search Dictionary DB built",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        }).start();
//        mHandler.post(mRunnable);
    }


    private void deleteAllSearchItems() {
//		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
//		String[] selectionArgs = { "account" };
//        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
        getContentResolver().delete(
                SearchesContract.CONTENT_URI_TRUNCATE,
                null, null);

    }


    @Override
    public boolean onSearchRequested() {
//        Log.d(TAG, "onSearchRequested: started");
        return super.onSearchRequested();
    }

//    @Override
//    public void onAccountDeleteClick(Account account) {
//        getContentResolver().delete(AccountsContract.buildIdUri(account.getId()), null, null);
//    }
//
//    @Override
//    public void onAccountEditClick(Account account) {
//        editAccountRequest(account);
//    }

//    @Override
//    public void onAccountsClicked() {
//        accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//    }
//
//    @Override
//    public void onSuggestsClicked() {
//        suggestsListRequest();
//    }

//    @Override
//    public void onSearchClicked() {
//        Intent detailIntent = new Intent(this, SearchActivity.class);
//        detailIntent.putExtra(SearchActivity.class.getSimpleName(), (int)-1);
////        detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
//        startActivity(detailIntent);
//    }

//    @Override
//    public void onAddAccountClicked() {
//        editAccountRequest(null);
//    }

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
        AccountListActivityFragment listFragment;

        switch (dialogId) {

            case AppDialog.DIALOG_ID_ASK_REFRESH_SEARCHDB:

                Log.d(TAG, "onPositiveDialogResult: request to rebuild search");
                loadSearchDB();


//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//
//                listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                listFragment.setQuery("");
//
//                Toast.makeText(this, "Long click on item for more options", Toast.LENGTH_LONG).show();

                    //                Intent detailIntent = new Intent(this, SearchActivity.class);
//                detailIntent.putExtra(SearchActivity.class.getSimpleName(), true);
//                startActivity(detailIntent);
                break;
            case AppDialog.DIALOG_ID_LEAVE_APP:
                finish();
                break;
            case AppDialog.DIALOG_ID_CANCEL_EDIT:
            case AppDialog.DIALOG_ID_CANCEL_EDIT_UP:
                // If we're editing, remove the fragment. Otherwise, close the app
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
                if(editFragment != null) {
                    // we were not editing
                    getSupportFragmentManager().beginTransaction()
                            .remove(editFragment)
                            .commit();
                    if(mTwoPane) {
//                        Log.d(TAG, "onPositiveDialogResult: get list");
//                        listFragment = (AccountListActivityFragment)
//                                getSupportFragmentManager().findFragmentById(R.id.fragment);
//                        listFragment.resetSelectItem();
                    } else {
                        // hide the edit container in single pane mode
                        // and make sure the left-hand container is visible
                        View addEditLayout = findViewById(R.id.task_details_container);
                        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
                        View mainFragment = findViewById(R.id.fragment);
                        // We're just removed the editing fragment, so hide the frame
                        addEditLayout.setVisibility(View.GONE);
                        addEditLayoutScroll.setVisibility(View.GONE);
//                        setMenuItemVisible(R.id.menuacct_save, false);

                        // and make sure the MainActivityFragment is visible
                        mainFragment.setVisibility(View.VISIBLE);


                    }
                } else {
                    // not editing, so quit regardless of orientation
                    finish();
                }
                break;
            case AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT:
                int acctId = args.getInt(AppDialog.DIALOG_ACCOUNT_ID);
                if (BuildConfig.DEBUG && acctId == 0) throw new AssertionError("Account Id is zero");
                Log.d(TAG, "onPositiveDialogResult: ready to delete " + acctId);
                deleteAccount(acctId);
                break;
            case AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT:
                acctEditRequest(-1);
                break;
            case AppDialog.DIALOG_ID_EDITS_APPLIED:
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        switch (dialogId) {

            case AppDialog.DIALOG_ID_ASK_REFRESH_SEARCHDB:

//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//
//                Log.d(TAG, "sharedPreferences: return a value " + queryResult);
//
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                listFragment.setQuery(queryResult);
//
//
//                if (queryResult.equals("")) {
//                    Toast.makeText(this, "Long click on item for more options", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(this, "List from search on " + queryResult, Toast.LENGTH_LONG).show();
//                }

                //                Intent detailIntent = new Intent(this, SearchActivity.class);
//        detailIntent.putExtra(SearchActivity.class.getSimpleName(), false);
//                startActivity(detailIntent);
                break;
            case AppDialog.DIALOG_ID_LEAVE_APP:
                case AppDialog.DIALOG_ID_CANCEL_EDIT:
            case AppDialog.DIALOG_ID_CANCEL_EDIT_UP:
            case AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT:
                break;
        }

    }

    private void deleteAccount(int acctId) {
        getContentResolver().delete(AccountsContract.buildIdUri((long)acctId), null, null);
        if (!mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
        sharedPreferences.edit().putBoolean(SEARCH_DICT_REFRESHED, false).apply();


//        AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment);
//        listFragment.setAcctId(-1);


        if (mTwoPane) {
            removeEditing();
        }


//        searchListRequest();
    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }


//    private class DownloadData extends AsyncTask<String, Void, String> {
//        private static final String TAG = "DownloadData";
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
////            Log.d(TAG, "onPostExecute: parameter is " + s);
//            ParseApplications parseApplications = new ParseApplications();
//            parseApplications.parse(s);
//
////            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
////                    MainActivity.this, R.layout.list_item, parseApplications.getApplications());
////            listApps.setAdapter(arrayAdapter);
//            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record,
//                    parseApplications.getApplications());
//            listApps.setAdapter(feedAdapter);
//
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d(TAG, "doInBackground: starts with " + strings[0]);
//            String rssFeed = downloadXML(strings[0]);
//            if (rssFeed == null) {
//                Log.e(TAG, "doInBackground: Error downloading");
//            }
//            return rssFeed;
//        }
//
//        @Nullable
//        private String downloadXML(String urlPath) {
//            StringBuilder xmlResult = new StringBuilder();
//
//            try {
//                URL url = new URL(urlPath);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                int response = connection.getResponseCode();
//                Log.d(TAG, "downloadXML: The response code was " + response);
////                InputStream inputStream = connection.getInputStream();
////                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
////                BufferedReader reader = new BufferedReader(inputStreamReader);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                int charsRead;
//                char[] inputBuffer = new char[500];
//                while (true) {
//                    charsRead = reader.read(inputBuffer);
//                    if (charsRead < 0) {
//                        break;
//                    }
//                    if (charsRead > 0) {
//                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
//                    }
//                }
//                reader.close();
//
//                return xmlResult.toString();
//            } catch (MalformedURLException e) {
//                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
//            } catch (IOException e) {
//                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
//            } catch (SecurityException e) {
//                Log.e(TAG, "downloadXML: Security Exception.  Needs permisson? " + e.getMessage());
////                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }


//    @Override
//    public void setSaveIcon(boolean setting) {
//        setMenuItemVisible(R.id.menuacct_save, setting);
//    }
//    @Override
//    public void onSaveClicked(int acctId) {
//        Log.d(TAG, "onSaveClicked: starts");
//
//        View addEditLayout = findViewById(R.id.task_details_container);
//        View mainFragment = findViewById(R.id.fragment);
//
//        if(!mTwoPane) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
//            if (fragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .remove(fragment)
//                        .commit();
//            }
//
//            // We've just removed the editing fragment, so hide the frame
//            addEditLayout.setVisibility(View.GONE);
//
//            // and make sure the MainActivityFragment is visible.
//            mainFragment.setVisibility(View.VISIBLE);
//        }
//
//        AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment);
//        listFragment.setAcctId(acctId);
//
//
//    }

//    @Override
//    public void onDateClicked(long acctActvyLong) {
//        Calendar mCalendar = Calendar.getInstance();
////        Date dte = new Date(account.getActvyLong());
//        Date dte = new Date();
//        mCalendar.setTime(dte);
//        Log.d(TAG, "onDateClicked: " + acctActvyLong);
//
//
////        showDatePickerDialog("Activity Date", 0, mCalendar);
//    }


    private void showDatePickerDialog(String title, int dialogId, GregorianCalendar mCalendar) {
        Log.d(TAG, "showDatePickerDialog: " + mCalendar.toString());
        DialogFragment dialogFragment = new DialogFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(DatePickerFragment.DATE_PICKER_ID, dialogId);
        arguments.putString(DatePickerFragment.DATE_PICKER_TITLE, title);
        arguments.putSerializable(DatePickerFragment.DATE_PICKER_DATE, mCalendar.getTime());


        dialogFragment.setArguments(arguments);
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
        Log.d(TAG, "Exiting showDatePickerDialog");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: ");

        // Check the id, so we know what to di wht the result
        int dialogId = (int) view.getTag();
        Log.d(TAG, "onDateSet: " + dialogId);
//        switch(dialogId) {
//            case DIALOG_FILTER:
//                mCalendar.set(year, month, dayOfMonth, 0, 0, 0);
//                applyFilter();
//                getSupportLoaderManager().restartLoader(LOADER_ID, mArgs, this);
//                break;
//            case DIALOG_DELETE:
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid mode when receiving DatePickerDialog result");
//        }

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment editFragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);


        if(editFragment == null) {
            showConfirmationLeaveApp();
            return;
        }

        if (!editFragment.canClose()) {
            showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT_UP);
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .remove(editFragment)
                .commit();
        if (!mTwoPane) {
            View addEditLayout = findViewById(R.id.task_details_container);
            View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
            addEditLayout.setVisibility(View.GONE);
            addEditLayoutScroll.setVisibility(View.GONE);
            mainFragment.setVisibility(View.VISIBLE);
            Toast.makeText(this,
                    "Long click select for more details",
                    Toast.LENGTH_SHORT).show();

//            super.onBackPressed();

        }

//
//
////                    Log.d(TAG, "onBackPressed: mainFragment gone");
////                    super.onBackPressed();
//            if (editFragment == null) {
//            } else if (editFragment.canClose()) {
////                returnToMain();
//                return;
//            }
        }


//            View mainFragment = findViewById(R.id.fragment);
//
//        if (!mTwoPane) {
//            showConfirmationLeaveApp();
//            return;
//        }
//
//        if (editFragment == null) {
//            showConfirmationLeaveApp();
//        } else if (editFragment.canClose()) {
//
//        } else {
//            showConfirmationLeaveApp();
////                    showConfirmationLeaveApp();
//            return;
//        }
//
////            super.onBackPressed();
////                showConfirmationLeaveApp();
//            return;
////        } else {
////            showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT);
////            return;
//
//
//
//
////            SuggestListActivityFragment suggestFragment = (SuggestListActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
////            if (suggestFragment == null) {
//////            super.onBackPressed();
////                showConfirmationLeaveApp();
////            } else {
////                showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT);
////            }
//
//    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void showConfirmationLeaveApp() {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_LEAVE_APP);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_leave_warning));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.confirmdiag_leave_app_warning_sub_message));
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.confirmdiag_leave_negative_caption);
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.confirmdiag_leave_positive_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }

    private void showConfirmationDialog(int dialogId) {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, dialogId);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_leave_warning));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.confirmdiag_leave_warning_sub_message));
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.confirmdiag_ask_abandon_negative_caption);
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.confirmdiag_ask_abandon_positive_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }


    private void showAddConfirmationDialog(int dialogId) {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, dialogId);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_add));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.confirmdiag_add_sub_message));
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.confirmdiag_add_negative_caption);
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.confirmdiag_add_positive_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }


    private void showConfirmationDialogOk(int dialogId) {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, dialogId);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_OK);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.confirmdiag_edits));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.confirmdiag_edits_sub_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.ok);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

    }



}
