package com.kinsey.passwords;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddEditProfileActivity extends AppCompatActivity {
    public static final String TAG = "AddEditProfileActivity";

    public static final String EXTRA_ID =
            "com.kinsey.passwords.EXTRA_ID";
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

    private EditText editCorpName;
    private EditText editUserName;
    private EditText editUserEmail;
    private EditText editCorpWebsite;
    private EditText editNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        , @Nullable PersistableBundle persistentState
//        super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addedit_profile);

        Log.d(TAG, "onCreate: activity AddEditProfileActivity");

        editCorpName = findViewById(R.id.edit_corp_name);
        editUserName = findViewById(R.id.edit_user_name);
        editUserEmail = findViewById(R.id.edit_user_email);
        editCorpWebsite = findViewById(R.id.edit_corp_website);
        editNote = findViewById(R.id.edit_notes);

        Log.d(TAG, "onCreate: activity AddEditProfileActivity cols set");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Profile Account");
            editCorpName.setText(intent.getStringExtra(EXTRA_CORP_NAME));
            editUserName.setText(intent.getStringExtra(EXTRA_USER_NAME));
            editUserEmail.setText(intent.getStringExtra(EXTRA_USER_EMAIL));
            editCorpWebsite.setText(intent.getStringExtra(EXTRA_CORP_WEBSITE));
            editNote.setText(intent.getStringExtra(EXTRA_NOTE));
        } else {
            setTitle("Add Profile Account");
        }

    }

    private void saveProfile() {
        String corpName = editCorpName.getText().toString();
        String userName = editUserName.getText().toString();
        String userEmail = editUserEmail.getText().toString();
        String note = editNote.getText().toString();
        String corpWebsite = editCorpWebsite.getText().toString();

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
