package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jd.living.R;
import com.jd.living.activity.settings.pickers.adapter.PlacesAutoCompleteAdapter;


public class AreaPickerPreference extends PickerPreference {

    private static final String DEFAULT_VALUE = "Kiruna";

    public AreaPickerPreference(Context context, AttributeSet attrs) {
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