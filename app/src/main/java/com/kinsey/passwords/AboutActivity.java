package com.kinsey.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    public static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        TextView tvDatabasename = findViewById(R.id.databasename);
        TextView tvVerNo = findViewById(R.id.ver_no);
        TextView tvVerName = findViewById(R.id.ver_name);
        tvDatabasename.setText("DB: " + MainActivity.profileViewModel.dbMsg);
        tvVerNo.setText("Ver#: " + String.valueOf(BuildConfig.VERSION_CODE));
        tvVerName.setText("VerName: " + BuildConfig.VERSION_NAME);
//        Log.d(TAG, String.valueOf(BuildConfig.VERSION_CODE));
//        Log.d(TAG, BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About Accounts App");
    }


    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        setResult(RESULT_CANCELED);
        finish();
        return false;
    }
}
