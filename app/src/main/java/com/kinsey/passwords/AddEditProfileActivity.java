package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditProfileActivity extends AppCompatActivity {
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

    private DatePicker mDtePickOpen;
    private TextView tvActvyDate;
    private TextView tvPassportId;
    private TextView tvSequence;
    private long lngOpenDate = 0l;
    private long lngActvDate = 0l;
    private int intId = -1;
    private int intPassportId = -1;
    private int intSequence = 0;
    private boolean editModeAdd = false;
    private ImageButton mImgWebView;

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);


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

        textInputCorpName = findViewById(R.id.text_input_corp_name);
        textInputUserName = findViewById(R.id.text_input_user_name);
        textInputUserEmail = findViewById(R.id.text_input_user_email);
        textInputCorpWebsite = findViewById(R.id.text_input_corp_website);
        textInputNote = findViewById(R.id.text_input_note);

        mDtePickOpen = (DatePicker) findViewById(R.id.datePicker);
        mImgWebView = (ImageButton) findViewById(R.id.img_website);
        tvActvyDate = findViewById(R.id.actvy_date);
        tvPassportId = findViewById(R.id.passport_id);
        tvSequence = findViewById(R.id.sequence);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if (savedInstanceState != null) {
            restoreScreen(savedInstanceState);
        } else {
            Intent intent = getIntent();

            if (intent.hasExtra(EXTRA_ID)) {
                editModeAdd = false;
                setEditUICols(intent);
            } else {
                editModeAdd = true;
                setAddUIDefaults(intent);
            }
        }

        if (editModeAdd) {
            setTitle("Add Profile Account");
        } else {
            setTitle("Profile ID: " + intPassportId);
        }

        Log.d(TAG, "onCreate");

        mImgWebView.setOnClickListener(new View.OnClickListener(){
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

    }


    private void setEditUICols(Intent intent) {
        textInputCorpName.getEditText().setText(intent.getStringExtra(EXTRA_CORP_NAME).toString());
        textInputUserName.getEditText().setText(intent.getStringExtra(EXTRA_USER_NAME).toString());
        textInputUserEmail.getEditText().setText(intent.getStringExtra(EXTRA_USER_EMAIL).toString());
        textInputCorpWebsite.getEditText().setText(intent.getStringExtra(EXTRA_CORP_WEBSITE).toString());
        textInputNote.getEditText().setText(intent.getStringExtra(EXTRA_NOTE).toString());

        lngOpenDate = intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0);
        Date dte = new Date(intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0));
//        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
        setOpenDateCalendar(dte);

        this.lngActvDate = intent.getLongExtra(EXTRA_ACTVY_LONG, 0);
        if (lngActvDate == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(lngActvDate));
        }

        this.intId = intent.getIntExtra(EXTRA_ID, -1);
        this.intPassportId = intent.getIntExtra(EXTRA_PASSPORT_ID, 0);
        this.intSequence = intent.getIntExtra(EXTRA_SEQUENCE, 0);
        this.tvPassportId.setText(" | Id: " + String.valueOf(this.intPassportId));
        this.tvSequence.setText(" | Seq: " + String.valueOf(this.intSequence));
    }

    private void setOpenDateCalendar(Date dte) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dte);
        lngOpenDate = c1.getTimeInMillis();

        mDtePickOpen.init(c1.get(Calendar.YEAR),
                c1.get(Calendar.MONTH),
                c1.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                Log.d(TAG, "onDateChanged: clicked ");
                        Calendar c2 = Calendar.getInstance();
                        c2.set(year, monthOfYear, dayOfMonth);
                        lngOpenDate = c2.getTimeInMillis();
//                        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
                    }
                });

    }

    private void setAddUIDefaults(Intent intent) {
        mDtePickOpen.setMaxDate(new Date().getTime());
        mDtePickOpen.setMinDate(0);
        Date dte = new Date();

        setOpenDateCalendar(dte);
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

        lngOpenDate = savedInstanceState.getLong(EXTRA_OPEN_DATE_LONG, 0l);
        Date dte = new Date(lngOpenDate);
        setOpenDateCalendar(dte);

        lngActvDate = savedInstanceState.getLong(EXTRA_ACTVY_LONG, 0);
        if (lngActvDate == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(lngActvDate));
        }
        intPassportId = savedInstanceState.getInt(EXTRA_PASSPORT_ID, 0);
        intSequence = savedInstanceState.getInt(EXTRA_SEQUENCE, 0);
        tvPassportId.setText(" | Id: " + String.valueOf(intPassportId));
        tvSequence.setText(" | Seq: " + String.valueOf(intSequence));
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
            Toast.makeText(this, "Please insert corp name, user name, user email", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent data = new Intent();
        data.putExtra(EXTRA_CORP_NAME, corpName);
        data.putExtra(EXTRA_USER_NAME, userName);
        data.putExtra(EXTRA_USER_EMAIL, userEmail);
        data.putExtra(EXTRA_CORP_WEBSITE, corpWebsite);
        data.putExtra(EXTRA_NOTE, note);


//        Calendar c2 = Calendar.getInstance();
//        c2.set(mDtePickOpen.getYear(), mDtePickOpen.getMonth(), mDtePickOpen.getDayOfMonth());
//        long lngDatePickerOpenDate = c2.getTimeInMillis();
//
//        data.putExtra(EXTRA_OPEN_DATE_LONG, lngDatePickerOpenDate);

        data.putExtra(EXTRA_OPEN_DATE_LONG, lngOpenDate);
//        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);

        data.putExtra(EXTRA_ACTVY_LONG, new Date().getTime());


        if (!editModeAdd) {
            if (intId != -1) {
                data.putExtra(EXTRA_ID, intId);
            }
        }

//        int passportId = getIntent().getIntExtra(EXTRA_PASSPORT_ID, 0);
        data.putExtra(EXTRA_PASSPORT_ID, intPassportId);
//        int sequence = getIntent().getIntExtra(EXTRA_SEQUENCE, 0);
        data.putExtra(EXTRA_SEQUENCE, intSequence);

        setResult(RESULT_OK, data);
        finish();
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
}
