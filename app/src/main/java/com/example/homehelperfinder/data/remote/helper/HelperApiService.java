package com.example.homehelperfinder.data.remote.helper;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.HelperViewIncomeResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.NetworkUtils;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperApiService extends BaseApiService{
    private final HelperApiInterface apiInterface;

    public HelperApiService() {
        this.apiInterface = RetrofitClient.getHelperApiService();
    }




    /**
     * View helper income by ID
     */
    public void getIncome(Context context, int helperId, BaseApiService.ApiCallback<HelperViewIncomeResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<ApiResponse<HelperViewIncomeResponse>> call = apiInterface.viewHelperIncome(helperId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<HelperViewIncomeResponse>> call, Response<ApiResponse<HelperViewIncomeResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HelperViewIncomeResponse>> call, Throwable t) {
                callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
            }
        });
    }
}
