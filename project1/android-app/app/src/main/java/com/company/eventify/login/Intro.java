package com.company.eventify.login;

import android.Manifest;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.github.paolorotolo.appintro.AppIntro2;

public class Intro extends AppIntro2 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
        // Manifest.permission.CALL_PHONE}, 1); // OR
        IntroFragment f0 = new IntroFragment();
        f0.init(R.drawable.a, "Hi, you're into Eventify!", "Check out events and share them with you friends");
        addSlide(f0);

        IntroFragment f2 = new IntroFragment();
        f2.init(R.drawable.b, "What happens around you?", "Discover events in your place.");
        addSlide(f2);

        IntroFragment f1 = new IntroFragment();
        f1.init(R.drawable.e, "Spread the word!", "Register as an organizer to create and manage events.");
        addSlide(f1);

        IntroFragment f4 = new IntroFragment();
        f4.init(R.drawable.c, "Personalize your world", "Choose what to see according to your liking.");
        addSlide(f4);

        IntroFragment f3 = new IntroFragment();
        f3.init(R.drawable.d, "Keep an eye your events", "Use your calendar to add liked events.");
        addSlide(f3);

        IntroFragment f5 = new IntroFragment();
        f5.init(R.drawable.f, "Here we are!", "Get started!");
        addSlide(f5);

        showStatusBar(false);
    }

    @Override
    public void onSkipPressed() {

        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE }, 1);
        finish();
    }

    @Override
    public void onDonePressed() {
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE }, 1);
        finish();
    }

}