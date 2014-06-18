package com.jd.living.activity.settings;

import java.util.HashSet;
import java.util.Set;

import org.androidannotations.annotations.EFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.jd.living.R;

/**
 * Created by jennynilsson on 2014-06-17.
 */

@EFragment
public class SearchPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface SharedPreferencesListener {
        void onSettingInvalid();
        void onSettingValid();
    }

    private SharedPreferences preferences;
    private SharedPreferencesListener sharedPreferencesListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.search_preferences);

        preferences = getPreferenceManager().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);

        setSummary(preferences, "preferences_area_location");
        checkMinMax(preferences, "");
        setSummaryForBuildingTypes(preferences, "preference_building_type");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Living", "key " + key);
        if (key.equals("preferences_area_location")) {
            setSummary(sharedPreferences, key);
        } else if (key.equals("preference_min_numbers") || key.equals("preference_max_numbers")) {
           checkMinMax(sharedPreferences, key);
        } else if (key.equals("preference_building_type")) {
            setSummaryForBuildingTypes(sharedPreferences, key);
        }
    }

    private void checkMinMax(SharedPreferences sharedPreferences, String key) {

        int max = Integer.valueOf(sharedPreferences.getString("preference_max_numbers", ""));
        int min = Integer.valueOf(sharedPreferences.getString("preference_min_numbers", ""));

        if (min <= max) {
            setSummary(sharedPreferences, "preference_min_numbers", String.valueOf(min));
            setSummary(sharedPreferences, "preference_max_numbers", String.valueOf(max));
        } else if (key.equals("preference_max_numbers")) {
            setSummary(sharedPreferences, "preference_max_numbers", "Max value needs to be bigger then min");
        } else if (key.equals("preference_min_numbers")) {
            setSummary(sharedPreferences, "preference_min_numbers", "Min value needs to be smaller then max");
        } else {
            setSummary(sharedPreferences, "preference_max_numbers", "Max value needs to be bigger then min");
            setSummary(sharedPreferences, "preference_min_numbers", "Min value needs to be smaller then max");
        }
        notifyListener(min <= max);
    }

    private void notifyListener(boolean valid) {
        if (sharedPreferencesListener != null) {
            if (valid){
                sharedPreferencesListener.onSettingValid();
            } else {
                sharedPreferencesListener.onSettingInvalid();
            }
        }
    }

    public void setSharedPreferencesListener(SharedPreferencesListener listener) {
        sharedPreferencesListener = listener;
    }

    private void setSummary(SharedPreferences sharedPreferences, String key) {
        setSummary(sharedPreferences, key, sharedPreferences.getString(key, ""));
    }

    private void setSummary(SharedPreferences sharedPreferences, String key, String summary) {
        findPreference(key).setSummary(summary);
    }

    private void setSummaryForBuildingTypes(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        Set<String> set = sharedPreferences.getStringSet(key, new HashSet<String>());
        String text = "None";

        if (!set.isEmpty()) {
            text = "";
            String[] names = getResources().getStringArray(R.array.building);
            String[] types = getResources().getStringArray(R.array.building_types);
            String[] selected = set.toArray(new String[] {});
            for (String s : selected) {
                for (int i = 0 ; i < types.length ; i ++) {
                    if (types[i].equals(s)) {
                        text = text + names[i] + ", ";
                        break;
                    }
                }
            }
            text = text.substring(0, text.length() - 2);
        }

        pref.setSummary(text);
    }
}