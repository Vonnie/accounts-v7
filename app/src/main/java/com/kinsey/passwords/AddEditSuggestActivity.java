package com.kinsey.passwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public static final String EXTRA_ACTVY_DATE_LONG =
            "com.kinsey.passwords.EXTRA_ACTVY_DATE_LONG";

    private TextInputLayout textInputPassword;
    private TextInputLayout textInputNote;
    private TextView textActvyDate;
//    private NumberPicker numberPickerPriority;

    private int intId = -1;
    private int sequence = -1;
    private long lngActvDate = 0l;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if (savedInstanceState != null) {
            restoreScreen(savedInstanceState);
        } else {

            Intent intent = getIntent();

            if (intent.hasExtra(EXTRA_ID)) {
                intId = getIntent().getIntExtra(EXTRA_ID, -1);
                textInputPassword.getEditText().setText(intent.getStringExtra(EXTRA_PASSWORD));
                textInputNote.getEditText().setText(intent.getStringExtra(EXTRA_NOTE));
                sequence = getIntent().getIntExtra(EXTRA_SEQUENCE, 0);
                lngActvDate = intent.getLongExtra(EXTRA_ACTVY_DATE_LONG, 0L);
                if (lngActvDate == 0) {
                    textActvyDate.setText("");
                } else {
                    textActvyDate.setText(R.string.activity_date + " " + format_ymdtimehm.format(lngActvDate));
                }
//            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            } else {

            }
        }

        if (intId == -1) {
            setTitle(R.string.add_suggestion);
        } else {
            setTitle(R.string.edit_suggestion);
        }
        getSupportActionBar().setLogo(R.drawable.ic_launcher_test2_foreground);
    }


    private void restoreScreen(Bundle savedInstanceState) {
        intId = savedInstanceState.getInt(EXTRA_ID, -1);
        sequence = savedInstanceState.getInt(EXTRA_SEQUENCE, 0);
        textInputPassword.getEditText().setText(savedInstanceState.getString(EXTRA_PASSWORD));
        textInputNote.getEditText().setText(savedInstanceState.getString(EXTRA_NOTE));
        lngActvDate = savedInstanceState.getLong(EXTRA_ACTVY_DATE_LONG, 0);
        if (lngActvDate == 0) {
            textActvyDate.setText("");
        } else {
            textActvyDate.setText(R.string.activity_date + " " + format_ymdtimehm.format(lngActvDate));
        }

    }

    private void saveNote() {
        String password = textInputPassword.getEditText().getText().toString().trim();
        String note = textInputNote.getEditText().getText().toString();
//        int priority = numberPickerPriority.getValue();


        if (password.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_enter_title_descr, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ID, intId);
        data.putExtra(EXTRA_PASSWORD, password);
        data.putExtra(EXTRA_NOTE, note);
        data.putExtra(EXTRA_ACTVY_DATE_LONG, new Date().getTime());
        data.putExtra(EXTRA_SEQUENCE, sequence);

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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_ID, intId);
        outState.putInt(EXTRA_SEQUENCE, sequence);
        outState.putString(EXTRA_PASSWORD, textInputPassword.getEditText().getText().toString().trim());
        outState.putString(EXTRA_NOTE, textInputNote.getEditText().getText().toString().trim());
        outState.putLong(EXTRA_ACTVY_DATE_LONG, lngActvDate);
    }

}
