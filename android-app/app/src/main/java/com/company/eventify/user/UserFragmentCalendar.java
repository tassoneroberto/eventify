package com.company.eventify.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.Event;
import com.company.eventify.utilities.EventAdapter;
import com.company.eventify.utilities.MyAdapter;
import com.company.eventify.utilities.RequestInterface;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFragmentCalendar extends Fragment {
    private static UserFragmentCalendar instance = null;
    private ArrayList<Event> eventList;
    private EventAdapter ca;
    private RecyclerView recList;
    private SharedPreferences pref;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyAdapter myAdapter;

    public static UserFragmentCalendar newInstance() {
        if (instance == null) {
            instance = new UserFragmentCalendar();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recList = view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        eventList = new ArrayList<Event>();
        myAdapter = new MyAdapter();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCalendarEvents();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

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
                try {
                    ServerResponse resp = response.body();
                    if (resp.getResult().equals(Constants.SUCCESS)) {
                        eventList = resp.getEvents();
                        Collections.sort(eventList);
                        ca = new EventAdapter(eventList, (UserActivity) getActivity(), "calendar");
                        recList.setAdapter(ca);
                        Constants.events = new ArrayList<Event>(eventList);
                    } else {
                        recList.setAdapter(myAdapter);
                    }
                } catch (Exception e) {
                    ((UserActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 800, getActivity());
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), Constants.NO_NETWORK_ERROR, Snackbar.LENGTH_LONG).show();
                recList.setAdapter(myAdapter);
            }
        });
    }

    public void refreshCalendar() {
        if (eventList.isEmpty()) {
            recList.setAdapter(myAdapter);
        } else {
            ca = new EventAdapter(eventList, (UserActivity) getActivity(), "calendar");
            recList.setAdapter(ca);
        }

    }

}
