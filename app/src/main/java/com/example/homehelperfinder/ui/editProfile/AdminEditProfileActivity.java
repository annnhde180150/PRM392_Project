package com.example.homehelperfinder.ui.editProfile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.Admin;
import com.example.homehelperfinder.data.model.request.AdminUpdateRequest;
import com.example.homehelperfinder.data.remote.auth.AuthApiService;
import com.example.homehelperfinder.data.model.response.LogoutResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.profile.EditProfileApiService;
import com.example.homehelperfinder.ui.DashboardActivity;
import com.example.homehelperfinder.ui.LoginActivity;
import com.example.homehelperfinder.ui.WelcomeActivity;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.base.BaseAdminActivity;

import com.example.homehelperfinder.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.homehelperfinder.utils.UserManager;
import com.example.homehelperfinder.utils.Logger;

import android.view.View;
import com.example.homehelperfinder.data.model.request.ChangePasswordRequest;
import com.example.homehelperfinder.data.remote.user.UserApiService;
import com.example.homehelperfinder.data.model.response.ApiResponse;

public class AdminEditProfileActivity extends BaseAdminActivity {
    private Toolbar toolbar;

    private TextInputEditText etUsername, etFullName, etEmail, etRole;
    private SwitchMaterial switchActive;
    private ImageView ivProfilePicture;
    private Button btnConfirm, btnLogout;
    private TextView tvAdminId, tvRegistrationDate, tvAdminFullName;
    private ImageButton btnEditProfile;
    private Button btnChangePassword;
    private UserApiService userApiService;

    private int currentAdminId;
    private EditProfileApiService editProfileApiService;
    private UserManager userManager;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UserManager
        userManager = UserManager.getInstance(this);
        userApiService = new UserApiService();

        initViews();
        loadCurrentProfile();
        setEditMode(false);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    @Override
    protected AdminPage getCurrentPage() {
        return AdminPage.PROFILE;
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Admin Profile");
        }
    }

    private void initViews() {
        editProfileApiService = new EditProfileApiService();
        setupToolbar();
        etUsername = findViewById(R.id.etUsername);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etRole = findViewById(R.id.etRole);
        switchActive = findViewById(R.id.switchActive);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnLogout = findViewById(R.id.btnLogout);
        tvAdminId = findViewById(R.id.tvAdminId);
        tvRegistrationDate = findViewById(R.id.tvRegistrationDate);
        tvAdminFullName = findViewById(R.id.tvAdminFullName);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnConfirm.setOnClickListener(v -> {
            updateProfile();
            setEditMode(false);
        });
        btnEditProfile.setOnClickListener(v -> {
            setEditMode(!isEditMode);
        });
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void setEditMode(boolean editable) {
        isEditMode = editable;
        etUsername.setEnabled(editable);
        etFullName.setEnabled(editable);
        etEmail.setEnabled(editable);
        switchActive.setEnabled(editable);
        etRole.setEnabled(false);
        btnConfirm.setVisibility(editable ? View.VISIBLE : View.GONE);
        
        if (btnLogout != null) {
            btnLogout.setVisibility(editable ? View.GONE : View.VISIBLE);
        }
        
        if (editable) {
            btnEditProfile.setImageResource(R.drawable.ic_close);
        } else {
            btnEditProfile.setImageResource(R.drawable.ic_edit);
        }
    }



    private void loadCurrentProfile() {
        showLoading("Loading profile...");

        currentAdminId = userManager.getCurrentUserId();
        editProfileApiService.getAdminProfile(this, currentAdminId, new BaseApiService.ApiCallback<Admin>() {
            @Override
            public void onSuccess(Admin profile) {
                runOnUiThread(() -> {
                    hideLoading();
                    populateFields(profile);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(AdminEditProfileActivity.this, 
                        "Failed to load profile: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });

        setEditMode(false);
    }

    private void populateFields(Admin profile) {
        if (profile.getUsername() != null) {
            etUsername.setText(profile.getUsername());
        }
        if (profile.getFullName() != null) {
            etFullName.setText(profile.getFullName());
            tvAdminFullName.setText(profile.getFullName());
        }
        if (profile.getEmail() != null) {
            etEmail.setText(profile.getEmail());
        }
        if (profile.getRole() != null) {
            etRole.setText(profile.getRole());
        }
        
        switchActive.setChecked(profile.isActive());
        
        if (profile.getId() != 0) {
            tvAdminId.setText("ID: " + profile.getId());
        }

        if (profile.getCreationDate() != null) {
            try {
                java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", java.util.Locale.getDefault());
                java.util.Date creationDate = inputFormat.parse(profile.getCreationDate());
                java.text.SimpleDateFormat monthYearFormat = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault());
                String monthYear = monthYearFormat.format(creationDate);
                tvRegistrationDate.setText("Joined " + monthYear);
            } catch (Exception e) {
                tvRegistrationDate.setText("Joined " + profile.getCreationDate());
            }
        } else {
            tvRegistrationDate.setText("");
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate username
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            etUsername.setError("Username is required");
            isValid = false;
        }

        // Validate full name
        if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
            etFullName.setError("Full name is required");
            isValid = false;
        }

        // Validate email
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        return isValid;
    }

    private void updateProfile() {
        if (!validateForm()) {
            return;
        }

        showLoading("Updating profile...");
        submitProfileUpdate();
    }

    private void submitProfileUpdate() {
        AdminUpdateRequest updateDto = new AdminUpdateRequest();
        updateDto.setUsername(etUsername.getText().toString().trim());
        updateDto.setFullName(etFullName.getText().toString().trim());
        updateDto.setEmail(etEmail.getText().toString().trim());
        updateDto.setActive(switchActive.isChecked());

        editProfileApiService.updateAdminProfile(this, currentAdminId, updateDto, new BaseApiService.ApiCallback<Admin>() {
            @Override
            public void onSuccess(Admin updatedProfile) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(AdminEditProfileActivity.this, 
                        "Profile updated successfully!", Toast.LENGTH_LONG).show();
                    loadCurrentProfile();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    if (errorMessage != null) {
                        if (errorMessage.contains("Username is already registered")) {
                            etUsername.setError("Username is already registered");
                            Toast.makeText(AdminEditProfileActivity.this, "Username is already registered.", Toast.LENGTH_LONG).show();
                        } else if (errorMessage.contains("Email is already registered")) {
                            etEmail.setError("Email is already registered");
                            Toast.makeText(AdminEditProfileActivity.this, "Email is already registered.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AdminEditProfileActivity.this,
                                "Failed to update profile: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AdminEditProfileActivity.this,
                            "Failed to update profile.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void performLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    AuthApiService authApiService = new AuthApiService();
                    authApiService.logout(this, new AuthApiService.AuthCallback<LogoutResponse>() {
                        @Override
                        public void onSuccess(LogoutResponse response) {
                            runOnUiThread(() -> {
                                userManager.logout();
                                Toast.makeText(AdminEditProfileActivity.this, 
                                    "Logged out successfully", Toast.LENGTH_SHORT).show();
                                
                                // Navigate to login screen
                                Intent intent = new Intent(AdminEditProfileActivity.this, WelcomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        }

                        @Override
                        public void onError(String errorMessage, Throwable throwable) {
                            runOnUiThread(() -> {
                                userManager.logout();
                                Toast.makeText(AdminEditProfileActivity.this, 
                                    "Logout failed: " + errorMessage + ". Logging out locally.", Toast.LENGTH_LONG).show();

                                // Navigate to login screen
                                Intent intent = new Intent(AdminEditProfileActivity.this, WelcomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showChangePasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnOk).setOnClickListener(v -> {
            String currentPassword = ((android.widget.EditText) dialogView.findViewById(R.id.etCurrentPassword)).getText().toString();
            String newPassword = ((android.widget.EditText) dialogView.findViewById(R.id.etNewPassword)).getText().toString();
            String confirmPassword = ((android.widget.EditText) dialogView.findViewById(R.id.etConfirmPassword)).getText().toString();
            if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            ChangePasswordRequest req = new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);
            userApiService.changePassword(this, userManager.getCurrentUserId(), req, new BaseApiService.ApiCallback<String>() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(AdminEditProfileActivity.this, message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    });
                }
                @Override
                public void onError(String errorMessage, Throwable throwable) {
                    runOnUiThread(() -> Toast.makeText(AdminEditProfileActivity.this, "Failed: " + errorMessage, Toast.LENGTH_LONG).show());
                }
            });
        });
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 