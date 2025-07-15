package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.postRequest.PostRequestActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

public class MenuActivity extends AppCompatActivity {

    private Button btnWelcome;
    private Button btnUserType;
    private Button btnLogin;
    private Button btnDashboard;
    private Button btnProfileManagement;
    private Button btnNotifications;
    private Button btnTestCustomer;
    private Button btnTestHelper;
    private Button btnTestAdmin;
    private Button btnTestChat;
    private Button btnTestPayment;
    private Button btnResetFlow;
    private Button btn_Helper_dashboard;
    private Button btn_post_request;
    private Button btn_helper_booking_list;
    private Button btn_view_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnWelcome = findViewById(R.id.btn_welcome);
        btnUserType = findViewById(R.id.btn_user_type);
        btnLogin = findViewById(R.id.btn_login);
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnProfileManagement = findViewById(R.id.btn_profile_management);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnTestCustomer = findViewById(R.id.btn_test_customer);
        btnTestHelper = findViewById(R.id.btn_test_helper);
        btnTestAdmin = findViewById(R.id.btn_test_admin);
        btnTestChat = findViewById(R.id.btn_test_chat);
        btnTestPayment = findViewById(R.id.btn_test_payment);
        btnResetFlow = findViewById(R.id.btn_reset_flow);
        btn_Helper_dashboard = findViewById(R.id.btn_Helper_dashboard);
        btn_post_request = findViewById(R.id.btn_post_request);
        btn_helper_booking_list = findViewById(R.id.btn_helper_booking_list);
        btn_view_wallet = findViewById(R.id.btn_view_wallet);
    }

    private void setupClickListeners() {
        btnWelcome.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        btnUserType.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, UserTypeActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.putExtra("user_type", "customer");
            startActivity(intent);
        });

        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        btnProfileManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, com.example.homehelperfinder.ui.profileManagement.ProfileManagementActivity.class);
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });

        btnTestCustomer.setOnClickListener(v -> {
            testUserFlow("customer");
        });

        btnTestHelper.setOnClickListener(v -> {
            testUserFlow("helper");
        });

        btnTestAdmin.setOnClickListener(v -> {
            testUserFlow("admin");
        });

        btnTestChat.setOnClickListener(v -> {
            testChatFlow();
        });

        btnResetFlow.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnTestPayment.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MakePaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btn_Helper_dashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, HelperDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btn_post_request.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PostRequestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btn_helper_booking_list.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, HelperBookingListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btn_view_wallet.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, HelperWalletActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void testUserFlow(String userType) {
        Toast.makeText(this, "Testing " + userType + " flow...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MenuActivity.this, UserTypeActivity.class);
        intent.putExtra("auto_select_user_type", userType);
        startActivity(intent);
    }

    private void testChatFlow() {
        Intent intent = new Intent(MenuActivity.this, com.example.homehelperfinder.ui.chat.ConversationsActivity.class);
        startActivity(intent);
    }
} 