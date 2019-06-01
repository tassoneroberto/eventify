package com.company.eventify.utilities;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simone on 21/03/2017.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private static ArrayList<Event> events;
    private static SharedPreferences pref;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());

    }

    public static Intent createIntentStartNotificationService(Context context) {
        pref = context.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        events = new ArrayList<Event>();
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    // Delete notification by phone
    private void processDeleteNotification(Intent intent) {
        Log.d(getClass().getSimpleName(), "delete notification");
    }

    private void processStartNotification() {
        try {
            events = new ArrayList<>(Constants.events);

            notification();
        } catch (Exception e) {
            getCalendarEvents();

        }

    }

    public void getCalendarEvents() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.GET_CALENDAR_EVENTS);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();

                if (resp.getResult().equals(Constants.SUCCESS)) {
                    events = resp.getEvents();
                    notification();

                    Constants.events = new ArrayList<Event>(events);
                } else {
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "failed");

            }
        });

    }

    // send notification
    private void notification() {
        boolean yesNotification = pref.getBoolean(Constants.NOTIFICATION_SWITCH, true);// check if notification is still
                                                                                       // active
        if (!events.isEmpty() && yesNotification) {

            for (Event e : events) {
                GregorianCalendar gc = new GregorianCalendar();
                int month = gc.get(Calendar.MONTH) + 1;
                String m = "";
                if (month < 10) {
                    m = "0" + month;
                }
                String currentDate = "" + gc.get(Calendar.YEAR) + "-" + m + "-" + gc.get(Calendar.DAY_OF_MONTH);
                Log.d(getClass().getSimpleName(), "currentDate " + currentDate);

                StringTokenizer st = new StringTokenizer(e.getOpening());
                String eventDate = st.nextToken(" ");

                if (currentDate.equals(eventDate)) {
                    Log.d(getClass().getSimpleName(), "event Today");

                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setContentTitle(e.getTitle()).setAutoCancel(true)
                            .setContentText("This event is close!! Check your Calendar ")
                            .setSmallIcon(R.drawable.cast_ic_notification_0);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                            new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

                    final NotificationManager manager = (NotificationManager) this
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, builder.build());
                }
            }

        } else {
            Log.d(getClass().getSimpleName(), "no event");

        }
    }
}