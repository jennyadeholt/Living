package com.jd.living.activity.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.app.Fragment;

import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

@EFragment
public abstract class DetailsInfo extends Fragment implements ListingsDatabase.DetailsListener {

    protected Listing listing;

    protected abstract void onInit();
    protected abstract void onUpdate();

    @Bean
    ListingsDatabase listingsDatabase;

    @AfterViews
    public void init() {
        listingsDatabase.addDetailsListener(this);
        onInit();
    }

    @Override
    public void onDetailsRequested(int booliId) {
        listing =  listingsDatabase.getListing(booliId);
        onUpdate();
    }
}
