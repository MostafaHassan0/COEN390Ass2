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
        String createProfileTable = "CREATE TABLE Profile (ProfileID INTEGER PRIMARY KEY, Name TEXT, Surname TEXT, GPA REAL, Created TEXT)";
        String createAccessTable = "CREATE TABLE Access (AccessID INTEGER PRIMARY KEY, ProfileID INTEGER, AccessType TEXT, Timestamp TEXT)";
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
        String currentTimestamp = getDateFormat();
        values.put("ProfileID", profile.getProfileId());
        values.put("Name", profile.getName());
        values.put("Surname", profile.getSurname());
        values.put("GPA", profile.getGpa());
        values.put("Created", currentTimestamp);
        db.insert("Profile", null, values);
        this.addAccess(profile.getProfileId(), "Created");
    }

    public Profile getProfile(int profileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Profile", null, "ProfileID = ?", new String[]{String.valueOf(profileId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String surname = cursor.getString(cursor.getColumnIndex("Surname"));
            float gpa = cursor.getFloat(cursor.getColumnIndex("GPA"));
            String created = cursor.getString(cursor.getColumnIndex("Created"));
            cursor.close();
            return new Profile(profileId, name, surname, gpa, created);
        }
        return null;
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
        String currentTimestamp = getDateFormat();

        // Insert the formatted timestamp
        values.put("Timestamp", currentTimestamp);
        db.insert("Access", null, values);
    }

    public String getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ' @ ' HH:mm:ss");
        String currentTimestamp = dateFormat.format(new Date());
        return currentTimestamp;
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
                String created = cursor.getString(cursor.getColumnIndex("Created"));

                // Create a new Profile object and add it to the list
                Profile profile = new Profile(profileId, name, surname, gpa, created);
                profileList.add(profile);
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor to release resources
        }

        return profileList;
    }

}

