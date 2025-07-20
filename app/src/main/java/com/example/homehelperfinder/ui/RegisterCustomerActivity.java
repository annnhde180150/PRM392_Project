package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.UserRequest;
import com.example.homehelperfinder.data.model.response.UserResponse;
import com.example.homehelperfinder.data.remote.auth.AuthApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.content.DialogInterface;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import android.widget.ImageView;

public class RegisterCustomerActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword, etContactNumber;
    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword, tilContactNumber;
    private Button btnSignup;
    private AuthApiService authApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initViews();
        setupMenuNavigation();
        setupRegistration();
    }
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register as Customer");
        }
    }
    private void initViews() {
        setupToolbar();
        
        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etContactNumber = findViewById(R.id.etContactNumber);
        
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tilContactNumber = findViewById(R.id.tilContactNumber);
        
        btnSignup = findViewById(R.id.btnSignup);
        
        // Initialize API service
        authApiService = new AuthApiService();
        
        
    }
    
    private void setupRegistration() {
        btnSignup.setOnClickListener(v -> {
            if (validateInputs()) {
                showLoading("Processing registration...");
                performRegistration();
            }
        });
    }
    
    private boolean validateInputs() {
        boolean isValid = true;
        
        // Clear previous errors
        clearErrors();
        
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();
        
        // Validate full name
        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setError("Full name is required");
            isValid = false;
        } else if (fullName.length() < 2) {
            tilFullName.setError("Full name must be at least 2 characters");
            isValid = false;
        }
        
        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError("Please enter a valid email address");
            isValid = false;
        }
        
        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }
        
        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }
        
        // Validate contact number
        if (TextUtils.isEmpty(contactNumber)) {
            tilContactNumber.setError("Contact number is required");
            isValid = false;
        } else if (!ValidationUtils.isValidPhone(contactNumber)) {
            tilContactNumber.setError("Please enter a valid phone number");
            isValid = false;
        }
        
        return isValid;
    }
    
    private void clearErrors() {
        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
        tilContactNumber.setError(null);
    }
    
    private void performRegistration() {
        UserRequest request = new UserRequest();
        request.setPhoneNumber(etContactNumber.getText().toString().trim());
        request.setEmail(etEmail.getText().toString().trim());
        request.setPassword(etPassword.getText().toString().trim());
        request.setFullName(etFullName.getText().toString().trim());
        
        authApiService.registerUser(this, request, new AuthApiService.AuthCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse response) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(RegisterCustomerActivity.this, "Registration successful! Please login to continue", Toast.LENGTH_LONG).show();
                    showOtpDialog(etEmail.getText().toString().trim());
                });
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(RegisterCustomerActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showOtpDialog(String email) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_otp_verification, null);
        builder.setView(view);
        builder.setCancelable(false);
        android.app.AlertDialog dialog = builder.create();

        EditText[] otpFields = new EditText[] {
            view.findViewById(R.id.etOtp1),
            view.findViewById(R.id.etOtp2),
            view.findViewById(R.id.etOtp3),
            view.findViewById(R.id.etOtp4),
            view.findViewById(R.id.etOtp5),
            view.findViewById(R.id.etOtp6)
        };
        Button btnVerify = view.findViewById(R.id.btnVerifyOtp);

        // Auto-move focus
        for (int i = 0; i < otpFields.length; i++) {
            final int idx = i;
            otpFields[i].setText("");
            otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (otpFields[idx].getText().length() == 1 && idx < otpFields.length - 1) {
                    otpFields[idx + 1].requestFocus();
                }
                return false;
            });
        }

        btnVerify.setOnClickListener(v -> {
            StringBuilder otp = new StringBuilder();
            for (EditText et : otpFields) {
                String digit = et.getText().toString().trim();
                if (digit.length() != 1) {
                    et.setError(" ");
                    et.requestFocus();
                    return;
                }
                otp.append(digit);
            }
            verifyOtp(email, otp.toString(), dialog);
        });


        dialog.show();
    }

    private void verifyOtp(String email, String otp, AlertDialog dialog) {
        showLoading("Verifying OTP...");
        authApiService.verifyOtp(this, email, otp, new AuthApiService.AuthCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    hideLoading();
                    dialog.dismiss();
                    Toast.makeText(RegisterCustomerActivity.this, "Email verified! Please login.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterCustomerActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(RegisterCustomerActivity.this, errorMessage != null ? errorMessage : "Verification failed", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}