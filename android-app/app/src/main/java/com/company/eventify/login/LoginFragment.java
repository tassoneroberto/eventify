package com.company.eventify.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.organizer.OrganizerActivity;
import com.company.eventify.user.UserActivity;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.MyBounceInterpolator;
import com.company.eventify.utilities.RequestInterface;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.TwitterButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment
        implements View.OnClickListener, View.OnLongClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    String returnedId;
    CallbackManager callbackManager;
    String email;
    String firstname;
    String lastname;
    TwitterSession session;
    private Button btn_login;
    private EditText et_email, et_password;
    private Button btn_register_user, btn_register_organizer;
    private LoginButton login_face;
    private FacebookButton face;
    private TwitterButton twitter;
    private SharedPreferences pref;
    private boolean click = false;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private TwitterLoginButton loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {

        btn_login = view.findViewById(R.id.btn_login);

        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        btn_register_user = view.findViewById(R.id.btn_register_user);
        btn_register_organizer = view.findViewById(R.id.btn_register_organizer);

        btn_login.setOnClickListener(this);
        btn_register_user.setOnClickListener(this);
        btn_register_organizer.setOnClickListener(this);

        btn_register_user.setOnLongClickListener(this);
        btn_register_organizer.setOnLongClickListener(this);

        et_email.setText(pref.getString(Constants.EMAIL, ""));

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        login_face = view.findViewById(R.id.face);
        face = view.findViewById(R.id.login_face);
        login_face.setVisibility(View.GONE);
        face.setOnClickListener(this);
        twitter = view.findViewById(R.id.login_twitter);
        twitter.setOnClickListener(this);
        gso = ((MainActivity) getActivity()).getGso();
        mGoogleApiClient = ((MainActivity) getActivity()).getmGoogleApiClient();
        loginButton = view.findViewById(R.id.twitterLogin);
        loginButton.setVisibility(View.GONE);
        loginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;
                firstname = session.getUserName();
                lastname = session.getUserName();
                TwitterSession sessionClient = Twitter.getSessionManager().getActiveSession();

                final TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(sessionClient, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        email = result.data;
                        checkAndRegisterUser();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("TwitterKit", "email error", exception);
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                session = Twitter.getInstance().core.getSessionManager().getActiveSession();

            }

        });

        final Profile fbProfile = Profile.getCurrentProfile();
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    LoginManager.getInstance().logOut();
                }
            }
        };
        if (fbProfile == null) {
            callbackManager = CallbackManager.Factory.create();

            login_face.setReadPermissions(Arrays.asList("public_profile", "email"));
            login_face.setFragment(this);
            login_face.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    FacebookSdk.setIsDebugEnabled(true);
                                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

                                    Profile profile = Profile.getCurrentProfile();

                                    if (profile != null) {
                                        firstname = profile.getFirstName();
                                        lastname = profile.getLastName();
                                        try {
                                            email = object.getString("email");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        checkAndRegisterUser();

                                    } else {
                                        Snackbar.make(getView(), Constants.FACEBOOK_LOGIN_ERROR, Snackbar.LENGTH_LONG)
                                                .show();
                                        LoginManager.getInstance().logOut();
                                        login_face.callOnClick();
                                        // return;
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getActivity(), "Login with Facebook error!", Toast.LENGTH_LONG).show();

                }

            });
        } else {
            Intent ua = new Intent(getActivity(), UserActivity.class);
            startActivity(ua);
        }
        accessTokenTracker.startTracking();
    }

    @Override
    public void onClick(View v) {
        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        ((MainActivity) getActivity()).setExit(false);
        switch (v.getId()) {
        case R.id.btn_register_user:
            goToRegisterUser();
            break;
        case R.id.btn_register_organizer:
            goToRegisterOrganizer();
            break;

        case R.id.login_face:
            login_face.callOnClick();
            break;
        case R.id.login_twitter:
            loginButton.performClick();
            break;

        case R.id.btn_login:

            if (!click) {
                et_email.setEnabled(false);
                et_password.setEnabled(false);
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    click = true;
                    loginProcess(email, password);

                } else {
                    btn_login.startAnimation(myAnim);

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                et_email.setEnabled(true);
                et_password.setEnabled(true);

            } else {
                click = false;
            }

            break;

        }
    }

    private void loginProcess(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setEmail(email);
        request.setPassword(password);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                try {
                    ServerResponse resp = response.body();
                    SharedPreferences.Editor editor = pref.edit();
                    if (resp.getResult().equals(Constants.SUCCESS)) {
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);
                        editor.apply();

                        if (!resp.is_organizer()) {
                            editor.putBoolean(Constants.IS_ORGANIZER, false);
                            editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                            editor.putString(Constants.EMAIL, resp.getEmail());
                            editor.putString(Constants.FIRSTNAME, resp.getFirstname());
                            editor.putString(Constants.LASTNAME, resp.getLastname());
                            editor.apply();

                            goToUserProfile();
                        } else if (resp.is_organizer()) {
                            editor.putBoolean(Constants.IS_ORGANIZER, true);
                            editor.putString(Constants.EMAIL, resp.getEmail());
                            editor.putString(Constants.LOCATION, resp.getLocation());
                            editor.putString(Constants.LATITUDE, resp.getLatitude());
                            editor.putString(Constants.LONGITUDE, resp.getLongitude());
                            editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                            editor.putString(Constants.NAME, resp.getOrganizer_name());
                            editor.putString(Constants.PHONE, resp.getPhone());
                            editor.apply();

                            goToOrganizerProfile();
                        }

                    } else {
                        Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    ((MainActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 800);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");
                ((MainActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 1000);

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);

        boolean isLogged = pref.getBoolean(Constants.GOOGLE_LOG, false);
        if (isLogged) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {

                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {

                        handleSignInResult(googleSignInResult);
                    }
                });
            }
            SharedPreferences.Editor e = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE)
                    .edit();

            e.clear();
            e.putBoolean(Constants.GOOGLE_LOG, true);
            e.apply();
        } else {
            if (mGoogleApiClient.isConnected()) {
                signOut();
            }
        }
    }

    private void checkAndRegisterUser() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.GET_ID);
        request.setEmail(email);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    if (resp.getMessage().equals("Id not exists!")) {
                        registerSocial(firstname, lastname, email,
                                "" + (new Random(new GregorianCalendar().getTimeInMillis())).nextLong());
                    } else {
                        returnedId = resp.getAccount_id();
                    }
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.putBoolean(Constants.IS_ORGANIZER, false);
                    editor.putString(Constants.LOGIN_ID, returnedId);
                    editor.putString(Constants.FIRSTNAME, firstname);
                    editor.putString(Constants.LASTNAME, lastname);
                    editor.apply();
                    if (mGoogleApiClient.isConnected()) {
                        signOut();
                    }
                    goToUserProfile();
                } catch (Exception e) {
                    ((MainActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 8000);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                returnedId = "";
                Log.d(Constants.TAG, "failed");

            }
        });
    }

    private void goToRegisterUser() {

        Fragment register = new RegisterUserFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, register);
        ft.commit();
    }

    private void goToRegisterOrganizer() {

        Fragment register = new RegisterOrganizerFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, register);
        ft.commit();
    }

    private void goToUserProfile() {

        Intent userActivity = new Intent(getActivity(), UserActivity.class);
        userActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(userActivity);
        getActivity().overridePendingTransition(R.xml.from_middle, R.xml.to_middle);
        getActivity().finish();

    }

    private void goToOrganizerProfile() {

        Intent organizerHome = new Intent(getActivity(), OrganizerActivity.class);
        organizerHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
        startActivity(organizerHome);
        getActivity().overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

        getActivity().finish();

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(Constants.TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firstname = acct.getGivenName();
            lastname = acct.getFamilyName();
            email = acct.getEmail();

            checkAndRegisterUser();

        } else {

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

            }
        });
    }

    @Override
    public boolean onLongClick(View view) {
        /*
         * switch (view.getId()) { case R.id.btn_register_user: Intent userActivity =
         * new Intent(getActivity(), UserActivity.class); startActivity(userActivity);
         * break; case R.id.btn_register_organizer: Intent organizerHome = new
         * Intent(getActivity(), OrganizerActivity.class); startActivity(organizerHome);
         * break; } return false;
         */
        return false;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(Constants.TAG, "handleSignInResult:" + requestCode + " " + resultCode + " " + data);

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);

        } else if (TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {

            loginButton.onActivityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void registerSocial(final String firstname, final String lastname, final String email, String password) {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_USER);
        request.setEmail(email);
        request.setPassword(password);
        request.setFirstname(firstname);
        request.setLastname(lastname);

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    if (resp.getMessage().equals("Registered Successfully !")) {
                        ((MainActivity) getActivity()).insertAllTags(resp.getAccount_id());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.EMAIL, email);
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);
                        editor.putBoolean(Constants.IS_ORGANIZER, false);
                        editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                        editor.putString(Constants.FIRSTNAME, firstname);
                        editor.putString(Constants.LASTNAME, lastname);
                        editor.apply();
                        goToUserProfile();
                    }
                } catch (Exception e) {
                    ((MainActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 8000);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class Uniqid {

        private String uniqid(String prefix, boolean more_entropy) {
            long time = System.currentTimeMillis();
            String uniqid;
            if (!more_entropy) {
                uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
            } else {
                SecureRandom sec = new SecureRandom();
                byte[] sbuf = sec.generateSeed(8);
                ByteBuffer bb = ByteBuffer.wrap(sbuf);
                String time2 = "" + time;
                time2 = time2.substring(0, 8);
                long time2long = Long.parseLong(time2);
                uniqid = String.format("%s%08x%05x", prefix, time / 1000, time2long);
                uniqid += "." + String.format("%.8s", "" + bb.getLong() * -1);
            }
            return uniqid;
        }
    }
}
