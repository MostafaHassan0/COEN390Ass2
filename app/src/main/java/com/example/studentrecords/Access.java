package com.example.studentrecords;

public class Access {
    private final String accessType;
    private final String timestamp;

    public Access(String accessType, String timestamp) {
        this.accessType = accessType;
        this.timestamp = timestamp;
    }

    public String getAccessType() { return accessType; }
    public String getTimestamp() { return timestamp; }
}


