package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.RecyclerViewPositionHelper;

import java.util.ArrayList;
import java.util.List;

public class ProfileCorpNameFrag extends Fragment {

    public static final String TAG = "ProfileCorpNameFrag";

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    private RecyclerView.LayoutManager mLayoutManager;
//    ProfileViewModel profileViewModel;
    private TextView tvListTitle;
    private List<Profile> profileListFull;
    private ProfileAdapter adapter = new ProfileAdapter();

    private final String RECYCLER_POSITION_KEY = "recyclerViewPos";
    private int mRecyclerViewPos = RecyclerView.NO_POSITION;

    private ProfileCorpNameFrag.OnProfileCorpNameClickListener mListener;
    public interface OnProfileCorpNameClickListener {

        void onProfileCorpNameListSelect(Profile profile);

        void onDeleteConfirmCorpName(Profile profile, int position);

        void onEmptyWarning();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);


        recyclerView = view.findViewById(R.id.account_items_list);
        tvListTitle = view.findViewById(R.id.list_title);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager;
        LinearLayoutManager layoutManager;
        int screenLayout = getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        int spanSize = 3;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spanSize = 4;
                } else {
                    spanSize = 3;
                }
                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
                recyclerView.setLayoutManager(gridLayoutManager);
                Log.d(TAG, "screen size Xlarge");
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spanSize = 3;
                } else {
                    spanSize = 2;
                }
                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
                recyclerView.setLayoutManager(gridLayoutManager);
                Log.d(TAG, "screen size large");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    Log.d(TAG, "screen size normal");
                } else {
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                }
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
        }


//        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
//            GridLayoutManager layoutManager;
//            layoutManager = new GridLayoutManager(getActivity(), 4);
//            recyclerView.setLayoutManager(layoutManager);
//            Log.d(TAG, "screen size Xlarge");
//        } else {
////        boolean isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                GridLayoutManager layoutManager;
//                layoutManager = new GridLayoutManager(getActivity(), 2);
//                recyclerView.setLayoutManager(layoutManager);
//            } else {
//                LinearLayoutManager layoutManager;
//                layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(layoutManager);
//            }
//        }


        recyclerView.setAdapter(adapter);
        mLayoutManager = recyclerView.getLayoutManager();
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);

//        MainActivity.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        MainActivity.profileViewModel.getAllProfilesByCorpName().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);


                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount());
                    recyclerView.scrollToPosition(pos);
                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    Log.d(TAG, "onCreateView: getItemCount " +  adapter.getItemCount());
                    if (adapter.getItemCount() == 0) {
                            mListener.onEmptyWarning();
                    } else {
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });

//        tvListTitle.setText(MainActivity.profileViewModel.dbMsg);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Profile profile = adapter.getProfileAt(viewHolder.getAdapterPosition());
                mListener.onDeleteConfirmCorpName(profile, viewHolder.getAdapterPosition());
            }

        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                mListener.onProfileCorpNameListSelect(profile);
//                Intent intent = new Intent(context, AddEditProfileActivity.class);
//                intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
//                intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
//                intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
//                intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
//
//                Log.d(TAG, "edit requested");
//                startActivityForResult(intent, EDIT_PROFILE_REQUEST);

            }
        });


        adapter.notifyDataSetChanged();



//        new CountDownTimer(30000, 100) {
//
//            public void onTick(long millisUntilFinished) {
//                tvListTitle.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//                tvListTitle.setText("done!");
////                progressBar.setVisibility(View.GONE);
//            }
//        }.start();



        Log.d(TAG, "view set");
        return view;

    }

    private void refreshList() {
        if (adapter.getItemCount() > 0) {
            adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1);
        }
    }


    public void deleteFromList(int profileId) {
        List<Profile> profiles = adapter.getCurrentList();
        for (Profile item : profiles) {
            if (item.getPassportId() == profileId) {
                MainActivity.profileViewModel.delete(item);
                break;
            }
        }
        refreshList();
    }

    public void refreshListPos(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.RESULT_CANCELED) {
            return;
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: len " + adapter.getItemCount());
        // Save RecyclerView state
        outState.putInt(RECYCLER_POSITION_KEY,  mRecyclerViewHelper.findFirstCompletelyVisibleItemPosition());

        super.onSaveInstanceState(outState);

//        int pos = spinner.getSelectedItemPosition();
//        outState.putInt("spinnerPos", pos);
//        pos = mRecyclerViewHelper.findFirstVisibleItemPosition();
//        outState.putInt("recyclerPos", pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof ProfileCorpNameFrag.OnProfileCorpNameClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement ProfileCustomFrag interface");
        }
        mListener = (ProfileCorpNameFrag.OnProfileCorpNameClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
