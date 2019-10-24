package com.kinsey.passwords.provider;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.SuggestListActivity;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.tools.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.List;

public class SuggestAdapter extends ListAdapter<Suggest, SuggestAdapter.SuggestHolder>
        implements ItemTouchHelperAdapter {

    private OnItemClickListener listener;
    private ItemTouchHelper touchHelper;

    public SuggestAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Suggest> DIFF_CALLBACK = new DiffUtil.ItemCallback<Suggest>() {
        @Override
        public boolean areItemsTheSame(@NonNull Suggest oldItem, @NonNull Suggest newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Suggest oldItem, @NonNull Suggest newItem) {
            return oldItem.getPassword().equals(newItem.getPassword()) &&
                    oldItem.getNote().equals(newItem.getNote()) &&
                    oldItem.getSequence() == newItem.getSequence();
        }
    };

    @NonNull
    @Override
    public SuggestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggest_item, parent, false);
        return new SuggestHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestHolder holder, int position) {
        Suggest currentSuggest = getItem(position);
        holder.textViewTitle.setText(currentSuggest.getPassword());
        holder.textViewDescription.setText(String.valueOf(currentSuggest.getId()));
        holder.textViewPriority.setText(String.valueOf(currentSuggest.getSequence()));

    }


    public Suggest getSuggestAt(int position) {
        return getItem(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Suggest suggest = getItem(fromPosition);

    }

    @Override
    public void onItemSwiped(int position) {

    }


    class SuggestHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnTouchListener,
            GestureDetector.OnGestureListener {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private GestureDetector gestureDetector;

        public SuggestHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

//            gestureDetector = new GestureDetector(SuggestAdapter.this, this) ;

            itemView.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                    int position = getAdapterPosition();
//                    if (listener != null && position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(getItem(position));
//                    }
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(getItem(position));
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
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

    public interface OnItemClickListener {
        void onItemClick(Suggest suggest);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
