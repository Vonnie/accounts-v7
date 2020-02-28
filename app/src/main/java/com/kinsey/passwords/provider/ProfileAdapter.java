package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class ProfileAdapter extends ListAdapter<Profile, ProfileAdapter.ProfileHolder> {
    private static final String TAG = "ProfileAdapter";
    //    private List<Profile> profiles = new ArrayList<Profile>();

    private int selectedId = -1;
    private Context context;

    private static String pattern_mdy = "MM/dd/yyyy";
    public static SimpleDateFormat format_mdy = new SimpleDateFormat(
            pattern_mdy, Locale.US);

    private OnItemClickListener listener;

    public ProfileAdapter(int selectedId) {
        super(DIFF_CALLBACK);
        this.selectedId = selectedId;
    }

    private static final DiffUtil.ItemCallback<Profile> DIFF_CALLBACK = new DiffUtil.ItemCallback<Profile>() {

        @Override
        public boolean areItemsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem.getCorpName().equals(newItem.getCorpName()) &&
                    oldItem.getOpenLong() == newItem.getOpenLong();
        }
    };

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        this.context = parent.getContext();
        return new ProfileHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        Profile currentProfile = getItem(position);
        holder.tvCorpName.setText(currentProfile.getCorpName());
//        holder.tvAcctId.setText(String.valueOf(currentProfile.getId()));
//        holder.tvAcctId.setText(String.valueOf(currentProfile.getPassportId()));

//        holder.itemView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryDarkColor));
//        Context context = getApplicationContext();
        if (this.context == null) {
            Log.d(TAG, "onBindViewHolder: null context");
        }
//        holder.itemView.setBackgroundColor(
//                ContextCompat.getColor(this.context, R.color.primaryColor)
//        );
        if (currentProfile.getPassportId() == selectedId) {
//            context.getResources().getColor(R.color.primaryLightColor, context.getResources().newTheme());
//            holder.itemView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryLightColor));
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(this.context, R.color.secondaryColor)
            );
            holder.tvCorpName.setTextColor(
                    ContextCompat.getColor(this.context, R.color.secondaryTextColor)
            );
            holder.tvAcctId.setTextColor(
                    ContextCompat.getColor(this.context, R.color.secondaryTextColor)
            );
        } else {
//            ContextCompat.getColor(context, R.color.primaryColor);
//            holder.itemView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryDarkColor));
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(this.context, R.color.primaryColor)
            );
            holder.tvCorpName.setTextColor(
                    ContextCompat.getColor(this.context, R.color.primaryTextColor)
            );
            holder.tvAcctId.setTextColor(
                    ContextCompat.getColor(this.context, R.color.primaryTextColor)
            );
        }

//        textViewcolor.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorPrimary));

        if (MainActivity.listsortOrder == MainActivity.LISTSORT_CORP_NAME ||
        MainActivity.listsortOrder == MainActivity.LISTSORT_PASSPORT_ID) {
            holder.tvAcctId.setText(String.valueOf(currentProfile.getPassportId()));
        } else {
            if (MainActivity.listsortOrder == MainActivity.LISTSORT_OPEN_DATE) {
                long lngOpenDate = currentProfile.getOpenLong();
                if (lngOpenDate == 0) {
                    holder.tvAcctId.setText("");
                } else {
                    holder.tvAcctId.setText(format_mdy.format(lngOpenDate));
                }
            } else {
                if (MainActivity.listsortOrder == MainActivity.LISTSORT_CUSTOM_SORT) {
                    holder.tvAcctId.setText("");
                } else {
                    holder.tvAcctId.setText(String.valueOf(currentProfile.getSequence()));
                }
            }
        }


    }

    public Profile getProfileAt(int position) {
        if (position >= getItemCount()) {
            if (getItemCount() == 0) {
                return null;
            } else {
                return getItem(getItemCount() - 1);
            }
        } else {
            if (position < 0) {
                if (getItemCount() == 0) {
                    return null;
                } else {
                    return getItem(0);
                }
            } else {
                return getItem(position);
            }
        }
    }

//    public static int getColorWrapper(Context context, int id) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return context.getColor(id);
//        } else {
//            //noinspection deprecation
//            return context.getResources().getColor(id);
//        }
//    }



    class ProfileHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tvAcctId;
        private TextView tvCorpName;


        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvAcctId = itemView.findViewById(R.id.acct_id);
            tvCorpName = itemView.findViewById(R.id.corp_name);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        Profile profileItem = getItem(position);
                        selectedId = profileItem.getPassportId();
                        Log.d(TAG, "onClick: selected Id " + selectedId +
                                " " + profileItem.getCorpName() +
                                " " + profileItem.getPassportId());
                        listener.onItemClick(profileItem);
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

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }
}
