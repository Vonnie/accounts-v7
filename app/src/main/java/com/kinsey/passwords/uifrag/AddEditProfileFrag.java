package com.kinsey.passwords.uifrag;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.AddEditProfileActivity;
import com.kinsey.passwords.R;
import com.kinsey.passwords.WebViewActivity;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditProfileFrag extends Fragment
        implements View.OnClickListener, Task {

    private static final String TAG = "AddEditProfileFrag";

    public static final String EXTRA_ID =
            "com.kinsey.passwords.EXTRA_ID";
    public static final String EXTRA_PASSPORT_ID =
            "com.kinsey.passwords.EXTRA_PASSPORTID";
    public static final String EXTRA_CORP_NAME =
            "com.kinsey.passwords.EXTRA_CORP_NAME";
    public static final String EXTRA_USER_NAME =
            "com.kinsey.passwords.EXTRA_USER_NAME";
    public static final String EXTRA_USER_EMAIL =
            "com.kinsey.passwords.EXTRA_USER_EMAIL";
    public static final String EXTRA_SEQUENCE =
            "com.kinsey.passwords.EXTRA_SEQUENCE";
    public static final String EXTRA_CORP_WEBSITE =
            "com.kinsey.passwords.EXTRA_CORP_WEBSITE";
    public static final String EXTRA_NOTE =
            "com.kinsey.passwords.EXTRA_NOTE";
    public static final String EXTRA_ACTVY_LONG =
            "com.kinsey.passwords.EXTRA_ACTVY_LONG";
    public static final String EXTRA_OPEN_DATE_LONG =
            "com.kinsey.passwords.EXTRA_OPEN_DATE_LONG";
    public static final String EXTRA_EDIT_MODE =
            "com.kinsey.passwords.EXTRA_EDIT_MODE";

    private TextInputLayout textInputCorpName;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputUserEmail;
    private TextInputLayout textInputCorpWebsite;
    private TextInputLayout textInputNote;
    private Button btnOpenDate;
    private DatePickerDialog picker;
    private TextView tvActvyDate;
    private TextView tvPassportId;
    private TextView tvSequence;

    private long lngOpenDate = 0;
    private long lngBeginOpenDate = 0;
    private long lngActvDate = 0;
    private int intId = -1;
    private int intPassportId = -1;
    private int intSequence = 0;
    private boolean editModeAdd = false;
    private boolean blnChangesMade = false;
    private final Calendar cldrOpened = Calendar.getInstance();

    private Profile currProfile;

    private OnProfileModifyClickListener mListener;

    public interface OnProfileModifyClickListener {

        void onProfileModifyItem(Profile profile);

        void onProfileAddItem(Profile profile);

    }


    private static String pattern_mdy = "MM/dd/yyyy";
    private static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    private static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_edit_profile, container, false);

        textInputCorpName = view.findViewById(R.id.text_input_corp_name);
        textInputUserName = view.findViewById(R.id.text_input_user_name);
        textInputUserEmail = view.findViewById(R.id.text_input_user_email);
        textInputCorpWebsite = view.findViewById(R.id.text_input_corp_website);
        textInputNote = view.findViewById(R.id.text_input_note);
        ImageButton mImgWebView = view.findViewById(R.id.img_website);
        tvActvyDate = view.findViewById(R.id.actvy_date);
        tvPassportId = view.findViewById(R.id.passport_id);
        tvSequence = view.findViewById(R.id.sequence);
        btnOpenDate = view.findViewById(R.id.pick_open_date);

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
            buildScreen();
        } else {
            restoreScreen(savedInstanceState);
        }
//        Log.d(TAG, "onCreateView: corp name " + bundle.getString(EXTRA_CORP_NAME));
        mImgWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String corpWebsiteInput = textInputCorpWebsite.getEditText().getText().toString().trim();
                Intent detailIntent = new Intent(getContext(), WebViewActivity.class);
                detailIntent.putExtra(WebViewActivity.class.getSimpleName(), corpWebsiteInput);
                detailIntent.putExtra(WebViewActivity.EXTRA_CORP_NAME,
                        textInputCorpName.getEditText().getText().toString().trim());
                detailIntent.putExtra(WebViewActivity.EXTRA_CORP_WEBSITE,
                        textInputCorpWebsite.getEditText().getText().toString().trim());
//                Log.d(TAG, "onClick: website " + account.getCorpWebsite());
//                Log.d(TAG, "onClick: wv class " + WebViewActivity.class.getSimpleName());
                startActivity(detailIntent);

            }
        });


        btnOpenDate.setOnClickListener(this);

//        textInputCorpName.addOnEditTextAttachedListener(new TextInputLayout.OnEditTextAttachedListener() {
//            @Override
//            public void onEditTextAttached(TextInputLayout textInputLayout) {
//                Log.d(TAG, "onEditTextAttached: " + textInputLayout.getEditText());
//            }
//        });
//
//        EditText textInputCorpName_text = view.findViewById(R.id.text_input_corp_name_text);
//        textInputCorpName_text.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
////                field2.setText("");
////                if (currCorpName.equal(s)) {
//
////                }
////                Log.d(TAG, "onTextChanged: s " + s + " : " + currCorpName);
////                currCorpName = String.valueOf(s);
//            }
//        });

//        textInputCorpName.setOnKeyListener(this);
//        view.findViewById(R.id.text_input_corp_name).setOnKeyListener((OnKeyListener) this);


//        textInputCorpName.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d(TAG, "onKey: " + keyCode);
//                return false;
//            }
//        });


        FloatingActionButton buttonModifyProfile = view.findViewById(R.id.button_add_profile);
//        buttonAddPofile.setImageResource(R.drawable.ic_audiotrack_light);
        buttonModifyProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEditProfileActivity.class);
                Log.d(TAG, "onCreate: save request");
                saveProfile();
//                startActivityForResult(intent, ADD_PROFILE_REQUEST);
//                startActivity(intent);

//                onSignInClickedButton(v);
            }
        });

        return view;
    }

    private void buildScreen() {
        Bundle bundle = getArguments();

        int id = bundle.getInt(EXTRA_ID, -1);
        Log.d(TAG, "buildScreen: id " + id);
        if (id == -1) {
            editModeAdd = true;
            setAddUIDefaults();
        } else {
            editModeAdd = false;
            setEditUICols(bundle);
        }

    }
    private void setEditUICols(Bundle bundle) {
        textInputCorpName.getEditText().setText(bundle.getString(EXTRA_CORP_NAME));
        textInputUserName.getEditText().setText(bundle.getString(EXTRA_USER_NAME));
        textInputUserEmail.getEditText().setText(bundle.getString(EXTRA_USER_EMAIL));
        textInputCorpWebsite.getEditText().setText(bundle.getString(EXTRA_CORP_WEBSITE));
        textInputNote.getEditText().setText(bundle.getString(EXTRA_NOTE));

        lngOpenDate = bundle.getLong(EXTRA_OPEN_DATE_LONG, 0);
        lngBeginOpenDate = lngOpenDate;
        Log.d(TAG, String.valueOf(lngOpenDate));
        if (lngOpenDate == 0) {
            btnOpenDate.setText(R.string.openClickForCalendar);
//            alertInfo("No Intents Open Date");
        } else {
            Date dteOpen = new Date(lngOpenDate);
            btnOpenDate.setText(String.format(getString(R.string.opened), format_mdy.format(dteOpen)));
            cldrOpened.setTime(dteOpen);
//            alertInfo("Have Intents Open Date");
        }


        //        Date dte = new Date(intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0));
////        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
//        setOpenDateCalendar(dte);

        lngActvDate = bundle.getLong(EXTRA_ACTVY_LONG, 0);
        if (lngActvDate == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText(String.format(getString(R.string.activity_date), format_ymdtimehm.format(lngActvDate)));
        }

        intId = bundle.getInt(EXTRA_ID, -1);
        intPassportId = bundle.getInt(EXTRA_PASSPORT_ID, 0);
        intSequence = bundle.getInt(EXTRA_SEQUENCE, 0);
        tvPassportId.setText(String.format(getString(R.string.id_descr), String.valueOf(intPassportId)));
        tvSequence.setText(String.format(getString(R.string.seq_descr), String.valueOf(this.intSequence)));

        this.currProfile = new Profile();
        this.currProfile.setId(intId);
        this.currProfile.setPassportId(intPassportId);
        this.currProfile.setCorpName(bundle.getString(EXTRA_CORP_NAME));
        this.currProfile.setUserName(bundle.getString(EXTRA_USER_NAME));
        this.currProfile.setUserEmail(bundle.getString(EXTRA_USER_EMAIL));


        Log.d(TAG, "setEditUICols: ids " + intId + " : " + intPassportId);
    }

    private void setAddUIDefaults() {
//        mDtePickOpen.setMaxDate(new Date().getTime());
//        mDtePickOpen.setMinDate(0);
        Date dte = new Date();

        lngOpenDate = dte.getTime();
        lngBeginOpenDate = lngOpenDate;

        btnOpenDate.setText(String.format(getString(R.string.opened_button_descr), format_mdy.format(lngOpenDate)));

        //        setOpenDateCalendar(dte);
        this.currProfile = new Profile();
    }

    private boolean validateUserEmail() {
        String emailInput = textInputUserEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputUserEmail.getEditText().setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputUserEmail.getEditText().setError("Please enter a valid email address");
            return false;
        } else {
            textInputUserEmail.getEditText().setError(null);
//            textInputUserEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String usernameInput = textInputUserName.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUserName.getEditText().setError("User name can't be empty");
            return false;
        } else {
            textInputUserName.getEditText().setError(null);
            return true;
        }
    }

    private boolean validateCorpName() {
        String corpnameInput = textInputCorpName.getEditText().getText().toString().trim();

        if (corpnameInput.isEmpty()) {
            Log.d(TAG, "corp name empty");
//            textInputCorpName.getEditText().setFocusable(true);
            textInputCorpName.getEditText().setError("Corp name is required");
//            textInputCorpName.setErrorEnabled(true);
//            textInputCorpName.getEditText().requestFocus();
            return false;
        } else {
            textInputCorpName.getEditText().setError(null);
//            textInputCorpName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCorpWebsite() {
        String corpWebsiteInput = textInputCorpWebsite.getEditText().getText().toString().trim();

//        if (!corpWebsiteInput.toLowerCase().startsWith("http://")) {
//            corpWebsiteInput = "http://" + corpWebsiteInput;
//        }

        if (corpWebsiteInput.isEmpty()) {
            return true;
        } else if (!Patterns.WEB_URL.matcher(corpWebsiteInput).matches()) {
            textInputCorpWebsite.getEditText().setError("Please enter a valid corp website");
            return false;
        } else if (!corpWebsiteInput.startsWith("http")) {
            textInputCorpWebsite.getEditText().setError("Websites must start with http");
            return false;
        } else {
            textInputCorpName.getEditText().setError(null);
            return true;
        }
    }

    public boolean haveChanges() {
        if (currProfile == null) {
            return false;
        }
        if (!currProfile.getCorpName().equals(textInputCorpName.getEditText().getText().toString().trim())) {
//            Log.d(TAG, "haveChanges: " + currCorpName + " : " + textInputCorpName.getEditText().getText().toString().trim());
            return true;
        }
        if (!currProfile.getUserName().equals(textInputUserName.getEditText().getText().toString().trim())) {
            return true;
        }
        if (!currProfile.getUserEmail().equals(textInputUserEmail.getEditText().getText().toString().trim())) {
            return true;
        }
        if (!currProfile.getCorpWebsite().equals(textInputCorpWebsite.getEditText().getText().toString().trim())) {
            return true;
        }
        if (!currProfile.getNote().equals(textInputNote.getEditText().getText().toString().trim())) {
            return true;
        }
        if (currProfile.getOpenLong() != lngBeginOpenDate) {
            return true;
        }
        return false;
    }

    private void saveProfile() {
        if (!haveChanges()) {
            showDialogMsg("No changes made to apply");
            return;
        }
        if (!validateCorpName()) {
            return;
        }
        if (!validateUserName() | !validateUserEmail() | !validateCorpWebsite()) {
            return;
        }
        String corpName = textInputCorpName.getEditText().getText().toString().trim();
        String userName = textInputUserName.getEditText().getText().toString().trim();
        String userEmail = textInputUserEmail.getEditText().getText().toString().trim();
        String corpWebsite = textInputCorpWebsite.getEditText().getText().toString();
        String note = textInputNote.getEditText().getText().toString();

        if (corpName.trim().isEmpty() || userName.trim().isEmpty()
                || userEmail.trim().isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_reqd_cols_for_save, Toast.LENGTH_SHORT).show();
            return;
        }


        Profile profile = new Profile(intSequence, corpName, userName, userEmail, corpWebsite);
        profile.setId(intId);
        profile.setPassportId(intPassportId);
        profile.setNote(note);
        profile.setOpenLong(lngOpenDate);
        profile.setActvyLong(System.currentTimeMillis());

        if (editModeAdd) {
            mListener.onProfileAddItem(profile);
            editModeAdd = false;
            showDialogMsg("New Account added");
        } else {
//        profileViewModel.update(profile);
            mListener.onProfileModifyItem(profile);
            showDialogMsg("Account changes applied");
        }
        blnChangesMade = false;
//        Toast.makeText(getContext(), R.string.toast_profile_updated, Toast.LENGTH_SHORT).show();


        this.currProfile.setCorpName(corpName);
        this.currProfile.setUserName(userName);
        this.currProfile.setUserEmail(userEmail);
        this.currProfile.setCorpWebsite(corpWebsite);
        this.currProfile.setNote(note);
        this.currProfile.setOpenLong(lngOpenDate);
        lngBeginOpenDate = lngOpenDate;

//        Intent data = new Intent();
//        data.putExtra(EXTRA_CORP_NAME, corpName);
//        data.putExtra(EXTRA_USER_NAME, userName);
//        data.putExtra(EXTRA_USER_EMAIL, userEmail);
//        data.putExtra(EXTRA_CORP_WEBSITE, corpWebsite);
//        data.putExtra(EXTRA_NOTE, note);
//
//
////        Calendar c2 = Calendar.getInstance();
////        c2.set(mDtePickOpen.getYear(), mDtePickOpen.getMonth(), mDtePickOpen.getDayOfMonth());
////        long lngDatePickerOpenDate = c2.getTimeInMillis();
////
////        data.putExtra(EXTRA_OPEN_DATE_LONG, lngDatePickerOpenDate);
//
//        data.putExtra(EXTRA_OPEN_DATE_LONG, lngOpenDate);
////        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
//
//        data.putExtra(EXTRA_ACTVY_LONG, new Date().getTime());
//
//
//        if (!editModeAdd) {
//            if (intId != -1) {
//                data.putExtra(EXTRA_ID, intId);
//            }
//        }
//
////        int passportId = getIntent().getIntExtra(EXTRA_PASSPORT_ID, 0);
//        data.putExtra(EXTRA_PASSPORT_ID, intPassportId);
////        int sequence = getIntent().getIntExtra(EXTRA_SEQUENCE, 0);
//        data.putExtra(EXTRA_SEQUENCE, intSequence);
//
//
//
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
//        if (editModeAdd) {
//            alertDialogBuilder.setMessage(getString(R.string.account_added) + "\n" +
//                    getString(R.string.ask_go_to_list));
//        } else {
//            alertDialogBuilder.setMessage(getString(R.string.account_updated) + "\n" +
//                    getString(R.string.ask_go_to_list));
//        }
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//
////                        setResult(RESULT_OK, data);
////                        finish();
//
//                    }
//                })
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();


    }

    public void showDialogMsg(String msg) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_msg_ok);
        dialog.setTitle("Account Modify Info");

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_info_black_24dp);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int mYear, mMonth, mDay, mHour, mMinute;
        if (v == btnOpenDate) {
            // Get Current Date
//            final Calendar c = Calendar.getInstance();
            mYear = cldrOpened.get(Calendar.YEAR);
            mMonth = cldrOpened.get(Calendar.MONTH);
            mDay = cldrOpened.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

//                            btnOpenDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            btnOpenDate.setText(R.string.opened + " " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            Calendar c2 = Calendar.getInstance();
                            c2.set(year, monthOfYear, dayOfMonth);
                            lngOpenDate = c2.getTimeInMillis();
                            Log.d(TAG, "callback date long " + lngOpenDate);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.setTitle(getString(R.string.datePickerTitle));
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        }
    }


    private void restoreScreen(Bundle savedInstanceState) {
        textInputCorpName.getEditText().setText(
                savedInstanceState.getString(EXTRA_CORP_NAME)
        );
        textInputUserName.getEditText().setText(
                savedInstanceState.getString(EXTRA_USER_NAME)
        );
        textInputUserEmail.getEditText().setText(
                savedInstanceState.getString(EXTRA_USER_EMAIL)
        );
        textInputCorpWebsite.getEditText().setText(
                savedInstanceState.getString(EXTRA_CORP_WEBSITE)
        );
        textInputNote.getEditText().setText(
                savedInstanceState.getString(EXTRA_NOTE)
        );
        lngOpenDate = savedInstanceState.getLong(EXTRA_OPEN_DATE_LONG, 0);
        if (lngOpenDate == 0) {
            btnOpenDate.setText("Click here for OpenDate");
        } else {
            Date dteOpen = new Date(lngOpenDate);
            btnOpenDate.setText("OPENED " + format_mdy.format(dteOpen));
            cldrOpened.setTime(dteOpen);
        }
        lngActvDate = savedInstanceState.getLong(EXTRA_ACTVY_LONG, 0);
        if (lngActvDate == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(lngActvDate));
        }
        intPassportId = savedInstanceState.getInt(EXTRA_PASSPORT_ID, 0);
        intSequence = savedInstanceState.getInt(EXTRA_SEQUENCE, 0);
        tvPassportId.setText(" | Id: " + intPassportId);
        tvSequence.setText(" | Seq: " + intSequence);
        editModeAdd = savedInstanceState.getBoolean(EXTRA_EDIT_MODE, false);
        if (!editModeAdd) {
            intId = savedInstanceState.getInt(EXTRA_ID, -1);
        }

    }

        @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CORP_NAME, textInputCorpName.getEditText().getText().toString().trim());
        outState.putString(EXTRA_USER_NAME, textInputUserName.getEditText().getText().toString().trim());
        outState.putString(EXTRA_USER_EMAIL, textInputUserEmail.getEditText().getText().toString().trim());
        outState.putString(EXTRA_CORP_WEBSITE, textInputCorpWebsite.getEditText().getText().toString().trim());
        outState.putString(EXTRA_NOTE, textInputNote.getEditText().getText().toString().trim());
        outState.putLong(EXTRA_OPEN_DATE_LONG, lngOpenDate);
        outState.putLong(EXTRA_ACTVY_LONG, lngActvDate);
        outState.putInt(EXTRA_ID, intId);
        outState.putInt(EXTRA_PASSPORT_ID, intPassportId);
        outState.putInt(EXTRA_SEQUENCE, intSequence);
        outState.putBoolean(EXTRA_EDIT_MODE, editModeAdd);
        Log.d(TAG, "saved instance " + textInputCorpName.getEditText().getText().toString().trim());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks
        Activity activity = getActivity();
        if (!(activity instanceof OnProfileModifyClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditProfileFrag interface");
        }
        mListener = (OnProfileModifyClickListener) activity;

    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mListener = null;
    }

}
