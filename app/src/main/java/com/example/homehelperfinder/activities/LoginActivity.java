package com.example.homehelperfinder.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;

public class LoginActivity extends AppCompatActivity {

    private TextView tvGreeting;
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
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        if (userType != null) {
            switch (userType) {
                case "customer":
                    tvGreeting.setText("Hello Customer!");
                    break;
                case "helper":
                    tvGreeting.setText("Hello Helper!");
                    break;
                case "admin":
                    tvGreeting.setText("Hello Admin!");
                    break;
                default:
                    tvGreeting.setText("Hello User!");
                    break;
            }
        } else {
            tvGreeting.setText("Hello User!");
        }

    }
}