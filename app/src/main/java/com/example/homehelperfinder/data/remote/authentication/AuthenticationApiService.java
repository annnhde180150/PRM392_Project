package com.example.homehelperfinder.data.remote.authentication;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.ProfileResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuthenticationApiService extends BaseApiService {

    private final AuthenticationApiInterface apiInterface;

    public AuthenticationApiService() {
        super();
        this.apiInterface = RetrofitClient.getAuthenticationApiService();
    }

    public CompletableFuture<HelperResponse> registerHelper(Context context, HelperRequest request) {
        return executeCall(context, apiInterface.registerHelper(request), "registerHelper");
    }

    public void registerHelper(Context context, HelperRequest request , ApiCallback<HelperResponse> callback) {
        handleApiResponse(context, registerHelper(context, request), callback);
    }
}
