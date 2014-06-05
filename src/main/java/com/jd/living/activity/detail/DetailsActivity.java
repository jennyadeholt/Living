package com.jd.living.activity.detail;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.jd.living.R;
import com.jd.living.activity.DrawerActivity;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class DetailsActivity extends DrawerActivity {

    private DetailsView_ details;
    private DetailsMap_ map;
    private DetailsWebView_ web;

    private int currentPosition = -1;
    private Listing listing;

    @Bean
    ListingsDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.main);

        details = new DetailsView_();
        map = new DetailsMap_();
        web = new DetailsWebView_();

        mActionTitles = getResources().getStringArray(R.array.details_actions);

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, (Fragment) web);
        transaction.add(R.id.content_frame, (Fragment) map);
        transaction.add(R.id.content_frame, (Fragment) details);

        transaction.commit();

        setup(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getIntExtra("id", -1);
        listing = database.getListing(id);

        mTitle = listing.getAddress();
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = (listing != null ? listing.getAddress() : "");
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = getIntent().getIntExtra("id", -1);
        listing = database.getListing(id);
    }

    @Override
    protected void selectItem(int position) {

        if (currentPosition != position) {
            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (position) {
                case 0:
                    transaction.hide((Fragment) web);
                    transaction.hide((Fragment) map);
                    transaction.show((Fragment) details);
                    break;
                case 1:
                    transaction.hide((Fragment) web);
                    transaction.hide((Fragment) details);
                    transaction.show((Fragment) map);
                    break;
                case 2:
                    transaction.hide((Fragment) map);
                    transaction.hide((Fragment) details);
                    transaction.show((Fragment) web);
                    break;
            }
            transaction.commit();
        }
        super.selectItem(position);
    }

    public void goToHomepage(View v){
        String url = listing.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}