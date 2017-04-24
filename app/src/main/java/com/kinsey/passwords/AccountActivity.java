package com.kinsey.passwords;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;
import com.kinsey.passwords.tools.AppDialog;

public class AccountActivity extends AppCompatActivity
        implements AccountActivityFragment.OnActionListener,
        AppDialog.DialogEvents,
        AccountActivity1Fragment.OnSaveClicked {
    private static final String TAG = "AccountActivity";

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


//        AccountActivity1Fragment fragment = new AccountActivity1Fragment();
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragmentEdit, fragment).commit();


//        ==============================================================


//        fragment.setArguments(getIntent().getExtras());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        Log.d(TAG, "onMenuOpened: menuSize " + menu.size());
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            switch (menuItem.getItemId()) {
//                case R.id.menuacct_add:
//                    menuItem.setVisible(false);
//                    Log.d(TAG, "onMenuOpened: set off add");
//                    break;
//                default:
//                    menuItem.setVisible(true);
//                    break;
//            }
//        }
//        return super.onMenuOpened(featureId, menu);
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            switch (menuItem.getItemId()) {
                case R.id.menuacct_add:
                    menuItem.setVisible(false);
                    Log.d(TAG, "onMenuOpened: set off add");
                    break;
                default:
                    menuItem.setVisible(true);
                    break;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        Log.d(TAG, "onOptionsItemSelected: id " + id);
        switch (id) {
            case R.id.menuacct_save:
                saveAccount();
                break;
            case R.id.menuacct_delete:
                deleteAccount();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveAccount() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.Fragment frag = fragmentManager.findFragmentById(R.id.fragmentEdit);
        AccountActivityFragment fragment = (AccountActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentEdit);
        if (fragment.save()) {
            finish();
        }
    }

    private void deleteAccount() {
//        Log.d(TAG, "deleteAccount: ");
        if (account == null) {
            Toast.makeText(this,
                    "account not added in order to delete",
                    Toast.LENGTH_LONG).show();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppDialog newFragment = AppDialog.newInstance();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT);
        args.putInt(AppDialog.DIALOG_TYPE, AppDialog.DIALOG_YES_NO);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message));
        args.putString(AppDialog.DIALOG_SUB_MESSAGE, getString(R.string.deldiag_sub_message, account.getPassportId(), account.getCorpName()));
        args.putInt(AppDialog.DIALOG_ACCOUNT_ID, account.getId());
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

        newFragment.setArguments(args);
        newFragment.show(fragmentManager, "dialog");
    }

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

    @Override
    public void onAccountRetreived(Account account) {
        this.account = account;
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
//        Log.d(TAG, "onPositiveDialogResult: ");
        switch (dialogId) {
            case AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT:
//            Log.d(TAG, "onPositiveDialogResult: confirmed to delete");
//            Log.d(TAG, "onPositiveDialogResult: acctid " + args.getInt(AppDialog.DIALOG_ACCOUNT_ID));
                getContentResolver().delete(AccountsContract.buildIdUri(args.getInt(AppDialog.DIALOG_ACCOUNT_ID)), null, null);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
//        Log.d(TAG, "onNegativeDialogResult: ");
        switch (dialogId) {
            case AppDialog.DIALOG_ID_CONFIRM_DELETE_ACCOUNT:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onActionRequestDialogResult(int dialogId, Bundle args, int which) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }
}

