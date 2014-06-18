package com.jd.living.drawer;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jd.living.R;
import com.jd.living.activity.searchList.SearchListAction;


@EBean
public class DrawerListAdapter extends ArrayAdapter<SearchListAction> {

    private List<SearchListAction> content = new ArrayList<SearchListAction>();
    private int selected;

    public DrawerListAdapter(Context context) {
        super(context, R.layout.drawer_list_item);
    }

    public void setContent(List<SearchListAction> content) {
        this.content = content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        SearchListAction action = content.get(position);

        DrawerHeader header = null;
        DrawerListItem listItem = null;

        if (convertView != null) {
            if (convertView instanceof DrawerHeader) {
                header = (DrawerHeader) convertView;
            } else {
                listItem = (DrawerListItem) convertView;
            }
        } else {
            if (action.isHeader()) {
                header = DrawerHeader_.build(getContext());
            } else {
                listItem = DrawerListItem_.build(getContext());
            }
        }

        if (header != null) {
            header.bind(action);
            view = header;
        } else if (listItem != null) {
            if (selected == position) {
                listItem.setBackgroundResource(R.drawable.list_item_selected);
            } else {
                listItem.setBackgroundResource(R.drawable.drawer_list_item);
            }
            listItem.bind(action);
            view = listItem;
        }

        return view;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public SearchListAction getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }
}
