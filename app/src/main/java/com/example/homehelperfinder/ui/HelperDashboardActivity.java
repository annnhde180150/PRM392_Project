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
import com.example.homehelperfinder.ui.activeBookings.UpdateBookingStatusActivity;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.listBooking.HelperBookingListActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.UserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelperDashboardActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchActiveStatus;
    private SharedPrefsHelper sharedPrefsHelper;
    private TextView tv_greeting;
    private ImageButton btnNotification;
    private CardView btn_view_income,btn_view_wallet, btn_avail_requests,btn_active_bookings;

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
                return true;
            }
            if (item.getItemId()== R.id.nav_messages){
                Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.chat.ConversationsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
    private void setupClickListeners() {
        HelperAvailableStatusApiService api = new HelperAvailableStatusApiService(this);
        HelperAvailableRequest request = new HelperAvailableRequest();
        String userIdStr = sharedPrefsHelper.getUserId();
        UserManager userManager = UserManager.getInstance(this);
        request.setUserId(userManager.getCurrentUserId());
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
                intent.putExtra(ActiveBookingsActivity.EXTRA_MODE, "helper");
                intent.putExtra(ActiveBookingsActivity.EXTRA_ID, 0);
                startActivity(intent);
            });
        }
        
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });
        btn_view_wallet.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.HelperWalletActivity.class);
            startActivity(intent);
        });
        btn_avail_requests.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.viewRequests.ViewAvailableRequestActivity.class);
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
        btn_view_wallet = findViewById(R.id.card_manage_view_Wallet);
        btn_avail_requests = findViewById(R.id.card_view_request);
    }
}