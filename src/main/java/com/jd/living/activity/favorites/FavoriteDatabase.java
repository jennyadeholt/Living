package com.jd.living.activity.favorites;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.util.Log;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.model.SoldResult;
import com.jd.living.server.BooliServer;
import com.jd.living.server.ListingsDatabase;

@EBean(scope = EBean.Scope.Singleton)
public class FavoriteDatabase implements BooliServer.ServerConnectionListener {

    @Bean
    BooliServer server;

    @Bean
    ListingsDatabase listingsDatabase;

    public interface FavoriteListener {
        void addedFavorite(Listing listing);
        void removedFavorite(Listing listing);
    }

    private List<Integer> favorites = new ArrayList<Integer>();
    private List<Listing> listings = new ArrayList<Listing>();
    private List<FavoriteListener> favoriteListeners = new ArrayList<FavoriteListener>();

    public void updateFavorite(Listing listing) {

        if (isFavorite(listing.getBooliId())) {
            favorites.remove((Integer) listing.getBooliId());
            notifyListeners(false, listing);
        } else {
            favorites.add(listing.getBooliId());
            notifyListeners(true, listingsDatabase.getListing(listing.getBooliId()));
            //server.getListing(listing.getBooliId());
        }
    }

    public boolean isFavorite(int booliId) {
        return favorites.contains(booliId);
    }

    @AfterInject
    public void init() {
        server.addServerConnectionListener(this);

        for (Integer favorite : favorites) {
            notifyListeners(true, listingsDatabase.getListing(favorite));
        }
    }

    public void addFavoriteListener(FavoriteListener listener) {
        favoriteListeners.add(listener);

        for (Listing listing: listings) {
            listener.addedFavorite(listing);
        }

    }

    public List<Listing> getFavorites() {
        return listings;
    }

    @Override
    public void onListingsResult(ListingsDatabase.ActionCode action, Result result) {
        Log.d("Living", "FavoriteDatabase.onListingsResult" + action.name());
        switch (action) {
            case LISTING:
                for (Listing listing : result.getListings()) {
                    notifyListeners(true, listing);
                }
                break;
            default:
                break;
        }
    }

    public int getListLocation(Listing listing) {
        return favorites.indexOf(listing);
    }

    private Listing getListing(int booliId) {
        Listing l = null;
        for (Listing listing : listings) {
            if (listing.getBooliId() == booliId ) {
                l = listing;
                break;
            }
        }
        if (l == null && !listings.isEmpty()) {
            l = listings.get(0);
        }
        return l;
    }

    private void notifyListeners(boolean add, Listing listing) {
        if (add) {
            listings.add(listing);
            for (FavoriteListener listener : favoriteListeners) {
                listener.addedFavorite(listing);
            }
        } else {
            listings.remove(listing);
            for (FavoriteListener listener : favoriteListeners) {
                listener.removedFavorite(listing);
            }
        }

    }

}
