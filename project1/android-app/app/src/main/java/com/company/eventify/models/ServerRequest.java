package com.company.eventify.models;

import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.Event;

public class ServerRequest {

    private final String api_key = Constants.API_KEY;
    private String operation;

    private String account_id;
    private String event_id;
    private String email;
    private String password;
    private String old_password;
    private String new_password;

    private String firstname;
    private String lastname;
    private String tags;
    private int rangeDistance;
    private int rangeTime;
    private String query;

    private String organizer_name;
    private String phone;

    private String location;
    private String latitude;
    private String longitude;

    private Event event;

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRangeDistance(int rangeDistance) {
        this.rangeDistance = rangeDistance;
    }

    public void setRangeTime(int rangeTime) {
        this.rangeTime = rangeTime;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
