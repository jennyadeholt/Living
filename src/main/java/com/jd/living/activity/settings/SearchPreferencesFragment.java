package com.jd.living.activity.settings;

import java.util.HashSet;
import java.util.Set;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Button;

import com.jd.living.R;

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

        setSummaryForTypeList(preferences);
        setSummaryForObjectList(preferences);
        setSummary(preferences, SearchPreferenceKey.PREFERENCE_LOCATION);
        checkMinMax(preferences, "");
        setSummaryForBuildingTypes(preferences, SearchPreferenceKey.PREFERENCE_BUILDING_TYPE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SearchPreferenceKey.PREFERENCE_LOCATION)) {
            setSummary(sharedPreferences, key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS)
                || key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS)) {
           checkMinMax(sharedPreferences, key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_BUILDING_TYPE)) {
            setSummaryForBuildingTypes(sharedPreferences, key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_BUILD_TYPE)) {
            setSummaryForTypeList(sharedPreferences);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_OBJECT_TYPE)) {
            setSummaryForObjectList(sharedPreferences);
        }
    }

    private void checkMinMax(SharedPreferences sharedPreferences, String key) {

        sharedPreferences.edit().putString(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "5").commit();

        int max = Integer.valueOf(sharedPreferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, ""));
        int min = Integer.valueOf(sharedPreferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, ""));

        if (min <= max) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, String.valueOf(min));
            if (max == 5) {
                setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "5+");
            } else {
                setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, String.valueOf(max));
            }
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS)) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "Max value needs to be bigger then min");
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS)) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "Min value needs to be smaller then max");
        } else {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "Max value needs to be bigger then min");
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "Min value needs to be smaller then max");
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
        setSummary(key, sharedPreferences.getString(key, ""));
    }

    private void setSummaryForObjectList(SharedPreferences sharedPreferences) {
        String[] names = getResources().getStringArray(R.array.build_object_strings);
        String[] types = getResources().getStringArray(R.array.build_object);
        setSummaryForList(sharedPreferences, SearchPreferenceKey.PREFERENCE_OBJECT_TYPE, names, types);
    }

    private void setSummaryForTypeList(SharedPreferences sharedPreferences) {
        String[] names = getResources().getStringArray(R.array.build_types_strings);
        String[] types = getResources().getStringArray(R.array.build_types);
        setSummaryForList(sharedPreferences, SearchPreferenceKey.PREFERENCE_BUILD_TYPE, names, types);
    }

    private void setSummaryForList(SharedPreferences sharedPreferences, String key, String[] names, String[] types) {
        String text = names[0];
        String value = sharedPreferences.getString(key, "");

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

    private void setSummaryForBuildingTypes(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        Set<String> set = sharedPreferences.getStringSet(key, new HashSet<String>());
        String text = getString(R.string.building_type_none);

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
