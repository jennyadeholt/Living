package com.jd.living.activity.search;

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
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;


@EBean
public class SearchListAdapter extends ArrayAdapter<Listing> implements DatabaseHelper.DatabaseListener {

    @Bean
    DatabaseHelper database;

    private List<Listing> listings = new ArrayList<Listing>();

    public SearchListAdapter(Context context) {
        super(context, R.layout.list_item);
    }

    @AfterInject
    public void init(){
        database.addDatabaseListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchListItem searchListItem;
        if (convertView == null) {
            searchListItem = SearchListItem_.build(getContext());
        } else {
            searchListItem = (SearchListItem) convertView;
        }

        searchListItem.bind(getItem(position));

        return searchListItem;
    }

    @UiThread
    public void update() {
        notifyDataSetChanged();
    }

    @UiThread
    public void updateInBackground() {
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return listings.size();
    }

    @Override
    public Listing getItem(int position) {
        return listings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onUpdate(List<Listing> result) {
        Collections.sort(result, COMPARE_BY_ADDRESS);
        this.listings = result;
        update();
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onFavoriteUpdated() {
        update();
    }

    @Override
    public void onDetailsRequested(int booliId) {

    }

    private static Comparator<Listing> COMPARE_BY_ADDRESS = new Comparator<Listing>() {
        public int compare(Listing one, Listing other) {
            return one.compareTo(other);
        }
    };
}
