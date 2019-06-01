package com.company.eventify.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.RequestInterface;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.company.eventify.R.id.seekbar;

public class UserFragmentSettings extends Fragment implements View.OnClickListener {
    private static UserFragmentSettings instance = null;
    TextView seekBarTimeValue, tv_message;
    SeekBar seekbarTime;
    TextView seekBarDistanceValue;
    SeekBar seekbarDistance;
    int rangeTime = Constants.DEFAULT_RANGE_TIME;
    int rangeDistance = Constants.DEFAULT_RANGE_DISTANCE;
    HashMap<String, boolean[]> selectedTags;
    HashMap<String, String[]> categoryTags;
    private String[] categoryList;
    private TextView btn_logout;
    private TextView btn_change_pass;
    private TextView btn_preference;
    private TextView btn_time;
    private TextView btn_distance, btn_delete_account;
    private EditText et_old_password, et_new_password;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private String category;
    private ProgressBar progress;
    private AlertDialog dialogChangePass;
    private SharedPreferences pref;
    private Switch notification_switch;

    public static UserFragmentSettings newInstance() {
        if (instance == null) {
            instance = new UserFragmentSettings();
        }
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        scrollView = view.findViewById(R.id.main_content);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        notification_switch = view.findViewById(R.id.notification_switch);
        selectedTags = ((UserActivity) this.getActivity()).getSelectedTags();
        categoryTags = ((UserActivity) this.getActivity()).getCategoryTags();

        categoryTags.put("Festival", getResources().getStringArray(R.array.festival_tags));
        categoryTags.put("Food & Drink", getResources().getStringArray(R.array.foodanddrink_tags));
        categoryTags.put("Disco", getResources().getStringArray(R.array.disco_tags));
        categoryTags.put("Live Music", getResources().getStringArray(R.array.livemusic_tags));
        categoryTags.put("Cinema", getResources().getStringArray(R.array.cinema_tags));
        categoryTags.put("Outdoor", getResources().getStringArray(R.array.outdoor_tags));
        categoryTags.put("Theatre", getResources().getStringArray(R.array.theatre_tags));
        categoryTags.put("Museum", getResources().getStringArray(R.array.museum_tags));

        for (String key : categoryTags.keySet()) {
            selectedTags.put(key, new boolean[categoryTags.get(key).length]);
        }

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_change_pass = view.findViewById(R.id.btn_change_pass);
        btn_preference = view.findViewById(R.id.btn_pref);
        btn_time = view.findViewById(R.id.btn_time);
        btn_distance = view.findViewById(R.id.btn_distance);
        btn_delete_account = view.findViewById(R.id.btn_delete_account);

        btn_delete_account.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_change_pass.setOnClickListener(this);
        btn_preference.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_distance.setOnClickListener(this);
        notification_switch.setChecked(pref.getBoolean(Constants.NOTIFICATION_SWITCH, true));
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String s = "Notification enabled!";
                if (!isChecked)
                    s = "Notification disabled!";
                pref.edit().putBoolean(Constants.NOTIFICATION_SWITCH, isChecked).apply();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_pref:
            chooseCategory();
            break;
        case R.id.btn_delete_account:
            deleteAccount();
            break;
        case R.id.btn_time:
            chooseTime();
            break;
        case R.id.btn_distance:
            chooseDistance();
            break;
        case R.id.btn_logout:
            logout();
            break;
        case R.id.btn_change_pass:
            changePass();
            break;

        }
    }

    private void deleteAccount() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogStyle)
                .setTitle("Do you really want to delete your account? ):")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        deleteAccountProcess();

                        SharedPreferences.Editor e = getActivity()
                                .getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE).edit();
                        e.clear();
                        e.putBoolean(Constants.IS_LOGGED_IN, false);
                        e.putBoolean(Constants.FIRST_START, false);
                        e.putBoolean(Constants.GOOGLE_LOG, false);
                        e.apply();

                        LoginManager.getInstance().logOut();

                        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                        startActivity(mainActivity);

                        getActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
        dialog.show();
        dialog.getWindow().setLayout(900, 350);

    }

    private void changePass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

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
                try {
                    ServerResponse resp = response.body();
                    if (resp.getResult().equals(Constants.SUCCESS)) {
                        progress.setVisibility(View.GONE);
                        tv_message.setVisibility(View.GONE);
                        dialogChangePass.dismiss();

                        Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                        progress.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);
                        tv_message.setText(resp.getMessage());

                    }
                } catch (Exception e) {
                    ((UserActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 1000, getActivity());
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

    private void logout() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogStyle).setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean notification = pref.getBoolean(Constants.NOTIFICATION_SWITCH, true);
                        SharedPreferences.Editor e = getActivity()
                                .getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE).edit();
                        e.clear();
                        e.putBoolean(Constants.IS_LOGGED_IN, false);
                        e.putBoolean(Constants.FIRST_START, false);
                        e.putBoolean(Constants.GOOGLE_LOG, false);
                        e.putBoolean(Constants.NOTIFICATION_SWITCH, notification);
                        e.apply();
                        LoginManager.getInstance().logOut();

                        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
                        startActivity(mainActivity);
                        getActivity().overridePendingTransition(R.xml.from_middle, R.xml.to_middle);

                        getActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
        dialog.show();
        dialog.getWindow().setLayout(900, 300);

    }

    private void chooseCategory() {
        Dialog dialog = onCreateCategoryChoice();
        dialog.show();
    }

    private void chooseTime() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogStyle).setTitle("Show event until")
                .setView(R.layout.dialog1).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        rangeTime = seekbarTime.getProgress();
                        savePref();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();

        dialog.show();

        seekbarTime = (SeekBar) dialog.findViewById(seekbar);

        seekBarTimeValue = (TextView) dialog.findViewById(R.id.label_time);
        seekbarTime.setProgress(rangeTime);
        upgradeLabelTime(rangeTime);

        seekbarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upgradeLabelTime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        }

        );

    }

    private void upgradeLabelTime(int progress) {
        switch (progress) {
        case 0:
            seekBarTimeValue.setText("1 Day");
            break;
        case 1:
            seekBarTimeValue.setText("3 Days");
            break;
        case 2:
            seekBarTimeValue.setText("1 Week");
            break;
        case 3:
            seekBarTimeValue.setText("2 Weeks");
            break;
        case 4:
            seekBarTimeValue.setText("1 Month");
            break;
        case 5:
            seekBarTimeValue.setText("2 Months");
            break;
        }
    }

    private void upgradeLabelDistance(int progress) {
        switch (progress) {
        case 0:
            seekBarDistanceValue.setText("1 Km");
            break;
        case 1:
            seekBarDistanceValue.setText("2 Km");
            break;
        case 2:
            seekBarDistanceValue.setText("10 Km");
            break;
        case 3:
            seekBarDistanceValue.setText("25 Km");
            break;
        case 4:
            seekBarDistanceValue.setText("50 Km");
            break;
        case 5:
            seekBarDistanceValue.setText("100 Km");
            break;
        }
    }

    private void chooseDistance() {
        AlertDialog dialog2 = new AlertDialog.Builder(getActivity(), R.style.DialogStyle).setTitle("Show event in")
                .setView(R.layout.dialog1).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        rangeDistance = seekbarDistance.getProgress();
                        savePref();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();

        dialog2.show();

        seekBarDistanceValue = (TextView) dialog2.findViewById(R.id.label_time);

        seekbarDistance = (SeekBar) dialog2.findViewById(seekbar);
        Drawable thumb = getResources().getDrawable(R.drawable.ic_location_on_white_24dp);
        seekbarDistance.setThumb(thumb);

        seekbarDistance.setProgress(rangeDistance);
        upgradeLabelDistance(rangeDistance);

        seekbarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upgradeLabelDistance(seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        }

        );

    }

    public Dialog onCreateCategoryChoice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        categoryList = getResources().getStringArray(R.array.category_list);
        builder.setTitle("Select category");
        builder.setItems(categoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category = categoryList[which];
                chooseTags();

            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                savePref();
            }
        });
        builder.setNeutralButton("Reset all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                resetAllFilter();
                savePref();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    private void savePref() {
        StringBuilder sb = new StringBuilder();
        for (String category : categoryTags.keySet()) {
            sb.append(category + "=");
            String[] tags = categoryTags.get(category);
            for (int i = 0; i < tags.length; i++) {
                if (selectedTags.get(category)[i]) {
                    sb.append(tags[i]);
                    sb.append(",");
                }

            }
            sb.append(";");
        }

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.SAVE_PREF);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setTags(sb.toString());
        request.setRangeTime(rangeTime);
        request.setRangeDistance(rangeDistance);

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

    public Dialog chooseTags() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);

        builder.setTitle("Select tags").setMultiChoiceItems(categoryTags.get(category), selectedTags.get(category),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                }).setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePref();
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        chooseCategory();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] tags = selectedTags.get(category);
                for (int i = 0; i < tags.length; i++) {
                    tags[i] = false;
                    dialog.getListView().setItemChecked(i, false);
                }
            }
        });

        return dialog;
    }

    public void getPref() {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.GET_PREF);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    if (resp.getMessage().equals("Preferences downloaded!")) {
                        rangeTime = resp.getRangeTime();
                        rangeDistance = resp.getRangeDistance();

                        String tags = resp.getTags();
                        if (tags != null) {
                            StringTokenizer st = new StringTokenizer(tags, ";");
                            while (st.hasMoreTokens()) {
                                String line = st.nextToken();
                                String category = line.split("=")[0];
                                String tag = line.split("=")[1];
                                String[] allTagsPerCategory = categoryTags.get(category);
                                int index = 0;
                                for (int i = 0; i < allTagsPerCategory.length; i++) {
                                    if (allTagsPerCategory[i].equals(tag)) {
                                        index = i;
                                        break;
                                    }
                                }
                                boolean[] temp = selectedTags.get(category);
                                temp[index] = true;
                                selectedTags.put(category, temp);
                            }
                        }
                    }
                } catch (Exception e) {
                    ((UserActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 1000, getActivity());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "failed");

            }
        });
    }

    private void resetAllFilter() {
        for (String category : selectedTags.keySet()) {
            boolean[] tags = selectedTags.get(category);
            for (int i = 0; i < tags.length; i++) {
                tags[i] = false;
            }

        }
    }

}
