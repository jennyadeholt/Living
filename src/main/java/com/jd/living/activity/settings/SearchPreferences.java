package com.jd.living.activity.settings;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jd.living.R;

/**
 * Created by jennynilsson on 2014-06-17.
 */

@EFragment
public class SearchPreferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.search_preferences);
    }
}
