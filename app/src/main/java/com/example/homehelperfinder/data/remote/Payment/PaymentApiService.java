package com.example.homehelperfinder.data.remote.Payment;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UpdatePaymentRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.request.GetPaymentRequest;
import com.example.homehelperfinder.data.model.response.GetPaymentResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.NetworkUtils;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentApiService extends BaseApiService {
    private PaymentApiInterface apiInterface;
    private Context context;
    public PaymentApiService(Context context) {
        super();
        this.context = context.getApplicationContext();
        RetrofitClient.init(this.context);
        this.apiInterface = RetrofitClient.getPaymentApiService();
    }
    public CompletableFuture<GetPaymentResponse> getPaymentDetails(Context context,GetPaymentRequest getPaymentRequest) {
        CompletableFuture<GetPaymentResponse> future = new CompletableFuture<>();
        return executeCall(context, apiInterface.getPaymentDetails(getPaymentRequest.getUserId(), getPaymentRequest.getBookingId()), "getPaymentDetails");
    }
    public void getPaymentDetails(Context context, GetPaymentRequest getPaymentRequest, ApiCallback<GetPaymentResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<ApiResponse<GetPaymentResponse>> call = apiInterface.getPaymentDetails(getPaymentRequest.getUserId(), getPaymentRequest.getBookingId());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<GetPaymentResponse>> call, Response<ApiResponse<GetPaymentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GetPaymentResponse>> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    }
    public void updatePaymentStatus(Context context, UpdatePaymentRequest updatePaymentRequest, ApiCallback<Void> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<Void> call = apiInterface.updatePaymentStatus(updatePaymentRequest);
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
