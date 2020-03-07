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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.R;
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

    private ProfileAdapter adapter = new ProfileAdapter(-1);

    private final String RECYCLER_POSITION_KEY = "recyclerViewPos";
    private final String ARG_SELECTED_ID = "SELECTED_ID";
    private final String ARG_SELECTED_POS = "SELECTED_POS";
    private int mRecyclerViewPos = RecyclerView.NO_POSITION;
    private int selectedId = -1;
    int screenLayout;
    GridLayoutManager gridLayoutManager;
//    LinearLayoutManager layoutManager;

    private OnProfileCorpNameClickListener mListener;
    public interface OnProfileCorpNameClickListener {

        void onProfileCorpNameListSelect(int selectedId, Profile profile);

        void onProfileCorpNameAdd();

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


        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                this.selectedId = bundle.getInt(ARG_SELECTED_ID, -1);
            }
            Log.d(TAG, "onCreateView: init selectedId " + this.selectedId);
        } else {
            this.selectedId = savedInstanceState.getInt(ARG_SELECTED_ID, -1);
            Log.d(TAG, "onCreateView: selectedId " + adapter.getSelectedId());
        }

        screenLayout = getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        int spanSize = 3;
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
                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
                recyclerView.setLayoutManager(gridLayoutManager);
                Log.d(TAG, "screen size Xlarge");
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        spanSize = 3;
                    } else {
                        spanSize = 2;
                    }
                } else {
                    spanSize = 2;
                }
                gridLayoutManager = new GridLayoutManager(getActivity(), spanSize);
                recyclerView.setLayoutManager(gridLayoutManager);
                Log.d(TAG, "screen size large");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    } else {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        recyclerView.setLayoutManager(gridLayoutManager);
//                        layoutManager = new LinearLayoutManager(getActivity());
//                        recyclerView.setLayoutManager(layoutManager);
                    }
                    Log.d(TAG, "screen size normal");
                } else {
                    gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                    recyclerView.setLayoutManager(gridLayoutManager);
//                    layoutManager = new LinearLayoutManager(getActivity());
//                    recyclerView.setLayoutManager(layoutManager);
                }
                break;
            default:
                gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                recyclerView.setLayoutManager(gridLayoutManager);
//                layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(layoutManager);
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
                    Log.d(TAG, "onCreateView: len " + recyclerView.getChildCount() +
                            " pos " + pos);
                    recyclerView.scrollToPosition(pos);
//                    Log.d(TAG, "onCreateView: pos " + pos);
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
            public void onItemClick(int selectedId, Profile profile) {
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


        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                adapter.setSelectedId(bundle.getInt(ARG_SELECTED_ID, -1));
            }
        } else {
            adapter.setSelectedId(savedInstanceState.getInt(ARG_SELECTED_ID, -1));
            Log.d(TAG, "onCreateView: selectedId " + adapter.getSelectedId());
//            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_add_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        spanSize = 3;
                    } else {
                        spanSize = 2;
                    }
                } else {
                    spanSize = 2;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (selectedId == -1) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else {
                    spanSize = 1;
                }
                break;
            default:
                spanSize = 1;
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
        int pos = findIdPos(id);
        if (pos != -1) {
        }
    }

    private int findIdPos(int id) {
        int pos = 0;
        List<Profile> profiles = adapter.getCurrentList();
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

    public void refreshListAll() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

    public int getSelectedId() {
        return this.adapter.getSelectedId();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: len " + adapter.getItemCount() +
                " id " + adapter.getSelectedId());
        // Save RecyclerView state
        outState.putInt(RECYCLER_POSITION_KEY,  mRecyclerViewHelper.findFirstCompletelyVisibleItemPosition());

        outState.putInt(ARG_SELECTED_ID, this.adapter.getSelectedId());

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
                    + " must implement ProfileCustomFrag interface");
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
