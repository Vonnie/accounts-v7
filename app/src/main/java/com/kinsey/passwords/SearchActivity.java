package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.provider.ProfileAdapter;
import com.kinsey.passwords.provider.ProfileViewModel;
import com.kinsey.passwords.provider.SearchAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SearchActivity";

//    public static final Pattern PASSWORD_PATTERN =
//            Pattern.compile(....)

    public static final String EXTRA_SEARCH_CORPNAME =
            "com.kinsey.passwords.SearchActivity";

    public static final int EDIT_PROFILE_REQUEST = 2;


    private TextInputLayout textInputSearchCorpName;

    private SearchAdapter adapter;
//    private ProfileViewModel profileViewModel;

    private TextView tvActvyDate;
    private DatePicker mDtePickOpen;
    private long lngOpenDate;
    private ImageButton mImgWebView;
    ProgressBar progressBar;
    private String searchforValue = "";

    private static String pattern_ymdtimehm = "yyyy-MM-dd kk:mm";
    public static SimpleDateFormat format_ymdtimehm = new SimpleDateFormat(
            pattern_ymdtimehm, Locale.US);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        , @Nullable PersistableBundle persistentState
//        super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_list);

        textInputSearchCorpName = findViewById(R.id.text_input_corp_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
        progressBar  = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        this.adapter = new SearchAdapter();
        recyclerView.setAdapter(adapter);
        searchResults("-1-1");


        textInputSearchCorpName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.d(TAG, "profiles search " + s);
                if (s.length() >= 3) {
                    searchResults(s.toString());
                }
            }
        });

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                Intent intent = new Intent(SearchActivity.this, AddEditProfileActivity.class);
                intent.putExtra(AddEditProfileActivity.EXTRA_ID, profile.getId());
                intent.putExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, profile.getPassportId());
                intent.putExtra(AddEditProfileActivity.EXTRA_SEQUENCE, profile.getSequence());
                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_NAME, profile.getCorpName());
                intent.putExtra(AddEditProfileActivity.EXTRA_USER_NAME, profile.getUserName());
                intent.putExtra(AddEditProfileActivity.EXTRA_USER_EMAIL, profile.getUserEmail());
                intent.putExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE, profile.getCorpWebsite());
                intent.putExtra(AddEditProfileActivity.EXTRA_NOTE, profile.getNote());
                intent.putExtra(AddEditProfileActivity.EXTRA_ACTVY_LONG, profile.getActvyLong());
                intent.putExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, profile.getOpenLong());

                startActivityForResult(intent, EDIT_PROFILE_REQUEST);

            }
        });


        if (savedInstanceState != null) {
            this.searchforValue = savedInstanceState.getString("searchfor");
            textInputSearchCorpName.getEditText().setText(this.searchforValue);
        }
    }

    private void searchResults(String searchfor) {
        this.searchforValue = searchfor;
        String searchforreq = "%" + searchfor + "%";
        MainActivity.profileViewModel.searchCorpNameProfiles(searchforreq).observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                Log.d(TAG, "profiles search len " + profiles.size());
//                profileListFull = new ArrayList<>(profiles);
                adapter.submitList(profiles);
//                setTitle(profiles.size() + " Search Results Items");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case EDIT_PROFILE_REQUEST: {
                int id = data.getIntExtra(AddEditProfileActivity.EXTRA_ID, -1);

                if (id == -1) {
                    Toast.makeText(this, "Profile can't be updated", Toast.LENGTH_SHORT).show();
                    return;
                }

                int passporId = data.getIntExtra(AddEditProfileActivity.EXTRA_PASSPORT_ID, -1);
                int sequence = data.getIntExtra(AddEditProfileActivity.EXTRA_SEQUENCE, 0);
                String corpName = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_NAME);
                String userName = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_NAME);
                String userEmail = data.getStringExtra(AddEditProfileActivity.EXTRA_USER_EMAIL);
                String corpWebsite = data.getStringExtra(AddEditProfileActivity.EXTRA_CORP_WEBSITE);
                String note = data.getStringExtra(AddEditProfileActivity.EXTRA_NOTE);

                Profile profile = new Profile(sequence, corpName, userName, userEmail, corpWebsite);
                profile.setId(id);
                profile.setPassportId(passporId);
                profile.setNote(note);
                long lngDate = data.getLongExtra(AddEditProfileActivity.EXTRA_OPEN_DATE_LONG, 0);
                profile.setOpenLong(lngDate);
                profile.setActvyLong(System.currentTimeMillis());

                MainActivity.profileViewModel.update(profile);
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                break;

            }
            default:
                break;
        }



        }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchfor", this.searchforValue);
    }
}
