package com.example.homehelperfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;

public class LoginActivity extends AppCompatActivity {

    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userType = getIntent().getStringExtra("user_type");
        initViews();
        setUpClickListeners();
    }

    private void initViews() {
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        String greeting = "Hello User!";
        if (userType != null) {
            switch (userType) {
                case "customer":
                    greeting = "Hello Customer!";
                    break;
                case "helper":
                    greeting = "Hello Helper!";
                    break;
                case "admin":
                    greeting = "Hello Admin!";
                    break;
            }
        }
        tvGreeting.setText(greeting);
    }
    private void setUpClickListeners() {
        TextView tvSignUpLink = findViewById(R.id.tvSignUpLink);
        tvSignUpLink.setOnClickListener(v -> {
            Class<?> targetActivity = getRegisterActivityByUserType();
            if (targetActivity != null) {
                startActivity(new Intent(this, targetActivity));
                finish();
            }
        });
    }

    private Class<?> getRegisterActivityByUserType() {
        if (userType == null) return null;
        switch (userType) {
            case "customer":
                return RegisterCustomerActivity.class;
            case "helper":
                return RegisterHelperActivity.class;
            default:
                return null;
        }
    }
}