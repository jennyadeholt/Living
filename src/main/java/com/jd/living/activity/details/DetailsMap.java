package com.jd.living.activity.details;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;


@EActivity(R.layout.details_map_view)
public class DetailsMap extends DetailsActivity implements GoogleMap.OnMapLoadedCallback {

    @FragmentById
    MapFragment detailsMap;

    private GoogleMap googleMap;
    private LatLng target;

    @Override
    public void onInit() {
        googleMap = detailsMap.getMap();
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onUpdate() {

            googleMap.clear();

            target = new LatLng(listing.getLatitude(), listing.getLongitude());

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(target)
                            .title(listing.getAddress()));

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
        listingsDatabase.addDetailsListener(this);
    }
}
