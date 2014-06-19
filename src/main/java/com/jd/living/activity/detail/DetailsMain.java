package com.jd.living.activity.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.jd.living.R;

@EFragment(R.layout.details_main)
public class DetailsMain extends Fragment {

    @ViewById(android.R.id.tabhost)
    TabHost mTabHost;

    private DetailsWebView_ web;
    private DetailsMap_ map;
    private DetailsView_ details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init(){
        mTabHost.setup();

        mTabHost.addTab(mTabHost.newTabSpec("details")
                        .setIndicator("", getResources().getDrawable(R.drawable.details))
                        .setContent(new DetailsTabContent(getActivity()))
        );

        mTabHost.addTab(mTabHost.newTabSpec("images")
                        .setIndicator(getString(R.string.images), getResources().getDrawable(R.drawable.images))
                        .setContent(new DetailsTabContent(getActivity()))
        );
        mTabHost.addTab(mTabHost.newTabSpec("map")
                .setIndicator(getString(R.string.map_result), getResources().getDrawable(R.drawable.map))
                .setContent(new DetailsTabContent(getActivity())));

        web = new DetailsWebView_();
        details = new DetailsView_();
        map = new DetailsMap_();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.realtabcontent, details, "details");
        ft.add(R.id.realtabcontent, web, "web");
        ft.add(R.id.realtabcontent, map, "map");
        ft.commit();

        updateTab("details");

        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                updateTab(tabId);
            }
        };

        mTabHost.setOnTabChangedListener(tabChangeListener);
    }

    private void updateTab(String tabId) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (tabId.equals("details")) {
            ft.hide(web);
            ft.hide(map);
            ft.show(details);
        } else if (tabId.equals("images")) {
            ft.hide(details);
            ft.hide(map);
            ft.show(web);
        } else if (tabId.equals("map")) {
            ft.hide(web);
            ft.hide(details);
            ft.show(map);
        }

        ft.commit();
    }
}
