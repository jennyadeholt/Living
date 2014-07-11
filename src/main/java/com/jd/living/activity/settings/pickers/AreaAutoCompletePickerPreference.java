package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.util.AttributeSet;

import com.jd.living.R;
import com.jd.living.activity.settings.pickers.adapter.PlacesAutoCompleteAdapter;


public class AreaAutoCompletePickerPreference extends AutoCompletePickerPreference {

    private static final String DEFAULT_VALUE = "Kiruna";

    public AreaAutoCompletePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getDialogResource() {
        return R.layout.area_picker_dialog;
    }

    @Override
    protected PlacesAutoCompleteAdapter getAdapter() {
        return new PlacesAutoCompleteAdapter(getContext());
    }

    @Override
    public String getDefaultValue() {
        return DEFAULT_VALUE;
    }
}