package com.company.eventify.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.MyPagerAdapter;
import com.company.eventify.utilities.NotificationEventReceiver;
import com.company.eventify.utilities.RequestInterface;
import com.company.eventify.utilities.TimedToast;
import com.company.eventify.utilities.UserInteraction;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public Button shareButton;
    private boolean confirmClose = false;
    private SharedPreferences pref;
    private BottomBar bar;
    private HashMap<String, boolean[]> selectedTags;
    private HashMap<String, String[]> categoryTags;
    private boolean mRequestingLocationUpdates;
    private String REQUESTING_LOCATION_UPDATES_KEY;
    private String LOCATION_KEY;
    private String LAST_UPDATED_TIME_STRING_KEY;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double latitude = 0.0, longitude = 0.0;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private ViewPager pager;
    private MyPagerAdapter mPagerAdapter;
    private boolean changeCalendar = true;
    private String eventTitle;
    private String eventDescription;
    private String eventImageUrl;
    private CallbackManager callbackManager;
    private TimedToast toast;

    public HashMap<String, boolean[]> getSelectedTags() {
        return selectedTags;
    }

    public HashMap<String, String[]> getCategoryTags() {
        return categoryTags;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);

        toast = new TimedToast(null, "");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        setContentView(R.layout.activity_user);
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000).setFastestInterval(5000);
        pref = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        bar = (BottomBar) findViewById(R.id.bottomBar);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareButton = (Button) findViewById(R.id.fb_share_btn);
        shareButton.setVisibility(View.GONE);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInteraction.shareOnFB(UserActivity.this, callbackManager, eventTitle, eventDescription,
                        eventImageUrl);
            }
        });
        NotificationEventReceiver.setupAlarm(getApplicationContext());

        makePager();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        selectedTags = new HashMap<String, boolean[]>();
        categoryTags = new HashMap<String, String[]>();
        latitude = 0;
        longitude = 0;
        if (!pref.contains(Constants.NOTIFICATION_SWITCH))
            getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE).edit()
                    .putBoolean(Constants.NOTIFICATION_SWITCH, true).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    public void setExit(boolean exit) {
        this.confirmClose = exit;
    }

    public void setEvent(String title, String description, String UrlImage) {
        eventTitle = title;
        eventDescription = description;
        eventImageUrl = UrlImage;

    }

    public void setSelectedItemBar(int index) {
        bar.animate().translationY(0);
        bar.selectTabAtPosition(index, true);
    }

    private boolean getCalendar() {
        return this.changeCalendar;
    }

    public void setCalendar(boolean change) {
        this.changeCalendar = change;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * this method is used to check whether the position has been detected
     */
    public boolean getProviderEnabled() {
        return !(getLatitude() == 0 && getLongitude() == 0);
    }

    private void makePager() {
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(mPagerAdapter);
        pager.addOnPageChangeListener(this);
        pager.setPageTransformer(true, new RotateUpTransformer());

    }

    private void makeBar() {
        bar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_near) {
                    pager.setCurrentItem(1);
                }
                if (tabId == R.id.tab_home) {
                    pager.setCurrentItem(0);
                }
                if (tabId == R.id.tab_calendar) {
                    pager.setCurrentItem(2);
                }
                if (tabId == R.id.tab_search) {
                    pager.setCurrentItem(3);
                }
                if (tabId == R.id.tab_settings) {
                    pager.setCurrentItem(4);
                }

            }
        });
        if (pager.getCurrentItem() == 0) {
            UserFragmentHome.newInstance().getHomeEvents();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addEvent(String event_id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.ADD_TO_CALENDAR);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setEvent_id(event_id);
        setCalendar(true);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "failed");
            }
        });
    }

    public void removeEvent(String event_id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REMOVE_FROM_CALENDAR);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, "NULL"));
        request.setEvent_id(event_id);

        setCalendar(true);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "failed");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            if (!confirmClose) {
                confirmClose = true;
                toast.showToast("Go to settings to logout!\nPress again to close", 800, this);
            } else {
                finish();
            }
        } else {
            pager.setCurrentItem(0);
        }
    }

    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();

        }
        makeBar();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        makeBar();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        makeBar();
    }

    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setExit(false);
        setSelectedItemBar(position);

        if (pager.getCurrentItem() == 0) {
            UserFragmentHome.newInstance().getHomeEvents();
            setCalendar(true);
        }
        if (pager.getCurrentItem() == 1) {
            UserFragmentNear.newInstance().getNearEvents();
        }
        if (pager.getCurrentItem() == 2) {
            if (getCalendar()) {
                setCalendar(false);
                UserFragmentCalendar.newInstance().getCalendarEvents();
            } else {
                UserFragmentCalendar.newInstance().refreshCalendar();
            }
        }
        if (pager.getCurrentItem() == 4) {
            setCalendar(true);
            UserFragmentSettings.newInstance().getPref();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void showToast(String noNetworkError, int i, FragmentActivity activity) {
        toast.showToast(noNetworkError, i, activity);
    }
}