package com.jd.living.activity.map;

import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.ListingsDatabase;


@EFragment(R.layout.map)
public class ResultMapFragment extends Fragment implements ListingsDatabase.ListingsListener,
        OnInfoWindowClickListener, GoogleMap.OnMapLoadedCallback{

    @FragmentById
    MapFragment resultMap;

    @ViewById
    FrameLayout mapView;

    @Bean
    ListingsDatabase database;

    @Bean
    ResultInfoWindowAdapter adapter;

    private GoogleMap googleMap;
    private LatLngBounds.Builder bounds;

    @AfterViews
    public void init(){

        googleMap = resultMap.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(adapter);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapLoadedCallback(this);

    }

    @UiThread
    @Override
    public void onUpdate(Result result) {
        googleMap.clear();

        bounds = new LatLngBounds.Builder();
        for (Listing listing : result.getResult()) {
            LatLng target = new LatLng(listing.getLatitude(), listing.getLongitude());

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(target)
                            .title(listing.getAddress())
                            .snippet(String.valueOf(listing.getBooliId())));

            bounds.include(target);
        }

        if (!result.getResult().isEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 15));
        }
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        database.setCurrentId(Integer.valueOf(marker.getSnippet()));
    }

    @Override
    public void onMapLoaded() {
        database.registerListingsListener(this);
    }
}
