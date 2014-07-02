package com.jd.living.database;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.BooliServer;

@EBean
public abstract class BooliDatabase implements BooliServer.ServerConnectionListener {

    @Bean
    protected BooliServer server;

    @RootContext
    protected Context context;

    protected SharedPreferences preferences;

    public enum ActionCode {
        FAVORITE,
        LISTINGS,
        SOLD,
        SOLD_SINGLE,
        AREA_COORDINATES,
        AREA_TEXT,
        SEARCH_STARTED;
    }

    protected int currentBooliId = -1;
    protected int currentListIndex = -1;

    @AfterInject
    public void onInit(){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        server.addServerConnectionListener(this);
        init();
    }

    protected abstract void init();
    public abstract List<Listing> getResult();
    public abstract void setCurrentId(int booliId);

    public Listing getListing(int booliId) {
        Listing l = null;
        for (Listing listing : getResult()) {
            if (listing.getBooliId() == booliId ) {
                l = listing;
                break;
            }
        }
        if (l == null && !getResult().isEmpty()) {
            l = getResult().get(0);
        }
        return l;
    }

    public Listing getListingBasedOnLocation(int location) {
        if (!getResult().isEmpty()) {
            return getResult().get(location);
        } else {
            return null;
        }
    }

    public int getListIndex(int booliId) {
        Listing l = getListing(booliId);
        return getResult().indexOf(l);
    }

    public void setCurrentListIndex(int index) {
        Listing listing = getListingBasedOnLocation(index);
        currentListIndex = getListIndex(listing.getBooliId());
    }
}
