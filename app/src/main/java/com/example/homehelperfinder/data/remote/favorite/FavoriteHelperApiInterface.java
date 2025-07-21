package com.example.homehelperfinder.data.remote.favorite;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.FavoriteHelperDto;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

public interface FavoriteHelperApiInterface {
    @POST("FavoriteHelper")
    Call<ApiResponse<Void>> addFavorite(@Body RequestBody body);

    @HTTP(method = "DELETE", path = "FavoriteHelper", hasBody = true)
    Call<ApiResponse<Void>> deleteFavorite(@Body RequestBody body);

    @GET("FavoriteHelper/user/{userId}")
    Call<ApiResponse<List<FavoriteHelperDto>>> getFavoriteHelpersByUserId(@Path("userId") int userId);
} 