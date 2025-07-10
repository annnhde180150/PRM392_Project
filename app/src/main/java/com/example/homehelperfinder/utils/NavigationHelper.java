package com.example.homehelperfinder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.homehelperfinder.ui.DashboardActivity;
import com.example.homehelperfinder.ui.HelperDashboardActivity;
import com.example.homehelperfinder.ui.LoginActivity;
import com.example.homehelperfinder.ui.MenuActivity;
import com.example.homehelperfinder.ui.UserTypeActivity;
import com.example.homehelperfinder.ui.WelcomeActivity;
import com.example.homehelperfinder.ui.profileManagement.ProfileManagementActivity;

public class NavigationHelper {

    // Private constructor to prevent instantiation
    private NavigationHelper() {
        throw new UnsupportedOperationException("NavigationHelper class cannot be instantiated");
    }

    // Navigate to Welcome screen
    public static void navigateToWelcome(Context context) {
        navigateToWelcome(context, false);
    }

    public static void navigateToWelcome(Context context, boolean clearStack) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to Welcome");
    }

    // Navigate to User Type selection
    public static void navigateToUserType(Context context) {
        Intent intent = new Intent(context, UserTypeActivity.class);
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to UserType");
    }

    // Navigate to Login
    public static void navigateToLogin(Context context) {
        navigateToLogin(context, null);
    }

    public static void navigateToLogin(Context context, String userType) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (userType != null) {
            intent.putExtra(Constants.INTENT_USER_TYPE, userType);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to Login with userType: " + userType);
    }

    // Navigate to Dashboard
    public static void navigateToDashboard(Context context) {
        navigateToDashboard(context, false);
    }

    public static void navigateToDashboard(Context context, boolean clearStack) {
        Intent intent = new Intent(context, DashboardActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to Dashboard");
    }

    // Navigate to Helper Dashboard
    public static void navigateToHelperDashboard(Context context) {
        navigateToHelperDashboard(context, false);
    }

    public static void navigateToHelperDashboard(Context context, boolean clearStack) {
        Intent intent = new Intent(context, HelperDashboardActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to Helper Dashboard");
    }

    // Navigate to Profile Management
    public static void navigateToProfileManagement(Context context) {
        Intent intent = new Intent(context, ProfileManagementActivity.class);
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to ProfileManagement");
    }

    // Navigate to Menu (for testing)
    public static void navigateToMenu(Context context) {
        navigateToMenu(context, false);
    }

    public static void navigateToMenu(Context context, boolean clearStack) {
        Intent intent = new Intent(context, MenuActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to Menu");
    }

    // Navigate with custom intent and extras
    public static void navigateWithExtras(Context context, Class<?> targetActivity, Bundle extras) {
        Intent intent = new Intent(context, targetActivity);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
        Logger.d("NavigationHelper", "Navigated to " + targetActivity.getSimpleName() + " with extras");
    }

    // Navigate for result
    public static void navigateForResult(Activity activity, Class<?> targetActivity, int requestCode) {
        navigateForResult(activity, targetActivity, requestCode, null);
    }

    public static void navigateForResult(Activity activity, Class<?> targetActivity, int requestCode, Bundle extras) {
        Intent intent = new Intent(activity, targetActivity);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivityForResult(intent, requestCode);
        Logger.d("NavigationHelper", "Navigated to " + targetActivity.getSimpleName() + " for result with requestCode: " + requestCode);
    }

    // Logout and navigate to welcome
    public static void logout(Context context) {
        // Clear user data
        SharedPrefsHelper.getInstance(context).clearUserData();

        // Navigate to welcome screen and clear stack
        navigateToWelcome(context, true);

        Logger.i("NavigationHelper", "User logged out successfully");
    }

    // Check login status and navigate accordingly
    public static void navigateBasedOnLoginStatus(Context context) {
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);

        if (prefsHelper.isLoggedIn()) {
            String userType = prefsHelper.getUserType();
            Logger.d("NavigationHelper", "User is logged in as: " + userType);

            // Navigate based on user type
            switch (userType) {
                case Constants.USER_TYPE_CUSTOMER:
                    navigateToDashboard(context, true);
                    break;
                case Constants.USER_TYPE_HELPER:
                    navigateToHelperDashboard(context, true);
                    break;
                case Constants.USER_TYPE_ADMIN:
                    navigateToMenu(context, true);
                    break;
                default:
                    Logger.w("NavigationHelper", "Unknown user type: " + userType);
                    navigateToDashboard(context, true);
                    break;
            }
        } else {
            Logger.d("NavigationHelper", "User is not logged in");
            navigateToWelcome(context, true);
        }
    }

    // Finish current activity
    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            Logger.d("NavigationHelper", "Finished activity: " + activity.getClass().getSimpleName());
        }
    }
}
