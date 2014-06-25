package com.jd.living.activity.searchList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.widget.TabHost;

import com.jd.living.R;
import com.jd.living.activity.map.ResultMapFragment_;

@EFragment(R.layout.tab_host)
public class SearchMain extends Fragment {

    @ViewById(R.id.tabhost)
    TabHost mTabHost;

    private SearchList_ list;
    private ResultMapFragment_ map;

    @AfterViews
    public void init(){
        mTabHost.setup();

        mTabHost.addTab(mTabHost.newTabSpec("list")
                        .setIndicator("", getResources().getDrawable(R.drawable.ic_menu_copy_holo_dark))
                        .setContent(new DetailsTabContent(getActivity()))
        );

        mTabHost.addTab(mTabHost.newTabSpec("map")
                        .setIndicator("", getResources().getDrawable(R.drawable.ic_menu_mapmode))
                        .setContent(new DetailsTabContent(getActivity()))
        );


        list = new SearchList_();
        map = new ResultMapFragment_();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.realtabcontent, list, "list");
        ft.add(R.id.realtabcontent, map, "map");
        ft.commit();

        updateTab("list");


        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab(tabId);
            }
        };

        mTabHost.setOnTabChangedListener(tabChangeListener);
    }

    private void updateTab(String tabId) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (tabId.equals("list")) {
            ft.hide(map);
            ft.show(list);
        }  else if (tabId.equals("map")) {
            ft.hide(list);
            ft.show(map);
        }

        ft.commit();

    }


    private class DetailsTabContent implements TabHost.TabContentFactory {
        private Context mContext;

        public DetailsTabContent(Context context){
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            return new View(mContext);
        }
    }
}
