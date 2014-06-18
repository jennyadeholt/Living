package com.jd.living.activity.searchList;

import com.jd.living.R;

/**
 * Created by jennynilsson on 2014-06-17.
 */
public enum SearchListAction {

    NEW_SEARCH(R.string.new_search, R.drawable.ic_menu_add, false),
    RESULT_HEADER(R.string.search_result_header,true),
    SEARCH_RESULT(R.string.search_result, R.drawable.action_search, false),
    MAP(R.string.map_result, R.drawable.ic_menu_mapmode, false),
    SETTINGS_HEADER(R.string.search_result_header_2, true),
    SEARCHES(R.string.searches, R.drawable.ic_menu_archive, false),
    FAVORITES(R.string.favorites, R.drawable.ic_menu_star, false),
    DETAILS(R.string.detail_view, R.drawable.action_search, false),
    IMAGES(R.string.images, R.drawable.ic_menu_camera, false),
    SETTINGS(R.string.settings, R.drawable.ic_menu_preferences, false),
    HELP(R.string.help, R.drawable.ic_menu_help, false),
    ABOUT(R.string.about, R.drawable.ic_menu_attachment, false);

    private int textRes;
    private int imageRes;
    private boolean isHeader;

    SearchListAction(int textRes, boolean isHeader) {
       this(textRes, -1, isHeader);
    }

    SearchListAction(int textRes, int imageRes, boolean isHeader) {
        this.textRes = textRes;
        this.imageRes = imageRes;
        this.isHeader = isHeader;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getTextRes() {
        return textRes;
    }

    public boolean isHeader(){
        return isHeader;
    }
}
