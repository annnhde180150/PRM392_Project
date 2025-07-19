package com.example.homehelperfinder.ui.editProfile;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.homehelperfinder.data.remote.helper.HelperApiService;
import com.example.homehelperfinder.data.remote.service.ServiceApiService;
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
import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.ui.registerHelper.adapter.SkillAdapter;
import com.example.homehelperfinder.ui.registerHelper.adapter.WorkAreaAdapter;
import com.example.homehelperfinder.utils.FirebaseHelper;
import com.example.homehelperfinder.utils.UserManager;
import com.example.homehelperfinder.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import com.example.homehelperfinder.data.model.response.HelperDocumentResponse;
import com.example.homehelperfinder.data.model.request.HelperSkillRequest;
import com.example.homehelperfinder.data.model.request.HelperWorkAreaRequest;

public class HelperEditProfileActivity extends BaseActivity {
    
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etBio;
    private ImageView ivProfilePicture, ivIdPreview, ivCvPreview;
    private ImageButton btnChangePicture, btnEditProfile;
    private Button btnConfirm, btnAddSkill, btnAddWorkArea, btnUploadId, btnUploadCV;
    private RecyclerView rvSkills, rvWorkAreas;
    private TextView tvHelperId, tvRegistrationDate, tvHelperFullName;
    private Toolbar toolbar;
    
    private Uri profilePictureUri, idFileUri, cvFileUri;
    private String profilePictureUrl, idFileUrl, cvFileUrl;

    private HelperApiService helperApiService;
    private ServiceApiService serviceApiService;
    
    private SkillAdapter skillAdapter;
    private WorkAreaAdapter workAreaAdapter;
    private List<HelperSkillResponse> skillList = new ArrayList<>();
    private List<HelperWorkAreaResponse> workAreaList = new ArrayList<>();
    private List<ServiceResponse> serviceList = new ArrayList<>();
    
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> idFilePickerLauncher;
    private ActivityResultLauncher<Intent> cvFilePickerLauncher;
    private ActivityResultLauncher<Intent> locationPickerLauncher;

    private UserManager userManager;
    private boolean isEditMode = false;
    private int currentHelperId;

    // Dialog fields
    private AutoCompleteTextView actvService;
    private TextInputEditText etYearsOfExperience, etCity, etDistrict, etWard, etLatitude, etLongitude, etRadiusKm;
    private CheckBox cbIsPrimarySkill;
    private TextInputLayout tilService, tilYears;
    private Button btnAdd, btnCancel, btnPickLocation;
    private TextView tvSelectedLocation;

    private boolean profileLoaded = false;

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
        setupLocationPicker();
        setupRecyclerViews();
        fetchServices();
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
        helperApiService = new HelperApiService();
        serviceApiService = new ServiceApiService();
        setupToolbar();
        
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
        tvHelperId = findViewById(R.id.tvHelperId);
        tvRegistrationDate = findViewById(R.id.tvRegistrationDate);
        tvHelperFullName = findViewById(R.id.tvHelperFullName);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnChangePicture.setOnClickListener(v -> openImagePicker());
        btnConfirm.setOnClickListener(v -> {
            updateProfile();
            setEditMode(false);
        });
        btnAddSkill.setOnClickListener(v -> showAddSkillDialog());
        btnAddWorkArea.setOnClickListener(v -> showAddWorkAreaDialog());
        btnUploadId.setOnClickListener(v -> openIdFilePicker());
        btnUploadCV.setOnClickListener(v -> openCvFilePicker());
        btnEditProfile.setOnClickListener(v -> setEditMode(!isEditMode));
    }

    private void setupLocationPicker() {
        locationPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        double latitude = result.getData().getDoubleExtra("latitude", 0.0);
                        double longitude = result.getData().getDoubleExtra("longitude", 0.0);
                        String city = result.getData().getStringExtra("city");
                        String district = result.getData().getStringExtra("district");
                        String ward = result.getData().getStringExtra("ward");
                        
                        if (etCity != null) etCity.setText(city);
                        if (etDistrict != null) etDistrict.setText(district);
                        if (etWard != null) etWard.setText(ward);
                        if (etLatitude != null) etLatitude.setText(String.valueOf(latitude));
                        if (etLongitude != null) etLongitude.setText(String.valueOf(longitude));
                        if (tvSelectedLocation != null) {
                            tvSelectedLocation.setText("Lat: " + latitude + ", Lng: " + longitude);
                        }
                    }
                }
        );
    }

    private void fetchServices() {
        try {
            serviceApiService.getActiveServices(this, new BaseApiService.ApiCallback<List<ServiceResponse>>() {
                @Override
                public void onSuccess(List<ServiceResponse> services) {
                    runOnUiThread(() -> {
                        serviceList.clear();
                        serviceList.addAll(services);
                        if (profileLoaded && currentHelperId > 0) {
                            loadCurrentProfile();
                        }
                    });
                }

                @Override
                public void onError(String errorMessage, Throwable throwable) {
                    runOnUiThread(() -> {
                        Toast.makeText(HelperEditProfileActivity.this, 
                            "Failed to load services: " + errorMessage, Toast.LENGTH_LONG).show();
                    });
                }
            });
        } catch (Exception ex) {
            runOnUiThread(() -> {
                Toast.makeText(HelperEditProfileActivity.this, 
                    "Error fetching services: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            });
            ex.printStackTrace();
        }
    }

    private void setEditMode(boolean editable) {
        isEditMode = editable;
        etFullName.setEnabled(editable);
        etEmail.setEnabled(editable);
        etPhoneNumber.setEnabled(editable);
        etBio.setEnabled(editable);
        btnChangePicture.setEnabled(editable);
        btnChangePicture.setAlpha(editable ? 1f : 0.5f);
        btnConfirm.setVisibility(editable ? View.VISIBLE : View.GONE);
        btnAddSkill.setVisibility(editable ? View.VISIBLE : View.GONE);
        btnAddWorkArea.setVisibility(editable ? View.VISIBLE : View.GONE);
        btnUploadId.setVisibility(editable ? View.VISIBLE : View.GONE);
        btnUploadCV.setVisibility(editable ? View.VISIBLE : View.GONE);
        
        if (skillAdapter != null) {
            skillAdapter.setShowEditDeleteButtons(editable);
        }
        if (workAreaAdapter != null) {
            workAreaAdapter.setShowEditDeleteButtons(editable);
        }
        
        if (editable) {
            btnEditProfile.setImageResource(R.drawable.ic_close);
        } else {
            btnEditProfile.setImageResource(R.drawable.ic_edit);
        }
    }

    private void setupImagePickers() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        profilePictureUri = result.getData().getData();
                        if (profilePictureUri != null) {
                            CircularImageUtils.loadCircularImage(this, String.valueOf(profilePictureUri), ivProfilePicture);
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
        
        currentHelperId = userManager.getCurrentUserId();
        
        helperApiService.getHelperProfile(this, currentHelperId, new BaseApiService.ApiCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse profile) {
                runOnUiThread(() -> {
                    hideLoading();
                    populateFields(profile);
                    profileLoaded = true; // Mark profile as loaded
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
            tvHelperFullName.setText(profile.getFullName());
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
            profilePictureUrl = profile.getProfilePictureUrl();
            CircularImageUtils.loadCircularImage(this, profile.getProfilePictureUrl(), ivProfilePicture);
        }

        if (profile.getId() != 0) {
            tvHelperId.setText("ID: " + profile.getId());
        }

        if (profile.getRegistrationDate() != null) {
            java.util.Date regDate = profile.getRegistrationDate();
            java.text.SimpleDateFormat monthYearFormat = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault());
            String monthYear = monthYearFormat.format(regDate);
            tvRegistrationDate.setText("Joined " + monthYear);
        } else {
            tvRegistrationDate.setText("");
        }

        // Load skills and work areas if available
        if (profile.getSkills() != null) {
            skillList.clear();
            // Map serviceId to serviceName for each skill
            for (HelperSkillResponse skill : profile.getSkills()) {
                if (skill.getServiceId() != null) {
                    String serviceName = getServiceNameById(skill.getServiceId());
                    skill.setServiceName(serviceName);
                }
                skillList.add(skill);
            }
            skillAdapter.notifyDataSetChanged();
        }

        if (profile.getWorkAreas() != null) {
            workAreaList.clear();
            workAreaList.addAll(profile.getWorkAreas());
            workAreaAdapter.notifyDataSetChanged();
        }

        // Load documents if available
        if (profile.getDocuments() != null) {
            for (HelperDocumentResponse document : profile.getDocuments()) {
                if ("ID".equals(document.getDocumentType())) {
                    // Load ID document preview
                    if (document.getDocumentUrl() != null) {
                        ivIdPreview.setVisibility(View.VISIBLE);
                        Glide.with(this).load(document.getDocumentUrl()).into(ivIdPreview);
                    }
                } else if ("CV".equals(document.getDocumentType())) {
                    // Load CV document preview
                    if (document.getDocumentUrl() != null) {
                        ivCvPreview.setVisibility(View.VISIBLE);
                        Glide.with(this).load(document.getDocumentUrl()).into(ivCvPreview);
                    }
                }
            }
        }
    }

    private String getServiceNameById(Integer serviceId) {
        for (ServiceResponse service : serviceList) {
            if (service.getServiceId() == serviceId) {
                return service.getServiceName();
            }
        }
        return "Unknown Service"; // Fallback if service not found
    }

    private void initSkillDialogFields(View dialogView) {
        actvService = dialogView.findViewById(R.id.actvService);
        etYearsOfExperience = dialogView.findViewById(R.id.etYearsOfExperience);
        cbIsPrimarySkill = dialogView.findViewById(R.id.cbIsPrimarySkill);
        btnAdd = dialogView.findViewById(R.id.btnAdd);
        btnCancel = dialogView.findViewById(R.id.btnCancel);

        tilService = dialogView.findViewById(R.id.tilService);
        tilYears = dialogView.findViewById(R.id.tilYears);
    }

    private void showAddSkillDialog() {
        showAddSkillDialog(null, -1);
    }

    private void showAddSkillDialog(HelperSkillResponse skillToEdit, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_skill, null);

        initSkillDialogFields(dialogView);

        List<String> serviceNames = new ArrayList<>();
        for (ServiceResponse service : serviceList) {
            serviceNames.add(service.getServiceName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, serviceNames);
        actvService.setAdapter(adapter);

        // If editing, pre-fill fields
        if (skillToEdit != null) {
            // Use serviceName if available, otherwise try to map from serviceId
            String serviceName = skillToEdit.getServiceName();
            if (serviceName == null || serviceName.isEmpty()) {
                serviceName = getServiceNameById(skillToEdit.getServiceId());
            }
            actvService.setText(serviceName, false);
            etYearsOfExperience.setText(skillToEdit.getYearsOfExperience() == null ? "" : String.valueOf(skillToEdit.getYearsOfExperience()));
            cbIsPrimarySkill.setChecked(skillToEdit.isPrimarySkill());
            btnAdd.setText("Update");
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnAdd.setOnClickListener(v -> handleAddSkillClick(dialog, skillToEdit, position));
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void handleAddSkillClick(AlertDialog dialog, HelperSkillResponse skillToEdit, int position) {
        boolean valid = true;
        String selectedService = actvService.getText().toString().trim();
        String yearsStr = etYearsOfExperience.getText().toString().trim();

        if (tilService != null) tilService.setError(null);
        if (tilYears != null) tilYears.setError(null);

        if (selectedService.isEmpty()) {
            if (tilService != null) tilService.setError("Service is required");
            valid = false;
        }

        Integer years = null;
        if (yearsStr.isEmpty()) {
            if (tilYears != null) tilYears.setError("Years of experience is required");
            valid = false;
        } else {
            try {
                years = Integer.parseInt(yearsStr);
                if (years <= 0 || years >= 100) {
                    if (tilYears != null)
                        tilYears.setError("Years of experience must be between 1 and 99");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                if (tilYears != null) tilYears.setError("Enter a valid number");
                valid = false;
            }
        }

        if (!valid) return;

        boolean isPrimary = cbIsPrimarySkill.isChecked();
        
        // Find the serviceId for the selected service name
        Integer serviceId = getServiceIdByName(selectedService);
        
        if (skillToEdit != null && position >= 0) {
            // Update existing skill - use Response model
            HelperSkillResponse updatedSkill = new HelperSkillResponse();
            updatedSkill.setHelperSkillId(skillToEdit.getHelperSkillId());
            updatedSkill.setServiceId(serviceId);
            updatedSkill.setServiceName(selectedService);
            updatedSkill.setYearsOfExperience(years);
            updatedSkill.setPrimarySkill(isPrimary);
            skillAdapter.updateSkill(position, updatedSkill);
        } else {
            // Create new skill - use Request model for API, Response model for adapter
            HelperSkillRequest requestSkill = new HelperSkillRequest();
            requestSkill.setServiceId(serviceId != null ? serviceId : 0);
            requestSkill.setYearsOfExperience(years != null ? years : 0);
            requestSkill.setPrimarySkill(isPrimary);
            
            // Convert to Response model for the adapter
            HelperSkillResponse responseSkill = new HelperSkillResponse();
            responseSkill.setServiceId(serviceId);
            responseSkill.setServiceName(selectedService);
            responseSkill.setYearsOfExperience(years);
            responseSkill.setPrimarySkill(isPrimary);
            skillAdapter.addSkill(responseSkill);
        }

        dialog.dismiss();
    }

    private Integer getServiceIdByName(String serviceName) {
        for (ServiceResponse service : serviceList) {
            if (service.getServiceName().equals(serviceName)) {
                return service.getServiceId();
            }
        }
        return null; 
    }

    private void initWorkAreaDialogFields(View dialogView) {
        etCity = dialogView.findViewById(R.id.etCity);
        etDistrict = dialogView.findViewById(R.id.etDistrict);
        etWard = dialogView.findViewById(R.id.etWard);
        etLatitude = dialogView.findViewById(R.id.etLatitude);
        etLongitude = dialogView.findViewById(R.id.etLongitude);
        etRadiusKm = dialogView.findViewById(R.id.etRadiusKm);
        btnAdd = dialogView.findViewById(R.id.btnAdd);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnPickLocation = dialogView.findViewById(R.id.btnPickLocation);
        tvSelectedLocation = dialogView.findViewById(R.id.tvSelectedLocation);
    }

    private void showAddWorkAreaDialog() {
        showAddWorkAreaDialog(null, -1);
    }

    private void showAddWorkAreaDialog(HelperWorkAreaResponse areaToEdit, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_work_area, null);

        initWorkAreaDialogFields(dialogView);

        btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.LocationPickerActivity.class);
            locationPickerLauncher.launch(intent);
        });

        // If editing, pre-fill fields
        if (areaToEdit != null) {
            etCity.setText(areaToEdit.getCity());
            etDistrict.setText(areaToEdit.getDistrict());
            etWard.setText(areaToEdit.getWard());
            etLatitude.setText(areaToEdit.getLatitude() == null ? "" : String.valueOf(areaToEdit.getLatitude()));
            etLongitude.setText(areaToEdit.getLongitude() == null ? "" : String.valueOf(areaToEdit.getLongitude()));
            etRadiusKm.setText(areaToEdit.getRadiusKm() == null ? "" : String.valueOf(areaToEdit.getRadiusKm()));
            tvSelectedLocation.setText((areaToEdit.getLatitude() != null && areaToEdit.getLongitude() != null) ?
                    ("Lat: " + areaToEdit.getLatitude() + ", Lng: " + areaToEdit.getLongitude()) : "No location selected");
            btnAdd.setText("Update");
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnAdd.setOnClickListener(v -> handleAddWorkArea(areaToEdit, position, dialog));
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void handleAddWorkArea(HelperWorkAreaResponse areaToEdit, int position, AlertDialog dialog) {
        String city = etCity.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        String ward = etWard.getText().toString().trim();
        String latStr = etLatitude.getText().toString().trim();
        String lngStr = etLongitude.getText().toString().trim();
        String radiusStr = etRadiusKm.getText().toString().trim();

        if (city.isEmpty()) {
            etCity.setError("City is required");
            return;
        }
        if (district.isEmpty()) {
            etDistrict.setError("District is required");
            return;
        }

        Double latitude = latStr.isEmpty() ? 0.0 : Double.parseDouble(latStr);
        Double longitude = lngStr.isEmpty() ? 0.0 : Double.parseDouble(lngStr);
        Double radiusKm = radiusStr.isEmpty() ? 0.0 : Double.parseDouble(radiusStr);

        if (areaToEdit != null && position >= 0) {
            // Update existing work area - use Response model
            HelperWorkAreaResponse updatedArea = new HelperWorkAreaResponse();
            updatedArea.setWorkAreaId(areaToEdit.getWorkAreaId());
            updatedArea.setCity(city);
            updatedArea.setDistrict(district);
            updatedArea.setWard(ward);
            updatedArea.setLatitude(latitude);
            updatedArea.setLongitude(longitude);
            updatedArea.setRadiusKm(radiusKm);
            workAreaAdapter.updateWorkArea(position, updatedArea);
        } else {
            // Create new work area - use Request model
            HelperWorkAreaRequest newArea = new HelperWorkAreaRequest();
            newArea.setCity(city);
            newArea.setDistrict(district);
            newArea.setWard(ward);
            newArea.setLatitude(latitude);
            newArea.setLongitude(longitude);
            newArea.setRadiusKm(radiusKm);
            
            // Convert to Response model for the adapter
            HelperWorkAreaResponse responseArea = new HelperWorkAreaResponse();
            responseArea.setCity(city);
            responseArea.setDistrict(district);
            responseArea.setWard(ward);
            responseArea.setLatitude(latitude);
            responseArea.setLongitude(longitude);
            responseArea.setRadiusKm(radiusKm);
            workAreaAdapter.addWorkArea(responseArea);
        }

        dialog.dismiss();
    }

    private void updateProfile() {
        if (!validateForm()) {
            return;
        }

        showLoading("Updating profile...");

        // Upload files if changed
        if (profilePictureUri != null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();
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

        helperApiService.updateHelperProfile(this, currentHelperId, updateDto, new BaseApiService.ApiCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse updatedProfile) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(HelperEditProfileActivity.this,
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
                            Toast.makeText(HelperEditProfileActivity.this, "Email is already registered.", Toast.LENGTH_LONG).show();
                        } else if (errorMessage.contains("Phone is already registered")) {
                            etPhoneNumber.setError("Phone number is already registered");
                            Toast.makeText(HelperEditProfileActivity.this, "Phone number is already registered.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HelperEditProfileActivity.this,
                                "Failed to update profile: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(HelperEditProfileActivity.this,
                            "Failed to update profile.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String fullName = etFullName.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            valid = false;
        } else if (fullName.length() < 2) {
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