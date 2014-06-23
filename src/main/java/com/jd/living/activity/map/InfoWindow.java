package com.jd.living.activity.map;

import java.io.InputStream;
import java.net.URL;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.jd.living.R;
import com.jd.living.model.Listing;

@EViewGroup(R.layout.map_info_window)
public class InfoWindow extends LinearLayout {

    @ViewById
    TextView text;

    @ViewById
    ImageView image;

    private Listing listing;
    private Marker marker;

    public InfoWindow(Context context) {
        super(context);
    }

    public void bind(final Listing listing, final Marker marker) {

        this.listing = listing;
        this.marker = marker;
        text.setText(listing.getAddress());
        getImage();
    }

    @Background
    public void getImage() {
        Drawable drawable = null;
        try {
            InputStream is = (InputStream) new URL(listing.getImageUrl()).getContent();
            drawable = Drawable.createFromStream(is, "src name");
            is.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }

        setImage(drawable);
    }

    @UiThread
    public void setImage(Drawable drawable) {

        if (drawable != null) {
            image.setImageDrawable(drawable);
        }
        marker.showInfoWindow();
    }
}
