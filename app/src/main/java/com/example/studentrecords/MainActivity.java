package com.example.studentrecords;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI elements
    private ListView listView;
    private FloatingActionButton fab;
    private TextView infoTextView;
    private ProfileAdapter adapter;

    private DatabaseHelper dbHelper; // Database
    private static boolean Nametoggle = true; // Flag to toggle the Naming system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the UI elements
        listView = findViewById(R.id.Profiles_listView);
        fab = findViewById(R.id.add_button);
        infoTextView = findViewById(R.id.infotextView);

        dbHelper = new DatabaseHelper(this); // Create a database

        loadProfiles();  // Load profiles from the database

        // Set the click listener for the floating action button to open the insert profile dialog
        fab.setOnClickListener(v -> {
            new InsertProfileDialogFragment().show(getSupportFragmentManager(), "InsertProfile");
        });

        // Set the title of the action bar
        if (getSupportActionBar() != null) { // Check if the action bar is not null to prevent crashing
            getSupportActionBar().setTitle("Student Records");
        }

        // Set the click listener for the list view to open the profile activity when a profile is clicked
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Profile profile = adapter.getItem(position); // Get the profile at the clicked position
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class); // Create an intent to open the profile activity
            intent.putExtra("profileId", profile.getProfileId()); // Put the profile ID in the intent
            startActivity(intent); // Start the profile activity
        });
    }

    // load profiles from the database when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }

    // Method to load profiles from the database
    public void loadProfiles() {
        List<Profile> profiles = dbHelper.getAllProfiles(); // Get all profiles from the database and put them into a list of Profile objects
        adapter = new ProfileAdapter(this, profiles,Nametoggle); // Create a ProfileAdapter object to adapt the profiles to the list view
        listView.setAdapter(adapter); // Set the adapter for the list view

        // Set the text for the info text view based on the number of profiles and the Naming system
        if (Nametoggle) {
            if (profiles.size() == 1) { infoTextView.setText(profiles.size() + " Profile, by Surname");} // If there is only one profile, set the text accordingly
            else {infoTextView.setText(profiles.size() + " Profiles, by Surname");}// If there are more than one profile, set the text accordingly

        } else {
            if (profiles.size() == 1) { infoTextView.setText(profiles.size() + " Profile, by ID");} // If there is only one profile, set the text accordingly
            else {infoTextView.setText(profiles.size() + " Profiles, by ID");} // If there are more than one profile, set the text accordingly
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Inflate the menu to appear in the action bar
        return true;
    }

    // Handle the menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Toggle_Listing) { // If the toggle listing menu item is clicked
            Nametoggle = !Nametoggle; // Toggle the Nametoggle flag
            loadProfiles(); // Load profiles again
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
