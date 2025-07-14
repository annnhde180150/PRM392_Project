package com.example.homehelperfinder.ui.editProfile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.homehelperfinder.ui.base.BaseActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.utils.CircularImageUtils;
import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.HelperSkillResponse;
import com.example.homehelperfinder.data.model.response.HelperWorkAreaResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.helper.HelperApiService;
import com.example.homehelperfinder.ui.registerHelper.adapter.SkillAdapter;
import com.example.homehelperfinder.ui.registerHelper.adapter.WorkAreaAdapter;
import com.example.homehelperfinder.utils.FirebaseHelper;
import com.example.homehelperfinder.utils.UserManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class HelperEditProfileActivity extends BaseActivity {
    
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etBio;
    private ImageView ivProfilePicture, ivIdPreview, ivCvPreview;
    private ImageButton btnChangePicture;
    private Button btnConfirm, btnAddSkill, btnAddWorkArea, btnUploadId, btnUploadCV;
    private RecyclerView rvSkills, rvWorkAreas;
    
    private Uri profilePictureUri, idFileUri, cvFileUri;
    private String profilePictureUrl, idFileUrl, cvFileUrl;

    private HelperApiService helperApiService;
    
    private SkillAdapter skillAdapter;
    private WorkAreaAdapter workAreaAdapter;
    private List<HelperSkillResponse> skillList = new ArrayList<>();
    private List<HelperWorkAreaResponse> workAreaList = new ArrayList<>();
    
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> idFilePickerLauncher;
    private ActivityResultLauncher<Intent> cvFilePickerLauncher;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_helper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Initialize UserManager
        userManager = UserManager.getInstance(this);

        initViews();
        setupImagePickers();
        setupRecyclerViews();
        loadCurrentProfile();
    }

    private void initViews() {
        helperApiService = new HelperApiService();
        
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBio = findViewById(R.id.etBio);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        ivIdPreview = findViewById(R.id.ivIdPreview);
        ivCvPreview = findViewById(R.id.ivCvPreview);
        btnChangePicture = findViewById(R.id.btnChangePicture);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnAddSkill = findViewById(R.id.btnAddSkill);
        btnAddWorkArea = findViewById(R.id.btnAddWorkArea);
        btnUploadId = findViewById(R.id.btnUploadId);
        btnUploadCV = findViewById(R.id.btnUploadCV);
        rvSkills = findViewById(R.id.rvSkills);
        rvWorkAreas = findViewById(R.id.rvWorkAreas);

        btnChangePicture.setOnClickListener(v -> openImagePicker());
        btnConfirm.setOnClickListener(v -> updateProfile());
        btnAddSkill.setOnClickListener(v -> showAddSkillDialog());
        btnAddWorkArea.setOnClickListener(v -> showAddWorkAreaDialog());
        btnUploadId.setOnClickListener(v -> openIdFilePicker());
        btnUploadCV.setOnClickListener(v -> openCvFilePicker());
    }

    private void setupImagePickers() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        profilePictureUri = result.getData().getData();
                        if (profilePictureUri != null) {
                            CircularImageUtils.loadCircularImage(this, profilePictureUri, ivProfilePicture);
                        }
                    }
                }
        );

        idFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        idFileUri = result.getData().getData();
                        if (idFileUri != null) {
                            String mimeType = getContentResolver().getType(idFileUri);
                            if (mimeType != null && mimeType.startsWith("image/")) {
                                ivIdPreview.setVisibility(android.view.View.VISIBLE);
                                Glide.with(this).load(idFileUri).into(ivIdPreview);
                            } else {
                                ivIdPreview.setVisibility(android.view.View.GONE);
                            }
                        }
                    }
                }
        );

        cvFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        cvFileUri = result.getData().getData();
                        if (cvFileUri != null) {
                            String mimeType = getContentResolver().getType(cvFileUri);
                            if (mimeType != null && mimeType.startsWith("image/")) {
                                ivCvPreview.setVisibility(android.view.View.VISIBLE);
                                Glide.with(this).load(cvFileUri).into(ivCvPreview);
                            } else {
                                ivCvPreview.setVisibility(android.view.View.GONE);
                            }
                        }
                    }
                }
        );
    }

    private void setupRecyclerViews() {
        skillAdapter = new SkillAdapter(skillList);
        rvSkills.setLayoutManager(new LinearLayoutManager(this));
        rvSkills.setAdapter(skillAdapter);
        skillAdapter.setSkillActionListener(new SkillAdapter.SkillActionListener() {
            @Override
            public void onEdit(int position) {
                HelperSkillResponse skill = skillAdapter.getSkill(position);
                showAddSkillDialog(skill, position);
            }

            @Override
            public void onDelete(int position) {
                skillAdapter.removeSkill(position);
            }
        });

        workAreaAdapter = new WorkAreaAdapter(workAreaList);
        rvWorkAreas.setLayoutManager(new LinearLayoutManager(this));
        rvWorkAreas.setAdapter(workAreaAdapter);
        workAreaAdapter.setWorkAreaActionListener(new WorkAreaAdapter.WorkAreaActionListener() {
            @Override
            public void onEdit(int position) {
                HelperWorkAreaResponse area = workAreaAdapter.getWorkArea(position);
                showAddWorkAreaDialog(area, position);
            }

            @Override
            public void onDelete(int position) {
                workAreaAdapter.removeWorkArea(position);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Profile Picture"));
    }

    private void openIdFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        idFilePickerLauncher.launch(Intent.createChooser(intent, "Select ID Document"));
    }

    private void openCvFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        cvFilePickerLauncher.launch(Intent.createChooser(intent, "Select CV File"));
    }

    private void loadCurrentProfile() {
        showLoading("Loading profile...");
        
        // TODO: Get the current helper ID from SharedPreferences or AuthManager
        int currentHelperId = userManager.getCurrentUserId(); // You need to implement this method
        
        helperApiService.getHelperProfile(this, currentHelperId, new BaseApiService.ApiCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse profile) {
                runOnUiThread(() -> {
                    hideLoading();
                    populateFields(profile);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(HelperEditProfileActivity.this,
                        "Failed to load profile: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }


    private void populateFields(HelperResponse profile) {
        if (profile.getFullName() != null) {
            etFullName.setText(profile.getFullName());
        }
        if (profile.getEmail() != null) {
            etEmail.setText(profile.getEmail());
        }
        if (profile.getPhoneNumber() != null) {
            etPhoneNumber.setText(profile.getPhoneNumber());
        }
        if (profile.getBio() != null) {
            etBio.setText(profile.getBio());
        }
        
        if (profile.getProfilePictureUrl() != null) {
            CircularImageUtils.loadCircularImage(this, profile.getProfilePictureUrl(), ivProfilePicture);
        }

        // Load skills and work areas if available
        if (profile.getSkills() != null) {
            skillList.clear();
            skillList.addAll(profile.getSkills());
            skillAdapter.notifyDataSetChanged();
        }

        if (profile.getWorkAreas() != null) {
            workAreaList.clear();
            workAreaList.addAll(profile.getWorkAreas());
            workAreaAdapter.notifyDataSetChanged();
        }
    }

    private void showAddSkillDialog() {
        showAddSkillDialog(null, -1);
    }

    private void showAddSkillDialog(HelperSkillResponse skillToEdit, int position) {
        // Implement skill dialog similar to RegisterHelperActivity
        // This would show a dialog to add/edit skills
        Toast.makeText(this, "Add skill dialog - implement this", Toast.LENGTH_SHORT).show();
    }

    private void showAddWorkAreaDialog() {
        showAddWorkAreaDialog(null, -1);
    }

    private void showAddWorkAreaDialog(HelperWorkAreaResponse areaToEdit, int position) {
        // Implement work area dialog similar to RegisterHelperActivity
        // This would show a dialog to add/edit work areas
        Toast.makeText(this, "Add work area dialog - implement this", Toast.LENGTH_SHORT).show();
    }

    private void updateProfile() {
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        if (!validateForm()) {
            return;
        }

        showLoading("Updating profile...");

        // Upload files if changed
        if (profilePictureUri != null) {
            firebaseHelper.uploadProfilePicture(profilePictureUri, uploadedUrl -> {
                if (uploadedUrl != null) {
                    profilePictureUrl = uploadedUrl;
                    uploadDocumentsAndSubmit();
                } else {
                    hideLoading();
                    Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            uploadDocumentsAndSubmit();
        }
    }

    private void uploadDocumentsAndSubmit() {
        List<FirebaseHelper.FileUploadTask> filesToUpload = new ArrayList<>();
        
        if (idFileUri != null) {
            filesToUpload.add(new FirebaseHelper.FileUploadTask(idFileUri, FirebaseHelper.FileType.ID));
        }
        if (cvFileUri != null) {
            filesToUpload.add(new FirebaseHelper.FileUploadTask(cvFileUri, FirebaseHelper.FileType.CV));
        }

        if (filesToUpload.isEmpty()) {
            submitProfileUpdate();
            return;
        }

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.uploadMultipleFiles(filesToUpload, results -> {
            // Extract URLs from results
            String idUrl = results.get("id");
            String cvUrl = results.get("cv");
            
            // Check for errors
            if (results.containsKey("id_error")) {
                Toast.makeText(this, "Failed to upload ID: " + results.get("id_error"), Toast.LENGTH_LONG).show();
            }
            if (results.containsKey("cv_error")) {
                Toast.makeText(this, "Failed to upload CV: " + results.get("cv_error"), Toast.LENGTH_LONG).show();
            }
            
            // If any upload failed, abort update
            if (results.containsKey("id_error") || results.containsKey("cv_error")) {
                hideLoading();
                Toast.makeText(this, "File upload failed. Profile update aborted.", Toast.LENGTH_LONG).show();
                return;
            }
            
            // All uploads successful, update URLs and proceed
            if (idUrl != null) idFileUrl = idUrl;
            if (cvUrl != null) cvFileUrl = cvUrl;
            
            submitProfileUpdate();
        });
    }




    private void submitProfileUpdate() {
        HelperUpdateRequest updateDto = new HelperUpdateRequest();
        updateDto.setFullName(etFullName.getText().toString().trim());
        updateDto.setEmail(etEmail.getText().toString().trim());
        updateDto.setPhoneNumber(etPhoneNumber.getText().toString().trim());
        updateDto.setBio(etBio.getText().toString().trim());
        
        if (profilePictureUrl != null) {
            updateDto.setProfilePictureUrl(profilePictureUrl);
        }
        if (idFileUrl != null) {
            updateDto.setIdDocumentUrl(idFileUrl);
        }
        if (cvFileUrl != null) {
            updateDto.setCvDocumentUrl(cvFileUrl);
        }

        // Add skills and work areas
        updateDto.setSkills(skillList);
        updateDto.setWorkAreas(workAreaList);

        helperApiService.updateHelperProfile(this, updateDto, new BaseApiService.ApiCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse updatedProfile) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(HelperEditProfileActivity.this,
                        "Profile updated successfully!", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(HelperEditProfileActivity.this,
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
        }

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            valid = false;
        }

        String phone = etPhoneNumber.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 8) {
            etPhoneNumber.setError("Valid phone number is required");
            valid = false;
        }

        if (skillList.isEmpty()) {
            Toast.makeText(this, "Please add at least one skill", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (workAreaList.isEmpty()) {
            Toast.makeText(this, "Please add at least one work area", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }


} 