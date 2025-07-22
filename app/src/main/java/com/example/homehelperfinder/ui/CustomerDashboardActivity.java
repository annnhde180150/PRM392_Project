package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.activeBookings.ActiveBookingsActivity;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.UserManager;

public class CustomerDashboardActivity extends BaseActivity {

    // Header
    private TextView tvDashboardTitle;
    private ImageView ivProfile;
    private ImageView ivNotifications;
    private ImageView ivFavoriteHelpers;

    // Statistics Cards
    private CardView cardActiveRequests, cardCompletedServices, cardTotalSpent, cardAvailableHelpers;
    private TextView tvActiveRequestsCount, tvActiveRequestsGrowth;
    private TextView tvCompletedServicesCount, tvCompletedServicesGrowth;
    private TextView tvTotalSpentCount, tvTotalSpentGrowth;
    private TextView tvAvailableHelpersCount, tvAvailableHelpersGrowth;

    // Quick Actions Cards
    private CardView cardPostRequest, cardViewRequests, cardChat, cardProfile, cardActiveBookings;

    // Bottom Navigation
    private LinearLayout navHome, navRequests, navMessages, navProfile;
    private TextView tvNavHome, tvNavRequests, tvNavMessages, tvNavProfile;
    private ImageView ivNavHome, ivNavRequests, ivNavMessages, ivNavProfile;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // Hide action bar for custom UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userManager = UserManager.getInstance(this);
        initViews();
        setupClickListeners();
        setupMenuNavigation();
        loadDashboardData();
    }

    private void initViews() {
        // Header
        tvDashboardTitle = findViewById(R.id.tv_dashboard_title);
        ivProfile = findViewById(R.id.iv_profile);
        ivNotifications = findViewById(R.id.iv_notifications);
        ivFavoriteHelpers = findViewById(R.id.iv_favorite_helpers);

        // Statistics Cards
        cardActiveRequests = findViewById(R.id.card_active_requests);
        cardCompletedServices = findViewById(R.id.card_completed_services);
        cardTotalSpent = findViewById(R.id.card_total_spent);
        cardAvailableHelpers = findViewById(R.id.card_available_helpers);

        tvActiveRequestsCount = findViewById(R.id.tv_active_requests_count);
        tvActiveRequestsGrowth = findViewById(R.id.tv_active_requests_growth);
        tvCompletedServicesCount = findViewById(R.id.tv_completed_services_count);
        tvCompletedServicesGrowth = findViewById(R.id.tv_completed_services_growth);
        tvTotalSpentCount = findViewById(R.id.tv_total_spent_count);
        tvTotalSpentGrowth = findViewById(R.id.tv_total_spent_growth);
        tvAvailableHelpersCount = findViewById(R.id.tv_available_helpers_count);
        tvAvailableHelpersGrowth = findViewById(R.id.tv_available_helpers_growth);

        // Quick Actions Cards
        cardPostRequest = findViewById(R.id.card_post_request);
        cardViewRequests = findViewById(R.id.card_view_requests);
        cardChat = findViewById(R.id.card_chat);
        cardProfile = findViewById(R.id.card_profile);
        cardActiveBookings = findViewById(R.id.card_active_bookings);

        // Bottom Navigation
        navHome = findViewById(R.id.nav_home);
        navRequests = findViewById(R.id.nav_requests);
        navMessages = findViewById(R.id.nav_messages);
        navProfile = findViewById(R.id.nav_profile);

        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavRequests = findViewById(R.id.tv_nav_requests);
        tvNavMessages = findViewById(R.id.tv_nav_messages);
        tvNavProfile = findViewById(R.id.tv_nav_profile);

        ivNavHome = findViewById(R.id.iv_nav_home);
        ivNavRequests = findViewById(R.id.iv_nav_requests);
        ivNavMessages = findViewById(R.id.iv_nav_messages);
        ivNavProfile = findViewById(R.id.iv_nav_profile);
    }

    private void setupClickListeners() {
        // Profile click
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.CustomerEditProfileActivity.class);
            startActivity(intent);
        });

        // Notifications click
        ivNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });

        // Quick Actions Cards
        cardPostRequest.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.postRequest.PostRequestActivity.class);
            startActivity(intent);
        });

        cardViewRequests.setOnClickListener(v -> {
            // TODO: Navigate to customer requests list
            // Intent intent = new Intent(CustomerDashboardActivity.this, CustomerRequestsActivity.class);
            // startActivity(intent);
        });

        cardChat.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.chat.ConversationsActivity.class);
            startActivity(intent);
        });

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.CustomerEditProfileActivity.class);
            startActivity(intent);
        });

        // Active Bookings click
        if (cardActiveBookings != null) {
            cardActiveBookings.setOnClickListener(v -> {
                Intent intent = new Intent(CustomerDashboardActivity.this, ActiveBookingsActivity.class);
                intent.putExtra("isHelperView", false);
                startActivity(intent);
            });
        }

        ivFavoriteHelpers.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoriteHelpersActivity.class);
            startActivity(intent);
        });

        // Statistics cards click listeners for reports
        cardTotalSpent.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.reports.CustomerReportsActivity.class);
            startActivity(intent);
        });

        cardActiveRequests.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.reports.CustomerReportsActivity.class);
            startActivity(intent);
        });

        cardCompletedServices.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.reports.CustomerReportsActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        navHome.setOnClickListener(v -> {
            // Already on home, just highlight
            setBottomNavSelected(0);
        });

        navRequests.setOnClickListener(v -> {
            setBottomNavSelected(1);
            // TODO: Navigate to requests screen
            // Intent intent = new Intent(CustomerDashboardActivity.this, CustomerRequestsActivity.class);
            // startActivity(intent);
        });

        navMessages.setOnClickListener(v -> {
            setBottomNavSelected(2);
            // Navigate to conversations screen
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.chat.ConversationsActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            setBottomNavSelected(3);
            Intent intent = new Intent(CustomerDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.CustomerEditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadDashboardData() {
        // Load statistics data (mock data for now)
        tvActiveRequestsCount.setText("3");
        tvActiveRequestsGrowth.setText("+1");

        tvCompletedServicesCount.setText("12");
        tvCompletedServicesGrowth.setText("+2");

        tvTotalSpentCount.setText("$450");
        tvTotalSpentGrowth.setText("+$50");

        tvAvailableHelpersCount.setText("25");
        tvAvailableHelpersGrowth.setText("+5");

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
            case 1: // Requests
                tvNavRequests.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavRequests.setColorFilter(getResources().getColor(R.color.text_primary));
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
        tvNavRequests.setTextColor(defaultColor);
        tvNavMessages.setTextColor(defaultColor);
        tvNavProfile.setTextColor(defaultColor);

        ivNavHome.setColorFilter(defaultColor);
        ivNavRequests.setColorFilter(defaultColor);
        ivNavMessages.setColorFilter(defaultColor);
        ivNavProfile.setColorFilter(defaultColor);

        navHome.setBackgroundResource(android.R.color.transparent);
        navRequests.setBackgroundResource(android.R.color.transparent);
        navMessages.setBackgroundResource(android.R.color.transparent);
        navProfile.setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Navigate back to user type selection or login
        Intent intent = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}