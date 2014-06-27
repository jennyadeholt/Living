package com.jd.living.database;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.AuthStore;
import com.jd.living.server.BooliServer;

@EBean(scope = EBean.Scope.Singleton)
public class FavoriteDatabase implements BooliServer.ServerConnectionListener {

    @Bean
    BooliServer server;

    @Bean
    ListingsDatabase listingsDatabase;

    private static final String API_BASE = "http://api.booli.se/listings/";

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
            server.getListing(listing.getBooliId());
            //getListingFromBooli(listing.getBooliId());
        }
    }

    @Background
    public void getListingFromBooli(int booliId) {
        ArrayList<String> resultList = new ArrayList<String>();

        AuthStore authStore = new AuthStore();

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(API_BASE);
            sb.append(booliId);
            sb.append("&callerId=" + authStore.getCallerId());
            sb.append("&time=" + authStore.getTime());
            sb.append("&unique=" + authStore.getUnique());
            sb.append("&hash=" + authStore.getHash());

            URL url = new URL(sb.toString());

            Log.d("Living", "URL " + url.toString());
            Log.d("Living", "URL " + url.toExternalForm());
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Accept:" , "application/vnd.booli-v2+json");
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("Living", "Error processing Booli API URL", e);
            //eturn resultList;
        } catch (IOException e) {
            Log.e("Living", "Error connecting to Booli API", e);
            //return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            if(jsonResults != null && jsonResults.length() != 0) {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("listings");

                // Extract the Place descriptions from the results
                resultList = new ArrayList<String>();
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString("listPrice"));
                }
            }
        } catch (JSONException e) {
            Log.e("Living", "Cannot process JSON results", e);
        }

        //return resultList;
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
                for (Listing listing : result.getResult()) {
                    notifyListeners(true, listing);
                }
                break;
            default:
                break;
        }
    }

    public Listing getListingFromList(int location) {
        if (!listings.isEmpty()) {
            return listings.get(location);
        } else {
            return null;
        }
    }

    public int getListLocation(Listing listing) {
        return favorites.indexOf(listing);
    }

    public Listing getListing(int booliId) {
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
