package com.example.homehelperfinder.ui.registerHelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.remote.auth.AuthApiService;
import com.example.homehelperfinder.data.remote.service.ServiceApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.data.model.response.HelperSkillResponse;
import com.example.homehelperfinder.data.model.response.HelperWorkAreaResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.ui.LocationPickerActivity;

import com.example.homehelperfinder.ui.LoginActivity;
import com.example.homehelperfinder.ui.registerHelper.adapter.SkillAdapter;
import com.example.homehelperfinder.ui.registerHelper.adapter.WorkAreaAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.model.request.HelperDocumentRequest;
import com.example.homehelperfinder.data.model.request.HelperRequest;
import com.example.homehelperfinder.data.model.request.HelperSkillRequest;
import com.example.homehelperfinder.data.model.request.HelperWorkAreaRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;

public class RegisterHelperActivity extends BaseActivity {
    private SkillAdapter skillAdapter;
    private WorkAreaAdapter workAreaAdapter;
    private ServiceApiService serviceService;

    private final List<HelperSkillResponse> skillList = new ArrayList<>();
    private final List<HelperWorkAreaResponse> workAreaList = new ArrayList<>();
    private List<ServiceResponse> serviceList = new ArrayList<>();

    private ActivityResultLauncher<Intent> locationPickerLauncher;
    private AutoCompleteTextView actvService;
    private ActivityResultLauncher<Intent> filePickerLauncher;


    private CheckBox cbIsPrimarySkill;
    private TextInputLayout tilService, tilYears;
    private TextInputEditText etCity, etDistrict, etWard, etLatitude, etLongitude, etRadiusKm, etYearsOfExperience;
    private Button btnAdd, btnCancel, btnPickLocation, btnViewIdFile;
    private TextView tvSelectedLocation;
    private Uri idFileUri, cvFileUri;
    private String finalIdUrl = null, finalCvUrl = null;
    private ImageView ivIdPreview, ivCvPreview;
    private String idFileMimeType, cvFileMimeType;
    private ActivityResultLauncher<Intent> cvFilePickerLauncher;
    private AtomicInteger pendingUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_helper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupMenuNavigation();

    }

    private void initViews() {
        ServiceApiService serviceService = new ServiceApiService();
        fetchServices();

        getSelectedGender();
        setupDatePicker();

        setupSkillsRecyclerView();
        setupAddSkillButton();
        setupWorkAreasRecyclerView();
        setupAddWorkAreaButton();
        setupLocationPickerButton();

        setupBtnUploadId();
        setupBtnUploadCV();
        
        setupIdFilePicker();
        setupCvFilePicker();

        setupSignUpButton();
    }

    //region for Setup

    private void fetchServices(){
        serviceService.getActiveServices(this, new BaseApiService.ApiCallback<List<ServiceResponse>>() {
            @Override
            public void onSuccess(List<ServiceResponse> services) {
                runOnUiThread(() -> {
                    serviceList.clear();
                    serviceList.addAll(services);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterHelperActivity.this, "Failed to load services: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setupSkillsRecyclerView() {
        RecyclerView rvSkills = findViewById(R.id.rvSkills);
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
    }

    private void setupWorkAreasRecyclerView() {
        RecyclerView rvWorkAreas = findViewById(R.id.rvWorkAreas);
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

    private void setupDatePicker() {
        EditText etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR) - 18; // default to 18 years ago
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterHelperActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etDateOfBirth.setText(date);
                }, year, month, day);

                // Set min/max date
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.YEAR, -100); // 100 years ago
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.YEAR, -18); // 18 years ago
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });
    }

    private String getSelectedGender() {
        RadioGroup rgGender = findViewById(R.id.rgGender);
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMale) return "Male";
        if (checkedId == R.id.rbFemale) return "Female";
        if (checkedId == R.id.rbOthers) return "Others";
        return null;
    }

    private void setupAddSkillButton() {
        Button btnAddSkill = findViewById(R.id.btnAddSkill);
        btnAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSkillDialog(null, -1);
            }
        });
    }

    private void setupAddWorkAreaButton() {
        Button btnAddWorkArea = findViewById(R.id.btnAddWorkArea);
        btnAddWorkArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWorkAreaDialog(null, -1);
            }
        });
    }

    private void setupLocationPickerButton() {
        locationPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        double lat = result.getData().getDoubleExtra("latitude", 0);
                        double lng = result.getData().getDoubleExtra("longitude", 0);
                        String city = result.getData().getStringExtra("city");
                        String district = result.getData().getStringExtra("district");
                        String ward = result.getData().getStringExtra("ward");
                        if (etLatitude != null && etLongitude != null && tvSelectedLocation != null) {
                            etLatitude.setText(String.valueOf(lat));
                            etLongitude.setText(String.valueOf(lng));
                            tvSelectedLocation.setText("Lat: " + lat + ", Lng: " + lng);
                        }
                        // Set city, district, ward if available
                        if (etCity != null && city != null) etCity.setText(city);
                        if (etDistrict != null && district != null) etDistrict.setText(district);
                        if (etWard != null && ward != null) etWard.setText(ward);
                    }
                }
        );
    }



    private void setupIdFilePicker() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        idFileUri = result.getData().getData();
                        if (idFileUri != null) {
                            idFileMimeType = getContentResolver().getType(idFileUri);
                            if (idFileMimeType != null && idFileMimeType.startsWith("image/")) {
                                ivIdPreview.setVisibility(View.VISIBLE);
                                Glide.with(this).load(idFileUri).into(ivIdPreview);
                            } else {
                                ivIdPreview.setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );
    }

    private void setupCvFilePicker() {
        cvFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        cvFileUri = result.getData().getData();
                        if (cvFileUri != null) {
                            cvFileMimeType = getContentResolver().getType(cvFileUri);
                            if (cvFileMimeType != null && cvFileMimeType.startsWith("image/")) {
                                ivCvPreview.setVisibility(View.VISIBLE);
                                Glide.with(this).load(cvFileUri).into(ivCvPreview);
                            } else {
                                ivCvPreview.setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );
    }

    private void setupSignUpButton() {
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            if (!validateRegistrationForm()) {
                return;
            }
            showLoading("Processing registration...");
            uploadFilesAndRegister();
        });
    }

    //endregion

    //region for Skills
    private void initSkillDialogFields(View dialogView) {
        actvService = dialogView.findViewById(R.id.actvService);
        etYearsOfExperience = dialogView.findViewById(R.id.etYearsOfExperience);
        cbIsPrimarySkill = dialogView.findViewById(R.id.cbIsPrimarySkill);
        btnAdd = dialogView.findViewById(R.id.btnAdd);
        btnCancel = dialogView.findViewById(R.id.btnCancel);

        tilService = dialogView.findViewById(R.id.tilService);
        tilYears = dialogView.findViewById(R.id.tilYears);
    }


    private void showAddSkillDialog(HelperSkillResponse skillToEdit, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_skill, null);

        initSkillDialogFields(dialogView);

        // showProgressDialog("Loading services...");

        List<String> serviceNames = new ArrayList<>();
        for (ServiceResponse service : serviceList) {
            serviceNames.add(service.getServiceName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterHelperActivity.this, android.R.layout.simple_dropdown_item_1line, serviceNames);
        actvService.setAdapter(adapter);

        // If editing, pre-fill fields
        if (skillToEdit != null) {
            actvService.setText(skillToEdit.getServiceName(), false);
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
        HelperSkillResponse newSkill = new HelperSkillResponse(selectedService, years, isPrimary);

        if (skillToEdit != null && position >= 0) {
            skillAdapter.updateSkill(position, newSkill);
        } else {
            skillAdapter.addSkill(newSkill);
        }

        dialog.dismiss();
    }
    //endregion

    //region for Work Areas
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

    private void showAddWorkAreaDialog(HelperWorkAreaResponse areaToEdit, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_work_area, null);

        initWorkAreaDialogFields(dialogView);

        btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, LocationPickerActivity.class);
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

        Double latitude = latStr.isEmpty() ? null : Double.parseDouble(latStr);
        Double longitude = lngStr.isEmpty() ? null : Double.parseDouble(lngStr);
        Double radiusKm = radiusStr.isEmpty() ? null : Double.parseDouble(radiusStr);

        HelperWorkAreaResponse newArea = new HelperWorkAreaResponse(city, district, ward, latitude, longitude, radiusKm);

        if (areaToEdit != null && position >= 0) {
            workAreaAdapter.updateWorkArea(position, newArea);
        } else {
            workAreaAdapter.addWorkArea(newArea);
        }

        dialog.dismiss();
    }


    //endregion


    //region for File Upload
    private void setupBtnUploadId() {
        ivIdPreview = findViewById(R.id.ivIdPreview);
        Button btnUploadId = findViewById(R.id.btnUploadId);
        btnUploadId.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            String[] mimeTypes = {"application/pdf", "image/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            filePickerLauncher.launch(Intent.createChooser(intent, "Select ID Document"));
        });
    }

    private void setupBtnUploadCV() {
        ivCvPreview = findViewById(R.id.ivCvPreview);
        Button btnUploadCV = findViewById(R.id.btnUploadCV);
        btnUploadCV.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            cvFilePickerLauncher.launch(Intent.createChooser(intent, "Select CV File"));
        });
    }

    private void uploadFileToFirebase(Uri fileUri, String folder, BiConsumer<String, String> resultHandler) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = folder + "/" + System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> resultHandler.accept(uri.toString(), null)) // Success: pass URL
                        .addOnFailureListener(e -> resultHandler.accept(null, e.getMessage())))  // Failure: pass null
                .addOnFailureListener(e -> resultHandler.accept(null, e.getMessage()));         // Failure: pass null
    }

    private void uploadIdFileToFirebase(Uri fileUri) {
        uploadFileToFirebase(fileUri, "ids", (url, error) -> {
            if (url != null) {
                this.finalIdUrl = url;
                Toast.makeText(this, "ID uploaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to upload ID: " + error, Toast.LENGTH_SHORT).show();
            }
            onUploadTaskComplete(); // Notify that this task is done
        });
    }


    private void uploadCvFileToFirebase(Uri fileUri) {
        uploadFileToFirebase(fileUri, "cvs", (url, error) -> {
            if (url != null) {
                this.finalCvUrl = url;
                Toast.makeText(this, "CV uploaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to upload CV: " + error, Toast.LENGTH_SHORT).show();
            }
            onUploadTaskComplete(); // Notify that this task is done
        });
    }

    private void onUploadTaskComplete() {
        // Decrement the counter. If it reaches zero, all uploads are finished.
        if (pendingUploads.decrementAndGet() == 0) {
            if ((idFileUri != null && finalIdUrl == null) || (cvFileUri != null && finalCvUrl == null)) {
                hideLoading();
                Toast.makeText(this, "A file upload failed. Registration aborted.", Toast.LENGTH_LONG).show();
                finalIdUrl = null;
                finalCvUrl = null;
            } else {
                submitRegistration(finalIdUrl, finalCvUrl);
            }
        }
    }
    //endregion


    private void uploadFilesAndRegister() {
        int uploadCount = 0;
        if (idFileUri != null) uploadCount++;
        if (cvFileUri != null) uploadCount++;

        if (uploadCount == 0) {
            submitRegistration(null, null);
            return;
        }

        pendingUploads = new AtomicInteger(uploadCount);
        // Show loading indicator (already shown in setupSignUpButton)
        if (idFileUri != null) {
            uploadIdFileToFirebase(idFileUri);
        }

        if (cvFileUri != null) {
            uploadCvFileToFirebase(cvFileUri);
        }
    }


    private void submitRegistration(String idFileUrl, String cvFileUrl) {
        String fullName = ((TextInputEditText) findViewById(R.id.etFullName)).getText().toString().trim();
        String dob = ((TextInputEditText) findViewById(R.id.etDateOfBirth)).getText().toString().trim();
        String gender = getSelectedGender();
        String email = ((TextInputEditText) findViewById(R.id.etEmail)).getText().toString().trim();
        String phone = ((TextInputEditText) findViewById(R.id.etPhoneNumber)).getText().toString().trim();
        String password = ((TextInputEditText) findViewById(R.id.etPassword)).getText().toString();
        String bio =  ((TextInputEditText) findViewById(R.id.etBio)).getText().toString();


        List<HelperSkillRequest> skills = new ArrayList<>();
        for (HelperSkillResponse s : skillList) {

            int serviceId = -1;
            for (ServiceResponse sr : serviceList) {
                if (sr.getServiceName().equals(s.getServiceName())) {
                    serviceId = sr.getServiceId();
                    break;
                }
            }
            if (serviceId == -1) continue;

            HelperSkillRequest dto = new HelperSkillRequest();
            dto.serviceId = serviceId;
            dto.yearsOfExperience = s.getYearsOfExperience();
            dto.isPrimarySkill = s.isPrimarySkill();
            skills.add(dto);
        }

        List<HelperWorkAreaRequest> workAreas = new ArrayList<>();
        for (HelperWorkAreaResponse w : workAreaList) {
            HelperWorkAreaRequest dto = new HelperWorkAreaRequest();
            dto.City = w.getCity();
            dto.District = w.getDistrict();
            dto.Ward = w.getWard();
            dto.Latitude = w.getLatitude() != null ? w.getLatitude() : 0.0;
            dto.Longitude = w.getLongitude() != null ? w.getLongitude() : 0.0;
            dto.RadiusKm = w.getRadiusKm() != null ? w.getRadiusKm() : 0.0;
            workAreas.add(dto);
        }

        List<HelperDocumentRequest> documents = new ArrayList<>();
        if (idFileUrl != null) {
            HelperDocumentRequest idDoc = new HelperDocumentRequest();
            idDoc.DocumentType = "ID";
            idDoc.DocumentUrl = idFileUrl;
            documents.add(idDoc);
        }
        if (cvFileUrl != null) {
            HelperDocumentRequest cvDoc = new HelperDocumentRequest();
            cvDoc.DocumentType = "CV";
            cvDoc.DocumentUrl = cvFileUrl;
            documents.add(cvDoc);
        }

        HelperRequest request = new HelperRequest();
        request.setPhoneNumber(phone);
        request.setEmail(email);
        request.setPassword(password);
        request.setFullName(fullName);
        request.setDateOfBirth(dob);
        request.setBio(bio);
        request.setGender(gender);
        request.setSkills(skills);
        request.setWorkAreas(workAreas);
        request.setDocuments(documents);

        AuthApiService authService = new AuthApiService();
        authService.registerHelper(this, request, new AuthApiService.AuthCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse data) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(RegisterHelperActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterHelperActivity.this, LoginActivity.class));
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(RegisterHelperActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private boolean validateRegistrationForm() {
        boolean valid = true;

        // Full Name
        TextInputEditText etFullName = findViewById(R.id.etFullName);
        String fullName = etFullName.getText().toString().trim();
        if (fullName.isEmpty()) {
            ((TextInputLayout) findViewById(R.id.tilFullName)).setError("Full name is required");
            valid = false;
        } else {
            ((TextInputLayout) findViewById(R.id.tilFullName)).setError(null);
        }

        // Date of Birth
        TextInputEditText etDateOfBirth = findViewById(R.id.etDateOfBirth);
        String dob = etDateOfBirth.getText().toString().trim();
        if (dob.isEmpty()) {
            ((TextInputLayout) findViewById(R.id.tilDateOfBirth)).setError("Date of birth is required");
            valid = false;
        } else {
            ((TextInputLayout) findViewById(R.id.tilDateOfBirth)).setError(null);
        }

        // Gender
        RadioGroup rgGender = findViewById(R.id.rgGender);
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Email
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((TextInputLayout) findViewById(R.id.tilEmail)).setError("Valid email is required");
            valid = false;
        } else {
            ((TextInputLayout) findViewById(R.id.tilEmail)).setError(null);
        }

        // Phone Number
        TextInputEditText etPhone = findViewById(R.id.etPhoneNumber);
        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 8) {
            ((TextInputLayout) findViewById(R.id.tilPhoneNumber)).setError("Valid phone number is required");
            valid = false;
        } else {
            ((TextInputLayout) findViewById(R.id.tilPhoneNumber)).setError(null);
        }

        // Password
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        String password = etPassword.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            ((TextInputLayout) findViewById(R.id.tilPassword)).setError("Password must be at least 6 characters");
            valid = false;
        } else {
            ((TextInputLayout) findViewById(R.id.tilPassword)).setError(null);
        }

        // Skills
        if (skillList.isEmpty()) {
            Toast.makeText(this, "Please add at least one skill", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Work Areas
        if (workAreaList.isEmpty()) {
            Toast.makeText(this, "Please add at least one work area", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Documents
        if (idFileUri == null) {
            Toast.makeText(this, "Please upload your ID document", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}