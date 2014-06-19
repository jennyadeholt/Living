package com.jd.living.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;

/**
 * Created by jennynilsson on 2014-06-03.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ListingsDatabase implements BooliServer.ServerConnectionListener {

    public enum ActionCode {
        LISTING,
        LISTINGS,
        SOLD,
        SOLD_SINGLE,
        AREA_COORDINATES,
        AREA_TEXT,
        SEARCH_STARTED;
    }

    public interface ListingsListener {
        void onUpdate(Result result);
        void onSearchStarted();
    }

    public interface DetailsListener {
        void onDetailsRequested(int booliId);
    }

    @Bean
    BooliServer server;

    @RootContext
    Context context;


    private SharedPreferences preferences;

    private boolean searchInprogress = false;
    private int currentBooliId = -1;
    private Result result = new Result();

    private List<ListingsListener> listeners = new ArrayList<ListingsListener>();
    private List<DetailsListener> detailsListeners = new ArrayList<DetailsListener>();

    @AfterInject
    public void init(){
        server.addServerConnectionListener(this);
    }

    public void addListingsListener(ListingsListener listener) {
        listeners.add(listener);

        if (!result.listings.isEmpty()) {
            listener.onUpdate(result);
        } else if (searchInprogress) {
            listener.onSearchStarted();
        }
    }

    public void addDetailsListener(DetailsListener detailsListener) {
        detailsListeners.add(detailsListener);
        if (currentBooliId != -1) {
            detailsListener.onDetailsRequested(currentBooliId);
        }
    }

    public void launchListingsSearch(){
        if (!searchInprogress) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> buildTypes = preferences.getStringSet("preference_building_type", new HashSet<String>());
            String types = "";

            for (String type : buildTypes.toArray(new String[]{})) {
                types = types + type + ", ";
            }

            if (!TextUtils.isEmpty(types)) {
                types = types.substring(0, types.length() - 2);
            }

            String minRooms = preferences.getString("preference_min_numbers", "1");
            String maxRooms = preferences.getString("preference_max_numbers", "5");
            String location = preferences.getString("preferences_area_location", "Hörby");
            String production = preferences.getString("preference_build_type", "null");

            Log.d("Living", "" + location + " " + minRooms + " " + maxRooms);

            searchInprogress = true;
            notifyListerner(ActionCode.SEARCH_STARTED);
            server.getListings(location, minRooms, maxRooms, types, production);
        }
    }

    private void notifyListerner(ActionCode action) {
        for (ListingsListener listener : listeners) {
            switch (action) {
                case LISTINGS:
                    listener.onUpdate(result);
                    break;
                case SEARCH_STARTED:
                    listener.onSearchStarted();
                    break;
                default:
                    break;
            }
        }
    }

    public Listing getListing(int booliId) {
        Listing l = null;
        for (Listing listing : result.listings) {
            if (listing.getBooliId() == booliId ) {
                l = listing;
                break;
            }
        }
        if (l == null && !result.listings.isEmpty()) {
            l = result.listings.get(0);
        }
        return l;
    }

    public Listing getListing() {
        return getListing(currentBooliId);
    }

    public void setCurrentId(int booliId) {
        currentBooliId = booliId;
        for (DetailsListener detailsListener : detailsListeners) {
            detailsListener.onDetailsRequested(currentBooliId);
        }
    }

    @Override
    public void onResponse(ActionCode action, Result result) {

        searchInprogress = false;
        Log.d("We have a result", "" + result.count);
        switch (action) {
            case LISTINGS:
            case SOLD:
                Log.d("We have a result", "" + result.count);
                this.result = result;
                break;
            default:
                break;
        }

        notifyListerner(action);
    }
}
