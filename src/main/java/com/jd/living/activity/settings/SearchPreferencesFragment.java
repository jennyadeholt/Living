package com.jd.living.activity.settings;

import java.util.HashSet;
import java.util.Set;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jd.living.R;
import com.jd.living.Search;
import com.jd.living.database.DatabaseHelper;

@EFragment
public class SearchPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Bean
    DatabaseHelper databaseHelper;

    @Bean
    Search search;

    private SharedPreferences preferences;
    private boolean isValid = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        addPreferencesFromResource(R.xml.search_preferences);

        preferences = getPreferenceManager().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);

        setSummaryForTypeList(preferences);
        setSummaryForObjectList(preferences);
        setSummary(preferences, SearchPreferenceKey.PREFERENCE_LOCATION);
        checkMinMaxAmount("");
        checkMinMax("");
        setSummaryForBuildingTypes(preferences, SearchPreferenceKey.PREFERENCE_BUILDING_TYPE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_preferences, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actions_start_search:
                if (isValid) {
                    databaseHelper.launchSearch();
                }
                return true;
            case R.id.action_clear_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SearchPreferenceKey.PREFERENCE_LOCATION) ) {
            setSummary(sharedPreferences, key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS)
                || key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS)) {
            checkMinMax(key);
        }  else if ( key.equals(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX)
                || key.equals(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN)) {
            checkMinMaxAmount(key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_BUILDING_TYPE)) {
            setSummaryForBuildingTypes(sharedPreferences, key);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_BUILD_TYPE)) {
            setSummaryForTypeList(sharedPreferences);
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_OBJECT_TYPE)) {
            setSummaryForObjectList(sharedPreferences);
        }
    }

    private void checkMinMaxAmount(String key) {
        boolean result = false;
        int max = Integer.valueOf(search.getMaxAmount(true));
        int min = Integer.valueOf(search.getMinAmount(true));

        if (min < max || min == 0 || max == 0) {
            result = true;
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, search.getMinAmount(false));
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, search.getMaxAmount(false) );
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX)) {
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, "Max value needs to be bigger then min");
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN)) {
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, "Min value needs to be smaller then max");
        } else {
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, "Max value needs to be bigger then min");
            setSummary(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, "Min value needs to be smaller then max");
        }

        isValid = result;
    }

    private void checkMinMax(String key) {

        int max = Integer.valueOf(search.getMaxRooms(false));
        int min = Integer.valueOf(search.getMinRooms());

        if (min <= max) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, String.valueOf(min));
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, max == 5 ? "5+" : String.valueOf(max));

        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS)) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "Max value needs to be bigger then min");
        } else if (key.equals(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS)) {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "Min value needs to be smaller then max");
        } else {
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "Max value needs to be bigger then min");
            setSummary(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "Min value needs to be smaller then max");
        }
        isValid = (min <= max);
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
