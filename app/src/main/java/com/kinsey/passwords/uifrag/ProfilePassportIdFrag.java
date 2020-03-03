package com.kinsey.passwords.uifrag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.provider.RecyclerViewPositionHelper;

import java.util.ArrayList;
import java.util.List;

public class ProfilePassportIdFrag extends Fragment {

    public static final String TAG = "ProfileCorpNameFrag";

    Context context;
    RecyclerView recyclerView;
    ProfileViewModel profileViewModel;
    private List<Profile> profileListFull;
    private ProfileAdapter adapter = new ProfileAdapter(-1);
    RecyclerViewPositionHelper mRecyclerViewHelper;
    private final String RECYCLER_POSITION_KEY = "recyclerViewPos";
    private int mRecyclerViewPos = RecyclerView.NO_POSITION;

    private ProfilePassportIdFrag.OnProfilePassportIdClickListener mListener;
    public interface OnProfilePassportIdClickListener {

        void onProfilePassportIdSelect(int selectedId, Profile profile);

        void onDeleteConfirmPassportId(Profile profile, int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list_passport_id, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.account_items_list_passport_id);

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


//        boolean isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//        if (isLandscape) {
//            GridLayoutManager layoutManager;
//            layoutManager = new GridLayoutManager(getActivity(), 2);
//            recyclerView.setLayoutManager(layoutManager);
//        } else {
//            LinearLayoutManager layoutManager;
//            layoutManager = new LinearLayoutManager(getActivity());
//            recyclerView.setLayoutManager(layoutManager);
//        }


        recyclerView.setAdapter(adapter);
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getAllProfilesByPassportId().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);
                Log.d(TAG, "list submit");

                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount());
                    recyclerView.scrollToPosition(pos);
                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    recyclerView.scrollToPosition(0);
                }

            }
        });



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
                mListener.onDeleteConfirmPassportId(profile, viewHolder.getAdapterPosition());
            }

        }).attachToRecyclerView(recyclerView);


        recyclerView.scrollToPosition(0);
        this.adapter.notifyDataSetChanged();


        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int selectedId, Profile profile) {
                mListener.onProfilePassportIdSelect(selectedId, profile);
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


    private void refreshList() {
        if (adapter.getItemCount() > 0) {
            adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1);
        }
    }

    public void refreshListAll() {
        adapter.notifyDataSetChanged();
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

    public int getSelectedId() {
        return this.adapter.getSelectedId();
    }

    public void refreshListPos(int position) {
        adapter.notifyItemChanged(position);
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
//            case EDIT_PROFILE_REQUEST: {
//                int id = data.getIntExtra(AddEditProfileActivity.EXTRA_ID, -1);
//
//                if (id == -1) {
//                    Toast.makeText(context, "Profile can't be updated", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                int passportId = data.getIntExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, 0);
//                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
//                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
//                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
//                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
//                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);
//
//                Profile profile = new Profile(1, corpName, userName, userEmail, corpWebsite);
//                profile.setId(id);
//                profile.setPassportId(passportId);
//                profile.setNote(note);
//                profile.setOpenLong(data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0));
//                profile.setActvyLong(System.currentTimeMillis());
//
//                profileViewModel.update(profile);
//                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show();
//            }


            default:
                break;
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: len " + adapter.getItemCount());
        // Save RecyclerView state
        outState.putInt(RECYCLER_POSITION_KEY,  mRecyclerViewHelper.findFirstCompletelyVisibleItemPosition());

        super.onSaveInstanceState(outState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof ProfilePassportIdFrag.OnProfilePassportIdClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement ProfileOpenDateFrag interface");
        }
        mListener = (ProfilePassportIdFrag.OnProfilePassportIdClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
