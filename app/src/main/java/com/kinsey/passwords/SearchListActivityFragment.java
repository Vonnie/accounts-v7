package com.kinsey.passwords;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kinsey.passwords.items.SearchesContract;
import com.kinsey.passwords.provider.SearchRecyclerViewAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListActivityFragment extends Fragment {
    private static final String TAG = "SearchListActivityFragm";

    private SearchRecyclerViewAdapter mSearchListAdapter; // add adapter reference
    
    public SearchListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.srchlst_items_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Bundle arguments = getActivity().getIntent().getExtras();

        Log.d(TAG, "onCreateView: search adt cursor");
        
        mSearchListAdapter = new SearchRecyclerViewAdapter(SearchesContract.cursorSearch,
                (SearchRecyclerViewAdapter.OnAccountClickListener) getActivity());
        recyclerView.setAdapter(mSearchListAdapter);
        return view;
    }
}
