package com.jd.living.activity.detail;

import java.io.InputStream;
import java.net.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.model.Listing;

/**
 * Created by jennynilsson on 2014-06-03.
 */
@EFragment(R.layout.details_view)
public class DetailsView extends DetailsFragment {

    @ViewById
    TextView address;

    @ViewById
    TextView area;

    @ViewById
    TextView price;

    @ViewById
    TextView rent;

    @ViewById
    TextView room;

    @ViewById
    TextView living_area;

    @ViewById
    ImageView thumbnail;

    @FragmentById
    MapFragment mapFragment;

    private GoogleMap googleMap;
    private LatLng target;

    @Override
    protected void onInit(){
        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onUpdate() {

        Log.d("Living", "DetailsView");
        if (listing != null) {
            Log.d("Living", "DetailsView 2");
            googleMap.clear();

            address.setText(listing.getAddress());
            area.setText(listing.getArea());
            living_area.setText(listing.getLivingArea() + " kvm ");
            room.setText(listing.getRooms() + " rum");
            rent.setText(listing.getRent() + " kr/månad");
            price.setText(listing.getListPrice() + " kr");

            target = new LatLng(listing.getLatitude(), listing.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(target)      // Sets the center of the googleMap to Mountain View
                    .zoom(13)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            MarkerOptions marker = new MarkerOptions();
            marker.position(target);
            googleMap.addMarker(marker);

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            getImage(listing);
        }
    }

    @Background
    public void getImage(Listing listing) {
        Drawable drawable = null;
        try {
            InputStream is = (InputStream) new URL(listing.getImageUrl()).getContent();
            drawable = Drawable.createFromStream(is, "src name");
            is.close();
        } catch (Exception e) {
            System.out.println("Exc="+e);
        }

        setImage(drawable);
    }

    @UiThread
    public void setImage(Drawable drawable) {
        if (drawable != null) {
            thumbnail.setImageDrawable(drawable);
        }
    }
}
