package com.jd.living.activity.history;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.model.ormlite.SearchHistory;
import com.jd.living.util.StringUtil;

@EViewGroup(R.layout.history_list_item)
public class HistoryListItem extends LinearLayout {

    @ViewById
    TextView address;

    @ViewById
    TextView rooms;

    @ViewById
    TextView time;

    @ViewById
    TextView type;

    @ViewById
    TextView price;

    @ViewById
    TextView production;


    public HistoryListItem(Context context) {
        super(context);
    }

    public void bind(SearchHistory history) {
        address.setText(history.getLocation());

        price.setText(getContext().getString(R.string.preferences_amount_min) + ": " +  history.getMinAmount() +
                ". "  + getContext().getString(R.string.preferences_amount_max) + ": "+ history.getMaxAmount());

        type.setText(StringUtil.startWithUpperCase(history.getTypes()) + ".");
        production.setText(StringUtil.getText(
                history.getProduction(),
                getResources().getStringArray(R.array.build_types_strings),
                getResources().getStringArray(R.array.build_types)) + ". "  +
                (history.isSold() ? getContext().getString(R.string.building_sold) : getContext().getString(R.string.building_on_sale)));

        time.setText(StringUtil.getTimeStampAsString(history.getTimestamp()));

        rooms.setText(getContext().getString(R.string.details_room_text, history.getMinRooms()) + " - "
                +  getContext().getString(R.string.details_room_text, history.getMaxRooms()));
    }
}