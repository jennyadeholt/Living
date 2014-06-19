package com.jd.living.activity.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;


@EFragment(R.layout.details_map_view)
public class DetailsMap extends DetailsFragment {

    @FragmentById
    MapFragment detailsMap;

    private GoogleMap googleMap;
    private LatLng target;

    @Override
    protected void onInit(){
        googleMap = detailsMap.getMap();
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onUpdate() {
        if (listing != null) {
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
    }
}
