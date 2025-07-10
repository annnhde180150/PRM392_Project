package com.example.homehelperfinder.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    // Logging configuration
    public static final String LOG_TAG = "ProfileAPI";

    // Private constructor to prevent instantiation
    private NetworkUtils() {
        throw new UnsupportedOperationException("NetworkUtils class cannot be instantiated");
    }

    // Check if network is available
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Check if WiFi is connected
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo != null && wifiInfo.isConnected();
        }
        return false;
    }

    // Check if mobile data is connected
    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mobileInfo != null && mobileInfo.isConnected();
        }
        return false;
    }

    // Get network type
    public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return "WiFi";
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return "Mobile Data";
                }
            }
        }
        return "No Connection";
    }

    // Build full API URL
    public static String buildApiUrl(String endpoint) {
        return Constants.BASE_URL + Constants.API_BASE_PATH + endpoint;
    }

    // Build profile management API URL
    public static String buildProfileApiUrl(String endpoint) {
        return Constants.BASE_URL + Constants.API_BASE_PATH + Constants.PROFILE_MANAGEMENT_PATH + endpoint;
    }
} 