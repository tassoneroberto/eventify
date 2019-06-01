package com.company.eventify.models;

import com.company.eventify.utilities.Event;

import java.util.ArrayList;

public class ServerResponse {

    private String result;
    private String message;
    private String email;
    private boolean is_organizer;
    private String organizer_name;
    private String firstname;
    private String lastname;
    private String phone;
    private String location;
    private String latitude;
    private String longitude;
    private String account_id;
    private String tags;
    private int rangeTime;
    private int rangeDistance;

    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLocation() {
        return location;
    }

    public String getOrganizer_name() {
        return organizer_name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean is_organizer() {
        return is_organizer;
    }

    public int getRangeDistance() {
        return rangeDistance;
    }

    public int getRangeTime() {
        return rangeTime;
    }

    public String getTags() {
        return tags;
    }
}
