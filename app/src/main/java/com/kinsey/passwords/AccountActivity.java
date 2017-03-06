package com.kinsey.passwords;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class AccountActivity extends AppCompatActivity
        implements AccountActivityFragment.OnSaveClicked {
    private static final String TAG = "AccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        AccountActivityFragment fragment = new AccountActivityFragment();
//
//        Bundle arguments = getIntent().getExtras();
////        arguments.putSerializable(Task.class.getSimpleName(), task);
//        fragment.setArguments(arguments);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.Fragment frag = fragmentManager.findFragmentById(R.id.fragmentEdit);
//        Log.d(TAG, "onCreate: frag " + frag);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(frag);
//        fragmentTransaction.commit();
//        fragmentTransaction.replace(R.id.fragmentEdit, fragment);
//        fragmentTransaction.commit();

    }

//    private void closeFragment(int containerId) {
//        try {
//            Fragment fragment = getFragmentManager().findFragmentById(
//                    containerId);
//            if (fragment == null) {
////				Log.v(TAG, "close not found");
//                return;
//            }
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction.remove(fragment);
//            // fragment = new ClosedFrag();
//            // fragmentTransaction.add(R.id.item_detail_container2, fragment);
//            // fragmentTransaction.show(fragment);
//            fragmentTransaction.commit();
//        } catch (Exception e) {
//            Log.e(TAG, "closeFragment error: " + e.getMessage());
//        }
//    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onSaveClicked() {
        setResult(Activity.RESULT_OK);
        finish();
    }

}

