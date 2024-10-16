package com.example.studentrecords;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add_button;
    TextView infotextView;
    ListView Profiles_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) { // Check if the action bar is there in the first place to prevent crashing
            getSupportActionBar().setTitle("Main Activity");
        }

        add_button = findViewById(R.id.add_button);
        infotextView = findViewById(R.id.infotextView);
        Profiles_listView = findViewById(R.id.Profiles_listView);

        add_button.setOnClickListener(v -> {
            new InsertProfileDialogFragment().show(getSupportFragmentManager(), "InsertProfile");
        });


    }
}
