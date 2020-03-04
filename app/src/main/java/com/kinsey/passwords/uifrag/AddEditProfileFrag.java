package com.kinsey.passwords.uifrag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.R;

public class AddEditProfileFrag extends Fragment {
    private static final String TAG = "AddEditProfileFrag";

    public static final String ARG_CORP_NAME = "ARG_CORP_NAME";
    public static final String ARG_USER_NAME = "ARG_USER_NAME";
    public static final String ARG_USER_EMAIL = "ARG_USER_EMAIL";

    private TextInputLayout textInputCorpName;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputUserEmail;
    private TextInputLayout textInputCorpWebsite;
    private TextInputLayout textInputNote;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_edit_profile, container, false);

        textInputCorpName = view.findViewById(R.id.text_input_corp_name);
        textInputUserName = view.findViewById(R.id.text_input_user_name);
        textInputUserEmail = view.findViewById(R.id.text_input_user_email);

//        if(savedInstanceState != null){
//            Log.d(TAG, "A SavedInstanceState exists, using past values");
////            legislatorType = savedInstanceState.getInt("id");
////            person = Parcels.unwrap(savedInstanceState.getParcelable("person"));
//            isRotated = true;
//            mSpinnerPos = savedInstanceState.getInt(SPINNER_POSITION_KEY);
//            mRecyclerViewPos = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
//        }else{
//            Bundle bundle = getArguments();
////            legislatorType = bundle.getInt("id");
//        }


        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            textInputCorpName.getEditText().setText(bundle.getString(ARG_CORP_NAME));
            textInputUserName.getEditText().setText(bundle.getString(ARG_USER_NAME));
            textInputUserEmail.getEditText().setText(bundle.getString(ARG_USER_EMAIL));
        } else {
            restoreScreen(savedInstanceState);
        }
//        Log.d(TAG, "onCreateView: corp name " + bundle.getString(ARG_CORP_NAME));

        return view;
    }

    private void restoreScreen(Bundle savedInstanceState) {
        textInputCorpName.getEditText().setText(
                savedInstanceState.getString(ARG_CORP_NAME)
        );
        textInputUserName.getEditText().setText(
                savedInstanceState.getString(ARG_USER_NAME)
        );
        textInputUserEmail.getEditText().setText(
                savedInstanceState.getString(ARG_USER_EMAIL)
        );
    }

        @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_CORP_NAME, textInputCorpName.getEditText().getText().toString().trim());
        outState.putString(ARG_USER_NAME, textInputUserName.getEditText().getText().toString().trim());
        outState.putString(ARG_USER_EMAIL, textInputUserEmail.getEditText().getText().toString().trim());
        Log.d(TAG, "saved instance " + textInputCorpName.getEditText().getText().toString().trim());
    }

}
