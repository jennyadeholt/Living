package com.jd.living.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.activity.detail.DetailsMain_;
import com.jd.living.activity.map.ResultMapFragment_;
import com.jd.living.activity.searchList.SearchListAction;
import com.jd.living.activity.searchList.SearchList_;
import com.jd.living.activity.settings.SearchPreferences_;
import com.jd.living.drawer.DrawerActivity;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class MainActivity extends DrawerActivity implements ListingsDatabase.DetailsListener {

    @Bean
    ListingsDatabase listingsDatabase;

    private SearchList_ searchList;
    private SearchPreferences_ newSearch;
    private ResultMapFragment_ resultMap;
    private DetailsMain_ detailsMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listingsDatabase.addDetailsListener(this);

        currentPosition = 1;

        searchList = new SearchList_();
        resultMap = new ResultMapFragment_();
        newSearch = new SearchPreferences_();
        detailsMain = new DetailsMain_();

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, (Fragment) resultMap);
        transaction.add(R.id.content_frame, (Fragment) searchList);
        transaction.add(R.id.content_frame, (Fragment) newSearch);
        transaction.add(R.id.content_frame, (Fragment) detailsMain);

        transaction.commit();

        setup(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mContainer);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void selectItem(int position) {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position) {
            case 1:
                transaction.hide((Fragment) detailsMain);
                transaction.hide((Fragment) newSearch);
                transaction.hide((Fragment) resultMap);
                transaction.show((Fragment) searchList);
                break;
            case 2:
                transaction.hide((Fragment) detailsMain);
                transaction.hide((Fragment) newSearch);
                transaction.hide((Fragment) searchList);
                transaction.show((Fragment) resultMap);
                break;
            case 4:
                transaction.hide((Fragment) detailsMain);
                transaction.hide((Fragment) resultMap);
                transaction.hide((Fragment) searchList);
                transaction.show((Fragment) newSearch);
                break;
            case 5:
                break;
            default:
                break;
        }


        transaction.commit();
        currentPosition = position;

        super.selectItem(position);
    }

    public void doSearch(View v) {
        listingsDatabase.launchListingsSearch();
        selectItem(1);
    }

    public void clearSearch(View v) {

    }

    public void goToHomepage(View v){
        String url = ""; //listing.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void setFavorite(View v) {
        Toast.makeText(this, "Favoite", Toast.LENGTH_LONG).show();

        ((ImageView) v).setImageResource(R.drawable.btn_rating_star_on_normal_holo_light);
    }


    @Override
    protected List<SearchListAction> getActions() {
        List<SearchListAction> actionList = new ArrayList<SearchListAction>();
        actionList.add(SearchListAction.RESULT_HEADER);
        actionList.add(SearchListAction.SEARCH_RESULT);
        actionList.add(SearchListAction.MAP);
        actionList.add(SearchListAction.SETTINGS_HEADER);
        actionList.add(SearchListAction.NEW_SEARCH);
        actionList.add(SearchListAction.SEARCHES);
        actionList.add(SearchListAction.FAVORITES);
        actionList.add(SearchListAction.SETTINGS);
        actionList.add(SearchListAction.HELP);
        actionList.add(SearchListAction.ABOUT);
        return actionList;
    }

    @Override
    protected int getStartPosition() {
        return currentPosition;
    }

    @Override
    public void onDetailsRequested(int booliId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.hide((Fragment) newSearch);
        transaction.hide((Fragment) resultMap);
        transaction.hide((Fragment) searchList);
        transaction.show((Fragment) detailsMain);

        transaction.commit();
    }
}
