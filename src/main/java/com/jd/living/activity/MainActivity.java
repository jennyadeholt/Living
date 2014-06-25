package com.jd.living.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.jd.living.R;
import com.jd.living.activity.favorites.FavoriteDatabase;
import com.jd.living.activity.favorites.FavoriteList_;
import com.jd.living.activity.searchList.SearchListAction;
import com.jd.living.activity.searchList.SearchResult_;
import com.jd.living.activity.settings.SearchPreferences_;
import com.jd.living.drawer.DrawerActivity;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class MainActivity extends DrawerActivity {

    @Bean
    FavoriteDatabase favoriteDatabase;

    @Bean
    ListingsDatabase listingsDatabase;

    private SearchResult_ searchResult;
    private SearchPreferences_ newSearch;
    private FavoriteList_ favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        searchResult = new SearchResult_();
        newSearch = new SearchPreferences_();
        favoriteList = new FavoriteList_();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, searchResult);
        transaction.add(R.id.content_frame, newSearch);
        transaction.add(R.id.content_frame, favoriteList);
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
            case 0:
                transaction.hide(newSearch);
                transaction.hide(favoriteList);
                transaction.show(searchResult);
                searchResult.onShowSearch();
                break;
            case 1:
                transaction.hide(favoriteList);
                transaction.hide(searchResult);
                transaction.show(newSearch);
                break;
            case 3:
                transaction.hide(newSearch);
                transaction.hide(searchResult);
                transaction.show(favoriteList);
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
            selectItem(0);
        } else {
            super.onBackPressed();
        }
    }

    public void doSearch(View v) {
        listingsDatabase.launchListingsSearch();
        selectItem(0);
    }

    public void clearSearch(View v) {

    }

    @Override
    protected List<SearchListAction> getActions() {
        List<SearchListAction> actionList = new ArrayList<SearchListAction>();
        actionList.add(SearchListAction.SEARCH_RESULT);
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
