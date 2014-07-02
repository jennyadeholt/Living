package com.jd.living.activity.search;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.jd.living.R;
import com.jd.living.activity.details.DetailsViewPagerFragment_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

@EFragment(R.layout.search_main)
public class SearchResult extends Fragment implements DatabaseHelper.DatabaseListener {

    @Bean
    DatabaseHelper database;

    private DetailsViewPagerFragment_ detailsView;
    private SearchMain_ searchMain;

    private boolean showDetails = false;

    @AfterViews
    public void init() {
        database.addDatabaseListener(this);

        detailsView = new DetailsViewPagerFragment_();
        searchMain = new SearchMain_();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, detailsView);
        transaction.add(R.id.content_frame, searchMain);
        transaction.commit();

        onShowSearch();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(detailsView);
            ft.hide(searchMain);

            ft.commit();
        } else {
            if (showDetails) {
                onDetailsRequested(0);
            } else {
                onShowSearch();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onUpdate(List<Listing> result) {

    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onDetailsRequested(int booliId) {
        showDetails = true;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.hide(searchMain);
        transaction.show(detailsView);
        transaction.commit();
    }

    public void onShowSearch() {
        if (isAdded()) {

            showDetails = false;

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.hide(detailsView);
            transaction.show(searchMain);
            transaction.commit();
        }

    }

    public boolean isDetailsShown(){
        return showDetails;
    }

}
