package com.jd.living.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jd.living.activity.settings.SearchPreferenceKey;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;

@EBean(scope = EBean.Scope.Singleton)
public class FavoriteDatabase extends BooliDatabase {

    @Bean
    ListingsDatabase listingsDatabase;

    public interface FavoriteListener {
        void updatedFavorites(List<Listing> listings);
        void onFavoriteClicked(Listing listing);
    }

    private Map<Integer, Listing> map = new HashMap<Integer, Listing>();
    private List<FavoriteListener> favoriteListeners = new ArrayList<FavoriteListener>();

    @Override
    protected void init() {
        preferences = context.getSharedPreferences(SearchPreferenceKey.PREFERENCE_FAVORITES, Context.MODE_PRIVATE);

        int numbers = preferences.getInt(SearchPreferenceKey.PREFERENCE_NBR_OF_FAVORITES, 0);
        for (int i = 1 ; i <= numbers ; i++) {
            server.getListing(preferences.getInt(SearchPreferenceKey.PREFERENCE_FAVORITE + i, 0));
        }
    }

    @Override
    public List<Listing> getResult(){
        return new ArrayList<Listing>(map.values());
    }

    @Override
    public void setCurrentId(int booliId) {
        currentBooliId = booliId;

        for (FavoriteListener listener : favoriteListeners) {
            listener.onFavoriteClicked(getListing(booliId));
        }
    }

    public void updateFavorite(Listing listing) {
        if (isFavorite(listing)) {
            removeFavorite(listing);
        } else {
            addFavorite(listing);
            server.getListing(listing.getBooliId());
        }
    }

    private void addFavorite(Listing listing) {
        SharedPreferences.Editor editor = preferences.edit();

        int nbrOfFavorites = preferences.getInt(SearchPreferenceKey.PREFERENCE_NBR_OF_FAVORITES, 0);
        Log.d("living" , "add " + nbrOfFavorites);
        editor.putInt(SearchPreferenceKey.PREFERENCE_FAVORITE + (nbrOfFavorites + 1), listing.getBooliId() );
        editor.putInt(SearchPreferenceKey.PREFERENCE_NBR_OF_FAVORITES, nbrOfFavorites + 1);
        editor.commit();

    }

    private void removeFavorite(Listing listing) {
        SharedPreferences.Editor editor = preferences.edit();

        int nbrOfFavorites = preferences.getInt(SearchPreferenceKey.PREFERENCE_NBR_OF_FAVORITES, 0);
        for (int i = 1; i <= nbrOfFavorites; i++ ) {
            editor.remove(SearchPreferenceKey.PREFERENCE_FAVORITE + i);
        }
        editor.commit();

        map.remove(listing.getBooliId());

        int index = 1;
        for (Listing l : getResult()) {
            editor.putInt(SearchPreferenceKey.PREFERENCE_FAVORITE + index++, l.getBooliId());
        }

        editor.putInt(SearchPreferenceKey.PREFERENCE_NBR_OF_FAVORITES, getResult().size());
        editor.commit();

        notifyListeners();
    }

    public boolean isFavorite(Listing listing) {
        return map.containsKey(listing.getBooliId());
    }

    public void addFavoriteListener(FavoriteListener listener) {
        favoriteListeners.add(listener);
        listener.updatedFavorites(getResult());
    }

    @Override
    public void onListingsResult(BooliDatabase.ActionCode action, Result result) {
        switch (action) {
            case FAVORITE:
                for (Listing listing : result.getResult()) {
                    map.put(listing.getBooliId(), listing);
                }
                notifyListeners();
                break;
            default:
                break;
        }
    }

    private void notifyListeners() {
        for (FavoriteListener listener : favoriteListeners) {
            listener.updatedFavorites(getResult());
        }
    }
}
