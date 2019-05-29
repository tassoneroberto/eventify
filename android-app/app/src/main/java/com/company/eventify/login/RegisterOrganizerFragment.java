package com.company.eventify.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.company.eventify.MainActivity;
import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.organizer.OrganizerActivity;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.RequestInterface;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class RegisterOrganizerFragment extends Fragment implements View.OnClickListener {

    String location;
    private AppCompatButton btn_register;
    private EditText et_email, et_password, et_repeat_password, et_organizer_name, et_address, et_phone;
    private TextView tv_login;
    private ProgressBar progress;
    private SharedPreferences pref;
    private String latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_register_organizer, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        btn_register = view.findViewById(R.id.btn_register);
        tv_login = view.findViewById(R.id.tv_login);
        et_organizer_name = view.findViewById(R.id.et_organizer_name);
        et_address = view.findViewById(R.id.et_address);
        et_phone = view.findViewById(R.id.et_mobile);
        et_password = view.findViewById(R.id.et_password);
        et_repeat_password = view.findViewById(R.id.et_repeat_password);
        progress = view.findViewById(R.id.progress);
        et_email = view.findViewById(R.id.et_email);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        et_address.setKeyListener(null);
        et_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case com.company.eventify.R.id.tv_login:
            goToLogin();
            break;

        case com.company.eventify.R.id.btn_register:

            String organizerName = et_organizer_name.getText().toString();
            String phone = et_phone.getText().toString();
            String address = et_address.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            if (!organizerName.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !email.isEmpty()
                    && !password.isEmpty()) {
                if (et_password.getText().toString().equals(et_repeat_password.getText().toString())) {
                    progress.setVisibility(View.VISIBLE);
                    registerProcess(organizerName, phone, address, email, password);
                } else {
                    Snackbar.make(getView(), "Password doesn't match!", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
            }
            break;
        case (R.id.et_address):
            findPlace(v);
            break;
        }

    }

    private void registerProcess(final String name, final String phone, String address, final String email,
            String password) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        location = address;

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_ORGANIZER);
        request.setEmail(email);
        request.setPassword(password);
        request.setOrganizer_name(name);
        request.setPhone(phone);
        request.setLocation(address);
        request.setLatitude(latitude);
        request.setLongitude(longitude);

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    progress.setVisibility(View.GONE);
                    if (resp.getMessage().equals("Registered Successfully !")) {
                        Snackbar.make(getView(), "Registered Successfully !", Snackbar.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                        editor.putString(Constants.LOCATION, location);
                        editor.putString(Constants.NAME, name);
                        editor.putString(Constants.EMAIL, email);

                        editor.putString(Constants.LATITUDE, latitude);
                        editor.putString(Constants.LONGITUDE, longitude);

                        editor.putString(Constants.PHONE, phone);

                        editor.apply();
                        goToOrganizerProfile();
                    } else {
                        Snackbar.make(getView(), "Registered Failed !", Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    ((MainActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 8000);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.GONE);
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), "Registered Failed !", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void goToLogin() {

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(com.company.eventify.R.id.fragment_frame, login);
        ft.commit();
    }

    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                latitude = "" + place.getLatLng().latitude;
                longitude = "" + place.getLatLng().longitude;

                StringBuilder s = new StringBuilder();
                if (!place.getAddress().equals(""))
                    s.append(place.getAddress());
                et_address.setText(s);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e("Tag", status.getStatusMessage());
            }
        }
    }

    private void goToOrganizerProfile() {

        Intent organizerHome = new Intent(getActivity(), OrganizerActivity.class);
        startActivity(organizerHome);
        getActivity().finish();
    }
}
