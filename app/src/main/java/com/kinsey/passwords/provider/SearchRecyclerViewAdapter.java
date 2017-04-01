package com.kinsey.passwords.provider;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinsey.passwords.R;
import com.kinsey.passwords.items.Account;
import com.kinsey.passwords.items.AccountsContract;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Yvonne on 2/21/2017.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {
    private static final String TAG = "SrchRecyclerViewAdpt";

    private Context mContext;
    private int mAccountId;
    private Cursor mCursor;
    private OnAccountClickListener mListener;

    public interface OnAccountClickListener {
        void onAccountEditClick(Account account);
        void onAccountDeleteClick(Account account);
    }

    private static String pattern_mdy_display = "M/d/y";
    public static SimpleDateFormat format_mdy_display = new SimpleDateFormat(
            pattern_mdy_display, Locale.US);


    public SearchRecyclerViewAdapter(Context context, int accountId, Cursor cursor, OnAccountClickListener listener) {
//        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mContext = context;
        mAccountId = accountId;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_items, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: starts");

//        if (mCursor == null) {
//            Log.d(TAG, "onBindViewHolder: mCursor null");
//        } else {
//            Log.d(TAG, "onBindViewHolder: mCursor count " + mCursor.getCount());
//        }
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
//            Log.d(TAG, "onBindViewHolder: no search db items");
            holder.corp_name.setText(R.string.no_account_items);
//            holder.user_name.setText("Click Info button to add accounts");
//            holder.editButton.setVisibility(View.GONE);
//            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

//            holder.corp_name.setText(mCursor.getString(mCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
//            holder.userName.setText(mCursor.getString(mCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
//            holder.website.setText(mCursor.getString(mCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2_URL)));

            int dbId;
            if (mAccountId == -1) {
                dbId = mCursor.getInt(mCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA));
            } else {
                dbId = mAccountId;
            }
//            Log.d(TAG, "onBindViewHolder: acctId " + dbId);
//            Log.d(TAG, "onBindViewHolder: column count " + mCursor.getColumnCount());
//            for (int i = 0; i < mCursor.getColumnCount(); i++) {
//                Log.d(TAG, "showSuggestions: " + mCursor.getColumnName(i)
//                        + ": " + mCursor.getString(i));
//            }
//            Log.d(TAG, "searched Accounts: ==========================");
//
//
            Cursor cursorAccount = mContext.getContentResolver().query(
                    AccountsContract.buildIdUri(dbId), null, null, null, null);
            if (cursorAccount.moveToFirst()) {
//                Log.d(TAG, "onBindViewHolder: account found ");

                holder.corp_name.setText(cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)));
                if (cursorAccount.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL) == -1) {
                    holder.website.setVisibility(View.GONE);
                } else {
                    String website = cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL));
                    if (website == null || website.equals("")) {
                        holder.website.setVisibility(View.GONE);
                    } else {
                        holder.website.setText(website);
                    }
                }
                holder.userName.setText(cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)));
                if (cursorAccount.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL) != -1){
//                    Log.d(TAG, "onBindViewHolder: email " + cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)));
                    holder.userEmail.setText(cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)));
                }

                Log.d(TAG, "onBindViewHolder: passportId " + cursorAccount.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL));
                if (cursorAccount.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
                    if (!cursorAccount.isNull(cursorAccount.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
                        holder.acctId.setText("Acct:" + String.valueOf(cursorAccount.getInt(cursorAccount.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))));
                    }
                }

//                if (cursorAccount.getColumnIndex(AccountsContract.Columns.NOTE_COL) == -1) {
//                    holder.note.setVisibility(View.GONE);
//                } else {
//                    String note = cursorAccount.getString(cursorAccount.getColumnIndex(AccountsContract.Columns.NOTE_COL));
//                    if (note == null || note.equals("")) {
//                        holder.note.setVisibility(View.GONE);
//                    } else {
//                        holder.note.setText(note);
//                    }
//                }

            }


//            holder.user_email.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)));
//            holder.corp_.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)));

//            holder.editButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
//            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onClick listener
//
//            View.OnClickListener buttonListener = new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
////                    Log.d(TAG, "onClick: starts");
//                    switch (view.getId()) {
//                        case R.id.srli_acct_edit:
//                            if (mListener != null) {
////                                Log.d(TAG, "onClick: account " + account);
////                                mListener.onAccountEditClick(account);
//                            }
//                            break;
//                        case R.id.srli_acct_delete:
//                            if (mListener != null) {
////                                mListener.onAccountDeleteClick(account);
//                            }
//                            break;
//                        default:
//                            Log.d(TAG, "onClick: found unexpected button id");
//                    }
//                    Log.d(TAG, "onClick: button with id " + view.getId() + " clicked");
////                    Log.d(TAG, "onClick: task name is " + task.getName());
//                }
//            };

//            holder.editButton.setOnClickListener(buttonListener);
//            holder.deleteButton.setOnClickListener(buttonListener);
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

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "SearchViewHolder";

        TextView corp_name = null;
        TextView website = null;
        TextView userName = null;
        TextView userEmail = null;
        TextView acctId = null;
//        TextView note = null;
//        TextView open_date = null;
//        ImageButton editButton = null;
//        ImageButton deleteButton = null;


        public SearchViewHolder(View itemView) {
            super(itemView);
//        Log.d(TAG, "SearchViewHolder: starts");

            this.corp_name = (TextView) itemView.findViewById(R.id.tli_corp_name);
            this.website = (TextView) itemView.findViewById(R.id.srli_user_name);
            this.userName = (TextView) itemView.findViewById(R.id.srli_user_name);
            this.userEmail = (TextView) itemView.findViewById(R.id.srli_user_email);
            this.acctId = (TextView) itemView.findViewById(R.id.srli_account_id);
//            this.note = (TextView) itemView.findViewById(R.id.srli_note);
//            this.editButton = (ImageButton) itemView.findViewById(R.id.srli_acct_edit);
//            this.deleteButton = (ImageButton) itemView.findViewById(R.id.srli_acct_delete);
        }
    }
}
