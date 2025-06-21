package com.example.homehelperfinder.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Retrofit client factory with logging interceptor
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:5272/api/ProfileManagement/";
    private static Retrofit retrofit = null;
    
    /**
     * Get Retrofit instance with logging interceptor
     * @return Retrofit instance
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Create OkHttp client with logging interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            
            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    
    /**
     * Get ProfileApiInterface instance
     * @return ProfileApiInterface instance
     */
    public static ProfileApiInterface getProfileApiService() {
        return getClient().create(ProfileApiInterface.class);
    }
} 