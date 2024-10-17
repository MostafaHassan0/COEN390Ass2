package com.example.studentrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class InsertProfileDialogFragment extends DialogFragment {

    private static final String TAG = "InsertProfile"; // Tag for dialog fragment

    // UI elements
    private EditText surnameEditText, nameEditText, sidEditText, gpaEditText;
    private Button saveButton, cancelButton;

    private DatabaseHelper dbHelper; // Database

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Create a dialog builder
        LayoutInflater inflater = requireActivity().getLayoutInflater(); // Get the layout inflater
        View view = inflater.inflate(R.layout.fragment_insert_profile_dialog, null); // Inflate the layout

        builder.setView(view); // Set the view of the dialog

        dbHelper = new DatabaseHelper(getActivity()); // Create a database

        // Get the UI elements
        surnameEditText = view.findViewById(R.id.editTextSurname);
        nameEditText = view.findViewById(R.id.editTextName);
        sidEditText = view.findViewById(R.id.editTextSID);
        gpaEditText = view.findViewById(R.id.editTextGPA);
        saveButton = view.findViewById(R.id.Save_button);
        cancelButton = view.findViewById(R.id.Cancel_button);

        // Set the click listeners for the buttons
        saveButton.setOnClickListener(v -> saveProfile());
        cancelButton.setOnClickListener(v -> dismiss());

        return builder.create(); // Create the dialog
    }

    // Method to save the profile when save button is clicked
    private void saveProfile() {
        if (!nameEditText.getText().toString().isEmpty() && // Check if all fields are filled
                !surnameEditText.getText().toString().isEmpty() &&
                !sidEditText.getText().toString().isEmpty() &&
                !gpaEditText.getText().toString().isEmpty())
        {
            // Get the values from the UI elements and store in variables
            String name = nameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();
            int profileId = Integer.parseInt(sidEditText.getText().toString());
            float gpa = Float.parseFloat(gpaEditText.getText().toString());

            // Check if the values are valid
            if (profileId > 0 && gpa >= 0 && gpa <= 4.3)
            {
                if (dbHelper.getProfile(profileId) == null) // checking if profileID already exists in database
                {
                    Profile profile = new Profile(profileId, name, surname, gpa , dbHelper.getDateFormat()); // Create a new Profile object
                    dbHelper.addProfile(profile); // Add the profile to the database
                    Toast.makeText(getActivity(), "Profile saved!", Toast.LENGTH_SHORT).show(); // Show a toast message
                    ((MainActivity) getActivity()).loadProfiles(); // load profiles in main activity
                    dismiss(); // Dismiss the dialog

                } else { Toast.makeText(getActivity(), "Student ID already exists!", Toast.LENGTH_SHORT).show(); } // Show a toast message if profileID already exists
            } else { Toast.makeText(getActivity(), "Invalid Student ID and/or GPA", Toast.LENGTH_SHORT).show(); } // Show a toast message if values are invalid
        } else { Toast.makeText(getActivity(), "Please fill all fields!", Toast.LENGTH_SHORT).show(); } // Show a toast message if any field is empty
    }
}
