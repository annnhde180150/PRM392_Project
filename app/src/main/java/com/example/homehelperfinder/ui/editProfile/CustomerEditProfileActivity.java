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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.homehelperfinder.BuildConfig;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;
import com.example.homehelperfinder.data.remote.address.AddressApiService;
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
import com.example.homehelperfinder.ui.editProfile.adapter.UserAddressAdapter;
import com.example.homehelperfinder.utils.FirebaseHelper;
import com.example.homehelperfinder.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.function.Consumer;
import com.example.homehelperfinder.utils.UserManager;
import com.example.homehelperfinder.utils.Logger;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.app.AlertDialog;
import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.request.UserAddressCreateRequest;
import android.view.MenuItem;

public class CustomerEditProfileActivity extends BaseActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Toolbar toolbar;

    private TextInputEditText etFullName, etEmail, etPhoneNumber;
    private ImageView ivProfilePicture;
    private ImageButton btnChangePicture;
    private Button btnConfirm;
    private Uri profilePictureUri;
    private String profilePictureUrl;
    private TextView tvUserId, tvRegistrationDate, tvUserFullName, tvDefaultAddressLabel;
    private ImageButton btnEditProfile;
    private RecyclerView rvAddresses;
    private UserAddressAdapter addressAdapter;
    private List<UserAddressResponse> addressList = new ArrayList<>();
    private Button btnAddAddress;

    private int currentUserId;
    private UserApiService userApiService;
    private AddressApiService addressApiService;
    private UserManager userManager;
    private static final int MAP_PICKER_REQUEST_CODE = 1002;

    private TextInputEditText dialogEtLine1, dialogEtLine2, dialogEtWard, dialogEtDistrict, dialogEtCity, dialogEtFullAddress;
    private AlertDialog editAddressDialog = null;
    private boolean isEditingAddress = false;

    private Integer pendingDefaultAddressId = null;


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
        setEditMode(false);

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
        addressApiService = new AddressApiService();
        setupToolbar();
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnChangePicture = findViewById(R.id.btnChangePicture);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvUserId = findViewById(R.id.tvUserId);
        tvRegistrationDate = findViewById(R.id.tvRegistrationDate);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        rvAddresses = findViewById(R.id.rvAddresses);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        tvDefaultAddressLabel = findViewById(R.id.tvDefaultAddressLabel);
        setupAddressAdapter();

        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.LocationPickerActivity.class);
            startActivityForResult(intent, MAP_PICKER_REQUEST_CODE);
        });
        btnChangePicture.setOnClickListener(v -> openImagePicker());
        btnConfirm.setOnClickListener(v -> {
            updateProfile();
            setEditMode(false);
        });
        btnEditProfile.setOnClickListener(v -> {
            setEditMode(!isEditMode);
        });
    }

    private boolean isEditMode = false;
    private void setEditMode(boolean editable) {
        isEditMode = editable;
        etFullName.setEnabled(editable);
        etEmail.setEnabled(editable);
        etPhoneNumber.setEnabled(editable);
        btnChangePicture.setEnabled(editable);
        btnChangePicture.setAlpha(editable ? 1f : 0.5f);
        btnConfirm.setVisibility(editable ? View.VISIBLE : View.GONE);
        if (tvDefaultAddressLabel != null) {
            tvDefaultAddressLabel.setVisibility(editable ? View.VISIBLE : View.GONE);
        }
        if (addressAdapter != null) {
            addressAdapter.setShowDefaultSelector(editable);
            addressAdapter.setShowEditDeleteButtons(editable);
        }
        if (btnAddAddress != null) {
            btnAddAddress.setVisibility(editable ? View.VISIBLE : View.GONE);
        }
        if (editable) {         //Enter edit mode
            btnEditProfile.setImageResource(R.drawable.ic_close);
            if (addressAdapter != null) {
                pendingDefaultAddressId = addressAdapter.defaultAddressId;
            }
        } else {
            btnEditProfile.setImageResource(R.drawable.ic_edit);
            pendingDefaultAddressId = null;
        }
    }


    private void setupAddressAdapter(){
        addressAdapter = new UserAddressAdapter(addressList, new UserAddressAdapter.OnAddressActionListener() {
            @Override
            public void onEdit(int position, UserAddressResponse address) {
                showAddressDialog(address);
            }
            @Override
            public void onDelete(int position, UserAddressResponse address) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerEditProfileActivity.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this address?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    addressApiService.deleteUserAddress(CustomerEditProfileActivity.this, address.getAddressId(), new BaseApiService.ApiCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(CustomerEditProfileActivity.this, "Address deleted", Toast.LENGTH_SHORT).show();
                                loadCurrentProfile();
                            });
                        }
                        @Override
                        public void onError(String errorMessage, Throwable throwable) {
                            runOnUiThread(() -> Toast.makeText(CustomerEditProfileActivity.this, "Failed to delete address", Toast.LENGTH_SHORT).show());
                        }
                    });
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
            @Override
            public void onDefaultSelected(int position, UserAddressResponse address) {
                pendingDefaultAddressId = address.getAddressId();
                if (addressAdapter != null) {
                    addressAdapter.setDefaultAddressId(address.getAddressId());
                }
            }
        });
        rvAddresses.setAdapter(addressAdapter);
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));

    }
    private void setupImagePicker() {

        this.imagePickerLauncher = registerForActivityResult(
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
                    if (addressAdapter != null && profile.getDefaultAddressId() != null) {
                        addressAdapter.setDefaultAddressId(profile.getDefaultAddressId());
                    }
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

        addressApiService.getUserAddressesByUserId(this, currentUserId, new BaseApiService.ApiCallback<List<UserAddressResponse>>() {
            @Override
            public void onSuccess(List<UserAddressResponse> address) {
                runOnUiThread(() -> populateAddresses(address));
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Logger.e("CustomerEditProfileActivity", "Failed to load address: " + errorMessage);
                });
            }
        });

        setEditMode(false);


    }

    private void populateFields(UserResponse profile) {
        if (profile.getFullName() != null) {
            etFullName.setText(profile.getFullName());
            tvUserFullName.setText(profile.getFullName());
        }
        if (profile.getEmail() != null) {
            etEmail.setText(profile.getEmail());
        }
        if (profile.getPhoneNumber() != null) {
            etPhoneNumber.setText(profile.getPhoneNumber());
        }
        
        if (profile.getProfilePictureUrl() != null) {
            profilePictureUrl = profile.getProfilePictureUrl();
            Glide.with(this).load(profile.getProfilePictureUrl()).circleCrop().into(ivProfilePicture);
        }
        if (profile.getId() != 0) {
            tvUserId.setText("ID: " + profile.getId());
        }

        
        if (profile.getRegistrationDate() != null) {
            java.util.Date regDate = profile.getRegistrationDate();
            java.text.SimpleDateFormat monthYearFormat = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault());
            String monthYear = monthYearFormat.format(regDate);
            tvRegistrationDate.setText("Joined " + monthYear);
        } else {
            tvRegistrationDate.setText("");
        }
        if (addressAdapter != null && profile.getDefaultAddressId() != null) {
            addressAdapter.setDefaultAddressId(profile.getDefaultAddressId());
        }
    }

    private void populateAddresses(List<UserAddressResponse> addresses) {
        addressList.clear();
        if (addresses != null) addressList.addAll(addresses);
        addressAdapter.notifyDataSetChanged();
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
        if (pendingDefaultAddressId != null) {
            updateDto.setDefaultAddressId(pendingDefaultAddressId);
        }
        userApiService.updateUserProfile(this, currentUserId, updateDto, new BaseApiService.ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse updatedProfile) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(CustomerEditProfileActivity.this, 
                        "Profile updated successfully!", Toast.LENGTH_LONG).show();
                    loadCurrentProfile();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    if (errorMessage != null) {
                        if (errorMessage.contains("Email is already registered")) {
                            etEmail.setError("Email is already registered");
                            Toast.makeText(CustomerEditProfileActivity.this, "Email is already registered.", Toast.LENGTH_LONG).show();
                        } else if (errorMessage.contains("Phone is already registered")) {
                            etPhoneNumber.setError("Phone number is already registered");
                            Toast.makeText(CustomerEditProfileActivity.this, "Phone number is already registered.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CustomerEditProfileActivity.this, 
                                "Failed to update profile: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CustomerEditProfileActivity.this, 
                            "Failed to update profile.", Toast.LENGTH_LONG).show();
                    }
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

    private void showAddressDialog(UserAddressResponse address) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_address, null);
        dialogEtLine1 = dialogView.findViewById(R.id.etLine1);
        dialogEtLine2 = dialogView.findViewById(R.id.etLine2);
        dialogEtWard = dialogView.findViewById(R.id.etWard);
        dialogEtDistrict = dialogView.findViewById(R.id.etDistrict);
        dialogEtCity = dialogView.findViewById(R.id.etCity);
        dialogEtFullAddress = dialogView.findViewById(R.id.etFullAddress);


        Button btnPickOnMap = dialogView.findViewById(R.id.btnAutoCompleteAddress);
        if (btnPickOnMap != null) {
            btnPickOnMap.setOnClickListener(v -> {
                Intent intent = new Intent(this, com.example.homehelperfinder.ui.LocationPickerActivity.class);
                startActivityForResult(intent, MAP_PICKER_REQUEST_CODE);
            });
        }

        if (address != null) {
            dialogEtLine1.setText(address.getAddressLine1());
            dialogEtLine2.setText(address.getAddressLine2());
            dialogEtWard.setText(address.getWard());
            dialogEtDistrict.setText(address.getDistrict());
            dialogEtCity.setText(address.getCity());
            dialogEtFullAddress.setText(address.getFullAddress());
        }

        isEditingAddress = true;
        editAddressDialog = new AlertDialog.Builder(this)
            .setTitle("Edit Address")
            .setView(dialogView)
            .setPositiveButton("Save", (dialog, which) -> {
                String line1 = dialogEtLine1.getText().toString();
                String line2 = dialogEtLine2.getText().toString();
                String ward = dialogEtWard.getText().toString();
                String district = dialogEtDistrict.getText().toString();
                String city = dialogEtCity.getText().toString();
                String fullAddress = dialogEtFullAddress.getText().toString();

                // Edit existing address
                UserAddressUpdateRequest req = new UserAddressUpdateRequest();
                req.setUserId(currentUserId);
                req.setAddressLine1(line1);
                req.setAddressLine2(line2);
                req.setWard(ward);
                req.setDistrict(district);
                req.setCity(city);
                req.setFullAddress(fullAddress);
                addressApiService.updateUserAddress(this, address.getAddressId(), req, new BaseApiService.ApiCallback<>() {
                    @Override
                    public void onSuccess(UserAddressResponse result) {
                        runOnUiThread(() -> {
                            Toast.makeText(CustomerEditProfileActivity.this, "Address updated", Toast.LENGTH_SHORT).show();
                            loadCurrentProfile();
                        });
                    }

                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        runOnUiThread(() -> Toast.makeText(CustomerEditProfileActivity.this, "Failed to update address", Toast.LENGTH_SHORT).show());
                    }
                });

            })
            .setNegativeButton("Cancel", null)
            .setOnDismissListener(dialog -> {
                isEditingAddress = false;
                editAddressDialog = null;
            })
            .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                String city = data.getStringExtra("city");
                String district = data.getStringExtra("district");
                String ward = data.getStringExtra("ward");
                String line1 = data.getStringExtra("line1");
                String fullAddress = data.getStringExtra("fullAddress");

                if (isEditingAddress && editAddressDialog != null) {
                    // Populate the edit dialog fields
                    if (dialogEtLine1 != null) dialogEtLine1.setText(line1);
                    if (dialogEtWard != null) dialogEtWard.setText(ward);
                    if (dialogEtDistrict != null) dialogEtDistrict.setText(district);
                    if (dialogEtCity != null) dialogEtCity.setText(city);
                    if (dialogEtFullAddress != null) dialogEtFullAddress.setText(fullAddress);
                } else {
                    // Show add confirmation dialog (add address flow)
                    StringBuilder message = new StringBuilder();
                    if (fullAddress != null && !fullAddress.isEmpty()) {
                        message.append(fullAddress);
                    } else {
                        if (line1 != null && !line1.isEmpty()) message.append(line1).append("\n");
                        if (ward != null && !ward.isEmpty()) message.append(ward).append("\n");
                        if (district != null && !district.isEmpty()) message.append(district).append("\n");
                        if (city != null && !city.isEmpty()) message.append(city);
                    }
                    new android.app.AlertDialog.Builder(this)
                        .setTitle("Confirm Address")
                        .setMessage(message.toString())
                        .setPositiveButton("Save", (dialog, which) -> {
                            UserAddressCreateRequest req = new UserAddressCreateRequest();
                            req.setUserId(currentUserId);
                            req.setAddressLine1(line1);
                            req.setWard(ward);
                            req.setDistrict(district);
                            req.setCity(city);
                            req.setFullAddress(fullAddress);
                            addressApiService.createUserAddress(this, req, new BaseApiService.ApiCallback<>() {
                                @Override
                                public void onSuccess(Void result) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(CustomerEditProfileActivity.this, "Address added", Toast.LENGTH_SHORT).show();
                                        loadCurrentProfile();
                                    });
                                }
                                @Override
                                public void onError(String errorMessage, Throwable throwable) {
                                    runOnUiThread(() -> Toast.makeText(CustomerEditProfileActivity.this, "Failed to add address", Toast.LENGTH_SHORT).show());
                                }
                            });
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}