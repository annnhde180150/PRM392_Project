package com.example.homehelperfinder.ui.editProfile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.homehelperfinder.ui.base.BaseActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.user.UserApiService;
import com.example.homehelperfinder.data.model.response.UserResponse;
import com.example.homehelperfinder.utils.FirebaseHelper;
import com.example.homehelperfinder.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.function.Consumer;
import com.example.homehelperfinder.utils.UserManager;
import com.example.homehelperfinder.utils.Logger;

public class CustomerEditProfileActivity extends BaseActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Toolbar toolbar;

    private TextInputEditText etFullName, etEmail, etPhoneNumber, etAddress;
    private ImageView ivProfilePicture;
    private ImageButton btnChangePicture;
    private Button btnConfirm;
    private Uri profilePictureUri;
    private String profilePictureUrl;

    private int currentUserId;
    private UserApiService userApiService;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userManager = UserManager.getInstance(this);

        initViews();
        setupImagePicker();
        loadCurrentProfile();
    }
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Profile");
        }
    }
    private void initViews() {
        userApiService = new UserApiService();

        setupToolbar();
        
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnChangePicture = findViewById(R.id.btnChangePicture);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnChangePicture.setOnClickListener(v -> openImagePicker());
        btnConfirm.setOnClickListener(v -> updateProfile());
    }

    private void setupImagePicker() {
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        profilePictureUri = result.getData().getData();
                        if (profilePictureUri != null) {
                            Glide.with(this).load(profilePictureUri).circleCrop().into(ivProfilePicture);
                        }
                    }
                }
        );

        this.imagePickerLauncher = imagePickerLauncher;
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Profile Picture"));
    }

    private void loadCurrentProfile() {
        showLoading("Loading profile...");

        currentUserId = userManager.getCurrentUserId();
        userApiService.getUserProfile(this, currentUserId, new BaseApiService.ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse profile) {
                runOnUiThread(() -> {
                    hideLoading();
                    populateFields(profile);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(CustomerEditProfileActivity.this, 
                        "Failed to load profile: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void populateFields(UserResponse profile) {
        if (profile.getFullName() != null) {
            etFullName.setText(profile.getFullName());
        }
        if (profile.getEmail() != null) {
            etEmail.setText(profile.getEmail());
        }
        if (profile.getPhoneNumber() != null) {
            etPhoneNumber.setText(profile.getPhoneNumber());
        }
        
        if (profile.getProfilePictureUrl() != null) {
            Glide.with(this).load(profile.getProfilePictureUrl()).circleCrop().into(ivProfilePicture);
        }
    }

    private void updateProfile() {
        if (!validateForm()) {
            return;
        }

        showLoading("Updating profile...");

        // If profile picture was changed, upload it first
        if (profilePictureUri != null) {
            uploadProfilePicture(profilePictureUri, uploadedUrl -> {
                if (uploadedUrl != null) {
                    profilePictureUrl = uploadedUrl;
                    submitProfileUpdate();
                } else {
                    hideLoading();
                    Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            submitProfileUpdate();
        }
    }

    private void uploadProfilePicture(Uri imageUri, Consumer<String> onComplete) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.uploadProfilePicture(imageUri, onComplete);
    }

    private void submitProfileUpdate() {
        UserUpdateRequest updateDto = new UserUpdateRequest();
        updateDto.setFullName(etFullName.getText().toString().trim());
        updateDto.setEmail(etEmail.getText().toString().trim());
        updateDto.setPhoneNumber(etPhoneNumber.getText().toString().trim());
        if (profilePictureUrl != null) {
            updateDto.setProfilePictureUrl(profilePictureUrl);
        }

        userApiService.updateUserProfile(this, currentUserId, updateDto, new BaseApiService.ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse updatedProfile) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(CustomerEditProfileActivity.this, 
                        "Profile updated successfully!", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(CustomerEditProfileActivity.this, 
                        "Failed to update profile: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String fullName = etFullName.getText().toString().trim();
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            valid = false;
        }else if (fullName.length() < 2) {
            etFullName.setError("Full name must be at least 2 characters");
            valid = false;
        }

        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            valid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email address");
            valid = false;
        }

        String phone = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etPhoneNumber.setError("Contact number is required");
            valid = false;
        } else if (!ValidationUtils.isValidPhone(phone)) {
            etPhoneNumber.setError("Please enter a valid phone number");
            valid = false;
        }
        return valid;
    }




}