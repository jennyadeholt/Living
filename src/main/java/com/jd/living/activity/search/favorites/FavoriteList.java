package com.jd.living.activity.search.favorites;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;


@EFragment
public class FavoriteList extends ListFragment implements FavoriteDatabase.FavoriteListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    FavoriteDatabase favoriteDatabase;

    @Bean
    FavoriteListAdapter favoriteListAdapter;

    @AfterViews
    public void init() {
        favoriteDatabase.addFavoriteListener(this);
        setListAdapter(favoriteListAdapter);
        updateNumberOfFavorites();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list, container, false);
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        Log.d("Living", "FavoriteList.listItemClicked");
        favoriteDatabase.setCurrentId(listing.getBooliId());
    }

    @Override
    public void updatedFavorites(List<Listing> listings) {
        updateNumberOfFavorites();
    }

    @Override
    public void onFavoriteClicked(Listing listing) {

    }

    @UiThread
    protected void updateNumberOfFavorites() {
        int nbrOfFavorites = favoriteListAdapter.getCount();
        info.setText(getString(R.string.number_of_objects, nbrOfFavorites, nbrOfFavorites));
    }
}
