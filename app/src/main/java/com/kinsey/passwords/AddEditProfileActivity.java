package com.kinsey.passwords;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
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

    private EditText editNote;
    private TextView tvActvyDate;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;


    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);


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

        editNote = findViewById(R.id.edit_notes);
        tvActvyDate = findViewById(R.id.actvy_date);
        mDtePickOpen = (DatePicker) findViewById(R.id.datePicker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Profile Account: " + intent.getIntExtra(EXTRA_ID, 0));
            setEditUICols(intent);
        } else {
            setTitle("Add Profile Account");
        }
    }


    private void setEditUICols(Intent intent) {
        textInputCorpName.getEditText().setText(intent.getStringExtra(EXTRA_CORP_NAME).toString());
        textInputUserName.getEditText().setText(intent.getStringExtra(EXTRA_USER_NAME).toString());
        textInputUserEmail.getEditText().setText(intent.getStringExtra(EXTRA_USER_EMAIL).toString());
        textInputCorpWebsite.getEditText().setText(intent.getStringExtra(EXTRA_CORP_WEBSITE).toString());

        editNote.setText(intent.getStringExtra(EXTRA_NOTE));

        if (intent.getLongExtra(EXTRA_ACTVY_LONG, 0) == 0) {
            tvActvyDate.setText("");
        } else {
            tvActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(intent.getLongExtra(EXTRA_ACTVY_LONG, 0)));
        }

    }

    private boolean validateUserEmail() {
        String emailInput = textInputUserEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputUserEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputUserEmail.setError("Please enter a valid email address");
            return false;
        } else {
//            Log.d(TAG, "xxxxxxxxxx ");
            textInputUserEmail.setError(null);
//            textInputUserEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String usernameInput = textInputUserName.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUserName.setError("Field can't be empty");
            return false;
        } else {
            textInputUserName.setError(null);
            return true;
        }
    }

    private boolean validateCorpName() {
        String corpnameInput = textInputCorpName.getEditText().getText().toString().trim();

        if (corpnameInput.isEmpty()) {
            textInputCorpName.setError("Field can't be empty");
            return false;
        } else {
            textInputCorpName.setError(null);
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
            textInputCorpWebsite.setError("Please enter a valid corp website");
            return false;
        } else {
            textInputCorpName.setError(null);
            return true;
        }
}

    private void saveProfile() {
        if (!validateCorpName() | !validateUserName() | !validateUserEmail() | !validateCorpWebsite()) {
            return;
        }
        String corpName = textInputCorpName.getEditText().getText().toString().trim();
        String userName = textInputUserName.getEditText().getText().toString().trim();
        String userEmail = textInputUserEmail.getEditText().getText().toString().trim();
        String corpWebsite = textInputCorpWebsite.getEditText().getText().toString();
        String note = editNote.getText().toString();

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

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

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
}
