package com.jd.living.activity.settings;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jd.living.LivingApplication;
import com.jd.living.R;
import com.jd.living.activity.settings.search.PlacesAutoCompleteAdapter;
import com.jd.living.model.Area;
import com.jd.living.model.AreaResult;
import com.jd.living.server.ListingsDatabase;


public class AreaPickerPreference extends DialogPreference  {

    private static final String DEFAULT_VALUE = "Kiruna";

    private ListingsDatabase database;

    private AutoCompleteTextView textView;
    private String search = "";
    private PlacesAutoCompleteAdapter adapter;

    public AreaPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);

        setDialogLayoutResource(R.layout.area_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        adapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);

        textView = (AutoCompleteTextView) view.findViewById(R.id.textView);
        textView.setAdapter(adapter);

    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistString(search);
        }
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
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {

            return superState;
        }
        final SavedState myState = new SavedState(superState);

        myState.value = search;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        textView.setText(myState.value);
    }

    private static class SavedState extends BaseSavedState {
        String value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}