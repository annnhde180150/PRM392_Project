package com.example.homehelperfinder.data.remote.favorite;

import android.content.Context;
import com.example.homehelperfinder.data.model.response.FavoriteHelperDto;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import okhttp3.RequestBody;

public class FavoriteHelperApiService extends BaseApiService {
    private final FavoriteHelperApiInterface apiInterface;

    public FavoriteHelperApiService() {
        this.apiInterface = RetrofitClient.getFavoriteHelperApiService();
    }

    public CompletableFuture<List<FavoriteHelperDto>> getFavoriteHelpersByUserId(Context context, int userId) {
        return executeCall(context, apiInterface.getFavoriteHelpersByUserId(userId), "getFavoriteHelpersByUserId");
    }

    public void getFavoriteHelpersByUserId(Context context, int userId, ApiCallback<List<FavoriteHelperDto>> callback) {
        handleApiResponse(context, getFavoriteHelpersByUserId(context, userId), callback);
    }

    public void deleteFavorite(Context context, RequestBody body, ApiCallback<Void> callback) {
        handleApiResponse(context, executeCall(context, apiInterface.deleteFavorite(body), "deleteFavorite"), callback);
    }

    public void addFavorite(Context context, RequestBody body, ApiCallback<Void> callback) {
        handleApiResponse(context, executeCall(context, apiInterface.addFavorite(body), "addFavorite"), callback);
    }
} 