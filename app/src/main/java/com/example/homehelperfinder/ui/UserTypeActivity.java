package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.base.BaseActivity;

public class UserTypeActivity extends BaseActivity {

    private Button btnCustomer;
    private Button btnHelper;
    private Button btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        // Hide action bar for consistent UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupClickListeners();
        setupMenuNavigation();

        // Check for auto-selection from menu
        checkAutoSelection();
    }

    private void initViews() {
        btnCustomer = findViewById(R.id.btn_customer);
        btnHelper = findViewById(R.id.btn_helper);
        btnAdmin = findViewById(R.id.btn_admin);
    }

    private void setupClickListeners() {
        btnCustomer.setOnClickListener(v -> navigateToUserFlow("customer"));

        btnHelper.setOnClickListener(v -> navigateToUserFlow("helper"));

        btnAdmin.setOnClickListener(v -> navigateToUserFlow("admin"));
    }

    private void navigateToUserFlow(String userType) {
        // You can create different activities based on user type
        // For now, navigate to MainActivity with user type info
        Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
        intent.putExtra("user_type", userType);
        startActivity(intent);
        finish();
    }

    private void checkAutoSelection() {
        // Check if auto-selection is requested from menu
        String autoSelectUserType = getIntent().getStringExtra("auto_select_user_type");
        if (autoSelectUserType != null) {
            // Automatically navigate based on the selected user type
            navigateToUserFlow(autoSelectUserType);
        }
    }
}