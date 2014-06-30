package com.jd.living.activity.details.favorites;

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
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;

@EFragment(R.layout.favorite_fragment_pager)
public class FavoriteDetailsViewPagerFragment extends Fragment implements FavoriteDatabase.FavoriteListener{

    private static int LOOPS_COUNT = 1000;
    private int NUM_PAGES = 0;

    @ViewById
    ViewPager pager;

    @Bean
    FavoriteDatabase database;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private int currentPageIndex;

    @AfterViews
    public void init() {
        database.addFavoriteListener(this);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (currentPageIndex != i) {
                    FavoriteDetailsView view =  mPagerAdapter.getItem(i);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mPagerAdapter != null) {
            mPagerAdapter.clearContent();
        }
    }

    @UiThread
    @Override
    public void updatedFavorites(List<Listing> listings) {
        LOOPS_COUNT = listings.size() < 3 ? 1 : 1000;
        NUM_PAGES = listings.size();

        if (mPagerAdapter == null) {
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
            pager.setAdapter(mPagerAdapter);
        } else {
            currentPageIndex = -1;
            mPagerAdapter.clearContent();

        }
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFavoriteClicked(Listing listing) {
        int position = database.getListIndex(listing.getBooliId());
        position += LOOPS_COUNT == 1 ? 0 : (LOOPS_COUNT * NUM_PAGES) / 2;
        pager.setCurrentItem(position, false);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, FavoriteDetailsView> details = new HashMap<Integer, FavoriteDetailsView>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void clearContent() {
            details = new HashMap<Integer, FavoriteDetailsView>();
        }

        @Override
        public FavoriteDetailsView getItem(int position) {
            position = LOOPS_COUNT == 1 ? position : position % NUM_PAGES;
            if (!details.containsKey(position)) {
                details.put(position, FavoriteDetailsView.newInstance(position));
            }
            return details.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES * LOOPS_COUNT;
        }
    }
}