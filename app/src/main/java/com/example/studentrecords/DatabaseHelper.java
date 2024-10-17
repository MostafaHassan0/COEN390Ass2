package com.example.studentrecords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "studentrecords.db"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version

    public DatabaseHelper(Context context) { // Constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProfileTable = "CREATE TABLE Profile (ProfileID INTEGER PRIMARY KEY, Name TEXT, Surname TEXT, GPA REAL, Created TEXT)"; // Create the Profile table
        String createAccessTable = "CREATE TABLE Access (AccessID INTEGER PRIMARY KEY, ProfileID INTEGER, AccessType TEXT, Timestamp TEXT)"; // Create the Access table
        db.execSQL(createProfileTable);
        db.execSQL(createAccessTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) { // Drop the tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Profile");
        db.execSQL("DROP TABLE IF EXISTS Access");
        onCreate(db);
    }

    // Methods to add a Profile to the database
    public void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database to write data into
        ContentValues values = new ContentValues(); // Create a ContentValues object to store the data
        String currentTimestamp = getDateFormat(); // Get the current timestamp formated as a string as "yyyy-MM-dd ' @ ' HH:mm:ss"

        values.put("ProfileID", profile.getProfileId()); // Put the data into the ContentValues object
        values.put("Name", profile.getName());
        values.put("Surname", profile.getSurname());
        values.put("GPA", profile.getGpa());
        values.put("Created", currentTimestamp);

        db.insert("Profile", null, values); // Insert the data into the Profile table
        this.addAccess(profile.getProfileId(), "Created"); // Add an access record of type Created to the Access table
    }

    // Method to retrive a Profile from the database by its ID
    public Profile getProfile(int profileId) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database to read data from
        Cursor cursor = db.query("Profile", null, "ProfileID = ?", new String[]{String.valueOf(profileId)}, null, null, null); // Query the Profile table for the profile with the given ID

        if (cursor != null && cursor.moveToFirst()) { // If a profile with the ProfileID is found
            String name = cursor.getString(cursor.getColumnIndex("Name")); // Get the data from the cursor and put them into variables
            String surname = cursor.getString(cursor.getColumnIndex("Surname"));
            float gpa = cursor.getFloat(cursor.getColumnIndex("GPA"));
            String created = cursor.getString(cursor.getColumnIndex("Created"));
            cursor.close(); // Close the cursor to release resources
            return new Profile(profileId, name, surname, gpa, created); // Return a new Profile object with the data
        }
        return null; // Return null if no profile is found
    }

    // Method to retrive all Profiles from the database and put them into a list of Profile objects
    public List<Profile> getAllProfiles() {
        List<Profile> profileList = new ArrayList<>(); // Create a list to store the profiles
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database to read data from

        // Query to get all profiles from the Profile table
        Cursor cursor = db.query("Profile", null, null, null, null, null, "Surname ASC");

        // Loop through the result set
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int profileId = cursor.getInt(cursor.getColumnIndex("ProfileID"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String surname = cursor.getString(cursor.getColumnIndex("Surname"));
                float gpa = cursor.getFloat(cursor.getColumnIndex("GPA"));
                String created = cursor.getString(cursor.getColumnIndex("Created"));

                // Create a new Profile object and add it to the list
                Profile profile = new Profile(profileId, name, surname, gpa, created);
                profileList.add(profile);
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor to release resources
        }
        return profileList;
    }

    // Method to delete a Profile from the database
    public void deleteProfile(int profileId) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database to write data into
        db.delete("Profile", "ProfileID = ?", new String[]{String.valueOf(profileId)}); // Delete the profile with the given ID
    }

    // Method to add an access record to the database
    public void addAccess(int profileId, String accessType) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database to write data into
        ContentValues values = new ContentValues(); // Create a ContentValues object to store the data
        String currentTimestamp = getDateFormat(); // Get the current timestamp formated as a string as "yyyy-MM-dd ' @ ' HH:mm:ss"

        values.put("ProfileID", profileId);
        values.put("AccessType", accessType);
        values.put("Timestamp", currentTimestamp);

        db.insert("Access", null, values); // Insert the data into the Access table
    }

    // Method to retrive the access history of a Profile from the database and put them into a list of Access objects
    public List<Access> getAccessHistory(int profileId) {
        List<Access> accessList = new ArrayList<>(); // Create a list to store the access history
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database to read data from
        Cursor cursor = db.query("Access", null, "ProfileID = ?", new String[]{String.valueOf(profileId)}, null, null, "Timestamp DESC"); // Query the Access table for the access history of the profile with the given ID

        // Loop through the result set
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String accessType = cursor.getString(cursor.getColumnIndex("AccessType"));
                String timestamp = cursor.getString(cursor.getColumnIndex("Timestamp"));
                accessList.add(new Access(accessType, timestamp));
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor to release resources
        }
        return accessList;
    }


    // Method to get the current timestamp formated as a string as "yyyy-MM-dd ' @ ' HH:mm:ss"
    public String getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ' @ ' HH:mm:ss"); // Create a SimpleDateFormat object to format the date
        String currentTimestamp = dateFormat.format(new Date()); // Get the current date and time and format it
        return currentTimestamp; // Return the formatted date and time as a string
    }

}

