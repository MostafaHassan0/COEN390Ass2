package com.example.studentrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// This Class is to adapt the access history to display on the list view
public class AccessAdapter extends ArrayAdapter<Access> {
    private final Context context;
    private final List<Access> accessList;

    public AccessAdapter(Context context, List<Access> accessList) {
        super(context, 0, accessList);
        this.context = context;
        this.accessList = accessList;
    }

    // Get the view for the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Access access = getItem(position); // Get the access at the current position

        if (convertView == null) { // Inflate the view if it is null
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false); // Set the layout for the list item
        }

        TextView TextView = convertView.findViewById(android.R.id.text1);
        TextView.setText(access.getTimestamp() + " " + access.getAccessType()); // Set the text for the list item

        return convertView;
    }
}
