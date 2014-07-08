package com.jd.living.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class VerificationDialogFragment extends DialogFragment {


    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private NoticeDialogListener listener;
    private int titleRes;
    private int textRes;

    public VerificationDialogFragment(NoticeDialogListener listener, int titleRes, int textRes) {
        this.listener = listener;
        this.titleRes = titleRes;
        this.textRes = textRes;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(textRes)
                .setTitle(titleRes)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onDialogPositiveClick(VerificationDialogFragment.this);
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onDialogNegativeClick(VerificationDialogFragment.this);
                        }
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

}
