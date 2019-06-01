package com.company.eventify.organizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.Event;
import com.company.eventify.utilities.EventAdapter;
import com.company.eventify.utilities.MyAdapter;
import com.company.eventify.utilities.RequestInterface;
import com.company.eventify.utilities.TimedToast;
import com.company.eventify.utilities.UserInteraction;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizerActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    public Button shareButton;
    public FacebookCallback<Sharer.Result> shareCallBack;
    private FloatingActionButton add;
    private List<Event> myEvents;
    private RecyclerView recList;
    private EventAdapter eventAdapter;
    private CallbackManager callbackManager;
    private SharedPreferences pref;
    private CoordinatorLayout mainLayout;
    private ProgressBar progress;
    private AlertDialog dialogChangePass;
    private Toolbar toolbar;

    private TextView tv_message;
    private EditText et_old_password, et_new_password;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean exit;
    private Animation bounce;
    private Animation rotate;
    private String eventTitle;
    private String eventDescription;
    private String eventImageUrl;
    private TimedToast toast;

    @Override
    public void onResume() {
        super.onResume();
        getOwnedEvents();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_organizer);
        toast = new TimedToast(null, "");
        shareCallBack = new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("SUCCESS");
            }

            @Override
            public void onCancel() {
                System.out.println("cancel");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("erorr");
            }
        };
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage event");
        bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        bounce.setAnimationListener(this);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        rotate.setAnimationListener(this);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        View header = nvDrawer.getHeaderView(0);
        TextView organizer_name_navbar = header.findViewById(R.id.organizer_name_navbar);
        TextView organizer_email_navbar = header.findViewById(R.id.organizer_email_navbar);
        pref = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recList.setLayoutManager(llm);
        add = (FloatingActionButton) findViewById(R.id.add_btn);
        add.setOnClickListener(this);
        getOwnedEvents();
        add.startAnimation(rotate);
        MyAdapter eventAdapter = new MyAdapter();
        recList.setAdapter(eventAdapter);
        organizer_name_navbar.setText(pref.getString(Constants.NAME, ""));
        organizer_email_navbar.setText(pref.getString(Constants.EMAIL, ""));
        recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && add.isShown())
                    add.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    add.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getOwnedEvents();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareButton = (Button) findViewById(R.id.fbk_share_btn);
        shareButton.setVisibility(View.GONE);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInteraction.shareOnFB(OrganizerActivity.this, callbackManager, eventTitle, eventDescription,
                        eventImageUrl);
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
        case R.id.share_all:
            shareAll();
            break;
        case R.id.logout:
            logout();
            break;
        case R.id.addeventmenu:
            Intent createEvent = new Intent(this, OrganizerCreateEventActivity.class);
            startActivity(createEvent);
            overridePendingTransition(R.xml.from_middle, R.xml.to_middle);
            break;
        case R.id.change_pass:
            changePass();
            break;
        case R.id.delete_account:
            deleteAccount();
            break;
        }
    }

    private void logout() {

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle).setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor e = getSharedPreferences(Constants.PREF, MODE_PRIVATE).edit();
                        e.clear();
                        e.putBoolean("firstStart", false);
                        e.putBoolean(Constants.IS_LOGGED_IN, false);
                        e.putBoolean(Constants.GOOGLE_LOG, false);

                        e.apply();

                        LoginManager.getInstance().logOut();

                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                        overridePendingTransition(R.xml.from_middle, R.xml.to_middle);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
        dialog.show();
        dialog.getWindow().setLayout(900, 300);

    }

    private void deleteAccount() {

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setTitle("Do you really want to delete your account? ):")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        deleteAccountProcess();

                        SharedPreferences.Editor e = getSharedPreferences(Constants.PREF, MODE_PRIVATE).edit();
                        e.clear();
                        e.putBoolean(Constants.FIRST_START, false);
                        e.putBoolean(Constants.IS_LOGGED_IN, false);
                        e.putBoolean(Constants.GOOGLE_LOG, false);

                        e.apply();

                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
        dialog.show();
        dialog.getWindow().setLayout(900, 350);

    }

    @Override
    public void onClick(View v) {
        exit = false;
        switch (v.getId()) {
        case R.id.add_btn:
            Intent createEvent = new Intent(this, OrganizerCreateEventActivity.class);
            startActivity(createEvent);
            overridePendingTransition(R.xml.from_middle, R.xml.to_middle);
            break;
        }

    }

    public void editEvent(Event e) {
        Intent editEvent = new Intent(this, OrganizerEditEventActivity.class);
        editEvent.putExtra("Event", e);
        startActivity(editEvent);
        overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

    }

    public void deleteEvent(String event_id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REMOVE_EVENT);
        request.setEvent_id(event_id);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Snackbar.make(mainLayout, resp.getMessage(), Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");
            }
        });

    }

    private void getOwnedEvents() {
        myEvents = new ArrayList<Event>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.GET_OWNED_EVENTS);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if (resp.getMessage().equals("Got Owned Events")) {
                    myEvents = resp.getEvents();
                    eventAdapter = new EventAdapter(myEvents, instance());
                    recList.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");
            }
        });
    }

    private OrganizerActivity instance() {
        return this;
    }

    private void changePass() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = view.findViewById(R.id.et_old_password);
        et_new_password = view.findViewById(R.id.et_new_password);
        tv_message = view.findViewById(R.id.tv_message);
        progress = view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogChangePass = builder.create();
        dialogChangePass.show();
        dialogChangePass.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(pref.getString(Constants.EMAIL, ""), old_password, new_password);

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }

        });
    }

    private void changePasswordProcess(String email, String old_password, String new_password) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD);
        request.setEmail(email);
        request.setOld_password(old_password);
        request.setNew_password(new_password);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if (resp.getResult().equals(Constants.SUCCESS)) {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialogChangePass.dismiss();
                    Snackbar.make(mainLayout, resp.getMessage(), Snackbar.LENGTH_LONG).show();

                } else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (!exit) {
            exit = true;
            toast.showToast("Go to settings to logout!\n  Press again to close", 1000, this);
        } else {
            finish();
        }

    }

    private void deleteAccountProcess() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.DELETE_ACCOUNT);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");

            }
        });
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.share:
            shareAll();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareAll() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        // sharingIntent.setType("application/octet-stream") ;
        String shareBodyText = "";
        for (Event e : myEvents) {
            String event = e.getTitle() + "\n" + e.getLocation() + "\nOpening: " + e.getOpening() + "\nEnding: "
                    + e.getEnding() + "\n" + e.getDescription();
            shareBodyText += event + "\n";
            shareBodyText += "https://play.google.com/store/apps/details?id=com.mind.eventifyApp&hl=en";
            shareBodyText += "\n";
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
    }

    public void setEvent(String title, String description, String UrlImage) {
        eventTitle = title;
        eventDescription = description;
        eventImageUrl = UrlImage;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
