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
import com.company.eventify.user.UserActivity;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.RequestInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUserFragment extends Fragment implements View.OnClickListener {

    private AppCompatButton btn_register;
    private EditText et_email, et_password, et_repeat_password, et_firstname, et_lastname;
    private TextView tv_login;
    private ProgressBar progress;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        btn_register = view.findViewById(R.id.btn_register);
        tv_login = view.findViewById(R.id.tv_login);
        et_firstname = view.findViewById(R.id.et_firstname);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_repeat_password = view.findViewById(R.id.et_repeat_password);
        progress = view.findViewById(R.id.progress);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.tv_login:
            goToLogin();
            break;

        case R.id.btn_register:

            String firstname = et_firstname.getText().toString();
            String lastname = et_lastname.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            if (!firstname.isEmpty() && !lastname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (et_password.getText().toString().equals(et_repeat_password.getText().toString())) {
                    progress.setVisibility(View.VISIBLE);
                    registerProcess(firstname, lastname, email, password);
                } else {
                    Snackbar.make(getView(), "Password doesn't match! [" + et_password.getText() + " / "
                            + et_repeat_password.getText() + "]", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
            }

            break;

        }

    }

    private void registerProcess(final String firstname, final String lastname, final String email, String password) {
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
                    SharedPreferences.Editor editor = pref.edit();
                    progress.setVisibility(View.GONE);
                    if (resp.getMessage().equals("Registered Successfully !")) {
                        editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                        editor.putString(Constants.EMAIL, email);
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);
                        editor.putBoolean(Constants.IS_ORGANIZER, false);
                        editor.putString(Constants.LOGIN_ID, resp.getAccount_id());
                        editor.putString(Constants.FIRSTNAME, firstname);
                        editor.putString(Constants.LASTNAME, lastname);
                        ((MainActivity) getActivity()).insertAllTags(resp.getAccount_id());
                        Snackbar.make(getView(), "Registered Successfully !", Snackbar.LENGTH_LONG).show();
                        editor.apply();
                        goToUserProfile();
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
        ft.replace(R.id.fragment_frame, login);
        ft.commit();
    }

    private void goToUserProfile() {

        Intent userActivity = new Intent(getActivity(), UserActivity.class);
        startActivity(userActivity);
        getActivity().finish();
    }
}
