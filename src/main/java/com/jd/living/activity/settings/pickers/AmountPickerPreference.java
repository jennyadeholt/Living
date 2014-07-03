package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.jd.living.R;
import com.jd.living.activity.settings.pickers.adapter.AmountAutoCompleteAdapter;


public class AmountPickerPreference extends DialogPreference implements TextWatcher {

    private static final String DEFAULT_VALUE = "4 000 000kr";

    private AutoCompleteTextView textView;
    private String search = "";
    private AmountAutoCompleteAdapter adapter;

    public AmountPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.amount_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        adapter = new AmountAutoCompleteAdapter(getContext());

        textView = (AutoCompleteTextView) view.findViewById(R.id.amount);
        textView.addTextChangedListener(this);
        textView.setAdapter(adapter);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            if (TextUtils.isEmpty(textView.getText().toString())) {
                persistString(textView.getText().toString());
            } else {
                persistString(search);
            }
        }
    }


    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {  // Restore existing state
            search = this.getPersistedString(DEFAULT_VALUE);
        } else { // Set default state from the XML attribute
            search = (String) defaultValue;
            persistString(search);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        search = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
