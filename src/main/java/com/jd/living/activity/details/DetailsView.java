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
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;


@EFragment(R.layout.details_view)
public class DetailsView extends Fragment implements GoogleMap.OnMapClickListener {

    @ViewById
    protected TableLayout tableLayout;

    @ViewById(R.id.number_of_objects)
    protected TextView nbrOfObjects;

    @ViewById
    protected TextView address;

    @ViewById
    protected TextView area;

    @ViewById
    protected ImageView thumbnail;

    @ViewById
    protected ImageView favorite;

    @ViewById
    protected WebView webView;

    @Bean
    DatabaseHelper database;

    @Bean
    FavoriteDatabase favoriteDatabase;

    protected MapView mapView;
    protected GoogleMap googleMap;
    protected LatLng target;
    protected Listing listing;

    protected int objectIndex = 0;

    public static DetailsView_ newInstance(int num) {
        DetailsView_ f = new DetailsView_();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_view, container, false);

        mapView = (MapView) v.findViewById(R.id.mapFragment);
        mapView.onCreate(savedInstanceState);
        return v;
    }

    @AfterViews
    public void init() {
        objectIndex = getArguments() != null ? getArguments().getInt("objectIndex") : 1;
        listing = database.getListingBasedOnLocation(objectIndex);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), DetailsWebView_.class);
                    intent.putExtra("booliId", listing.getBooliId());
                    startActivity(intent);
                }
                return false;
            }
        });

        favorite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateFavorite(true);
                return false;
            }
        });

        if(listing != null) {
            update();
        } else {
            getActivity().onBackPressed();
        }
    }

    public void update(int booliId) {
        this.listing = database.getListing(booliId);
        objectIndex = database.getListIndex(booliId);

        update();
    }

    private void update() {
        setupMap();
        setupDetails();
        updateFavorite(false);
        setupWebView();
        getImage();
    }

    protected Listing getListing() {
        return listing;
    }

    protected void updateFavorite(boolean onTouch) {
        boolean isFavorite = favoriteDatabase.isFavorite(listing);
        int resId = R.drawable.btn_star_off_disabled_focused_holo_light;

        if (listing.isSold()) {
            favorite.setVisibility(View.GONE);
        } else  {
            if (onTouch) {
                int resText = R.string.toast_removed_favorite;
                if (!isFavorite) {
                    resText = R.string.toast_added_favorite;
                    resId = R.drawable.btn_rating_star_on_normal_holo_light;
                }
                favoriteDatabase.updateFavorite(listing);
                if (database.getDatabaseState() == DatabaseHelper.DatabaseState.FAVORITE) {
                    getActivity().onBackPressed();
                }

                Toast.makeText(getActivity(), resText, Toast.LENGTH_SHORT).show();
            } else if (isFavorite) {
                resId = R.drawable.btn_rating_star_on_normal_holo_light;
            }
            favorite.setVisibility(View.VISIBLE);
            favorite.setImageResource(resId);
        }
    }

    protected void setupDetails() {
        boolean isSold = listing.isSold();

        tableLayout.removeAllViews();
        address.setText(listing.getAddress());
        area.setText(listing.getArea());

        int totalSize = database.getResult().size();
        nbrOfObjects.setText(totalSize == 1 ?  "" : (objectIndex + 1) + "/" + totalSize);

        if (isSold) {
            addDetails(R.string.details_sold_price, listing.getSoldPrice());
        }

        addDetails(R.string.details_list_price, listing.getListPrice());
        addDetails(R.string.details_living_area, getString(R.string.details_living_area_text, listing.getLivingArea()));
        addDetails(R.string.details_type, listing.getObjectType());

        if (!listing.getRent().equals("0")) {
            addDetails(R.string.details_rent, listing.getRent());
        }
        if (listing.getFloor() != 0) {
            addDetails(R.string.details_floor, getString(R.string.details_floor_text, listing.getFloor()));
        }

        addDetails(R.string.details_rooms,getString(R.string.details_room_text, listing.getRooms()));
        addDetails(R.string.details_published, listing.getPublished());

        if (isSold) {
            addDetails(R.string.details_sold, listing.getSoldDate());
        }

        addDetails(R.string.details_construction_year, String.valueOf(listing.getConstructionYear()));
        addDetails(R.string.details_source, listing.getSource());
    }

    protected void addDetails(int nameId, String content) {
        View row = getActivity().getLayoutInflater().inflate(R.layout.details_row_table, null);

        ((TextView) row.findViewById(R.id.extra_name)).setText(nameId);
        ((TextView) row.findViewById(R.id.content)).setText(content);
        tableLayout.addView(row);
    }

    protected void setupMap() {
        googleMap = mapView.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.clear();
        target = new LatLng(listing.getLatitude(), listing.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build();

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
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(getActivity(), DetailsMap_.class);
        intent.putExtra("booliId", listing.getBooliId());
        startActivity(intent);
    }

    private void setupWebView() {
        String url = listing.getUrl() + "/bilder/";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
        webView.loadUrl(url);
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

