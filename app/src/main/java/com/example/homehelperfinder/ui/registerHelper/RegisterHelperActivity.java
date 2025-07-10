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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperSkillResponse;
import com.example.homehelperfinder.data.model.response.HelperWorkAreaResponse;
import com.example.homehelperfinder.ui.LocationPickerActivity;
import com.example.homehelperfinder.ui.registerHelper.adapter.SkillAdapter;
import com.example.homehelperfinder.ui.registerHelper.adapter.WorkAreaAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterHelperActivity extends AppCompatActivity {
    private SkillAdapter skillAdapter;
    private WorkAreaAdapter workAreaAdapter;

    private final List<HelperSkillResponse> skillList = new ArrayList<>();
    private final List<HelperWorkAreaResponse> workAreaList = new ArrayList<>();

    private ActivityResultLauncher<Intent> locationPickerLauncher;
    private AutoCompleteTextView actvService;

    private CheckBox cbIsPrimarySkill;
    private TextInputLayout tilService, tilYears;
    private TextInputEditText etYearsOfExperience;
    private TextInputEditText etCity, etDistrict, etWard, etLatitude, etLongitude, etRadiusKm;
    private Button btnAdd, btnCancel, btnPickLocation;
    private TextView tvSelectedLocation;


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

    }

    private void initViews() {
        getSelectedGender();
        setupDatePicker();

        setupSkillsRecyclerView();
        setupAddSkillButton();
        setupWorkAreasRecyclerView();
        setupAddWorkAreaButton();
        setupLocationPickerButton();
    }

    //region for Setup
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

        //TODO: fetch from server or database
        String[] services = {"Cleaning", "Cooking", "Gardening", "Babysitting"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, services);
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


}