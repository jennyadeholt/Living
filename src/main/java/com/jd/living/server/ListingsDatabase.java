package com.jd.living.server;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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
        AREA_TEXT
    }

    public interface ListingsListener {
        void onUpdate(List<Listing> listings);
    }

    @Bean
    BooliServer server;

    private List<Listing> listings = new ArrayList<Listing>();
    private List<ListingsListener> listeners = new ArrayList<ListingsListener>();

    @AfterInject
    public void init(){
        server.addServerConnectionListener(this);
        server.getListings("Malmö");
    }

    public void addListingsListener(ListingsListener listener) {
        listeners.add(listener);
        if (listings != null) {
            listener.onUpdate(listings);
        }
    }

    public void getListings() {
        if (listings.isEmpty()) {
            server.getListings("Malmö");
        } else {
            notifyListerner(ActionCode.LISTINGS);
        }
    }

    private void notifyListerner(ActionCode action) {
        for (ListingsListener listener : listeners) {
            switch (action) {
                case LISTINGS:
                    listener.onUpdate(listings);
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
