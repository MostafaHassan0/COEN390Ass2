package com.example.studentrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccessAdapter extends ArrayAdapter<Access> {
    private Context context;
    private List<Access> accessList;

    public AccessAdapter(Context context, List<Access> accessList) {
        super(context, 0, accessList);
        this.context = context;
        this.accessList = accessList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Access access = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView accessTypeView = convertView.findViewById(android.R.id.text1);
        TextView timestampView = convertView.findViewById(android.R.id.text2);

        accessTypeView.setText(access.getAccessType());
        timestampView.setText(access.getTimestamp());

        return convertView;
    }
}
