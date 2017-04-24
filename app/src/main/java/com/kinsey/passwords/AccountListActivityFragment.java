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
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AccountRecyclerViewAdapter.OnAccountClickListener {

    private static final String TAG = "AccountListActivityFrag";

    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference
    TextView twCurrentTitle;
    Loader<Cursor> loader;

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private int mSortorder;

    @Override
    public void onAccountEditClick(Account account) {

    }

    @Override
    public void onAccountDeleteClick(Account account) {

    }

    @Override
    public void onAccountListSelect(Account account) {
//        Context context = getContext();
//        Intent intent = new Intent(context, AccountActivity.class);
//        intent.putExtra(AccountActivity.ARG_ITEM_ID, holder.mItem.id);
//
//        context.startActivity(intent);

    }

    @Override
    public void onAccountLandListSelect(Account account) {
//        Bundle arguments = new Bundle();
//        arguments.putString(AccountListActivityFragment.ARG_ITEM_ID, holder.mItem.id);
//        AccountActivityFragment fragment = new AccountActivityFragment();
//        fragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.item_detail_container, fragment)
//                .commit();

    }


    public enum FragmentListMode {CORP_NAME, OPEN_DATE}

    ;
    private FragmentListMode mListMode = FragmentListMode.CORP_NAME;

    public enum FragmentMsgMode {ADD, SORTED_OPT}

    ;
    private FragmentMsgMode mMsgMode = FragmentMsgMode.ADD;

    public AccountListActivityFragment() {
//        Log.d(TAG, "AccountListActivityFragment: starts");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onActivityCreated: starts loader_id " + LOADER_ID);
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.account_items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);
        Bundle arguments = getActivity().getIntent().getExtras();
//        Log.d(TAG, "onCreateView: agrs " + Account.class.getSimpleName());
//        Log.d(TAG, "onCreateView: agrs " + arguments.getSize(Account.class.getSimpleName()));
//        Log.d(TAG, "onCreateView: agrs " + arguments.getSize(AccountsContract.ACCOUNT_TWO_PANE));

//        Log.d(TAG, "onCreateView: arg() " + getArguments().get(AccountsContract.ACCOUNT_TWO_PANE));

//        mSortorder = (int) getArguments().get(Account.class.getSimpleName());
//        Log.d(TAG, "onCreateView: sortorder " + mSortorder);
//        Log.d(TAG, "onCreateView: twopane "
//                + AccountsContract.ACCOUNT_TWO_PANE + ":"
//                + getArguments().get(AccountsContract.ACCOUNT_TWO_PANE));
//        boolean twopane = (boolean) getArguments().get(AccountsContract.ACCOUNT_TWO_PANE);
//        Log.d(TAG, "onCreateView: mSortorder " + mSortorder);

        if (view.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

//        Log.d(TAG, "onCreate: twoPane " + mTwoPane);
        boolean twopane = false;
        mSortorder = (int) arguments.getSerializable(Account.class.getSimpleName());
//        mAccountAdapter = new AccountRecyclerViewAdapter(twopane, mSortorder, null,
//                (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
        recyclerView.setAdapter(mAccountAdapter);
        Log.d(TAG, "onCreateView: returning adapter count: " + mAccountAdapter.getItemCount());

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.d(TAG, "onCreateLoader: starts");
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
            sortOrder = AccountsContract.Columns.OPEN_DATE_COL + " DESC," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
        } else {
            if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
                sortOrder = AccountsContract.Columns.SEQUENCE_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
            } else {
                if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_PASSPORT_ID) {
                    sortOrder = AccountsContract.Columns.PASSPORT_ID_COL + "," + AccountsContract.Columns.CORP_NAME_COL + " COLLATE NOCASE ASC";
                } else {
                    sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE ASC";
                }
            }
        }
//        String sortOrder = AccountsContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
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
//        Log.d(TAG, "onLoadFinished: starts");
        this.loader = loader;
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
}
