package com.example.homehelperfinder.data.remote.helper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HelperAvailableStatusApiInterface {
    @POST("Helper/OnlineRequest")
    Call<Void> onlineRequest(@Body int helperId);
    @POST("Helper/OfflineRequest")
    Call<Void> OfflineRequest(@Body int helperId);
    @POST("Helper/BusyRequest")
    Call<Void> BusyRequest(@Body int helperId);
}
