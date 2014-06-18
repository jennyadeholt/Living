package com.jd.living.activity.detail;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.activity.searchList.SearchListAction;
import com.jd.living.drawer.DrawerActivity;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class DetailsActivity extends DrawerActivity {

    private DetailsView_ details;
    private DetailsMap_ map;
    private DetailsWebView_ web;

    private Listing listing;

    @Bean
    ListingsDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPosition = 0;

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.main);

        details = new DetailsView_();
        map = new DetailsMap_();
        web = new DetailsWebView_();

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
        currentPosition = position;

        super.selectItem(position);
    }

    public void goToHomepage(View v){
        String url = listing.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void setFavorite(View v) {
        Toast.makeText(this, "Favoite", Toast.LENGTH_LONG).show();

        FragmentManager fragmentManager = getFragmentManager();
        ((ImageView) v).setImageResource(R.drawable.btn_rating_star_on_normal_holo_light);

    }

    @Override
    protected List<SearchListAction> getActions() {
        List<SearchListAction> actionList = new ArrayList<SearchListAction>();
        actionList.add(SearchListAction.DETAILS);
        actionList.add(SearchListAction.MAP);
        actionList.add(SearchListAction.IMAGES);
        return actionList;
    }

    @Override
    protected int getStartPosition() {
        return currentPosition;
    }
}
