package com.jd.living.activity.settings.pickers.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


public class AmountAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private static final String LOG_TAG = "Living";

    private ArrayList<String> resultList;

    private Map<String, String> numbers = new HashMap<String, String>();

    public AmountAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

        resultList = new ArrayList<String>();

        numbers.put("10000","10 000kr");
        numbers.put("100000", "100 000kr");
        numbers.put("1000000", "1 000 000kr");
        numbers.put("10000000", "10 000 000kr");
        numbers.put("20000", "20 000kr");
        numbers.put("200000", "200 000kr");
        numbers.put("2000000", "2 000 000kr");
        numbers.put("20000000", "20 000 000kr");
        numbers.put("30000", "30 000kr");
        numbers.put("300000", "300 000kr");
        numbers.put("3000000", "3 000 000kr");
        numbers.put("40000", "40 000kr");
        numbers.put("400000", "400 000kr");
        numbers.put("4000000", "4 000 000kr");
        numbers.put("50000", "50 000kr");
        numbers.put("500000", "500 000kr");
        numbers.put("5000000", "5 000 000kr");
        numbers.put("60000", "60 000kr");
        numbers.put("600000", "600 000kr");
        numbers.put("6000000", "6 000 000kr");
        numbers.put("70000", "70 000kr");
        numbers.put("700000", "700 000kr");
        numbers.put("7000000", "7 000 000kr");
        numbers.put("80000", "80 000kr");
        numbers.put("800000", "800 000kr");
        numbers.put("8000000", "8 000 000kr");
        numbers.put("90000", "90 000kr");
        numbers.put("900000", "900 000kr");
        numbers.put("9000000", "9 000 000kr");
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence search) {
                FilterResults filterResults = new FilterResults();
                resultList.clear();
                if (search != null) {
                    for (String number : numbers.keySet()) {
                        if (number.contains(search)) {
                            resultList.add(numbers.get(number));
                        }
                    }
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
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
