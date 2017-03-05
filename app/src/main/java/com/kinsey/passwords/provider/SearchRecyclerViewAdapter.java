package com.kinsey.passwords.provider;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.SearchesContract;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Yvonne on 2/21/2017.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.AccountViewHolder> {
    private static final String TAG = "SuggRecyclerViewAdpt";

    private Cursor mCursor;
    private OnAccountClickListener mListener;

    public interface OnAccountClickListener {
        void onAccountEditClick(Account account);
        void onAccountDeleteClick(Account account);
    }

    private static String pattern_mdy_display = "M/d/y";
    public static SimpleDateFormat format_mdy_display = new SimpleDateFormat(
            pattern_mdy_display, Locale.US);


    public SearchRecyclerViewAdapter(Cursor cursor, OnAccountClickListener listener) {
//        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_list_items, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: starts");

//        if (mCursor == null) {
//            Log.d(TAG, "onBindViewHolder: mCursor null");
//        } else {
//            Log.d(TAG, "onBindViewHolder: mCursor count " + mCursor.getCount());
//        }
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
            Log.d(TAG, "onBindViewHolder: no search db items");
            holder.corp_name.setText(R.string.no_account_items);
//            holder.user_name.setText("Click Info button to add accounts");
            holder.open_date.setText("");
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            holder.corp_name.setText(mCursor.getString(mCursor.getColumnIndex(SearchesContract.Columns.SUGGEST_TEXT_1_COL)));
            holder.corp_name.setText(mCursor.getString(mCursor.getColumnIndex(SearchesContract.Columns.SUGGEST_TEXT_2_COL)));


//            holder.user_email.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)));
//            holder.corp_.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)));
            holder.editButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onClick listener

            View.OnClickListener buttonListener = new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    Log.d(TAG, "onClick: starts");
                    switch (view.getId()) {
                        case R.id.tli_acct_edit:
                            if (mListener != null) {
//                                Log.d(TAG, "onClick: account " + account);
//                                mListener.onAccountEditClick(account);
                            }
                            break;
                        case R.id.tli_acct_delete:
                            if (mListener != null) {
//                                mListener.onAccountDeleteClick(account);
                            }
                            break;
                        default:
                            Log.d(TAG, "onClick: found unexpected button id");
                    }
                    Log.d(TAG, "onClick: button with id " + view.getId() + " clicked");
//                    Log.d(TAG, "onClick: task name is " + task.getName());
                }
            };

            holder.editButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);
        }

    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: starts");
        if (mCursor == null || (mCursor.getCount()) == 0) {
            return 1; // fib, becuase we populate a single view Holder with instructions
        } else {
            return mCursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor,
     * The returned old Cursor is <em>not</em> closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns the previousely set Cursor, or null if there wasn't one.
     * If the given new Cursor is the same instance as the previousel set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AccountViewHolder";

        TextView corp_name = null;
        TextView website = null;
        TextView open_date = null;
        ImageButton editButton = null;
        ImageButton deleteButton = null;


        public AccountViewHolder(View itemView) {
            super(itemView);
        Log.d(TAG, "AccountViewHolder: starts");

            this.corp_name = (TextView) itemView.findViewById(R.id.tli_corp_name);
//            this.user_name = (TextView) itemView.findViewById(R.id.tli_user_name);
            this.editButton = (ImageButton) itemView.findViewById(R.id.tli_acct_edit);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.tli_acct_delete);
        }
    }
}
