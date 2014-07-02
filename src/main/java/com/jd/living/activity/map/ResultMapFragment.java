package com.jd.living.activity.map;

import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;

import android.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchDatabase;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;

@EFragment(R.layout.map)
public class ResultMapFragment extends Fragment implements DatabaseHelper.DatabaseListener,
        OnInfoWindowClickListener, GoogleMap.OnMapLoadedCallback{


    @FragmentById
    MapFragment resultMap;

    @Bean
    DatabaseHelper database;

    @Bean
    ResultInfoWindowAdapter adapter;

    private GoogleMap googleMap;
    private LatLngBounds.Builder bounds;


    @AfterViews
    public void init(){
        googleMap = resultMap.getMap();
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(adapter);
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @UiThread
    @Override
    public void onUpdate(List<Listing> result) {

        googleMap.clear();

        bounds = new LatLngBounds.Builder();
        for (Listing listing : result) {
            LatLng target = new LatLng(listing.getLatitude(), listing.getLongitude());

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(target)
                            .title(listing.getAddress())
                            .snippet(String.valueOf(listing.getBooliId())));

            bounds.include(target);
        }

        if (!result.isEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 15));
        }

    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onDetailsRequested(int booliId) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        database.setCurrentId(Integer.valueOf(marker.getSnippet()));
    }

    @Override
    public void onMapLoaded() {
        database.addDatabaseListener(this);
    }
}
