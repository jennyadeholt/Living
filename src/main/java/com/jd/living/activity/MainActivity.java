package com.jd.living.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.jd.living.R;
import com.jd.living.activity.search.SearchListAction;
import com.jd.living.activity.search.SearchResult_;
import com.jd.living.activity.settings.SearchPreferences_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.drawer.DrawerActivity;

@EActivity
public class MainActivity extends DrawerActivity {

    @Bean
    DatabaseHelper database;

    private SearchResult_ searchResult;
    private SearchPreferences_ searchPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        searchResult = new SearchResult_();
        searchPreferences = new SearchPreferences_();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.add(R.id.content_frame, searchResult, "searchResult");
        transaction.add(R.id.content_frame, searchPreferences, "searchPreferences");
        transaction.commit();

        setup(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mContainer);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void selectItem(int position) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (position) {
            case 1:
            case 4:
                transaction.hide(searchPreferences);
                transaction.show(searchResult);
                if (position == 1) {
                   database.setDatabaseState(DatabaseHelper.DatabaseState.SEARCH);
                } else {
                   database.setDatabaseState(DatabaseHelper.DatabaseState.FAVORITE);
                }

                searchResult.onShowSearch();

                break;
            case 2:
                transaction.hide(searchResult);
                transaction.show(searchPreferences);
                break;
            default:
                break;
        }
        transaction.commit();
        currentPosition = position;
        super.selectItem(position);
    }

    public void doSearch(View v) {
        database.launchSearch();
        selectItem(1);
    }

    public void clearSearch(View v) {

    }

    @Override
    public void onBackPressed() {
        if (searchResult.isVisible() && searchResult.isDetailsShown()) {
            searchResult.onShowSearch();
            int index = database.getDatabaseState() == DatabaseHelper.DatabaseState.FAVORITE ? 4 : 1;
            setTitle(mDrawerListAdapter.getItem(index).getTextRes());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected List<SearchListAction> getActions() {
        List<SearchListAction> actionList = new ArrayList<SearchListAction>();
        actionList.add(SearchListAction.RESULT_HEADER);
        actionList.add(SearchListAction.SEARCH_RESULT);
        actionList.add(SearchListAction.NEW_SEARCH);
        actionList.add(SearchListAction.SEARCHES);
        actionList.add(SearchListAction.FAVORITES);
        actionList.add(SearchListAction.SETTINGS_HEADER);
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
