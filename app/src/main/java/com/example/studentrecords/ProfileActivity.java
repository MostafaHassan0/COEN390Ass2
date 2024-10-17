package com.example.studentrecords;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView userTextView, accessTextView;
    private ListView historyListView;
    private Button deleteButton;
    private DatabaseHelper dbHelper;
    private int profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userTextView = findViewById(R.id.UsertextView);
        accessTextView = findViewById(R.id.AccesstextView);
        historyListView = findViewById(R.id.historyListView);
        deleteButton = findViewById(R.id.delete_button);

        dbHelper = new DatabaseHelper(this);
        profileId = getIntent().getIntExtra("profileId", -1);

        loadProfile(profileId);
        loadAccessHistory(profileId);

        deleteButton.setOnClickListener(v -> {
            dbHelper.addAccess(profileId, "Deleted");
            dbHelper.deleteProfile(profileId);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.addAccess(profileId, "Opened");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.addAccess(profileId, "Closed");
        loadProfile(profileId);
        loadAccessHistory(profileId);
    }


    private void loadProfile(int profileId) {
        Profile profile = dbHelper.getProfile(profileId);
        if (profile != null) {
            userTextView.setText("   User Profile\nID: " + profile.getProfileId() + "\nName: " + profile.getName() + "\nSurname: " + profile.getSurname() + "\nGPA: " + profile.getGpa() + "\nCreated: " + dbHelper.getCreated(profileId));
        }
    }

    private void loadAccessHistory(int profileId) {
        List<Access> accessHistory = dbHelper.getAccessHistory(profileId);
        AccessAdapter adapter = new AccessAdapter(this, accessHistory);
        historyListView.setAdapter(adapter);
    }
}

