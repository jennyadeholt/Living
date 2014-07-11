package com.jd.living.activity.settings.pickers;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jd.living.R;


public abstract class AutoCompletePickerPreference extends DialogPreference implements TextWatcher {

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView textView;
    private String search = "";
    private boolean textChanged = false;

    public AutoCompletePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(getDialogResource());
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    protected abstract int getDialogResource();
    protected abstract ArrayAdapter<String> getAdapter();
    protected abstract String getDefaultValue();


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        adapter = getAdapter();

        textView = (AutoCompleteTextView) view.findViewById(R.id.textView);
        textView.addTextChangedListener(this);
        textView.setAdapter(adapter);

        textView.setHint(search);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            if (textChanged && TextUtils.isEmpty(textView.getText().toString())) {
                persistString("");
            } else {
                persistString(search);
            }
        }
        textChanged = false;
        textView.setHint(search);
    }

    protected boolean needInputMethod() {
        return true;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {  // Restore existing state
            search = this.getPersistedString(getDefaultValue());
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
        textChanged = true;
        search = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
