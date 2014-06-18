package com.jd.living.activity.searchList;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.activity.detail.DetailsActivity_;
import com.jd.living.dialog.SpinnerDialogFragment;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;


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
        Log.d("Living", "SearchList.init");
        listingsDatabase.addListingsListener(this);
        setListAdapter(searchListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_list, container, false);
        return view;
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        Log.d("Living", "SearchList.listItemClicked");
        Intent intent = new Intent(getActivity(), DetailsActivity_.class);
        intent.putExtra("id", listing.getBooliId());
        startActivity(intent);
    }

    @Override
    public void onUpdate(List<Listing> listings) {
        Log.d("Living", "SearchList.onUpdate");
        update(listings.size());
    }

    @Override
    public void onSearchStarted() {
        Log.d("Living", "SearchList.onSearchStarted");
        spinner = ProgressDialog.show(getActivity(), "", "Loading..", true);
    }

    @UiThread
    public void update(int searches) {
        if (spinner != null) {
            spinner.dismiss();
        }
        info.setText("Antal objekt: " + searches);
    }
}

