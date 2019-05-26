package com.company.eventify.utilities;

import com.company.eventify.models.ServerRequest;
import com.company.eventify.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("API/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}