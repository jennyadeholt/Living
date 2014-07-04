package com.jd.living.dialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.ormlite.SearchHistory;


public class RunSearchDialogFragment extends DialogFragment {


    private DatabaseHelper databaseHelper;

    private SearchHistory searchHistory;


    public RunSearchDialogFragment(DatabaseHelper databaseHelper, SearchHistory searchHistory) {
        this.databaseHelper = databaseHelper;
        this.searchHistory = searchHistory;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_update_search_text)
                .setTitle(R.string.dialog_update_search_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Search", "onClick");
                        databaseHelper.launchSearch(searchHistory);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
