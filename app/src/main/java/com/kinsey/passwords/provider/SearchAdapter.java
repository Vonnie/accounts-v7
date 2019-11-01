package com.kinsey.passwords.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;

public class SearchAdapter extends ListAdapter<Profile, SearchAdapter.ProfileHolder> {
//    private List<Profile> profiles = new ArrayList<Profile>();

    private OnItemClickListener listener;

    public SearchAdapter() {
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
                    oldItem.getUserEmail().equals(newItem.getUserEmail()) &&
                    oldItem.getCorpWebsite().equals(newItem.getCorpWebsite());
        }
    };

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ProfileHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        Profile currentProfile = getItem(position);
        holder.tvAcctId.setText(String.valueOf(currentProfile.getPassportId()));
        holder.tvCorpName.setText(currentProfile.getCorpName());
        if (currentProfile.getCorpWebsite().equals("") || currentProfile.getCorpWebsite().equals("http://")) {
            holder.tvCorpWebsite.setVisibility(View.GONE);
        } else {
            holder.tvCorpWebsite.setVisibility(View.VISIBLE);
            holder.tvCorpWebsite.setText(currentProfile.getCorpWebsite());
        }
        holder.tvUserName.setText(currentProfile.getUserName());
        holder.tvUserEmail.setText(currentProfile.getUserEmail());
    }

    public Profile getProfileAt(int position) {
        return getItem(position);
    }

    class ProfileHolder extends RecyclerView.ViewHolder {
        private TextView tvAcctId;
        private TextView tvCorpName;
        private TextView tvCorpWebsite;
        private TextView tvUserName;
        private TextView tvUserEmail;


        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            tvAcctId = itemView.findViewById(R.id.acct_id);
            tvCorpName = itemView.findViewById(R.id.corp_name);
            tvCorpWebsite = itemView.findViewById(R.id.corp_website);
            tvUserName = itemView.findViewById(R.id.user_name);
            tvUserEmail = itemView.findViewById(R.id.user_email);

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

    public void setOnItemClickListener(SearchAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

//            \nSearch DB is an abbreviated copy of the data db.
//    Will need to re-align again if data db has any corp name changes, any add of an account, or any delete of an account.
//    If the app see changes, it will rebuild that search db on the next search. User can ask to rebuild that search db thru the menu.


}
