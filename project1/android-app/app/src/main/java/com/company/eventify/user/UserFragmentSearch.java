package com.company.eventify.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.company.eventify.R;
import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;
import com.company.eventify.utilities.Constants;
import com.company.eventify.utilities.Event;
import com.company.eventify.utilities.EventAdapter;
import com.company.eventify.utilities.RequestInterface;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFragmentSearch extends Fragment {
    private static UserFragmentSearch instance = null;
    ArrayList<Event> eventList;
    EventAdapter ca;
    RecyclerView recList;
    LinearLayout ll;
    SearchView searchView;
    private SharedPreferences pref;

    public static UserFragmentSearch newInstance() {
        if (instance == null) {
            instance = new UserFragmentSearch();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ll = view.findViewById(R.id.ll);
        recList = view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        eventList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recList.setLayoutManager(llm);
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 3) {
                    searchEvents(query);
                } else {

                    Snackbar.make(ll, Constants.WRONG_SEARCH, Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 3)
                    searchEvents(query);
                else {
                    eventList.clear();
                    recList.setVisibility(View.GONE);
                    recList.removeAllViewsInLayout();
                    recList.setVisibility(View.VISIBLE);
                    if (query.length() != 0) {
                        Snackbar.make(ll, Constants.NO_EVENTS_FOUND, Snackbar.LENGTH_LONG).show();
                    }
                }

                return false;
            }
        });
        searchView.setIconifiedByDefault(false);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void searchEvents(String query) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.SEARCH_EVENTS);
        request.setAccount_id(pref.getString(Constants.LOGIN_ID, ""));
        request.setQuery(query);

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
                    } else {
                        if (eventList != null)
                            eventList.clear();
                        recList.setVisibility(View.GONE);
                        recList.removeAllViewsInLayout();
                        recList.setVisibility(View.VISIBLE);
                        Snackbar.make(ll, Constants.NO_EVENTS_FOUND, Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    ((UserActivity) getActivity()).showToast(Constants.NO_NETWORK_ERROR, 1000, getActivity());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Snackbar.make(ll, Constants.ERROR_SEARCH, Snackbar.LENGTH_LONG).show();
                Log.d(Constants.TAG, Constants.NO_NETWORK_ERROR);
            }
        });
    }

}
