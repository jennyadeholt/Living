package com.jd.living.activity.map;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

@EBean
public class ResultInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    @RootContext
    Context context;

    @Bean
    ListingsDatabase database;

    private Map<Marker, InfoWindow> view = new HashMap<Marker, InfoWindow>();

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getView(marker);
    }


    private View getView(Marker marker) {
        InfoWindow infoWindow = null;
        String id = marker.getSnippet();

        for (Marker m : view.keySet()) {
            if (m.getSnippet().equals(id)) {
                infoWindow = view.get(m);
                break;
            }
        }
        if (infoWindow == null) {
            Listing listing = database.getListing(Integer.valueOf(id));
            infoWindow = InfoWindow_.build(context);
            infoWindow.bind(listing, marker);
            view.put(marker, infoWindow);
        }
        return infoWindow;
    }
}
