package com.jd.living.tabhost;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;


public class DetailsTabContent implements TabHost.TabContentFactory {
    private Context mContext;

    public DetailsTabContent(Context context){
        mContext = context;
    }

    @Override
    public View createTabContent(String tag) {
        return new View(mContext);
    }
}
