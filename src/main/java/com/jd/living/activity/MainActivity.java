package com.jd.living.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.jd.living.R;
import com.jd.living.activity.map.ResultMapFragment_;
import com.jd.living.activity.searchList.SearchListAction;
import com.jd.living.activity.searchList.SearchList_;
import com.jd.living.activity.settings.SearchPreferences_;
import com.jd.living.drawer.DrawerActivity;
import com.jd.living.server.ListingsDatabase;

@EActivity
public class MainActivity extends DrawerActivity {

    @Bean
    ListingsDatabase listingsDatabase;

    private SearchList_ searchList;
    private SearchPreferences_ newSearch;
    private ResultMapFragment_ resultMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        currentPosition = 1;

        searchList = new SearchList_();
        resultMap = new ResultMapFragment_();
        newSearch = new SearchPreferences_();

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, (Fragment) resultMap);
        transaction.add(R.id.content_frame, (Fragment) searchList);
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

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position) {
            case 1:
                transaction.hide((Fragment) newSearch);
                transaction.hide((Fragment) resultMap);
                transaction.show((Fragment) searchList);
                break;
            case 2:
                transaction.hide((Fragment) newSearch);
                transaction.hide((Fragment) searchList);
                transaction.show((Fragment) resultMap);
                break;
            case 4:
                transaction.hide((Fragment) resultMap);
                transaction.hide((Fragment) searchList);
                transaction.show((Fragment) newSearch);
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
}
