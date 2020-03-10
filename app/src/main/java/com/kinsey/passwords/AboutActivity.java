package com.kinsey.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {
    public static final String TAG = "AboutActivity";

    private AdView mAdView;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        TextView tvDatabasename = findViewById(R.id.databasename);
        TextView tvVerNo = findViewById(R.id.ver_no);
        TextView tvVerName = findViewById(R.id.ver_name);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//        profileViewModel.getAllProfilesByCorpName().observe(this, (Observer<? super List<Integer>>) new Observer<List<Integer>>() {
        profileViewModel.getCount().observe(this, new Observer<Integer>() {
        @Override
            public void onChanged(Integer count) {
                Log.d(TAG, "onChanged: count " + count);
                String msg = count + " rows on DB " + profileViewModel.dbMsg;
                tvDatabasename.setText(msg);
            }
        });


        Log.d(TAG, "onCreate: dbname " + profileViewModel.dbMsg);
//        tvDatabasename.setText("DB: " + profileViewModel.dbMsg);
        tvVerNo.setText("Ver#: " + BuildConfig.VERSION_CODE);
        tvVerName.setText("VerName: " + BuildConfig.VERSION_NAME);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

//        Log.d(TAG, String.valueOf(BuildConfig.VERSION_CODE));
//        Log.d(TAG, BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.about_title);
    }


    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        setResult(RESULT_CANCELED);
        finish();
        return false;
    }
}
