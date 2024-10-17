package com.example.studentrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class InsertProfileDialogFragment extends DialogFragment {

    private static final String TAG = "InsertProfile";

    private EditText surnameEditText, nameEditText, sidEditText, gpaEditText;
    private Button saveButton, cancelButton;
    private DatabaseHelper dbHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_insert_profile_dialog, null);

        builder.setView(view);

        dbHelper = new DatabaseHelper(getActivity());

        surnameEditText = view.findViewById(R.id.editTextSurname);
        nameEditText = view.findViewById(R.id.editTextName);
        sidEditText = view.findViewById(R.id.editTextSID);
        gpaEditText = view.findViewById(R.id.editTextGPA);
        saveButton = view.findViewById(R.id.Save_button);
        cancelButton = view.findViewById(R.id.Cancel_button);

        saveButton.setOnClickListener(v -> saveProfile());
        cancelButton.setOnClickListener(v -> dismiss());


        return builder.create();
    }

    private void saveProfile() {

        if (!nameEditText.getText().toString().isEmpty() && !surnameEditText.getText().toString().isEmpty() && !sidEditText.getText().toString().isEmpty() && !gpaEditText.getText().toString().isEmpty()) {

            String name = nameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();
            int profileId = Integer.parseInt(sidEditText.getText().toString());
            float gpa = Float.parseFloat(gpaEditText.getText().toString());

            if (profileId > 0 && gpa >= 0 && gpa <= 4.3) {
                if (dbHelper.getProfile(profileId) == null) { // checking if profileid already existes in db
                    Profile profile = new Profile(profileId, name, surname, gpa , dbHelper.getDateFormat());
                    dbHelper.addProfile(profile);
                    Toast.makeText(getActivity(), "Profile saved!", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).loadProfiles();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Student ID already exists!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Invalid Student ID and/or GPA", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }
}
