package com.kinsey.passwords;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.SuggestViewModel;
import com.kinsey.passwords.provider.SuggestAdapter;

import java.util.List;

public class SuggestListActivity extends AppCompatActivity {
    public static final int ADD_SUGGEST_REQUEST = 1;
    public static final int EDIT_SUGGEST_REQUEST = 2;

    private SuggestViewModel suggestViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_list);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_suggest);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestListActivity.this, AddEditSuggestActivity.class);
                startActivityForResult(intent, ADD_SUGGEST_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final SuggestAdapter adapter = new SuggestAdapter();
        recyclerView.setAdapter(adapter);

//        suggestViewModel = ViewModelProviders.of(this).get(SuggestViewModel.class);
        suggestViewModel = new ViewModelProvider(this).get(SuggestViewModel.class);
        suggestViewModel.getAllSuggests().observe(this, new Observer<List<Suggest>>() {
            @Override
            public void onChanged(@Nullable List<Suggest> suggests) {
//                update RecyclerView
//                Toast.makeText(SuggestListActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
                adapter.submitList(suggests);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                suggestViewModel.delete(adapter.getSuggestAt(viewHolder.getAdapterPosition()));
                Toast.makeText(SuggestListActivity.this, "Suggestion deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new SuggestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Suggest suggest) {
                Intent intent = new Intent(SuggestListActivity.this, AddEditSuggestActivity.class);
                intent.putExtra(AddEditSuggestActivity.EXTRA_ID, suggest.getId());
                intent.putExtra(AddEditSuggestActivity.EXTRA_TITLE, suggest.getPassword());
                intent.putExtra(AddEditSuggestActivity.EXTRA_DESCRIPTION, suggest.getNote());
                intent.putExtra(AddEditSuggestActivity.EXTRA_PRIORITY, suggest.getSequence());

                startActivityForResult(intent, EDIT_SUGGEST_REQUEST);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SUGGEST_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditSuggestActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditSuggestActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditSuggestActivity.EXTRA_PRIORITY, 1);

            Suggest suggest = new Suggest(title, priority, description );
            suggestViewModel.insert(suggest);

            Toast.makeText(this, "Suggest Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_SUGGEST_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditSuggestActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Suggest can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditSuggestActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditSuggestActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditSuggestActivity.EXTRA_PRIORITY, 1);

            Suggest suggest = new Suggest(title, priority, description);
            suggest.setId(id);
            suggestViewModel.update(suggest);

            Toast.makeText(this, "Suggestion updated", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Suggest not saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_suggest_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_suggestions:
                suggestViewModel.deleteAllSuggests();
                Toast.makeText(this, "All suggestions deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
