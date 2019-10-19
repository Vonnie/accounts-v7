package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SearchActivity";

//    public static final Pattern PASSWORD_PATTERN =
//            Pattern.compile(....)

    public static final String EXTRA_SEARCH_CORPNAME =
            "com.kinsey.passwords.SearchActivity";

    private TextInputLayout textInputSearchCorpName;

    private TextView tvActvyDate;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;
    private ImageButton mImgWebView;

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        , @Nullable PersistableBundle persistentState
//        super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        textInputSearchCorpName = findViewById(R.id.text_input_corp_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        setTitle("Enter Corp Name to search");


//        mImgWebView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String corpWebsiteInput = textInputCorpWebsite.getEditText().getText().toString().trim();
//                Intent detailIntent = new Intent(SearchActivity.this, WebViewActivity.class);
//                detailIntent.putExtra(WebViewActivity.class.getSimpleName(), corpWebsiteInput);
//                detailIntent.putExtra(WebViewActivity.EXTRA_CORP_NAME,
//                        textInputCorpName.getEditText().getText().toString().trim());
//                detailIntent.putExtra(WebViewActivity.EXTRA_CORP_WEBSITE,
//                        textInputCorpWebsite.getEditText().getText().toString().trim());
////                Log.d(TAG, "onClick: website " + account.getCorpWebsite());
////                Log.d(TAG, "onClick: wv class " + WebViewActivity.class.getSimpleName());
//                startActivity(detailIntent);
//
//            }
//        });
//
    }

}
