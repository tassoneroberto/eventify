package com.company.eventify.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.company.eventify.utilities.UserInteraction;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFragmentNear extends Fragment {
    private static UserFragmentNear instance;
    ArrayList<Event> eventList;
    EventAdapter ca;
    RecyclerView recList;
    private SharedPreferences pref;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyAdapter myAdapter;

    public static UserFragmentNear newInstance() {
        if (instance == null) {
            instance = new UserFragmentNear();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recList = view.findViewById(R.id.cardList);
        eventList = new ArrayList<Event>();
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        myAdapter = new MyAdapter();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getNearEvents();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getNearEvents() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.GET_NEAR_EVENTS);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setLatitude("" + ((UserActivity) getActivity()).getLatitude());
        request.setLongitude("" + ((UserActivity) getActivity()).getLongitude());

        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                try {
                    ServerResponse resp = response.body();
                    if (resp.getResult().equals(Constants.SUCCESS)) {
                        eventList = resp.getEvents();
                        Collections.sort(eventList);
                        ca = new EventAdapter(eventList, (UserActivity) getActivity(), "near");
                        recList.setAdapter(ca);

                    } else if (!((UserActivity) getActivity()).getProviderEnabled()) {
                        recList.setAdapter(myAdapter);
                        UserInteraction.dialogEnablePosition(getActivity());
                    } else {
                        recList.setAdapter(myAdapter);
                        UserInteraction.dialogNear(getActivity());
                    }
                } catch (Exception e) {
                    ((UserActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 1000, getActivity());
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

}
