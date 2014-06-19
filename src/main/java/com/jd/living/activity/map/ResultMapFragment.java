package com.jd.living.activity.map;

import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.activity.detail.DetailsActivity_;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.ListingsDatabase;


@EFragment(R.layout.map)
public class ResultMapFragment extends Fragment implements ListingsDatabase.ListingsListener,
        OnInfoWindowClickListener, GooglePlayServicesClient.ConnectionCallbacks, GoogleMap.OnMapLoadedCallback {

    @FragmentById
    MapFragment resultMap;

    @ViewById
    FrameLayout mapView;

    @Bean
    ListingsDatabase database;

    private GoogleMap googleMap;

    private LatLngBounds.Builder bounds;
    private boolean mapReady = false;
    private LocationClient mLocationClient;

    @AfterViews
    public void init(){

        mLocationClient = new LocationClient(getActivity(), this, null);

        googleMap = resultMap.getMap();
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapLoadedCallback(this);
        database.addListingsListener(this);
    }

    @UiThread
    @Override
    public void onUpdate(Result result) {
        bounds = new LatLngBounds.Builder();
        for (Listing listing : result.listings) {
            LatLng target = new LatLng(listing.getLatitude(), listing.getLongitude());

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(target)
                            .title(listing.getAddress())
                            .snippet(String.valueOf(listing.getBooliId())));
            bounds.include(target);
        }

        if (mapReady) {
            updateMap();
        }

    }

    @Override
    public void onSearchStarted() {

    }

    private void updateMap() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 15));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String s = marker.getSnippet();
        Log.d("Living", "Marker string " + s);
        if (!TextUtils.isEmpty(s)) {
            int id = Integer.parseInt(s);

            Listing listing = database.getListing(id);
            if (listing != null) {
                Intent intent = new Intent(getActivity(), DetailsActivity_.class);
                intent.putExtra("id", listing.getBooliId());
                startActivity(intent);

            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        android.location.Location location = mLocationClient.getLastLocation();
        LatLng target = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mapReady = true;
        updateMap();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onMapLoaded() {
        mLocationClient.connect();
    }
}
