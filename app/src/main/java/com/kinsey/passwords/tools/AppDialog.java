package com.kinsey.passwords.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;

import com.kinsey.passwords.R;

/**
 * Created by Yvonne on 2/27/2017.
 */

public class AppDialog extends DialogFragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    public static final int DIALOG_YES_NO = 1;
    public static final int DIALOG_ACCOUNT_LIST_OPTIONS = 2;

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

    public AppDialog() {

    }

    public static AppDialog newInstance(int dialogId, String messageString) {
        AppDialog frag = new AppDialog();
        Bundle args = new Bundle();
        args.putInt("id", dialogId);
        args.putString("message", messageString);
        frag.setArguments(args);
        return frag;
    }

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
        Log.d(TAG, "onCreateDialog: starts");

        String message = getArguments().getString("message");

        Log.d(TAG, "onCreateDialog: message " + message);
//        mEditText.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(message);

        final Bundle arguments = getArguments();
        final int dialogId;
        String messageString;
        int positiveStringId;
        int negativeStringId;

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID);
            messageString = arguments.getString(DIALOG_MESSAGE);

            if (dialogId == 0 || messageString == null) {
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in the bundle");
            }

            positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
            if (positiveStringId == 0) {
                positiveStringId = R.string.ok;
            }
            negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
            if (negativeStringId == 0) {
                negativeStringId = R.string.cancel;
            }
        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle");
        }

        Log.d(TAG, "onCreateDialog: posString " + positiveStringId);
        Log.d(TAG, "onCreateDialog: negString " + negativeStringId);
        if (dialogId == DIALOG_YES_NO) {
            builder.setMessage(messageString)
                    .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            // callback positive result method
                            mDialogEvents.onPositiveDialogResult(dialogId, arguments);
                        }
                    })
                    .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            // callback negative result method
                            mDialogEvents.onNegativeDialogResult(dialogId, arguments);
                        }
                    });
        } else {
            builder.setTitle(messageString)
                    .setItems(R.array.account_list_options,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Log.d(TAG, "onClick: which " + which);
                                    mDialogEvents.onActionRequestDialogResult(dialogId, arguments, which);
                                }
                            });
        }
//        return super.onCreateDialog(savedInstanceState);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: Entering onAttach, activity is " + context.toString());
        super.onAttach(context);

        // Activities containing this fragment must implement its callbacks.
        if (!(context instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement Appdialog.DialogEvents interface");
        }

        mDialogEvents = (DialogEvents) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: Entering...");
        super.onDetach();

        // Reset the active callbacks interface, because we don't have an activity any longer.
        mDialogEvents = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d(TAG, "onCancel: called");
//        super.onCancel(dialog);
        if (mDialogEvents != null) {
            int dialogId = getArguments().getInt(DIALOG_ID);
            mDialogEvents.onDialogCancelled(dialogId);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss: called");
        super.onDismiss(dialog);
    }
}
