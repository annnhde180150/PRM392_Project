package com.example.homehelperfinder.data.remote.notification;

import com.example.homehelperfinder.data.model.request.NotificationCreateRequest;
import com.example.homehelperfinder.data.model.request.NotificationUpdateRequest;
import com.example.homehelperfinder.data.model.response.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Retrofit interface for notification API endpoints
 */
public interface NotificationApiInterface {

    @GET("notification")
    Call<NotificationResponse> getAllNotifications();

    @GET("notification/{id}")
    Call<NotificationResponse> getNotificationById(@Path("id") Long notificationId);

    @POST("notification")
    Call<NotificationResponse> createNotification(@Body NotificationCreateRequest request);

    @PUT("notification/{id}")
    Call<NotificationResponse> updateNotification(@Path("id") Long notificationId, 
                                                 @Body NotificationUpdateRequest request);

    @DELETE("notification/{id}")
    Call<NotificationResponse> deleteNotification(@Path("id") Long notificationId);

    @GET("notification/user/{userId}")
    Call<NotificationResponse> getNotificationsByUserId(@Path("userId") Integer userId);

    @GET("notification/helper/{helperId}")
    Call<NotificationResponse> getNotificationsByHelperId(@Path("helperId") Integer helperId);

    @GET("notification/user/{userId}/unread")
    Call<NotificationResponse> getUnreadNotificationsByUserId(@Path("userId") Integer userId);

    @GET("notification/helper/{helperId}/unread")
    Call<NotificationResponse> getUnreadNotificationsByHelperId(@Path("helperId") Integer helperId);

    @PATCH("notification/{id}/mark-read")
    Call<NotificationResponse> markNotificationAsRead(@Path("id") Long notificationId);

    @PATCH("notification/user/{userId}/mark-all-read")
    Call<NotificationResponse> markAllUserNotificationsAsRead(@Path("userId") Integer userId);

    @PATCH("notification/helper/{helperId}/mark-all-read")
    Call<NotificationResponse> markAllHelperNotificationsAsRead(@Path("helperId") Integer helperId);
}
