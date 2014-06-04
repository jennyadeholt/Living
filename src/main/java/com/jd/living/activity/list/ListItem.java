package com.jd.living.activity.list;

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

import com.jd.living.R;
import com.jd.living.model.Listing;

@EViewGroup(R.layout.list_item)
public class ListItem extends LinearLayout {

    @ViewById
    TextView address;

    @ViewById
    TextView info;

    @ViewById
    TextView area;

    @ViewById
    TextView price;

    @ViewById
    ImageView image;

    public ListItem(Context context) {
        super(context);
    }

    public void bind(Listing listing) {
        address.setText(listing.getAddress());
        info.setText(listing.getRooms() + " rum, " + listing.getLivingArea() + " kvm på " + listing.getFloor() + " våningen");
        area.setText(listing.getArea());
        price.setText(listing.getListPrice() + " kr, " + listing.getRent() + " kr/månad");

        getImage(listing);
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
            image.setImageDrawable(drawable);
        }
    }
}