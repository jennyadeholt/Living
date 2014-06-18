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
        void onUpdate(List<Listing> listings);
        void onSearchStarted();
    }

    @Bean
    BooliServer server;

    @RootContext
    Context context;


    private SharedPreferences preferences;

    private boolean searchInprogress = false;
    private List<Listing> listings = new ArrayList<Listing>();
    private List<ListingsListener> listeners = new ArrayList<ListingsListener>();

    @AfterInject
    public void init(){
        server.addServerConnectionListener(this);
    }

    public void addListingsListener(ListingsListener listener) {
        listeners.add(listener);
        if (listings != null) {
            listener.onUpdate(listings);
        } else if (searchInprogress) {
            listener.onSearchStarted();
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
            Log.d("Living", "" + location + " " + minRooms + " " + maxRooms);

            searchInprogress = true;
            notifyListerner(ActionCode.SEARCH_STARTED);
            server.getListings(location, minRooms, maxRooms, types);
        }
    }

    private void notifyListerner(ActionCode action) {
        for (ListingsListener listener : listeners) {
            switch (action) {
                case LISTINGS:
                    listener.onUpdate(listings);
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
        Listing result = null;
        for (Listing listing : listings) {
            if (listing.getBooliId() == booliId ) {
                result = listing;
                break;
            }
        }
        return result;
    }

    @Override
    public void onResponse(ActionCode action, Result result) {

        searchInprogress = false;
        Log.d("We have a result", "" + result.count);
        switch (action) {
            case LISTINGS:
                listings = result.listings;
                break;
            case SOLD:
                Log.d("We have a result", "" + result.count);
                listings = result.listings;
                break;
            default:
                break;
        }

        notifyListerner(action);
    }
}
