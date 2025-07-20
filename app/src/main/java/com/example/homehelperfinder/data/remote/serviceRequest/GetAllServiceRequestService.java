package com.example.homehelperfinder.data.remote.serviceRequest;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.ServiceRequestUpdateStatusRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.NetworkUtils;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllServiceRequestService extends BaseApiService {
    private GetAllServiceRequestInterface apiInterface;
    private Context context;
    public GetAllServiceRequestService(Context context) {
        super();
        this.context = context.getApplicationContext();
        RetrofitClient.init(this.context);
        this.apiInterface = RetrofitClient.getAllServiceRequestApiService();
    }
    public void getAllBookingsByHelperId(Context context, int helperId, BaseApiService.ApiCallback<List<GetAllServiceRequestResponse>> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<ApiResponse<List<GetAllServiceRequestResponse>>> call = apiInterface.getAllBookingsByHelperId(helperId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetAllServiceRequestResponse>>> call, Response<ApiResponse<List<GetAllServiceRequestResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<GetAllServiceRequestResponse>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Dữ liệu rỗng", null);
                    }
                } else {
                    callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetAllServiceRequestResponse>>> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    }

    public void updateRequestStatus(Context context, ServiceRequestUpdateStatusRequest request, BaseApiService.ApiCallback<Void> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<ApiResponse<Void>> call = apiInterface.updateRequestStatus(request);
        call.enqueue(new Callback<>(){

            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Lỗi kết nối API", t);
            }
        });
    };
}