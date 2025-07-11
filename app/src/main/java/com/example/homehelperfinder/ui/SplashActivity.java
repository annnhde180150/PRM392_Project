package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.NavigationHelper;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    private ImageView ivLogo;
    private TextView tvAppName;
    private TextView tvWelcomeMessage;
    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar for full screen experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        initSharedPrefs();
        startSplashSequence();
    }

    private void initViews() {
        ivLogo = findViewById(R.id.iv_logo);
        tvAppName = findViewById(R.id.tv_app_name);
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
    }

    private void initSharedPrefs() {
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
    }

    private void startSplashSequence() {
        Logger.d(TAG, "Starting splash sequence");

        // Check login state after splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkLoginStateAndNavigate();
        }, SPLASH_DELAY);
    }

    private void checkLoginStateAndNavigate() {
        try {
            boolean isLoggedIn = sharedPrefsHelper.isLoggedIn();
            String authToken = sharedPrefsHelper.getAuthToken();

            Logger.d(TAG, "Login state check - isLoggedIn: " + isLoggedIn + ", hasToken: " + (authToken != null && !authToken.isEmpty()));

            if (isLoggedIn && authToken != null && !authToken.isEmpty()) {
                // User is logged in with valid token
                String userName = sharedPrefsHelper.getUserName();
                String userType = sharedPrefsHelper.getUserType();

                Logger.i(TAG, "User is logged in: " + userName + " (" + userType + ")");

                // Show welcome back message
                showWelcomeMessage(userName);

                // Navigate to appropriate dashboard after a short delay
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    navigateToUserDashboard(userType);
                }, 1000);

            } else {
                // User is not logged in or token is invalid
                Logger.d(TAG, "User is not logged in, navigating to welcome screen");
                navigateToWelcome();
            }

        } catch (Exception e) {
            Logger.e(TAG, "Error checking login state", e);
            // On error, navigate to welcome screen as fallback
            navigateToWelcome();
        }
    }

    private void showWelcomeMessage(String userName) {
        if (tvWelcomeMessage != null && userName != null && !userName.isEmpty()) {
            tvWelcomeMessage.setText("Welcome back, " + userName + "!");
            tvWelcomeMessage.setVisibility(TextView.VISIBLE);
        }
    }

    private void navigateToUserDashboard(String userType) {
        try {
            switch (userType) {
                case Constants.USER_TYPE_CUSTOMER:
                    Logger.d(TAG, "Navigating to customer dashboard");
                    NavigationHelper.navigateToDashboard(this, true);
                    break;
                case Constants.USER_TYPE_HELPER:
                    Logger.d(TAG, "Navigating to helper dashboard");
                    NavigationHelper.navigateToHelperDashboard(this, true);
                    break;
                case Constants.USER_TYPE_ADMIN:
                    Logger.d(TAG, "Navigating to admin menu");
                    NavigationHelper.navigateToMenu(this, true);
                    break;
                default:
                    Logger.w(TAG, "Unknown user type: " + userType + ", navigating to customer dashboard");
                    NavigationHelper.navigateToDashboard(this, true);
                    break;
            }
            finish();
        } catch (Exception e) {
            Logger.e(TAG, "Error navigating to dashboard", e);
            navigateToWelcome();
        }
    }

    private void navigateToWelcome() {
        try {
            Logger.d(TAG, "Navigating to welcome screen");
            NavigationHelper.navigateToWelcome(this, true);
            finish();
        } catch (Exception e) {
            Logger.e(TAG, "Error navigating to welcome", e);
            // Last resort fallback
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable back button during splash
        // Do nothing
    }
}
