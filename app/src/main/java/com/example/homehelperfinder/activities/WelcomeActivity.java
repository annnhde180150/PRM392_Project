package com.example.homehelperfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Hide action bar for full screen experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnGetStarted = findViewById(R.id.btn_get_started);
    }

    private void setupClickListeners() {
        btnGetStarted.setOnClickListener(v -> {
            // Navigate to user type selection screen
            Intent intent = new Intent(WelcomeActivity.this, UserTypeActivity.class);
            startActivity(intent);
            finish(); // Close welcome screen
        });
    }
}