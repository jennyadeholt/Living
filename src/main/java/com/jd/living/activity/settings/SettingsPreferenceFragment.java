package com.jd.living.activity.settings;

import org.androidannotations.annotations.EFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jd.living.R;

@EFragment
public class SettingsPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preferences);

        preferences = getPreferenceManager().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);

        setSummaryForSortList(preferences);
        setSummaryForSortOrderList(preferences);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SearchPreferenceKey.PREFERENCE_SORT_LIST_ON) ) {
            setSummaryForSortList(sharedPreferences);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_SORT_ORDER)) {
            setSummaryForSortOrderList(sharedPreferences);
        }
    }

    private void setSummaryForSortList(SharedPreferences sharedPreferences) {
        String[] names = getResources().getStringArray(R.array.sort_strings);
        String[] types = getResources().getStringArray(R.array.sort_types);
        setSummaryForList(sharedPreferences, SearchPreferenceKey.PREFERENCE_SORT_LIST_ON, names, types);
    }

    private void setSummaryForSortOrderList(SharedPreferences sharedPreferences) {
        String[] names = getResources().getStringArray(R.array.sort_order_strings);
        String[] types = getResources().getStringArray(R.array.sort_order_types);
        setSummaryForList(sharedPreferences, SearchPreferenceKey.PREFERENCE_SORT_ORDER, names, types);
    }

    private void setSummaryForList(SharedPreferences sharedPreferences, String key, String[] names, String[] types) {
        String text = names[0];
        String value = sharedPreferences.getString(key, "1");

        for (int i = 0; i < types.length ; i++) {
            if (value.equals(types[i])){
                text = names[i];
                break;
            }

        }
        setSummary(key, text);
    }

    private void setSummary(String key, String summary) {
        findPreference(key).setSummary(summary);
    }
}

