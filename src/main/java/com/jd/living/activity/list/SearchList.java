package com.jd.living.activity.list;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.content.Intent;
import android.widget.ListView;

import com.jd.living.R;
import com.jd.living.activity.detail.DetailsView_;
import com.jd.living.model.Listing;


@EFragment(R.layout.search_list)
public class SearchList extends ListFragment {

    @ViewById
    ListView list;

    @Bean
    Adapter adapter;

    @AfterViews
    public void init() {
        setListAdapter(adapter);
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        Intent intent = new Intent(getActivity(), DetailsView_.class);
        intent.putExtra("id", listing.getBooliId());
        startActivity(intent);
    }
}

