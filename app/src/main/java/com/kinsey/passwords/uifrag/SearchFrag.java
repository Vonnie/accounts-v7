package com.kinsey.passwords.uifrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.provider.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFrag extends Fragment {
    private static final String TAG = "SearchFrag";

    private List<Profile> profileListFull;
    private SearchAdapter adapter;
    private List<String> names;
    private String searchforValue = "";
    private AutoCompleteTextView editTextFilledExposedDropdown;
    private ProfileViewModel profileViewModel;

    private OnSearchClickListener mListener;
    public interface OnSearchClickListener {
        void showSearchSelected(int selectedId, Profile profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_account_search, container, false);


        editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
//        AppCompatAutoCompleteTextView editTextFilledExposedDropdown =
//                view.findViewById(R.id.filled_exposed_dropdown);
//        TextView tvAcctId = (TextView) view.findViewById(R.id.acct_id);
//        TextView tvCorpName = view.findViewById(R.id.corp_name);
//        TextView tvUserName = view.findViewById(R.id.user_name);


//        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
//
//        this.adapter = new SearchAdapter();
//        recyclerView.setAdapter(adapter);

//        textInputSearchCorpName.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                Log.d(TAG, "profiles search " + s);
//                if (s.length() >= 3) {
//                    searchResults(s.toString());
//                }
//            }
//        });

//        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Profile profile) {
//                Log.d(TAG, "onItemClick: " + profile.getCorpName());
////                Intent intent = new Intent(SearchActivity.this, AddEditProfileActivity.class);
////                intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
////                intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
////                intent.putExtra(AddEditProfileActivity.EXTRA_SEQUENCE, profile.getSequence());
////                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
////                intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
////                intent.putExtra(AddEditProfileActivity.EXTRA_USER_EMAIL, profile.getUserEmail());
////                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
////                intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
////                intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
////                intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());
////
////                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
//
//            }
//        });


//        if (savedInstanceState != null) {
//            this.searchforValue = savedInstanceState.getString("searchfor");
//            textInputSearchCorpName.getEditText().setText(this.searchforValue);
//        }

//        adapter = new ProfileAdapter(-1);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        profileViewModel.getAllProfilesByCorpName().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                List<Profile> profileListFull = new ArrayList<>(profiles);
                names = new ArrayList<String>();
                for (Profile profileItem : profileListFull) {
                    String idName = profileItem.getCorpName() + " [ Id " +
                            profileItem.getPassportId() + " ]";
                    names.add(idName);
                }
//                fruitAdapter = new SearchAdapter(this, R.layout.profile_item, profiles);
//                ListItemAdapter adapter = new ListItemAdapter(items, this);
                ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, names);
//                android.R.layout.simple_list_item_1, names);
//                ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
//                        R.layout.dropdown_menu_popup_item, names);

                editTextFilledExposedDropdown.setAdapter(adapter2);
//                editTextFilledExposedDropdown.setText(adapter2.getItem(0).toString());

                editTextFilledExposedDropdown.setThreshold(3);


                editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String selectedName = adapter2.getItem(position).toString();
                        Log.d(TAG, "onItemClick: selName " + selectedName);
                        Log.d(TAG, "onItemClick: pos:id " + position + " : " + id);
                        Log.d(TAG, "onItemClick: id " + adapter2.getItem(position).toString());
                        Log.d(TAG, "onItemClick: adt " + profiles.get(position).getPassportId());
                        Log.d(TAG, "onItemClick: adt " + profiles.get(position).getCorpName());

//                        Toast.makeText(MainActivity.this,
//                                adapter.getItem(position).toString(),
//                                Toast.LENGTH_SHORT).show();

//                        if (tvAcctId == null) {
//                            Log.d(TAG, "onItemClick: tvAcctId is null");
//                        }


                        String[] separated = selectedName.split(" ");
                        Log.d(TAG, "onItemClick: sep " + separated.length);
                        int selId = Integer.valueOf(separated[separated.length-2]);
                        Log.d(TAG, "onItemClick: sep " + selId);

                        for (Profile profileItem : profileListFull) {
//                            names.add(profileItem.getCorpName());
                            if (profileItem.getPassportId() == selId) {
                                mListener.showSearchSelected(selId, profileItem);
                            }
                        }

//                        Profile profile = profiles.get(position);
//                        tvAcctId.setText("ID: " + String.valueOf(profile.getPassportId()));
//                        tvCorpName.setText(profile.getCorpName());
//                        tvUserName.setText(profile.getUserName());
//                        mListener.showSearchSelected(profile.getPassportId(), profile);
                    }
                });

            }
        });

        editTextFilledExposedDropdown.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + v.getId()
                        + " " + editTextFilledExposedDropdown.getText());
//                Log.d(TAG, "onClick: " + v.getItemId());
            }
        });
//
//
//        editTextFilledExposedDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d(TAG, "onItemSelected: pos " + position
//                + " " + editTextFilledExposedDropdown.getText());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d(TAG, "onItemSelected: nothing ");
//            }
//        });
//
//        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Fruit fruit = (Fruit) adapterView.getItemAtPosition(i);
//                fruitDesc.setText(fruit.getDesc());
//            }
//        });

        return view;
    }

    public void resetSearch() {
        editTextFilledExposedDropdown.setText("");
    }

    private void searchResults(String searchfor) {
        this.searchforValue = searchfor;
        String searchforreq = "%" + searchfor + "%";
        profileViewModel.searchCorpNameProfiles(searchforreq).observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                Log.d(TAG, "profiles search len " + profiles.size());
//                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);
//                setTitle(profiles.size() + " Search Results Items");
            }
        });

    }
//    https://material.io/develop/android/components/menu/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof OnSearchClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement SearchFrag interface");
        }
        mListener = (OnSearchClickListener) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
