package com.example.studentrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class InsertProfileDialogFragment extends DialogFragment {

    private EditText surnameEditText;
    private EditText nameEditText;
    private EditText idEditText;
    private EditText gpaEditText;
    Button saveButton;
    Button cancelButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_insert_profile_dialog, null);

//        dbHelper = new DatabaseHelper(getActivity());

        surnameEditText = view.findViewById(R.id.editTextSurname);
        nameEditText = view.findViewById(R.id.editTextName);
        idEditText = view.findViewById(R.id.editTextSID);
        gpaEditText = view.findViewById(R.id.editTextGPA);
        saveButton = view.findViewById(R.id.Save_button);
        cancelButton = view.findViewById(R.id.Cancel_button);

        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);


        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_insert_profile_dialog, container, false);
//    }
}