package com.jd.living.activity.history;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.model.ormlite.SearchHistory;
import com.jd.living.util.StringUtil;

@EViewGroup(R.layout.history_list_item)
public class HistoryListItem extends LinearLayout {

    @ViewById
    TextView address;

    @ViewById
    TextView time;

    @ViewById
    TableLayout tableLayout;


    public HistoryListItem(Context context) {
        super(context);
    }

    public void bind(SearchHistory history) {
        tableLayout.removeAllViews();

        address.setText(history.getLocation());

        String priceString = "";
        if (!TextUtils.isEmpty(history.getMinAmount())) {
            priceString = getContext().getString(R.string.preferences_amount_min) + ": " +  history.getMinAmount() + ". ";
        }
        if (!TextUtils.isEmpty(history.getMaxAmount())) {
            priceString += getContext().getString(R.string.preferences_amount_max) + ": " +  history.getMaxAmount() + ". ";
        }
        if (TextUtils.isEmpty(priceString)) {
            priceString = " - ";
        }

        addDetails(R.string.preferences_amount, priceString);

        addDetails(R.string.preferences_type_of_building, StringUtil.startWithUpperCase(history.getTypes()) + ".");

        addDetails(R.string.preferences_type_of_object, StringUtil.getText(
                history.getProduction(),
                getResources().getStringArray(R.array.build_types_strings),
                getResources().getStringArray(R.array.build_types)) + ". "  +
                (history.isSold() ? getContext().getString(R.string.building_sold) : getContext().getString(R.string.building_on_sale)) + ".");

        time.setText(StringUtil.getTimeStampAsString(history.getTimestamp()));

        addDetails(R.string.preferences_nbr_of_rooms, getContext().getString(R.string.details_room_text, history.getMinRooms()) + " - "
                +  getContext().getString(R.string.details_room_text, history.getMaxRooms()));
    }

    protected void addDetails(int nameId, String content) {

        View row = inflate(getContext(), R.layout.table_row, null);

        ((TextView) row.findViewById(R.id.extra_name)).setText(nameId);
        ((TextView) row.findViewById(R.id.content)).setText(content);
        tableLayout.addView(row);
    }
}