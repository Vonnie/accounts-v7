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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileOpenDateFrag extends Fragment {

    public static final String TAG = "ProfileCorpNameFrag";

    public static final int ADD_PROFILE_REQUEST = 1;
    public static final int EDIT_PROFILE_REQUEST = 2;

    Context context;
    RecyclerView recyclerView;
    ProfileViewModel profileViewModel;
    private List<Profile> profileListFull;
    private ProfileAdapter adapter = new ProfileAdapter();

    private ProfileOpenDateFrag.OnProfileOpenDateClickListener mListener;
    public interface OnProfileOpenDateClickListener {

        void onProfileOpenDateListSelect(Profile profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list_opendate, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.account_items_list_opendate);

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

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getAllProfilesByOpenDate().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);
                Log.d(TAG, "list submit");
            }
        });

        recyclerView.scrollToPosition(0);
        this.adapter.notifyDataSetChanged();


        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                mListener.onProfileOpenDateListSelect(profile);
//                Intent intent = new Intent(context, AddEditProfileActivity.class);
//                intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
//                intent.putExtra(AddEditProfileActivity.EXTRA_USER_EMAIL, profile.getUserEmail());
//                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
//                intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
//                intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
//                intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
//
//                Log.d(TAG, "edit requested");
//                startActivityForResult(intent, EDIT_PROFILE_REQUEST);

            }
        });


        Log.d(TAG, "view set");
        return view;

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
        switch (requestCode) {
//            case ADD_PROFILE_REQUEST: {
//                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
//                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
//                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
//                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
//                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);
//
//                Profile profile = new Profile(this.adapter.getItemCount() + 1,
//                        corpName, userName, userEmail, corpWebsite);
//                profile.setNote(note);
//                profile.setOpenLong(data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0));
//                profile.setActvyLong(System.currentTimeMillis());
//
//                profileViewModel.insertProfile(profile);
//
//
//                Log.d(TAG, "profile added");
//                Toast.makeText(context, "Profile added", Toast.LENGTH_SHORT).show();
//            }
            case EDIT_PROFILE_REQUEST: {
                int id = data.getIntExtra(AddEditProfileActivity.EXTRA_ID, -1);

                if (id == -1) {
                    Toast.makeText(context, "Profile can't be updated", Toast.LENGTH_SHORT).show();
                    return;
                }

                int passportId = data.getIntExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, 0);
                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);

                Profile profile = new Profile(1, corpName, userName, userEmail, corpWebsite);
                profile.setId(id);
                profile.setPassportId(passportId);
                profile.setNote(note);
                profile.setOpenLong(data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0));
                profile.setActvyLong(System.currentTimeMillis());

                profileViewModel.update(profile);
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show();
            }


            default:
                break;
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof ProfileOpenDateFrag.OnProfileOpenDateClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement ProfileOpenDateFrag interface");
        }
        mListener = (ProfileOpenDateFrag.OnProfileOpenDateClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}