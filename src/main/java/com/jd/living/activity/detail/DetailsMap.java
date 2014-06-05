package com.jd.living.activity.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

import android.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;


@EFragment(R.layout.map)
public class DetailsMap extends Fragment implements GoogleMap.OnMapLoadedCallback {

    @FragmentById
    MapFragment resultMap;

    @Bean
    ListingsDatabase database;

    private GoogleMap googleMap;

    private LatLng target;
    private boolean mapReady = false;
    private Listing listing;

    @AfterViews
    public void init(){
        googleMap = resultMap.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Living", "onResume");
        int id = getActivity().getIntent().getIntExtra("id", -1);
        listing = database.getListing(id);
        onUpdate();
    }

    private void onUpdate() {
        target = new LatLng(listing.getLatitude(), listing.getLongitude());

        googleMap.addMarker(
                new MarkerOptions()
                        .position(target)
                        .title(listing.getAddress()));

        if (mapReady) {
            updateMap();
        }

    }

    private void updateMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onMapLoaded() {
        mapReady = true;
        updateMap();
    }
}
