package com.example.homehelperfinder.data.remote;

import android.content.Context;

import com.example.homehelperfinder.data.remote.Payment.PaymentApiInterface;
import com.example.homehelperfinder.data.remote.auth.AuthApiInterface;
import com.example.homehelperfinder.data.remote.chat.ChatApiInterface;
import com.example.homehelperfinder.data.remote.profile.ProfileManagementApiInterface;
import com.example.homehelperfinder.data.remote.service.ServiceApiInterface;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = Constants.RETROFIT_BASE_URL;
    private static Retrofit publicRetrofit = null;  // For login/register (no auth)
    private static Retrofit authenticatedRetrofit = null;  // For authenticated APIs
    private static Context applicationContext;

    public static void init(Context context) {
        applicationContext = context.getApplicationContext();
    }

    /**
     * Get Retrofit client for public APIs (login, register) - NO AuthInterceptor
     */
    public static Retrofit getPublicClient() {
        if (publicRetrofit == null) {
            Logger.d("RetrofitClient", "Creating new public Retrofit instance with base URL: " + BASE_URL);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                    Logger.network("HTTP: " + message));
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            publicRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Logger.i("RetrofitClient", "Public Retrofit client created successfully");
        }
        return publicRetrofit;
    }

    /**
     * Get Retrofit client for authenticated APIs - WITH AuthInterceptor
     */
    public static Retrofit getAuthenticatedClient() {
        if (authenticatedRetrofit == null) {
            if (applicationContext == null) {
                throw new IllegalStateException("RetrofitClient not initialized. Call RetrofitClient.init(context) first.");
            }

            Logger.d("RetrofitClient", "Creating new authenticated Retrofit instance with base URL: " + BASE_URL);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                    Logger.network("HTTP: " + message));
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(applicationContext))
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            authenticatedRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Logger.i("RetrofitClient", "Authenticated Retrofit client created successfully");
        }
        return authenticatedRetrofit;
    }

    // API Service methods
    public static ProfileManagementApiInterface getProfileApiService() {
        Logger.d("RetrofitClient", "Creating ProfileApiInterface service with authentication");
        return getAuthenticatedClient().create(ProfileManagementApiInterface.class);
    }

    public static ChatApiInterface getChatApiService() {
        Logger.d("RetrofitClient", "Creating ChatApiInterface service with authentication");
        return getAuthenticatedClient().create(ChatApiInterface.class);
    }

    public static AuthApiInterface getAuthApiService() {
        Logger.d("RetrofitClient", "Creating AuthApiInterface service without authentication (for login)");
        return getPublicClient().create(AuthApiInterface.class);
    }

    public static AuthApiInterface getAuthenticatedAuthApiService() {
        Logger.d("RetrofitClient", "Creating AuthApiInterface service with authentication (for logout)");
        return getAuthenticatedClient().create(AuthApiInterface.class);
    }

    public static ServiceApiInterface getServiceApiService() {
        Logger.d("RetrofitClient", "Creating ServiceApiInterface service");
        return getClient().create(ServiceApiInterface.class);
    }

    /**
     * Clear all cached Retrofit instances - useful when token changes or user logs out
     */
    public static void clearCache() {
        Logger.i("RetrofitClient", "Clearing all cached Retrofit instances");
        publicRetrofit = null;
        authenticatedRetrofit = null;
    }

    /**
     * Clear only authenticated client cache - useful when token changes
     */
    public static void clearAuthenticatedCache() {
        Logger.i("RetrofitClient", "Clearing authenticated Retrofit instance cache");
        authenticatedRetrofit = null;
    }

    // Backward compatibility methods (deprecated)
    @Deprecated
    public static Retrofit getClient() {
        Logger.w("RetrofitClient", "getClient() is deprecated. Use getPublicClient() or getAuthenticatedClient()");
        return getPublicClient();
    }

    @Deprecated
    public static Retrofit getChatClient() {
        Logger.w("RetrofitClient", "getChatClient() is deprecated. Use getAuthenticatedClient()");
        return getAuthenticatedClient();
    }


    public static PaymentApiInterface getPaymentApiService() {
        Logger.d("RetrofitClient", "Creating PaymentApiInterface service");
        return getClient().create(PaymentApiInterface.class);

    }
}

