package com.jd.living.database;


import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import com.jd.living.model.Listing;
import com.jd.living.model.ormlite.SearchHistory;

@EBean(scope = EBean.Scope.Singleton)
public class DatabaseHelper implements SearchDatabase.SearchListener, FavoriteDatabase.FavoriteListener {

    public enum DatabaseState {
        FAVORITE,
        SEARCH
    }

    @Bean
    protected SearchDatabase searchDatabase;
    @Bean
    protected FavoriteDatabase favoriteDatabase;

    public interface DatabaseListener {
        void onUpdate(List<Listing> result);
        void onSearchStarted();
        void onDetailsRequested(int booliId);
        void onFavoriteUpdated();
    }

    private List<DatabaseListener> databaseListeners = new ArrayList<DatabaseListener>();
    private List<Listing> searchResult = new ArrayList<Listing>();
    private List<Listing> favoriteResult = new ArrayList<Listing>();

    private DatabaseState databaseState = DatabaseState.SEARCH;

    @AfterInject
    public void init() {
        searchDatabase.setListingsListeners(this);
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
         onUpdate(state);
    }

    public DatabaseState getDatabaseState() {
        return databaseState;
    }

    public void setCurrentListIndex(int i) {
        switch (databaseState) {
            case SEARCH:
                searchDatabase.setCurrentListIndex(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentListIndex(i);
                break;
        }
    }

    public void setCurrentId(int i) {
        switch (databaseState) {
            case SEARCH:
                searchDatabase.setCurrentId(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentId(i);
                break;
        }
    }

    public List<Listing> getResult() {
        switch (databaseState) {
            case SEARCH:
                return searchDatabase.getResult();
            case FAVORITE:
                return favoriteDatabase.getResult();
        }
        return new ArrayList<Listing>();
    }

    public Listing getListingBasedOnLocation(int objectIndex) {
        switch (databaseState) {
            case SEARCH:
                return searchDatabase.getListingBasedOnLocation(objectIndex);
            case FAVORITE:
                return favoriteDatabase.getListingBasedOnLocation(objectIndex);
        }
        return new Listing();
    }

    public int getListIndex(int booliId) {
        switch (databaseState) {
            case SEARCH:
                return searchDatabase.getListIndex(booliId);
            case FAVORITE:
                return favoriteDatabase.getListIndex(booliId);
        }
        return -1;
    }

    public Listing getListing(int booliId) {
        switch (databaseState) {
            case SEARCH:
                return searchDatabase.getListing(booliId);
            case FAVORITE:
                return favoriteDatabase.getListing(booliId);
        }
        return null;
    }

    public void launchSearch() {
        searchDatabase.launchListingsSearch();
    }

    public void launchSearch(SearchHistory searchHistory) {
        searchDatabase.launchListingsSearch(searchHistory);
    }

    @Override
    public void onUpdateFavorites() {
        favoriteResult = favoriteDatabase.getResult();
        onUpdate(DatabaseState.FAVORITE);
    }

    @Override
    public void onUpdateSearch() {
        searchResult = searchDatabase.getResult();
        onUpdate(DatabaseState.SEARCH);
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

    public SearchDatabase getSearchDatabase() {
        return searchDatabase;
    }

    private void onUpdate(DatabaseState state) {

        switch (state) {
            case FAVORITE:
                if (databaseState == state) {
                    for (DatabaseListener listener : databaseListeners) {
                        listener.onUpdate(favoriteResult);
                    }
                } else {
                    for (DatabaseListener listener : databaseListeners) {
                        listener.onFavoriteUpdated();
                    }
                }
                break;
            case SEARCH:
                if (databaseState == state) {
                    for (DatabaseListener listener : databaseListeners) {
                        listener.onUpdate(searchResult);
                    }
                }
                break;
            default:
                break;
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
