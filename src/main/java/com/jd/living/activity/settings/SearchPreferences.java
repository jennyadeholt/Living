package com.jd.living.activity.settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jd.living.R;

@EFragment(R.layout.preferences)
public class SearchPreferences extends Fragment implements SearchPreferencesFragment.SharedPreferencesListener {

    @FragmentById
    SearchPreferencesFragment_ preferences;

    @ViewById
    Button search;

    @AfterViews
    public void init() {
        preferences.setSharedPreferencesListener(this);
    }

    @Override
    public void onSettingInvalid() {
        search.setEnabled(false);
    }

    @Override
    public void onSettingValid() {
        search.setEnabled(true);
    }
}
