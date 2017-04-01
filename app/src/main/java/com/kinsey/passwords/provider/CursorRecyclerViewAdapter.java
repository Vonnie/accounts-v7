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
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.items.SuggestsContract;

/**
 * Created by Yvonne on 2/21/2017.
 */

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.PasswordViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";

    private Cursor mCursor;
    private OnSuggestClickListener mListener;

    public interface OnSuggestClickListener {

        void onSuggestUpClick(Suggest suggest);

        void onSuggestDownClick(Suggest suggest);

        void onSuggestDeleteClick(Suggest suggest);
    }


    public CursorRecyclerViewAdapter(Cursor cursor, OnSuggestClickListener listener) {
//        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_items, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: starts");

//        if (mCursor == null) {
//            Log.d(TAG, "onBindViewHolder: mCursor null");
//        } else {
//            Log.d(TAG, "onBindViewHolder: mCursor count " + mCursor.getCount());
//        }
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
//            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.password.setText(R.string.generate_passwords_instruction);
            holder.upButton.setVisibility(View.GONE);
            holder.downButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            final Suggest suggest = new Suggest(mCursor.getInt(mCursor.getColumnIndex(SuggestsContract.Columns._ID_COL)),
                    mCursor.getString(mCursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)),
                    mCursor.getInt(mCursor.getColumnIndex(SuggestsContract.Columns.SEQUENCE_COL)));

            holder.password.setText(mCursor.getString(mCursor.getColumnIndex(SuggestsContract.Columns.PASSWORD_COL)));
            holder.upButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
            holder.downButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onClick listener

            holder.password.setText(suggest.getPassword());
            holder.upButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
            holder.downButton.setVisibility(View.VISIBLE);  // TODO add onClick listener
            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onClick listener

            View.OnClickListener buttonListener = new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    Log.d(TAG, "onClick: starts");
                    switch (view.getId()) {
                        case R.id.srli_acct_edit:
                            if (mListener != null) {
                                mListener.onSuggestUpClick(suggest);
                            }
                            break;
                        case R.id.tli_suggest_down:
                            if (mListener != null) {
                                mListener.onSuggestDownClick(suggest);
                            }
                            break;
                        case R.id.acc_delete:
                            if (mListener != null) {
                                mListener.onSuggestDeleteClick(suggest);
                            }
                            break;
                        default:
                            Log.d(TAG, "onClick: found unexpected button id");
                    }
//                    Log.d(TAG, "onClick: button with id " + view.getId() + " clicked");
//                    Log.d(TAG, "onClick: task name is " + task.getName());
                }
            };

            holder.upButton.setOnClickListener(buttonListener);
            holder.downButton.setOnClickListener(buttonListener);
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

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "PasswordViewHolder";

        TextView password = null;
        ImageButton upButton = null;
        ImageButton downButton = null;
        ImageButton deleteButton = null;


        public PasswordViewHolder(View itemView) {
            super(itemView);
//        Log.d(TAG, "TaskViewHolder: starts");

            this.password = (TextView) itemView.findViewById(R.id.tli_password);
            this.upButton = (ImageButton) itemView.findViewById(R.id.srli_acct_edit);
            this.downButton = (ImageButton) itemView.findViewById(R.id.tli_suggest_down);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.acc_delete);
        }
    }
}
