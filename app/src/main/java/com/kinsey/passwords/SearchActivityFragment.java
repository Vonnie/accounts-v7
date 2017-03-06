package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.provider.SearchRecyclerViewAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "SearchActivityFragment";

    private SearchRecyclerViewAdapter mSuggestAdapter; // add adapter reference
    ImageButton mSearchRequestButton;
    TextView mSearchValue;

    private OnActionListener mActionListener = null;

    interface OnActionListener {
        void onLoadSearchClicked();
        void onSearchRequestClicked(String searchValue);
    }

    public SearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.srch_items_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        Bundle arguments = getActivity().getIntent().getExtras();

//        mSuggestAdapter = new SearchRecyclerViewAdapter(null,
//                (SearchRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        recyclerView.setAdapter(mSuggestAdapter);
//        Log.d(TAG, "onCreateView: returning adapter count: " + mSuggestAdapter.getItemCount());

//        mSearchValue = (TextView) view.findViewById(R.id.srch_value);
//        mSearchRequestButton = (ImageButton) view.findViewById(R.id.srch_request_button);
//
//        mSearchRequestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mActionListener.onSearchRequestClicked(mSearchValue.getText().toString());
//            }
//        });

        return view;
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
                    + " must implement MainActivityFragment.OnActionClicked interface");
        }
        mActionListener = (OnActionListener) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mActionListener = null;
    }
}
