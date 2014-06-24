package com.jd.living.activity.details;

import java.io.InputStream;
import java.net.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;


@EFragment
public class DetailsView extends Fragment implements GoogleMap.OnMapClickListener, View.OnTouchListener {

    @ViewById
    View mainLayout;

    @ViewById
    TableLayout tableLayout;

    @ViewById(R.id.number_of_objects)
    TextView nbrOfObjects;

    @ViewById
    TextView address;

    @ViewById
    TextView area;

    @ViewById
    ImageView thumbnail;

    @ViewById
    WebView webView;

    @Bean
    ListingsDatabase listingsDatabase;

    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng target;
    protected Listing listing;

    private int objectPosition = 0;

    /**
     * Create a new instance of CountingFragment, providing "objectPosition"
     * as an argument.
     */

    static DetailsView newInstance(int num) {
        DetailsView f = new DetailsView_();
        Bundle args = new Bundle();
        args.putInt("objectPosition", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Living", "onCreateView ");
        View v = inflater.inflate(R.layout.details_view, container, false);
        mapView = (MapView) v.findViewById(R.id.mapFragment);
        mapView.onCreate(savedInstanceState);

        return v;
    }

    @AfterViews
    protected void init(){
        objectPosition = getArguments() != null ? getArguments().getInt("objectPosition") : 1;
        Log.d("Living", "init " + objectPosition);

        listing = listingsDatabase.getListingFromList(objectPosition);

        googleMap = mapView.getMap();

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setAllGesturesEnabled(false);


        webView.setOnTouchListener(this);
        mainLayout.setOnTouchListener(this);

        setDetails();
        setMap();
        setWebView();
        getImage();
    }

    private void setDetails() {

        tableLayout.removeAllViews();

        address.setText(listing.getAddress());
        area.setText(listing.getArea());

        nbrOfObjects.setText((objectPosition + 1) + "/" + listingsDatabase.getNumberOfObject());

        addDetails(R.string.details_list_price, getString(R.string.details_list_price_text, listing.getListPrice()));
        addDetails(R.string.details_living_area, getString(R.string.details_living_area_text, listing.getLivingArea()));
        addDetails(R.string.details_type, listing.getObjectType());

        if (!listing.getRent().equals("0")) {
            addDetails(R.string.details_rent, getString(R.string.details_rent_text, listing.getRent()));
        }
        if (listing.getFloor() != 0) {
            addDetails(R.string.details_floor, getString(R.string.details_floor_text, listing.getFloor()));
        }

        addDetails(R.string.details_rooms,getString(R.string.details_room_text, listing.getRooms()));
        addDetails(R.string.details_published, listing.getPublished());
        addDetails(R.string.details_construction_year, String.valueOf(listing.getConstructionYear()));
        addDetails(R.string.details_source, listing.getSource());
    }

    private void addDetails(int nameId, String content) {
        View row = getActivity().getLayoutInflater().inflate(R.layout.details_row_table, null);

        ((TextView) row.findViewById(R.id.extra_name)).setText(nameId);
        ((TextView) row.findViewById(R.id.content)).setText(content);
        tableLayout.addView(row);
    }

    private void setMap() {
        googleMap.clear();
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
    }

    @Background
    public void getImage() {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_DOWN){
            startActivity(new Intent(getActivity(), DetailsWebView_.class));
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        startActivity(new Intent(getActivity(), DetailsMap_.class));
    }

    private void setWebView() {
        String url = listing.getUrl() + "/bilder/";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl(url);
    }

    public void setSelected() {
        if (listing != null) {
            getActivity().setTitle(listing.getAddress());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
