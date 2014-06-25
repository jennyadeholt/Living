package com.jd.living.activity.favorites;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jd.living.R;
import com.jd.living.activity.searchList.ListItem;
import com.jd.living.activity.searchList.ListItem_;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.server.ListingsDatabase;

@EBean
public class FavoriteListAdapter extends ArrayAdapter<Listing> implements FavoriteDatabase.FavoriteListener {

    @Bean
    FavoriteDatabase database;

    private List<Listing> listings = new ArrayList<Listing>();

    public FavoriteListAdapter(Context context) {
        super(context, R.layout.list_item);
    }

    @AfterInject
    public void init(){
        database.addFavoriteListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem listItem;
        if (convertView == null) {
            listItem = ListItem_.build(getContext());
        } else {
            listItem = (ListItem) convertView;
        }

        listItem.bind(getItem(position));

        return listItem;
    }

    @UiThread
    public void update() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listings.size();
    }

    @Override
    public Listing getItem(int position) {
        return listings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void addedFavorite(Listing listing) {
        Log.d("Living", "FavoriteListAdapter.addedFavorite");
        listings.add(listing);
        update();
    }

    @Override
    public void removedFavorite(Listing listing) {
        Log.d("Living", "FavoriteListAdapter.removedFavorite");
        listings.remove(listing);
        update();
    }
}
