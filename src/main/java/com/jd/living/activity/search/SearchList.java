package com.jd.living.activity.search;

import java.util.List;

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
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;


@EFragment
public class SearchList extends ListFragment implements DatabaseHelper.DatabaseListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    DatabaseHelper database;

    @Bean
    SearchListAdapter searchListAdapter;

    private ProgressDialog spinner;

    @AfterViews
    public void init() {
        setListAdapter(searchListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        database.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        database.removeDatabaseListener(this);
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        database.setCurrentId(listing.getBooliId());
    }

    @Override
    public void onUpdate(List<Listing> result) {
        update(result);
    }

    @Override
    public void onSearchStarted() {
        //spinner = ProgressDialog.show(getActivity(), "", "Loading..", true);
    }

    @Override
    public void onDetailsRequested(int booliId) {

    }

    @UiThread
    public void update(List<Listing> result) {
        if (spinner != null) {
            spinner.dismiss();
        }

        info.setText(getString(R.string.number_of_objects, result.size(), result.size()));
    }
}

