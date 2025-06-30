package com.example.homehelperfinder.data.remote;

import android.content.Context;
import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.utils.ErrorHandler;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.NetworkUtils;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseApiService {
    
    protected final String TAG;
    
    public BaseApiService() {
        this.TAG = getClass().getSimpleName();
    }
    
    // Generic method to handle API calls
    protected <T> CompletableFuture<T> executeCall(Context context, Call<ApiResponse<T>> call, String operation) {
        CompletableFuture<T> future = new CompletableFuture<>();
        
        // Check network connectivity
        if (!NetworkUtils.isNetworkAvailable(context)) {
            String errorMsg = "No internet connection available";
            Logger.e(TAG, errorMsg);
            future.completeExceptionally(new Exception(errorMsg));
            return future;
        }
        
        Logger.d(TAG, "Starting " + operation);
        
        call.enqueue(new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<T> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        T data = apiResponse.getData();
                        Logger.i(TAG, operation + " successful: " + apiResponse.getMessage());
                        future.complete(data);
                    } else {
                        String errorMsg = "API Error - " + apiResponse.getMessage();
                        Logger.e(TAG, operation + " failed: " + errorMsg);
                        future.completeExceptionally(new Exception(errorMsg));
                    }
                } else {
                    String errorMsg = "HTTP Error: " + response.code() + " - " + response.message();
                    Logger.e(TAG, operation + " failed: " + errorMsg);
                    future.completeExceptionally(new Exception(errorMsg));
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
                String errorMsg = operation + " network error: " + t.getMessage();
                Logger.e(TAG, errorMsg, t);
                future.completeExceptionally(t);
            }
        });
        
        return future;
    }
    
    // Handle API response with error handling
    protected <T> void handleApiResponse(Context context, CompletableFuture<T> future, 
                                       ApiCallback<T> callback) {
        future.thenAccept(data -> {
            if (callback != null) {
                callback.onSuccess(data);
            }
        }).exceptionally(throwable -> {
            if (callback != null) {
                ErrorHandler.ErrorResult errorResult = ErrorHandler.handleError(context, throwable);
                callback.onError(errorResult.getMessage(), throwable);
            }
            return null;
        });
    }
    
    // Callback interface for API responses
    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage, Throwable throwable);
    }
}
