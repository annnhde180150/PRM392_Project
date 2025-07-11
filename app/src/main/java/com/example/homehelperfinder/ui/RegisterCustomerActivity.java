package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.base.BaseActivity;

public class RegisterCustomerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_customer);
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
        TextView tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(v -> {
            // Navigate to LoginActivity
            startActivity(new Intent(RegisterCustomerActivity.this, LoginActivity.class));
            finish();
        });
    }
}