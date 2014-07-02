package com.jd.living.database;


import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import com.jd.living.model.Listing;

@EBean(scope = EBean.Scope.Singleton)
public class DatabaseHelper implements SearchDatabase.SearchListener, FavoriteDatabase.FavoriteListener {

    public enum DatabaseState {
        FAVORITE,
        SEARCH
    }

    @Bean
    protected SearchDatabase listingsDatabase;
    @Bean
    protected FavoriteDatabase favoriteDatabase;

    public interface DatabaseListener {
        void onUpdate(List<Listing> result);
        void onSearchStarted();
        void onDetailsRequested(int booliId);
    }

    private List<DatabaseListener> databaseListeners = new ArrayList<DatabaseListener>();
    private List<Listing> searchResult = new ArrayList<Listing>();
    private List<Listing> favoriteResult = new ArrayList<Listing>();

    private DatabaseState databaseState = DatabaseState.SEARCH;

    @AfterInject
    public void init() {
        listingsDatabase.setListingsListeners(this);
        favoriteDatabase.setFavoriteListener(this);
    }

    public void addDatabaseListener(DatabaseListener listener) {
        databaseListeners.add(listener);


        switch (databaseState) {
            case SEARCH:
                listener.onUpdate(searchResult);
                break;
            case FAVORITE:
                listener.onUpdate(favoriteResult);
                break;
        }


    }

    public void removeDatabaseListener(DatabaseListener listener) {
       databaseListeners.remove(listener);
    }

    public void setDatabaseState(DatabaseState state) {
        this.databaseState = state;

        switch (databaseState) {
            case SEARCH:
                onUpdate(state, searchResult);
                break;
            case FAVORITE:
                onUpdate(state, favoriteResult);
                break;
        }
    }

    public void setCurrentListIndex(int i) {
        switch (databaseState) {
            case SEARCH:
                listingsDatabase.setCurrentListIndex(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentListIndex(i);
                break;
        }
    }

    public void setCurrentId(int i) {
        switch (databaseState) {
            case SEARCH:
                listingsDatabase.setCurrentId(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentId(i);
                break;
        }
    }

    public List<Listing> getResult() {
        switch (databaseState) {
            case SEARCH:
                return listingsDatabase.getResult();
            case FAVORITE:
                return favoriteDatabase.getResult();
        }
        return new ArrayList<Listing>();
    }

    public Listing getListingBasedOnLocation(int objectIndex) {
        switch (databaseState) {
            case SEARCH:
                return listingsDatabase.getListingBasedOnLocation(objectIndex);
            case FAVORITE:
                return favoriteDatabase.getListingBasedOnLocation(objectIndex);
        }
        return new Listing();
    }

    public int getListIndex(int booliId) {
        switch (databaseState) {
            case SEARCH:
                return listingsDatabase.getListIndex(booliId);
            case FAVORITE:
                return favoriteDatabase.getListIndex(booliId);
        }
        return 0;
    }

    public void launchSearch() {
        switch (databaseState) {
            case SEARCH:
                listingsDatabase.launchListingsSearch();
                break;
            case FAVORITE:
                break;
        }
    }

    @Override
    public void onUpdateFavorites(List<Listing> listings) {
        favoriteResult = listings;
        onUpdate(DatabaseState.FAVORITE, listings);
    }

    @Override
    public void onUpdateSearch(List<Listing> result) {
        searchResult = result;
        onUpdate(DatabaseState.SEARCH, result);
    }

    @Override
    public void onSearchStarted() {
        for (DatabaseListener listener : databaseListeners) {
            listener.onSearchStarted();
        }
    }

    @Override
    public void onDetailsRequestedForFavorite(int booliId) {
        onDetailsRequested(DatabaseState.FAVORITE, booliId);
    }

    @Override
    public void onDetailsRequestedForSearch(int booliId) {
        onDetailsRequested(DatabaseState.SEARCH, booliId);
    }

    public FavoriteDatabase getFavoriteDatabase() {
        return favoriteDatabase;
    }

    private void onUpdate(DatabaseState state, List<Listing> listings) {
        if (databaseState == state) {
            for (DatabaseListener listener : databaseListeners) {
                listener.onUpdate(listings);
            }
        }
    }

    private void onDetailsRequested(DatabaseState state, int booliId) {
        if (databaseState == state) {
            for (DatabaseListener listener : databaseListeners) {
                listener.onDetailsRequested(booliId);
            }
        }
    }

}
