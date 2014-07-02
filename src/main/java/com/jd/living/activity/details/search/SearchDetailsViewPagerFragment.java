package com.jd.living.activity.details.search;

import java.util.HashMap;
import java.util.List;
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
import com.jd.living.activity.details.DetailsView;
import com.jd.living.activity.details.DetailsView_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

@EFragment(R.layout.fragment_pager)
public class SearchDetailsViewPagerFragment extends Fragment implements DatabaseHelper.DatabaseListener {

    private static int LOOPS_COUNT = 1000;
    private int NUM_PAGES = 0;

    @ViewById
    ViewPager pager;

    @Bean
    DatabaseHelper database;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private int currentPageIndex;

    @AfterViews
    public void init() {
        database.addDatabaseListener(this);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (currentPageIndex != i) {
                    DetailsView view =  mPagerAdapter.getItem(i);
                    view.setSelected();
                    currentPageIndex = i;
                    database.setCurrentListIndex(LOOPS_COUNT == 1 ? currentPageIndex : currentPageIndex % NUM_PAGES);
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
    public void onUpdate(List<Listing> result) {
        LOOPS_COUNT = result.size() < 3 ? 1 : 1000;
        NUM_PAGES = result.size();

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
        int position = database.getListIndex(booliId);
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
        private Map<Integer, DetailsView_> details = new HashMap<Integer, DetailsView_>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void clearContent() {
            details = new HashMap<Integer, DetailsView_>();
        }

        @Override
        public DetailsView_ getItem(int position) {
            position = LOOPS_COUNT == 1 ? position : position % NUM_PAGES;
            if (!details.containsKey(position)) {
                details.put(position, DetailsView_.newInstance(position));
            }
            return details.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES * LOOPS_COUNT;
        }
    }
}