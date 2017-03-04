package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {
    private static final String TAG = "SearchActivityFragment";

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

        mSearchValue = (TextView) view.findViewById(R.id.srch_value);
        mSearchRequestButton = (ImageButton) view.findViewById(R.id.srch_request_button);

        mSearchRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onSearchRequestClicked(mSearchValue.getText().toString());
            }
        });
        return view;
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
