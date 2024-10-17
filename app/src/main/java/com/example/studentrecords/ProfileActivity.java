package com.example.studentrecords;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    // UI elements
    private TextView userTextView;
    private ListView historyListView;
    private Button deleteButton;
    private int profileId;

    private DatabaseHelper dbHelper; // Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the UI elements
        userTextView = findViewById(R.id.UsertextView);
        historyListView = findViewById(R.id.historyListView);
        deleteButton = findViewById(R.id.delete_button);

        dbHelper = new DatabaseHelper(this); // Create a database
        profileId = getIntent().getIntExtra("profileId", -1); // Get the profile ID from the intent

        loadProfile(profileId); // Load the profile data
        loadAccessHistory(profileId); // Load the access history data

        // Set the click listener for the delete button
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this); // Create a dialog to confirm reset
            builder.setTitle("Delete Profile"); // Set the title of the dialog
            builder.setMessage("Are you sure you want to delete "+ profileId +"'s profile?"); // Set the message of the dialog

            builder.setPositiveButton("Yes", (dialog, which) -> { // Set the positive button of the dialog
                dbHelper.addAccess(profileId, "Deleted"); // Add a delete access record to the database
                dbHelper.deleteProfile(profileId); // Delete the profile from the database
                Toast.makeText(this, "Profile Deleted!", Toast.LENGTH_SHORT).show(); // Show a toast message
                finish(); // Finish the activity and go back to the main activity
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()); // Set the negative button of the dialog
            builder.show(); // Show the dialog
        });

        // Set the title of the action bar
        if (getSupportActionBar() != null) { // Check if the action bar is not null to prevent crashing
            getSupportActionBar().setTitle(profileId + "'s Profile"); // Set the title of the action bar to the profile ID
        }
    }

    // Add an access record of type Opened to the database when the activity is started
    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.addAccess(profileId, "Opened");
    }

    // Add an access record of type Closed to the database when the activity is stopped
    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.addAccess(profileId, "Closed");
        loadProfile(profileId);
        loadAccessHistory(profileId);
    }

    // Method to load the profile data from the database
    private void loadProfile(int profileId) {
        Profile profile = dbHelper.getProfile(profileId); // Get the profile from the database

        // Set the text for the text view based on the profile data
        if (profile != null) { // Check if the profile is returned from the database to prevent crashing
            userTextView.setText("    Student Profile\n" +
                                    "Surname: " + profile.getSurname() +
                                    "\nName: " + profile.getName() +
                                    "\nID: " + profile.getProfileId() +
                                    "\nGPA: " + profile.getGpa() +
                                    "\nProfile Created: " + profile.getCreated());
        }
    }

    // Method to load the access history data from the database
    private void loadAccessHistory(int profileId) {
        List<Access> accessHistory = dbHelper.getAccessHistory(profileId); // Get the access history from the database and put it into a list of Access objects
        AccessAdapter adapter = new AccessAdapter(this, accessHistory); // Create an AccessAdapter object to adapt the access history to the list view
        historyListView.setAdapter(adapter); // Set the adapter for the list view
    }
}

