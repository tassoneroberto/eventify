package com.company.eventify.utilities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import com.company.eventify.R;
import com.company.eventify.user.UserActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Calendar;
import java.util.StringTokenizer;

public class UserInteraction {

    public static void dialogHome(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.DialogStyle).setTitle("Information")
                .setMessage("No events found. Check your application settings!")
                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((UserActivity) activity).onPageSelected(4);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

    public static void dialogEnablePosition(final Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog dialog = new AlertDialog.Builder(activity, R.style.DialogStyle).setTitle("Information")
                    .setMessage("Seems that you haven't given permission to access the location provider!")
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            activity.startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(activity, R.style.DialogStyle).setTitle("Information")
                    .setMessage("Seems that your GPS is off! Enable it please !")
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

    }

    public static void dialogNear(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.DialogStyle).setTitle("Information")
                .setMessage("Ops! There are no events in your area. Try increasing the distance for the search!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
        dialog.show();
    }

    public static void sendCall(final Activity activity, String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        activity.startActivity(callIntent);
    }

    public static void getNotificationCalendar(final Activity activity, Event e) {
        StringTokenizer st = new StringTokenizer(e.getOpening());
        int year = Integer.parseInt(st.nextToken("-"));
        int month = Integer.parseInt(st.nextToken("-")) - 1;
        int day = Integer.parseInt(st.nextToken(" ").substring(1, 3));
        String time = st.nextToken(" ");
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        st = new StringTokenizer(e.getEnding());
        year = Integer.parseInt(st.nextToken("-"));
        month = Integer.parseInt(st.nextToken("-")) - 1;
        day = Integer.parseInt(st.nextToken(" ").substring(1, 3));
        time = st.nextToken(" ");
        hour = Integer.parseInt(time.substring(0, 2));
        minute = Integer.parseInt(time.substring(3, 5));
        cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        intent.putExtra("allDay", false);
        intent.putExtra("endTime", cal.getTimeInMillis());
        intent.putExtra("title", e.getTitle());
        intent.putExtra("eventLocation", e.getLocation());
        intent.putExtra("description", e.getDescription() + "\n\nPhone: " + e.getPhone());
        activity.startActivity(intent);

    }

    public static void openMap(final Activity activity, Event e) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + e.getLatitude() + "," + e.getLongitude() + " ("
                + e.getLocation() + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        activity.startActivity(intent);
    }

    public static void shareOnFB(final Activity activity, CallbackManager callbackManager, String eventTitle,
            String eventDescription, String eventImageUrl) {
        ShareDialog shareDialog = new ShareDialog(activity);
        FacebookCallback<Sharer.Result> shareCallBack = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        shareDialog.registerCallback(callbackManager, shareCallBack);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://eventifyserver.altervista.org/index.html")).setContentTitle(eventTitle)
                .setContentDescription(eventDescription).setImageUrl(Uri.parse(eventImageUrl)).build();
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }
}
