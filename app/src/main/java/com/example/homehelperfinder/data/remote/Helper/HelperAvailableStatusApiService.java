package com.example.homehelperfinder.data.remote.Helper;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.GetPaymentRequest;
import com.example.homehelperfinder.data.model.request.HelperAvailableRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.GetPaymentResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperAvailableStatusApiService extends BaseApiService {
    private HelperAvailableStatusApiInterface apiInterface;
    private Context context;

    public HelperAvailableStatusApiService(Context context) {
        super();
        this.context = context.getApplicationContext();
        RetrofitClient.init(this.context);
        this.apiInterface = RetrofitClient.getHelperAvailableStatusApiService();
    }

    public void onlineRequest(Context context, HelperAvailableRequest helperAvailableRequest , ApiCallback<Void> callback) {
        Call<Void> call = apiInterface.onlineRequest(helperAvailableRequest.getUserId());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    }
    public void offlineRequest(Context context, HelperAvailableRequest helperAvailableRequest , ApiCallback<Void> callback) {
        Call<Void> call = apiInterface.OfflineRequest(helperAvailableRequest.getUserId());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    }
    public void busyRequest(Context context, HelperAvailableRequest helperAvailableRequest , ApiCallback<Void> callback) {
        Call<Void> call = apiInterface.BusyRequest(helperAvailableRequest.getUserId());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    }
}
