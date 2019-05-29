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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashSet;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizerEditEventActivity extends AppCompatActivity implements View.OnClickListener {
    final boolean[] selectedList = new boolean[17];
    Calendar c;
    Event e;
    int selectedPosition = 0;
    private ProgressBar progress;
    private TimedToast toast;
    private Button btn_create;
    private EditText et_title, et_description, et_location, et_opening, et_ending, et_category, et_opening_time,
            et_ending_time, et_tag1;
    private int openingYear, openingMonth, openingDay, endingYear, endingMonth, endingDay, openingHour, openingMinute,
            endingHour, endingMinute;
    private CoordinatorLayout mainLayout;
    private HashSet<String> selectedTags;
    private String[] categoryTags;
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
        btn_create.setText("EDIT");
        progress = (ProgressBar) findViewById(R.id.progress);
        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_description);
        et_location = (EditText) findViewById(R.id.et_location);
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

        et_tag1.setEnabled(true);

        et_ending.setKeyListener(null);
        selectedTags = new HashSet<>();
        et_location.setKeyListener(null);
        et_location.setOnClickListener(this);
        e = (Event) getIntent().getSerializableExtra("Event");
        et_title.setText(e.getTitle());
        et_description.setText(e.getDescription());
        et_location.setText(e.getLocation());
        StringTokenizer st = new StringTokenizer(e.getOpening(), " ");
        String openingDate = st.nextToken();
        StringTokenizer odt = new StringTokenizer(openingDate, "-");
        openingYear = Integer.parseInt(odt.nextToken());
        openingMonth = Integer.parseInt(odt.nextToken()) - 1;
        openingDay = Integer.parseInt(odt.nextToken());
        et_opening.setText(openingDay + "-" + (openingMonth + 1) + "-" + openingYear);
        String openingTime = st.nextToken();
        StringTokenizer ott = new StringTokenizer(openingTime, ":");
        openingHour = Integer.parseInt(ott.nextToken());
        openingMinute = Integer.parseInt(ott.nextToken());
        et_opening_time.setText(openingTime);
        StringTokenizer st2 = new StringTokenizer(e.getEnding(), " ");
        String endingDate = st2.nextToken();
        StringTokenizer edt = new StringTokenizer(endingDate, "-");
        endingYear = Integer.parseInt(edt.nextToken());
        endingMonth = Integer.parseInt(edt.nextToken()) - 1;
        endingDay = Integer.parseInt(edt.nextToken());
        et_ending.setText(endingDay + "-" + (endingMonth + 1) + "-" + endingYear);
        String endingTime = st2.nextToken();
        StringTokenizer ett = new StringTokenizer(endingTime, ":");
        endingHour = Integer.parseInt(ett.nextToken());
        endingMinute = Integer.parseInt(ett.nextToken());
        et_ending_time.setText(endingTime);
        et_category.setText(e.getCategory());
        et_tag1.setText(e.getTags().replaceAll("\\-", "\n").trim());

        categoryTags = new String[0];
        String category = et_category.getText().toString();
        if (category.equals("Festival")) {
            categoryTags = getResources().getStringArray(R.array.festival_tags);
            selectedPosition = 0;
        } else if (category.equals("Food & Drink")) {
            categoryTags = getResources().getStringArray(R.array.foodanddrink_tags);
            selectedPosition = 1;
        } else if (category.equals("Disco")) {
            categoryTags = getResources().getStringArray(R.array.disco_tags);
            selectedPosition = 2;
        } else if (category.equals("Live Music")) {
            categoryTags = getResources().getStringArray(R.array.livemusic_tags);
            selectedPosition = 3;
        } else if (category.equals("Cinema")) {
            categoryTags = getResources().getStringArray(R.array.cinema_tags);
            selectedPosition = 4;
        } else if (category.equals("Outdoor")) {
            categoryTags = getResources().getStringArray(R.array.outdoor_tags);
            selectedPosition = 5;
        } else if (category.equals("Theatre")) {
            categoryTags = getResources().getStringArray(R.array.theatre_tags);
            selectedPosition = 6;
        } else if (category.equals("Museum")) {
            categoryTags = getResources().getStringArray(R.array.museum_tags);
            selectedPosition = 7;
        }

        StringTokenizer st3 = new StringTokenizer(e.getTags(), "-");
        while (st3.hasMoreTokens()) {
            String tag = st3.nextToken();
            for (int i = 0; i < categoryTags.length; i++) {
                if (tag.equals(categoryTags[i])) {
                    selectedList[i] = true;
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.btn_create:
            editEvent();
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
                        selectedTags.clear();
                        for (int i = 0; i < selectedList.length; i++)
                            selectedList[i] = false;
                        et_tag1.setText("");
                        et_tag1.setEnabled(true);
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
        TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

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
        DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

    private void editEvent() {
        e.setTitle(et_title.getText().toString());
        e.setDescription(et_description.getText().toString());
        e.setLocation(et_location.getText().toString());
        e.setOpening(et_opening.getText().toString());
        e.setEnding(et_ending.getText().toString());
        e.setOpeningTime(et_opening_time.getText().toString());
        e.setEndingTime(et_ending_time.getText().toString());
        e.setCategory(et_category.getText().toString());
        e.setTags(et_tag1.getText().toString().replaceAll("\\r|\\n", "-"));

        if (e.isValid()) {
            if (checkTime()) {

                progress.setVisibility(View.VISIBLE);
                editEventProcess();

                finish();
                overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

            } else {
                Snackbar.make(mainLayout, "Wrong date !", Snackbar.LENGTH_LONG).show();
            }
        } else
            Snackbar.make(mainLayout, "Fields are empty !", Snackbar.LENGTH_LONG).show();
    }

    private void editEventProcess() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.MODIFY_EVENT);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setEvent(e);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                e.setLatitude("" + place.getLatLng().latitude);
                e.setLongitude("" + place.getLatLng().longitude);
                et_location.setText(place.getName() + ",\n" + place.getAddress() + "\n" + place.getPhoneNumber());
            }
        }
    }

    private Activity getIstance() {
        return getIstance();
    }
}
