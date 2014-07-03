package com.jd.living.model;


import java.util.HashSet;
import java.util.Set;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.jd.living.activity.settings.SearchPreferenceKey;
import com.jd.living.util.StringUtil;


@EBean
public class Search {

    @RootContext
    Context context;


    protected SharedPreferences preferences;

    @AfterInject
    public void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTypes() {
        Set<String> buildTypes = preferences.getStringSet(SearchPreferenceKey.PREFERENCE_BUILDING_TYPE, new HashSet<String>());
        String types = "";

        for (String type : buildTypes.toArray(new String[]{})) {
            types = types + type + ", ";
        }

        if (!TextUtils.isEmpty(types)) {
            types = types.substring(0, types.length() - 2);
        }

        return types;
    }

    public String getMinRooms() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "1");
    }

    public String getMaxRooms(boolean modify) {
        String maxRooms = preferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "");
        if (modify) {
            if (maxRooms.equals("5")) {
                maxRooms = "";
            }
        }
        return maxRooms;
    }

    public String getMinAmount(boolean modify) {
        String minAmount = preferences.getString(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, "");
        if (modify) {
           minAmount = StringUtil.getStringAsNumber(minAmount);
        }
        return minAmount;
    }

    public String getMaxAmount(boolean modify) {
        String maxAmount = preferences.getString(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, "");
        if (modify) {
            maxAmount = StringUtil.getStringAsNumber(maxAmount);
        }
        return maxAmount;
    }

    public String getLocation() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_LOCATION, "Hörby");
    }

    public String getProduction() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_BUILD_TYPE, "null");
    }

    public boolean fetchSoldObjects() {
        return !preferences.getString(SearchPreferenceKey.PREFERENCE_OBJECT_TYPE, "0").equals("0");
    }
}
