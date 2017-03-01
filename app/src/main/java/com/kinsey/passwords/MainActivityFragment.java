package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
//        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivityFragment";

    public static int LOADER_ID = 1;
    public static final int LOADER_SUGGESTS_ID = 0;
    public static final int LOADER_ACCOUNTS_ID = 1;

    public static final String BUNDLE_TABLE_ID = "TABLE_ID";

//    private CursorRecyclerViewAdapter mSuggestAdapter; // add adapter reference
//    private AccountRecyclerViewAdapter mAccountAdapter; // add adapter reference

    TextView twCurrentTitle;
//    Loader<Cursor> loader;

    Button mAccountsButton, mAddAccountButton, mSuggestsButton;
    private OnActionClicked mActionListener = null;

    interface OnActionClicked {
        void onAccountsClicked();
        void onAddAccountClicked();
        void onSuggestsClicked();
    }


    public MainActivityFragment() {
        Log.d(TAG, "MainActivityFragment: starts");
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts loader_id " + LOADER_ID);
        super.onActivityCreated(savedInstanceState);
//        getLoaderManager().initLoader(LOADER_ID, null, this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.items_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        return inflater.inflate(R.layout.fragment_main, container, false);

//        twCurrentTitle = (TextView) view.findViewById(R.id.current_title);


        Bundle bundle=getArguments();
        if (bundle == null) {
//            LOADER_ID = 1;
        } else {
            Log.d(TAG, "onCreateView: bundle " + bundle);
            String arg1 = bundle.getString(BUNDLE_TABLE_ID, "none");
            Log.d(TAG, "onCreateView: arg1 " + arg1);
            if (arg1 == "passport_detail") {
                LOADER_ID = 1;
            } else {
                LOADER_ID = 0;
            }
        }


        mAccountsButton = (Button) view.findViewById(R.id.home_btn_accounts);
        mAddAccountButton = (Button) view.findViewById(R.id.home_btn_add_account);
        mSuggestsButton = (Button) view.findViewById(R.id.home_btn_suggests);

        Log.d(TAG, "onCreateView: loader_id " + LOADER_ID);


        mAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onAccountsClicked();
            }
        });

        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onAddAccountClicked();
            }
        });

        mSuggestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onSuggestsClicked();
            }
        });


//        Log.d(TAG, "onCreateView: to cast");
//        Log.d(TAG, "onCreateView: activity " + getActivity());
//        Log.d(TAG, "onCreateView: activity listener " + (CursorRecyclerViewAdapter.OnSuggestClickListener) getActivity());

//        switch (LOADER_ID) {
//            case LOADER_SUGGESTS_ID:
//                mSuggestAdapter = new CursorRecyclerViewAdapter(null,
//                        (CursorRecyclerViewAdapter.OnSuggestClickListener) getActivity());
//                recyclerView.setAdapter(mSuggestAdapter);
//                Log.d(TAG, "onCreateView: returning adapter count: " + mSuggestAdapter.getItemCount());
//                break;
//            case LOADER_ACCOUNTS_ID:
//                Log.d(TAG, "onCreateView: " + getActivity());
//                Log.d(TAG, "onCreateView: " + (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//                mAccountAdapter = new AccountRecyclerViewAdapter(null,
//                        (AccountRecyclerViewAdapter.OnAccountClickListener) getActivity());
//                recyclerView.setAdapter(mAccountAdapter);
//                Log.d(TAG, "onCreateView: returning adapter count: " + mAccountAdapter.getItemCount());
//                break;
//            default:
//                throw new InvalidParameterException(TAG + ".onCreateView called with invalid loader id" + LOADER_ID);
//        }



        return view;
//        return inflater.inflate(R.layout.fragment_main, container, false);
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.d(TAG, "onCreateLoader: starts");
//
//        Log.d(TAG, "onCreateLoader: uri " + SuggestsContract.CONTENT_URI);
//        Log.d(TAG, "onCreateLoader: id " + String.valueOf(id));
//
//        String sortOrder = "";
//        switch (id) {
//            case LOADER_SUGGESTS_ID:
//                String[] projection =
//                        {SuggestsContract.Columns._ID_COL,
//                                SuggestsContract.Columns.PASSWORD_COL,
//                                SuggestsContract.Columns.SEQUENCE_COL};
////        , SuggestsContract.Columns.ACTVY_DATE_COL,
////                SuggestsContract.Columns.NOTE_COL};
//                // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
//                sortOrder = SuggestsContract.Columns.SEQUENCE_COL + "," + SuggestsContract.Columns.PASSWORD_COL + " COLLATE NOCASE";
////        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
//                return new CursorLoader(getActivity(),
//                        SuggestsContract.CONTENT_URI,
//                        projection,
//                        null,
//                        null,
//                        sortOrder);
//            case LOADER_ACCOUNTS_ID:
//                String[] projectionAcct =
//                        {AccountsContract.Columns._ID_COL,
//                                AccountsContract.Columns.CORP_NAME_COL,
//                                AccountsContract.Columns.USER_NAME_COL,
//                                AccountsContract.Columns.USER_EMAIL_COL,
//                                AccountsContract.Columns.CORP_WEBSITE_COL,
//                                AccountsContract.Columns.SEQUENCE_COL};
////        , SuggestsContract.Columns.ACTVY_DATE_COL,
////                SuggestsContract.Columns.NOTE_COL};
//                // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
//                sortOrder = AccountsContract.Columns.CORP_NAME_COL + "," + AccountsContract.Columns.SEQUENCE_COL + " COLLATE NOCASE";
////        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME;
//                return new CursorLoader(getActivity(),
//                        AccountsContract.CONTENT_URI,
//                        projectionAcct,
//                        null,
//                        null,
//                        sortOrder);
//            default:
//                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + id);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
////        Log.d(TAG, "onLoadFinished: starts");
//        this.loader = loader;
//        int count = 0;
//        switch (LOADER_ID) {
//            case LOADER_SUGGESTS_ID:
//                mSuggestAdapter.swapCursor(data);
//                count = mSuggestAdapter.getItemCount();
//                if (count == 0) {
//                    twCurrentTitle.setText("No generated passwords, check below");
//                } else {
//                    twCurrentTitle.setText("Generated passwords");
//                }
//                break;
//            case LOADER_ACCOUNTS_ID:
//                mAccountAdapter.swapCursor(data);
//                count = mAccountAdapter.getItemCount();
//                if (count == 0) {
//                    twCurrentTitle.setText("No accounts, + to add");
//                } else {
//                    twCurrentTitle.setText("Accounts");
//                }
//                break;
//            default:
//                throw new InvalidParameterException(TAG + ".onLoadFinished called with invalid loader id" + LOADER_ID);
//        }
////        if(data != null) {
////            while(data.moveToNext()) {
////                for(int i=0; i<data.getColumnCount(); i++) {
////                    Log.d(TAG, "onLoadFinished: " + data.getColumnName(i) + ": " + data.getString(i));
////                }
////                Log.d(TAG, "onLoadFinished: ==========================");
////            }
////            count = data.getCount();
////        }
//
//        Log.d(TAG, "onLoadFinished: count is " + count);
//    }
//
//    @Override
//    public void onDestroyView() {
//        Log.d(TAG, "onDestroyView: starts");
//        onLoaderReset(this.loader);
//        super.onDestroyView();
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        Log.d(TAG, "onLoaderReset: starts");
//        if (mSuggestAdapter != null) {
//            mSuggestAdapter.swapCursor(null);
//        }
//        if (mAccountAdapter != null) {
//            mAccountAdapter.swapCursor(null);
//        }
////        switch (LOADER_ID) {
////            case LOADER_SUGGESTS_ID:
////                mSuggestAdapter.swapCursor(null);
////                break;
////            case LOADER_ACCOUNTS_ID:
////                mAccountAdapter.swapCursor(null);
////                break;
////            default:
////                throw new InvalidParameterException(TAG + ".onLoaderReset called with invalid loader id" + LOADER_ID);
////        }
//    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activies containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof OnActionClicked)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement MainActivityFragment.OnActionClicked interface");
        }
        mActionListener = (OnActionClicked) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mActionListener = null;
    }

}
