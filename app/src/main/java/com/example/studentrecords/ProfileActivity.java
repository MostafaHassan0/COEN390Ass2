package com.example.studentrecords;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView userTextView;
    private ListView historyListView;
    private Button deleteButton;
    private DatabaseHelper dbHelper;
    private int profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userTextView = findViewById(R.id.UsertextView);
        historyListView = findViewById(R.id.historyListView);
        deleteButton = findViewById(R.id.delete_button);

        dbHelper = new DatabaseHelper(this);
        profileId = getIntent().getIntExtra("profileId", -1);

        loadProfile(profileId);
        loadAccessHistory(profileId);

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this); // Create a dialog to confirm reset
            builder.setTitle("Delete Profile"); // Set the title of the dialog
            builder.setMessage("Are you sure you want to delete "+ profileId +"'s profile?"); // Set the message of the dialog
            builder.setPositiveButton("Yes", (dialog, which) -> { // Set the positive button of the dialog
                dbHelper.addAccess(profileId, "Deleted");
                dbHelper.deleteProfile(profileId);
                Toast.makeText(this, "Profile Deleted!", Toast.LENGTH_SHORT).show();
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()); // Set the negative button of the dialog
            builder.show();

        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(profileId + " Profile");
        }
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
            userTextView.setText("    User Profile\nSurname: " + profile.getSurname() + "\nName: " + profile.getName() + "\nID: " + profile.getProfileId()+ "\nGPA: " + profile.getGpa() + "\nProfile Created: " + profile.getCreated());
        }
    }

    private void loadAccessHistory(int profileId) {
        List<Access> accessHistory = dbHelper.getAccessHistory(profileId);
        AccessAdapter adapter = new AccessAdapter(this, accessHistory);
        historyListView.setAdapter(adapter);
    }
}

