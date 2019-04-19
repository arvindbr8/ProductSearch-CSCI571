package com.bright.productsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TwoColumnListViewAdapter extends ArrayAdapter<DataModel> {

    public TwoColumnListViewAdapter(Context context, ArrayList<DataModel> rows) {
        super(context, 0, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataModel row = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.twocolumn_custom_listitem, parent, false);
        }

        TextView head = convertView.findViewById(R.id.heading);
        TextView desc = convertView.findViewById(R.id.desc);

        head.setText(row.heading);
        desc.setText(row.desc);

        return convertView;
    }
}
