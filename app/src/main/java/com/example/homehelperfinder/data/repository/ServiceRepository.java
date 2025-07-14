package com.example.homehelperfinder.data.repository;

import android.content.Context;

import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.service.ServiceApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceRepository {
    private final ServiceApiService serviceApiService;

    public ServiceRepository() {
        this.serviceApiService = new ServiceApiService();
    }

    // CompletableFuture methods
    public CompletableFuture<List<ServiceResponse>> getActiveServices(Context context) {
        return serviceApiService.getActiveServices(context);
    }

    // Callback methods for easier usage
    public void getActiveServices(Context context, BaseApiService.ApiCallback<List<ServiceResponse>> callback) {
        serviceApiService.getActiveServices(context, callback);
    }
}
