package com.jd.living.drawer;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.activity.searchList.SearchListAction;

@EViewGroup(R.layout.drawer_list_item_header)
public class DrawerHeader extends LinearLayout {

    @ViewById
    TextView text;

    public DrawerHeader(Context context) {
        super(context);
    }

    public void bind(SearchListAction action) {
        text.setText(action.getTextRes());
    }

}
