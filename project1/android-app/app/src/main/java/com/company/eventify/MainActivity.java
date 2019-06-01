package com.company.eventify;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.company.eventify.login.Intro;
import com.company.eventify.login.LoginFragment;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.organizer.OrganizerActivity;
import com.company.eventify.user.UserActivity;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.RequestInterface;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code
    // before shipping.
    private static final String TWITTER_KEY = "WmhicV2P1dS4gEbmyd8uPmtbu";
    private static final String TWITTER_SECRET = "3XU5oDBAQxzoplEwYH1aatTduvozDbjRLDRbSfdpVbcvs5WPo5";

    private static final int REQUEST_LOCATION = 2;
    private SharedPreferences pref;
    private boolean exit = false;
    private Toast mToastToShow = null;
    private String messageBeingDisplayed = "";
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        pref = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        boolean isFirstStart = pref.getBoolean(Constants.FIRST_START, true);
        if (isFirstStart) {
            e.putBoolean(Constants.FIRST_START, false);
            e.apply();
            Intent i = new Intent(MainActivity.this, Intro.class);
            startActivity(i);

        } else {
            e.putBoolean(Constants.FIRST_START, false);
            e.apply();
            checkPermissionLocation();
        }

        setContentView(R.layout.activity_main);
        initFragment();
        Constants.typeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        TextView tv_logo = (TextView) findViewById(R.id.tv_eventify);
        tv_logo.setTypeface(Constants.typeface);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void showIntro(View view) {
        Intent i = new Intent(MainActivity.this, Intro.class);
        startActivity(i);
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    private void initFragment() {
        if (pref.getBoolean(Constants.IS_LOGGED_IN, false)) {
            if (pref.getBoolean(Constants.IS_ORGANIZER, false)) {
                Intent i = new Intent(this, OrganizerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
            } else if (pref.contains(Constants.IS_ORGANIZER) && !pref.getBoolean(Constants.IS_ORGANIZER, false)) {
                Intent i = new Intent(this, UserActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
            }
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(com.company.eventify.R.id.fragment_frame, new LoginFragment());
            ft.commit();
        }

    }

    @Override
    public void onBackPressed() {
        Fragment f = getFragmentManager().findFragmentById(R.id.fragment_frame);
        if (f instanceof LoginFragment) {
            if (!exit) {
                showToast("Press back again to exit!", 800);
                // Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_LONG).show();
                exit = true;
            } else {
                this.finish();
            }
        } else {
            Fragment login = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(com.company.eventify.R.id.fragment_frame, login);
            ft.commit();
        }
    }

    public void showToast(String message, int timeInMSecs) {
        if (mToastToShow != null && message == messageBeingDisplayed) {
            Log.d("DEBUG", "Not Showing another Toast, Already Displaying");
            return;
        } else {
            Log.d("DEBUG", "Displaying Toast");
        }
        messageBeingDisplayed = message;
        // Set the toast and duration
        int toastDurationInMilliSeconds = timeInMSecs;
        mToastToShow = Toast.makeText(this, message, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, timeInMSecs /* Tick duration */) {
            public void onTick(long millisUntilFinished) {
                if (mToastToShow != null) {
                    mToastToShow.show();
                }
            }

            public void onFinish() {
                if (mToastToShow != null) {
                    mToastToShow.cancel();
                }
                // Making the Toast null again
                mToastToShow = null;
                // Emptying the message to compare if its the same message being displayed or
                // not
                messageBeingDisplayed = "";
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
        case REQUEST_LOCATION:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(MainActivity.this, "Permission Granted!",
                // Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(MainActivity.this, "Permission Denied!",
                // Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showExplanation(String title, String message, final String permission,
            final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[] { permissionName }, permissionRequestCode);
    }

    public void checkPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 2);
            // ActivityCompat.requestPermissions(this,
            // new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will then pass the result to
        // the login
        // button.
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void insertAllTags(String generatedId) {
        HashMap<String, String[]> categoryTags = new HashMap<String, String[]>();

        categoryTags.put("Festival", getResources().getStringArray(R.array.festival_tags));
        categoryTags.put("Food & Drink", getResources().getStringArray(R.array.foodanddrink_tags));
        categoryTags.put("Disco", getResources().getStringArray(R.array.disco_tags));
        categoryTags.put("Live Music", getResources().getStringArray(R.array.livemusic_tags));
        categoryTags.put("Cinema", getResources().getStringArray(R.array.cinema_tags));
        categoryTags.put("Outdoor", getResources().getStringArray(R.array.outdoor_tags));
        categoryTags.put("Theatre", getResources().getStringArray(R.array.theatre_tags));
        categoryTags.put("Museum", getResources().getStringArray(R.array.museum_tags));

        StringBuilder sb = new StringBuilder();
        for (String category : categoryTags.keySet()) {
            sb.append(category + "=");
            String[] tags = categoryTags.get(category);
            for (int i = 0; i < tags.length; i++) {
                sb.append(tags[i]);
                sb.append(",");
            }
            sb.append(";");
        }

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.SAVE_PREF);
        request.setAccount_id(generatedId);
        request.setTags(sb.toString());
        request.setRangeTime(Constants.DEFAULT_RANGE_TIME);
        request.setRangeDistance(Constants.DEFAULT_RANGE_DISTANCE);

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");

            }
        });

    }
}