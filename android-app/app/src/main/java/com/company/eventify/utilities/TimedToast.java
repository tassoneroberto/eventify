package com.company.eventify.utilities;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by simone on 06/04/2017.
 */

public class TimedToast {

    private Toast mToastToShow;
    private String messageBeingDisplayed;

    public TimedToast(Toast toast, String messageBeingDisplayed) {
        this.mToastToShow = toast;
        this.messageBeingDisplayed = messageBeingDisplayed;
    }

    /**
     * Show Toast message for a specific duration, does not show again if the
     * message is same
     *
     * @param message     The Message to display in toast
     * @param timeInMSecs Time in ms to show the toast
     */
    public void showToast(String message, int timeInMSecs, Activity a) {
        if (mToastToShow != null && message == messageBeingDisplayed) {
            Log.d("DEBUG", "Not Showing another Toast, Already Displaying");
            return;
        } else {
            Log.d("DEBUG", "Displaying Toast");
        }
        messageBeingDisplayed = message;
        // Set the toast and duration
        int toastDurationInMilliSeconds = timeInMSecs;
        mToastToShow = Toast.makeText(a, message, Toast.LENGTH_LONG);

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
}
