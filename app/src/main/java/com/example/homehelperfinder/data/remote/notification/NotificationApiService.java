package com.example.homehelperfinder.data.remote.notification;

import android.content.Context;

import com.example.homehelperfinder.data.model.NotificationModel;
import com.example.homehelperfinder.data.model.request.NotificationCreateRequest;
import com.example.homehelperfinder.data.model.request.NotificationUpdateRequest;
import com.example.homehelperfinder.data.model.response.NotificationResponse;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Service class for notification API operations
 */
public class NotificationApiService {
    private static final String TAG = "NotificationApiService";
    private final NotificationApiInterface apiInterface;
    private final Context context;

    public NotificationApiService(Context context) {
        this.context = context.getApplicationContext();
        RetrofitClient.init(this.context);
        this.apiInterface = RetrofitClient.getAuthenticatedClient().create(NotificationApiInterface.class);
    }

    /**
     * Callback interface for notification operations
     */
    public interface NotificationCallback {
        void onSuccess(NotificationModel notification);
        void onError(String error);
    }

    /**
     * Callback interface for notification list operations
     */
    public interface NotificationListCallback {
        void onSuccess(List<NotificationModel> notifications);
        void onError(String error);
    }

    /**
     * Callback interface for simple operations (delete, mark as read)
     */
    public interface SimpleCallback {
        void onSuccess();
        void onError(String error);
    }

    /**
     * Get all notifications
     */
    public void getAllNotifications(NotificationListCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getAllNotifications();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationListResponse(response, callback, "getAllNotifications");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getAllNotifications failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get notification by ID
     */
    public void getNotificationById(Long notificationId, NotificationCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getNotificationById(notificationId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationResponse(response, callback, "getNotificationById");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getNotificationById failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Create new notification
     */
    public void createNotification(NotificationCreateRequest request, NotificationCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        if (!request.isValid()) {
            callback.onError("Invalid notification data");
            return;
        }

        Call<NotificationResponse> call = apiInterface.createNotification(request);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationResponse(response, callback, "createNotification");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "createNotification failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Update notification
     */
    public void updateNotification(Long notificationId, NotificationUpdateRequest request, NotificationCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        if (!request.isValid()) {
            callback.onError("Invalid update data");
            return;
        }

        Call<NotificationResponse> call = apiInterface.updateNotification(notificationId, request);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationResponse(response, callback, "updateNotification");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "updateNotification failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Delete notification
     */
    public void deleteNotification(Long notificationId, SimpleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.deleteNotification(notificationId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleSimpleResponse(response, callback, "deleteNotification");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "deleteNotification failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get notifications by user ID
     */
    public void getNotificationsByUserId(Integer userId, NotificationListCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getNotificationsByUserId(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationListResponse(response, callback, "getNotificationsByUserId");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getNotificationsByUserId failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get notifications by helper ID
     */
    public void getNotificationsByHelperId(Integer helperId, NotificationListCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getNotificationsByHelperId(helperId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationListResponse(response, callback, "getNotificationsByHelperId");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getNotificationsByHelperId failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get unread notifications by user ID
     */
    public void getUnreadNotificationsByUserId(Integer userId, NotificationListCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getUnreadNotificationsByUserId(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationListResponse(response, callback, "getUnreadNotificationsByUserId");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getUnreadNotificationsByUserId failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get unread notifications by helper ID
     */
    public void getUnreadNotificationsByHelperId(Integer helperId, NotificationListCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.getUnreadNotificationsByHelperId(helperId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleNotificationListResponse(response, callback, "getUnreadNotificationsByHelperId");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "getUnreadNotificationsByHelperId failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Mark notification as read
     */
    public void markNotificationAsRead(Long notificationId, SimpleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.markNotificationAsRead(notificationId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleSimpleResponse(response, callback, "markNotificationAsRead");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "markNotificationAsRead failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Mark all user notifications as read
     */
    public void markAllUserNotificationsAsRead(Integer userId, SimpleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.markAllUserNotificationsAsRead(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleSimpleResponse(response, callback, "markAllUserNotificationsAsRead");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "markAllUserNotificationsAsRead failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Mark all helper notifications as read
     */
    public void markAllHelperNotificationsAsRead(Integer helperId, SimpleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection");
            return;
        }

        Call<NotificationResponse> call = apiInterface.markAllHelperNotificationsAsRead(helperId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                handleSimpleResponse(response, callback, "markAllHelperNotificationsAsRead");
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Logger.e(TAG, "markAllHelperNotificationsAsRead failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Handle notification response for single notification operations
     */
    private void handleNotificationResponse(Response<NotificationResponse> response,
                                          NotificationCallback callback, String operation) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                NotificationResponse notificationResponse = response.body();
                if (notificationResponse.isSuccessful()) {
                    NotificationModel notification = notificationResponse.getNotification();
                    if (notification != null) {
                        Logger.d(TAG, operation + " successful");
                        callback.onSuccess(notification);
                    } else {
                        Logger.w(TAG, operation + " successful but no notification data");
                        callback.onError("No notification data received");
                    }
                } else {
                    Logger.w(TAG, operation + " failed: " + notificationResponse.getErrorMessage());
                    callback.onError(notificationResponse.getErrorMessage());
                }
            } else {
                String error = handleErrorResponse(response);
                Logger.e(TAG, operation + " failed: " + error);
                callback.onError(error);
            }
        } catch (Exception e) {
            Logger.e(TAG, operation + " error", e);
            callback.onError("Error processing response: " + e.getMessage());
        }
    }

    /**
     * Handle notification response for list operations
     */
    private void handleNotificationListResponse(Response<NotificationResponse> response,
                                              NotificationListCallback callback, String operation) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                NotificationResponse notificationResponse = response.body();
                if (notificationResponse.isSuccessful()) {
                    List<NotificationModel> notifications = notificationResponse.getNotifications();
                    if (notifications != null) {
                        Logger.d(TAG, operation + " successful, received " + notifications.size() + " notifications");
                        callback.onSuccess(notifications);
                    } else {
                        Logger.d(TAG, operation + " successful but no notifications");
                        callback.onSuccess(new ArrayList<>()); // Return empty list
                    }
                } else {
                    Logger.w(TAG, operation + " failed: " + notificationResponse.getErrorMessage());
                    callback.onError(notificationResponse.getErrorMessage());
                }
            } else {
                String error = handleErrorResponse(response);
                Logger.e(TAG, operation + " failed: " + error);
                callback.onError(error);
            }
        } catch (Exception e) {
            Logger.e(TAG, operation + " error", e);
            callback.onError("Error processing response: " + e.getMessage());
        }
    }

    /**
     * Handle simple response for operations that don't return data
     */
    private void handleSimpleResponse(Response<NotificationResponse> response,
                                    SimpleCallback callback, String operation) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                NotificationResponse notificationResponse = response.body();
                if (notificationResponse.isSuccessful()) {
                    Logger.d(TAG, operation + " successful");
                    callback.onSuccess();
                } else {
                    Logger.w(TAG, operation + " failed: " + notificationResponse.getErrorMessage());
                    callback.onError(notificationResponse.getErrorMessage());
                }
            } else {
                String error = handleErrorResponse(response);
                Logger.e(TAG, operation + " failed: " + error);
                callback.onError(error);
            }
        } catch (Exception e) {
            Logger.e(TAG, operation + " error", e);
            callback.onError("Error processing response: " + e.getMessage());
        }
    }

    /**
     * Handle error response from API
     */
    private String handleErrorResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return "HTTP " + response.code() + ": " + response.message();
            } else {
                return "HTTP " + response.code() + ": " + response.message();
            }
        } catch (Exception e) {
            Logger.e(TAG, "Error parsing error response", e);
            return "HTTP " + response.code() + ": Unknown error";
        }
    }
}
