package com.jd.living.activity.history;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.SearchHistoryDatabase;
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
    HistoryListAdapter historyListAdapter;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("History", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("History", "onResume");
    }

    @AfterViews
    public void init() {
        Log.d("History", "init");
        setListAdapter(historyListAdapter);
        database.registerSearchHistoryDatabaseListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("History", "onCreateView");
        return inflater.inflate(R.layout.history_list, container, false);
    }

    @ItemClick
    void listItemClicked(SearchHistory searchHistory) {

    }


    @UiThread
    public void update(List<SearchHistory> result) {

        info.setText(getString(R.string.number_of_objects, result.size(), result.size()));
    }

    @Override
    public void onUpdate(List<SearchHistory> searchHistories) {
        Log.d("History", "onUpdate " + searchHistories.size());
        update(searchHistories);
    }
}

