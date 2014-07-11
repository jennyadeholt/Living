package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.jd.living.R;


public class NumberPickerPreference extends DialogPreference {

    private static final String DEFAULT_VALUE = "3000 kr";

    private NumberPicker numberPicker;
    private String value;

    String nums[]= {"0 kr", "1000 kr", "1500 kr", "2000 kr", "2500 kr", "3000 kr", "3500 kr", "4000 kr" , "4500 kr", "5000 kr", "5500 kr", "6000 kr", "6500 kr", "7000 kr", "7500 kr"};


    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.number_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(nums.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setValue(6);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        value = getPersistedString("3000kr");

        for (int i = 0 ; i < nums.length; i++) {
            if (nums[i].equals(value)) {
                numberPicker.setValue(i);
                break;
            }
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int position = numberPicker.getValue();
            value = nums[position];
            Log.d("Number", " " + value);
            persistString(value);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            value = this.getPersistedString(DEFAULT_VALUE);
        } else {
            value = (String) defaultValue;
            persistString(value);
        }
    }
}
