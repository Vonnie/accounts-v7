package com.kinsey.passwords.provider;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<AccountRecyclerViewAdapter.AccountViewHolder>
implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "AcctRecyclerViewAdpt";

    private int mSortorder;
    private Cursor mCursor;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
//    private boolean mTwoPane;
    Account selectedAccount = new Account();
    int selected_position = -1;

    private OnAccountClickListener mListener;

    public interface OnAccountClickListener {
//        void onAccountEditClick(Account account);

//        void onAccountDeleteClick(Account account);

        void onAccountListSelect(Account account);

//        void onAccountLandListSelect(Account account);
    }

    private static String pattern_mdy_display = "M/d/y";
    public static SimpleDateFormat format_mdy_display = new SimpleDateFormat(
            pattern_mdy_display, Locale.US);


    public AccountRecyclerViewAdapter(int sortorder, int selected_position,
                                      Cursor cursor, OnAccountClickListener listener) {
//        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");

//        Log.d(TAG, "AccountRecyclerViewAdapter: twopane " + twoPane);
        Log.d(TAG, "AccountRecyclerViewAdapter: sortorder " + sortorder);

//        mTwoPane = twoPane;
        mSortorder = sortorder;
        this.selected_position = selected_position;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_account_items, parent, false);
//        Log.d(TAG, "AccountRecyclerViewAdapter: cursor " + mCursor.getCount());
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AccountViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: starts");

//        if (mCursor == null) {
//            Log.d(TAG, "onBindViewHolder: mCursor null");
//        } else {
//            Log.d(TAG, "onBindViewHolder: mCursor count " + mCursor.getCount());
//        }
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
            Log.d(TAG, "onBindViewHolder: no accts");
            holder.corp_name.setText(R.string.no_account_items);
//            if (mTwoPane) {
//                holder.user_name.setText(R.string.no_account_items_twopane_line2);
//            } else {
//                holder.user_name.setText(R.string.no_account_items_line2);
//            }
            holder.user_name.setText(R.string.no_account_items_line2);
            holder.open_date.setText("");
//            holder.editButton.setVisibility(View.GONE);
//            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            if(selected_position == position){
                // Here I am just highlighting the background
                holder.itemView.setBackgroundColor(Color.GREEN);
            }else{
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            final Account account = AccountsContract.getAccountFromCursor(mCursor);
            Log.d(TAG, "onBindViewHolder: " + account.getPassportId());
//            final Account account = new Account(mCursor.getInt(mCursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
//                    mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
//                    mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
//                    mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
//                    mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
//                    mCursor.getInt(mCursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
//
//            if (mCursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
//                if (!mCursor.isNull(mCursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
//                    account.setPassportId(mCursor.getInt(mCursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
////                    holder.account_id.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))));
//                }
//            }
//
//            if (mCursor.getColumnIndex(AccountsContract.Columns.NOTE_COL) != -1) {
//                account.setNote(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.NOTE_COL)));
//            }
////            Log.d(TAG, "onBindViewHolder: dte col " + mCursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL));
//            if (mCursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
//                if (mCursor.isNull(mCursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
//                    holder.open_date.setText("");
//                } else {
//                    account.setOpenLong(mCursor.getLong(mCursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
////            Date dte = new Date(item.getActvyLong());
//                    holder.open_date.setText(format_mdy_display.format(account.getOpenLong()));
//                }
//            } else {
//                holder.open_date.setText("");
//            }

//            holder.corp_name.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)));
//            holder.user_name.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)));

//            holder.user_email.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)));
//            holder.corp_.setText(mCursor.getString(mCursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)));

            holder.corp_name.setText(account.getCorpName());
            holder.user_name.setText(account.getUserName());
            if (account.getOpenLong() == 0) {
                holder.open_date.setText("");
            } else {
                //            Date dte = new Date(item.getActvyLong());
                holder.open_date.setText(format_mdy_display.format(account.getOpenLong()));
            }
            holder.seq.setText("Seq:" + String.valueOf(account.getSequence()));
            holder.acctId.setText("AcctId:" + String.valueOf(account.getPassportId()));
//            holder.editButton.setVisibility(View.VISIBLE);
//            holder.deleteButton.setVisibility(View.VISIBLE);

            if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_OPEN_DATE) {
//                holder.user_name.setVisibility(View.GONE);?
                holder.open_date.setVisibility(View.VISIBLE);
                holder.seq.setVisibility(View.GONE);
            } else {
                if (mSortorder == AccountsContract.ACCOUNT_LIST_BY_SEQUENCE) {
//                    holder.user_name.setVisibility(View.GONE);
//                    holder.open_date.setVisibility(View.GONE);
                    holder.seq.setVisibility(View.VISIBLE);
                } else {
                    holder.user_name.setVisibility(View.VISIBLE);
//                    holder.open_date.setVisibility(View.GONE);
//                    holder.seq.setVisibility(View.GONE);
                }
            }



            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) {
                        Log.d(TAG, "onClick: listener is null");
                        return;
                    }
                    notifyItemChanged(selected_position);
                    selected_position = position;
                    notifyItemChanged(selected_position);

//                    if (mTwoPane) {
//                        mListener.onAccountLandListSelect(account);
//                    } else {
//                        mListener.onAccountListSelect(account);
//                    }
                    mListener.onAccountListSelect(account);
                }
            });


//            View.OnClickListener buttonListener = new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
////                    Log.d(TAG, "onClick: starts");
//                    if (mTwoPane) {
//
//                    } else {
//                        switch (view.getId()) {
//                            case R.id.srli_acct_edit:
//                                if (mListener != null) {
////                                Log.d(TAG, "onClick: account " + account);
//                                    mListener.onAccountEditClick(account);
//                                }
//                                break;
//                            case R.id.acc_delete:
//                                if (mListener != null) {
//                                    mListener.onAccountDeleteClick(account);
//                                }
//                                break;
//                            default:
//                                Log.d(TAG, "onClick: found unexpected button id");
//                        }
//                    }
////                    Log.d(TAG, "onClick: button with id " + view.getId() + " clicked");
////                    Log.d(TAG, "onClick: task name is " + task.getName());
//                }
//            };

//            holder.editButton.setOnClickListener(buttonListener);
//            holder.deleteButton.setOnClickListener(buttonListener);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

    public void resetSelection() {
        selected_position = -1;
//        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setSelected_position(int selected_position) {
        this.selected_position = selected_position;
    }

    public int getSelected_position() {
        return selected_position;
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AccountViewHolder";
        public final View mView;
        TextView corp_name = null;
        TextView user_name = null;
        TextView open_date = null;
        TextView seq = null;
        TextView acctId = null;
//        ImageButton editButton = null;
//        ImageButton deleteButton = null;


        public AccountViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "AccountViewHolder: starts");
            mView = itemView;
            this.corp_name = (TextView) itemView.findViewById(R.id.srli_corp_name);
            this.user_name = (TextView) itemView.findViewById(R.id.srli_user_name);
            this.open_date = (TextView) itemView.findViewById(R.id.tli_open_date);
            this.seq = (TextView) itemView.findViewById(R.id.tli_seq);
            this.acctId = (TextView) itemView.findViewById(R.id.tli_acct_id);
//            this.editButton = (ImageButton) itemView.findViewById(R.id.srli_acct_edit);
//            this.deleteButton = (ImageButton) itemView.findViewById(R.id.acc_delete);
        }
    }


}
