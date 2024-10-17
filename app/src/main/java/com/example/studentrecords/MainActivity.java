package com.example.studentrecords;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private FloatingActionButton fab;
    private DatabaseHelper dbHelper;
    private TextView infoTextView;
    private ProfileAdapter adapter;

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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Profile profile = (Profile) adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("profileId", profile.getProfileId());
            startActivity(intent);
           // addAccessTimestamp(profile.getProfileId(), "Opened");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }

    public void loadProfiles() {
        List<Profile> profiles = dbHelper.getAllProfiles();
        adapter = new ProfileAdapter(this, profiles);
        listView.setAdapter(adapter);
        infoTextView.setText("Total profiles: " + profiles.size());
    }

    public void addAccessTimestamp(int profileId, String accessType) {
        dbHelper.addAccess(profileId, accessType);
    }
}
