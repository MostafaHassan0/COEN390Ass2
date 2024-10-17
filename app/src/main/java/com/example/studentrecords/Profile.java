package com.example.studentrecords;

public class Profile {
    private int profileId;
    private String name;
    private String surname;
    private float gpa;
    private String created;

    public Profile(int profileId, String name, String surname, float gpa, String created) {
        this.profileId = profileId;
        this.name = name;
        this.surname = surname;
        this.gpa = gpa;
        this.created = created;
    }

    public int getProfileId() { return profileId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public float getGpa() { return gpa; }
    public String getCreated() { return created; }
}

