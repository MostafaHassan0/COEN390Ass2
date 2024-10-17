package com.example.studentrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccessAdapter extends ArrayAdapter<Access> {
    private final Context context;
    private final List<Access> accessList;

    public AccessAdapter(Context context, List<Access> accessList) {
        super(context, 0, accessList);
        this.context = context;
        this.accessList = accessList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Access access = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView TextView = convertView.findViewById(android.R.id.text1);

        TextView.setText(access.getTimestamp() + " " + access.getAccessType());

        return convertView;
    }
}
