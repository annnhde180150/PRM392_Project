package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.Admin;
import com.example.homehelperfinder.data.model.Helper;
import com.example.homehelperfinder.data.model.User;
import com.example.homehelperfinder.data.model.request.LoginRequest;
import com.example.homehelperfinder.data.model.response.AdminLoginResponse;
import com.example.homehelperfinder.data.model.response.HelperLoginResponse;
import com.example.homehelperfinder.data.model.response.UserLoginResponse;
import com.example.homehelperfinder.data.model.response.ValidationErrorResponse;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.data.remote.auth.AuthApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.registerHelper.RegisterHelperActivity;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.NavigationHelper;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.ValidationUtils;
import com.example.homehelperfinder.utils.signalr.SignalRHelper;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private String userType;
    private AuthApiService authApiService;
    private SharedPrefsHelper sharedPrefsHelper;

    // UI Components
    private TextView tvGreeting;
    private TextInputLayout tilEmail;
    private EditText etEmail;
    private TextInputLayout tilPassword;
    private EditText etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userType = getIntent().getStringExtra("user_type");

        // Initialize services
        authApiService = new AuthApiService();
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);

        initViews();
        loadRememberedCredentials();
        setUpClickListeners();
        setupMenuNavigation();

        Logger.d(TAG, "LoginActivity created for user type: " + userType);
    }

    private void loadRememberedCredentials() {
        if (sharedPrefsHelper.isRememberMeEnabled()) {
            String savedEmail = sharedPrefsHelper.getSavedEmail();
            String savedUserType = sharedPrefsHelper.getSavedUserType();

            if (savedEmail != null && !savedEmail.isEmpty()) {
                etEmail.setText(savedEmail);
                cbRememberMe.setChecked(true);
                Logger.d(TAG, "Auto-filled saved email: " + savedEmail);

                // If user type matches, focus on password field
                if (userType != null && userType.equals(savedUserType)) {
                    etPassword.requestFocus();
                }
            }
        }
    }

    private void initViews() {
        // Initialize UI components
        tvGreeting = findViewById(R.id.tvGreeting);
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        // Set greeting and adapt UI based on user type
        String greeting = "Hello User!";
        if (userType != null) {
            switch (userType) {
                case Constants.USER_TYPE_CUSTOMER:
                    greeting = "Hello Customer!";
                    etEmail.setHint("Email Address");
                    break;
                case Constants.USER_TYPE_HELPER:
                    greeting = "Hello Helper!";
                    etEmail.setHint("Email Address");
                    break;
                case Constants.USER_TYPE_ADMIN:
                    greeting = "Hello Admin!";
                    etEmail.setHint("Username");
                    tilEmail.setHint("Username");
                    break;
            }
        }
        tvGreeting.setText(greeting);
    }

    private void setUpClickListeners() {
        // Login button click
        btnLogin.setOnClickListener(v -> performLogin());

        // Sign up link click
        tvSignUpLink.setOnClickListener(v -> {
            Class<?> targetActivity = getRegisterActivityByUserType();
            if (targetActivity != null) {
                startActivity(new Intent(this, targetActivity));
                finish();
            }
        });
    }

    private void performLogin() {
        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        // Get input values
        String emailOrUsername = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(emailOrUsername, password)) {
            return;
        }

        // Show loading state
        setLoadingState(true);

        // Perform login based on user type
        if (userType != null) {
            switch (userType) {
                case Constants.USER_TYPE_CUSTOMER:
                    loginUser(emailOrUsername, password);
                    break;
                case Constants.USER_TYPE_HELPER:
                    loginHelper(emailOrUsername, password);
                    break;
                case Constants.USER_TYPE_ADMIN:
                    loginAdmin(emailOrUsername, password);
                    break;
                default:
                    setLoadingState(false);
                    showError("Invalid user type");
                    break;
            }
        } else {
            setLoadingState(false);
            showError("User type not specified");
        }
    }

    private boolean validateInputs(String emailOrUsername, String password) {
        boolean isValid = true;

        // Validate email/username
        if (TextUtils.isEmpty(emailOrUsername)) {
            tilEmail.setError(userType != null && userType.equals(Constants.USER_TYPE_ADMIN)
                ? "Username is required" : "Email is required");
            isValid = false;
        } else if (!userType.equals(Constants.USER_TYPE_ADMIN) && !ValidationUtils.isValidEmail(emailOrUsername)) {
            tilEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            tilPassword.setError("Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
            isValid = false;
        }

        return isValid;
    }

    private void loginUser(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        authApiService.loginUser(this, request, new AuthApiService.AuthCallback<UserLoginResponse>() {
            @Override
            public void onSuccess(UserLoginResponse response) {
                Logger.i(TAG, "User login successful");
                handleLoginSuccess(response.getToken(), response.getUser());
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                setLoadingState(false);
                showError(errorMessage);
                Logger.e(TAG, "User login failed: " + errorMessage, throwable);
            }

            @Override
            public void onValidationError(ValidationErrorResponse validationError) {
                setLoadingState(false);
                handleValidationErrors(validationError);
            }
        });
    }

    private void loginHelper(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        authApiService.loginHelper(this, request, new AuthApiService.AuthCallback<HelperLoginResponse>() {
            @Override
            public void onSuccess(HelperLoginResponse response) {
                Logger.i(TAG, "Helper login successful");
                handleLoginSuccess(response.getToken(), response.getHelper());
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                setLoadingState(false);
                showError(errorMessage);
                Logger.e(TAG, "Helper login failed: " + errorMessage, throwable);
            }

            @Override
            public void onValidationError(ValidationErrorResponse validationError) {
                setLoadingState(false);
                handleValidationErrors(validationError);
            }
        });
    }

    private void loginAdmin(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        authApiService.loginAdmin(this, request, new AuthApiService.AuthCallback<AdminLoginResponse>() {
            @Override
            public void onSuccess(AdminLoginResponse response) {
                Logger.i(TAG, "Admin login successful");
                handleLoginSuccess(response.getToken(), response.getAdmin());
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                setLoadingState(false);
                showError(errorMessage);
                Logger.e(TAG, "Admin login failed: " + errorMessage, throwable);
            }

            @Override
            public void onValidationError(ValidationErrorResponse validationError) {
                setLoadingState(false);
                handleValidationErrors(validationError);
            }
        });
    }

    private Class<?> getRegisterActivityByUserType() {
        if (userType == null) return null;
        switch (userType) {
            case Constants.USER_TYPE_CUSTOMER:
                return RegisterCustomerActivity.class;
            case Constants.USER_TYPE_HELPER:
                return RegisterHelperActivity.class;
            default:
                return null;
        }
    }

    // Handle successful login for any user type
    private void handleLoginSuccess(String token, Object userObject) {
        setLoadingState(false);

        // Extract user data based on type
        String userId, userName, userEmail, mappedUserType;

        if (userObject instanceof User) {
            User user = (User) userObject;
            userId = String.valueOf(user.getId());
            userName = user.getFullName();
            userEmail = user.getEmail();
            mappedUserType = Constants.USER_TYPE_CUSTOMER; // Map "User" role to "customer"
        } else if (userObject instanceof Helper) {
            Helper helper = (Helper) userObject;
            userId = String.valueOf(helper.getId());
            userName = helper.getFullName();
            userEmail = helper.getEmail();
            mappedUserType = Constants.USER_TYPE_HELPER; // Map "Helper" role to "helper"
        } else if (userObject instanceof Admin) {
            Admin admin = (Admin) userObject;
            userId = String.valueOf(admin.getId());
            userName = admin.getFullName();
            userEmail = admin.getEmail();
            mappedUserType = Constants.USER_TYPE_ADMIN; // Map "Admin" role to "admin"
        } else {
            showError("Invalid user data received");
            return;
        }

        // Save user data to SharedPreferences
        sharedPrefsHelper.saveUserData(userId, mappedUserType, userName, userEmail, token);

        // Clear authenticated Retrofit cache to ensure new token is used
        RetrofitClient.clearAuthenticatedCache();
        Logger.d(TAG, "Cleared authenticated Retrofit cache for new token");

        // Save remember me preference and credentials
        if (cbRememberMe.isChecked()) {
            sharedPrefsHelper.saveRememberMeData(userEmail, mappedUserType);
            Logger.d(TAG, "Remember Me data saved for: " + userEmail);
        } else {
            // Clear remember me data if unchecked
            sharedPrefsHelper.clearRememberMeData();
            Logger.d(TAG, "Remember Me data cleared");
        }

        Logger.i(TAG, "User data saved successfully for: " + userName);

        // Initialize SignalR connection after successful login
        initializeSignalRAfterLogin();

        // Navigate to appropriate dashboard
        navigateToUserDashboard(mappedUserType);
    }

    private void handleValidationErrors(ValidationErrorResponse validationError) {
        // Show field-specific validation errors
        if (validationError.getEmail() != null && !validationError.getEmail().isEmpty()) {
            tilEmail.setError(validationError.getEmail().get(0));
        }
        if (validationError.getPassword() != null && !validationError.getPassword().isEmpty()) {
            tilPassword.setError(validationError.getPassword().get(0));
        }
        if (validationError.getUsername() != null && !validationError.getUsername().isEmpty()) {
            tilEmail.setError(validationError.getUsername().get(0));
        }
    }

    private void setLoadingState(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnLogin.setText(isLoading ? "Logging in..." : "LOGIN");
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
        cbRememberMe.setEnabled(!isLoading);
        tvSignUpLink.setEnabled(!isLoading);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void navigateToUserDashboard(String userType) {
        switch (userType) {
            case Constants.USER_TYPE_CUSTOMER:
                NavigationHelper.navigateToCustomerDashboard(this);
                break;
            case Constants.USER_TYPE_HELPER:
                NavigationHelper.navigateToHelperDashboard(this);
                break;
            case Constants.USER_TYPE_ADMIN:
                // Navigate to admin dashboard - you may need to implement this
                NavigationHelper.navigateToDashboard(this);
                break;
        }
        finish(); // Close login activity
    }

    /**
     * Initialize SignalR connection after successful login
     */
    private void initializeSignalRAfterLogin() {
        try {
            Logger.i(TAG, "Initializing SignalR after login...");

            SignalRHelper.onUserLogin(this);

            Logger.d(TAG, "SignalR initialization triggered successfully");

        } catch (Exception e) {
            Logger.e(TAG, "Error initializing SignalR after login", e);
            // Don't show error to user as SignalR is not critical for login flow
        }
    }
}