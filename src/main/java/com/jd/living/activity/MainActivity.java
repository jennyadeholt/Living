package com.jd.living.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.Fragment;
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
import com.jd.living.activity.searchList.SearchListAction;
import com.jd.living.activity.searchList.SearchResult_;
import com.jd.living.activity.settings.SearchPreferences_;
import com.jd.living.drawer.DrawerActivity;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class MainActivity extends DrawerActivity {

    @Bean
    ListingsDatabase listingsDatabase;

    private SearchResult_ searchResult;
    private SearchPreferences_ newSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        currentPosition = 1;

        searchResult = new SearchResult_();
        newSearch = new SearchPreferences_();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, (Fragment) searchResult);
        transaction.add(R.id.content_frame, (Fragment) newSearch);
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

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (position) {
            case 1:
                transaction.hide((Fragment) newSearch);
                transaction.show((Fragment) searchResult);
                searchResult.onShowSearch();
                break;
            case 3:
                transaction.hide((Fragment) searchResult);
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

    @Override
    public void onBackPressed() {
        if (searchResult.isVisible() && searchResult.isDetailsShown()) {
            searchResult.onShowSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void doSearch(View v) {
        listingsDatabase.launchListingsSearch();
        selectItem(1);
    }

    public void clearSearch(View v) {

    }

    public void goToHomepage(View v){
        String url = listingsDatabase.getListing().getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void setFavorite(View v) {
        Toast.makeText(this, "Favorite", Toast.LENGTH_LONG).show();

        ((ImageView) v).setImageResource(R.drawable.btn_rating_star_on_normal_holo_light);
    }

    @Override
    protected List<SearchListAction> getActions() {
        List<SearchListAction> actionList = new ArrayList<SearchListAction>();
        actionList.add(SearchListAction.RESULT_HEADER);
        actionList.add(SearchListAction.SEARCH_RESULT);

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
}
