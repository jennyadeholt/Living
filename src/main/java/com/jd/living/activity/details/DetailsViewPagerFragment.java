package com.jd.living.activity.details;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.jd.living.R;
import com.jd.living.activity.details.search.SearchDetailsView;
import com.jd.living.model.Result;
import com.jd.living.database.ListingsDatabase;

@EFragment(R.layout.fragment_pager)
public class DetailsViewPagerFragment extends Fragment implements ListingsDatabase.ListingsListener, ListingsDatabase.DetailsListener{

    private static int LOOPS_COUNT = 1000;
    private int NUM_PAGES = 0;

    @ViewById
    ViewPager pager;

    @Bean
    ListingsDatabase database;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private int currentPageIndex;

    @AfterViews
    public void init() {
        database.registerListingsListener(this);
        database.registerDetailsListener(this);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (currentPageIndex != i) {
                    SearchDetailsView view =  mPagerAdapter.getItem(i);
                    view.setSelected();
                    currentPageIndex = i;
                    database.setCurrentIndex(LOOPS_COUNT == 1 ? currentPageIndex : currentPageIndex % NUM_PAGES);
                }
            }

            @Override
            public void onPageSelected(int i) {
                currentPageIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int currentState) {

            }
        });

        pager.setOffscreenPageLimit(1);
    }

    @UiThread
    @Override
    public void onUpdate(Result result) {
        LOOPS_COUNT = result.count < 3 ? 1 : 1000;
        NUM_PAGES = result.count;

        if (mPagerAdapter == null) {
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
            pager.setAdapter(mPagerAdapter);
        } else {
            currentPageIndex = -1;
            mPagerAdapter.clearContent();
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onDetailsRequested(int booliId) {
        int position = database.getListLocation(booliId);
        position += LOOPS_COUNT == 1 ? 0 : (LOOPS_COUNT * NUM_PAGES) / 2;
        pager.setCurrentItem(position, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mPagerAdapter != null) {
            mPagerAdapter.clearContent();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, SearchDetailsView> details = new HashMap<Integer, SearchDetailsView>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void clearContent() {
            details = new HashMap<Integer, SearchDetailsView>();
        }

        @Override
        public SearchDetailsView getItem(int position) {
            position = LOOPS_COUNT == 1 ? position : position % NUM_PAGES;
            if (!details.containsKey(position)) {
                details.put(position, SearchDetailsView.newInstance(position));
            }
            return details.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES * LOOPS_COUNT;
        }
    }
}