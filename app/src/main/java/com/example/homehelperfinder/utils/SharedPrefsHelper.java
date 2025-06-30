package com.example.homehelperfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    private static SharedPrefsHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context.getApplicationContext());
        }
        return instance;
    }

    // String methods
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
        Logger.d("SharedPrefs", "Saved string: " + key + " = " + value);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        String value = sharedPreferences.getString(key, defaultValue);
        Logger.d("SharedPrefs", "Retrieved string: " + key + " = " + value);
        return value;
    }

    // Boolean methods
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
        Logger.d("SharedPrefs", "Saved boolean: " + key + " = " + value);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean value = sharedPreferences.getBoolean(key, defaultValue);
        Logger.d("SharedPrefs", "Retrieved boolean: " + key + " = " + value);
        return value;
    }

    // Integer methods
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
        Logger.d("SharedPrefs", "Saved int: " + key + " = " + value);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        int value = sharedPreferences.getInt(key, defaultValue);
        Logger.d("SharedPrefs", "Retrieved int: " + key + " = " + value);
        return value;
    }

    // Long methods
    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
        Logger.d("SharedPrefs", "Saved long: " + key + " = " + value);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        long value = sharedPreferences.getLong(key, defaultValue);
        Logger.d("SharedPrefs", "Retrieved long: " + key + " = " + value);
        return value;
    }

    // User-specific methods
    public void saveUserData(String userId, String userType, String userName, String userEmail, String authToken) {
        putString(Constants.PREF_USER_ID, userId);
        putString(Constants.PREF_USER_TYPE, userType);
        putString(Constants.PREF_USER_NAME, userName);
        putString(Constants.PREF_USER_EMAIL, userEmail);
        putString(Constants.PREF_AUTH_TOKEN, authToken);
        putBoolean(Constants.PREF_IS_LOGGED_IN, true);
        Logger.i("SharedPrefs", "User data saved for: " + userName);
    }

    public boolean isLoggedIn() {
        return getBoolean(Constants.PREF_IS_LOGGED_IN);
    }

    public String getUserId() {
        return getString(Constants.PREF_USER_ID);
    }

    public String getUserType() {
        return getString(Constants.PREF_USER_TYPE);
    }

    public String getUserName() {
        return getString(Constants.PREF_USER_NAME);
    }

    public String getUserEmail() {
        return getString(Constants.PREF_USER_EMAIL);
    }

    public String getAuthToken() {
        return getString(Constants.PREF_AUTH_TOKEN);
    }

    // Clear methods
    public void clearUserData() {
        editor.remove(Constants.PREF_USER_ID);
        editor.remove(Constants.PREF_USER_TYPE);
        editor.remove(Constants.PREF_USER_NAME);
        editor.remove(Constants.PREF_USER_EMAIL);
        editor.remove(Constants.PREF_AUTH_TOKEN);
        editor.remove(Constants.PREF_IS_LOGGED_IN);
        editor.apply();
        Logger.i("SharedPrefs", "User data cleared");
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
        Logger.i("SharedPrefs", "All preferences cleared");
    }

    public void remove(String key) {
        editor.remove(key);
        editor.apply();
        Logger.d("SharedPrefs", "Removed key: " + key);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
