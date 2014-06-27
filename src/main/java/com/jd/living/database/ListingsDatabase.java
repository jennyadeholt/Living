package com.jd.living.database;

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

import com.jd.living.model.Area;
import com.jd.living.model.AreaResult;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.BooliServer;


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
    private int currentListIndex = -1;
    private Result result = new Result();

    private List<ListingsListener> listeners = new ArrayList<ListingsListener>();
    private List<DetailsListener> detailsListeners = new ArrayList<DetailsListener>();

    @AfterInject
    public void init(){
        server.addServerConnectionListener(this);
    }

    public void registerListingsListener(ListingsListener listener) {
        listeners.add(listener);

        if (!result.getResult().isEmpty()) {
            listener.onUpdate(result);
        } else if (searchInprogress) {
            listener.onSearchStarted();
        }
    }

    public void registerDetailsListener(DetailsListener detailsListener) {
        detailsListeners.add(detailsListener);
        if (currentBooliId != -1) {
            detailsListener.onDetailsRequested(currentBooliId);
        }
    }

    public void unregisterDetailsListener(DetailsListener detailsListener) {
        detailsListeners.remove(detailsListener);
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

            searchInprogress = true;
            notifyListerner(ActionCode.SEARCH_STARTED, null);

            if ( preferences.getString("preference_object_type", "0").equals("0")) {
                server.getListings(location, minRooms, maxRooms, types, production);
            } else {
                server.getObjectsSold(location, minRooms, maxRooms, types, production);
            }
        }
    }

    public int getNumberOfObjects() {
        return result.count;
    }

    private void notifyListerner(ActionCode action, Result result) {
        for (ListingsListener listener : listeners) {
            switch (action) {
                case LISTINGS:
                case SOLD:
                    listener.onUpdate(result);
                    break;
                case SEARCH_STARTED:
                    listener.onSearchStarted();
                    break;
                case AREA_TEXT:

                default:
                    break;
            }
        }
    }

    public Listing getListing(int booliId) {
        Listing l = null;
        for (Listing listing : result.getResult()) {
            if (listing.getBooliId() == booliId ) {
                l = listing;
                break;
            }
        }
        if (l == null && !result.getResult().isEmpty()) {
            l = result.getResult().get(0);
        }
        return l;
    }

    public Listing getListing() {
        return getListing(currentBooliId);
    }

    public Listing getListingFromList(int location) {
        if (!result.getResult().isEmpty()) {
            return result.getResult().get(location);
        } else {
            return null;
        }
    }

    public int getListLocation(int booliId) {
        Listing l = getListing(booliId);
        return result.getResult().indexOf(l);
    }

    public void setCurrentId(int booliId) {
        currentBooliId = booliId;
        for (DetailsListener detailsListener : detailsListeners) {
            detailsListener.onDetailsRequested(currentBooliId);
        }
    }

    public void setCurrentIndex(int index) {
        Listing listing = getListingFromList(index);
        currentListIndex = getListLocation(listing.getBooliId());
    }

    @Override
    public void onListingsResult(ActionCode action, Result result) {

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

        notifyListerner(action, result);
    }
}
