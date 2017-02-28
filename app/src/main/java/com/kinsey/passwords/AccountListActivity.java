package com.kinsey.passwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.provider.AccountRecyclerViewAdapter;
import com.kinsey.passwords.tools.AppDialog;

public class AccountListActivity extends AppCompatActivity
        implements AccountRecyclerViewAdapter.OnAccountClickListener,
        AppDialog.DialogEvents {

    private static final String TAG = "AccountListActivity";

    // whether or not the activity is i 2-pane mode
    // i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private OnListClickListener mListener;
    public interface OnListClickListener {
        void onListSuggestsClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new account", Snackbar.LENGTH_LONG)
                        .setAction("Action",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Toast.makeText(AccountListActivity.this,
//                                                "Snackbar action clicked",
//                                                Toast.LENGTH_SHORT).show();
//                                        editAccountRequest(null);
//                                        mListener.onListSuggestsClick();

                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        AppDialog newFragment = AppDialog.newInstance(2, "select action");
                                        newFragment.show(fragmentManager, "dialog");

//                                        Intent returnIntent = new Intent();
//                                        returnIntent.putExtra("result", "open_date");
//                                        setResult(Activity.RESULT_OK, returnIntent);
//                                        finish();
                                    }
                                }
                                ).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        AccountListActivityFragment fragment = new AccountListActivityFragment();
//
//        Bundle arguments = getIntent().getExtras();
////        arguments.putSerializable(Task.class.getSimpleName(), task);
//        fragment.setArguments(arguments);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
////        android.support.v4.app.Fragment frag = fragmentManager.findFragmentById(R.id.fragmentEdit);
////        Log.d(TAG, "onCreate: frag " + frag);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.remove(frag);
////        fragmentTransaction.commit();
//        fragmentTransaction.replace(R.id.fragmentAccount, fragment);
//        fragmentTransaction.commit();
    }

    @Override
    public void onAccountDeleteClick(Account account) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance(1, "hi from dialog");
        newFragment.show(fragmentManager, "dialog");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        AppDialog newFragment = new AppDialog();
//        Bundle args = new Bundle();
//        args.putInt("id", 1);
//        args.putString("message", "hi from dialog");
//        newFragment.setArguments(args);
//        newFragment.show(fragmentManager, "dialog");


////        newFragment.show(getSupportFragmentManager(), "NoticeDialogFragment");
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
////        transaction.add(android.R.id.content, newFragment)
////                .addToBackStack(null).commit();

//        FragmentManager fm = getFragmentManager();
//        AppDialog dialogFragment = new AppDialog ();
//        dialogFragment.show(fm, "Sample Fragment");

//        Intent detailIntent = new Intent(this, AppDialog.class);
//        detailIntent.putExtra("id", 1);
//        detailIntent.putExtra("message", "hi from dialog");
//        startActivity(detailIntent);

//        AppDialog dialog = new AppDialog();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        dialog.show(fragmentManager, "NoticeDialogFragment");
//        getContentResolver().delete(AccountsContract.buildIdUri(account.getId()), null, null);
    }

    @Override
    public void onAccountEditClick(Account account) {
        editAccountRequest(account);
    }

    private void editAccountRequest(Account account) {
        Log.d(TAG, "addAccountRequest: starts");
        if (mTwoPane) {
            Log.d(TAG, "addAccountRequest: in two-pane mode (tablet)");
        } else {
            Log.d(TAG, "addAccountRequest: in single-pan mode (phone)");
            // in single-pane mode, start the detail activity for the selected item Id.
            Intent detailIntent = new Intent(this, AccountActivity.class);

            if (account != null) { // editing an account
                detailIntent.putExtra(Account.class.getSimpleName(), account);
                startActivity(detailIntent);
            } else { // adding an account
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: confirmed to delete");
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: delete cancel");
    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {
        switch (which) {
            case 0:
                editAccountRequest(null);
                break;
            case 4:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("which", which);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default:
        }
    }
}
