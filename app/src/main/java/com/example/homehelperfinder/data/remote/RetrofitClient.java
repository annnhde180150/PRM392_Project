package com.example.homehelperfinder.data.remote;

import com.example.homehelperfinder.data.remote.authentication.AuthenticationApiInterface;
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
    private static Retrofit retrofit = null;

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

    public static ServiceApiInterface getServiceApiService() {
        Logger.d("RetrofitClient", "Creating ServiceApiInterface service");
        return getClient().create(ServiceApiInterface.class);
    }
    public static AuthenticationApiInterface getAuthenticationApiService() {
        Logger.d("RetrofitClient", "Creating AuthenticationApiInterface service");
        return getClient().create(AuthenticationApiInterface.class);
    }
} 