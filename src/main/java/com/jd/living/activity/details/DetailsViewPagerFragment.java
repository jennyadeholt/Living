package com.jd.living.activity.details;

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
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

@EFragment(R.layout.fragment_pager)
public class DetailsViewPagerFragment extends Fragment implements DatabaseHelper.DatabaseListener {

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
                    getActivity().setTitle(mPagerAdapter.getPageTitle(i));
                    currentPageIndex = i;
                    database.setCurrentListIndex(LOOPS_COUNT == 1 ? currentPageIndex : currentPageIndex % NUM_PAGES);
                }
            }

            @Override
            public void onPageSelected(int i) {
                currentPageIndex = i;
                getActivity().setTitle(mPagerAdapter.getPageTitle(i));

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
        LOOPS_COUNT = result.size() == 1 ? 1 : 1000;
        NUM_PAGES = result.size();

        if (mPagerAdapter == null) {
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
            pager.setAdapter(mPagerAdapter);
        } else {
            mPagerAdapter.updateContent();
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onFavoriteUpdated() {

    }

    @Override
    public void onDetailsRequested(int booliId) {
        int position = database.getListIndex(booliId);
        position += LOOPS_COUNT == 1 ? 0 : (LOOPS_COUNT * NUM_PAGES) / 2;
        pager.setCurrentItem(position, false);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, DetailsView_> details = new HashMap<Integer, DetailsView_>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Listing listing = database.getListingBasedOnLocation(LOOPS_COUNT == 1 ? position : position % NUM_PAGES);
            return listing.getAddress();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void updateContent() {
            for (DetailsView_ detail : details.values()) {
                getFragmentManager().beginTransaction().remove(detail).commit();
            }
            details = new HashMap<Integer, DetailsView_>();
        }

        @Override
        public DetailsView_ getItem(int position) {
            position = LOOPS_COUNT == 1 ? position : position % NUM_PAGES;

            if (!details.containsKey(position)) {
                details.put(position, DetailsView_.newInstance(position));
            }
            return DetailsView_.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES * LOOPS_COUNT;
        }
    }
}