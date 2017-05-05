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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinsey.passwords.items.SuggestsContract;
import com.kinsey.passwords.provider.CursorRecyclerViewAdapter;

import static com.kinsey.passwords.MainActivity.SUGGEST_LOADER_ID;


/**
 * A placeholder fragment containing a simple view.
 */
public class SuggestListActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "SuggestListActivityFrag";

    private CursorRecyclerViewAdapter mSuggestAdapter; // add adapter reference

    TextView twCurrentTitle;
    Loader<Cursor> loader;

    public SuggestListActivityFragment() {
//        Log.d(TAG, "SuggestListActivityFragment: starts");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onActivityCreated: starts loader_id " + LOADER_ID);
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SUGGEST_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_suggest_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.suggest_items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);

        mSuggestAdapter = new CursorRecyclerViewAdapter(null,
                (CursorRecyclerViewAdapter.OnSuggestClickListener) getActivity());
        recyclerView.setAdapter(mSuggestAdapter);
//        Log.d(TAG, "onCreateView: returning adapter count: " + mSuggestAdapter.getItemCount());

        return view;

//        return inflater.inflate(R.layout.fragment_suggest_list, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection =
                {SuggestsContract.Columns._ID_COL,
                        SuggestsContract.Columns.PASSWORD_COL,
                        SuggestsContract.Columns.SEQUENCE_COL};
//        , SuggestsContract.Columns.ACTVY_DATE_COL,
//                SuggestsContract.Columns.NOTE_COL};
        // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
        String sortOrder = SuggestsContract.Columns.SEQUENCE_COL + "," + SuggestsContract.Columns.PASSWORD_COL + " COLLATE NOCASE";
//        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
        return new CursorLoader(getActivity(),
                SuggestsContract.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.loader = loader;
        int count = 0;
        mSuggestAdapter.swapCursor(data);
        count = mSuggestAdapter.getItemCount();
        if (count == 0) {
            twCurrentTitle.setText("No generated passwords, check below");
        } else {
            twCurrentTitle.setText("");
            twCurrentTitle.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSuggestAdapter.swapCursor(null);
    }
}
