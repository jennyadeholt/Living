package com.jd.living.activity.history;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchHistoryDatabase;
import com.jd.living.dialog.VerificationDialogFragment;
import com.jd.living.model.ormlite.SearchHistory;


@EFragment
public class HistoryList extends ListFragment implements SearchHistoryDatabase.SearchHistoryDatabaseListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    SearchHistoryDatabase database;

    @Bean
    DatabaseHelper databaseHelper;

    @Bean
    HistoryListAdapter historyListAdapter;


    @AfterViews
    public void init() {
        setListAdapter(historyListAdapter);
        database.registerSearchHistoryDatabaseListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.history_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_history:
                database.clearSearchHistoryDatabase();
                return true;
            case R.id.action_delete_history:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @ItemClick
    void listItemClicked(final SearchHistory searchHistory) {
        VerificationDialogFragment dialogFragment = new VerificationDialogFragment(new VerificationDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
               databaseHelper.launchSearch(searchHistory);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        }, R.string.dialog_update_search_title, R.string.dialog_update_search_text);
        dialogFragment.show(getFragmentManager(), "VerificationDialogFragment");
    }

    @UiThread
    public void update(List<SearchHistory> result) {
        if (!isDetached()) {
            info.setText(getString(R.string.number_of_objects, result.size(), result.size()));
        }
    }

    @Override
    public void onUpdate(List<SearchHistory> searchHistories) {
        update(searchHistories);
    }
}

