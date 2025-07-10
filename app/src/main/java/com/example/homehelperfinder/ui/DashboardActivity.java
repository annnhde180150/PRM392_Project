package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.base.BaseActivity;

public class DashboardActivity extends BaseActivity {

    // Header
    private TextView tvDashboardTitle;
    private ImageView ivProfile;

    // Statistics Cards
    private CardView cardUsers, cardOrders, cardRevenue, cardHelpers;
    private TextView tvUsersCount, tvUsersGrowth;
    private TextView tvOrdersCount, tvOrdersGrowth;
    private TextView tvRevenueCount, tvRevenueGrowth;
    private TextView tvHelpersCount, tvHelpersGrowth;

    // Management Cards
    private CardView cardManageUsers, cardManageOrders, cardManagePayments, cardManageSupport;

    // Bottom Navigation
    private LinearLayout navHome, navOrders, navMessages, navProfile;
    private TextView tvNavHome, tvNavOrders, tvNavMessages, tvNavProfile;
    private ImageView ivNavHome, ivNavOrders, ivNavMessages, ivNavProfile;

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
        loadDashboardData();
    }

    private void initViews() {
        // Header
        tvDashboardTitle = findViewById(R.id.tv_dashboard_title);
        ivProfile = findViewById(R.id.iv_profile);

        // Statistics Cards
        cardUsers = findViewById(R.id.card_users);
        cardOrders = findViewById(R.id.card_orders);
        cardRevenue = findViewById(R.id.card_revenue);
        cardHelpers = findViewById(R.id.card_helpers);

        tvUsersCount = findViewById(R.id.tv_users_count);
        tvUsersGrowth = findViewById(R.id.tv_users_growth);
        tvOrdersCount = findViewById(R.id.tv_orders_count);
        tvOrdersGrowth = findViewById(R.id.tv_orders_growth);
        tvRevenueCount = findViewById(R.id.tv_revenue_count);
        tvRevenueGrowth = findViewById(R.id.tv_revenue_growth);
        tvHelpersCount = findViewById(R.id.tv_helpers_count);
        tvHelpersGrowth = findViewById(R.id.tv_helpers_growth);

        // Management Cards
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardManageOrders = findViewById(R.id.card_manage_orders);
        cardManagePayments = findViewById(R.id.card_manage_payments);
        cardManageSupport = findViewById(R.id.card_manage_support);

        // Bottom Navigation
        navHome = findViewById(R.id.nav_home);
        navOrders = findViewById(R.id.nav_orders);
        navMessages = findViewById(R.id.nav_messages);
        navProfile = findViewById(R.id.nav_profile);

        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavOrders = findViewById(R.id.tv_nav_orders);
        tvNavMessages = findViewById(R.id.tv_nav_messages);
        tvNavProfile = findViewById(R.id.tv_nav_profile);

        ivNavHome = findViewById(R.id.iv_nav_home);
        ivNavOrders = findViewById(R.id.iv_nav_orders);
        ivNavMessages = findViewById(R.id.iv_nav_messages);
        ivNavProfile = findViewById(R.id.iv_nav_profile);
    }

    private void setupClickListeners() {
        // Profile click
        ivProfile.setOnClickListener(v -> {
            // TODO: Navigate to profile settings
        });

        // Management Cards
        cardManageUsers.setOnClickListener(v -> {
            // TODO: Navigate to user management
        });

        cardManageOrders.setOnClickListener(v -> {
            // TODO: Navigate to order management
        });

        cardManagePayments.setOnClickListener(v -> {
            // TODO: Navigate to payment management
        });

        cardManageSupport.setOnClickListener(v -> {
            // TODO: Navigate to support management
        });

        // Bottom Navigation
        navHome.setOnClickListener(v -> {
            // Already on home, just highlight
            setBottomNavSelected(0);
        });

        navOrders.setOnClickListener(v -> {
            setBottomNavSelected(1);
            // TODO: Navigate to orders screen
        });

        navMessages.setOnClickListener(v -> {
            setBottomNavSelected(2);
            // TODO: Navigate to messages screen
        });

        navProfile.setOnClickListener(v -> {
            setBottomNavSelected(3);
            // TODO: Navigate to profile screen
        });
    }

    private void loadDashboardData() {
        // Load statistics data (mock data for now)
        tvUsersCount.setText("1,234");
        tvUsersGrowth.setText("+10%");

        tvOrdersCount.setText("56");
        tvOrdersGrowth.setText("+5%");

        tvRevenueCount.setText("$12,345");
        tvRevenueGrowth.setText("+15%");

        tvHelpersCount.setText("234");
        tvHelpersGrowth.setText("+8%");

        // Set Home as selected by default
        setBottomNavSelected(0);
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
            case 2: // Messages
                tvNavMessages.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavMessages.setColorFilter(getResources().getColor(R.color.text_primary));
                break;
            case 3: // Profile
                tvNavProfile.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavProfile.setColorFilter(getResources().getColor(R.color.text_primary));
                break;
        }
    }

    private void resetBottomNavItems() {
        int defaultColor = getResources().getColor(R.color.text_secondary);

        tvNavHome.setTextColor(defaultColor);
        tvNavOrders.setTextColor(defaultColor);
        tvNavMessages.setTextColor(defaultColor);
        tvNavProfile.setTextColor(defaultColor);

        ivNavHome.setColorFilter(defaultColor);
        ivNavOrders.setColorFilter(defaultColor);
        ivNavMessages.setColorFilter(defaultColor);
        ivNavProfile.setColorFilter(defaultColor);

        navHome.setBackgroundResource(android.R.color.transparent);
        navOrders.setBackgroundResource(android.R.color.transparent);
        navMessages.setBackgroundResource(android.R.color.transparent);
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