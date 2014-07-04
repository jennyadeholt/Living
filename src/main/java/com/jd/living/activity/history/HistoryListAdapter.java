package com.jd.living.activity.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jd.living.R;
import com.jd.living.database.SearchHistoryDatabase;
import com.jd.living.model.ormlite.SearchHistory;


@EBean
public class HistoryListAdapter extends ArrayAdapter<SearchHistory> implements SearchHistoryDatabase.SearchHistoryDatabaseListener {

    @Bean
    SearchHistoryDatabase database;

    private List<SearchHistory> searchHistories = new ArrayList<SearchHistory>();

    public HistoryListAdapter(Context context) {
        super(context, R.layout.history_list_item);
    }

    @AfterInject
    public void init(){
        database.registerSearchHistoryDatabaseListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HistoryListItem listItem;
        if (convertView == null) {
            listItem = HistoryListItem_.build(getContext());
        } else {
            listItem = (HistoryListItem) convertView;
        }

        listItem.bind(getItem(position));

        return listItem;
    }

    @UiThread
    public void update() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchHistories.size();
    }

    @Override
    public SearchHistory getItem(int position) {
        return searchHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onUpdate(List<SearchHistory> searchHistories) {
        Collections.sort(searchHistories, COMPARE_BY_TIMESTAMP);
        this.searchHistories = searchHistories;
        update();
    }

    private static Comparator<SearchHistory> COMPARE_BY_TIMESTAMP = new Comparator<SearchHistory>() {
        public int compare(SearchHistory one, SearchHistory other) {
            return one.compareTo(other);
        }
    };
}
