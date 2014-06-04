package com.jd.living.activity.detail;

import java.io.InputStream;
import java.net.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

/**
 * Created by jennynilsson on 2014-06-03.
 */
@EActivity(R.layout.detail_view)
public class DetailsView extends FragmentActivity {

    @Bean
    ListingsDatabase database;

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


    @ViewById
    View mapView;

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private Listing listing;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Living", "onResume");
        int id = getIntent().getIntExtra("id", -1);
        listing = database.getListing(id);
        if (listing != null) {
            updateUI(listing);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("Living", "onNewIntent");
        int id = getIntent().getIntExtra("id", -1);
        listing = database.getListing(id);
        if (listing != null) {
            updateUI(listing);
        }
    }

    @AfterViews
    public void init(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
    }

    public void goToHomepage(View v){
        String url = listing.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void updateUI(Listing listing) {


        address.setText(listing.getAddress());
        area.setText(listing.getArea());
        living_area.setText(listing.getLivingArea() + " kvm ");
        room.setText(listing.getRooms() + " rum");
        rent.setText(listing.getRent() + " kr/månad");
        price.setText(listing.getListPrice() + " kr");


        LatLng target = new LatLng(listing.getLatitude(), listing.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        MarkerOptions marker = new MarkerOptions();
        marker.position(target);
        map.addMarker(marker);

        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        getImage(listing);

    }

    @ItemClick
    void imageItemClicked() {
        /**
         if (!url.startsWith("http://") && !url.startsWith("https://")) {
         url = "http://" + url;
         }
         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
         */
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
