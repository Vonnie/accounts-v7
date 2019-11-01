package com.kinsey.passwords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.kinsey.passwords.provider.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileCorpNameFrag extends Fragment {

    public static final String TAG = "ProfileCorpNameFrag";

    Context context;
    RecyclerView recyclerView;
//    ProfileViewModel profileViewModel;
    TextView tvListTitle;
    private List<Profile> profileListFull;
    private ProfileAdapter adapter = new ProfileAdapter();

    private ProfileCorpNameFrag.OnProfileCorpNameClickListener mListener;
    public interface OnProfileCorpNameClickListener {

        void onProfileCorpNameListSelect(Profile profile);

        void onDeleteConfirmCorpName(Profile profile, int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.account_items_list);
        tvListTitle = view.findViewById(R.id.list_title);


        recyclerView.setHasFixedSize(true);

        boolean isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if (isLandscape) {
            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }


        recyclerView.setAdapter(adapter);

//        MainActivity.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        MainActivity.profileViewModel.getAllProfilesByCorpName().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);
                Log.d(TAG, "list submit");
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


        recyclerView.scrollToPosition(0);
        adapter.notifyDataSetChanged();



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
