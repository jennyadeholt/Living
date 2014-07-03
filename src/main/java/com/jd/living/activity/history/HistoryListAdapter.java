package com.jd.living.activity.history;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import android.content.Context;
import android.util.Log;
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
        Log.d("History", "HistoryListAdapter.init");
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
        this.searchHistories = searchHistories;
        update();
    }
}
