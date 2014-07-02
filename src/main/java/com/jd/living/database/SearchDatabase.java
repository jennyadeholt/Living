package com.jd.living.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.text.TextUtils;
import android.util.Log;

import com.jd.living.activity.settings.SearchPreferenceKey;
import com.jd.living.model.Area;
import com.jd.living.model.AreaResult;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.model.Search;


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

    private boolean searchInprogress = false;

    protected Result result = new Result();

    private SearchListener listingsListener;


    @Override
    protected void init() {
    }

    @Override
    public List<Listing> getResult() {
        return result.getResult();
    }

    public void setListingsListeners(SearchListener listener) {
        listingsListener = listener;

        if (!result.getResult().isEmpty()) {
            listener.onUpdateSearch(result.getResult());
        } else if (searchInprogress) {
            listener.onSearchStarted();
        }
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
        }
    }

    private void notifyListener(BooliDatabase.ActionCode action, Result result) {
        switch (action) {
            case LISTINGS:
            case SOLD:
                listingsListener.onUpdateSearch(result.getResult());
                break;
            case SEARCH_STARTED:
                listingsListener.onSearchStarted();
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
                this.result = result;
                break;
            case AREA_TEXT:
                for (Area area : ((AreaResult) result).getAreas()) {
                    Log.d("Living", "Area " + area.getName());
                }
                break;
            default:
                break;
        }
        notifyListener(action, result);
    }

    @Override
    public void setCurrentId(int booliId) {
        currentBooliId = booliId;
        listingsListener.onDetailsRequestedForSearch(currentBooliId);
    }
}
