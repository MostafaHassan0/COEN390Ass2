package com.example.studentrecords;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private FloatingActionButton fab;
    private DatabaseHelper dbHelper;
    private TextView infoTextView;
    private ProfileAdapter adapter;
    private static boolean Nametoggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.Profiles_listView);
        fab = findViewById(R.id.add_button);
        infoTextView = findViewById(R.id.infotextView);

        dbHelper = new DatabaseHelper(this);

        loadProfiles();  // Load profiles from the database

        fab.setOnClickListener(v -> {
            new InsertProfileDialogFragment().show(getSupportFragmentManager(), "InsertProfile");
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Records");
        }




        listView.setOnItemClickListener((parent, view, position, id) -> {
            Profile profile = adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("profileId", profile.getProfileId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }

    public void loadProfiles() {
        List<Profile> profiles = dbHelper.getAllProfiles();
        adapter = new ProfileAdapter(this, profiles,Nametoggle);
        listView.setAdapter(adapter);
        if (Nametoggle) {
            if (profiles.size() == 1) { infoTextView.setText(profiles.size() + " Profile, by Surname");}
            else {infoTextView.setText(profiles.size() + " Profiles, by Surname");}
        }
        else {
            if (profiles.size() == 1) { infoTextView.setText(profiles.size() + " Profile, by ID");}
            else {infoTextView.setText(profiles.size() + " Profiles, by ID");}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Inflate the menu to appear in the action bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Toggle_Listing) {
            Nametoggle = !Nametoggle;
            loadProfiles();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
