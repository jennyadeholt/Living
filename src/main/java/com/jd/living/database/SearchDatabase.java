package com.jd.living.database;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.util.Log;

import com.jd.living.model.Area;
import com.jd.living.model.AreaResult;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.Search;
import com.jd.living.model.ormlite.SearchHistory;


@EBean(scope = EBean.Scope.Singleton)
public class SearchDatabase extends BooliDatabase {

    @Bean
    FavoriteDatabase database;

    @Bean
    Search search;

    public interface SearchListener {
        void onUpdateSearch(List<Listing> result);
        void onSearchStarted();
        void onDetailsRequestedForSearch(int booliId);
    }

    public interface SearchHistoryListener {
        void onNewSearch(Search search);
    }

    private boolean searchInprogress = false;

    protected Result result = new Result();

    private SearchListener searchListener;
    private SearchHistoryListener searchHistoryListener;


    @Override
    protected void init() {
    }

    @Override
    public List<Listing> getResult() {
        return result.getResult();
    }

    public void setListingsListeners(SearchListener listener) {
        searchListener = listener;

        if (!result.getResult().isEmpty()) {
            listener.onUpdateSearch(result.getResult());
        } else if (searchInprogress) {
            listener.onSearchStarted();
        }
    }

    public void setSearchHistoryListener(SearchHistoryListener listener) {
        searchHistoryListener = listener;
        searchHistoryListener.onNewSearch(search);
    }

    public void launchListingsSearch(){
        if (!searchInprogress) {
            searchInprogress = true;
            notifyListener(BooliDatabase.ActionCode.SEARCH_STARTED, null);

            if (search.fetchSoldObjects()) {
                server.getObjectsSold(search);
            } else {
                server.getListings(search);
            }

            if (searchHistoryListener != null) {
                searchHistoryListener.onNewSearch(search);
            }
        }
    }

    public void launchListingsSearch(SearchHistory searchHistory){
        if (!searchInprogress) {
            searchInprogress = true;
            notifyListener(BooliDatabase.ActionCode.SEARCH_STARTED, null);

            search.updateSearch(searchHistory);

            if (search.fetchSoldObjects()) {
                server.getObjectsSold(search);
            } else {
                server.getListings(search);
            }

            if (searchHistoryListener != null) {
                searchHistoryListener.onNewSearch(search);
            }
        }
    }

    private void notifyListener(BooliDatabase.ActionCode action, Result result) {
        switch (action) {
            case LISTINGS:
            case SOLD:
                if (searchHistoryListener != null) {
                    searchHistoryListener.onNewSearch(search);
                }
                searchListener.onUpdateSearch(result.getResult());
                break;
            case SEARCH_STARTED:
                searchListener.onSearchStarted();
                break;
            case AREA_TEXT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onListingsResult(BooliDatabase.ActionCode action, Result result) {
        searchInprogress = false;
        switch (action) {
            case LISTINGS:
            case SOLD:
                search.setTime(System.currentTimeMillis());
                this.result = result;
                break;
            case AREA_TEXT:
                break;
            default:
                break;
        }
        notifyListener(action, result);
    }

    @Override
    public void setCurrentId(int booliId) {
        currentBooliId = booliId;
        searchListener.onDetailsRequestedForSearch(currentBooliId);
    }
}
