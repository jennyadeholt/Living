package com.jd.living.drawer;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.activity.searchList.SearchListAction;

/**
 * Created by jennynilsson on 2014-06-17.
 */
@EViewGroup(R.layout.drawer_list_item)
public class DrawerListItem extends LinearLayout implements Checkable {

    @ViewById
    TextView text;

    @ViewById
    ImageView image;

    private boolean isChecked;

    public DrawerListItem(Context context) {
        super(context);
    }

    public void bind(SearchListAction action) {
        text.setText(action.getTextRes());
        if (action.getImageRes() != -1) {
            image.setImageResource(action.getImageRes());
        }
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }
}
