package com.jd.living.activity.settings.search;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.jd.living.server.AuthStore;


public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private static final String LOG_TAG = "Living";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autoCompleteViaGooglePlaces";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDPGgizbgjQe2SeXnZ_8Z_wmd_jM4EXhII";

    private static final String API_BASE = "http://api.booli.se/areas";

    private ArrayList<String> resultList;

    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

        resultList = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence search) {
                FilterResults filterResults = new FilterResults();
                if (search != null) {
                    resultList = autoCompleteViaGooglePlaces(search.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }


    private ArrayList<String> autoCompleteViaBooli(String search) {
        ArrayList<String> resultList = new ArrayList<String>();

        AuthStore authStore = new AuthStore();

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(API_BASE);
            sb.append("?q=" + URLEncoder.encode(search, "utf-8"));
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
            Log.e(LOG_TAG, "Error processing Booli API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Booli API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            if(jsonResults != null && jsonResults.length() != 0) {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("areas");

                // Extract the Place descriptions from the results
                resultList = new ArrayList<String>();
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString("fullName"));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;

    }

    private ArrayList<String> autoCompleteViaGooglePlaces(String input) {
        ArrayList<String> resultList = new ArrayList<String>();

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:se");
            sb.append("&language=sv");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            for (int i = 0; i < predsJsonArray.length(); i++) {
                String result = predsJsonArray.getJSONObject(i).getString("description");
                resultList.add(result.replace(", Sverige", "" ));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}