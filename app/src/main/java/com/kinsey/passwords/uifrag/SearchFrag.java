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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFrag extends Fragment {
    private static final String TAG = "SearchFrag";

    private List<Profile> profileListFull;
    private ProfileAdapter adapter;
    private List<String> names;
//    private List<Profile> profiles;

    private OnSearchClickListener mListener;
    public interface OnSearchClickListener {
        void showSearchSelected(int selectedId, Profile profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_account_search, container, false);

        AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(R.id.filled_exposed_dropdown);
        TextView tvAcctId = (TextView) view.findViewById(R.id.acct_id);
//        TextView tvCorpName = view.findViewById(R.id.corp_name);
//        TextView tvUserName = view.findViewById(R.id.user_name);

        adapter = new ProfileAdapter(-1);
        MainActivity.profileViewModel.getAllProfilesByCorpName().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                List<Profile> profileListFull = new ArrayList<>(profiles);
                names = new ArrayList<String>();
//                this.profiles = profiles;
                for (Profile profileItem : profileListFull) {
//                    profiles.add(profileItem);
                    names.add(profileItem.getCorpName());
                }
                ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, names);
//                android.R.layout.simple_list_item_1, names);
//                ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
//                        R.layout.dropdown_menu_popup_item, names);

                editTextFilledExposedDropdown.setAdapter(adapter2);
//                editTextFilledExposedDropdown.setText(adapter2.getItem(0).toString());




                editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d(TAG, "onItemClick: pos:id " + position + " : " + id);
                        Log.d(TAG, "onItemClick: id " + adapter2.getItem(position).toString());
                        Log.d(TAG, "onItemClick: adt " + profiles.get(position).getPassportId());
                        Log.d(TAG, "onItemClick: adt " + profiles.get(position).getCorpName());

//                        Toast.makeText(MainActivity.this,
//                                adapter.getItem(position).toString(),
//                                Toast.LENGTH_SHORT).show();
                        
                        if (tvAcctId == null) {
                            Log.d(TAG, "onItemClick: tvAcctId is null");
                        }

                        Profile profile = profiles.get(position);
//                        tvAcctId.setText("ID: " + String.valueOf(profile.getPassportId()));
//                        tvCorpName.setText(profile.getCorpName());
//                        tvUserName.setText(profile.getUserName());
                        mListener.showSearchSelected(profile.getPassportId(), profile);
                    }
                });

            }
        });

        editTextFilledExposedDropdown.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + v.getId()
                        + " " + editTextFilledExposedDropdown.getText());
            }
        });


        editTextFilledExposedDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemSelected: pos " + position
                + " " + editTextFilledExposedDropdown.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onItemSelected: nothing ");
            }
        });


        return view;
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
