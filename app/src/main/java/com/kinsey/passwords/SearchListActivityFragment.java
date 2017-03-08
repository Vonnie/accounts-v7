package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.provider.SearchRecyclerViewAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchRecyclerViewAdapter.OnAccountClickListener {
    private static final String TAG = "SearchListActivityFragm";

    private SearchRecyclerViewAdapter mSearchListAdapter; // add adapter reference

    private OnActionListener mActionListener = null;

    interface OnActionListener {
        void onLoadSearchClicked();
        void onSearchRequestClicked(String searchValue);
    }

    public SearchListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.srchlst_items_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Bundle arguments = getActivity().getIntent().getExtras();
        int accountId = (int) arguments.getSerializable(SearchListActivity.class.getSimpleName());
        Log.d(TAG, "onCreateView: accountid " + accountId);

//        Log.d(TAG, "onCreateView: search adt cursor");

        if (accountId == -1) {
            mSearchListAdapter = new SearchRecyclerViewAdapter(this.getContext(),
                    accountId,
                    SearchesContract.cursorSearch,
                    (SearchRecyclerViewAdapter.OnAccountClickListener) getActivity());
        } else {
            Cursor cursor = getAccount(accountId);
            mSearchListAdapter = new SearchRecyclerViewAdapter(this.getContext(),
                    accountId,
                    cursor,
                    (SearchRecyclerViewAdapter.OnAccountClickListener) getActivity());
        }
        recyclerView.setAdapter(mSearchListAdapter);
        return view;
    }

    private Cursor getAccount(int accountId) {
        Log.d(TAG, "getAccount: " + AccountsContract.buildIdUri(accountId));
        return getActivity().getContentResolver().query(
                AccountsContract.buildIdUri(accountId),
                null, null, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> cursor = new CursorLoader(getActivity(),
                SearchesContract.CONTENT_URI,
                null,
                null,
                null,
                null);
        return cursor;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activies containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof OnActionListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement SearchListFragment.OnActionClicked interface");
        }
        mActionListener = (OnActionListener) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mActionListener = null;
    }

    @Override
    public void onAccountEditClick(Account account) {

    }

    @Override
    public void onAccountDeleteClick(Account account) {

    }
}
