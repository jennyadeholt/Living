package com.jd.living.activity.map;

import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.activity.detail.DetailsView_;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;


@EFragment(R.layout.map)
public class ResultMapFragment extends Fragment implements ListingsDatabase.ListingsListener, OnInfoWindowClickListener {

    @FragmentById
    MapFragment resultMap;

    @ViewById
    FrameLayout mapView;

    @Bean
    ListingsDatabase database;

    private GoogleMap googleMap;

    private LatLngBounds.Builder bounds;
    private boolean mapReady = false;

    @AfterViews
    public void init(){

        googleMap = resultMap.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnInfoWindowClickListener(this);

        database.addListingsListener(this);
    }

    @Override
    public void onUpdate(List<Listing> listings) {
        bounds = new LatLngBounds.Builder();
        boolean firstLocation = true;
        for (Listing listing : listings) {
            double latitude = listing.getLatitude();
            double longitude = listing.getLongitude();

            LatLng target = new LatLng(latitude, longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(target)
                    .title(listing.getAddress())
                    .snippet(String.valueOf(listing.getBooliId()));

            if (firstLocation && mapReady) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(target)      // Sets the center of the map to Mountain View
                        .zoom(13)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                firstLocation = false;

            }

            googleMap.addMarker(options);
            bounds.include(target);
        }

        if (!mapReady) {
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mapReady = true;
                    updateMap();
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 15));

                }
            });
        } else {
           updateMap();
        }

    }

    private void updateMap() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 15));

        mapView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String s = marker.getSnippet();
        Log.d("Living", "Marker string " + s);
        if (!TextUtils.isEmpty(s)) {
            int id = Integer.parseInt(s);

            Listing listing = database.getListing(id);
            if (listing != null) {
                Intent intent = new Intent(getActivity(), DetailsView_.class);
                intent.putExtra("id", listing.getBooliId());
                startActivity(intent);

            }
        }

    }
}
