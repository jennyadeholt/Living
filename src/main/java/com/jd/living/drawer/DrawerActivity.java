package com.jd.living.drawer;

import java.util.List;

import org.androidannotations.annotations.EActivity;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jd.living.R;
import com.jd.living.activity.search.SearchListAction;

/**
 * Created by jennynilsson on 2014-06-05.
 */

@EActivity
public abstract class DrawerActivity extends Activity {

    protected DrawerLayout mDrawerLayout;
    protected LinearLayout mContainer;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;

    protected DrawerListAdapter mDrawerListAdapter;

    protected CharSequence mDrawerTitle;
    protected CharSequence mTitle;
    protected List<SearchListAction> searchListActions;

    protected int currentPosition = 3;

    /* The click listner for ListView in the navigation drawer */
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            selectItem(position);
        }
    }

    protected void setup(Bundle savedInstanceState) {
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.topList);
        mContainer = (LinearLayout) findViewById(R.id.container);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setStartDelay(LayoutTransition.CHANGE_APPEARING, 1000);
        layoutTransition.setDuration(LayoutTransition.CHANGE_DISAPPEARING, 1000);
        mDrawerLayout.setLayoutTransition(layoutTransition);

        searchListActions = getActions();

        mDrawerListAdapter = new DrawerListAdapter(this);
        mDrawerListAdapter.setContent(searchListActions);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(getStartPosition());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    protected void selectItem(int position) {
        //mDrawerList.setItemChecked(position, true);
        mDrawerListAdapter.setSelected(position);
        setTitle(mDrawerListAdapter.getItem(position).getTextRes());
        SearchListAction action = mDrawerListAdapter.getItem(position);
        if (!action.isHeader()) {
            mDrawerLayout.closeDrawer(mContainer);
        }
    }

    protected abstract List<SearchListAction> getActions();

    protected abstract int getStartPosition();
}
