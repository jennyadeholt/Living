package com.jd.living.activity;

import org.androidannotations.annotations.EActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jd.living.R;
import com.jd.living.activity.list.SearchList_;
import com.jd.living.activity.map.ResultMapFragment_;

@EActivity
public class MainActivity extends DrawerActivity {

    private SearchList_ searchList;
    private ResultMapFragment_ resultMap;

    protected ListView mDrawerList2;

    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        searchList = new SearchList_();
        resultMap = new ResultMapFragment_();

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, (Fragment) resultMap);
        transaction.add(R.id.content_frame, (Fragment) searchList);

        transaction.commit();

        mActionTitles = getResources().getStringArray(R.array.search_actions);
        setup(savedInstanceState);

        mDrawerList2 = (ListView) findViewById(R.id.bottomList);

        mDrawerList2.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, getResources().getStringArray(R.array.optimal_actions)));
        mDrawerList2.setOnItemClickListener(new DrawerItemClickListener());

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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLinearLayout);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void selectItem(int position) {

        if (currentPosition != position) {
            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (position) {
                case 0:
                    transaction.hide((Fragment) resultMap);
                    transaction.show((Fragment) searchList);
                    break;
                case 1:
                    transaction.hide((Fragment) searchList);
                    transaction.show((Fragment) resultMap);
                    break;
            }
            transaction.commit();
        }
        super.selectItem(position);
    }
}
