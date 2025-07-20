package com.example.homehelperfinder.ui.helperSearch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.base.BaseActivity;

public class HelperDetailActivity extends BaseActivity {
    
    private Toolbar toolbar;
    private int helperId;
    private int serviceId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_detail);
        
        // Get intent extras
        helperId = getIntent().getIntExtra("helperId", -1);
        serviceId = getIntent().getIntExtra("serviceId", -1);
        
        if (helperId == -1 || serviceId == -1) {
            Toast.makeText(this, "Invalid helper information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        setupToolbar();
        loadHelperDetails();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        // Initialize other views here
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Helper Details");
        }
    }
    
    private void loadHelperDetails() {
        // TODO: Load helper details from API
        // For now, just show a placeholder
        Toast.makeText(this, "Loading helper details...", Toast.LENGTH_SHORT).show();
    }
} 