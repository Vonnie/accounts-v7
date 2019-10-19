package com.kinsey.passwords.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileAdapter extends ListAdapter<Profile, ProfileAdapter.ProfileHolder> {
//    private List<Profile> profiles = new ArrayList<Profile>();

    private OnItemClickListener listener;

    public ProfileAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Profile> DIFF_CALLBACK = new DiffUtil.ItemCallback<Profile>() {

        @Override
        public boolean areItemsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem.getCorpName().equals(newItem.getCorpName()) &&
                    oldItem.getUserName().equals(newItem.getUserName()) &&
                    oldItem.getUserEmail().equals(newItem.getUserEmail());
        }
    };

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new ProfileHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        Profile currentProfile = getItem(position);
        holder.tvCorpName.setText(currentProfile.getCorpName());
        holder.tvAcctId.setText(String.valueOf(currentProfile.getId()));
        holder.tvUserName.setText(currentProfile.getUserName());

    }

    public Profile getProfileAt(int position) {
        return getItem(position);
    }

    class ProfileHolder extends RecyclerView.ViewHolder {
        private TextView tvAcctId;
        private TextView tvCorpName;
        private TextView tvUserName;


        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            tvAcctId = itemView.findViewById(R.id.acct_id);
            tvCorpName = itemView.findViewById(R.id.corp_name);
            tvUserName = itemView.findViewById(R.id.user_name);

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
        void onItemClick(Profile profile);
    }

    public void setOnItemClickListener(ProfileAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
