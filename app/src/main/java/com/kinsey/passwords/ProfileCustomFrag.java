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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class ProfileCustomFrag extends Fragment {
    public static final String TAG = "ProfileCustomFrag";

    public static final int ADD_PROFILE_REQUEST = 1;
    public static final int EDIT_PROFILE_REQUEST = 2;

    Context context;
    RecyclerView recyclerView;
    ProfileViewModel profileViewModel;
    private List<Profile> profileListFull;
    private ProfileAdapter adapter = new ProfileAdapter();

    private ProfileCustomFrag.OnProfileCustomClickListener mListener;
    public interface OnProfileCustomClickListener {

        void onProfileCustomListSelect(Profile profile);

        void onDeleteConfirmCustom(Profile profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list_custom, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.account_items_list_custom);

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
        profileViewModel.getAllProfilesCustomSort().observe(this, new Observer<List<Profile>>() {
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
                mListener.onProfileCustomListSelect(profile);
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



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END) {

            RecyclerView.ViewHolder fromViewHolder;
            RecyclerView.ViewHolder toViewHolder;
            int maxPos = 0, minPos = 0;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

//                Log.d(TAG, "onMove");
//                Profile profile = adapter.getProfileAt(viewHolder.getAdapterPosition());
//                profileViewModel.delete(adapter.getProfileAt(viewHolder.getAdapterPosition()));
//                profileViewModel.insertProfile(profile);
//                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

//                if (listsortOrder != LISTSORT_CUSTOM_SORT) {
//                    return false;
//                }

                if (target.getAdapterPosition() < minPos) {
                    minPos = target.getAdapterPosition();
                }
                if (viewHolder.getAdapterPosition() < minPos) {
                    minPos = viewHolder.getAdapterPosition();
                }

                if (target.getAdapterPosition() > maxPos) {
                    maxPos = target.getAdapterPosition();
                }
                if (viewHolder.getAdapterPosition() > maxPos) {
                    maxPos = viewHolder.getAdapterPosition();
                }

                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Profile profile = adapter.getProfileAt(viewHolder.getAdapterPosition());
                mListener.onDeleteConfirmCustom(profile);
//                confirmDeleteProfile(profile);

                //                profileViewModel.delete(adapter.getProfileAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Profile deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {

//                if (listsortOrder != LISTSORT_CUSTOM_SORT) {
//                    return;
//                }

//                if (toViewHolder != null) {
//                    toViewHolder.itemView.setBackgroundColor(
//                            ContextCompat.getColor(getApplicationContext(), R.color.primaryDarkColor)
//                    );
//                }

//                target.itemView.setBackgroundColor(
//                        ContextCompat.getColor(getApplicationContext(), R.color.secondaryDarkColor)
//                );

                toViewHolder = target;


                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }


            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

//                if (listsortOrder != LISTSORT_CUSTOM_SORT) {
//                    return;
//                }

                if (actionState == ACTION_STATE_DRAG) {

                    viewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.secondaryLightColor)
                    );
                    fromViewHolder = viewHolder;
                    minPos = adapter.getItemCount();
                    maxPos = 0;

                    if (viewHolder.getAdapterPosition() < minPos) {
                        minPos = viewHolder.getAdapterPosition();
                    }

                    if (viewHolder.getAdapterPosition() > maxPos) {
                        maxPos = viewHolder.getAdapterPosition();
                    }

                } else {
                    Log.d(TAG, "state " + actionState);
                    if (fromViewHolder == null) {
                        return;
                    }
                    if (toViewHolder == null) {
                        return;
                    }
                    fromViewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.backgroundTransparent)
                    );
                    if (toViewHolder != null) {
                        toViewHolder.itemView.setBackgroundColor(
                                ContextCompat.getColor(context, R.color.primaryDarkColor)
                        );
                    }
                    int fromPos = fromViewHolder.getAdapterPosition();
                    int toPos = toViewHolder.getAdapterPosition();
                    if (fromPos == toPos) {
                        return;
                    }

                    Log.d(TAG, "from:to " + fromPos + ":" + toPos);

                    Profile reposProfile = adapter.getProfileAt(fromViewHolder.getAdapterPosition());

                    int lowPos = fromPos < toPos ? fromPos : toPos;
                    int highPos = fromPos > toPos ? fromPos : toPos;
                    Log.d(TAG, "low:high " + lowPos + ":" + highPos);

                    Log.d(TAG, "repos " + reposProfile.getCorpName());

                    int nextSeq = -1;
                    if (lowPos == 0) {
                        nextSeq = 1;
                    } else {
                        Profile profileNext = adapter.getProfileAt(lowPos);
                        nextSeq = profileNext.getSequence();
                    }

                    List<Profile> modifyProfileList = new ArrayList<Profile>();
                    int currentPos = lowPos;

                    if (currentPos == toPos) {
                        reposProfile.setSequence(nextSeq);
                        modifyProfileList.add(reposProfile);
                        nextSeq += 1;
                        while (currentPos < highPos) {
                            Profile profileSeq = adapter.getProfileAt(currentPos);
                            profileSeq.setSequence(nextSeq);
                            modifyProfileList.add(profileSeq);
                            currentPos += 1;
                            nextSeq += 1;
                        }
                    } else {
                        currentPos += 1;
                        while (currentPos < highPos) {
                            Profile profileSeq = adapter.getProfileAt(currentPos);
                            profileSeq.setSequence(nextSeq);
                            modifyProfileList.add(profileSeq);
                            nextSeq += 1;
                            currentPos += 1;
                        }
                        reposProfile.setSequence(nextSeq);
                        modifyProfileList.add(reposProfile);
                    }


                    for (Profile item : modifyProfileList) {
                        profileViewModel.update(item);
                    }


//                    int fromPos2 = fromViewHolder.getLayoutPosition();
//                    int toPos2 = toViewHolder.getLayoutPosition();
//                    Log.d(TAG, "fromPos2 " + fromPos2 + ":" + toPos2);


//                    currentPos = lowPos;
//                    while (currentPos < highPos) {
//                        Profile profileSeq = adapterCustom.getProfileAt(currentPos);
//                    }

                    try {
                        int endCount = highPos - lowPos + 3;
                        endCount = endCount > adapter.getItemCount() - 1 ? adapter.getItemCount() - 1 : endCount;
                        adapter.notifyItemRangeChanged(lowPos, endCount);
                    } catch (Exception e) {
                        Log.d(TAG, "binding error" + fromPos + ":" + toPos);
                    }


//                    adapterCustom.notifyDataSetChanged();

//                    adapterCustom.n
                    int scrollPos = lowPos;
//                    if (lowPos > 3) {
//                        scrollPos = lowPos - 1;
//                    }
                    recyclerView.scrollToPosition(toPos);
//                        suggest.setSequence(toSeq);
//                        suggestTarget.setSequence(fromSeq);
//                        Log.d(TAG, "onMovePos " + fromViewHolder.getAdapterPosition() + ":" + toViewHolder.getAdapterPosition());
//                        Log.d(TAG, "onMovePswd " + suggest.getPassword() + ":" + suggestTarget.getPassword());
//                        Log.d(TAG, "onMoveId " + suggest.getId() + ":" + suggestTarget.getId());
//                        Log.d(TAG, "onMoveSeq " + suggest.getSequence() + ":" + suggestTarget.getSequence());
//                        Log.d(TAG, "notifyPos " + fromPos + ":" + toPos);
//
//                        suggestViewModel.update(suggest);
//                        suggestViewModel.update(suggestTarget);

//                        Log.d(TAG, "min:max " + minPos + ":" + maxPos);

//                        adapter.notifyDataSetChanged();


                    fromViewHolder = null;
                }


                super.onSelectedChanged(viewHolder, actionState);
            }
        }).attachToRecyclerView(recyclerView);


        Log.d(TAG, "view set");
        return view;
    }


    public void resequenceList() {
        int newSeq = 0;
        Log.d(TAG, "profile size " + profileListFull.size());
        for ( Profile item : profileListFull) {
            newSeq += 1;
            item.setSequence(newSeq);
            profileViewModel.update(item);
        }
        adapter.notifyItemRangeChanged(0, profileListFull.size() - 1);
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
        if (!(activity instanceof ProfileCustomFrag.OnProfileCustomClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement ProfileCustomFrag interface");
        }
        mListener = (ProfileCustomFrag.OnProfileCustomClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }


}
