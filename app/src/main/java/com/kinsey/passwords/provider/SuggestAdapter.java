package com.kinsey.passwords.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Suggest;

import java.util.ArrayList;
import java.util.List;

public class SuggestAdapter extends ListAdapter<Suggest, SuggestAdapter.SuggestHolder> {

    private OnItemClickListener listener;

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
        holder.textViewDescription.setText(currentSuggest.getNote());
        holder.textViewPriority.setText(String.valueOf(currentSuggest.getSequence()));

    }


    public Suggest getSuggestAt(int position) {
        return getItem(position);
    }


    class SuggestHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public SuggestHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Suggest suggest);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
