package com.jd.living.activity.search;

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
        area.setText(listing.getArea());

        String listPrice = listing.getListPrice();

        if (listing.isSold()) {
            listPrice = getContext().getString(R.string.details_list_price) + " " +listPrice;
            String soldFor = getContext().getString(R.string.details_sold_price) + " ";
            soldFor += listing.getSoldPrice();
            info.setText(soldFor);
        } else {
            String rooms = getContext().getString(R.string.details_room_text, listing.getRooms());
            String livingArea = getContext().getString(R.string.details_living_area_text, listing.getLivingArea());
            info.setText(rooms + ", " + livingArea);

            if (!listing.getRent().equals("0")) {
                listPrice += ", " + listing.getRent();
            }
        }
        price.setText(listPrice);
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