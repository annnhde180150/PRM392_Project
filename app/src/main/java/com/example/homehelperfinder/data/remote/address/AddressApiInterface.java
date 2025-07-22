package com.example.homehelperfinder.data.remote.address;

import com.example.homehelperfinder.data.model.request.UserAddressCreateRequest;
import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApiInterface {

    @GET("Address/User/{userId}")
    Call<ApiResponse<List<UserAddressResponse>>> getUserAddresses(@Path("userId") int userId);

    @GET("Address/UserAddress/{id}")
    Call<ApiResponse<UserAddressResponse>> getUserAddress(@Path("id") int id);
    @GET("Address/UserAddress/{id}")
    Call<ApiResponse<UserAddressResponse>> getUserAddress(@Path("id") int id);
    @PUT("Address/UserAddress/{id}")
    Call<ApiResponse<UserAddressResponse>> updateUserAddress(@Path("id") int id, @Body UserAddressUpdateRequest request);

    @DELETE("Address/UserAddress/{id}")
    Call<ApiResponse<Void>> deleteUserAddress(@Path("id") int id);

    @POST("Address/UserAddress")
    Call<ApiResponse<Void>> createUserAddress(@Body UserAddressCreateRequest request);

    @GET("Address/User/{userId}")
    Call<ApiResponse<List<UserAddressResponse>>> getUserAddressesByUserId(@Path("userId") int userId);

}
