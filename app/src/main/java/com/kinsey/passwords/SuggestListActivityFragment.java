package com.kinsey.passwords;

import android.content.ContentResolver;
import android.content.ContentValues;
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

import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.items.SuggestsContract;
import com.kinsey.passwords.provider.CursorRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.kinsey.passwords.MainActivity.SUGGEST_LOADER_ID;


/**
 * A placeholder fragment containing a simple view.
 */
public class SuggestListActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        CursorRecyclerViewAdapter.OnSuggestClickListener{

    private static final String TAG = "SuggestListActivityFrag";

    RecyclerView mRecyclerView;
    private CursorRecyclerViewAdapter mSuggestAdapter; // add adapter reference

    TextView twCurrentTitle;
    Loader<Cursor> loader;

    public SuggestListActivityFragment() {
//        Log.d(TAG, "SuggestListActivityiFragment: starts");
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
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_suggest_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.suggest_items_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);

//        Cursor cursor = getActivity().getContentResolver().query(
//                SuggestsContract.CONTENT_URI, null, null, null, SuggestsContract.Columns.SEQUENCE_COL);
//
//
//        mSuggestAdapter = new AccountRecyclerViewAdapter(cursor,
//                (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//
//        recyclerView.setAdapter(mSuggestAdapter);
//        Log.d(TAG, "onCreateView: returning adapter count: " + mSuggestAdapter.getItemCount());

        createLoader();

        return view;

//        return inflater.inflate(R.layout.fragment_suggest_list, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void createLoader() {
        Cursor cursor = getActivity().getContentResolver().query(
                SuggestsContract.CONTENT_URI, null, null, null, SuggestsContract.Columns.SEQUENCE_COL);

        Log.d(TAG, "onCreateView: abt to init loader: ");

        if (mSuggestAdapter == null) {
//            CursorRecyclerViewAdapter.OnSuggestClickListener listener = (CursorRecyclerViewAdapter.OnSuggestClickListener) getActivity();

            mSuggestAdapter = new CursorRecyclerViewAdapter(cursor, this);

//            (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
        }
//        if (mSuggestAdapter == null) {
//            AccountRecyclerViewAdapter.OnAccountClickListener listener = (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity();
//
//            mSuggestAdapter = new AccountRecyclerViewAdapter(getContext(), cursor, listener);
//
////            (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        }
//            mAccountAdapter = new AccountRecyclerViewAdapter(getContext(), cursor,
//                    (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        } else {
//            mAccountAdapter.setListener((AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//        }
        if (mSuggestAdapter == null) {
            Log.d(TAG, "createLoader: mSuggestAdapter is null");
        }
        if (mRecyclerView == null) {
            Log.d(TAG, "createLoader: mRecyclerView is null");
        }
        mRecyclerView.setAdapter(mSuggestAdapter);

        getLoaderManager().initLoader(SUGGEST_LOADER_ID, null, this);
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
    List<Suggest> loadPasswords() {
        Log.d(TAG, "loadPasswords: starts ");
        Cursor cursor = getActivity().getContentResolver().query(
                SuggestsContract.CONTENT_URI, null, null, null, SuggestsContract.Columns.SEQUENCE_COL);

        List<Suggest> listSuggests = new ArrayList<Suggest>();
        if (cursor != null) {
            while(cursor.moveToNext()) {
//                Log.d(TAG, "loadPasswords: seq " + cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL))
//                        + ":" + cursor.getString(cursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)));
                Suggest item = new Suggest(
                        cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns._ID_COL)),
                        cursor.getString(cursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)),
                        cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));
                item.setNewSequence(cursor.getInt(cursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));
                listSuggests.add(item);
            }
            cursor.close();
        }

        return listSuggests;
    }

    @Override
    public void onSuggestUpClick(Suggest suggest) {
        Log.d(TAG, "onSuggestUpClick: ");
        List<Suggest> listSuggests = loadPasswords();
        int priorId = -1;
        int iLimit = listSuggests.size();
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() == item.getId()) {
                break;
            }
            priorId = item.getId();
        }
//        Log.d(TAG, "onSuggestDownClick: priorId " + priorId);

        int reseq = 0;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (priorId != item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
                if (suggest.getId() == item.getId()) {
                    break;
                }
            }
        }

//        Log.d(TAG, "onSuggestDownClick: reseq " + reseq);

        boolean found = false;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (priorId == item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
            } else {
                if (suggest.getId() == item.getId()) {
                    found = true;
                } else {
                    if (found) {
                        reseq++;
                        item.setNewSequence(reseq);
                    }
                }
            }
        }

        ContentResolver contentResolver = getActivity().getContentResolver();

        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (item.getSequence() != item.getNewSequence()) {
//                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
                ContentValues values = new ContentValues();
                values.put(SuggestsContract.Columns.SEQUENCE_COL, item.getNewSequence());
                contentResolver.update(SuggestsContract.buildIdUri(item.getId()), values, null, null);
            }
        }

        mSuggestAdapter.setSuggestSelectedPos(mSuggestAdapter.getSuggestSelectedPos() - 1);
//        suggestSelectedPos -= 1;

    }

    @Override
    public void onSuggestDownClick(Suggest suggest) {
        Log.d(TAG, "onSuggestDownClick: ");
        List<Suggest> listSuggests = loadPasswords();
        int nextId = -1;
        int iLimit = listSuggests.size();
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (nextId != -1) {
                nextId = item.getId();
                break;
            }
            if (suggest.getId() == item.getId()) {
                nextId = item.getId();
            }
        }
//        Log.d(TAG, "onSuggestDownClick: nextId " + nextId);

        int reseq = 0;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() != item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
                if (nextId == item.getId()) {
                    break;
                }
            }
        }

        boolean found = false;
        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (suggest.getId() == item.getId()) {
                reseq++;
                item.setNewSequence(reseq);
            } else {
                if (nextId == item.getId()) {
                    found = true;
                } else {
                    if (found) {
                        reseq++;
                        item.setNewSequence(reseq);
                    }
                }
            }
        }

        ContentResolver contentResolver =  getActivity().getContentResolver();

        for (int i = 0; i < iLimit; i++) {
            Suggest item = listSuggests.get(i);
            if (item.getSequence() != item.getNewSequence()) {
//                Log.d(TAG, "onSuggestDownClick: " + item.getSequence() + ":" + item.getNewSequence());
                ContentValues values = new ContentValues();
                values.put(SuggestsContract.Columns.SEQUENCE_COL, item.getNewSequence());
                contentResolver.update(SuggestsContract.buildIdUri(item.getId()), values, null, null);
            }
        }

        mSuggestAdapter.setSuggestSelectedPos(mSuggestAdapter.getSuggestSelectedPos() + 1);
//        suggestSelectedPos += 1;

    }

    @Override
    public void onSuggestDeleteClick(Suggest suggest) {
        Log.d(TAG, "onSuggestDeleteClick: ");
        getActivity().getContentResolver().delete(SuggestsContract.buildIdUri(suggest.getId()), null, null);
    }
}
