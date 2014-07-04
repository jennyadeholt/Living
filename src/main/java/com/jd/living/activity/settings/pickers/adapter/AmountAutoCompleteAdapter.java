package com.jd.living.activity.settings.pickers.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.jd.living.util.StringUtil;


public class AmountAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> numbers = new ArrayList<String>();
    private int maxLenght = 9;
    private int minLenght = 4;

    public AmountAutoCompleteAdapter(Context context) {
        this(context, 4, 9);
    }

    public AmountAutoCompleteAdapter(Context context, int minLenght, int maxLenght) {
        super(context, android.R.layout.simple_spinner_dropdown_item);
        this.maxLenght = maxLenght;
        this.minLenght = minLenght;
    }

    @Override
    public int getCount() {
        return numbers.size();
    }

    @Override
    public String getItem(int index) {
        return numbers.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence search) {
                FilterResults filterResults = new FilterResults();
                numbers.clear();

                if (search != null) {
                    long value;
                    while (search.length() < maxLenght) {
                        if (search.length() > minLenght) {
                            value = Long.valueOf(search.toString());
                            numbers.add(StringUtil.getFormattedString(value));
                        }
                        search = search + "0";
                    }
                }

                filterResults.values = numbers;
                filterResults.count = numbers.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }
}
