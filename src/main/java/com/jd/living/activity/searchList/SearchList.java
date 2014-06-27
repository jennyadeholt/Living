package com.jd.living.activity.searchList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.database.ListingsDatabase;


@EFragment
public class SearchList extends ListFragment implements ListingsDatabase.ListingsListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    ListingsDatabase listingsDatabase;

    @Bean
    SearchListAdapter searchListAdapter;

    private ProgressDialog spinner;

    @AfterViews
    public void init() {
        listingsDatabase.registerListingsListener(this);
        setListAdapter(searchListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list, container, false);
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        listingsDatabase.setCurrentId(listing.getBooliId());
    }

    @Override
    public void onUpdate(Result result) {
        update(result);
    }

    @Override
    public void onSearchStarted() {
        spinner = ProgressDialog.show(getActivity(), "", "Loading..", true);
    }

    @UiThread
    public void update(Result result) {
        if (spinner != null) {
            spinner.dismiss();
        }
        info.setText(getString(R.string.number_of_objects, result.count, result.totalCount));
    }
}

