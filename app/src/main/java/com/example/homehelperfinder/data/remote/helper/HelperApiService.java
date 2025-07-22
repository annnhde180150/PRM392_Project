package com.example.homehelperfinder.data.remote.helper;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.AddMoneytoIncomeRequest;
import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.HelperSearchResponse;
import com.example.homehelperfinder.data.model.response.HelperViewIncomeResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.NetworkUtils;

import java.util.List;
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
     * Search helpers by service
     */
    public CompletableFuture<List<HelperSearchResponse>> searchHelpers(
            Context context, 
            int serviceId,
            int page, 
            int pageSize) {
        return executeCall(context, apiInterface.searchHelpers(serviceId, page, pageSize), "searchHelpers");
    }

    public void searchHelpers(
            Context context, 
            int serviceId,
            int page, 
            int pageSize, 
            ApiCallback<List<HelperSearchResponse>> callback) {
        handleApiResponse(context, searchHelpers(context, serviceId, page, pageSize), callback);
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

    public CompletableFuture<List<ServiceResponse>> getHelperServices(Context context, int helperId) {
        return executeCall(context, apiInterface.getHelperService(helperId), "get helper services by ID");
    }

    public void getHelperServices(Context context, int helperId, BaseApiService.ApiCallback<List<ServiceResponse>> callback) {
        handleApiResponse(context, getHelperServices(context, helperId), callback);
    }
    
    public void AddMoneytoIncome(Context context, AddMoneytoIncomeRequest addMoneytoIncomeRequest, BaseApiService.ApiCallback<Void> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }
        Call<ApiResponse<Void>> call = apiInterface.addMoneyToWallet(addMoneytoIncomeRequest);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Phản hồi không thành công hoặc dữ liệu rỗng", null);
            }
        });
    }
}
