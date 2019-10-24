package com.kinsey.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditSuggestActivity extends AppCompatActivity {

    public static final String TAG = "AddEditSuggestActivity";

    public static final String EXTRA_ID =
            "com.kinsey.passwords.EXTRA_ID";
    public static final String EXTRA_PASSWORD =
            "com.kinsey.passwords.EXTRA_PASSWORD";
    public static final String EXTRA_SEQUENCE =
            "com.kinsey.passwords.EXTRA_SEQUENCE";
    public static final String EXTRA_NOTE =
            "com.kinsey.passwords.EXTRA_NOTE";
    public static final String EXTRA_ACTVY_DATE =
            "com.kinsey.passwords.EXTRA_ACTVY_DATE";

    private TextInputLayout textInputPassword;
    private TextInputLayout textInputNote;
    private TextView textActvyDate;
//    private NumberPicker numberPickerPriority;


    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suggest);

        textInputPassword = findViewById(R.id.text_input_password);
        textInputNote = findViewById(R.id.text_input_note);
        textActvyDate = findViewById(R.id.text_actv_date);

//        numberPickerPriority = findViewById(R.id.number_picker_priority);

//        numberPickerPriority.setMinValue(1);
//        numberPickerPriority.setMaxValue(10);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Suggestion");
            textInputPassword.getEditText().setText(intent.getStringExtra(EXTRA_PASSWORD));
            textInputNote.getEditText().setText(intent.getStringExtra(EXTRA_NOTE));
            Long longActvyDate = intent.getLongExtra(EXTRA_ACTVY_DATE, 0L);
            if (longActvyDate == 0) {
                textActvyDate.setText("");
            } else {
                textActvyDate.setText("ActvyDate: " + format_ymdtimehm.format(longActvyDate));
            }

//            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Suggestion");
        }

    }

    private void saveNote() {
        String password = textInputPassword.getEditText().getText().toString().trim();
        String note = textInputNote.getEditText().getText().toString();
//        int priority = numberPickerPriority.getValue();


        if (password.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_PASSWORD, password);
        data.putExtra(EXTRA_NOTE, note);
        data.putExtra(EXTRA_ACTVY_DATE, new Date().getTime());

        int sequence = getIntent().getIntExtra(EXTRA_SEQUENCE, 0);
        if (sequence != 0) {
            data.putExtra(EXTRA_SEQUENCE, sequence);
        }

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

//        Log.d(TAG, "password " + password);

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
                Log.d(TAG, "save password");
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
