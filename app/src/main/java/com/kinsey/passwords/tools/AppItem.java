package com.kinsey.passwords.tools;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kinsey.passwords.R;

/**
 * Created by Yvonne on 2/27/2017.
 */

public class AppItem extends Fragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_TYPE = "type";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_SUB_MESSAGE = "sub-message";
    public static final String DIALOG_ACCOUNT_ID = "acct_id";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    public static final int DIALOG_YES_NO = 1;
    public static final int DIALOG_ACCOUNT_LIST_OPTIONS = 2;
    public static final int DIALOG_ACCOUNT_FILE_OPTIONS = 3;

    public static final int DIALOG_ID_CONFIRM_DELETE_ACCOUNT = 1;
    public static final int DIALOG_ID_REQUEST_GEN_PASSWORD_LENGTH = 2;
    public static final int DIALOG_ID_ACCOUNT_ACTIONS_LIST = 3;
    public static final int DIALOG_ID_ASK_IF_NEED_DICTIONARY_REBUILD = 4;
    public static final int DIALOG_ID_CONFIRM_TO_IMPORT = 6;
    public static final int DIALOG_ID_CONFIRM_TO_EXPORT = 7;
    public static final int DIALOG_ID_ASK_REFRESH_SEARCHDB = 8;
    public static final int DIALOG_ID_CANCEL_EDIT = 9;
    public static final int DIALOG_ID_CANCEL_EDIT_UP = 10;
    public static final int DIALOG_ID_LEAVE_APP = 11;


    public static final int DIALOG_ACCT_LIST_CORP_NAME = 0;
    public static final int DIALOG_ACCT_LIST_OPEN_DATE = 1;
    public static final int DIALOG_ACCT_LIST_ACCT_ID = 2;
    public static final int DIALOG_ACCT_LIST_USER_SEQ = 3;
    public static final int DIALOG_ACCT_LIST_EXPORT = 4;
    public static final int DIALOG_ACCT_LIST_IMPORT = 5;
    public static final int DIALOG_ACCT_LIST_VIEW_EXPORT_FILE = 6;
    public static final int DIALOG_ACCT_LIST_VIEW_SUGGESTIONS = 7;
    public static final int DIALOG_ACCT_SEARCH_REQUEST = 8;

    private EditText mEditText;

    /**
     * The dialogue's callback interface to notify of user selected results (deletion confirmed, etc.).
     */
    public interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);
        void onNegativeDialogResult(int dialogId, Bundle args);
        void onActionRequestDialogResult(int dialogId, Bundle args, int which);
        void onDialogCancelled(int dialogId);
//        void onDeleteConfirmed();
//        void onExitConfirmed();
//        void onDeleteTimings();
//        void onCancelled();
    }

    private DialogEvents mDialogEvents;

    public AppItem() {

    }

    public static AppItem newInstance() {
        AppItem frag = new AppItem();
        return frag;
    }

//    public static AppDialog newInstance(int dialogId, String messageString) {
//        AppDialog frag = new AppDialog();
//        Bundle args = new Bundle();
//        args.putInt("id", dialogId);
//        args.putString("message", messageString);
//        frag.setArguments(args);
//        return frag;
//    }
//
//    public static AppDialog newInstance(int dialogId, String messageString, int acctId) {
//        AppDialog frag = new AppDialog();
//        Bundle args = new Bundle();
////        args.putInt("id", dialogId);
////        args.putString("message", messageString);
//        args.putInt(DIALOG_ACCOUNT_ID, acctId);
//        frag.setArguments(args);
//        return frag;
//    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onAttach(Context context) {
//        Log.d(TAG, "onAttach: Entering onAttach, activity is " + context.toString());
        super.onAttach(context);

        // Activities containing this fragment must implement its callbacks.
        if (!(context instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement Appdialog.DialogEvents interface");
        }

        mDialogEvents = (DialogEvents) context;
    }

    @Override
    public void onDetach() {
//        Log.d(TAG, "onDetach: Entering...");
        super.onDetach();

        // Reset the active callbacks interface, because we don't have an activity any longer.
        mDialogEvents = null;
    }

}
