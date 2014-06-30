package com.jd.living.activity.details.favorites;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.jd.living.R;
import com.jd.living.activity.details.DetailsView;
import com.jd.living.model.Listing;

@EFragment
public class FavoriteDetailsView extends DetailsView {

    public static FavoriteDetailsView newInstance(int num) {
        Log.d("LivingLiving", "FavoriteDetailsView.newInstance("+ num  +")");
        FavoriteDetailsView f = new FavoriteDetailsView();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    protected Listing getListing() {
        return favoriteDatabase.getListingBasedOnLocation(objectIndex);
    }
}
