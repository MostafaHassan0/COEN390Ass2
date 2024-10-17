package com.example.studentrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Profile> {
    private Context context;
    private List<Profile> profiles;
    private boolean Nametoggle;

    public ProfileAdapter(Context context, List<Profile> profiles, boolean Nametoggle) {
        super(context, 0, profiles);
        this.context = context;
        this.profiles = profiles;
        this.Nametoggle = Nametoggle;

        // Sort the profiles list based on the toggle (Name or ID)
        if (Nametoggle) {
            // Sort by surname first, and by name second (if surnames are the same)
            Collections.sort(profiles, new Comparator<Profile>() {
                @Override
                public int compare(Profile p1, Profile p2) {
                    // Compare surnames first
                    int surnameComparison = p1.getSurname().compareToIgnoreCase(p2.getSurname());

                    // If surnames are the same, compare names
                    if (surnameComparison == 0) {
                        return p1.getName().compareToIgnoreCase(p2.getName());
                    }
                    // Otherwise, return the surname comparison result
                    return surnameComparison;
                }
            });
        } else {
            // Sort by profile ID
            Collections.sort(profiles, new Comparator<Profile>() {
                @Override
                public int compare(Profile p1, Profile p2) {
                    return Integer.compare(p1.getProfileId(), p2.getProfileId());
                }
            });
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Profile profile = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }


        if (Nametoggle){
            TextView text1 = convertView.findViewById(android.R.id.text1);
            text1.setText((position+1) + ". " + profile.getSurname() + ", " + profile.getName());
        }
        else {
            TextView text1 = convertView.findViewById(android.R.id.text1);
            text1.setText(String.valueOf((position+1) + ". " +profile.getProfileId()));
        }

        return convertView;
    }

}
