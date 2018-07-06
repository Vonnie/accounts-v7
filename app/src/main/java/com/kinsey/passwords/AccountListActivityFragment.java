package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;

import static com.kinsey.passwords.MainActivity.ACCOUNT_LOADER_ID;

//import static com.kinsey.passwords.AccountListActivity.account;
//import static com.kinsey.passwords.AccountListActivity.accountSelectedPos;
//import static com.kinsey.passwords.AccountListActivity.accountSortorder;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountListActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AccountRecyclerViewAdapter.OnAccountClickListener {
//        ViewPager.OnPageChangeListener {

    private static final String TAG = "AccountListActivityFrag";
    public static final String ARG_SELECTED_POS = "selected_pos";

    RecyclerView mRecyclerView;
    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference
    TextView twCurrentTitle;
//    Loader<Cursor> loader;

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
//    private boolean mTwoPane = false;

//    private int mSortorder = AccountsContract.ACCOUNT_LIST_BY_CORP_NAME;
//    private int accountSortorder = AccountsContract.ACCOUNT_LIST_BY_CORP_NAME;


    public enum FragmentListMode {CORP_NAME, OPEN_DATE}

    ;
    private FragmentListMode mListMode = FragmentListMode.CORP_NAME;


    public enum FragmentMsgMode {ADD, SORTED_OPT}

    ;
    private FragmentMsgMode mMsgMode = FragmentMsgMode.ADD;

    public AccountListActivityFragment() {
//        Log.d(TAG, "AccountListActivityFragment: starts");
    }

    public static AccountListActivityFragment newInstance() {
        AccountListActivityFragment fragment = new AccountListActivityFragment();
        Log.d(TAG, "newInstance: ");
//        Bundle args = new Bundle();
//        args.putInt(ARG_SELECTED_POS, selectedPos);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts loader_id " + ACCOUNT_LOADER_ID);
        super.onActivityCreated(savedInstanceState);

        // Activities containing this fragment must implement its callbacks.
        Activity activity = getActivity();
        if(!(activity instanceof AccountRecyclerViewAdapter.OnAccountClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AccountRecyclerViewAdapter.OnAccountClickListener interface");
        }

        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, this);
    }

    @Override
    public void onAccountListSelect(Account account) {
        Log.d(TAG, "onAccountListSelect: ");
        AccountRecyclerViewAdapter.OnAccountClickListener listener = (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity();
        if (listener !=  null) {
            listener.onAccountListSelect(account);
        }
    }

    @Override
    public void onAccountUpClick(Account account) {

    }

    @Override
    public void onAccountDownClick(Account account) {

    }

    @Override
    public void onAccountLong(Account account) {

        Log.d(TAG, "onAccountLong: ");
        AccountRecyclerViewAdapter.OnAccountClickListener listener = (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity();
        if(listener != null) {
            listener.onAccountLong(account);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: starts");
//        int selected_position = getArguments().getInt(ARG_SELECTED_POS);
//        Log.d(TAG, "onCreateView: selected_position " + selected_position);
//        selected_position = accountSelectedPos;
//        Log.d(TAG, "onCreateView: selected_position " + selected_position);
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.account_items_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);
//        Bundle arguments = getActivity().getIntent().getExtras();
//        Log.d(TAG, "onCreateView: agrs " + Account.class.getSimpleName());
//        Log.d(TAG, "onCreateView: agrs " + arguments.getSize(Account.class.getSimpleName()));
//        Log.d(TAG, "onCreateView: agrs " + arguments.getSize(AccountsContract.ACCOUNT_TWO_PANE));

//        Log.d(TAG, "onCreateView: arg() " + getArguments().get(AccountsContract.ACCOUNT_TWO_PANE));

//        accountSortorder = (int) getArguments().get(Account.class.getSimpleName());
//        mSortorder = (int) getArguments().get(Account.class.getSimpleName());
//        Log.d(TAG, "onCreateView: sortorder " + mSortorder);
//        Log.d(TAG, "onCreateView: twopane "
//                + AccountsContract.ACCOUNT_TWO_PANE + ":"
//                + getArguments().get(AccountsContract.ACCOUNT_TWO_PANE));
//        boolean twopane = (boolean) getArguments().get(AccountsContract.ACCOUNT_TWO_PANE);
//        Log.d(TAG, "onCreateView: mSortorder " + mSortorder);

//        if (view.findViewById(R.id.item_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }

//        Log.d(TAG, "onCreate: twoPane " + mTwoPane);
//        boolean twopane = false;
//        mSortorder = (int) arguments.getSerializable(Account.class.getSimpleName());
//        Log.d(TAG, "onCreateView: sortorder " + mSortorder);
//        mAccountAdapter = new AccountRecyclerViewAdapter(twopane, mSortorder, null,
//                (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        recyclerView.setAdapter(mAccountAdapter);
//        Log.d(TAG, "onCreateView: returning adapter count: " + mAccountAdapter.getItemCount());

        createLoader();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void createLoader() {
        Cursor cursor = null;
        Log.d(TAG, "onCreateView: abt to init loader: ");

        if (mAccountAdapter == null) {
            mAccountAdapter = new AccountRecyclerViewAdapter(getContext(),null, this);
        }
//            mAccountAdapter = new AccountRecyclerViewAdapter(getContext(), cursor,
//                    (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        } else {
//            mAccountAdapter.setListener((AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        }
        mRecyclerView.setAdapter(mAccountAdapter);

        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, this);
    }

    public void swapOut() {
        mAccountAdapter.swapCursor(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts");
//        Log.d(TAG, "onCreateLoader: id " + String.valueOf(id));

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
        int sortorderType = mAccountAdapter.getAccountSortorder();

        if (sortorderType == AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE) {
            sortOrder = AccountsContract.Columns.OPEN_DATE_COL + " DESC," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
        } else {
            if (sortorderType == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
                sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
            } else {
                if (sortorderType == AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID) {
                    sortOrder = AccountsContract.Columns.PASSPORT_ID_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
                } else {
                    sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE ASC";
                }
            }
        }



        Log.d(TAG, "onCreateLoader: sortorder " + sortOrder);
//        String sortOrder = AccountsContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
        Loader<Cursor> cursor = new CursorLoader(getActivity(),
                AccountsContract.CONTENT_URI,
                projectionAcct,
                null,
                null,
                sortOrder);
//        Log.d(TAG, "onCreateLoader: cursor " + cursor.toString());
        return cursor;
    }


    public void resortList(int sortorder) {
        Log.d(TAG, "resortList: " + sortorder);
        mAccountAdapter.setAccountSortorder(sortorder);
//        mRecyclerView.swapAdapter(mAccountAdapter, false);
        Log.d(TAG, "resortFragList: destroy Loader");
        getLoaderManager().destroyLoader(ACCOUNT_LOADER_ID);

        Log.d(TAG, "resortFragList: createLoader");
        createLoader();

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts");
        Log.d(TAG, "onLoadFinished: starts");
//        this.loader = loader;
        int count = 0;
        mAccountAdapter.swapCursor(data);
        if (mAccountAdapter.getAccountSelectedPos() != -1) {
            Log.d(TAG, "onLoadFinished: adapter set");
            mRecyclerView.scrollToPosition(mAccountAdapter.getAccountSelectedPos());
//            mRecyclerView.findViewHolderForAdapterPosition(accountSelectedPos);
//            mRecyclerView.findViewHolderForAdapterPosition(accountSelectedPos).itemView.performClick();
        }

//        count = mAccountAdapter.getItemCount();
//        if (count == 0) {
//            twCurrentTitle.setText("No accounts, + to add");
//        } else {
//            twCurrentTitle.setText("Accounts");
//        }
//        Log.d(TAG, "onLoadFinished: count is " + count);
    }


//    public void setViewerPos() {
//        if (accountSelectedPos != -1) {
//            Log.d(TAG, "setViewerPos: postioning requested");
////            mRecyclerView.findViewHolderForAdapterPosition(accountSelectedPos).itemView.performClick();
//        }
//    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAccountAdapter.swapCursor(null);
    }



    public void deleteAccount(Context context, int accountId) {
        context.getContentResolver().delete(AccountsContract.buildIdUri((long)accountId), null, null);
//        getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);
        resetSelectItem();
    }


    public void setAcctId(int acctId) {
        mAccountAdapter.setAccountSelectedId(acctId);
        mAccountAdapter.setAccountSelectById(true);
        mAccountAdapter.setPosById(acctId);
    }


    public void resetSelectItem() {
        Log.d(TAG, "resetSelectItem: ");
        mAccountAdapter.setAccountSelectedPos(-1);
        getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);


//        int savePos = accountSelectedPos;
//        mAccountAdapter.resetSelectItem();
//        mRecyclerView.scrollToPosition(savePos);
    }

//    public void saveAccount(Context context, boolean isForAdd) {
////        ContentResolver contentResolver = getActivity().getContentResolver();
//        ContentValues values = new ContentValues();
//
//        values.put(AccountsContract.Columns.CORP_NAME_COL, account.getCorpName());
//        values.put(AccountsContract.Columns.CORP_WEBSITE_COL, account.getCorpWebsite());
//        values.put(AccountsContract.Columns.USER_NAME_COL, account.getUserName());
//        values.put(AccountsContract.Columns.USER_EMAIL_COL, account.getUserEmail());
//        values.put(AccountsContract.Columns.SEQUENCE_COL, account.getSequence());
//        values.put(AccountsContract.Columns.OPEN_DATE_COL, account.getOpenLong());
//        values.put(AccountsContract.Columns.NOTE_COL, account.getNote());
//        values.put(AccountsContract.Columns.REF_FROM_COL, account.getRefFrom());
//        values.put(AccountsContract.Columns.REF_TO_COL, account.getRefTo());
//        long actvylong = System.currentTimeMillis();
//        values.put(AccountsContract.Columns.ACTVY_DATE_COL, actvylong);
//        account.setActvyLong(actvylong);
//
//        if (isForAdd) {
//            account.setPassportId(getMaxValue(context, AccountsContract.Columns.PASSPORT_ID_COL));
//            values.put(AccountsContract.Columns.PASSPORT_ID_COL,
//                    String.valueOf(account.getPassportId()));
//            Uri uri = context.getContentResolver().insert(AccountsContract.CONTENT_URI, values);
//
//            long id = AccountsContract.getId(uri);
//            account.setId((int) id);
//        } else {
//            context.getContentResolver().update(AccountsContract.buildIdUri(account.getId()), values, null, null);
//        }
//
//    }

    private int getMaxValue(Context context, String col) {
        int iId = 1;
        Cursor cursor = context.getContentResolver().query(
                AccountsContract.CONTENT_MAX_VALUE_URI, null, null, null, col);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int iIndex = cursor.getColumnIndex(col);
                iId = cursor.getInt(iIndex) + 1;
//                Log.d(TAG, "getMaxValue: " + iId);
            }
            cursor.close();
        }

//        Log.d(TAG, "getMaxValue: " + iId);
        return iId;
    }



//    private int getById(Context context, String col, int acctId) {
//        int iId = 1;
//        Cursor cursor = context.getContentResolver().query(
//                AccountsContract.CONTENT_MAX_VALUE_URI, null, null, null, col);
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                int iIndex = cursor.getColumnIndex(col);
//                iId = cursor.getInt(iIndex) + 1;
//                if (acctId == iId) {
//                    Log.d(TAG, "getById: " + iId);
//                    mAccountAdapter.resetSelectItem();
//                }
////                Log.d(TAG, "getMaxValue: " + iId);
//            }
//            cursor.close();
//        }
//
////        Log.d(TAG, "getMaxValue: " + iId);
//        return iId;
//    }



    public void resortFragList() {
//        this.mSortorder = sortorder;
//        mAccountAdapter.setmSortorder(sortorder);

        Log.d(TAG, "resortFragList: destroy Loader");
        getLoaderManager().destroyLoader(ACCOUNT_LOADER_ID);

        Log.d(TAG, "resortFragList: createLoader");
        createLoader();

//        Log.d(TAG, "resortFragList: init Loader");
//        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, this);
    }

    public void refreshList() {
        getLoaderManager().restartLoader(ACCOUNT_LOADER_ID, null, this);
    }

//    public int getSelected_position() {
//        return mAccountAdapter.getSelected_position();
//    }
//
//    public void setSelected_position(int selectedPos) {
//        mAccountAdapter.setSelected_position(selectedPos);
//    }
//
//    public void resetSelection() {
//        mAccountAdapter.setSelected_position(-1);
//    }
}
