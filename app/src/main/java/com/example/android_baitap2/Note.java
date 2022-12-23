package com.example.android_baitap2;

import java.io.Serializable;

public class Note implements Serializable {

    private String title;
    private String description;
    private long timeCreated;

    public Note()
    {
    }

    public Note(String title, String description, long timeCreated) {
        this.title = title;
        this.description = description;
        this.timeCreated = timeCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timeCreated=" + timeCreated +
                '}';
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
