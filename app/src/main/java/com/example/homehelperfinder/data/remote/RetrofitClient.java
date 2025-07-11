package com.example.homehelperfinder.data.remote;

import android.content.Context;

import com.example.homehelperfinder.data.remote.Payment.PaymentApiInterface;
import com.example.homehelperfinder.data.remote.auth.AuthApiInterface;
import com.example.homehelperfinder.data.remote.chat.ChatApiInterface;
import com.example.homehelperfinder.data.remote.profile.ProfileManagementApiInterface;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = Constants.RETROFIT_BASE_URL;
    private static Retrofit retrofit = null;
    private static Retrofit chatRetrofit = null;
    private static Context applicationContext;

    public static void init(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            Logger.d("RetrofitClient", "Creating new Retrofit instance with base URL: " + BASE_URL);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                    Logger.network("HTTP: " + message));
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Logger.i("RetrofitClient", "Retrofit client created successfully");
        }
        return retrofit;
    }

    public static ProfileManagementApiInterface getProfileApiService() {
        Logger.d("RetrofitClient", "Creating ProfileApiInterface service");
        return getClient().create(ProfileManagementApiInterface.class);
    }

    public static Retrofit getChatClient() {
        if (chatRetrofit == null) {
            if (applicationContext == null) {
                throw new IllegalStateException("RetrofitClient not initialized. Call RetrofitClient.init(context) first.");
            }

            Logger.d("RetrofitClient", "Creating new Chat Retrofit instance with base URL: " + BASE_URL);

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

            chatRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Logger.i("RetrofitClient", "Chat Retrofit client created successfully");
        }
        return chatRetrofit;
    }

    public static ChatApiInterface getChatApiService() {
        Logger.d("RetrofitClient", "Creating ChatApiInterface service");
        return getChatClient().create(ChatApiInterface.class);
    }

    public static AuthApiInterface getAuthApiService() {
        Logger.d("RetrofitClient", "Creating AuthApiInterface service");
        return getClient().create(AuthApiInterface.class);
    }

    public static PaymentApiInterface getPaymentApiService() {
        Logger.d("RetrofitClient", "Creating PaymentApiInterface service");
        return getClient().create(PaymentApiInterface.class);

    }
}