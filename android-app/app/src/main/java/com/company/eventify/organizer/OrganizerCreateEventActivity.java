package com.company.eventify.organizer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.Event;
import com.company.eventify.utilities.RequestInterface;
import com.company.eventify.utilities.TimedToast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizerCreateEventActivity extends AppCompatActivity implements View.OnClickListener {
    Calendar c;
    String latitude = "0.0";
    String longitude = "0.0";
    int selectedPosition = 0;
    private TimedToast toast;
    private ProgressBar progress;
    private Button btn_create;
    private EditText et_title, et_description, et_location, et_opening, et_ending, et_category, et_opening_time,
            et_ending_time, et_tag1;
    private int openingYear = -1, openingMonth = -1, openingDay = -1, endingYear = -1, endingMonth = -1, endingDay = -1,
            openingHour = -1, openingMinute = -1, endingHour = -1, endingMinute = -1;
    private CoordinatorLayout mainLayout;
    private HashSet<String> selectedTags;
    private String[] categoryTags;
    private boolean[] selectedList;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        toast = new TimedToast(null, "");
        setContentView(R.layout.create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainLayout = (CoordinatorLayout) findViewById(R.id.layout_create_event);
        btn_create = (Button) findViewById(R.id.btn_create);
        progress = (ProgressBar) findViewById(R.id.progress);
        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_description);
        et_location = (EditText) findViewById(R.id.et_location);
        et_location.setText(pref.getString(Constants.LOCATION, "null"));

        et_opening = (EditText) findViewById(R.id.et_opening);
        et_opening_time = (EditText) findViewById(R.id.et_opening_time);
        et_ending_time = (EditText) findViewById(R.id.et_ending_time);
        et_category = (EditText) findViewById(R.id.et_category);
        et_ending = (EditText) findViewById(R.id.et_ending);

        et_category = (EditText) findViewById(R.id.et_category);
        et_tag1 = (EditText) findViewById(R.id.et_category);
        et_tag1 = (EditText) findViewById(R.id.et_tag1);
        btn_create.setOnClickListener(this);
        et_opening.setOnClickListener(this);
        et_ending.setOnClickListener(this);
        et_opening_time.setOnClickListener(this);
        et_ending_time.setOnClickListener(this);
        et_category.setOnClickListener(this);
        et_tag1.setOnClickListener(this);
        et_tag1.setEnabled(false);
        et_opening.setKeyListener(null);
        et_opening_time.setKeyListener(null);
        et_ending_time.setKeyListener(null);
        et_category.setKeyListener(null);
        et_tag1.setKeyListener(null);

        et_ending.setKeyListener(null);
        selectedTags = new HashSet<String>();
        et_location.setKeyListener(null);
        et_location.setOnClickListener(this);

        selectedList = new boolean[0];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

            return true;
        }
        // other menu select events may be present here

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.btn_create:
            createEvent();
            break;
        case R.id.et_opening:
            chooseOpeningDate();
            break;
        case R.id.et_ending:
            chooseEndingDate();
            break;
        case R.id.et_opening_time:
            chooseOpeningTime();
            break;
        case R.id.et_ending_time:
            chooseEndingTime();
            break;
        case R.id.et_category:
            chooseCategory();
            break;
        case R.id.et_tag1:
            chooseTags();
            break;
        case R.id.et_location:
            findPlace(v);
            break;

        }

    }

    private void chooseTags() {
        Dialog dialog = onCreateDialogMultipleChoice();
        dialog.show();
    }

    private void chooseCategory() {
        Dialog dialog = onCreateDialogSingleChoice();
        dialog.show();
    }

    public Dialog onCreateDialogSingleChoice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        final CharSequence[] categories = {

                "Festival", "Food & Drink", "Disco", "Live Music", "Cinema", "Outdoor", "Theatre", "Museum" };

        builder.setTitle("Select category")
                .setSingleChoiceItems(categories, selectedPosition, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        et_category.setText(categories[selectedPosition].toString());
                        et_tag1.setEnabled(true);
                        selectedList = new boolean[0];
                        et_tag1.setText("");
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public Dialog onCreateDialogMultipleChoice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        categoryTags = new String[0];
        String category = et_category.getText().toString();
        if (category.equals("Theatre"))
            categoryTags = getResources().getStringArray(R.array.theatre_tags);
        else if (category.equals("Cinema"))
            categoryTags = getResources().getStringArray(R.array.cinema_tags);
        else if (category.equals("Food & Drink"))
            categoryTags = getResources().getStringArray(R.array.foodanddrink_tags);
        else if (category.equals("Live Music"))
            categoryTags = getResources().getStringArray(R.array.livemusic_tags);
        else if (category.equals("Outdoor"))
            categoryTags = getResources().getStringArray(R.array.outdoor_tags);
        else if (category.equals("Festival"))
            categoryTags = getResources().getStringArray(R.array.festival_tags);
        else if (category.equals("Disco"))
            categoryTags = getResources().getStringArray(R.array.disco_tags);
        else if (category.equals("Museum"))
            categoryTags = getResources().getStringArray(R.array.museum_tags);
        boolean[] tempSelectedList = new boolean[categoryTags.length];
        for (int i = 0; i < selectedList.length; i++) {
            tempSelectedList[i] = selectedList[i];
        }
        selectedList = tempSelectedList;
        builder.setTitle("Select tags")
                .setMultiChoiceItems(categoryTags, selectedList, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                })

                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < categoryTags.length; i++) {
                            if (selectedList[i]) {
                                selectedTags.add(categoryTags[i]);
                            } else {
                                selectedTags.remove(categoryTags[i]);
                            }
                        }
                        String tagsList = "";

                        for (String s : selectedTags)
                            tagsList = tagsList + s + "\n";
                        if (!tagsList.equals("")) {
                            tagsList = tagsList.substring(0, tagsList.length() - 1);
                        }
                        et_tag1.setText(tagsList);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private void chooseEndingTime() {
        TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, R.style.DialogStyle,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        et_ending_time.setText(hourOfDay + ":" + String.format("%02d", minute));
                        endingHour = hourOfDay;
                        endingMinute = minute;
                        checkTime();

                    }
                }, 0, 0, false);
        timePickerDialog2.show();
    }

    private void chooseOpeningTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogStyle,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        et_opening_time.setText(hourOfDay + ":" + String.format("%02d", minute));
                        openingHour = hourOfDay;
                        openingMinute = minute;
                        checkTime();

                    }
                }, 0, 0, false);
        timePickerDialog.show();

    }

    private void chooseEndingDate() {
        if (endingYear == -1) {
            c = Calendar.getInstance();
            endingYear = c.get(Calendar.YEAR);
            endingMonth = c.get(Calendar.MONTH);
            endingDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, R.style.DialogStyle,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et_ending.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        endingDay = dayOfMonth;
                        endingMonth = monthOfYear;
                        endingYear = year;
                        checkTime();

                    }

                }, endingYear, endingMonth, endingDay);

        datePickerDialog2.show();

    }

    private void chooseOpeningDate() {
        if (openingYear == -1) {
            c = Calendar.getInstance();
            openingYear = c.get(Calendar.YEAR);
            openingMonth = c.get(Calendar.MONTH);
            openingDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogStyle,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et_opening.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        openingDay = dayOfMonth;
                        openingMonth = monthOfYear;
                        openingYear = year;
                        if (endingDay == -1) {
                            et_ending.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            endingDay = dayOfMonth;
                            endingMonth = monthOfYear;
                            endingYear = year;
                        }
                        checkTime();

                    }
                }, openingYear, openingMonth, openingDay);
        datePickerDialog.show();

    }

    private boolean checkTime() {
        // Log.d(Constants.TAG, "from "+openingMonth+"/"+openingDay+"
        // "+openingHour+":"+openingMinute+" to "+endingMonth+"/"+endingDay+"
        // "+endingHour+":"+endingMinute);
        if (openingDay == -1 || endingDay == -1 || endingHour == -1 || openingHour == -1) {
            return false;
        }
        if (openingDay != -1 && endingDay != -1) {
            if (openingYear == endingYear) {
                if (openingMonth == endingMonth) {
                    if (openingDay == endingDay) {
                        if (openingHour != -1 && endingHour != -1) {
                            if (openingHour == endingHour) {
                                if (openingMinute > endingMinute) {
                                    Snackbar.make(mainLayout, "Opening minute is later ending minute!",
                                            Snackbar.LENGTH_LONG).show();

                                    return false;
                                }
                            } else if (openingHour > endingHour) {
                                Snackbar.make(mainLayout, "Opening hour is later ending hour!", Snackbar.LENGTH_LONG)
                                        .show();
                                return false;
                            }
                        }

                    } else if (openingDay > endingDay) {
                        Snackbar.make(mainLayout, "Opening day is later ending day!", Snackbar.LENGTH_LONG).show();
                        return false;
                    }
                } else if (openingMonth > endingMonth) {
                    Snackbar.make(mainLayout, "Opening month is later ending month!", Snackbar.LENGTH_LONG).show();
                    return false;
                }
            } else if (openingYear > endingYear) {
                Snackbar.make(mainLayout, "Opening year is later ending year!", Snackbar.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void createEvent() {
        Event e = new Event();
        e.setTitle(et_title.getText().toString());
        e.setDescription(et_description.getText().toString());
        e.setLocation(et_location.getText().toString());
        e.setOpening(et_opening.getText().toString());
        e.setEnding(et_ending.getText().toString());
        e.setOpeningTime(et_opening_time.getText().toString());
        e.setEndingTime(et_ending_time.getText().toString());
        e.setCategory(et_category.getText().toString());
        e.setTags(et_tag1.getText().toString().replaceAll("\\r|\\n", "-"));

        if (checkTime() && e.isValid()) {

            progress.setVisibility(View.VISIBLE);
            insertEvent(e);

            finish();
            overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

        } else {

            Snackbar.make(mainLayout, "Fields are empty !", Snackbar.LENGTH_LONG).show();
        }
    }

    private void insertEvent(Event event) {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        event.setPhone(pref.getString(Constants.PHONE, ""));
        if (latitude.equals("0.0"))
            event.setLatitude(pref.getString(Constants.LATITUDE, "NULL"));
        else
            event.setLatitude(latitude);
        if (longitude.equals("0.0"))
            event.setLongitude(pref.getString(Constants.LONGITUDE, "NULL"));
        else
            event.setLongitude(longitude);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.INSERT_EVENT);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setEvent(event);

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    Snackbar.make(mainLayout, resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    progress.setVisibility(View.GONE);
                } catch (Exception e) {
                    toast.showToast(Constants.NO_NETWORK_ERROR, 1000, getIstance());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.GONE);
                Log.d(Constants.TAG, "failed");
                Snackbar.make(mainLayout, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);

                latitude = "" + place.getLatLng().latitude;
                longitude = "" + place.getLatLng().longitude;
                et_location.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("Tag", status.getStatusMessage());
            }
        }
    }

    private Activity getIstance() {
        return getIstance();
    }

}
