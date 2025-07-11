package com.example.homehelperfinder.data.repository;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.authentication.AuthenticationApiService;

import java.util.concurrent.CompletableFuture;

public class AuthenticationRepository {

    private final AuthenticationApiService authenticationApiService;

    public AuthenticationRepository() {
        this.authenticationApiService = new AuthenticationApiService();
    }

    public CompletableFuture<HelperResponse> registerHelper(Context context, HelperRequest request) {
        return authenticationApiService.registerHelper(context, request);
    }
    public void registerHelper(Context context, HelperRequest request, BaseApiService.ApiCallback<HelperResponse> callback) {
        authenticationApiService.registerHelper(context, request, callback);
    }
}
