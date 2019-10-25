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
    public static final String EXTRA_CORP_WEBSITE =
            "com.kinsey.passwords.EXTRA_CORP_WEBSITE";
    public static final String EXTRA_NOTE =
            "com.kinsey.passwords.EXTRA_NOTE";
    public static final String EXTRA_ACTVY_LONG =
            "com.kinsey.passwords.EXTRA_ACTVY_LONG";
    public static final String EXTRA_OPEN_DATE_LONG =
            "com.kinsey.passwords.EXTRA_OPEN_DATE_LONG";

    private TextInputLayout textInputCorpName;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputUserEmail;
    private TextInputLayout textInputCorpWebsite;
    private TextInputLayout textInputNote;

    private TextView tvActvyDate;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;
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

        tvActvyDate = findViewById(R.id.actvy_date);
        mDtePickOpen = (DatePicker) findViewById(R.id.datePicker);
        mImgWebView = (ImageButton) findViewById(R.id.img_website);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Profile Account ID: " + intent.getIntExtra(EXTRA_PASSPORT_ID, 0));
            setEditUICols(intent);
        } else {
            setTitle("Add Profile Account");
            setAddUIDefaults(intent);
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

        if (savedInstanceState != null) {
            textInputCorpName.getEditText().setText(
                    savedInstanceState.getString("corpName")
            );
            textInputUserName.getEditText().setText(
                    savedInstanceState.getString("username")
            );
            textInputUserEmail.getEditText().setText(
                    savedInstanceState.getString("useremail")
            );
            textInputCorpWebsite.getEditText().setText(
                    savedInstanceState.getString("corpwebsite")
            );
            textInputNote.getEditText().setText(
                    savedInstanceState.getString("note")
            );

            lngOpenDate = savedInstanceState.getLong("opendate");
//            Log.d(TAG, "instance of " + savedInstanceState.getString("corpName"));
        }
    }


    private void setEditUICols(Intent intent) {
        textInputCorpName.getEditText().setText(intent.getStringExtra(EXTRA_CORP_NAME).toString());
        textInputUserName.getEditText().setText(intent.getStringExtra(EXTRA_USER_NAME).toString());
        textInputUserEmail.getEditText().setText(intent.getStringExtra(EXTRA_USER_EMAIL).toString());
        textInputCorpWebsite.getEditText().setText(intent.getStringExtra(EXTRA_CORP_WEBSITE).toString());
        textInputNote.getEditText().setText(intent.getStringExtra(EXTRA_NOTE).toString());

        if (intent.getLongExtra(EXTRA_ACTVY_LONG, 0) == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(intent.getLongExtra(EXTRA_ACTVY_LONG, 0)));
        }

        lngOpenDate = intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0);
        Date dte = new Date(intent.getLongExtra(EXTRA_OPEN_DATE_LONG, 0));
//        Log.d(TAG, "onDateChanged: lngOpenDate " + lngOpenDate);
        setOpenDateCalendar(dte);
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

    private boolean validateUserEmail() {
        String emailInput = textInputUserEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputUserEmail.getEditText().setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputUserEmail.getEditText().setError("Please enter a valid email address");
            return false;
        } else {
//            Log.d(TAG, "xxxxxxxxxx ");
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

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        int passportId = getIntent().getIntExtra(EXTRA_PASSPORT_ID, 0);
        data.putExtra(EXTRA_PASSPORT_ID, id);

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
        outState.putString("corpName", textInputCorpName.getEditText().getText().toString().trim());
        Log.d(TAG, "saved instance " + textInputCorpName.getEditText().getText().toString().trim());
        outState.putString("username", textInputUserName.getEditText().getText().toString().trim());
        outState.putString("useremail", textInputUserEmail.getEditText().getText().toString().trim());
        outState.putString("corpwebsite", textInputCorpWebsite.getEditText().getText().toString().trim());
        outState.putString("note", textInputNote.getEditText().getText().toString().trim());
        outState.putLong("openlong", lngOpenDate);
    }
}
