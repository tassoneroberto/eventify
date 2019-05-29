package com.company.eventify.utilities;

import android.graphics.Typeface;

import java.util.ArrayList;

public class Constants {

    public static final String API_URL = "https://apisocialevent.altervista.org/";
    public static final String API_KEY = "aEk027VIhPUc79SHLHq6zyUUpCDTZgsm";
    // Login/Registration operations
    public static final String REGISTER_USER = "registerUser";
    public static final String REGISTER_ORGANIZER = "registerOrganizer";
    public static final String LOGIN_OPERATION = "login";
    public static final String CHANGE_PASSWORD = "changePassword";
    public static final String DELETE_ACCOUNT = "deleteAccount";
    public static final String GET_ID = "getId";

    // Organizer operations
    public static final String INSERT_EVENT = "insertEvent";
    public static final String REMOVE_EVENT = "removeEvent";
    public static final String MODIFY_EVENT = "modifyEvent";
    public static final String GET_OWNED_EVENTS = "getOwnedEvents";

    // User operations
    public static final String GET_ACCOUNT_CUSTOM_EVENTS = "getAccountCustomEvents";
    public static final String GET_NEAR_EVENTS = "getUserNearEvents";
    public static final String SEARCH_EVENTS = "searchEvents";
    public static final String GET_CALENDAR_EVENTS = "getCalendarEvents";
    public static final String GET_PREF = "getPref";
    public static final String SAVE_PREF = "savePref";
    public static final String ADD_TO_CALENDAR = "addToCalendar";
    public static final String REMOVE_FROM_CALENDAR = "removeFromCalendar";

    // Server messages + toastbar messages
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String NO_EVENTS_FOUND = "No events found";
    public static final String ERROR_SEARCH = "Connection error";
    public static final String WRONG_SEARCH = "Search input too short";
    public static final String NO_NETWORK_ERROR = "Check your connection and try again.";
    public static final String FACEBOOK_LOGIN_ERROR = "Facebook login failed";

    // Console log tag
    public static final String TAG = "Eventify";

    // Default preference values for new users
    public static final int DEFAULT_RANGE_TIME = 2;
    public static final int DEFAULT_RANGE_DISTANCE = 2;

    // Shared Preferences key names
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String FIRST_START = "firstStart";
    public static final String IS_ORGANIZER = "isOrganizer";
    public static final String FIRSTNAME = "";
    public static final String LASTNAME = "";
    public static final String PREF = "PREF";
    public static final String EMAIL = "email";
    public static final String LOGIN_ID = "login_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    // Google Maps API key
    public static final String MAPS_API_KEY = "AIzaSyBZcxMQ5pDdjck0pfTIKkRsJA6egIJyRCo";
    public static String GOOGLE_LOG = "GoogleLog";

    // Other
    public static Typeface typeface;
    public static ArrayList<Event> events;
    public static String NOTIFICATION_SWITCH = "notification_switch";
}
