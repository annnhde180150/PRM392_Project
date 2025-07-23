package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.base.BaseActivity;

public class DashboardActivity extends BaseActivity {
    private ImageView ivNotifications;

    // Management Cards
    private CardView cardManageUsers, cardManageHelperApplications, cardMangeBooking, cardAnalytics;

    // Bottom Navigation
    private LinearLayout navHome, navOrders, navProfile;
    private TextView tvNavHome, tvNavOrders, tvNavProfile;
    private ImageView ivNavHome, ivNavOrders, ivNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Hide action bar for custom UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupClickListeners();
        setupMenuNavigation();
    }

    private void initViews() {
        ivNotifications = findViewById(R.id.iv_notifications);

        // Management Cards
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardManageHelperApplications = findViewById(R.id.card_manage_helper_applications);
        cardMangeBooking = findViewById(R.id.card_manage_bookings);
        cardAnalytics = findViewById(R.id.card_analytics);

        // Bottom Navigation
        navHome = findViewById(R.id.nav_home);
        navOrders = findViewById(R.id.nav_orders);
        navProfile = findViewById(R.id.nav_profile);

        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavOrders = findViewById(R.id.tv_nav_orders);
        tvNavProfile = findViewById(R.id.tv_nav_profile);

        ivNavHome = findViewById(R.id.iv_nav_home);
        ivNavOrders = findViewById(R.id.iv_nav_orders);
        ivNavProfile = findViewById(R.id.iv_nav_profile);
    }

    private void setupClickListeners() {
        // Notifications click
        ivNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });

        // Management Cards
        cardManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.profileManagement.ProfileManagementActivity.class);
            startActivity(intent);
        });

        cardManageHelperApplications.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.admin.HelperApplicationsActivity.class);
            startActivity(intent);
        });

        cardMangeBooking.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.admin.AdminRequestsActivity.class);
            startActivity(intent);
        });

        cardAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.admin.AdminAnalyticsActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        navHome.setOnClickListener(v -> {
            // Already on home, just highlight
            setBottomNavSelected(0);
        });

        navOrders.setOnClickListener(v -> {
            setBottomNavSelected(1);
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.admin.AdminRequestsActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            setBottomNavSelected(3);
            Intent intent = new Intent(DashboardActivity.this, com.example.homehelperfinder.ui.editProfile.AdminEditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setBottomNavSelected(int index) {
        // Reset all navigation items
        resetBottomNavItems();

        // Set selected item
        switch (index) {
            case 0: // Home
                tvNavHome.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavHome.setColorFilter(getResources().getColor(R.color.text_primary));
                navHome.setBackgroundResource(R.drawable.nav_item_selected_bg);
                break;
            case 1: // Orders
                tvNavOrders.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavOrders.setColorFilter(getResources().getColor(R.color.text_primary));
                break;
            case 2: // Profile
                tvNavProfile.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavProfile.setColorFilter(getResources().getColor(R.color.text_primary));
                break;
        }
    }

    private void resetBottomNavItems() {
        int defaultColor = getResources().getColor(R.color.text_secondary);

        tvNavHome.setTextColor(defaultColor);
        tvNavOrders.setTextColor(defaultColor);
        tvNavProfile.setTextColor(defaultColor);

        ivNavHome.setColorFilter(defaultColor);
        ivNavOrders.setColorFilter(defaultColor);
        ivNavProfile.setColorFilter(defaultColor);

        navHome.setBackgroundResource(android.R.color.transparent);
        navOrders.setBackgroundResource(android.R.color.transparent);
        navProfile.setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        // Navigate back to user type selection or login
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
} 