package com.example.homehelperfinder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.HelperAvailableRequest;
import com.example.homehelperfinder.data.remote.helper.HelperAvailableStatusApiService;
import com.example.homehelperfinder.ui.activeBookings.ActiveBookingsActivity;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.listBooking.HelperBookingListActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelperDashboardActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchActiveStatus;
    private SharedPrefsHelper sharedPrefsHelper;
    private TextView tv_greeting, tvNavProfile;
    private ImageButton btnNotification;
    private LinearLayout navProfile,nav_orders,nav_home;
    private ImageView ivNavProfile;
    private CardView btn_view_income, btn_active_bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helper_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
        setupMenuNavigation();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
             return true;
            }
            if (item.getItemId() == R.id.nav_orders) {
                Intent intent = new Intent(HelperDashboardActivity.this, HelperBookingListActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.HelperEditProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            if (item.getItemId()== R.id.nav_messages){
                //Implement intent message activity
                return true;
            }
            return false;
        });
    }
    private void setupClickListeners() {
        HelperAvailableStatusApiService api = new HelperAvailableStatusApiService(this);
        HelperAvailableRequest request = new HelperAvailableRequest();
        String userIdStr = sharedPrefsHelper.getUserId();
        int userId = 1; // HardCore to test
        request.setUserId(userId);
        // Kiểm tra trạng thái ban đầu (nếu cần)
        boolean isChecked = switchActiveStatus.isChecked();
        Log.d("SwitchStatus", "Trạng thái ban đầu: " + (isChecked ? "Đang hoạt động" : "Không hoạt động"));

        // Lắng nghe thay đổi
        switchActiveStatus.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            if (isChecked1) {
                // Switch bật


                api.onlineRequest(this, request, new HelperAvailableStatusApiService.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {

                        Log.d("SwitchStatus", "Cập nhật trạng thái thành công");
                    }

                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        Log.d("SwitchStatus", "Cập nhật trạng thái thất bại");
                    }
                });
                Toast.makeText(this, "Đang hoạt động", Toast.LENGTH_SHORT).show();
            } else {
                // Switch tắt

                api.offlineRequest(this, request, new HelperAvailableStatusApiService.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {

                        Log.d("SwitchStatus", "Cập nhật trạng thái thành công");
                    }

                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        Log.d("SwitchStatus", "Cập nhật trạng thái thất bại");
                    }
                });
                Toast.makeText(this, "Không hoạt động", Toast.LENGTH_SHORT).show();
            }
        });

        btn_view_income.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.reports.helper.HelperReportsActivity.class);
            startActivity(intent);
        });
        
        if (btn_active_bookings != null) {
            btn_active_bookings.setOnClickListener(v -> {
                Intent intent = new Intent(HelperDashboardActivity.this, ActiveBookingsActivity.class);
                intent.putExtra("isHelperView", true);
                startActivity(intent);
            });
        }
        
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });
        navProfile.setOnClickListener(v -> {
            setBottomNavSelected(3);
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.HelperEditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        btnNotification = findViewById(R.id.btn_notification);
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        switchActiveStatus = findViewById(R.id.switch_active_status);
        tv_greeting = findViewById(R.id.tv_greeting);
        tv_greeting.setText("Hello " + sharedPrefsHelper.getUserName());
        btn_view_income = findViewById(R.id.card_manage_view_income);
        btn_active_bookings = findViewById(R.id.card_active_bookings);

        tvNavProfile = findViewById(R.id.tv_nav_profile);
        ivNavProfile = findViewById(R.id.iv_nav_profile);
        navProfile = findViewById(R.id.nav_profile);
    }
}