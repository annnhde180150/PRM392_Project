package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvSignUp;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar for consistent UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Get user type from intent
        userType = getIntent().getStringExtra("user_type");
        if (userType == null) {
            userType = "customer"; // Default to customer
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvForgotPassword.setOnClickListener(v -> {
            // TODO: Implement forgot password functionality
            Toast.makeText(this, "Forgot password functionality will be implemented", Toast.LENGTH_SHORT).show();
        });

        tvSignUp.setOnClickListener(v -> {
            // TODO: Navigate to sign up screen
            Toast.makeText(this, "Sign up functionality will be implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void attemptLogin() {
        // Reset errors
        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean cancel = false;

        // Check for valid password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (password.length() < 6) {
            etPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }

        // Check for valid email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login
            return;
        }

        // TODO: Implement actual login logic with backend
        // For now, just show success message and navigate to main screen
        performLogin(email, password);
    }

    private void performLogin(String email, String password) {
        // TODO: Replace with actual login implementation
        // This is a mock implementation
        if (email.equals("test@example.com") && password.equals("password")) {
            Toast.makeText(this, "Login successful as " + userType, Toast.LENGTH_SHORT).show();
            
            // Navigate to appropriate main screen based on user type
            Intent intent;
            if ("admin".equals(userType)) {
                intent = new Intent(LoginActivity.this, DashboardActivity.class);
            } else {
                // TODO: Create customer and helper main activities
                intent = new Intent(LoginActivity.this, DashboardActivity.class);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Navigate back to user type selection
        Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}