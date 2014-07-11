package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.jd.living.R;
import com.jd.living.activity.settings.pickers.adapter.AmountAutoCompleteAdapter;


public class AmountPickerPreference extends PickerPreference {

    private static final String DEFAULT_VALUE = "4 000 000kr";

    public AmountPickerPreference(Context context, AttributeSet attrs) {
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
