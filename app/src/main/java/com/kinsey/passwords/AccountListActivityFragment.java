package com.kinsey.passwords;

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

import static com.kinsey.passwords.MainActivityFragment.LOADER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountListActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "AccountListActivityFrag";
    
    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference
    TextView twCurrentTitle;
    Loader<Cursor> loader;
    private int mSortorder;

    public enum FragmentListMode { CORP_NAME, OPEN_DATE };
    private FragmentListMode mListMode = FragmentListMode.CORP_NAME;

    public enum FragmentMsgMode { ADD, SORTED_OPT };
    private FragmentMsgMode mMsgMode = FragmentMsgMode.ADD;

    public AccountListActivityFragment() {
        Log.d(TAG, "AccountListActivityFragment: starts");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts loader_id " + LOADER_ID);
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.account_items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);
        Bundle arguments = getActivity().getIntent().getExtras();

        mSortorder = (int) arguments.getSerializable(Account.class.getSimpleName());
        Log.d(TAG, "onCreateView: mSortorder " + mSortorder);
        mAccountAdapter = new AccountRecyclerViewAdapter(mSortorder, null,
                (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
        recyclerView.setAdapter(mAccountAdapter);
        Log.d(TAG, "onCreateView: returning adapter count: " + mAccountAdapter.getItemCount());

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts");
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
        if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE) {
            sortOrder = AccountsContract.Columns.OPEN_DATE_COL + " DESC," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE";
        } else {
            if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
                sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE";
            } else {
                sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE";
            }
        }
//        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
        Loader<Cursor> cursor = new CursorLoader(getActivity(),
                AccountsContract.CONTENT_URI,
                projectionAcct,
                null,
                null,
                sortOrder);
        Log.d(TAG, "onCreateLoader: cursor " + cursor.toString());
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts");
        this.loader = loader;
        int count = 0;
        mAccountAdapter.swapCursor(data);
        count = mAccountAdapter.getItemCount();
        if (count == 0) {
            twCurrentTitle.setText("No accounts, + to add");
        } else {
            twCurrentTitle.setText("Accounts");
        }
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAccountAdapter.swapCursor(null);
    }
}
