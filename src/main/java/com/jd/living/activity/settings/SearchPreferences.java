package com.jd.living.activity.settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;

@EFragment(R.layout.preferences)
public class SearchPreferences extends Fragment implements SearchPreferencesFragment.SharedPreferencesListener {

    @FragmentById
    SearchPreferencesFragment_ preferences;

    @ViewById
    Button search;

    @Bean
    DatabaseHelper databaseHelper;

    @AfterViews
    public void init() {

        preferences.setSharedPreferencesListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.launchSearch();
            }
        });
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
