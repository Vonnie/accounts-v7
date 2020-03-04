package com.kinsey.passwords.provider;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Profile;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends ArrayAdapter<Profile> {
    private Context context;
    private int resourceId;
    private List<Profile> items, tempItems, suggestions;

    public SearchListAdapter(@NonNull Context context, int resourceId, int textViewResourceId,
                             @NonNull List<Profile> items) {
        super(context, resourceId, textViewResourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            Profile profile = getItem(position);
            TextView tvAcctId = view.findViewById(R.id.acct_id);
            TextView tvCorpName = view.findViewById(R.id.corp_name);
            tvAcctId.setText(profile.getPassportId());
            tvCorpName.setText(profile.getCorpName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Nullable
    @Override
    public Profile getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
//        return super.getItemId(position);
        return items.get(position).getPassportId();
    }

//    private Filter profileFilter = new Filter() {
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            Profile profile = (Profile) resultValue;
//            return profile.getCorpName();
//        }
//    };
//
//    protected FilterResults performFiltering(CharSequence charSequence) {
//        if (charSequence != null) {
//            suggestions.clear();
//            for (Profile profile : tempItems) {
//                if (profile.getCorpName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
//                    suggestions.add(profile);
//                }
//            }
//        }
//    }
//
//    Filter.FilterResults filterResults = new FilterResults();
//    filterResults.values = suggestions;
//    filterResults.count = suggestions.size();
//return filterResults;
//} else {
//        return new FilterResults();
//        }
//        }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return super.getFilter();
//    }
}
