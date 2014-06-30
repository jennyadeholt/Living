package com.jd.living.activity.search.favorites;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

import com.jd.living.R;
import com.jd.living.activity.details.favorites.FavoriteDetailsViewPagerFragment_;
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;


@EFragment(R.layout.search_main)
public class FavoriteResult extends Fragment implements FavoriteDatabase.FavoriteListener {

    @Bean
    FavoriteDatabase database;

    private FavoriteDetailsViewPagerFragment_ detailsView;
    private FavoriteList_ favoriteList;

    private boolean showDetails = false;


    @AfterViews
    public void init() {
        database.addFavoriteListener(this);

        detailsView = new FavoriteDetailsViewPagerFragment_();
        favoriteList = new FavoriteList_();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, detailsView);
        transaction.add(R.id.content_frame, favoriteList);
        transaction.commit();

        onShowSearch();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("LivingLiving", "FavoriteListAdapter.onHiddenChanged " + hidden);
        if (hidden) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(detailsView);
            ft.hide(favoriteList);
            ft.commit();
        } else {
            if (showDetails) {
                onFavoriteClicked(null);
            } else {
                onShowSearch();
            }
        }
        super.onHiddenChanged(hidden);
    }

    public void onShowSearch() {
        if (isAdded()) {
            showDetails = false;

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.hide(detailsView);
            transaction.show(favoriteList);
            transaction.commit();
        }
    }

    public boolean isDetailsShown(){
        return showDetails;
    }

    @Override
    public void updatedFavorites(List<Listing> listings) {

    }

    @Override
    public void onFavoriteClicked(Listing listing) {
        Log.d("LivingLiving", "FavoriteListAdapter.onFavoriteClicked " + listing.getBooliId());
        showDetails = true;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(favoriteList);
        transaction.show(detailsView);
        transaction.commit();
    }
}

