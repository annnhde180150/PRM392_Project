package com.example.homehelperfinder.data.remote.service;

import android.content.Context;

import com.example.homehelperfinder.data.model.response.ProfileResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceApiService extends BaseApiService {

    private final ServiceApiInterface apiInterface;

    public ServiceApiService() {
        super();
        this.apiInterface = RetrofitClient.getServiceApiService();
    }

    public CompletableFuture<List<ServiceResponse>> getActiveServices(Context context) {
        return executeCall(context, apiInterface.getActiveServices(), "getActiveServices");
    }

    public void getActiveServices(Context context, ApiCallback<List<ServiceResponse>> callback) {
        handleApiResponse(context, getActiveServices(context), callback);
    }

}
