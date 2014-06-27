package com.jd.living.activity.details.favorites;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;

import com.jd.living.activity.details.DetailsView;
import com.jd.living.model.Listing;

@EFragment
public class FavoriteDetailsView extends DetailsView {

    static FavoriteDetailsView newInstance(int num) {
        FavoriteDetailsView f = new FavoriteDetailsView_();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    protected Listing getListing() {
        return favoriteDatabase.getListingFromList(objectIndex);
    }
}
