package com.jd.living.activity.details.search;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;

import com.jd.living.activity.details.DetailsView;
import com.jd.living.model.Listing;


@EFragment
public class SearchDetailsView extends DetailsView {

    public static SearchDetailsView newInstance(int num) {
        SearchDetailsView f = new SearchDetailsView_();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    protected Listing getListing() {
        return listingsDatabase.getListingFromList(objectIndex);
    }
}
