package com.kinsey.passwords;


import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.AlertDialog;
//import android.app.Dialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.uifrag.AddEditProfileFrag;
//import com.kinsey.passwords.tools.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditProfileActivity extends AppCompatActivity
        implements View.OnClickListener {
    //implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "AddEditProfileActivity";

//    public static final Pattern PASSWORD_PATTERN =
//            Pattern.compile(....)

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
    public static final String EXTRA_MAX_SEQUENCE =
            "com.kinsey.passwords.EXTRA_MAX_SEQUENCE";
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
    private Profile currProfile = new Profile();
    private TextInputLayout textInputCorpName;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputUserEmail;
    private TextInputLayout textInputCorpWebsite;
    private TextInputLayout textInputNote;
    //    private TextView mtvOpenDate;
    private Button btnOpenDate;

    private DatePickerDialog picker;

    //    private DatePicker mDtePickOpen;
    private TextView tvActvyDate;
    private TextView tvPassportId;
//    private TextView tvSequence;
    private long lngOpenDate = 0;
    private long lngBeginOpenDate = 0;
    private long lngActvDate = 0;
    private int intId = -1;
    private int intPassportId = -1;
    private int intSequence = 0;
    private int intMaxSequence = 0;
    private boolean editModeAdd = false;
    private boolean blnChangesMade = false;
    //    private ImageButton mImgWebView;
    public static final int REQUEST_CODE = 11; // Used to identify the result

    private final Calendar cldrOpened = Calendar.getInstance();
    private Calendar mCalendar;
    private String customDate;

    private ProfileViewModel profileViewModel;

    private static String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);


    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

//    private AddEditProfileFrag.OnProfileModifyClickListener mListener;
//
//    public interface OnProfileModifyClickListener {
//
//        void onProfileModifyItem(Profile profile);
//
//        void onProfileAddItem(Profile profile);
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        setResult(RESULT_CANCELED);
        finish();
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        , @Nullable PersistableBundle persistentState
//        super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addedit_profile);

//        scrollView = findViewById(R.id.scroll_view);
        textInputCorpName = findViewById(R.id.text_input_corp_name);
        textInputUserName = findViewById(R.id.text_input_user_name);
        textInputUserEmail = findViewById(R.id.text_input_user_email);
        textInputCorpWebsite = findViewById(R.id.text_input_corp_website);
        textInputNote = findViewById(R.id.text_input_note);

//        mDtePickOpen = findViewById(R.id.datePicker);
//        ImageButton mImgWebView = findViewById(R.id.img_website);
        Button mbtnWebView = findViewById(R.id.img_website);
        tvActvyDate = findViewById(R.id.actvy_date);
        tvPassportId = findViewById(R.id.passport_id);
//        tvSequence = findViewById(R.id.sequence);
        btnOpenDate = findViewById(R.id.pick_open_date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mListener = (AddEditProfileFrag.OnProfileModifyClickListener) getApplicationContext();


        if (savedInstanceState != null) {
            restoreScreen(savedInstanceState);
        } else {
            Intent intent = getIntent();

            if (intent.hasExtra(EXTRA_ID)) {
                editModeAdd = false;
                Log.d(TAG, "onCreate: edit ");
                setEditUICols(intent);
            } else {
                editModeAdd = true;
                Log.d(TAG, "onCreate: add ");
                setAddUIDefaults(intent);
            }
        }

        if (editModeAdd) {
            setTitle("Add an Account");
        } else {
            setTitle("Account ID: " + intPassportId);
        }

//        profileViewModel = new ViewModelProvider(getApplicationContext()).get(ProfileViewModel.class);

        profileViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);

        mbtnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String corpWebsiteInput = textInputCorpWebsite.getEditText().getText().toString().trim();
                Intent detailIntent = new Intent(AddEditProfileActivity.this, WebViewActivity.class);
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

//        btnOpenDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                showDatePickerDialog(account.getCorpName(), 1);
//
////                if(mListener != null) {
////                    mListener.onDateClicked(account.getActvyLong());
////                }
//
//                int day = cldrOpened.get(Calendar.DAY_OF_MONTH);
//                int month = cldrOpened.get(Calendar.MONTH);
//                int year = cldrOpened.get(Calendar.YEAR);
//                // date picker dialog
//                picker = new DatePickerDialog(getApplicationContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                btnOpenDate.setText("Opened " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//                                mCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
//                                lngOpenDate = mCalendar.getTimeInMillis();
//                                Log.d(TAG, "onDateSet: " + lngOpenDate);
//                            }
//                        }, year, month, day);
//                picker.show();
//            }
//        });


//        mDtePickOpen.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//
//                switch (event.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        requestDisallowInterceptTouchEvent(v, true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        scrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//
//                return false;
//
//            }
//        });

    }

//    public void showDatePickerDialog(View v) {
////        DatePickerFragment datePickerDialog = new DatePickerFragment();
//
//
//
//        if (lngOpenDate == 0) {
//            alertInfo("No Open Date");
//            return;
//        }
//
//
//        Date dteOpen = new Date(lngOpenDate);
//        customDate = format_mdy.format(dteOpen);
//
//
////        int year = getYear();
////        int month = getMonth();
////        int day = getDay();
//
//        int day = cldrOpened.get(Calendar.DAY_OF_MONTH);
//        int month = cldrOpened.get(Calendar.MONTH);
//        int year = cldrOpened.get(Calendar.YEAR);
//
//
////        alertInfo("No Date Pickers Available");
//
////        datePickerDialog.day_ = getDay();
////        datePickerDialog.month_ = getMonth();
////        datePickerDialog.year_ = getYear();
//
//
//        alertInfo(customDate);
//
//
//
////         date picker dialog
//        picker = new DatePickerDialog(this.getApplicationContext(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        btnOpenDate.setText("OPENED " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//                        Calendar c2 = Calendar.getInstance();
//                        c2.set(year, monthOfYear, dayOfMonth);
//                        lngOpenDate = c2.getTimeInMillis();
//                        Log.d(TAG, "callback date long " + lngOpenDate);
//                    }
//                }, year, month, day);
//
//
//        picker.setTitle("Account Profile Open Date");
//        picker.getDatePicker().setMaxDate(new Date().getTime());
//        picker.show();
//
////        datePickerDialog.setCallbackListener(new DatePickerFragment.onDatePickerListener() {
////                                                 @Override
////                                                 public void onDataSet(int year, int month, int day) {
//////                 month = month + 1;
////                                                     mtvOpenDate.setText("OPENED " + (month + 1) + "/" + day + "/" + year);
////
////                                                     Calendar c2 = Calendar.getInstance();
////                                                     c2.set(year, month, day);
////                                                     lngOpenDate = c2.getTimeInMillis();
////                                                     Log.d(TAG, "callback date long " + lngOpenDate);
////                                                 }
////                                             }
////        );
////
////
////        datePickerDialog.show(getSupportFragmentManager(), "datePicker");
//
//    }


//    public void alertInfo(String info) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision " + info);
//        alertDialogBuilder.setPositiveButton("yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
////                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }


    private void setEditUICols(Intent intent) {
        Log.d(TAG, intent.getStringExtra(EXTRA_CORP_NAME));
        textInputCorpName.getEditText().setText(intent.getStringExtra(EXTRA_CORP_NAME));
        textInputUserName.getEditText().setText(intent.getStringExtra(EXTRA_USER_NAME));
        textInputUserEmail.getEditText().setText(intent.getStringExtra(EXTRA_USER_EMAIL));
        textInputCorpWebsite.getEditText().setText(intent.getStringExtra(EXTRA_CORP_WEBSITE));
        textInputNote.getEditText().setText(intent.getStringExtra(EXTRA_NOTE));

        lngOpenDate = intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0);
        Log.d(TAG, String.valueOf(lngOpenDate));
        if (lngOpenDate == 0) {
            btnOpenDate.setText("Click here for OpenDate");
//            alertInfo("No Intents Open Date");
        } else {
            Date dteOpen = new Date(lngOpenDate);
            btnOpenDate.setText("OPENED " + format_mdy.format(dteOpen));
            cldrOpened.setTime(dteOpen);
//            alertInfo("Have Intents Open Date");
        }


        //        Date dte = new Date(intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0));
////        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
//        setOpenDateCalendar(dte);

        lngActvDate = intent.getLongExtra(EXTRA_ACTVY_LONG, 0);
        if (lngActvDate == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(lngActvDate));
        }

        intMaxSequence = intent.getIntExtra(EXTRA_MAX_SEQUENCE, 0);
        intId = intent.getIntExtra(EXTRA_ID, -1);
        intPassportId = intent.getIntExtra(EXTRA_PASSPORT_ID, 0);
        intSequence = intent.getIntExtra(EXTRA_SEQUENCE, 0);
        tvPassportId.setText("AccountId: " + this.intPassportId);
//        tvSequence.setText(" | Seq: " + this.intSequence);

        this.currProfile = new Profile();
        this.currProfile.setId(intId);
        this.currProfile.setPassportId(intPassportId);
        this.currProfile.setCorpName(intent.getStringExtra(EXTRA_CORP_NAME));
        this.currProfile.setUserName(intent.getStringExtra(EXTRA_USER_NAME));
        this.currProfile.setUserEmail(intent.getStringExtra(EXTRA_USER_EMAIL));
        this.currProfile.setCorpWebsite(intent.getStringExtra(EXTRA_CORP_WEBSITE));
        this.currProfile.setNote(intent.getStringExtra(EXTRA_NOTE));
        this.currProfile.setOpenLong(intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0));

        Log.d(TAG, "setEditUICols: ids " + intId + " : " + intPassportId);


    }

//    private void setOpenDateCalendar(Date dte) {
//        Calendar c1 = Calendar.getInstance();
//        c1.setTime(dte);
//        lngOpenDate = c1.getTimeInMillis();
//
//        mDtePickOpen.init(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH),
//                c1.get(Calendar.DAY_OF_MONTH),
//                new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                                Log.d(TAG, "onDateChanged: clicked ");
//                        Calendar c2 = Calendar.getInstance();
//                        c2.set(year, monthOfYear, dayOfMonth);
//                        lngOpenDate = c2.getTimeInMillis();
////                        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
//                    }
//                });
//
//    }

    private void setAddUIDefaults(Intent intent) {
//        mDtePickOpen.setMaxDate(new Date().getTime());
//        mDtePickOpen.setMinDate(0);

        intMaxSequence = intent.getIntExtra(EXTRA_MAX_SEQUENCE, 0);

        Date dte = new Date();

        lngOpenDate = dte.getTime();

        btnOpenDate.setText("Default to Current Date " + format_mdy.format(lngOpenDate));

        //        setOpenDateCalendar(dte);

        this.currProfile = new Profile();
        this.currProfile.setOpenLong(lngOpenDate);
        this.currProfile.setSequence(intMaxSequence);
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
//        Date dte = new Date(lngOpenDate);
//        setOpenDateCalendar(dte);
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
        tvPassportId.setText("AccountId: " + intPassportId);
//        tvSequence.setText(" | Seq: " + intSequence);
        editModeAdd = savedInstanceState.getBoolean(EXTRA_EDIT_MODE, false);
        if (!editModeAdd) {
            intId = savedInstanceState.getInt(EXTRA_ID, -1);
        }
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

    private void saveProfile() {
        if (!haveChanges()) {
            Toast.makeText(this, "No changes to apply", Toast.LENGTH_SHORT).show();
//            showDialogMsg("No changes made to apply");
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
            Toast.makeText(this, R.string.toast_reqd_cols_for_save, Toast.LENGTH_SHORT).show();
            return;
        }


//        Intent data = new Intent();
//        data.putExtra(EXTRA_CORP_NAME, corpName);
//        data.putExtra(EXTRA_USER_NAME, userName);
//        data.putExtra(EXTRA_USER_EMAIL, userEmail);
//        data.putExtra(EXTRA_CORP_WEBSITE, corpWebsite);
//        data.putExtra(EXTRA_NOTE, note);
//        lngBeginOpenDate = lngOpenDate;
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


        Profile profile = new Profile(intSequence, corpName, userName, userEmail, corpWebsite);
        profile.setId(intId);
        profile.setPassportId(intPassportId);
        profile.setNote(note);
        profile.setOpenLong(lngOpenDate);
        profile.setActvyLong(System.currentTimeMillis());

//        this.currProfile.setCorpName(corpName);
//        this.currProfile.setUserName(userName);
//        this.currProfile.setUserEmail(userEmail);
//        this.currProfile.setCorpWebsite(corpWebsite);
//        this.currProfile.setNote(note);
//        this.currProfile.setOpenLong(lngOpenDate);
        lngBeginOpenDate = lngOpenDate;


        if (editModeAdd) {
//            mListener.onProfileAddItem(profile);
            profile.setId(0);
            profile.setSequence(intMaxSequence + 1);
            profileViewModel.insertProfile(profile);
            editModeAdd = false;
            Log.d(TAG, "saveProfile add " + profile);
//            Toast.makeText(this, "New account added", Toast.LENGTH_SHORT).show();
            //            showDialogMsg("New Account added for id " + profile.getId() + ":" + profile.getPassportId());
//            showDialogMsg("New Account added");
        } else {
            profileViewModel.update(profile);
//        profileViewModel.update(profile);
//            mListener.onProfileModifyItem(profile);
//            Toast.makeText(this, "Account changes applied", Toast.LENGTH_SHORT).show();
//            showDialogMsg("Account changes applied " + profile.getId() + ":" + profile.getPassportId());
            Log.d(TAG, "saveProfile " + profile);
        }
        blnChangesMade = false;
//        Toast.makeText(getContext(), R.string.toast_profile_updated, Toast.LENGTH_SHORT).show();


        closeKeyboard();
        Toast.makeText(this, "Account updated", Toast.LENGTH_SHORT).show();
//        finish();


//        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditProfileActivity.this);
//
//        // Set the message show for the Alert time
//        builder.setMessage("Do you want to exit ?");
//
//        // Set Alert Title
//        builder.setTitle("Alert !");
//
//        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
//        builder.setCancelable(false);
//
//        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
//        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
//            // When the user click yes button then app will close
//            finish();
//        });
//
//        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
//        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
//            // If user click no then dialog box is canceled.
//            dialog.cancel();
//        });
//
//        // Create the Alert dialog
//        AlertDialog alertDialog = builder.create();
//        // Show the Alert Dialog box
//        alertDialog.show();

//        if (editModeAdd) {
//            showDialogMsg(getString(R.string.account_added) + "\n" +
//                    getString(R.string.ask_go_to_list));
//        } else {
//            showDialogMsg(getString(R.string.account_updated) + "\n" +
//                    getString(R.string.ask_go_to_list));
//        }

//        setResult(RESULT_OK, data);
//        finish();

//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
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
//                        setResult(RESULT_OK, data);
//                        closeKeyboard();
//                        finish();
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
            Log.d(TAG, "haveChanges: corpWebsites not equal");
            return true;
        }
        if (!currProfile.getNote().equals(textInputNote.getEditText().getText().toString().trim())) {
            Log.d(TAG, "haveChanges: corpWebsites not equal");
            return true;
        }
        if (lngOpenDate != currProfile.getOpenLong()) {
            Log.d(TAG, "haveChanges: open date not equal");
            Log.d(TAG, "openDate " + String.valueOf(lngOpenDate));
            Log.d(TAG, "openDate " + String.valueOf(lngBeginOpenDate));
            Log.d(TAG, "openDate " + String.valueOf(currProfile.getOpenLong()));
            return true;
        }
        return false;
    }


    private void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CORP_NAME, textInputCorpName.getEditText().getText().toString().trim());
        Log.d(TAG, "saved instance " + textInputCorpName.getEditText().getText().toString().trim());
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            Log.d(TAG, "onActivityResult");
//            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
//            dateOfBirthET.setText(selectedDate);
        }

    }


    private int getYear() {
        return Integer.parseInt(changeDateFormat(customDate, "MM/dd/yyyy", "YYYY"));

    }

    private int getMonth() {
        return Integer.parseInt(changeDateFormat(customDate, "MM/dd/yyyy", "MM")) - 1;  //substract one from month because month gets start from 0 in Calendar.

    }

    private int getDay() {
        return Integer.parseInt(changeDateFormat(customDate, "MM/dd/yyyy", "dd"));
    }


    public String changeDateFormat(String dateString, String sourceDateFormat, String targetDateFormat) {
        if (dateString == null || dateString.isEmpty())
            return "";

        SimpleDateFormat inputDateFormat = new SimpleDateFormat(sourceDateFormat, Locale.US);
        Date date = new Date();

        try {
            date = inputDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outputDateFormat = new SimpleDateFormat(targetDateFormat, Locale.US);

        return outputDateFormat.format(date);
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


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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

    public void showDialogMsg(String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setMessage(msg)
//                .setTitle(msg);
//        AlertDialog dialog = builder.create();
//
//        dialog.show();

        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.dialog_msg_ok);
        dialog.setTitle("Account Modify Info");

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_info_24dp);


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


}
