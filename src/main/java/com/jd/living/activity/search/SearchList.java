package com.jd.living.activity.search;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;


@EFragment
public class SearchList extends ListFragment implements DatabaseHelper.DatabaseListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    DatabaseHelper database;
    @Bean
    SearchListAdapter searchListAdapter;
    @Bean
    FavoriteDatabase favoriteDatabase;

    private ProgressDialog spinner;
    private List<Listing> result;

    @AfterViews
    public void init() {
        setListAdapter(searchListAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //spinner = ProgressDialog.show(getActivity(), "", "Loading..", true);
        database.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (spinner != null) {
            spinner.dismiss();
        }
        database.removeDatabaseListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == android.R.id.list) {
            Listing listing = database.getListingBasedOnLocation((
                    (AdapterView.AdapterContextMenuInfo) menuInfo).position);
            getActivity().getMenuInflater().inflate(R.menu.search_list_menu, menu);
            menu.setHeaderTitle(listing.getAddress());

            if (favoriteDatabase.isFavorite(listing)) {
                menu.removeItem(R.id.action_add);
            } else {
                menu.removeItem(R.id.action_remove);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Listing listing = database.getListingBasedOnLocation(position);

        switch(item.getItemId()) {
            case R.id.action_add:
            case R.id.action_remove:
                favoriteDatabase.updateFavorite(listing);
                return true;
            case R.id.action_view:
                database.setCurrentId(listing.getBooliId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        database.setCurrentId(listing.getBooliId());
    }

    @Override
    public void onUpdate(List<Listing> result) {
        update(result);
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onFavoriteUpdated() {

    }

    @Override
    public void onDetailsRequested(int booliId) {

    }

    @UiThread
    public void update(List<Listing> result) {
        if (spinner != null) {
            spinner.dismiss();
        }

        info.setText(getString(R.string.number_of_objects, result.size(), result.size()));
    }
}

