package com.jd.living.activity.search;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

import com.jd.living.R;
import com.jd.living.activity.details.favorites.FavoriteDetailsViewPagerFragment_;
import com.jd.living.activity.details.search.SearchDetailsViewPagerFragment_;
import com.jd.living.database.ListingsDatabase;

@EFragment(R.layout.search_main)
public class SearchResult extends Fragment implements ListingsDatabase.DetailsListener {

    @Bean
    ListingsDatabase listingsDatabase;

    private SearchDetailsViewPagerFragment_ detailsView;
    private SearchMain_ searchMain;

    private boolean showDetails = false;

    @AfterViews
    public void init() {
        listingsDatabase.registerDetailsListener(this);

        detailsView = new SearchDetailsViewPagerFragment_();
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
                Log.d("LivingLiving", "SearchResult.onHiddenChanged showDetails" + hidden);
                onDetailsRequested(0);
            } else {
                onShowSearch();
            }
        }
        super.onHiddenChanged(hidden);
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
