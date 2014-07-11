package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.util.AttributeSet;

import com.jd.living.R;
import com.jd.living.activity.settings.pickers.adapter.AmountAutoCompleteAdapter;


public class AmountAutoCompletePickerPreference extends AutoCompletePickerPreference {

    private static final String DEFAULT_VALUE = "4 000 000kr";

    public AmountAutoCompletePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getDialogResource() {
        return R.layout.amount_picker_dialog;
    }

    @Override
    public AmountAutoCompleteAdapter getAdapter() {
        return new AmountAutoCompleteAdapter(getContext());
    }

    @Override
    public String getDefaultValue() {
        return DEFAULT_VALUE;
    }
}
