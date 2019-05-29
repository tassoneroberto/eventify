package com.company.eventify.utilities;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.StringTokenizer;

public class Event implements Serializable, Comparable {
    private String id;

    private String organizer_name;
    private String title;
    private String description;
    private String location;
    private String phone;
    private String latitude;
    private String longitude;
    private String opening;
    private String ending;
    private String openingTime;
    private String endingTime;
    private String category;
    private String tags;
    private boolean added;

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public String toString() {
        return "Title: " + title + "; Description: " + description + "; Category: " + category + "; Starting at :"
                + getOpening() + " " + getOpeningTime() + "; Ending at :" + getEnding() + " " + getEndingTime();
    }

    public String getOrganizer() {
        return organizer_name;
    }

    public boolean isValid() {
        return !(title.equals("") || category.equals("") || location.equals("") || tags.equals("") || opening.equals("")
                || ending.equals("") || openingTime.equals("") || endingTime.equals(""));
    }

    public int compareTo(@NonNull Object o) {
        if (!(o instanceof Event))
            throw new IllegalArgumentException();
        Event e = (Event) o;
        StringTokenizer st = new StringTokenizer(e.getOpening(), "-: ");
        StringTokenizer st1 = new StringTokenizer(this.getOpening(), "-: ");
        while (st.hasMoreTokens()) {
            String s = st1.nextToken();
            int res = Integer.parseInt(s) - Integer.parseInt(st.nextToken());
            if (res > 0)
                return 1;
            if (res < 0)
                return -1;
        }
        return 0;

    }

}
