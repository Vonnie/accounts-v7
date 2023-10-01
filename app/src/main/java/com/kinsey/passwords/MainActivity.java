package com.kinsey.passwords;

import android.accounts.Account;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.auth.AuthUI;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.dropbox.core.v2.users.FullAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.provider.Task;
import com.kinsey.passwords.tools.ProfileJsonListIO;
import com.kinsey.passwords.uifrag.AddEditProfileFrag;
import com.kinsey.passwords.uifrag.ProfileCorpNameFrag;
import com.kinsey.passwords.uifrag.ProfileCustomFrag;
import com.kinsey.passwords.uifrag.ProfileOpenDateFrag;
import com.kinsey.passwords.uifrag.ProfilePassportIdFrag;
import com.kinsey.passwords.uifrag.SearchFrag;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.GregorianCalendar;
//import java.util.List;
import java.util.List;
import java.util.Locale;

//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.v2.DbxClientV2;

// ====================
// Statement to assist in debugging
// if (BuildConfig.DEBUG && acctId == 0) throw new AssertionError("Account Id is zero");
//

// https://www.youtube.com/watch?v=FtIc5UYXeKk
// https://www.youtube.com/watch?v=t-yZUqthDMM
// https://square.github.io/picasso/
// https://medium.com/fullstack-with-react-native-aws-serverless-and/google-sign-in-for-react-native-android-7d43df78c082


public class MainActivity extends AppCompatActivity
        implements
        ProfileCorpNameFrag.OnProfileCorpNameClickListener,
        AddEditProfileFrag.OnProfileModifyClickListener,
//        ProfilePassportIdFrag.OnProfilePassportIdClickListener,
//        ProfileOpenDateFrag.OnProfileOpenDateClickListener,
//        ProfileCustomFrag.OnProfileCustomClickListener,
        SearchFrag.OnSearchClickListener,
        Task {
//        AppDialog.DialogEvents {
//        DatePickerDialog.OnDateSetListener {

//    AddEditActivityFragment.OnListenerClicked,
//    AccountListActivityFragment.OnAccountListClickListener,
//    Filterable,
//    AccountRecyclerViewAdapter.OnAccountClickListener,

//    ViewPager.OnPageChangeListener,
//    CursorRecyclerViewAdapter.OnSuggestClickListener,
//        AccountRecyclerViewAdapter.OnAccountClickListener,
//        MainActivityFragment.OnActionListener,

    public static final String TAG = "MainActivity";
    public static final String ARG_LISTSORT = "ARG_LISTSORT";
    public static final String ARG_SELECTED_ID = "ARG_SELECTED_ID";
    public static final String ARG_IS_SHEARCH_SHOWN = "ARG_IS_SHEARCH_SHOWN";
    public static final String ARG_EDIT_MODE_ADD = "ARG_EDIT_MODE_ADD";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
//    private boolean isLandscape = false;
    private Boolean editModeAdd = false;

//    private static final String ACCOUNT_FRAGMENT = "AccountFragment";

//    private FirebaseAuth mAuth;
    private static final String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);
    public static String BACKUP_FILENAME = "accounts.json";
    public static int profileMigrateLevel = 1;


    private boolean isSearchShown = false;
    public static boolean migrationStarted = false;

//    private boolean isTablet = getResources().getBoolean(R.bool.isTablet);
//    private boolean isTablet = false;
    private int selectedId = -1;

//    GoogleSignInClient mGoogleSignInClient;

//    private List<Profile> profileListFull;
//    private List<Profile> profileListFullCustom;
    //    private List<Profile> profileList;

    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";
    private int feedLimit = 10;
    private String feedCachedUrl = "INVALIDATED";
    public static final String STATE_URL = "feedUrl";
    public static final String STATE_LIMIT = "feedLimit";
    private ListView listApps;
    //    private ViewPager mViewPager;
//    private int fragListPos = -1;
//    private int frag1Pos = 0;
//    private int frag2Pos = 1;
//    private int frag3Pos = 2;

    public static boolean migration2Complete = false;

    public static final int ACCOUNT_LOADER_ID = 1;
    public static final int SEARCH_LOADER_ID = 2;
    public static final int SUGGEST_LOADER_ID = 3;

    public static final int ADD_PROFILE_REQUEST = 1;
    public static final int EDIT_PROFILE_REQUEST = 2;
//    public static final int REQUEST_ACCOUNTS_LIST = 3;
//    public static final int REQUEST_SUGGESTS_LIST = 4;
//    public static final int REQUEST_ACCOUNT_EDIT = 5;
//    public static final int REQUEST_ACCOUNT_SEARCH = 6;
    public static final int REQUEST_VIEW_EXPORT = 7;
    public static final int REQUEST_SIGN = 8;
    public static final int GOOGLE_SIGN = 9;

    public static int accountSelectedPos = -1;

    private final String APP_RESUMED = "Resumed";
    private final String SELECTION_QUERY = "Query";
    private final String SELECTION_ONE_ITEM = "SeLectionOneItem";
    private final String SORTED_LIST_ORDER = "SortedListOrder";
    public final static String SEARCH_DICT_REFRESHED = "SearchDictRefreshed";

    public static final int LISTSORT_CORP_NAME = 1;
    public static final int LISTSORT_PASSPORT_ID = 2;
    public static final int LISTSORT_OPEN_DATE = 3;
    public static final int LISTSORT_CUSTOM_SORT = 4;
    public static int listsortOrder = LISTSORT_CORP_NAME;

    Menu menu;
//    MenuItem miActionProgressItem;
//    public static ProgressBar progressBar;
//    private Handler mHandler = new Handler();
//    Runnable mRunnable;
//    boolean isUserPaging = true;
//    private static Account account = new Account();
    private final AlertDialog mDialog = null;
    private GregorianCalendar mCalendar;
//    private boolean appMsgSent = false;
//    private boolean isResumed = false;
//    private boolean conversionStarted = false;

//    private int accountMode = AccountsContract.ACCOUNT_ACTION_ADD;

    //    View addEditLayout;
//    View addEditLayoutScroll;
//    View mainFragment;
//    View fragCorpName;
//    View fragCustom;

    RecyclerView recyclerView;
//    public static ProfileViewModel profileViewModel;
//    public static ProfileAdapter adapter = new ProfileAdapter();
//    private Profile profileMaxItem;
    private int currentMaxSeq = 0;
    private boolean itemAdded = false;
    FrameLayout frameSearch, frame2;
//    boolean has2ndPanel = false;

//    public static ProfileAdapter adapterCorpName = new ProfileAdapter();
//
//    public static ProfileAdapter adapterOpenDate = new ProfileAdapter();
//
//    public static ProfileAdapter adapterPassportId = new ProfileAdapter();
//
//    public static ProfileAdapter adapterCustomSort = new ProfileAdapter();

    private LinearLayoutManager layoutManager;

    private static final String ACCESS_TOKEN = "d14k27w8xbyq9ex";

//    private SearchView mSearchView;
    private ActivityResultLauncher<Intent> startForResultLauncher;

    private ProfileAdapter adapter;
    private ProfileViewModel profileViewModel;

    public MainActivity() {
    }



    private enum ListHomeType {
        LISTACCOUNTS,
        TOP10FREEAPP,
        TOP25FREEAPP,
        TOPTVEPISODE,
        TOPTVSEASONS
    }

    ListHomeType currList = ListHomeType.LISTACCOUNTS;

    private enum AppFragType {
        ACCOUNTEDIT,
        PASSWORDS
    }

    AppFragType currFrag = AppFragType.ACCOUNTEDIT;

    private static String pattern_ymdtime = "yyyy-MM-dd HH:mm:ss.0";
    public static SimpleDateFormat format_ymdtime = new SimpleDateFormat(
            pattern_ymdtime, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mainV1);
//        setContentView(R.layout.activity_account_list);
//        setContentView(R.layout.activity_main_rss_list);


//        TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
////            Toast.makeText(MainActivity.this, "Detected... You're using a Tablet", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "onCreate: isTablet");
//            this.isTablet = true;
//        } else {
////            Toast.makeText(MainActivity.this, "Detected... You're using a Mobile Phone", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "onCreate: isPhone");
//        }


//        ______________________________________________________
//        temp removed until find out how to change config
//        ______________________________________________________
//        // Replace with a known container that you can safely add a
//        // view to where the view won't affect the layout and the view
//        // won't be replaced.
//        ViewGroup container = binding.container;
//
//        container.addView(new View(this) {
//            @Override
//            protected void onConfigurationChanged(Configuration newConfig) {
//                super.onConfigurationChanged(newConfig);
////                computeWindowSizeClasses();
//                setContentView(R.layout.activity_main);
//            }
//        });
//        ______________________________________________________

//        computeWindowSizeClasses();
        setContentView(R.layout.activity_main);

//        _________________________________________________________
//        Screen size not calc this way
//        _________________________________________________________
//        int screenLayout = getResources().getConfiguration().screenLayout;
//        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
//
//        switch (screenLayout) {
//            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    setContentView(R.layout.activity_main_large_land);
////                    setContentView(R.layout.activity_main);
////                    LinearLayoutCompat layout = recyclerView.findViewById(R.id.layout);
////                    layout.setOrientation(LinearLayoutCompat.OrientationMode());
////                    Toast.makeText(MainActivity.this, "Detected... XLarge Landscape", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Xlarge Landscape");
//                } else {
//                    setContentView(R.layout.activity_main);
////                    Toast.makeText(MainActivity.this, "Detected... XLarge Portrait", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Xlarge Portrait");
//                }
//
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    setContentView(R.layout.activity_main_large_land);
////                    setContentView(R.layout.activity_main);
////                    Toast.makeText(MainActivity.this, "Detected... Large Landscape", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Large Landscape");
//                } else {
//                    setContentView(R.layout.activity_main);
////                    Toast.makeText(MainActivity.this, "Detected... Large Portrait", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Large Portrait");
//                }
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                setContentView(R.layout.activity_main);
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////                    Toast.makeText(MainActivity.this, "Detected... Normal Landscape", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Normal landscape");
//                } else {
////                    Toast.makeText(MainActivity.this, "Detected... Normal Portrait", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "screen size Normal Portrait");
//                }
//                break;
//            default:
//                setContentView(R.layout.activity_main);
////                Toast.makeText(MainActivity.this, "Undetected... ", Toast.LENGTH_LONG).show();
//        }






        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setTitle(getString(R.string.app_name_corpname));
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_test2_foreground);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ProfileCorpNameFrag fragment = ProfileCorpNameFrag.newInstance(this.LISTSORT_CORP_NAME, this.selectedId);
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            FrameLayout frame = findViewById(R.id.fragment_container);
            Log.d(TAG, "onCreate: tag " + frame.getTag());

//            if (isTablet | frame.getTag().equals("sw600")
//                    |  frame.getTag().equals("large_land")
//                    |  frame.getTag().equals("main_large_land")) {
////                Log.d(TAG, "onCreate: show frag2");
////                FragmentManager fragmentManager2 = getSupportFragmentManager();
////                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
////
////                AddEditProfileFrag fragment2 = new AddEditProfileFrag();
////                fragmentTransaction2.add(R.id.fragment_container2, fragment2);
////                fragmentTransaction2.commit();
//                frame2 = findViewById(R.id.fragment_container2);
//                frame2.setVisibility(View.GONE);
//            }
            frame2 = findViewById(R.id.fragment_container2);
            frame2.setVisibility(View.GONE);
            FragmentManager fragmentManager3 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
            SearchFrag searchFrag = new SearchFrag();
            fragmentTransaction3.add(R.id.fragment_search_container, searchFrag);
            fragmentTransaction3.commit();
            frameSearch = findViewById(R.id.fragment_search_container);
            frameSearch.setVisibility(View.GONE);
//            showWarning();
        } else {
            this.isSearchShown = savedInstanceState.getBoolean(ARG_IS_SHEARCH_SHOWN, false);
            listsortOrder = savedInstanceState.getInt(ARG_LISTSORT, 1);
            this.selectedId = savedInstanceState.getInt(ARG_SELECTED_ID);
            this.editModeAdd = savedInstanceState.getBoolean(ARG_EDIT_MODE_ADD);
            Log.d(TAG, "onCreate: listsortOrder " + listsortOrder);
            FrameLayout frame = findViewById(R.id.fragment_container);
            Log.d(TAG, "onCreate: tag " + frame.getTag());
            frame2 = findViewById(R.id.fragment_container2);
            Log.d(TAG, "onCreate: rotate selectedId " + this.selectedId);
            if (this.selectedId == -1 && !this.editModeAdd) {
                frame2.setVisibility(View.GONE);
            } else {
                frame2.setVisibility(View.VISIBLE);

//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                AddEditProfileFrag fragment2 = new AddEditProfileFrag();
//                fragmentTransaction.replace(R.id.fragment_container2, fragment2, "AddEditProfileFrag");
//                fragmentTransaction.commit();

            }

            frameSearch = findViewById(R.id.fragment_search_container);
            if (isSearchShown) {
                frameSearch.setVisibility(View.VISIBLE);
            } else {
                frameSearch.setVisibility(View.GONE);
            }
        }

        startForResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        if (activityResult.getResultCode() == RESULT_OK) {
                            // Handle the data returned from Activity B
//                            LiveData<List<Word>> returnedData = mWordViewModel.getAllWords();
                            // Use the returnedData as needed
//                            Intent data = result.getData();
//                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();

//                            if (result == RESULT_OK) {
                                boolean blnRestored = data.getBooleanExtra(FileViewActivity.EXTRA_LIST_RESTORED, false);
                                if (blnRestored) {
                                    Log.d(TAG, "onActivityResult fileView: data restored " + blnRestored);
//                                  mWordViewModel.insert(data);
                                } else {
                                    Log.d(TAG, "onActivityResult fileView: data not restored " + blnRestored);
                                }
//                            }
                    } else {
                        Log.d(TAG, "onActivityResult fileView: canceled");
                    }
                                }
                });

//        if (findViewById(R.id.fragment_container2) == null) {
//            Log.d(TAG, "onCreate: has null 2nd container");
//            has2ndPanel = false;
//        } else {
//            Log.d(TAG, "onCreate: has a 2nd container");
//            has2ndPanel = true;
//        }

//        ================================================================================
//        Future growth for firebase signin
//        ================================================================================

        //        mAuth = FirebaseAuth.getInstance();
//
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
//                .Builder()
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//        btn_login.setOnClickListener(v -> SignInGoogle());

//        if (mAuth.getCurrentUser() != null) {
//            FirebaseUser user = mAuth.getCurrentUser();
//            updateUI(user);
//        }

//        fragCorpName = findViewById(R.id.fragment_container_corpname);
//        fragCorpName.setVisibility(View.VISIBLE);
//
//        fragCustom = findViewById(R.id.fragment_container_custom);
//        fragCustom.setVisibility(View.GONE);

//        FloatingActionButton buttonAddPofile = findViewById(R.id.button_add_profile);
////        buttonAddPofile.setImageResource(R.drawable.ic_audiotrack_light);
//        buttonAddPofile.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddEditProfileActivity.class);
//                Log.d(TAG, "onCreate: start activity AddEditProfileActivity");
//                intent.putExtra(Profile.class.getSimpleName(), ADD_PROFILE_REQUEST);
//                startActivity(intent);
////                startActivityForResult(intent, ADD_PROFILE_REQUEST);
////                startActivity(intent);
//
////                onSignInClickedButton(v);
//            }
//        });


//        progressBar = findViewById(R.id.progressBar);


//        recyclerView = findViewById(R.id.recycler_view);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//
//        boolean isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//        if (isLandscape) {
//            GridLayoutManager layoutManager;
//            layoutManager = new GridLayoutManager(this, 2);
//            recyclerView.setLayoutManager(layoutManager);
//        } else {
//            LinearLayoutManager layoutManager;
//            layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//        }
//
//        layoutManager = new LinearLayoutManager(this);
//
////        this.adapter = new ProfileAdapter();
////        adapter = adapterCorpName;
//        recyclerView.setAdapter(adapter);
//

//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//        profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
//            @Override
//            public void onChanged(List<Profile> profiles) {
//
//                profileListFull = new ArrayList<>(profiles);
//                adapter.submitList(profiles);
//            }
//        });
//
//        profileViewModel.getMaxSequence().observe(this, new Observer<Profile>() {
//            @Override
//            public void onChanged(@Nullable Profile profile) {
//
//                if (profile == null) {
////                    profileMaxItem = new Profile(0, "", "", "", "");
//                    currentMaxSeq = 0;
//                } else {
////                    profileMaxItem = profile;
//                    currentMaxSeq = profile.getSequence();
////                    Log.d(TAG, profile);
//                }
//
////                Log.d(TAG, "maxSeq " + profileMaxItem.getSequence());
////                if (!conversionStarted && profileMaxItem.getSequence() == 0) {
////                    Log.d(TAG, "Seq " + profileMaxItem.getSequence());
////                    if (!migrationStarted) {
////                        Log.d(TAG, "migration not started");
////                    } else {
////                        conversionStarted = true;
//////                    addSample();
//////                        migratePassport();
////                    }
////                }
//            }
//        });

//
//        profileViewModelCustom = new ViewModelProvider(this).get(ProfileViewModel.class);
//        profileViewModelCustom.getAllProfilesCustomSort().observe(this, new Observer<List<Profile>>() {
//            @Override
//            public void onChanged(List<Profile> profiles) {
//
//                profileListFullCustom = new ArrayList<>(profiles);
//                adapterCustom.submitList(profiles);
//            }
//        });





//        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Profile profile) {
//                Intent intent = new Intent(MainActivity.this, AddEditProfileActivity.class);
//                intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_USER_EMAIL, profile.getUserEmail());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
//                intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
//                intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
//                intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
//
//                Log.d(TAG, "edit requested");
//                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
//
//            }
//        });

//        recyclerView.onScreenStateChanged(int state);

//        isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//        Log.d(TAG, "onCreate: twoPane is " + isLandscape);

//        if (savedInstanceState != null) {
//            listsortOrder = savedInstanceState.getInt("listsortOrder", 1);
////            setMenuItemChecked(R.id.menuacct_sort_corpname, true);
//        }

//        listApps = (ListView) findViewById(R.id.xmlListView);
//        if (savedInstanceState != null) {
//            feedUrl = savedInstanceState.getString(STATE_URL);
//            feedLimit = savedInstanceState.getInt(STATE_LIMIT);
//        }
//        feedUrl = String.format("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml", feedLimit);

//        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
//        downloadUrl(feedUrl);


//        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_account);
//        buttonAddNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                acctEditRequest(-1);
//            }
//        });


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        // If the AddEditActivity fragment exists, we're editing
//        editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
////        Log.d(TAG, "onCreate: editing is " + editing);
//
//        // We need references to the containers, so we can show or hide them as necessary.
//        // No need to cast them, as we're only calling a method that's available for all views.
//        View addEditLayout = findViewById(R.id.task_details_container);
//        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
//        mainFragment = findViewById(R.id.fragment);
//        progressBar  = findViewById(R.id.progressBar);
////        progressBar.setVisibility(View.VISIBLE);
////        progressBar.setVisibility(View.GONE);
//
//        if(isLandscape) {
//            Log.d(TAG, "onCreate: twoPane mode");
//            mainFragment.setVisibility(View.VISIBLE);
//            addEditLayout.setVisibility(View.VISIBLE);
//            addEditLayoutScroll.setVisibility(View.VISIBLE);
//        } else if (editing) {
//            Log.d(TAG, "onCreate: single pane, editing");
//            // hide the left hand fragment, to make room for editing
//            mainFragment.setVisibility(View.GONE);
//        } else {
//            Log.d(TAG, "onCreate: single pane, not editing");
//            // Show left hand fragment
//            mainFragment.setVisibility(View.VISIBLE);
//            // Hide the editing frame
//            addEditLayout.setVisibility(View.GONE);
//            addEditLayoutScroll.setVisibility(View.GONE);
//        }


//        if (mSearchView != null) {
//            Log.d(TAG, "onCreate: activated " + mSearchView.isActivated());
//        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);




//        progressBar.setVisibility(View.VISIBLE);



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
//        SuggestDatabaseV1 suggestDatabase = SuggestDatabaseV1.getInstance(this);
//        final SQLiteDatabase dbSuggest = suggestDatabase.getReadableDatabase();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add_profile);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    private OnScrollListener onScrollListener = new OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            int visibleItemCount = layoutManager.getChildCount();
//            int totalItemCount = layoutManager.getItemCount();
//            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//            int lastVisibleItem = firstVisibleItemPosition +visibleItemCount;
//            Toast.makeText(MainActivity.this, "Visible Item Total:"+String.valueOf(visibleItemCount), Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "scroll " + String.valueOf(visibleItemCount) + ":" + String.valueOf(totalItemCount));
//            Log.d(TAG, "scroll " + String.valueOf(firstVisibleItemPosition) + ":" + String.valueOf(lastVisibleItem));
//
//        }
//    };

//    private void migratePassport() {
//
//        Log.d(TAG, "migration level " + profileMigrateLevel);
//        String[] projection = {AccountsContract.Columns._ID_COL,
//                AccountsContract.Columns.PASSPORT_ID_COL,
//                AccountsContract.Columns.CORP_NAME_COL,
//                AccountsContract.Columns.USER_NAME_COL,
//                AccountsContract.Columns.USER_EMAIL_COL,
//                AccountsContract.Columns.CORP_WEBSITE_COL,
//                AccountsContract.Columns.NOTE_COL,
//                AccountsContract.Columns.OPEN_DATE_COL,
//                AccountsContract.Columns.ACTVY_DATE_COL,
//                AccountsContract.Columns.SEQUENCE_COL,
//                AccountsContract.Columns.REF_FROM_COL,
//                AccountsContract.Columns.REF_TO_COL
//        };
//
//        String sortOrder = String.format("%s COLLATE NOCASE ASC, %s COLLATE NOCASE ASC",
//                AccountsContract.Columns.CORP_NAME_COL,
//                AccountsContract.Columns.SEQUENCE_COL);
//        Log.d(TAG, "onCreate: projection " + Arrays.toString(projection));
//        ContentResolver contentResolver = getContentResolver();
//
//////        Cursor cursor = contentResolver.query(AccountsContract.buildTaskUrl(2),
//        Cursor cursor = contentResolver.query(AccountsContract.CONTENT_URI,
//                projection,
//                null,
//                null,
//                sortOrder);
//
//        if (cursor == null) {
//            Log.d(TAG, "onCreate: null cursor");
//        } else {
//            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//            List<Account> listAccounts = new ArrayList<Account>();
//            while (cursor.moveToNext()) {
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: ===========================================");
//                Account item = new Account(
//                        cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
//                        cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
//                        cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
//                item.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
//                item.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
//                item.setActvyLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL)));
//                item.setNote(cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.NOTE_COL)));
//                listAccounts.add(item);
//            }
//
//
//            cursor.close();
//
//            for (Account account : listAccounts) {
//                Profile profile = new Profile(
//                        account.getSequence(),
//                        account.getCorpName(),
//                        account.getUserName(),
//                        account.getUserEmail(),
//                        account.getCorpWebsite()
//                );
//                profile.setPassportId(account.getPassportId());
//                profile.setOpenLong(account.getOpenLong());
//                profile.setActvyLong(account.getActvyLong());
//                profile.setNote(account.getNote());
//                profileViewModel.insertProfile(profile);
//            }
//
//        }
//
//    }

//    private void addSample() {
//        addSample("Vonnie Test 1", 100);
//        addSample("Vonnie Test 2", 101);
//        addSample("Vonnie Test 3", 102);
//    }
//    private void addSample(String corpName, int sequence) {
//        ContentResolver contentResolver = getContentResolver();
//        ContentValues values = new ContentValues();
//
//        values.put(AccountsContract.Columns.PASSPORT_ID_COL,
//                String.valueOf(1));
//        values.put(AccountsContract.Columns.CORP_NAME_COL, corpName);
//        values.put(AccountsContract.Columns.USER_NAME_COL, "Vonnie");
//        values.put(AccountsContract.Columns.USER_EMAIL_COL, "vonniekinsey@gmail.com");
//        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, "");
//        values.put(AccountsContract.Columns.NOTE_COL, "");
//        values.put(AccountsContract.Columns.SEQUENCE_COL, sequence);
//        values.put(AccountsContract.Columns.REF_FROM_COL, 0);
//        values.put(AccountsContract.Columns.REF_TO_COL, 0);
//
//        long ms = System.currentTimeMillis();
//        values.put(AccountsContract.Columns.OPEN_DATE_COL, ms);
//        values.put(AccountsContract.Columns.ACTVY_DATE_COL, ms);
//
//        Uri uri = contentResolver.insert(AccountsContract.CONTENT_URI, values);
//        long id = AccountsContract.getId(uri);
//        Account account = getAccount((int)(id));
//
//        values = new ContentValues();
//
//        values.put(AccountsContract.Columns.PASSPORT_ID_COL,
//                String.valueOf(id));
//
//        contentResolver.update(AccountsContract.buildIdUri(account.getId()), values, null, null);
//    }

//    private Account getAccount(int id) {
////        int iId = 0;
//        Cursor cursor = getContentResolver()
//                .query(AccountsContract.buildIdUri(id), null, null, null, null);
//        if (cursor == null) {
//            return null;
//        } else {
//            if (cursor.moveToFirst()) {
//                Account account = AccountsContract.getAccountFromCursor(cursor);
//                cursor.close();
//                return account;
//            }
//            cursor.close();
//            return null;
//        }
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "onConfigurationChanged: landscape");
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "onConfigurationChanged: portrait");
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "onConfigurationChanged: unk");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_LISTSORT, listsortOrder);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment profileFragment;
//        profileFragment = fragmentManager.findFragmentByTag("ProfileCorpNameFrag");
//        ProfileCorpNameFrag frag = (ProfileCorpNameFrag)profileFragment;
//        if (frag == null) {
//            profileFragment = fragmentManager.findFragmentByTag("ProfileCorpNameFrag");
//            ProfileCorpNameFrag frag = (ProfileCorpNameFrag)profileFragment;
//
//        }

        outState.putInt(ARG_SELECTED_ID, this.selectedId);
        outState.putBoolean(ARG_IS_SHEARCH_SHOWN, this.isSearchShown);
        outState.putBoolean(ARG_EDIT_MODE_ADD, this.editModeAdd);

//        if (listsortOrder == LISTSORT_CORP_NAME) {
//            profileFragment = fragmentManager.findFragmentByTag("ProfileCorpNameFrag");
//            ProfileCorpNameFrag frag = (ProfileCorpNameFrag)profileFragment;
//            if (frag == null) {
//                outState.putInt(ARG_SELECTED_ID, -1);
//                Log.d(TAG, "onSaveInstanceState: selectedId null frag");
//            } else {
//                outState.putInt(ARG_SELECTED_ID, frag.getSelectedId());
//                Log.d(TAG, "onSaveInstanceState: selectedId " + frag.getSelectedId());
//            }
//        } else if (listsortOrder == LISTSORT_PASSPORT_ID) {
//            profileFragment = fragmentManager.findFragmentByTag("ProfilePassportIdFrag");
//            ProfilePassportIdFrag frag = (ProfilePassportIdFrag)profileFragment;
//            if (frag == null) {
//                outState.putInt(ARG_SELECTED_ID, -1);
//            } else {
//                outState.putInt(ARG_SELECTED_ID, frag.getSelectedId());
//            }
//        } else if (listsortOrder == LISTSORT_OPEN_DATE) {
//            profileFragment = fragmentManager.findFragmentByTag("ProfileOpenDateFrag");
//            ProfileOpenDateFrag frag = (ProfileOpenDateFrag)profileFragment;
//            if (frag == null) {
//                outState.putInt(ARG_SELECTED_ID, -1);
//            } else {
//                outState.putInt(ARG_SELECTED_ID, frag.getSelectedId());
//            }
//        } else if (listsortOrder == LISTSORT_CUSTOM_SORT) {
//            profileFragment = fragmentManager.findFragmentByTag("ProfileCustomFrag");
//            ProfileCustomFrag frag = (ProfileCustomFrag)profileFragment;
//            if (frag == null) {
//                outState.putInt(ARG_SELECTED_ID, -1);
//            } else {
//                outState.putInt(ARG_SELECTED_ID, frag.getSelectedId());
//            }
//        }

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

        if (listsortOrder == LISTSORT_CORP_NAME) {
            menu.findItem(R.id.menuacct_sort_corpname).setChecked(true);
        } else if (listsortOrder == LISTSORT_PASSPORT_ID) {
            menu.findItem(R.id.menuacct_sort_passport).setChecked(true);
        } else if (listsortOrder == LISTSORT_OPEN_DATE) {
            menu.findItem(R.id.menuacct_sort_opendate).setChecked(true);
        } else if (listsortOrder == LISTSORT_CUSTOM_SORT) {
            menu.findItem(R.id.menuacct_sort_custom).setChecked(true);
        }

        Log.d(TAG, "onCreateOptionsMenu: listsortOrder " + this.listsortOrder);
//        View view = menu.findItem(R.id.button_item).getActionView();
//
//        String[] ITEM_ACTIONS = new String[] {"Account Edit", "Search", "Passwords"};
//
//        ArrayAdapter adapter = new ArrayAdapter<>(this,
//                R.layout.dropdown_menu_popup_item, ITEM_ACTIONS);
////        ArrayAdapter<String> adapter =
////                new ArrayAdapter<String>(
////                        getContext(),
////                        R.layout.dropdown_menu_popup_item,
////                        COUNTRIES);
//
//        AutoCompleteTextView editTextFilledExposedDropdown =
//                view.findViewById(R.id.filled_exposed_dropdown);
//        editTextFilledExposedDropdown.setAdapter(adapter);
//        editTextFilledExposedDropdown.setText(adapter.getItem(0).toString());


//        https://stackoverflow.com/questions/31231609/creating-a-button-in-android-toolbar
//        MenuItem item = menu.findItem(R.id.button_item);
//        Button btn = item.getActionView().findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Toolbar Button Clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });


        return true;
    }




    //        ================================================================================
//        Future growth for firebase signin
//        ================================================================================


//    public void onSignInClickedButton2(View view) {
////        progressBar.
//        Intent signIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signIntent, GOOGLE_SIGN);
//    }
//
//    public void onSignInClickedButton(View view) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            Toast.makeText(getApplicationContext(), "User already signed in, must sign out firebase", Toast.LENGTH_SHORT).show();
//        } else {
//
//            Log.d(TAG, "signing in");
//            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                    new AuthUI.IdpConfig.EmailBuilder().build());
////                    new AuthUI.IdpConfig.PhoneBuilder().build(),
////                    new AuthUI.IdpConfig.GoogleBuilder().build());
//
//            // Create and launch sign-in intent
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(providers)
//                            .build(),
//                    REQUEST_SIGN);
//
//        }
//    }
//
//    public void onSignOut(View view) {
//        AuthUI.getInstance()
//                .signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplication(), "User has signed out!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//  =============================================================================================
//  =============================================================================================


//    ===================================================================================================
//      Deprecated Search Method
//
//    ===================================================================================================

//    private void searchTextEntry(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu: search starting");
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) menu.findItem(R.id.menumain_search).getActionView();
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
//        mSearchView.setSearchableInfo(searchableInfo);
//
//        mSearchView.setIconified(true);
//
//        mSearchView.setOnSearchClickListener(new SearchView.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: v " + v.toString());
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                boolean isDictRefreshed = sharedPreferences.getBoolean(SEARCH_DICT_REFRESHED, false);
//                if (isDictRefreshed) {
//                    Log.d(TAG, "onClick: yes dict refreshed");
//                } else {
//                    Log.d(TAG, "onClick: no dict refreshed");
//                    Toast.makeText(getApplicationContext(), "One moment to populate search dictionary", Toast.LENGTH_LONG).show();
//                    loadSearchDB();
//                    isDictRefreshed = sharedPreferences.getBoolean(SEARCH_DICT_REFRESHED, false);
//                    Log.d(TAG, "onClick: dict refreshed");
//                }
//
//            }
//        });
//
//        //        mSearchView.clearFocus();
////        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////        String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//
////        if (queryResult.equals("")) {
////            setMenuItemChecked(R.id.menumain_ifSearch, false);
////        } else {
////            setMenuItemChecked(R.id.menumain_ifSearch, true);
////        }
//
//
////        Log.d(TAG, "onCreateOptionsMenu: activated " + mSearchView.isActivated());
//
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "onQueryTextSubmit: called " + query);
//                mSearchView.clearFocus();
////                AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
//                if (cursor == null) {
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                    sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//                    return false;
//                }
//                if (cursor.getCount() == 1) {
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                    int accountId = getSearchAccountId(0);
//                    Log.d(TAG, "onQueryTextSubmit: accountId " + accountId);
//                    sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, accountId).apply();
////                    listFragment.setQuery("");
////                    listFragment.setAcctId(accountId);
////                    int pos = listFragment.getAccountSelectedPos();
////                    Log.d(TAG, ": pos " + pos);
////                    if (pos == -1) {
////                        searchListRequest();
////                    } else {
////                        acctEditRequest(accountId);
////                    }
//                    return false;
//                }
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, query).apply();
//                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//
//                //                showSuggestions();
//
////                SearchesContract.cursorSearch = mSearchView.getSuggestionsAdapter().getCursor();
//
////                Log.d(TAG, "onQueryTextSubmit: #searches " + mSearchView.getSuggestionsAdapter().getCursor().getCount());
//
//                Log.d(TAG, "onQueryTextSubmit: showSearches");
//
////                showSearches();
////                finish();
////                listFragment.setQuery(query);
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                Log.d(TAG, "onQueryTextChange: adt " + mSearchView.getSuggestionsAdapter().getCount());
////                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////                sharedPreferences.edit().putString(SELECTION_QUERY, newText).apply();
//
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//
//                return false;
//            }
//
//
//        });
//
//
//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Log.d(TAG, "SearchView onClose: starts");
////                showSuggestions();
////                finish();
//                mSearchView.clearFocus();
////                setMenuItemChecked(R.id.menumain_ifSearch, false);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                listFragment.setQuery("");
//
//
////                Intent detailIntent = new Intent(this, AccountListActivity.class);
////                detailIntent.putExtra(Account.class.getSimpleName(), sortorder);
////                startActivityForResult(detailIntent, REQUEST_ACCOUNTS_LIST);
//
//                return false;
//            }
//        });
//
//        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                Log.d(TAG, "onSuggestionSelect: position " + position);
////                showSuggestions();
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                Log.d(TAG, "onSuggestionClick: position " + position);
////                showSuggestions();
//                mSearchView.clearFocus();
//                int accountId = getSearchAccountId(position);
//                Log.d(TAG, "onSuggestionClick: accountId " + accountId);
////                showOneSearches(accountId, position);
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, accountId).apply();
//                Log.d(TAG, "onSuggestionClick: finish");
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
////                listFragment.setQuery("");
////                listFragment.setAcctId(accountId);
////                int pos = listFragment.getAccountSelectedPos();
////                Log.d(TAG, "onSuggestionClick: pos " + pos);
////                if (pos == -1) {
////                    searchListRequest();
////                } else {
////                    acctEditRequest(accountId);
////                }
////                //                finish();
//
//                return false;
//            }
//        });
//
//        Log.d(TAG, "onCreateOptionsMenu: returned " + true);
//
//
////        return true;
//    }


//    private int getSearchAccountId(int position) {
////        Log.d(TAG, "showAccount: pos " + position);
//        mSearchView.getSuggestionsAdapter().getCursor().moveToFirst();
//        mSearchView.getSuggestionsAdapter().getCursor().move(position);
////        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(3));
////        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getString(3));
////        Log.d(TAG, "showAccount: " + mSearchView.getSuggestionsAdapter().getCursor().getColumnName(5));
//        int dbId = Integer.valueOf(mSearchView.getSuggestionsAdapter().getCursor()
//                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA)));
////        Log.d(TAG, "showAccount: " + dbId);
////        String corpName = mSearchView.getSuggestionsAdapter().getCursor()
////                .getString(mSearchView.getSuggestionsAdapter().getCursor().getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
////        Log.d(TAG, "showAccount: corpName " + corpName);
//
//        return dbId;
//
//    }
//  ==================================================================================================
//    End of commented depreciated search method
//    ================================================================================================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";

//        Log.d(TAG, "onOptionsItemSelected: menu");
//        if (!mSearchView.isIconified()) {
//            mSearchView.setIconified(true);
//        }
//        AddEditActivityFragment editFragment;
        FragmentTransaction transaction;
        FragmentManager fragmentManager;
        Fragment profileFragment;
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        switch (id) {
//            case R.id.menumain_add:
////                editAccountRequest(null);
////                addAccountRequest();
//                showAddConfirmationDialog(AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT);
//                break;
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

                listsortOrder = LISTSORT_CORP_NAME;

                Log.d(TAG, "request frag Corp Name");

//                fragmentManager = getSupportFragmentManager();
//                profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//
//                if (profileFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(profileFragment)
//                            .commit();
//
//                }

                profileFragment = ProfileCorpNameFrag.newInstance(LISTSORT_CORP_NAME, this.selectedId);
                transaction = getSupportFragmentManager().beginTransaction();

//                transaction.add(R.id.fragment_container, profileCorpNameFrag);
                transaction.replace(R.id.fragment_container, profileFragment, "profileFragment");
                transaction.addToBackStack(null);

                transaction.commit();

                if (this.selectedId != -1) {
                    ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
                    frag.refreshListAll();
                    frag.setSelectedId(this.selectedId);
                }

//                Fragment profileCorpNameFrag = new ProfileCorpNameFrag();
//                transaction = getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, profileCorpNameFrag);
//                transaction.addToBackStack(null);
//
//                transaction.commit();


                Log.d(TAG, "new corp name frag committed");

//                adapter = adapterCorpName;
//                recyclerView.setAdapter(adapter);

//                adapter = new ProfileAdapter();

//                recyclerView.setAdapter(adapter);

//                profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
//                    @Override
//                    public void onChanged(List<Profile> profiles) {
//                        profileListFull = new ArrayList<>(profiles);
//                        adapter.submitList(profiles);
//                    }
//
//                });

//                recyclerView.scrollToPosition(0);
//                this.adapter.notifyDataSetChanged();


//                resortList(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
                return true;

            case R.id.menuacct_sort_opendate:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }

                listsortOrder = LISTSORT_OPEN_DATE;


//                fragmentManager = getSupportFragmentManager();
//                profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//
//                if (profileFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(profileFragment)
//                            .commit();
//                }

//                Fragment profileOpenDateFrag = new ProfileOpenDateFrag();
                profileFragment = ProfileCorpNameFrag.newInstance(LISTSORT_OPEN_DATE, this.selectedId);
                transaction = getSupportFragmentManager().beginTransaction();

//                transaction.add(R.id.fragment_container, profileOpenDateFrag);
                transaction.replace(R.id.fragment_container, profileFragment, "profileFragment");
                transaction.addToBackStack(null);

                transaction.commit();

                if (this.selectedId != -1) {
                    ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
                    frag.refreshListAll();
                    frag.setSelectedId(this.selectedId);
                }

//                //                adapter = adapterOpenDate;
////                recyclerView.setAdapter(adapter);
//                adapter = new ProfileAdapter();
//
//                profileViewModel.getAllProfilesByOpenDate().observe(this, new Observer<List<Profile>>() {
//                    @Override
//                    public void onChanged(List<Profile> profiles) {
//                        profileListFull = new ArrayList<>(profiles);
//                        adapter.submitList(profiles);
//                    }
//                });
//
//                recyclerView.scrollToPosition(0);
//                this.adapter.notifyDataSetChanged();

//                resortList(AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE);
                return true;

            case R.id.menuacct_sort_passport:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }

                listsortOrder = LISTSORT_PASSPORT_ID;

//                fragmentManager = getSupportFragmentManager();
//                profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//
//                if (profileFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(profileFragment)
//                            .commit();
//
//                }

//                Fragment profilePassportIdFrag = new ProfilePassportIdFrag();
                profileFragment = ProfileCorpNameFrag.newInstance(LISTSORT_PASSPORT_ID, this.selectedId);
                transaction = getSupportFragmentManager().beginTransaction();

//                transaction.add(R.id.fragment_container, profilePassportIdFrag);
                transaction.replace(R.id.fragment_container, profileFragment, "profileFragment");
                transaction.addToBackStack(null);

                transaction.commit();

//                if (this.selectedId != -1) {
//                    ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
//                    frag.refreshListAll();
//                    frag.setSelectedId(this.selectedId);
//                }


//                Fragment profileCustomFrag = new ProfileCorpNameFrag();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, profileCustomFrag);
//                transaction.addToBackStack(null);
//
//                transaction.commit();



////                adapter = adapterPassportId;
////                recyclerView.setAdapter(adapter);
//                adapter = new ProfileAdapter();
//
//                Log.d(TAG, "sort by id");
//
//                profileViewModel.getAllProfilesByPassportId().observe(this, new Observer<List<Profile>>() {
//                    @Override
//                    public void onChanged(List<Profile> profiles) {
//                        profileListFull = new ArrayList<>(profiles);
//                        adapter.submitList(profiles);
//                        recyclerView.scrollToPosition(0);
//                        adapter.notifyDataSetChanged();
//                    }
//                });


//                resortList(AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID);
                return true;

            case R.id.menuacct_sort_custom:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }

                Log.d(TAG, "request frag Custom");
                listsortOrder = LISTSORT_CUSTOM_SORT;


//                fragmentManager = getSupportFragmentManager();
//                profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//
//                if (profileFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(profileFragment)
//                            .commit();
//
//                }

//                Fragment profileCustomFrag = new ProfileCustomFrag();
                profileFragment = ProfileCorpNameFrag.newInstance(LISTSORT_CUSTOM_SORT, this.selectedId);
                transaction = getSupportFragmentManager().beginTransaction();

//                transaction.add(R.id.fragment_container, profileCustomFrag);
                transaction.replace(R.id.fragment_container, profileFragment, "profileFragment");
                transaction.addToBackStack(null);

                transaction.commit();


                if (this.selectedId != -1) {
                    ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
                    frag.refreshListAll();
                    frag.setSelectedId(this.selectedId);
                }

                Log.d(TAG, "new frag Custom committed");

//                this.adapterCustomSort = new ProfileAdapter();
//                recyclerView.setAdapter(adapterCustomSort);

//                adapter = new ProfileAdapter();
//                adapter = adapterCustomSort;
//                recyclerView.setAdapter(adapter);
//                recyclerView.swapAdapter(adapterCustomSort, true);

//                recyclerView.setAdapter(adapterCustom);
//                profileViewModel.getAllProfilesCustomSort().observe(this, new Observer<List<Profile>>() {
//                    @Override
//                    public void onChanged(List<Profile> profiles) {
//                        profileListFull = new ArrayList<>(profiles);
//                        adapter.submitList(profiles);
//                    }
//                });

//                recyclerView.scrollToPosition(0);
//                this.adapter.notifyDataSetChanged();

//                this.adapter = new ProfileAdapter();
//                recyclerView.setAdapter(adapter);

//                resortList(AccountsContract.ACCOUNT_LIST_BY_SEQUENCE);
                return true;

//            case R.id.menuacct_showdate:
//                suggestsListRequest3();
//                break;

//            case R.id.menuacct_save:
//                saveAccountEdits();
//                break;

            case R.id.menumain_showSuggests:
                suggestsListRequest4();
                return true;

//            case R.id.menumain_showSuggestsV1:
//                suggestsListRequest2();
//                break;

//            case R.id.menumain_showProfile:
//                profileRequest();
//                break;


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
                return true;


            //            case R.id.menumain_refresh:
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                if (listFragment.getQueryCorp().equals("")) {
//                    listFragment.unselectItem();
//                } else {
//                    listFragment.setQuery("");
//                }
//
//                removeEditFrag();
//
//                Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
//                break;


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


            case R.id.menumain_search:
                item.setChecked(!item.isChecked());
                isSearchShown = item.isChecked();
                if (item.isChecked()) {
                    frameSearch.setVisibility(View.VISIBLE);
                    this.editModeAdd = false;
                    this.selectedId = -1;
                    frame2 = findViewById(R.id.fragment_container2);
                    frame2.setVisibility(View.GONE);
//                    FrameLayout frame = findViewById(R.id.fragment_container);
//                    frame.setVisibility(View.GONE);
                } else {
                    frameSearch.setVisibility(View.GONE);
////                    FrameLayout frame = findViewById(R.id.fragment_container);
////                    frame.setVisibility(View.VISIBLE);
//                    fragmentManager = getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    profileFragment = (ProfileCorpNameFrag) fragmentManager.findFragmentById(R.id.fragment_container);
//                    Log.d(TAG, "onOptionsItemSelected: selectedId " + this.selectedId);
//                    if (profileFragment != null) {
//                        ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        if (this.selectedId == -1) {
//                            frame2.setVisibility(View.GONE);
//                        } else {
//                            frame2.setVisibility(View.VISIBLE);
//                        }
//                    }


//                    if (profileFragment instanceof ProfileCorpNameFrag) {
//                        ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frame2.setVisibility(View.GONE);
//                    } else if (profileFragment instanceof ProfileCustomFrag) {
//                        ProfileCustomFrag frag = (ProfileCustomFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frame2.setVisibility(View.GONE);
//                    } else if (profileFragment instanceof ProfileOpenDateFrag) {
//                        ProfileOpenDateFrag frag = (ProfileOpenDateFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frame2.setVisibility(View.GONE);
//                    } else if (profileFragment instanceof ProfilePassportIdFrag) {
//                        ProfilePassportIdFrag frag = (ProfilePassportIdFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frame2.setVisibility(View.GONE);
//                    }
                }
//                searchRequestActivity();
                return true;


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

//                showAboutDialog();
                showAboutActivity();

                return true;

            case R.id.menumain_notify:

                showWarning();

                return true;

            case R.id.menumain_dropbox:
                showDropbox();
                return true;

//            case R.id.menumain_do_request:
//
//                resequenceList();
//
//                break;

//        =====================================
//            case android.R.id.home:
//
//                showConfirmationLeaveApp();
//
//                break;
//          ========================================

//                Log.d(TAG, "onOptionsItemSelected: home button pressed");
//
//
//                editFragment = (AddEditActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
//
//                if(editFragment == null) {
//                    showConfirmationLeaveApp();
//                    break;
//                }
//
//                if (!editFragment.canClose()) {
//                    showConfirmationDialog(AppDialog.DIALOG_ID_CANCEL_EDIT_UP);
//                    break;
//                }
//
//
//
//                getSupportFragmentManager().beginTransaction()
//                        .remove(editFragment)
//                        .commit();
//                if (isLandscape) {
//                    return false;
//                } else {
//                    mainFragment.setVisibility(View.VISIBLE);
//                    View addEditLayout = findViewById(R.id.task_details_container);
//                    View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
//                    addEditLayout.setVisibility(View.GONE);
//                    addEditLayoutScroll.setVisibility(View.GONE);
//                    Toast.makeText(this,
//                            "Long click select for more details",
//                            Toast.LENGTH_SHORT).show();
//
//                    return true;
//                }


//
//                    if (isLandscape) {
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
//                if (isLandscape) {
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
////                FileViewActivityFragmentV1 fvFragment = (FileViewActivityFragmentV1)
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }



//    =======================================================================================
//    remove code Fragment 2pane technique which is no longer used
//    =======================================================================================

//    private void removeEditFrag() {
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
//
//        if (editFragment != null) {
//
//            getSupportFragmentManager().beginTransaction()
//                    .remove(editFragment)
//                    .commit();
//
//            mainFragment.setVisibility(View.VISIBLE);
//
//            if (!isLandscape) {
//                View addEditLayout = findViewById(R.id.task_details_container);
//                View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
//                addEditLayout.setVisibility(View.GONE);
//                addEditLayoutScroll.setVisibility(View.GONE);
//            }
//
//        }
//    }

    private void resequenceList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String stringFragmentName = fragmentManager.findFragmentById(R.id.fragment_container).getClass().getSimpleName();

        Log.d(TAG, stringFragmentName);
        if (stringFragmentName.equals("ProfileCustomFrag")) {
            Log.d(TAG, "about to re-sequence");
            Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            ((ProfileCustomFrag) profileFragment).resequenceList();
        } else {
            Log.d(TAG, "no ProfileCustomFrag available");
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

//    private void setMenuItemEnabled(int id, boolean blnSet) {
//        MenuItem item = menu.findItem(id);
//        item.setEnabled(blnSet);
//    }

//    private void setMenuItemVisible(int id, boolean blnSet) {
//        Log.d(TAG, "setMenuItemVisible: id " + id);
//        if (menu == null) {
//            Log.d(TAG, "setMenuItemVisible: ");
//            return;
//        }
//        MenuItem item = menu.findItem(id);
//        if (blnSet) {
//            item.setVisible(true);
//            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        } else {
//            item.setVisible(false);
//            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//        }
//    }


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

//        // Choose a directory using the system's file picker.
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        startActivity(intent);

//        Log.d(TAG, adapter.getItemCount() + " count on db");
//        mActivityStart = true;
        Intent detailIntent = new Intent(this, FileViewActivity.class);
//        startActivity(detailIntent);

        startForResultLauncher.launch(detailIntent);

//        Old way
//        startActivityForResult(detailIntent, REQUEST_VIEW_EXPORT);
//    __________________________________________________________
    }


//    ============================================================================================
//    obsolete account list manipulate
//    ===========================================================================================

//    @Override
//    public void onAccountListSelect(Account account) {
//        Log.d(TAG, "onAccountListSelect: " + account.getCorpName());
////        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
////        Log.d(TAG, "onAccountListSelect: selected pos " + selected_position);
////        fragList.setSelected_position(selected_position);
////        accountSelectedPos = selected_position;
////        setMenuItemEnabled(R.id.menuacct_delete, true);
////        setMenuItemEnabled(R.id.menuacct_save, false);
////        setMenuItemVisible(R.id.menuacct_save, false);
////        if (account.getCorpWebsite().equals("")) {
////            setMenuItemEnabled(R.id.menuacct_internet, false);
////        } else {
////            setMenuItemEnabled(R.id.menuacct_internet, true);
////        }
//        this.account = account;
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, this.account.getId()).apply();
//
//
////        if (isLandscape) {
////            acctEditRequest(this.account.getId());
////        }
//
//        Toast.makeText(this,
//                "Long click select for more details",
//                Toast.LENGTH_SHORT).show();
//
////        mSectionsPagerAdapter.destroyItem(mViewPager, frag1Pos, frag1);
////        mSectionsPagerAdapter.destroyItem(mViewPager, frag2Pos, frag2);
////        mSectionsPagerAdapter.destroyItem(mViewPager, frag3Pos, frag3);
////        int currPage = mViewPager.getCurrentItem();
//
////        updatePages(currPage);
////        updatePages(frag1Pos);
//
//    }
//==============================================================================================

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
////        if (!isLandscape && accountSortorder == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
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





    private void searchRequestActivity() {

        Intent detailIntent = new Intent(this, SearchActivity.class);
        detailIntent.putExtra(Profile.class.getSimpleName(), "sortorder");
        startActivity(detailIntent);

    }

    private void showAboutActivity() {

        Intent detailIntent = new Intent(this, AboutActivity.class);
        startActivity(detailIntent);

    }


    private void suggestsListRequest4() {
        Log.d(TAG, "suggestsListRequest3: starts");
        currFrag = AppFragType.PASSWORDS;
//        if (isLandscape) {
//        } else {
//        }

        // Try to invoke the intent.
        try {
            Intent detailIntent = new Intent(this, SuggestListActivity.class);
            detailIntent.putExtra(Suggest.class.getSimpleName(), "sortorder");
            startActivity(detailIntent);
        } catch (ActivityNotFoundException e) {
            // Define what your app should do if no activity can handle the intent.
            Log.d(TAG, "suggestsListRequest4: ActivityNotFoundException ");
        }

    }

    private void showDropbox() {
        Log.d(TAG, "showDropbox: starts");

//        ProfileJsonListIO profileJsonListIO = new ProfileJsonListIO();
//        List<Account> listAccounts = profileJsonListIO.loadAccounts();
        getAccounts();
        Log.d(TAG, "viewModel db count " + adapter.getItemCount());
        Log.d(TAG, "viewModel db count " + this.profileViewModel.getAllProfilesByCorpName());
//        Log.d(TAG, "viewModel db count " + accountJsonProperties());
//        backupFilename();
////        currFrag = AppFragType.PASSWORDS;
//        // Create Dropbox client
//        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
//        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
//        Log.d(TAG, "showDropbox ");
    }

    private void getAccounts() {

        adapter = new ProfileAdapter(-1);
        this.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                adapter.submitList(profiles);
//                if (onFirstReported) {
//                    onFirstReported = false;
////                    infoPage();
//                }
                Log.d(TAG, "viewModel db count " + adapter.getItemCount());
            }
        });
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

    public void backupFilename() {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainActivity.this);

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
        final android.app.AlertDialog filenameDialog = mBuilder.create();

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
                    Toast.makeText(MainActivity.this, R.string.toast_enter_email, Toast.LENGTH_LONG).show();
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
                alertBackup(file, getString(R.string.fv_msg_33) + ' '
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
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
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


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
//                new DownloadProfileAsyncTask(getApplicationContext(),
//                        file, adapter, progressBar, webView).execute();
//                dialog.dismiss();
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

    private void shareExport() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = dlg.setIcon(getResources().getDrawable(R.drawable.ic_warning));
        dlg.setTitle(getResources().getString(R.string.app_name))
                .setMessage("Is the exported file up-to-date for this share.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        shareIntent();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: starts");

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, "cancelled");
            return;
        }

//        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
//        Log.d(TAG, "onActivityResult: resultCode " + resultCode);
        // Check which request we're responding to
        switch (requestCode) {
//            case ADD_PROFILE_REQUEST: {
//                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
//                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
//                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
//                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
////                int sequence = data.getIntExtra(AddEditProfileActivity.EXTRA_SEQUENCE, 0);
//                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);
//
//                Profile profile = new Profile(currentMaxSeq + 1,
//                        corpName, userName, userEmail, corpWebsite);
//                profile.setNote(note);
//                long lngDate = data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0);
//                Log.d(TAG, "add profile openDate " + lngDate);
//                profile.setOpenLong(lngDate);
//                profile.setActvyLong(System.currentTimeMillis());
////                profile.setActvyLong((new Date()).getTime());
//
//                profileViewModel.insertProfile(profile, this);
//
//                itemAdded = true;
//
//                Log.d(TAG, "profile added");
//                Toast.makeText(this, R.string.toast_profile_added, Toast.LENGTH_SHORT).show();
//                break;
//            }

//            case EDIT_PROFILE_REQUEST: {
//                int id = data.getIntExtra(AddEditProfileActivity.EXTRA_ID, -1);
//
//                if (id == -1) {
//                    Toast.makeText(this, R.string.toast_error_profile_not_updated, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                int passportId = data.getIntExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, 0);
//                int sequence = data.getIntExtra(AddEditProfileActivity.EXTRA_SEQUENCE, 0);
//                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
//                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
//                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
//                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
//                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);
//
//                Profile profile = new Profile(sequence, corpName, userName, userEmail, corpWebsite);
//                profile.setId(id);
//                profile.setPassportId(passportId);
//                profile.setNote(note);
//                long lngDate = data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0);
//                profile.setOpenLong(lngDate);
//                profile.setActvyLong(System.currentTimeMillis());
//
//                profileViewModel.update(profile);
//                Toast.makeText(this, R.string.toast_profile_updated, Toast.LENGTH_SHORT).show();
//                break;
//            }

            case REQUEST_VIEW_EXPORT:
                Log.d(TAG, "onActivityResult: fileView return");
                boolean blnRestored = data.getBooleanExtra(FileViewActivity.EXTRA_LIST_RESTORED, false);
                if (blnRestored) {
                    this.selectedId = -1;
                    frame2.setVisibility(View.GONE);
                    frameSearch.setVisibility(View.GONE);
                    FrameLayout frame = findViewById(R.id.fragment_container);
                    frame.setVisibility(View.VISIBLE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
                    if (profileFragment instanceof ProfileCorpNameFrag) {
                        ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
                        frag.setSelectedId(this.selectedId);
                        frag.refreshListAll();
//                    } else if (profileFragment instanceof ProfileCustomFrag) {
//                        ProfileCustomFrag frag = (ProfileCustomFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frag.refreshListAll();
//                    } else if (profileFragment instanceof ProfileOpenDateFrag) {
//                        ProfileOpenDateFrag frag = (ProfileOpenDateFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frag.refreshListAll();
//                    } else if (profileFragment instanceof ProfilePassportIdFrag) {
//                        ProfilePassportIdFrag frag = (ProfilePassportIdFrag) profileFragment;
//                        frag.setSelectedId(this.selectedId);
//                        frag.refreshListAll();
                    }
                }
                break;
//      ____________________________________________________________
//            Firebase signin to remove or temp take out
//      ____________________________________________________________
//            case REQUEST_SIGN: {
//                Log.d(TAG, "onActivityResult 1234");
//                if (resultCode == RESULT_OK) {
//                    Log.d(TAG, "onActivityResult Ok");
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    Log.d(TAG, "onActivityResult user " + user);
//                    Toast.makeText(getApplicationContext(), R.string.toast_signin, Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d(TAG, "resultCode " + resultCode + ":" + RESULT_OK);
//                    Log.d(TAG, "data " + data);
//                    Toast.makeText(getApplicationContext(), R.string.toast_unable_to_signin, Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            }
//        ================================================================================

//            case GOOGLE_SIGN: {
//                Task<GoogleSignInAccount> task = GoogleSignIn
//                        .getSignedInAccountFromIntent(data);
//
//
//                try {
//
//                    GoogleSignInAccount account = task.getResult(ApiException.class);
//                    if (account != null) firebaseauthWithGoogle(account);
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//        ================================================================================


            //            case REQUEST_ACCOUNTS_LIST: {
//
//                // Make sure the request was successful
//                if (resultCode == RESULT_OK) {
////                    Log.d(TAG, "onActivityResult: success");
//                    int resultWhich = data.getIntExtra("which", 0);
////                    Log.d(TAG, "onActivityResult: which " + resultWhich);
////                    switch (resultWhich) {
////                        case 1:
////                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
////                            break;
////                        case 2:
////                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE);
////                            break;
////                        case 3:
////                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_SEQUENCE);
////                            break;
////                        case 4:
////                            accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID);
////                            break;
////                        case 8:
////                            suggestsListRequest();
////                            break;
////                        default:
////                            break;
////                    }
//                    // The user picked a contact.
//                    // The Intent's data Uri identifies which contact was selected.
//
//                    // Do something with the contact here (bigger example below)
//                }
//                break;
//            }
//            case REQUEST_SUGGESTS_LIST:
//                if (resultCode == RESULT_OK) {
//                    String result = data.getStringExtra("result");
////                    Log.d(TAG, "onActivityResult: result " + result);
//                    suggestsListRequest();
//                }
//                break;
//            case REQUEST_ACCOUNT_EDIT:
//                accountsListRequest(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//                break;
//            case REQUEST_ACCOUNT_SEARCH:
//                Log.d(TAG, "onActivityResult: return from search");
//                break;
//            case REQUEST_VIEW_EXPORT:
//                if (resultCode == RESULT_OK) {
//                    boolean blnImported = data.getBooleanExtra("IMPORT", false);
////                    Log.d(TAG, "onActivityResult: fileview imported? "  + blnImported);
////                    returnToMain();
//                    if (blnImported) {
//
//                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//                        final AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                                getSupportFragmentManager().findFragmentById(R.id.fragment);
//                        listFragment.setAccountSelectedPos(-1);
//                        listFragment.setAcctId(-1);
//                        loadSearchDB();
//                        setMenuItemChecked(R.id.menuacct_sort_corpname, true);
//                        resortList(AccountsContract.ACCOUNT_LIST_BY_CORP_NAME);
//
//                        mainFragment.setVisibility(View.VISIBLE);
//                        View addEditLayout = findViewById(R.id.task_details_container);
//                        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
//                        addEditLayout.setVisibility(View.GONE);
//                        addEditLayoutScroll.setVisibility(View.GONE);
//
////                        Log.d(TAG, "onActivityResult: refreshed");
//                    }
//                }
//                break;


            default:
                break;
        }


    }


    //        ================================================================================
//        Future growth for firebase signin
//        ================================================================================


//    private void firebaseauthWithGoogle(GoogleSignInAccount account) {
//        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider
//                .getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
////                       progressBar.setVisible(View.VISIBLE);
//                        Log.d(TAG, "signin success");
//
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        updateUI(user);
//                    } else {
////                        progressBar.setVisibility(View.INVISIBLE);
//                        Log.w(TAG, "signin failure", task.getException());
//
//                        Toast.makeText(this, "Signin Failed!", Toast.LENGTH_SHORT).show();
//                        updateUI(null);
//                    }
//                });
//    }
//
//    private void updateUI(FirebaseUser user) {
//
//        if (user != null) {
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String photo = String.valueOf(user.getPhotoUrl());
//
//            text("Info : \n");
//            text.append(name + "\n");
//            text.append(email);
//
//            Picasso.get().load(photo).into(image);
//            btn_login.setVisibility(View.INVISIBLE);
//            btn_logout.setVisibility(View.VISIBLE);
//
//        } else {
//
//            test.setText("Firebase Login");
//            Picasso.get().load(R.drawable.ic_firebase_logo).into(image);
//            btn_login.setVisibility(View.VISIBLE);
//            btn_logout.setVisibility(View.INVISIBLE);
//
//        }
//    }
//
//    void Logout() {
//
//        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this,
//                        task -> updateUI(null));
//                }
//
//        ================================================================================
//        ================================================================================

//
//    @Override
//    protected void onResume() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
//        Log.d(TAG, "onResume: starts " + editing);
//
//
//        if (editing) {
//            super.onResume();
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
////        mainFragment.setVisibility(View.GONE);
//        isResumed = true;
////        Log.d(TAG, "onResume: isResumed " + isResumed);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                final String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
//                final int accountId = sharedPreferences.getInt(SELECTION_ONE_ITEM, -1);
//
////        Log.d(TAG, "onResume: qry/id " + queryResult + "/" + accountId);
//
//                final AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (!queryResult.equals("")) {
//                            listFragment.setQuery(queryResult);
//                        }
//
//
//                        Log.d(TAG, "run: the acctId " + accountId);
//
//                        if (accountId == -1) {
//                            listFragment.setAccountSelectedPos(-1);
//                        } else {
//                            if (listFragment.isOnDBById(accountId)) {
//
//
//                                Log.d(TAG, "run: on selectAccount " + accountId);
//                                listFragment.setAcctId(accountId);
//    //                            Cursor cursorSearch = getContentResolver().query(
//    //                                    AccountsContract.buildIdUri(accountId), null, null, null, null);
//
//
//    //                            if (cursorSearch.getCount() == 0) {
//    //                                searchListRequest();
//    //                            } else {
//                                acctEditRequest(accountId);
//                            } else {
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//                                sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//                            }
//    //                            }
//                        }
//                    //        sharedPreferences.edit().putBoolean(APP_RESUMED, true).apply();
//                        View mainFragment = findViewById(R.id.fragment);
//                        if (mainFragment.getVisibility() == View.VISIBLE) {
//
//                            Toast.makeText(MainActivity.this,
//                                    "Long click on item for more options",
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        progressBar.setVisibility(View.GONE);
//    //                        mainFragment.setVisibility(View.VISIBLE);
//
//                    }
//                });
//            }
//        }).start();
//
////        String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
////
////        Log.d(TAG, "onResume: return a value " + queryResult);
//
////        AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                getSupportFragmentManager().findFragmentById(R.id.fragment);
////        listFragment.setQuery(queryResult);
//
//
////        if (queryResult.length() > 0) {
////
////            int queryResultId = sharedPreferences.getInt(SEARCH_ACCOUNT, -1);
////            Log.d(TAG, "onResume: queryResultsId " + queryResultId);
////            sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();;
////            if (queryResultId == -1) {
////                Intent detailIntent = new Intent(this, SearchListActivity.class);
////                detailIntent.putExtra(SearchListActivity.class.getSimpleName(), (int)-1);
////                startActivity(detailIntent);
////            } else {
////                resetPreferences();
////                Cursor cursor = getContentResolver().query(
////                        AccountsContract.buildIdUri(queryResultId), null, null, null, null);
////                if (cursor.moveToFirst()) {
////                    Intent detailIntent = new Intent(this, AccountActivity.class);
////                    Account account = AccountsContract.getAccountFromCursor(cursor);
////                    detailIntent.putExtra(Account.class.getSimpleName(), account);
////                    Log.d(TAG, "showAccount: account " + account.toString());
////                }
////            }
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
////        }
//
//
////        onSearchRequested();
//
//        super.onResume();
//    }


//    ===================================================================================
//    obsolete shared preference method
//    ===================================================================================

//    private void resetPreferences() {
//        Log.d(TAG, "resetPreferences: starts");
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//    }


//    private void loadSearchDB() {
//
//        progressBar.setVisibility(View.VISIBLE);
//
////        mHandler = new Handler();
////        mRunnable = new Runnable() {
////
////            @Override
////            public void run() {
////                progressBar.setVisibility(View.GONE);
////                Toast.makeText(MainActivity.this,
////                        "Search Dictionary DB built",
////                        Toast.LENGTH_LONG).show();
////            }
////        };
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                deleteAllSearchItems();
//                AccountListActivityFragment listFragment = (AccountListActivityFragment)
//                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//                listFragment.rebuildDict();
//
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                sharedPreferences.edit().putBoolean(SEARCH_DICT_REFRESHED, true).apply();
//
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
////                        AccountSearchLoaderCallbacks loaderAcctCallbacks = new AccountSearchLoaderCallbacks(MainActivity.this);
////                        getLoaderManager().restartLoader(SEARCH_LOADER_ID, null, loaderAcctCallbacks);
////                        loaderAcctCallbacks.loadAccountDictionary();
//
//
//                        Toast.makeText(MainActivity.this,
//                                "Search Dictionary DB built",
//                                Toast.LENGTH_LONG).show();
//                        progressBar.setVisibility(View.GONE);
//
//                    }
//                });
//            }
//        }).start();
////        mHandler.post(mRunnable);
//    }


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
//        Intent detailIntent = new Intent(this, SearchActivityV1.class);
//        detailIntent.putExtra(SearchActivityV1.class.getSimpleName(), (int)-1);
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

//    @Override
//    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
////        Log.d(TAG, "onActionRequestDialogResult: starts which " + which);
////        switch (which) {
////            case 0:
////                break;
////            case 1:
////            case 2:
////            case 4:
////                break;
////            default:
////        }
//
//    }

//    @Override
//    public void onPositiveDialogResult(int dialogId, Bundle args) {
////        AccountListActivityFragment listFragment;
//
//        switch (dialogId) {
//
////            case AppDialog.DIALOG_ID_ASK_REFRESH_SEARCHDB:
////
////                Log.d(TAG, "onPositiveDialogResult: request to rebuild search");
////                loadSearchDB();
////
////
//////                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//////                sharedPreferences.edit().putString(SELECTION_QUERY, "").apply();
//////
//////                listFragment = (AccountListActivityFragment)
//////                        getSupportFragmentManager().findFragmentById(R.id.fragment);
//////                listFragment.setQuery("");
//////
//////                Toast.makeText(this, "Long click on item for more options", Toast.LENGTH_LONG).show();
////
////                //                Intent detailIntent = new Intent(this, SearchActivityV1.class);
//////                detailIntent.putExtra(SearchActivityV1.class.getSimpleName(), true);
//////                startActivity(detailIntent);
////                break;
//            case AppDialog.DIALOG_ID_LEAVE_APP:
//                finish();
//                break;
////            case AppDialog.DIALOG_ID_CANCEL_EDIT:
////            case AppDialog.DIALOG_ID_CANCEL_EDIT_UP:
////                // If we're editing, remove the fragment. Otherwise, close the app
////                FragmentManager fragmentManager = getSupportFragmentManager();
////                Fragment editFragment = fragmentManager.findFragmentById(R.id.task_details_container);
////                if (editFragment != null) {
////                    // we were not editing
////                    getSupportFragmentManager().beginTransaction()
////                            .remove(editFragment)
////                            .commit();
////                    if (isLandscape) {
//////                        Log.d(TAG, "onPositiveDialogResult: get list");
//////                        listFragment = (AccountListActivityFragment)
//////                                getSupportFragmentManager().findFragmentById(R.id.fragment);
//////                        listFragment.resetSelectItem();
////                    } else {
////                        // hide the edit container in single pane mode
////                        // and make sure the left-hand container is visible
////                        View addEditLayout = findViewById(R.id.task_details_container);
////                        View addEditLayoutScroll = findViewById(R.id.task_details_container_scroll);
////                        View mainFragment = findViewById(R.id.fragment);
////                        // We're just removed the editing fragment, so hide the frame
////                        addEditLayout.setVisibility(View.GONE);
////                        addEditLayoutScroll.setVisibility(View.GONE);
//////                        setMenuItemVisible(R.id.menuacct_save, false);
////
////                        // and make sure the MainActivityFragment is visible
////                        mainFragment.setVisibility(View.VISIBLE);
////
////
////                    }
////                } else {
////                    // not editing, so quit regardless of orientation
////                    finish();
////                }
////                break;
////            case AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT:
////                int acctId = args.getInt(AppDialog.DIALOG_ACCOUNT_ID);
////                if (BuildConfig.DEBUG && acctId == 0)
////                    throw new AssertionError("Account Id is zero");
////                Log.d(TAG, "onPositiveDialogResult: ready to delete " + acctId);
////                deleteAccount(acctId);
////                break;
//
////            case AppDialog.DIALOG_ID_CONFIRM_DELETE_PROFILE:
////                int profileId = args.getInt(AppDialog.DIALOG_ACCOUNT_ID);
////                int position = args.getInt(AppDialog.DIALOG_LIST_POSITION);
////                fragDelete(profileId);
////                break;
//
//                //            case AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT:
////                acctEditRequest(-1);
////                break;
//            case AppDialog.DIALOG_ID_EDITS_APPLIED:
//                break;
//        }
//    }


//    private void fragDelete(Profile profile) {
//        Log.d(TAG, "fragDelete function");
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        String stringFragmentName = fragmentManager.findFragmentById(R.id.fragment_container).getClass().getSimpleName();
//
//        Log.d(TAG, stringFragmentName);
//        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        switch (stringFragmentName) {
//            case "ProfileCorpNameFrag":
////                ((ProfileCorpNameFrag) profileFragment).deleteFromList(profileId);
//                profileViewModel.delete(profile);
//                Log.d(TAG, "delete id " + profile.getId());
//                break;
//            case "ProfileOpenDateFrag":
//                ((ProfileOpenDateFrag) profileFragment).deleteFromList(profileId);
//                break;
//            case "ProfilePassportIdFrag":
//                ((ProfilePassportIdFrag) profileFragment).deleteFromList(profileId);
//                break;
//            case "ProfileCustomFrag":
//                ((ProfileCustomFrag) profileFragment).deleteFromList(profileId);
//                break;
//        }
//
//    }

//    @Override
//    public void onNegativeDialogResult(int dialogId, Bundle args) {
//        switch (dialogId) {
//
//            case AppDialog.DIALOG_ID_ASK_REFRESH_SEARCHDB:
//
////                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////                String queryResult = sharedPreferences.getString(SELECTION_QUERY, "");
////
////                Log.d(TAG, "sharedPreferences: return a value " + queryResult);
////
////                AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                        getSupportFragmentManager().findFragmentById(R.id.fragment);
////                listFragment.setQuery(queryResult);
////
////
////                if (queryResult.equals("")) {
////                    Toast.makeText(this, "Long click on item for more options", Toast.LENGTH_LONG).show();
////                } else {
////                    Toast.makeText(this, "List from search on " + queryResult, Toast.LENGTH_LONG).show();
////                }
//
//                //                Intent detailIntent = new Intent(this, SearchActivityV1.class);
////        detailIntent.putExtra(SearchActivityV1.class.getSimpleName(), false);
////                startActivity(detailIntent);
//                break;
//            case AppDialog.DIALOG_ID_LEAVE_APP:
//            case AppDialog.DIALOG_ID_CANCEL_EDIT:
//            case AppDialog.DIALOG_ID_CANCEL_EDIT_UP:
//            case AppDialog.DIALOG_ID_CONFIRM_ADD_ACCOUNT:
//                break;
////            case AppDialog.DIALOG_ID_CONFIRM_DELETE_PROFILE:
////                int position = args.getInt(AppDialog.DIALOG_LIST_POSITION);
////                refreshDeletePos(position);
//////                adapter.notifyDataSetChanged();
////                break;
//        }
//
//    }

    private void refreshNonDeletePos(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String stringFragmentName = fragmentManager.findFragmentById(R.id.fragment_container).getClass().getSimpleName();

        Log.d(TAG, stringFragmentName);
        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (profileFragment instanceof ProfileCorpNameFrag) {
            ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
            frag.refreshListPos(position);
        }
//        switch (stringFragmentName) {
//            case "ProfileCorpNameFrag":
//                ((ProfileCorpNameFrag) profileFragment).refreshListPos(position);
//                break;
//            case "ProfileOpenDateFrag":
//                ((ProfileOpenDateFrag) profileFragment).refreshListPos(position);
//                break;
//            case "ProfilePassportIdFrag":
//                ((ProfilePassportIdFrag) profileFragment).refreshListPos(position);
//                break;
//            case "ProfileCustomFrag":
//                ((ProfileCustomFrag) profileFragment).refreshListPos(position);
//                break;
//        }

    }

//    private void deleteAccount(int acctId) {
//        getContentResolver().delete(AccountsContract.buildIdUri((long) acctId), null, null);
//        if (!isLandscape) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
//            if (fragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .remove(fragment)
//                        .commit();
//            }
//        }
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sharedPreferences.edit().putInt(SELECTION_ONE_ITEM, -1).apply();
//        sharedPreferences.edit().putBoolean(SEARCH_DICT_REFRESHED, false).apply();
//
//
////        AccountListActivityFragment listFragment = (AccountListActivityFragment)
////                getSupportFragmentManager().findFragmentById(R.id.fragment);
////        listFragment.setAcctId(-1);
//
//
//        if (isLandscape) {
//            removeEditing();
//        }
//
//
////        searchListRequest();
//    }

//    @Override
//    public void onDialogCancelled(int dialogId) {
//
//    }


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






    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }




//    @Override
//    public void onProfilePassportIdSelect(int selectedId, Profile profile) {
//        this.startUpProfileUpdate(selectedId, profile);
//    }
//
//    @Override
//    public void onProfileCustomListSelect(int selectedId, Profile profile) {
//        this.startUpProfileUpdate(selectedId, profile);
//    }
//
//
//    @Override
//    public void onProfileOpenDateListSelect(int selectedId, Profile profile) {
//        this.startUpProfileUpdate(selectedId, profile);
//    }

    @Override
    public void onProfileCorpNameListSelect(int selectedId, Profile profile) {
        this.startUpProfileUpdate(selectedId, profile);
    }



    @Override
    public void onDeleteConfirmCustom(Profile profile, int position) {
        alertConfirmDelete(profile, position );
    }

    @Override
    public void onDeleteConfirmCorpName(Profile profile, int position) {
        alertConfirmDelete(profile, position);
    }

//    @Override
//    public void onDeleteConfirmOpenDate(Profile profile, int position) {
//        alertConfirmDelete(profile, position);
//    }
//
//    @Override
//    public void onDeleteConfirmPassportId(Profile profile, int position) {
//        alertConfirmDelete(profile, position);
//    }

    @Override
    public void onProfileCorpNameAdd() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment addeditFragment = fragmentManager.findFragmentById(R.id.fragment_container2);
//        if (addeditFragment instanceof AddEditProfileFrag) {
//            AddEditProfileFrag frag = (AddEditProfileFrag) addeditFragment;
//            if (frag.getEditModeAdd()) {
//                return;
//            }
//        }
//        if (editModeAdd) {
//            editModeAdd = false;
//            frame2.setVisibility(View.GONE);
//            return;
//        }
        editModeAdd = true;
        this.selectedId = -1;
        Profile profile = new Profile();
        AddEditProfileFrag fragment2 = new AddEditProfileFrag();
        Bundle args = new Bundle();
//        args.putInt(AddEditProfileFrag.EXTRA_ID, profile.getId());
        args.putInt(AddEditProfileFrag.EXTRA_ID, -1);
//        args.putInt(AddEditProfileFrag.EXTRA_PASSPORT_ID, profile.getPassportId());
        args.putInt(AddEditProfileFrag.EXTRA_PASSPORT_ID, -1);
        args.putString(AddEditProfileFrag.EXTRA_CORP_NAME, profile.getCorpName());
        args.putString(AddEditProfileFrag.EXTRA_USER_NAME, profile.getUserName());
        args.putString(AddEditProfileFrag.EXTRA_USER_EMAIL, profile.getUserEmail());
        args.putInt(AddEditProfileFrag.EXTRA_SEQUENCE, profile.getSequence());
        args.putString(AddEditProfileFrag.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
        args.putString(AddEditProfileFrag.EXTRA_NOTE, profile.getNote());
        args.putLong(AddEditProfileFrag.EXTRA_ACTVY_LONG, profile.getActvyLong());
        args.putLong(AddEditProfileFrag.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
        fragment2.setArguments(args);
//        AddEditProfileFrag addeditFrag = (AddEditProfileFrag) fragmentManager.findFragmentByTag("AddEditProfileFrag");
//        if (addeditFrag == null) {
//            fragmentTransaction.add(R.id.fragment_container2, fragment2, "AddEditProfileFrag");
//        } else {
            fragmentTransaction.replace(R.id.fragment_container2, fragment2, "AddEditProfileFrag");
//        }
        fragmentTransaction.commit();
        frame2 = findViewById(R.id.fragment_container2);
        frame2.setVisibility(View.VISIBLE);
    }

    private void alertConfirmDelete(Profile profile, int position) {
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(getString(R.string.alert_delete_acct) + "\n" +
//                profile.getCorpName() +
//                "\n" + getString(R.string.alert_acct_id) + profile.getPassportId());
        String msg = getString(R.string.alert_delete_acct) + "\n" +
                profile.getCorpName() +
                "\n" + getString(R.string.alert_acct_id) + profile.getPassportId();

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
//                profileViewModel.delete(profile);
                ProfileCorpNameFrag frag = getProfileFrag();
                frag.deleteProfileViewModelItem(profile);
                dialog.dismiss();
            }
        });

        Button dialogButtonNo = (Button) dialog.findViewById(R.id.no);
        // if button is clicked, close the custom dialog
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNonDeletePos(position);
                dialog.dismiss();
            }
        });

        dialog.show();


//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        profileViewModel.delete(profile);
////                        fragDelete(profile);
////                        Log.d(TAG, "delete request " + profile.getId());
//                    }
//                })
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                refreshDeletePos(position);
//                            }
//                        });
//
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();

    }

    @Override
    public void onEmptyWarning() {
        FrameLayout frame = findViewById(R.id.fragment_container);
        if (itemAdded) {
            frame.setVisibility(View.VISIBLE);
            return;
        }
        frame.setVisibility(View.GONE);

//        String welcomeMsg = getString(R.string.warningmsg);
//        String msg = String.format(String.format("{0}\n{1}\n{2}", ) ) + getString(R.string.getting_started) + "\n" +
//                getString(R.string.add_first_account);
////        + "\n" +
////                getString(R.string.if_so_direction);

        String msg = String.format("%s\n\n\n%s\n\n%s",
                getString(R.string.warningmsg),
                getString(R.string.getting_started),
                getString(R.string.add_first_account));

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_msg_ok);
        dialog.setTitle("Account Modify Info");


        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_info_24dp);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                itemAdded = true;

                onProfileCorpNameAdd();

            }
        });

        dialog.show();



//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
//
//        alertDialogBuilder.setMessage(getString(R.string.getting_started) + "\n" +
//                getString(R.string.add_first_account) + "\n" +
//    getString(R.string.if_so_direction));
//
//
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                        itemAdded = true;
//
//                        onProfileCorpNameAdd();
//
//
////                        Intent intent = new Intent(MainActivity.this, AddEditProfileActivity.class);
////                        startActivityForResult(intent, ADD_PROFILE_REQUEST);
//
//                    }
//                })
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();

    }



//    public void msgDialog(String msg) {
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_msg_ok);
//        dialog.setTitle("Account Modify Info");
//
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText(msg);
//        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_info_24dp);
//
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//        // if button is clicked, close the custom dialog
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    @Override
    public void setMaxSeq(int maxSeq) {
        this.currentMaxSeq = maxSeq;
    }

    private void startUpProfileUpdate(int selectedId, Profile profile) {

//        if (has2ndPanel) {
        FragmentManager fragmentManager = getSupportFragmentManager();
//        AddEditProfileFrag addeditFrag = (AddEditProfileFrag) fragmentManager.findFragmentByTag("AddEditProfileFrag");
//        if (addeditFrag != null) {
//            if (addeditFrag.haveChanges()) {
//                askApplyCancel(selectedId, profile, "Changes not applied"+
//                        "\nDo you want to ignore these changes?");
//            } else {
//                selectAccountDetail(selectedId, profile);
//            }
//        } else {
//            selectAccountDetail(selectedId, profile);
//        }
        selectAccountDetail(selectedId, profile);

        if (this.isSearchShown) {


            Fragment searchFragment = fragmentManager.findFragmentById(R.id.fragment_search_container);
            if (searchFragment instanceof SearchFrag) {
                SearchFrag frag = (SearchFrag) searchFragment;
                frag.resetSearch();
            }
        }
    }

    private ProfileCorpNameFrag getProfileFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
        return frag;
    }

    private void selectAccountDetail(int selectedId, Profile profile) {
        this.selectedId = selectedId;
        Log.d(TAG, "selectAccountDetail: selectedId " + this.selectedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//            ProfileCorpNameFrag frag = (ProfileCorpNameFrag)profileFragment;
//            frag.refreshListAll();
            profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
            frag.setSelectedId(this.selectedId);
            frag.refreshListAll();
//            if (profileFragment instanceof ProfileCorpNameFrag) {
//                ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
//                frag.setSelectedId(this.selectedId);
//                frag.refreshListAll();
//            } else if (profileFragment instanceof ProfileCustomFrag) {
//                ProfileCustomFrag frag = (ProfileCustomFrag) profileFragment;
//                frag.setSelectedId(this.selectedId);
//                frag.refreshListAll();
//            } else if (profileFragment instanceof ProfileOpenDateFrag) {
//                ProfileOpenDateFrag frag = (ProfileOpenDateFrag) profileFragment;
//                frag.setSelectedId(this.selectedId);
//                frag.refreshListAll();
//            } else if (profileFragment instanceof ProfilePassportIdFrag) {
//                ProfilePassportIdFrag frag = (ProfilePassportIdFrag) profileFragment;
//                frag.setSelectedId(this.selectedId);
//                frag.refreshListAll();
//            }

//            if (frame2 != null) {
//                frame2.setVisibility(View.VISIBLE);
//                frame2.removeAllViews();
//                return;
//            }

//        ___________________________________________________________
//        in process to chg to activity
//        ___________________________________________________________

//            frame2 = findViewById(R.id.fragment_container2);
//            if (selectedId == -1) {
//                frame2.setVisibility(View.GONE);
//            } else {
//                frame2.setVisibility(View.VISIBLE);
//            }

//            Fragment fragmentA = fragmentManager.findFragmentByTag("AddEditProfileFrag");

            Log.d(TAG, "startUpProfileUpdate: replace addEdit");

//            AddEditProfileFrag fragment2 = new AddEditProfileFrag();
            Bundle args = new Bundle();
            args.putInt(AddEditProfileFrag.EXTRA_ID, profile.getId());
            args.putInt(AddEditProfileFrag.EXTRA_PASSPORT_ID, profile.getPassportId());
            args.putString(AddEditProfileFrag.EXTRA_CORP_NAME, profile.getCorpName());
            args.putString(AddEditProfileFrag.EXTRA_USER_NAME, profile.getUserName());
            args.putString(AddEditProfileFrag.EXTRA_USER_EMAIL, profile.getUserEmail());
            args.putInt(AddEditProfileFrag.EXTRA_SEQUENCE, profile.getSequence());
            args.putString(AddEditProfileFrag.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
            args.putString(AddEditProfileFrag.EXTRA_NOTE, profile.getNote());
            args.putLong(AddEditProfileFrag.EXTRA_ACTVY_LONG, profile.getActvyLong());
            args.putLong(AddEditProfileFrag.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
//            fragment2.setArguments(args);
//            fragmentTransaction.replace(R.id.fragment_container2, fragment2, "AddEditProfileFrag");
//            fragmentTransaction.commit();

            Log.d(TAG, "startUpProfileUpdate: startUp " + profile.getPassportId() +
                    " " + profile.getCorpName());

//        startForResultLauncher.launch(detailIntent);
//        startActivity(detailIntent);

            return;
//        }

//        Intent intent = new Intent(this, AddEditProfileActivity.class);
//        intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
//        intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
//        intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
//        intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
//        intent.putExtra(AddEditProfileActivity.EXTRA_USER_EMAIL, profile.getUserEmail());
//        intent.putExtra(AddEditProfileActivity.EXTRA_SEQUENCE, profile.getSequence());
//        intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
//        intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
//        intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
//        Log.d(TAG, "startup " + profile.getOpenLong());
//        intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
//
//        Log.d(TAG, "edit requested");
//        startActivityForResult(intent, EDIT_PROFILE_REQUEST);

    }

    private void askApplyCancel(final int selectedId, final Profile profile, final String msg) {

//        final boolean[] actionCancel = {true};
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
//                actionCancel[0] = false;
                dialog.dismiss();
                selectAccountDetail(selectedId, profile);
            }
        });

        Button dialogButtonNo = (Button) dialog.findViewById(R.id.no);
        // if button is clicked, close the custom dialog
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                actionCancel[0] = false;
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void showSearchSelected(int selectedId, Profile profile) {
        frame2.setVisibility(View.VISIBLE);
        this.selectedId = selectedId;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        ProfileCorpNameFrag frag = (ProfileCorpNameFrag) fragment;
        frag.setSelectedId(this.selectedId);
        frag.refreshListAll();
        Log.d(TAG, "showSearchSelected: selectedId " + selectedId);
//        startUpProfileUpdate(selectedId, profile);
        AddEditProfileFrag fragment2 = new AddEditProfileFrag();
        Bundle args = new Bundle();
        args.putInt(AddEditProfileFrag.EXTRA_ID, profile.getId());
        args.putInt(AddEditProfileFrag.EXTRA_PASSPORT_ID, profile.getPassportId());
        args.putString(AddEditProfileFrag.EXTRA_CORP_NAME, profile.getCorpName());
        args.putString(AddEditProfileFrag.EXTRA_USER_NAME, profile.getUserName());
        args.putString(AddEditProfileFrag.EXTRA_USER_EMAIL, profile.getUserEmail());
        args.putInt(AddEditProfileFrag.EXTRA_SEQUENCE, profile.getSequence());
        args.putString(AddEditProfileFrag.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
        args.putString(AddEditProfileFrag.EXTRA_NOTE, profile.getNote());
        args.putLong(AddEditProfileFrag.EXTRA_ACTVY_LONG, profile.getActvyLong());
        args.putLong(AddEditProfileFrag.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
        fragment2.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2, fragment2, "AddEditProfileFrag");
        fragmentTransaction.commit();
//        FrameLayout frame = findViewById(R.id.fragment_container);
//        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        profileFragment.setSe.setSelected(selectedId);
//        frame.setVisibility(View.GONE);
    }



    @Override
    public void onProfileModifyItem(Profile profile) {
        ProfileCorpNameFrag frag = getProfileFrag();
        frag.updateProfileViewModelItem(profile);

//        profileViewModel.update(profile);
    }


    @Override
    public void onProfileAddItem(Profile profile) {
        profile.setId(0);
        profile.setSequence(currentMaxSeq + 1);
        ProfileCorpNameFrag frag = getProfileFrag();
        frag.insertProfileViewModelItem(profile, this);
//        profileViewModel.insertProfile(profile, this);
    }

    @Override
    public void
    processInsertComplete(Profile profile) {
        this.selectedId = profile.getPassportId();
        Log.d(TAG, "processInsertComplete: " + profile.getPassportId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (profileFragment instanceof ProfileCorpNameFrag) {
            ProfileCorpNameFrag frag = (ProfileCorpNameFrag) profileFragment;
            frag.selectById(this.selectedId);
        } else if (profileFragment instanceof ProfileCustomFrag) {
            ProfileCustomFrag frag = (ProfileCustomFrag) profileFragment;
        } else if (profileFragment instanceof ProfileOpenDateFrag) {
            ProfileOpenDateFrag frag = (ProfileOpenDateFrag) profileFragment;
        } else if (profileFragment instanceof ProfilePassportIdFrag) {
            ProfilePassportIdFrag frag = (ProfilePassportIdFrag) profileFragment;
        }
        showSearchSelected(this.selectedId, profile);

    }


    private void showWarning() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_msg_ok);
        dialog.setTitle("Account Modify Info");

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(getString(R.string.warningmsg));
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_info_24dp);

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

    //    public boolean isFragmentPresent(String tag) {
//        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
//        if (frag instanceof HomeFragment) {
//            return true;
//        } else
//            return false;
//    }

//    private void refreshList() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        String stringFragmentName = fragmentManager.findFragmentById(R.id.fragment_container).getClass().getSimpleName();
//
//        Log.d(TAG, stringFragmentName);
//        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        switch (stringFragmentName) {
//            case "ProfileCorpNameFrag":
//                ((ProfileCorpNameFrag) profileFragment).refreshList();
//                break;
//            case "ProfileOpenDateFrag":
//                ((ProfileOpenDateFrag) profileFragment).refreshList();
//                break;
//            case "ProfilePassportIdFrag":
//                ((ProfilePassportIdFrag) profileFragment).refreshList();
//                break;
//            case "ProfileCustomFrag":
//                ((ProfileCustomFrag) profileFragment).refreshList();
//                break;
//        }
//        if (stringFragmentName.equals("ProfileCustomFrag")) {
//            Log.d(TAG, "about to re-sequence");
//
//
//        } else {
//            Log.d(TAG, "no ProfileCustomFrag available");
//        }
//
//    }

//    @Override
//    public Filter getFilter() {
//        return profileFilter;
//    }
//
//    private Filter profileFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Profile> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//               filteredList.addAll(profileListFull);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (Profile item : profileListFull) {
//                    if (item.getCorpName().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            profileList.clear();
//            profileList.addAll((List) results.values);
//
////            notifyDataSetChanged();
//        }
//    };
}
