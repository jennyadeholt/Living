package com.jd.living.activity.history;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchHistoryDatabase;
import com.jd.living.dialog.RunSearchDialogFragment;
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
        return inflater.inflate(R.layout.history_list, container, false);
    }

    @ItemClick
    void listItemClicked(SearchHistory searchHistory) {
        RunSearchDialogFragment dialogFragment = new RunSearchDialogFragment(databaseHelper, searchHistory);
        dialogFragment.show(getFragmentManager(), "RunSearchDialogFragment");
    }

    @UiThread
    public void update(List<SearchHistory> result) {
        info.setText(getString(R.string.number_of_objects, result.size(), result.size()));
    }

    @Override
    public void onUpdate(List<SearchHistory> searchHistories) {
        update(searchHistories);
    }
}

