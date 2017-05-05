package com.kinsey.passwords;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.kinsey.passwords.items.MyDataObject;
import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;
import com.kinsey.passwords.provider.AccountSearchLoaderCallbacks;
import com.kinsey.passwords.provider.RetainedFragment;
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
import static com.kinsey.passwords.MainActivity.SEARCH_LOADER_ID;
import static com.kinsey.passwords.MainActivity.format_ymdtime;

public class AccountListActivity extends AppCompatActivity
        implements AccountRecyclerViewAdapter.OnAccountClickListener,
        AccountActivityFragment.OnActionListener,
        AccountPlaceholderFrag1.OnAccountListener,
        AccountPlaceholderFrag2.OnAccountListener,
        AccountPlaceholderFrag3.OnAccountListener,
        ViewPager.OnPageChangeListener,
        AppDialog.DialogEvents {
//        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AccountListActivity";
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static final String TAG_ACTIVITY_LIST_FRAG = "AccountListActivityFragment";
    private static final String TAG_ACCOUNT_PLACEHOLDER_FRAG1 = "AccountPlaceholderFrag1";
    private static final String TAG_ACCOUNT_PLACEHOLDER_FRAG2 = "AccountPlaceholderFrag2";
    private static final String TAG_ACCOUNT_PLACEHOLDER_FRAG3 = "AccountPlaceholderFrag3";

    public static Account account = new Account();

    private RetainedFragment mRetainedFragment;
    FragmentManager fm;
    AccountListActivityFragment fragList;

    private boolean mTwoPane = false;
    int mSortorder = AccountsContract.ACCOUNT_LIST_BY_CORP_NAME;
    RecyclerView mRecyclerView;
//    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference

    private int accountMode = AccountsContract.ACCOUNT_ACTION_ADD;

//    AccountActivityFragment accountActivityFragment = new AccountActivityFragment();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AccountPlaceholderFrag1 frag1;
    private AccountPlaceholderFrag2 frag2;
    private AccountPlaceholderFrag3 frag3;
    private int fragListPos = -1;
    private int frag1Pos = 0;
    private int frag2Pos = 1;
    private int frag3Pos = 2;
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();

    boolean isUserPaging = true;
    boolean isRotated = false;

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

//        removeRetainedFrag();

        // find the retained fragment on activity restarts
        fm = getSupportFragmentManager();
        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);


        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new RetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setData(loadMyData());
            isRotated = false;
            Log.d(TAG, "onCreate: created new retained");
        } else {
            Log.d(TAG, "onCreate: retained present");
            isRotated = true;
//            mRetainedFragment.getData().getmSectionsPagerAdapter().setMyDataObject(mRetainedFragment.getData());

//            this.account = mRetainedFragment.getData().getAccount();
//            Log.d(TAG, "onCreate: retained account " + this.account);
        }

//        Log.d(TAG, "onCreate: check layout");

        if (findViewById(R.id.item_detail_container) != null) {
            mViewPager = (ViewPager) findViewById(R.id.item_detail_container);
//            if (isRotated) {
//                emptyPager();
//            }

//            AccountPlaceholderFrag1 frag1_2 = (AccountPlaceholderFrag1) fm.findFragmentByTag(TAG_ACCOUNT_PLACEHOLDER_FRAG1);
//
//                if (frag1_2 == null) {
//                    Log.d(TAG, "onCreate: frag1-2 is null");
//                } else {
//                    Log.d(TAG, "onCreate: has frag1-2");
//                }

//            fm = getSupportFragmentManager();
//            List<Fragment> frags = fm.getFragments();
//            if (frags == null) {
//                Log.d(TAG, "onCreate: frags " + frags);
//            } else {
//                int fragSize = frags.size();
//                Log.d(TAG, "emptyPager: size of frags " + fragSize);
//                Fragment fragListItem = null;
//                for (int i = 0; i < fragSize; i++) {
//                    Fragment fragItem = frags.get(i);
//                    Log.d(TAG, "onCreate: fragItem " + fragItem.getClass().getName());
//                    if (fragItem.getClass().getName().equals("com.kinsey.passwords.AccountListActivityFragment")) {
////                        fragList = (AccountListActivityFragment)fragItem;
//                        fragListItem = fragItem;
//                    } else if (fragItem.getClass().getName().equals("com.kinsey.passwords.AccountPlaceholderFrag1")) {
//                        frag1 = (AccountPlaceholderFrag1)fragItem;
//                    } else if (fragItem.getClass().getName().equals("com.kinsey.passwords.AccountPlaceholderFrag2")) {
//                        frag2 = (AccountPlaceholderFrag2)fragItem;
//                    } else if (fragItem.getClass().getName().equals("com.kinsey.passwords.AccountPlaceholderFrag3")) {
//                        frag3 = (AccountPlaceholderFrag3)fragItem;
//                    }
//                }
//                if (fragListItem != null) {
//                    fm.beginTransaction().remove(fragListItem).commit();
//                }
//            }



//            if (fragList == null) {
                fragList = AccountListActivityFragment.newInstance(mRetainedFragment.getData().getSelectedPos());
//            }
            if (frag1 == null) {
                frag1 = AccountPlaceholderFrag1.newInstance();
            }
            if (frag2 == null) {
                frag2 = AccountPlaceholderFrag2.newInstance();
            }
            if (frag3 == null) {
                frag3 = AccountPlaceholderFrag3.newInstance();
            }



//            fm.beginTransaction().replace(R.id.item_detail_container, frag1, TAG_ACCOUNT_PLACEHOLDER_FRAG1).commit();


//            if (mSectionsPagerAdapter == null) {
//                Log.d(TAG, "onCreate: mSectionsPagerAdapter is null ");
//            } else {
//                Log.d(TAG, "onCreate: has mSectionsPagerAdapter class ");
//            }


            if (mViewPager.getTag().equals("land")) {
                Log.d(TAG, "onCreate: landscape");
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
////            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
//                mRetainedFragment.getData().setmSectionsPagerAdapter(
//                        new SectionsPagerAdapter(getSupportFragmentManager(),
//                        mTwoPane, mRetainedFragment.getData()));
////            mSectionsPagerAdapter.setAccount(new Account());

                // Create the adapter that will return a fragment for each of the three

                // Set up the ViewPager with the sections adapter.
                fragments.add(frag1);
                fragments.add(frag2);
                fragments.add(frag3);
                Log.d(TAG, "onCreate: new size " + fragments.size());
                fragListPos = -1;
                frag1Pos = 0;
                frag2Pos = 1;
                frag3Pos = 2;

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                        fragListPos, frag1Pos, frag2Pos, frag3Pos, fragments);
//                mRetainedFragment.setData(setPlacements(mRetainedFragment.getData()));

//                mRetainedFragment.getData().getmSectionsPagerAdapter().setMyDataObject(mRetainedFragment.getData());


                mViewPager.setAdapter(mSectionsPagerAdapter);
                pagerEvents();


//                mRecyclerView = (RecyclerView) findViewById(R.id.account_items_list);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                setupAdapter();

//                getSupportFragmentManager().beginTransaction()
//                        .add(fragList2, TAG_ACTIVITY_LIST_FRAG)
////                                mRetainedFragment.getData().getFragList())
//                        .commit();

//                AccountListActivityFragment fragList2 = (AccountListActivityFragment) fm.findFragmentByTag(TAG_ACTIVITY_LIST_FRAG);

//                if (fragList == null) {
//                    Log.d(TAG, "onCreate: fragList is null");
//                }


//                AccountListActivityFragment fragList3 = (AccountListActivityFragment) fm.findFragmentByTag(TAG_ACTIVITY_LIST_FRAG);
//
//                if (fragList3 == null) {
//                    Log.d(TAG, "onCreate: fragList3 is null");
//                } else {
//                    Log.d(TAG, "onCreate: has fragList3");
//                }

//                findViewById(R.id.fragList).
//                FragmentManager fmc = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragList, fragList, TAG_ACTIVITY_LIST_FRAG).commit();


//                fragList2 = (AccountListActivityFragment)fmc.findFragmentById(R.id.fragList);
//                if (fragList2 != null) {
//                    transaction.hide(fragList2);
//                }
//                transaction.commit();

//                if (isRotated) {
//                    isUserPaging = false;
//                    if (mRetainedFragment.getData().getFrag3() == null) {
//                        Log.d(TAG, "onCreate: null frag3");
//                    }
//                    if (mRetainedFragment.getData().getFrag2() == null) {
//                        Log.d(TAG, "onCreate: null frag2");
//                    }
//                    if (mRetainedFragment.getData().getFrag1() == null) {
//                        Log.d(TAG, "onCreate: null frag1");
//                    }
//                    if (mRetainedFragment.getData().getFragList() == null) {
//                        Log.d(TAG, "onCreate: null fragList");
//                    }
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag3Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag3Pos());
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag2Pos());
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag1Pos());
////                    mSectionsPagerAdapter.notifyDataSetChanged();
//                    isUserPaging = true;
//                }


//
//            mViewPager.setCurrentItem(2);
////            mSectionsPagerAdapter.getItem(2);
////            mViewPager.setAdapter(mSectionsPagerAdapter);
//            mViewPager.setCurrentItem(1);
////            mSectionsPagerAdapter.getItem(1);
////            mViewPager.setAdapter(mSectionsPagerAdapter);
//            mViewPager.setCurrentItem(0);
////            mSectionsPagerAdapter.getItem(0);

                isUserPaging = false;
                mViewPager.setCurrentItem(frag3Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(frag2Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(frag1Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                isUserPaging = true;

            } else {
                Log.d(TAG, "onCreate: portrait");
                mTwoPane = false;

                if (fragList == null) {
                    Log.d(TAG, "onCreate: fragList is null");
                }

                fragments.add(fragList);
                fragments.add(frag1);
                fragments.add(frag2);
                fragments.add(frag3);
                Log.d(TAG, "onCreate: new size " + fragments.size());

                fragListPos = 0;
                frag1Pos = 1;
                frag2Pos = 2;
                frag3Pos = 3;
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                        fragListPos, frag1Pos, frag2Pos, frag3Pos, fragments);

//                mRetainedFragment.setData(setPlacements(mRetainedFragment.getData()));

//                mRetainedFragment.getData().getmSectionsPagerAdapter().setMyDataObject(mRetainedFragment.getData());

                //                mRetainedFragment.getData().setmSectionsPagerAdapter(
//                        new SectionsPagerAdapter(getSupportFragmentManager(),
//                        mTwoPane, mRetainedFragment.getData()));
                mViewPager.setAdapter(mSectionsPagerAdapter);
                pagerEvents();

                isUserPaging = false;
                mViewPager.setCurrentItem(frag3Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(frag2Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(frag1Pos);
                mSectionsPagerAdapter.notifyDataSetChanged();
                isUserPaging = true;

                if (isRotated) {
                    mViewPager.setCurrentItem(fragListPos);
//                    isUserPaging = false;
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag3Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(mViewPager, 2);
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag3Pos());
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag2Pos());
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
//                    mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag1Pos());
//                    mViewPager.setCurrentItem(mRetainedFragment.getData().getFragListPos());
////                    mSectionsPagerAdapter.notifyDataSetChanged();
//                    isUserPaging = true;
                } else {
                    isUserPaging = false;
                    mViewPager.setCurrentItem(fragListPos);
                    isUserPaging = true;
                    Toast.makeText(AccountListActivity.this,
                            "Account List, swipe to an item", Toast.LENGTH_LONG).show();
                }


            }
        } else {
            Log.d(TAG, "onCreate: xlarge");

        }

//        Log.d(TAG, "onCreate: port " + mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, mRetainedFragment.getData().getFrag1Pos()));
//        Log.d(TAG, "onCreate: port " + mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, mRetainedFragment.getData().getFrag2Pos()));
//        Log.d(TAG, "onCreate: port " + mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, mRetainedFragment.getData().getFrag3Pos()));
//        if (mRetainedFragment.getData().getFragListPos() != -1) {
//            Log.d(TAG, "onCreate: port " + mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                    mViewPager, mRetainedFragment.getData().getFragListPos()));
//        }

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

        if (mTwoPane) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


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

    private MyDataObject loadMyData() {
        MyDataObject obj = new MyDataObject();

        obj.setAccount(account);
        obj.setSelectedPos(-1);
//        mRetainedFragment = new RetainedFragment();
//        fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//        (RetainedFragment) retFrag = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

//        fragList = AccountListActivityFragment.newInstance();
//
//        if (fragList == null) {
//            Log.d(TAG, "loadMyData: fragList is null");
//        } else {
//            Log.d(TAG, "loadMyData: has fragList");
//        }
//
//        fm.beginTransaction().replace(R.id.fragList, fragList, TAG_ACTIVITY_LIST_FRAG).commit();
////                                mRetainedFragment.getData().getFragList())
//
//
//        AccountListActivityFragment fragList3 = (AccountListActivityFragment) fm.findFragmentByTag(TAG_ACTIVITY_LIST_FRAG);
//
//        if (fragList3 == null) {
//            Log.d(TAG, "loadMyData: fragList3 is null");
//        } else {
//            Log.d(TAG, "loadMyData: has fragList3");
//        }

//        FragmentManager fm = getSupportFragmentManager();
//        List<Fragment> frags = fm.getFragments();
//        int fragSize = frags.size();
//        Log.d(TAG, "emptyPager: size of frags " + fragSize);
//        for (int i = 0; i < fragSize; i++) {
//            Fragment fragItem = frags.get(i);
//            Log.d(TAG, "emptyPager: fragItem " + fragItem.getClass().getName());
//        }

//        obj.setFrag1(AccountPlaceholderFrag1.newInstance());
//        obj.setFrag2(AccountPlaceholderFrag2.newInstance());
//        obj.setFrag3(AccountPlaceholderFrag3.newInstance());
//        obj.setmSectionsPagerAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        return obj;
    }

//    private MyDataObject setPlacements(MyDataObject obj) {
//        if (mTwoPane) {
//            obj.setFragListPos(-1);
//            obj.setFrag1Pos(0);
//            obj.setFrag2Pos(1);
//            obj.setFrag3Pos(2);
//        } else {
//            obj.setFragListPos(0);
//            obj.setFrag1Pos(1);
//            obj.setFrag2Pos(2);
//            obj.setFrag3Pos(3);
//        }
//        return obj;
//    }

//    private void emptyPager() {
//        if ( mRetainedFragment.getData().getFrag1() == null) {
//            Log.d(TAG, "emptyPager: frag1 null");
//        }
//        mRetainedFragment.getData().getmSectionsPagerAdapter().destroyItem(mViewPager,
//                mRetainedFragment.getData().getFrag1Pos(),
//                mRetainedFragment.getData().getFrag1());
//        mRetainedFragment.getData().getmSectionsPagerAdapter().destroyItem(mViewPager,
//                mRetainedFragment.getData().getFrag2Pos(),
//                mRetainedFragment.getData().getFrag2());
//        mRetainedFragment.getData().getmSectionsPagerAdapter().destroyItem(mViewPager,
//                mRetainedFragment.getData().getFrag3Pos(),
//                mRetainedFragment.getData().getFrag3());
//        if (mRetainedFragment.getData().getFragListPos() != -1) {
//            mRetainedFragment.getData().getmSectionsPagerAdapter().destroyItem(mViewPager,
//                    mRetainedFragment.getData().getFragListPos(),
//                    mRetainedFragment.getData().getFragList());
//        }
//        Log.d(TAG, "emptyPager: is empty " + mViewPager.getChildCount());
//        Log.d(TAG, "emptyPager: is empty " + mRetainedFragment.getData().getFragListPos());
//        Fragment frag = (Fragment) mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, 0);
////        Log.d(TAG, "emptyPager: is empty "
////                + mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
////                mViewPager, 0)
////        );
//        Log.d(TAG, "emptyPager: " + frag.getClass().getName());
//        mRetainedFragment.getData().getmSectionsPagerAdapter().destroyItem(mViewPager,
//                0, frag);
//        frag = (Fragment) mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, 0);
//        Log.d(TAG, "emptyPager: " + frag.getClass().getName());
//
//        mViewPager.removeAllViews();
//        frag = (Fragment) mRetainedFragment.getData().getmSectionsPagerAdapter().instantiateItem(
//                mViewPager, 0);
//        Log.d(TAG, "emptyPager: " + frag.getClass().getName());
//
//
//        FragmentManager fm = getSupportFragmentManager();
//
//        List<Fragment> frags = fm.getFragments();
//        int fragSize = frags.size();
//        Log.d(TAG, "emptyPager: size of frags " + fragSize);
//        for (int i = 0; i < fragSize; i++) {
//            Fragment fragItem = frags.get(i);
//            Log.d(TAG, "emptyPager: fragItem " + fragItem.getClass().getName());
//            fm.beginTransaction().remove(fragItem).commit();
//        }
////        mRetainedFragment.getData().getmSectionsPagerAdapter().
//
//        frags = fm.getFragments();
//        fragSize = frags.size();
//        Log.d(TAG, "emptyPager: size of frags " + fragSize);
//
//        RetainedFragment retainFrag = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
//
//        if (retainFrag == null) {
//            Log.d(TAG, "emptyPager: retainFrag is null");
//        } else {
//            Log.d(TAG, "emptyPager: retainFrag " + retainFrag.getClass().getName());
//        }
//
////        FragmentManager fm = getSupportFragmentManager();
//        AccountListActivityFragment fragList = (AccountListActivityFragment) fm.findFragmentByTag(TAG_ACTIVITY_LIST_FRAG);
//
//        if (fragList == null) {
//            Log.d(TAG, "emptyPager: fragList is null");
//        } else {
//            Log.d(TAG, "emptyPager: fragList " + fragList.getClass().getName());
//        }
//
//        AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1) fm.findFragmentByTag(TAG_ACCOUNT_PLACEHOLDER_FRAG);
//        if (frag1 == null) {
//            Log.d(TAG, "emptyPager: frag1 is null");
//        } else {
//            Log.d(TAG, "emptyPager: frag1 " + frag1.getClass().getName());
//        }
//
//    }

    private void pagerEvents() {
// Attach the page change listener inside the activity
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                    mSectionsPagerAdapter.refreshPage(position);
                String msg = "";
                if (position == frag1Pos) {
                    msg = "Account Id info";
                } else if (position == frag2Pos) {
                    msg = "Account Open Date calendar";
                } else if (position == frag3Pos) {
                    msg = "Account Notes & ref";
                } else if (position == fragListPos) {
                    msg = "Accounts List";
                }
//                    switch (position) {
//                        case 0:
//                            if (mSectionsPagerAdapter.getFrag1RowId() == -1
//                                    || mSectionsPagerAdapter.getFrag1RowId() == -1) {
//
//                            } else {
//                                if (mSectionsPagerAdapter.getFrag1RowId() != mSectionsPagerAdapter.getAccount().getId()) {
////                                    mSectionsPagerAdapter.loadPage(position);
//                                }
//                            }
//                            msg = "Account Id info";
//                            break;
//                        case 1:
//                            if (mSectionsPagerAdapter.getFrag2RowId() == -1
//                                    || mSectionsPagerAdapter.getFrag2RowId() == -1) {
//
//                            } else {
//                                if (mSectionsPagerAdapter.getFrag2RowId() != mSectionsPagerAdapter.getAccount().getId()) {
////                                    mSectionsPagerAdapter.loadPage(position);
//                                }
//                            }
//                            msg = "Account Open Date calendar";
//                            break;
//                        case 2:
//                            if (mSectionsPagerAdapter.getFrag3RowId() == -1
//                                    || mSectionsPagerAdapter.getFrag3RowId() == -1) {
//
//                            } else {
//                                if (mSectionsPagerAdapter.getFrag3RowId() != mSectionsPagerAdapter.getAccount().getId()) {
////                                    mSectionsPagerAdapter.loadPage(position);
//                                }
//                            }
//                            msg = "Account Notes & ref";
//                            break;
//                    }
                if (isUserPaging) {
                    Toast.makeText(AccountListActivity.this,
                            msg, Toast.LENGTH_SHORT).show();
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
//                    Log.d(TAG, "onPageScrolled: scroll " + position + ":" + positionOffset);
//                    mSectionsPagerAdapter.validateFrags(position);
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
//                Log.d(TAG, "onPageScrollStateChanged: state " + state);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRetainedFragment != null) {

            try {

                mRetainedFragment.setData(collectMyData());
                if (mSectionsPagerAdapter != null) {
                    mSectionsPagerAdapter.clearAll();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onSaveInstanceState Error " + e.getMessage());
            }

            super.onSaveInstanceState(outState);
        }
    }

    private MyDataObject collectMyData() {
        try {

            MyDataObject myDataObject = mRetainedFragment.getData();
            myDataObject.setAccount(account);
            if (fragList == null) {
                myDataObject.setSelectedPos(-1);
            } else {
                myDataObject.setSelectedPos(fragList.getSelected_position());
            }
            return myDataObject;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "collectMyData Error " + e.getMessage());
            return null;
        }
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

    private void removeRetainedFrag() {
//        FragmentManager fm = getSupportFragmentManager();
        // we will not need this fragment anymore, this may also be a good place to signal
        // to the retained fragment object to perform its own cleanup.
//        fm.beginTransaction().remove(mRetainedFragment).commit();
        FragmentManager fm = getSupportFragmentManager();
        RetainedFragment retainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (retainedFragment != null) {
            fm.beginTransaction().remove(retainedFragment).commit();
        }
    }

    //    @Override
//    public void onPause() {
//        // perform other onPause related actions
//        // ...
//        // this means that this activity will not be recreated now, user is leaving it
//        // or the activity is otherwise finishing
//        if(isFinishing()) {
//            FragmentManager fm = getSupportFragmentManager();
//            // we will not need this fragment anymore, this may also be a good place to signal
//            // to the retained fragment object to perform its own cleanup.
//            fm.beginTransaction().remove(mRetainedFragment).commit();
//        }
//        super.onPause();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            switch (menuItem.getItemId()) {
//                case R.id.menuacct_add:
//                    menuItem.setVisible(true);
//                    Log.d(TAG, "onMenuOpened: set off add");
//                    break;
//                default:
//                    if (mTwoPane) {
//                        menuItem.setVisible(true);
//                    } else {
//                        menuItem.setVisible(false);
//                    }
//                    break;
//            }
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menuacct_add:
                addAccountRequest();
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

        Log.d(TAG, "saveAccount: mode " + accountMode);

        int currentItem = mViewPager.getCurrentItem();
//        if (!mSectionsPagerAdapter.validateFrags(currentItem)) {
//            Toast.makeText(AccountListActivity.this,
//                                                "Error on account entry",
//                                                Toast.LENGTH_LONG).show();
//            return;
//        };
        if (frag1.validatePageErrors()) {
            isUserPaging = false;
            mViewPager.setCurrentItem(frag1Pos);
            isUserPaging = true;
            Toast.makeText(AccountListActivity.this,
                    "Error on account entry, page 1",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (frag2.validatePageErrors()) {
            isUserPaging = false;
            mViewPager.setCurrentItem(frag2Pos);
            isUserPaging = true;
            Toast.makeText(AccountListActivity.this,
                    "Error on account entry, page 2",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (frag3.validatePageErrors()) {
            isUserPaging = false;
            mViewPager.setCurrentItem(frag3Pos);
            isUserPaging = true;
            Toast.makeText(AccountListActivity.this,
                    "Error on account entry, page 3",
                    Toast.LENGTH_LONG).show();
            return;
        }

//        Account account = mRetainedFragment.getData().getmSectionsPagerAdapter().collectChgs();
//        Log.d(TAG, "saveAccount: account to save " + account);

//        Log.d(TAG, "saveAccount: org account " + mRetainedFragment.getData().getAccount());

        boolean chgsMade = false;

        if (frag1 != null) {
            if (frag1.collectChgs()) {
                chgsMade = true;
            }
        }
        if (frag2 != null) {
            if (frag2.collectChgs()) {
                chgsMade = true;
            }
        }
        if (frag3 != null) {
            if (frag3.collectChgs()) {
                chgsMade = true;
            }
        }

        if (!chgsMade) {
            Toast.makeText(AccountListActivity.this,
                    "No changes made to the Account",
                    Toast.LENGTH_LONG).show();
            return;
        }
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(AccountsContract.Columns.CORP_NAME_COL, account.getCorpName());
        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, account.getCorpWebsite());
        values.put(AccountsContract.Columns.USER_NAME_COL, account.getUserName());
        values.put(AccountsContract.Columns.USER_EMAIL_COL, account.getUserEmail());
        values.put(AccountsContract.Columns.SEQUENCE_COL, account.getSequence());
        values.put(AccountsContract.Columns.OPEN_DATE_COL, account.getOpenLong());
        values.put(AccountsContract.Columns.NOTE_COL, account.getNote());
        values.put(AccountsContract.Columns.REF_FROM_COL, account.getRefFrom());
        values.put(AccountsContract.Columns.REF_TO_COL, account.getRefTo());
        long actvylong = System.currentTimeMillis();
        values.put(AccountsContract.Columns.ACTVY_DATE_COL, actvylong);
        account.setActvyLong(actvylong);

        if (accountMode == AccountsContract.ACCOUNT_ACTION_ADD) {
            Uri uri = contentResolver.insert(AccountsContract.CONTENT_URI, values);

            long id = AccountsContract.getId(uri);
            account.setId((int) id);
        } else {
            contentResolver.update(AccountsContract.buildIdUri(account.getId()), values, null, null);
        }

        frag1.fillPage();

        if (accountMode == AccountsContract.ACCOUNT_ACTION_ADD) {
            Toast.makeText(AccountListActivity.this,
                    "Account entry added to database",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AccountListActivity.this,
                    "Account entry updated to database",
                    Toast.LENGTH_LONG).show();
        }
        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
    }

//
//    private void setupAdapter() {
////        Cursor cursor = createCursor();
//        Cursor cursor = null;
//        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, this);
//
//        mAccountAdapter = new AccountRecyclerViewAdapter(mSortorder, 1, cursor,
//                (AccountRecyclerViewAdapter.OnAccountClickListener) this);
//        mRecyclerView.setAdapter(mAccountAdapter);
//    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
////    public Cursor createCursor() {
////        Log.d(TAG, "createCursor: starts");
//        Log.d(TAG, "onCreateLoader: id " + String.valueOf(id));
//
//        String[] projectionAcct =
//                {AccountsContract.Columns._ID_COL,
//                        AccountsContract.Columns.PASSPORT_ID_COL,
//                        AccountsContract.Columns.CORP_NAME_COL,
//                        AccountsContract.Columns.USER_NAME_COL,
//                        AccountsContract.Columns.USER_EMAIL_COL,
//                        AccountsContract.Columns.CORP_WEBSITE_COL,
//                        AccountsContract.Columns.NOTE_COL,
//                        AccountsContract.Columns.OPEN_DATE_COL,
//                        AccountsContract.Columns.ACTVY_DATE_COL,
//                        AccountsContract.Columns.SEQUENCE_COL,
//                        AccountsContract.Columns.REF_FROM_COL,
//                        AccountsContract.Columns.REF_TO_COL};
////        , SuggestsContract.Columns.ACTVY_DATE_COL,
////                SuggestsContract.Columns.NOTE_COL};{
//        // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
////        String sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE";
//        String sortOrder;
//        switch (mSortorder) {
//            case AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE:
//                sortOrder = AccountsContract.Columns.OPEN_DATE_COL + " DESC," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
//                break;
//            case AccountsContract.ACCOUNT_LIST_BY_SEQUENCE:
//                sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
//                break;
//            case AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID:
//                sortOrder = AccountsContract.Columns.PASSPORT_ID_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
//                break;
//            default:
//                sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE ASC";
//                break;
//        }
////        String sortOrder = AccountsContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
//        return new CursorLoader(this,
//                AccountsContract.CONTENT_URI,
//                projectionAcct,
////                null,
//                null,
//                null,
//                sortOrder);
////        Cursor cursor = getContentResolver().query(
////                AccountsContract.CONTENT_URI, null, null, null, sortOrder);
//////        Log.d(TAG, "onCreateLoader: cursor " + cursor.toString());
////        return cursor;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        //        Log.d(TAG, "onLoadFinished: starts");
////        this.loader = loader;
//        int count = 0;
//        mAccountAdapter.swapCursor(data);
//        count = mAccountAdapter.getItemCount();
////        if (count == 0) {
////            twCurrentTitle.setText("No accounts, + to add");
////        } else {
////            twCurrentTitle.setText("Accounts");
////        }
////        Log.d(TAG, "onLoadFinished: count is " + count);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
////        Log.d(TAG, "onLoaderReset: starts");
//        mAccountAdapter.swapCursor(null);
//    }


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


    private void addAccountRequest() {
//        Log.d(TAG, "addAccountRequest: starts");
//        if (mTwoPane) {
//            mAccountAdapter.resetSelection();
        accountMode = AccountsContract.ACCOUNT_ACTION_ADD;
        this.account = new Account();
        updatePages();

//        isUserPaging = false;
//        mViewPager.setCurrentItem(frag3Pos);
//        mSectionsPagerAdapter.notifyDataSetChanged();
//        mViewPager.setCurrentItem(frag2Pos);
//        mSectionsPagerAdapter.notifyDataSetChanged();
//        mViewPager.setCurrentItem(frag1Pos);
//        mSectionsPagerAdapter.notifyDataSetChanged();
//        isUserPaging = true;
//
////        frag1 = AccountPlaceholderFrag1.newInstance();
////        frag2 = AccountPlaceholderFrag2.newInstance();
////        frag3 = AccountPlaceholderFrag3.newInstance();
////        mSectionsPagerAdapter.setFrag1(frag1);
////        mSectionsPagerAdapter.setFrag2(frag2);
////        mSectionsPagerAdapter.setFrag3(frag3);
//
//        if (frag1 != null) {
//            frag1.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//        }
//        if (frag2 != null) {
//            frag2.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//        }
//        if (frag3 != null) {
//            frag3.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//        }
//
//        mViewPager.setCurrentItem(frag1Pos);
////        mRetainedFragment.getData().setAccount(new Account());
////            mSectionsPagerAdapter.setAccount(mRetainedFragment.getData().getAccount());
////        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag3Pos());
////            mSectionsPagerAdapter.notifyDataSetChanged();
////        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
////            mSectionsPagerAdapter.notifyDataSetChanged();
////        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().notifyDataSetChanged();
////            getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);
//
//////            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
////            FragmentManager fragmentManager = getSupportFragmentManager();
////            AppDialog newFragment = AppDialog.newInstance();
////            Bundle args = new Bundle();
////            args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_ACCOUNT_ACTIONS_LIST);
////            args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_ACCOUNT_LIST_OPTIONS);
////            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.listdiag_acc_message));
////            args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.listdiag_acc_sub_message));
////
////            newFragment.setArguments(args);
////
////            newFragment.show(fragmentManager, "dialog");
//
////        } else {
//////            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
////            // in single-pane mode, start the detail activity for the selected item Id.
////            Intent detailIntent = new Intent(this, AccountActivity.class);
////
////
////            if (accountMode == AccountsContract.ACCOUNT_ACTION_CHG) { // editing an account
////                detailIntent.putExtra(Account.class.getSimpleName(), account.getId());
////                detailIntent.putExtra(AccountsContract.ACCOUNT_TWO_PANE, false);
////                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_CHG);
////            } else { // adding an account
////                startActivityForResult(detailIntent, AccountsContract.ACCOUNT_ACTION_ADD);
////            }
////        }
    }


    @Override
    public void onAccountListSelect(Account account) {
        Log.d(TAG, "onAccountListSelect: ");
        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
        this.account = account;
        updatePages();
    }

    private void updatePages() {
        isUserPaging = false;
        mViewPager.setCurrentItem(frag2Pos);

        frag1.fillPage();
        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag1Pos, frag1);
        mSectionsPagerAdapter.notifyDataSetChanged();
//        new CountDownTimer(10000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
////                    mTextField.setText("done!");
//            }
//        }.start();


        frag2.fillPage();
        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag2Pos, frag2);
        mSectionsPagerAdapter.notifyDataSetChanged();
//        new CountDownTimer(10000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
////                    mTextField.setText("done!");
//            }
//        }.start();
        frag3.fillPage();
        mSectionsPagerAdapter.setPrimaryItem(mViewPager, frag3Pos, frag3);
        mSectionsPagerAdapter.notifyDataSetChanged();
//        new CountDownTimer(10000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
////                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
////                    mTextField.setText("done!");
//            }
//        }.start();
        isUserPaging = true;

//        mSectionsPagerAdapter.fillPages();

//        isUserPaging = false;

//        frag1 = (AccountPlaceholderFrag1)mSectionsPagerAdapter.instantiateItem(mViewPager, frag1Pos);
//        frag2 = (AccountPlaceholderFrag2)mSectionsPagerAdapter.instantiateItem(mViewPager, frag2Pos);
//        frag3 = (AccountPlaceholderFrag3)mSectionsPagerAdapter.instantiateItem(mViewPager, frag3Pos);

//        if (frag1 != null) {
//
//            mViewPager.setCurrentItem(frag3Pos);
//            frag1.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(frag3Pos);
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mSectionsPagerAdapter.getItem(frag1Pos);
//        }
//        if (frag2 != null) {
//            mViewPager.setCurrentItem(frag2Pos);
//            frag2.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mSectionsPagerAdapter.getItem(frag2Pos);
//            mViewPager.setCurrentItem(frag2Pos);
//            mSectionsPagerAdapter.notifyDataSetChanged();
//        }
//        if (frag3 != null) {
//            mViewPager.setCurrentItem(frag1Pos);
//            frag3.fillPage();
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            mSectionsPagerAdapter.getItem(frag3Pos);
//            mViewPager.setCurrentItem(frag1Pos);
//            mSectionsPagerAdapter.notifyDataSetChanged();
//        }

//        isUserPaging = true;

        //        mRetainedFragment.getData().setAccount(account);
//        mSectionsPagerAdapter.setAccount(account);
//        isUserPaging = false;
//        int currPage = mViewPager.getCurrentItem();
//        frag1 = AccountPlaceholderFrag1.newInstance();
//        frag2 = AccountPlaceholderFrag2.newInstance();
//        frag3 = AccountPlaceholderFrag3.newInstance();
//        mSectionsPagerAdapter.setFrag1(frag1);
//        mSectionsPagerAdapter.setFrag2(frag2);
//        mSectionsPagerAdapter.setFrag3(frag3);
        mViewPager.setCurrentItem(frag1Pos);
//        mViewPager.setCurrentItem(frag3Pos);
//        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag3Pos());
//        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
//        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag2Pos());
//        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
//        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(mRetainedFragment.getData().getFrag1Pos());
//        Log.d(TAG, "onAccountListSelect: currPage " + currPage);
//        if (currPage != 0) {
//            mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(currPage);
//        }
//        isUserPaging = true;


////        Context context = getContext();
//        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
//        Intent intent = new Intent(this, AccountActivity.class);
//        intent.putExtra(Account.class.getSimpleName(), account.getId());
//
//        startActivityForResult(intent, AccountsContract.ACCOUNT_ACTION_CHG);

    }

//    @Override
//    public void onAccountLandListSelect(Account account) {
//        Log.d(TAG, "onAccountLandListSelect: id " + account.getId());
////        accountActivityFragment = new AccountActivityFragment();
////        this.account = account;
////        Bundle arguments = new Bundle();
////        arguments.putInt(Account.class.getSimpleName(), account.getId());
////        accountActivityFragment.setArguments(arguments);
////        getSupportFragmentManager().beginTransaction()
////                .replace(R.id.item_detail_container, accountActivityFragment)
////                .commit();
//
////        Log.d(TAG, "onAccountLandListSelect: account " + account);
//        accountMode = AccountsContract.ACCOUNT_ACTION_CHG;
////        this.account = account;
//        mRetainedFragment.getData().setAccount(account);
////        mRetainedFragment.getData().setAccount(account);
//        isUserPaging = false;
////        int currPage = mViewPager.getCurrentItem();
//        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag3Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(SectionsPagerAdapter.frag3Pos);
//        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag2Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(SectionsPagerAdapter.frag2Pos);
//        mViewPager.setCurrentItem(mRetainedFragment.getData().getFrag1Pos());
////        mRetainedFragment.getData().getmSectionsPagerAdapter().loadPage(SectionsPagerAdapter.frag1Pos);
////        if (currPage != 0) {
////            mSectionsPagerAdapter.loadPage(currPage);
////        }
//        isUserPaging = true;
////        mSectionsPagerAdapter.notifyDataSetChanged();
////        mSectionsPagerAdapter.getItem(0);
////        mSectionsPagerAdapter.getItem(1);
////        mSectionsPagerAdapter.getItem(2);
//////        mViewPager.setCurrentItem(2);
//////        mViewPager.setCurrentItem(1);
////        mViewPager.setCurrentItem(0);
////        mViewPager.setCurrentItem(2);
////        mViewPager.setCurrentItem(1);
////        mViewPager.setCurrentItem(0);
////        frag1.fillPage(account);
////        frag2.fillPage(account);
////        frag3.fillPage(account);
////        if (mSectionsPagerAdapter != null) {
////            mSectionsPagerAdapter.setAccount(account);
////            mViewPager.setCurrentItem(2);
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mViewPager.setCurrentItem(1);
////            mSectionsPagerAdapter.notifyDataSetChanged();
////            mViewPager.setCurrentItem(0);
////            mSectionsPagerAdapter.notifyDataSetChanged();
//////            mSectionsPagerAdapter.fillPages();
//////            int limit = mSectionsPagerAdapter.getCountBuilt();
//////            for (int i = 0; i < limit; i++) {
//////                mSectionsPagerAdapter.fillPages;
////////                mSectionsPagerAdapter.removeFrag(0);
////
//////            if (mSectionsPagerAdapter.getCountBuilt() > 0) {
////////        getSupportFragmentManager().beginTransaction().remove(frag).commit();
//////                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.item_detail_container)).commit();
//////
//////            }
////        }
//
//
////        // primary sections of the activity.
////        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
////
////
////        // Create the adapter that will return a fragment for each of the three
////
////        // Set up the ViewPager with the sections adapter.
////        mViewPager = (ViewPager) findViewById(R.id.item_detail_container);
////        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//
//    }

//    private void clearFrag() {
//        this.account = new Account();
//        accountMode = AccountsContract.ACCOUNT_ACTION_ADD;
//        Bundle arguments = new Bundle();
//        arguments.putInt(Account.class.getSimpleName(), -1);
//        accountActivityFragment = new AccountActivityFragment();
//        accountActivityFragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.item_detail_container, accountActivityFragment)
//                .commit();
//    }


    private void clearAccount() {
        account = new Account();
        accountMode = AccountsContract.ACCOUNT_ACTION_ADD;
        fragList.resetSelection();
//        getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + mViewPager.getCurrentItem());
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

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
                fragList.deleteAccount(args.getInt(AppDialog.DIALOG_ACCOUNT_ID));
//                getContentResolver().delete(AccountsContract.buildIdUri(args.getInt(AppDialog.DIALOG_ACCOUNT_ID)), null, null);
//                setupAdapter();
//                if (mTwoPane) {
                clearAccount();
//                } else {
//                    clearFrag();
//                }
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
        deleteAllSearchItems();
        AccountSearchLoaderCallbacks loaderAcctCallbacks = new AccountSearchLoaderCallbacks(this);
        getLoaderManager().restartLoader(SEARCH_LOADER_ID, null, loaderAcctCallbacks);
        Toast.makeText(this,
                "Search Dictionary DB built",
                Toast.LENGTH_LONG).show();

    }

    private void deleteAllSearchItems() {
//		String selectionClause = SearchManager.SUGGEST_COLUMN_FLAGS + " = ?";
//		String[] selectionArgs = { "account" };
//        Log.d(TAG, "deleteAllSuggestions: delUri " + SearchesContract.CONTENT_URI_TRUNCATE);
        getContentResolver().delete(
                SearchesContract.CONTENT_URI_TRUNCATE,
                null, null);
        getLoaderManager().restartLoader(SEARCH_LOADER_ID, null, null);
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
                clearAccount();
                fragList.resortList(mSortorder);
//                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_OPEN_DATE:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE;
                clearAccount();
                fragList.resortList(mSortorder);
//                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_ACCT_ID:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID;
                clearAccount();
                fragList.resortList(mSortorder);
//                setupAdapter();
                break;
            case AppDialog.DIALOG_ACCT_LIST_USER_SEQ:
                mSortorder = AccountsContract.ACCOUNT_LIST_BY_SEQUENCE;
                clearAccount();
                fragList.resortList(mSortorder);
//                setupAdapter();
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
                fragList.setSelected_position(-1);
                fragList.refreshList();
//                setupAdapter();
                break;
            }
            case AccountsContract.ACCOUNT_ACTION_ADD: {
                Log.d(TAG, "onActivityResult: returned from edit add");
                fragList.refreshList();
//                setupAdapter();
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

    @Override
    public void onAccount2Instance() {
        Log.d(TAG, "onAccount2Instance: ");
//        mSectionsPagerAdapter.loadPage(SectionsPagerAdapter.frag2Pos);
//        frag2RowId = rowId;
//        frag2.fillPage(this.account);
    }

    @Override
    public void onAccount3Instance() {
        Log.d(TAG, "onAccount3Instance: ");
//        mSectionsPagerAdapter.loadPage(SectionsPagerAdapter.frag3Pos);
//        frag3RowId = rowId;
//        frag3.fillPage(this.account);
    }

    @Override
    public void onAccount1Instance() {
        Log.d(TAG, "onAccount1Instance: ");

//        mSectionsPagerAdapter.loadPage(SectionsPagerAdapter.frag1Pos);
//        frag1RowId = rowId;
//        frag1.fillPage(this.account);
    }


}