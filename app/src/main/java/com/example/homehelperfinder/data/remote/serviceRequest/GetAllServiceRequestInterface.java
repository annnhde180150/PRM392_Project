package com.example.homehelperfinder.data.remote.serviceRequest;
import com.example.homehelperfinder.data.model.request.ServiceRequestUpdateStatusRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetAllServiceRequestInterface {
    @GET("Bookings/GetBookingByHelperId/{helperId}")
    Call<ApiResponse<List<GetAllServiceRequestResponse>>> getAllBookingsByHelperId(@Path("helperId") int helperId);

    @PUT("ServiceRequests/UpdateRequestStatus")
    Call<ApiResponse<Void>> updateRequestStatus(@Body ServiceRequestUpdateStatusRequest request);

    @GET("Bookings/ActiveByUser/{userId}")
    Call<ApiResponse<List<GetAllServiceRequestResponse>>> getActiveBookingsByUserId(@Path("userId") int userId);

    @GET("Bookings/ActiveByHelper/{helperId}")
    Call<ApiResponse<List<GetAllServiceRequestResponse>>> getActiveBookingsByHelperId(@Path("helperId") int helperId);
}
