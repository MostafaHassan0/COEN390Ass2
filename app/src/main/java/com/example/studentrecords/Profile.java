package com.example.studentrecords;

public class Profile {
    private int profileId;
    private String name;
    private String surname;
    private float gpa;

    public Profile(int profileId, String name, String surname, float gpa) {
        this.profileId = profileId;
        this.name = name;
        this.surname = surname;
        this.gpa = gpa;
    }

    public int getProfileId() { return profileId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public float getGpa() { return gpa; }
}

