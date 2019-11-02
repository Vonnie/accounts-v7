package com.kinsey.passwords;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.SuggestViewModel;
import com.kinsey.passwords.provider.SuggestAdapter;
import com.kinsey.passwords.tools.ItemTouchHelperAdapter;
import com.kinsey.passwords.tools.PasswordFormula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class SuggestListActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    public static final String TAG = "SuggestListActivity";
    public static final int ADD_SUGGEST_REQUEST = 1;
    public static final int EDIT_SUGGEST_REQUEST = 2;

    private SuggestViewModel suggestViewModel;
    private PasswordFormula passwordFormula = new PasswordFormula();
    private List<Suggest> suggestList;
    GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;

    //    private List<Suggest> suggestListFull;
//    private int maxSeq = 0;
    Suggest suggestMaxItem;

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

        boolean isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if (isLandscape) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }

        recyclerView.setLayoutManager(layoutManager);


        final SuggestAdapter adapter = new SuggestAdapter();
        recyclerView.setAdapter(adapter);

//        suggestViewModel = ViewModelProviders.of(this).get(SuggestViewModel.class);
        suggestViewModel = new ViewModelProvider(this).get(SuggestViewModel.class);
        suggestViewModel.getAllSuggests().observe(this, new Observer<List<Suggest>>() {
            @Override
            public void onChanged(@Nullable List<Suggest> suggests) {
//                update RecyclerView
//                Toast.makeText(SuggestListActivity.this, "onChanged", Toast.LENGTH_SHORT).show();

                suggestList = new ArrayList<>(suggests);
                adapter.submitList(suggests);
//                Log.d(TAG, "suggests size " + suggestListFull.size());
            }
        });

//        Log.d(TAG, "suggests size " + suggestListFull.size());

        suggestViewModel.getMaxSequence().observe(this, new Observer<Suggest>() {
            @Override
            public void onChanged(@Nullable Suggest suggest) {
//                update RecyclerView
//                Toast.makeText(SuggestListActivity.this, "onChanged", Toast.LENGTH_SHORT).show();


//                adapter.submitList(suggests);

                Log.d(TAG, "max Item " + suggest);

                if (suggest == null) {
                    suggestMaxItem = new Suggest();
                } else {
                    suggestMaxItem = new Suggest(
                            suggest.getPassword(),
                            suggest.getSequence()
                    );
                    //            suggest.setActvyLong();
                }

//                this.maxSeq = suggestMaxItem.getSequence();

//                Log.d(TAG, "new seq " + this.maxSeq);


//                Suggest suggestItem = new Suggest(password, suggest.getSequence() + 1, new Date().getTime());
//                suggestItem.setNote(note);
//
//
//                suggestViewModel.insert(suggestItem);
//
//                Toast.makeText(SuggestListActivity.this, "Suggest Added", Toast.LENGTH_SHORT).show();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //            ItemTouchHelper itemTouchHelper;
            RecyclerView.ViewHolder fromViewHolder;
            RecyclerView.ViewHolder toViewHolder;
            int maxPos = 0, minPos = 0;
//            List<Integer> highlightPos;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
//                Log.d(TAG, "onMovePos * * * *");
//                Log.d(TAG, "onMovePos " + viewHolder.getAdapterPosition() + ":" + target.getAdapterPosition());

//                viewHolder.itemView.setBackgroundColor(
//                        ContextCompat.getColor(getApplicationContext(), R.color.secondaryLightColor)
//                );


//                if (fromSuggest == null) {
//                    fromSuggest = suggest;
//                    return false;
//                }
//                if (fromSuggest.getId() == suggest.getId()) {
//                    return false;
//                }


//                fromSuggest = suggest;
//                suggestViewModel.delete(adapter.getSuggestAt(viewHolder.getAdapterPosition()));
//                suggestViewModel.insert(suggest);

//                if (target == null) {
//                    Log.d(TAG, "target is null");
//                }

                if (target.getAdapterPosition() < minPos) {
                    minPos = target.getAdapterPosition();
                }
                if (viewHolder.getAdapterPosition() < minPos) {
                    minPos = viewHolder.getAdapterPosition();
                }

                if (target.getAdapterPosition() > maxPos) {
                    maxPos = target.getAdapterPosition();
                }
                if (viewHolder.getAdapterPosition() > maxPos) {
                    maxPos = viewHolder.getAdapterPosition();
                }

                return true;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "swipe direction " + direction);
//                Log.d(TAG, "view " + );
                suggestViewModel.delete(adapter.getSuggestAt(viewHolder.getAdapterPosition()));
                Toast.makeText(SuggestListActivity.this, "Suggestion deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
//                Log.d(TAG, "onMoved");

                if (toViewHolder != null) {
                    toViewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.primaryDarkColor)
                    );
                }

                target.itemView.setBackgroundColor(
                        ContextCompat.getColor(getApplicationContext(), R.color.secondaryDarkColor)
                );

                toViewHolder = target;
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.d(TAG, "selected changed " + actionState);
                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.secondaryLightColor)
                    );
                    fromViewHolder = viewHolder;
                    minPos = adapter.getItemCount();
                    maxPos = 0;

                    if (viewHolder.getAdapterPosition() < minPos) {
                        minPos = viewHolder.getAdapterPosition();
                    }

                    if (viewHolder.getAdapterPosition() > maxPos) {
                        maxPos = viewHolder.getAdapterPosition();
                    }

                } else {
                    if (fromViewHolder == null) {
                        return;
                    }
                    if (toViewHolder == null) {
                        return;
                    }
                    fromViewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.backgroundTransparent)
                    );
                    toViewHolder.itemView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.backgroundTransparent)
                    );
                    int fromPos = fromViewHolder.getAdapterPosition();
                    int toPos = toViewHolder.getAdapterPosition();
                    if (fromPos == toPos) {
                        return;
                    }


                    Log.d(TAG, "from:to " + fromPos + ":" + toPos);

                    Suggest reposSuggest = adapter.getSuggestAt(fromViewHolder.getAdapterPosition());

                    int lowPos = fromPos < toPos ? fromPos : toPos;
                    int highPos = fromPos > toPos ? fromPos : toPos;
                    Log.d(TAG, "low:high " + lowPos + ":" + highPos);

                    int nextSeq = -1;
                    if (lowPos == 0) {
                        nextSeq = 1;
                    } else {
                        Suggest suggestNext = adapter.getSuggestAt(lowPos);
                        nextSeq = suggestNext.getSequence();
                    }

                    List<Suggest> modifySuggestList = new ArrayList<Suggest>();
                    int currentPos = lowPos;

                    if (currentPos == toPos) {
                        reposSuggest.setSequence(nextSeq);
                        modifySuggestList.add(reposSuggest);
                        nextSeq += 1;
                        while (currentPos < highPos) {
                            Suggest suggestSeq = adapter.getSuggestAt(currentPos);
                            suggestSeq.setSequence(nextSeq);
                            modifySuggestList.add(suggestSeq);
                            currentPos += 1;
                            nextSeq += 1;
                        }
                    } else {
                        currentPos += 1;
                        while (currentPos < highPos) {
                            Suggest suggestSeq = adapter.getSuggestAt(currentPos);
                            suggestSeq.setSequence(nextSeq);
                            modifySuggestList.add(suggestSeq);
                            nextSeq += 1;
                            currentPos += 1;
                        }
                        reposSuggest.setSequence(nextSeq);
                        modifySuggestList.add(reposSuggest);
                    }


                    for (Suggest item : modifySuggestList) {
                        suggestViewModel.update(item);
                    }
//                        suggest.setSequence(toSeq);
//                        suggestTarget.setSequence(fromSeq);
//                        Log.d(TAG, "onMovePos " + fromViewHolder.getAdapterPosition() + ":" + toViewHolder.getAdapterPosition());
//                        Log.d(TAG, "onMovePswd " + suggest.getPassword() + ":" + suggestTarget.getPassword());
//                        Log.d(TAG, "onMoveId " + suggest.getId() + ":" + suggestTarget.getId());
//                        Log.d(TAG, "onMoveSeq " + suggest.getSequence() + ":" + suggestTarget.getSequence());
//                        Log.d(TAG, "notifyPos " + fromPos + ":" + toPos);
//
//                        suggestViewModel.update(suggest);
//                        suggestViewModel.update(suggestTarget);

                    Log.d(TAG, "min:max " + minPos + ":" + maxPos);
//                        adapter.notifyItemMoved(minPos, maxPos);
//                        adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeChanged(minPos, maxPos);

                    fromViewHolder = null;
                }
                super.onSelectedChanged(viewHolder, actionState);
            }


            //            @Override
//            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
//                Log.d(TAG, "canDropOver " + current.getAdapterPosition() + ":" + target.getAdapterPosition());
//                return super.canDropOver(recyclerView, current, target);
//            }


//            public void setTouchHelper(ItemTouchHelper itemTouchHelper) {
//                this.itemTouchHelper = itemTouchHelper;
//            }

        }).attachToRecyclerView(recyclerView);


//        gestureDetector = new GestureDetectorCompat(this,this);

//        gestureDetector = new GestureDetectorCompat(this, this);

        //        new ItemTouchHelperAdapter(new ItemTouchHelperAdapter() {
//
//            @Override
//            public void onItemMove(int fromPosition, int toPosition) {
//                Suggest suggest = adapter.getSuggestAt(fromPosition);
//                suggestViewModel.delete(adapter.getSuggestAt(fromPosition));
//                suggestViewModel.insert(suggest);
//                adapter.notifyItemMoved(fromPosition, toPosition);
//            }
//
//            @Override
//            public void onItemSwiped(int position) {
////                Log.d(TAG, "direction " + direction);
//                suggestViewModel.delete(adapter.getSuggestAt(position));
//                Toast.makeText(SuggestListActivity.this, "Suggestion deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
////        .attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new SuggestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Suggest suggest) {
                Intent intent = new Intent(SuggestListActivity.this, AddEditSuggestActivity.class);
                intent.putExtra(AddEditSuggestActivity.EXTRA_ID, suggest.getId());
                intent.putExtra(AddEditSuggestActivity.EXTRA_PASSWORD, suggest.getPassword());
                intent.putExtra(AddEditSuggestActivity.EXTRA_SEQUENCE, suggest.getSequence());
                intent.putExtra(AddEditSuggestActivity.EXTRA_NOTE, suggest.getNote());
//                intent.putExtra(AddEditSuggestActivity.EXTRA_ACTVY_DATE, suggest.getActvyDate().getTime());
                intent.putExtra(AddEditSuggestActivity.EXTRA_ACTVY_DATE_LONG, suggest.getActvyDate().getTime());
                startActivityForResult(intent, EDIT_SUGGEST_REQUEST);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SUGGEST_REQUEST && resultCode == RESULT_OK) {
            String password = data.getStringExtra(AddEditSuggestActivity.EXTRA_PASSWORD);
            String note = data.getStringExtra(AddEditSuggestActivity.EXTRA_NOTE);
            Long actvyDate = data.getLongExtra(AddEditSuggestActivity.EXTRA_ACTVY_DATE_LONG, 0);

//            requestAddSuggest(password, note);

            Log.d(TAG, "max seq " + suggestMaxItem.getSequence());

//            Suggest newSuggestItem = new Suggest(password, suggestMaxItem.getSequence() + 1, new Date(actvyDate));
            Suggest newSuggestItem = new Suggest(password, suggestMaxItem.getSequence() + 1);
            newSuggestItem.setNote(note);
            newSuggestItem.setActvyDate(new Date(actvyDate));

            suggestViewModel.insert(newSuggestItem);

            Toast.makeText(SuggestListActivity.this, "Suggest Added", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_SUGGEST_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditSuggestActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Suggest can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditSuggestActivity.EXTRA_PASSWORD);
            int sequence = data.getIntExtra(AddEditSuggestActivity.EXTRA_SEQUENCE, 0);
            String note = data.getStringExtra(AddEditSuggestActivity.EXTRA_NOTE);
            Long actvyDate = data.getLongExtra(AddEditSuggestActivity.EXTRA_ACTVY_DATE_LONG, 0);
//            int priority = data.getIntExtra(AddEditSuggestActivity.EXTRA_ACTVY_DATE, 1);

//            Suggest suggest = new Suggest(title, sequence, new Date(actvyDate));
            Suggest suggest = new Suggest(title, sequence);
            suggest.setId(id);
            suggest.setNote(note);
            suggest.setActvyDate(new Date(actvyDate));
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
            case R.id.menu_generate10:
                generatePasswords(10);
                return true;
            case R.id.menu_generate8:
                generatePasswords(8);
                return true;
            case R.id.delete_all_suggestions:
                suggestViewModel.deleteAllSuggests();
                Toast.makeText(this, "All suggestions deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void generatePasswords(int passwordLen) {

        int maxSeq = this.suggestMaxItem.getSequence();
        Log.d(TAG, "new max seq " + maxSeq);

        int nbrPasswords = 0;
        while (nbrPasswords < 10) {
//            int iSeq = getMaxValue(SuggestsContract.Columns.SEQUENCE_COL);

            maxSeq += 1;
            Suggest suggest = new Suggest(
                    passwordFormula.createPassword(passwordLen),
                    maxSeq);
//            suggest.setActvyLong();
            suggest.setActvyDate(new Date());

            suggestViewModel.insert(suggest);

            nbrPasswords++;
        }

        Toast.makeText(this, "10 new passwords added", Toast.LENGTH_SHORT).show();
    }


    private void requestAddSuggest(String password, String note) {
//        maxSeq = 0;
//        for (Suggest suggest: suggests) {
//            if (suggest.getSequence() > maxSeq) {
//                maxSeq = suggest.getSequence();
//            }
//        }
//
//        Log.d(TAG, "max " + maxSeq);
////        int maxSeq = suggestViewModel.getMaxSequence();

        Suggest maxSuggest;

        suggestViewModel.getMaxSequence().observe(this, new Observer<Suggest>() {
            @Override
            public void onChanged(@Nullable Suggest suggest) {
//                update RecyclerView
//                Toast.makeText(SuggestListActivity.this, "onChanged", Toast.LENGTH_SHORT).show();


//                adapter.submitList(suggests);

                Log.d(TAG, "max Item " + suggest);

//                this.maxSeq = suggest.getSequence();
//
//                SuggestListActivity.maxSeq = suggest.getSequence();
//
//                maxSuggest = suggest;

            }
        });

//        Log.d(TAG, "max Item " + this.maxSuggest);

        Log.d(TAG, "onChg new max seq " + this.suggestMaxItem.getSequence());
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "single tap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "on down");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

