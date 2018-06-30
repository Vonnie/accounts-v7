package com.kinsey.passwords;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class FileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share Exported file", Snackbar.LENGTH_LONG)
                        .setAction("Send",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        shareExport();

                                    }
                                }
                                ).show();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager.findFragmentById(R.id.fragment) == null) {
//
//        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_OK);
        finish();
//        return super.onSupportNavigateUp();
        return true;
    }

    private void shareExport() {

        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
//        ArrayList<Uri> uris = new ArrayList<Uri>();
        emailintent.setType("text/html");


        File file = new File(MainActivity.DEFAULT_APP_DIRECTORY,
                "/accounts.json");
        if (!file.exists()) {
            Toast.makeText(FileViewActivity.this,
                    "File not exported to email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        emailintent.putExtra(Intent.EXTRA_SUBJECT, "My Accounts App - import/export file");
//                Uri u = Uri.fromFile(file);
//                uris.add(u);
        emailintent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        emailintent.putExtra(Intent.EXTRA_TEXT, "Exported JSON file");

        emailintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        emailintent.putExtra(Intent.EXTRA_TEXT, "My Accounts Attachments");
        startActivity(Intent.createChooser(emailintent, "Send Email..."));

        Toast.makeText(FileViewActivity.this,
                "Exported file shared",
                Toast.LENGTH_SHORT).show();

    }
}
