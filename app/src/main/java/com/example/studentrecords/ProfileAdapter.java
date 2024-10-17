package com.example.studentrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Profile> {
    private Context context;
    private List<Profile> profiles;

    public ProfileAdapter(Context context, List<Profile> profiles) {
        super(context, 0, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Profile profile = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText(profile.getSurname() + ", " + profile.getName());
        text2.setText("ID: " + profile.getProfileId() + " | GPA: " + profile.getGpa());

        return convertView;
    }
}
