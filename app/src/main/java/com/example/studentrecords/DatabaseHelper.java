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
    private static final String DATABASE_NAME = "studentrecords.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProfileTable = "CREATE TABLE Profile (ProfileID INTEGER PRIMARY KEY, Name TEXT, Surname TEXT, GPA REAL)";
        String createAccessTable = "CREATE TABLE Access (AccessID INTEGER PRIMARY KEY, ProfileID INTEGER, AccessType TEXT, Timestamp TIMESTAMP DEFAULT Current_Timestamp)";
        db.execSQL(createProfileTable);
        db.execSQL(createAccessTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Profile");
        db.execSQL("DROP TABLE IF EXISTS Access");
        onCreate(db);
    }

    public void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ProfileID", profile.getProfileId());
        values.put("Name", profile.getName());
        values.put("Surname", profile.getSurname());
        values.put("GPA", profile.getGpa());
        db.insert("Profile", null, values);
    }

    public Profile getProfile(int profileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Profile", null, "ProfileID = ?", new String[]{String.valueOf(profileId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String surname = cursor.getString(cursor.getColumnIndex("Surname"));
            float gpa = cursor.getFloat(cursor.getColumnIndex("GPA"));
            cursor.close();
            return new Profile(profileId, name, surname, gpa);
        }
        return null;
    }

    public String getCreated(int profileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String createdTimestamp = null;

        // Querying the Access table for the "created" access type
        Cursor cursor = db.query(
                "Access",
                new String[]{"Timestamp"},  // We only need the Timestamp
                "ProfileID = ? AND AccessType = ?",  // WHERE clause
                new String[]{String.valueOf(profileId), "Created"},  // WHERE parameters
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the timestamp
            createdTimestamp = cursor.getString(cursor.getColumnIndex("Timestamp"));
            cursor.close();
        }

        return createdTimestamp;  // Returns null if no 'created' entry is found
    }

    public void deleteProfile(int profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Profile", "ProfileID = ?", new String[]{String.valueOf(profileId)});
    }

    public void addAccess(int profileId, String accessType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ProfileID", profileId);
        values.put("AccessType", accessType);
        db.insert("Access", null, values);
    }

    public List<Access> getAccessHistory(int profileId) {
        List<Access> accessList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Access", null, "ProfileID = ?", new String[]{String.valueOf(profileId)}, null, null, "Timestamp DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String accessType = cursor.getString(cursor.getColumnIndex("AccessType"));
                String timestamp = cursor.getString(cursor.getColumnIndex("Timestamp"));
                accessList.add(new Access(accessType, timestamp));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return accessList;
    }

    public List<Profile> getAllProfiles() {
        List<Profile> profileList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all profiles from the Profile table
        Cursor cursor = db.query("Profile", null, null, null, null, null, "Surname ASC");

        // Loop through the result set
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int profileId = cursor.getInt(cursor.getColumnIndex("ProfileID"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String surname = cursor.getString(cursor.getColumnIndex("Surname"));
                float gpa = cursor.getFloat(cursor.getColumnIndex("GPA"));

                // Create a new Profile object and add it to the list
                Profile profile = new Profile(profileId, name, surname, gpa);
                profileList.add(profile);
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor to release resources
        }

        return profileList;
    }

}

