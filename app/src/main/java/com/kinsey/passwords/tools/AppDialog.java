package com.kinsey.passwords.tools;

//import android.app.AlertDialog;
//import android.app.Dialog;

//import android.app.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;

import com.kinsey.passwords.R;


/**
 * Created by Yvonne on 2/27/2017.
 */

public class AppDialog extends AppCompatDialogFragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_TYPE = "type";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_SUB_MESSAGE = "sub-message";
    public static final String DIALOG_ACCOUNT_ID = "acct_id";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";
    public static final String DIALOG_LIST_POSITION = "position";

    public static final int DIALOG_YES_NO = 1;
    public static final int DIALOG_ACCOUNT_LIST_OPTIONS = 2;
    public static final int DIALOG_ACCOUNT_FILE_OPTIONS = 3;
    public static final int DIALOG_OK = 4;

    public static final int DIALOG_ID_CONFIRM_DELETE_ACCOUNT = 1;
    public static final int DIALOG_ID_CONFIRM_DELETE_PROFILE = 2;
    public static final int DIALOG_ID_REQUEST_GEN_PASSWORD_LENGTH = 3;
    public static final int DIALOG_ID_ACCOUNT_ACTIONS_LIST = 4;
    public static final int DIALOG_ID_ASK_IF_NEED_DICTIONARY_REBUILD = 5;
    public static final int DIALOG_ID_CONFIRM_TO_IMPORT = 6;
    public static final int DIALOG_ID_CONFIRM_TO_EXPORT = 7;
    public static final int DIALOG_ID_ASK_REFRESH_SEARCHDB = 8;
    public static final int DIALOG_ID_CANCEL_EDIT = 9;
    public static final int DIALOG_ID_CANCEL_EDIT_UP = 10;
    public static final int DIALOG_ID_LEAVE_APP = 11;
    public static final int DIALOG_ID_EDITS_APPLIED = 12;
    public static final int DIALOG_ID_EXPORT_FILENAME = 13;
    public static final int DIALOG_ID_CONFIRM_ADD_ACCOUNT = 14;


    public static final int DIALOG_ACCT_LIST_CORP_NAME = 0;
    public static final int DIALOG_ACCT_LIST_OPEN_DATE = 1;
    public static final int DIALOG_ACCT_LIST_ACCT_ID = 2;
    public static final int DIALOG_ACCT_LIST_USER_SEQ = 3;
    public static final int DIALOG_ACCT_LIST_EXPORT = 4;
    public static final int DIALOG_ACCT_LIST_IMPORT = 5;
    public static final int DIALOG_ACCT_LIST_VIEW_EXPORT_FILE = 6;
    public static final int DIALOG_ACCT_LIST_VIEW_SUGGESTIONS = 7;
    public static final int DIALOG_ACCT_SEARCH_REQUEST = 8;

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

    public AppDialog() {

    }

    public static AppDialog newInstance() {
        AppDialog frag = new AppDialog();
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
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        return super.onCreateView(inflater, container, savedInstanceState);
//        return inflater.inflate(R.layout.fragment_edit_name, container);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//
//        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateDialog: starts");

//        String message = getArguments().getString("message");

//        Log.d(TAG, "onCreateDialog: message " + message);
//        mEditText.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle arguments = getArguments();
        final int dialogId;
        final int dialogType;
        final int accountId;
        String messageString;
        String subMessageString;
        int positiveStringId;
        int negativeStringId;

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID);
            dialogType = arguments.getInt(DIALOG_TYPE);
            messageString = arguments.getString(DIALOG_MESSAGE);
            subMessageString = arguments.getString(DIALOG_SUB_MESSAGE);
            accountId = arguments.getInt(DIALOG_ACCOUNT_ID);

            if (dialogId == 0 || dialogType == 0 || messageString == null) {
                throw new IllegalArgumentException("DIALOG_ID, DIALOG_TYPE, and/or DIALOG_MESSAGE not present in the bundle");
            }

        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle");
        }

//        Log.d(TAG, "onCreateDialog: posString " + positiveStringId);
//        Log.d(TAG, "onCreateDialog: negString " + negativeStringId);


        switch (dialogType) {
            case DIALOG_YES_NO: {
                builder.setTitle(messageString);
                positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
                if (positiveStringId == 0) {
                    positiveStringId = R.string.ok;
                }
                negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
                if (negativeStringId == 0) {
                    negativeStringId = R.string.cancel;
                }

                builder.setMessage(subMessageString)
                        .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // callback positive result method
                                if (mDialogEvents != null) {
                                    mDialogEvents.onPositiveDialogResult(dialogId, arguments);
                                }
                            }
                        })
                        .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // callback negative result method
                                if (mDialogEvents != null) {
                                    mDialogEvents.onNegativeDialogResult(dialogId, arguments);
                                }
                            }
                        });
                break;
            }
            case DIALOG_ACCOUNT_LIST_OPTIONS: {
                builder.setTitle(messageString)
                        .setItems(R.array.account_list_options,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        Log.d(TAG, "onClick: which " + which);
                                        mDialogEvents.onActionRequestDialogResult(dialogId, arguments, which);
                                    }
                                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Log.d(TAG, "onClick: negative response");
                        // callback negative result method
                        if (mDialogEvents != null) {
                            mDialogEvents.onNegativeDialogResult(dialogId, arguments);
                        }
                    }
                })
                ;
                break;
            }
            case DIALOG_ACCOUNT_FILE_OPTIONS: {
                builder.setTitle(messageString)
                        .setItems(R.array.file_list_options,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
//                                        Log.d(TAG, "onClick: which " + which);
                                        mDialogEvents.onActionRequestDialogResult(dialogId, arguments, which);
                                    }
                                });
                break;
            }
            case DIALOG_OK: {
                builder.setTitle(messageString);
                positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
                if (positiveStringId == 0) {
                    positiveStringId = R.string.ok;
                }

                builder.setMessage(subMessageString)
                        .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // callback positive result method
                                if (mDialogEvents != null) {
                                    mDialogEvents.onPositiveDialogResult(dialogId, arguments);
                                }
                            }
                        });
                break;
            }
            default:
                break;
        }
//        return super.onCreateDialog(savedInstanceState);
        return builder.create();
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

    @Override
    public void onCancel(DialogInterface dialog) {
//        Log.d(TAG, "onCancel: called");
//        super.onCancel(dialog);
        if (mDialogEvents != null) {
            int dialogId = getArguments().getInt(DIALOG_ID);
            mDialogEvents.onDialogCancelled(dialogId);
        }
    }

}
