package com.kinsey.passwords.uifrag;

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.provider.RecyclerViewPositionHelper;
import com.kinsey.passwords.provider.Task;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class ProfileCorpNameFrag extends Fragment {
    public static final String TAG = "ProfileCorpNameFrag";

    //    private final String ARG_SELECTED_ID = "SELECTED_ID";
//    private final String ARG_SELECTED_POS = "SELECTED_POS";
    public static final String EXTRA_LIST_SORTORDER =
            "com.kinsey.passwords.EXTRA_LIST_SORTORDER";
    public static final String EXTRA_SELECTED_ID =
            "com.kinsey.passwords.SELECTED_ID";
    public static final String EXTRA_MAX_SEQ =
            "com.kinsey.passwords.MAX_SEQ";


    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProfileViewModel profileViewModel;
    private TextView tvListTitle, tvSplInstr;
    private List<Profile> profileListFull;
    private int listsortOrder;

    private ProfileAdapter adapter = new ProfileAdapter(-1);

    private final String RECYCLER_POSITION_KEY = "recyclerViewPos";
    private int mRecyclerViewPos = RecyclerView.NO_POSITION;
    int screenLayout;
    GridLayoutManager gridLayoutManager;
//    LinearLayoutManager layoutManager;

    private OnProfileCorpNameClickListener mListener;

    public interface OnProfileCorpNameClickListener {

        void onProfileCorpNameListSelect(int selectedId, Profile profile);

        void onProfileCorpNameAdd();

//        void onShowWelcome();

        void onDeleteConfirmCorpName(Profile profile, int position);

        void onDeleteConfirmCustom(Profile profile, int position);

        void onEmptyWarning();

        void setMaxSeq(int maxSeq);
    }


    public static ProfileCorpNameFrag newInstance(int listsortOrder, int selectedId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LIST_SORTORDER, listsortOrder);
        bundle.putInt(EXTRA_SELECTED_ID, selectedId);
        ProfileCorpNameFrag fragment = new ProfileCorpNameFrag();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            this.listsortOrder = bundle.getInt(EXTRA_LIST_SORTORDER);
            adapter.setListsortOrder(this.listsortOrder);
            adapter.setSelectedId(bundle.getInt(EXTRA_SELECTED_ID));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        //        return super.onCreateView(inflater, container, savedInstanceState);

        mListener = (OnProfileCorpNameClickListener) getActivity();
        recyclerView = view.findViewById(R.id.account_items_list);
        tvListTitle = view.findViewById(R.id.list_title);
        tvSplInstr = view.findViewById(R.id.spl_instr);

        recyclerView.setHasFixedSize(true);

        if (savedInstanceState == null) {
            readBundle(getArguments());
            Log.d(TAG, "onCreateView: init selectedId " + adapter.getSelectedId());
        } else {
            this.listsortOrder = savedInstanceState.getInt(EXTRA_LIST_SORTORDER, -1);
            adapter.setListsortOrder(this.listsortOrder);
            adapter.setSelectedId(savedInstanceState.getInt(EXTRA_SELECTED_ID, -1));
            Log.d(TAG, "onCreateView: selectedId " + adapter.getSelectedId());
        }

//        screenLayout = getResources().getConfiguration().screenLayout;
//        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        setGrid(adapter.getSelectedId());
//        int spanSize = 3;
//        switch (screenLayout) {
//            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    if (adapter.getSelectedId() == -1) {
//                        spanSize = 4;
//                    } else {
//                        spanSize = 3;
//                    }
//                } else {
//                    spanSize = 3;
//                }
//                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
//                recyclerView.setLayoutManager(gridLayoutManager);
//                Log.d(TAG, "screen size Xlarge");
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    if (adapter.getSelectedId() == -1) {
//                        spanSize = 3;
//                    } else {
//                        spanSize = 2;
//                    }
//                } else {
//                    spanSize = 2;
//                }
//                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
//                recyclerView.setLayoutManager(gridLayoutManager);
//                Log.d(TAG, "screen size large");
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    if (adapter.getSelectedId() == -1) {
//                        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//                        recyclerView.setLayoutManager(gridLayoutManager);
//                    } else {
//                        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//                        recyclerView.setLayoutManager(gridLayoutManager);
////                        layoutManager = new LinearLayoutManager(getActivity());
////                        recyclerView.setLayoutManager(layoutManager);
//                    }
//                    Log.d(TAG, "screen size normal");
//                } else {
//                    gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//                    recyclerView.setLayoutManager(gridLayoutManager);
////                    layoutManager = new LinearLayoutManager(getActivity());
////                    recyclerView.setLayoutManager(layoutManager);
//                }
//                break;
//            default:
//                gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//                recyclerView.setLayoutManager(gridLayoutManager);
////                layoutManager = new LinearLayoutManager(getActivity());
////                recyclerView.setLayoutManager(layoutManager);
//        }


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


        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
//        MainActivity.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        Log.d(TAG, "onCreateView: listsortOrder " + listsortOrder);
        switch (listsortOrder) {
            case MainActivity.LISTSORT_CORP_NAME:
                sortDataByCorpName(savedInstanceState);
                break;
            case MainActivity.LISTSORT_PASSPORT_ID:
                sortDataByPassortId(savedInstanceState);
                break;
            case MainActivity.LISTSORT_OPEN_DATE:
                sortDataByOpenDate(savedInstanceState);
                break;
//            case MainActivity.LISTSORT_CUSTOM_SORT:
//                sortDataByCustomSort(savedInstanceState);
//                break;
            default:
                sortDataByCorpName(savedInstanceState);
                break;
        }


        profileViewModel.getMaxSequence().observe(getActivity(), new Observer<Profile>() {
            @Override
            public void onChanged(@Nullable Profile profile) {

                int currentMaxSeq;
                if (profile == null) {
                    currentMaxSeq = 0;
                } else {
                    currentMaxSeq = profile.getSequence();
                }
                mListener.setMaxSeq(currentMaxSeq);
            }
        });


//        tvListTitle.setText(MainActivity.profileViewModel.dbMsg);

        if (listsortOrder == MainActivity.LISTSORT_CUSTOM_SORT) {
            customSeqTouchHelper();
        } else {
            regularTouchHelper();
        }

        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int selectedId, Profile profile) {
                Log.d(TAG, "onItemClick: selectedId " + adapter.getSelectedId() + ":" + selectedId);
                if (selectedId == -1) {
                    unselectId(adapter.getSelectedId());
                    adapter.setSelectedId(-1);
                    refreshListAll();
                }
                mListener.onProfileCorpNameListSelect(selectedId, profile);
                setGrid(selectedId);
//                selectedId = profile.getPassportId();
//                adapter.notifyDataSetChanged();
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_add_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: selectedId " + adapter.getSelectedId());
                unselectId(adapter.getSelectedId());
                adapter.setSelectedId(-1);
                mListener.onProfileCorpNameAdd();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


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


        return view;

    }

    private void regularTouchHelper() {
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
    }

    private void customSeqTouchHelper() {


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
                mListener.onDeleteConfirmCustom(profile, viewHolder.getAdapterPosition());
//                confirmDeleteProfile(profile);

                //                profileViewModel.delete(adapter.getProfileAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Profile deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {

//                if (listsortOrder != LISTSORT_CUSTOM_SORT) {
//                    return;
//                }

                if (toViewHolder != null) {
                    toViewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.primaryDarkColor)
                    );
                }

                target.itemView.setBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.primaryTextColor)
                );

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
                                ContextCompat.getColor(context, R.color.backgroundTransparent)
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


                    if (modifyProfileList != null) {
                        for (Profile item : modifyProfileList) {
                            profileViewModel.update(item);
                        }
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
//                    int scrollPos = lowPos;
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


    }

    private void sortDataByCorpName(Bundle savedInstanceState) {
        tvListTitle.setText(getString(R.string.acctlist_by_corp_name));
//        tvSplInstr.setVisibility(View.GONE);
        tvSplInstr.setText(R.string.splInstr2);
        profileViewModel.getAllProfilesByCorpName().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);

                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount() +
                            " pos " + pos);
                    recyclerView.scrollToPosition(pos);
//                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    Log.d(TAG, "onCreateView: getItemCount " + adapter.getItemCount());
                    if (adapter.getItemCount() == 0) {
//                        mListener.onProfileCorpNameAdd();
//                        mListener.onShowWelcome();
                        mListener.onEmptyWarning();
//                    } else {
                        if (adapter.getSelectedId() == -1) {
                            recyclerView.scrollToPosition(0);
                        } else {
                            setSelectedId(adapter.getSelectedId());
                        }
                    }
                }
            }


        });

    }


    private void sortDataByPassortId(Bundle savedInstanceState) {
        tvListTitle.setText(getString(R.string.acctlist_by_account_id));
//        tvSplInstr.setVisibility(View.GONE);
        tvSplInstr.setText(R.string.splInstr2);
        profileViewModel.getAllProfilesByPassportId().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);

                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount() +
                            " pos " + pos);
                    recyclerView.scrollToPosition(pos);
//                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    Log.d(TAG, "onCreateView: getItemCount " + adapter.getItemCount());
                    if (adapter.getItemCount() == 0) {
                        mListener.onEmptyWarning();
                    } else {
                        if (adapter.getSelectedId() == -1) {
                            recyclerView.scrollToPosition(0);
                        } else {
                            setSelectedId(adapter.getSelectedId());
                        }
                    }
                }
            }


        });

    }

    private void sortDataByOpenDate(Bundle savedInstanceState) {
        tvListTitle.setText(getString(R.string.acctlist_by_open_date_descending));
//        tvSplInstr.setVisibility(View.GONE);
        tvSplInstr.setText(R.string.splInstr2);
        profileViewModel.getAllProfilesByOpenDate().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);

                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount() +
                            " pos " + pos);
                    recyclerView.scrollToPosition(pos);
//                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    Log.d(TAG, "onCreateView: getItemCount " + adapter.getItemCount());
                    if (adapter.getItemCount() == 0) {
                        mListener.onEmptyWarning();
                    } else {
                        if (adapter.getSelectedId() == -1) {
                            recyclerView.scrollToPosition(0);
                        } else {
                            setSelectedId(adapter.getSelectedId());
                        }
                    }
                }
            }


        });

    }


    private void sortDataByCustomSort(Bundle savedInstanceState) {
        tvListTitle.setText(getString(R.string.acctlist_by_custom));
//        tvSplInstr.setVisibility(View.VISIBLE);
        tvSplInstr.setText(R.string.splInstr);
        profileViewModel.getAllProfilesCustomSort().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);

                if (savedInstanceState != null) {
                    int pos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount() +
                            " pos " + pos);
                    recyclerView.scrollToPosition(pos);
//                    Log.d(TAG, "onCreateView: pos " + pos);
                } else {
                    Log.d(TAG, "onCreateView: getItemCount " + adapter.getItemCount());
                    if (adapter.getItemCount() == 0) {
                        mListener.onEmptyWarning();
                    } else {
                        if (adapter.getSelectedId() == -1) {
                            recyclerView.scrollToPosition(0);
                        } else {
                            setSelectedId(adapter.getSelectedId());
                        }
                    }
                }
            }


        });

    }


    private void setGrid(int selectedId) {
        int spanSize;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        spanSize = 4;
                    } else {
                        spanSize = 3;
                    }
                } else {
                    spanSize = 3;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        if (this.listsortOrder == MainActivity.LISTSORT_OPEN_DATE) {
                            spanSize = 3;
                        } else {
                            spanSize = 4;
                        }
                    } else {
                        spanSize = 2;
                    }
                } else {
                    spanSize = 2;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Log.d(TAG, "setGrid: SCREENLAYOUT_SIZE_NORMAL");
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        if (this.listsortOrder == MainActivity.LISTSORT_OPEN_DATE) {
                            spanSize = 2;
                        } else {
                            spanSize = 3;
                        }
                    } else {
                        spanSize = 2;
                    }
                } else {
                    spanSize = 2;
                }
                break;
            default:
                Log.d(TAG, "setGrid: defaults");
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spanSize = 3;
                    Log.d(TAG, "setGrid: landscape");
                } else {
                    spanSize = 1;
                    Log.d(TAG, "setGrid: portrait");
                }
        }

        Log.d(TAG, "setGrid: spanSize " + spanSize);
        gridLayoutManager.setSpanCount(spanSize);

    }


    public void setSelectedId(int id) {
        Log.d(TAG, "setSelectedId: id " + id);
        adapter.setSelectedId(id);
        if (id == -1) {
            recyclerView.scrollToPosition(0);
        }
        int pos = findIdPos(id);
        Log.d(TAG, "setSelectedId: id/pos " + id + "/" + pos);
        if (pos != -1) {
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(pos);
            Log.d(TAG, "setSelectedId: pos " + pos);
        }
    }


    public void selectById(int id) {
        adapter.setSelectedId(id);
        int pos = findIdPos(id);
        if (pos != -1) {
            adapter.notifyItemInserted(pos);
            recyclerView.scrollToPosition(pos);
        }
    }

    public void unselectId(int id) {
        Log.d(TAG, "unselectId: id " + id);
        int pos = findIdPos(id);
        if (pos != -1) {
            Log.d(TAG, "unselectId: id/pos " + id + "/" + pos);
            adapter.notifyItemChanged(pos);
        }
    }

    private int findIdPos(int id) {
        int pos = 0;
        List<Profile> profiles = adapter.getCurrentList();
        Log.d(TAG, "findIdPos: list len " + adapter.getItemCount());
        for (Profile item : profiles) {
            if (item.getPassportId() == id) {
//                adapter.notifyItemChanged(pos);
                return pos;
            }
            pos += 1;
        }
        return -1;
    }


    private void refreshList() {
        if (adapter.getItemCount() > 0) {
            adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1);
        }
    }


    public void deleteProfileViewModelItem(Profile profile) {
        profileViewModel.delete(profile);
    }


    public void updateProfileViewModelItem(Profile profile) {
        profileViewModel.update(profile);
    }


    public void insertProfileViewModelItem(Profile profile, Task task) {
        profileViewModel.insertProfile(profile, task);
    }


    public void deleteFromList(int profileId) {
        List<Profile> profiles = adapter.getCurrentList();
        for (Profile item : profiles) {
            if (item.getPassportId() == profileId) {
                profileViewModel.delete(item);
                break;
            }
        }
        refreshList();
    }

    public void refreshListPos(int position) {
        adapter.notifyItemChanged(position);
    }

    public void refreshListAll() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        context = getContext();
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == MainActivity.RESULT_CANCELED) {
//            return;
//        }
//
//    }

    public int getSelectedId() {
        return this.adapter.getSelectedId();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: len " + adapter.getItemCount() +
                " id " + adapter.getSelectedId());
        // Save RecyclerView state

        if (mRecyclerViewHelper == null) {
            outState.putInt(RECYCLER_POSITION_KEY, 0);
        } else {
            outState.putInt(RECYCLER_POSITION_KEY, mRecyclerViewHelper.findFirstCompletelyVisibleItemPosition());
        }

        outState.putInt(EXTRA_LIST_SORTORDER, this.listsortOrder);
        outState.putInt(EXTRA_SELECTED_ID, this.adapter.getSelectedId());

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
        if (!(activity instanceof OnProfileCorpNameClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement ProfileCorpNameFrag interface");
        }
        mListener = (OnProfileCorpNameClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
