package com.example.homehelperfinder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

public class HelperDashboardActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchActiveStatus;
    private SharedPrefsHelper sharedPrefsHelper;
    private TextView tv_greeting, tvNavProfile;
    private ImageButton btnNotification;
    private LinearLayout navProfile;
    private ImageView ivNavProfile;
    private CardView btn_view_income;

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
            Intent intent = new Intent(HelperDashboardActivity.this, HelperWalletActivity.class);
            startActivity(intent);
        });

        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
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

        tvNavProfile = findViewById(R.id.tv_nav_profile);
        ivNavProfile = findViewById(R.id.iv_nav_profile);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void setupClickListeners() {
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });
        btn_view_income.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, HelperWalletActivity.class);
            startActivity(intent);
        });
        navProfile.setOnClickListener(v -> {
            setBottomNavSelected(3);
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.editProfile.HelperEditProfileActivity.class);
            startActivity(intent);
        });

    }

    private void setBottomNavSelected(int index) {
        // Reset all navigation items
        resetBottomNavItems();

        // Set selected item
        switch (index) {
//            case 0: // Home
//                tvNavHome.setTextColor(getResources().getColor(R.color.text_primary));
//                ivNavHome.setColorFilter(getResources().getColor(R.color.text_primary));
//                navHome.setBackgroundResource(R.drawable.nav_item_selected_bg);
//                break;
//            case 1: // Orders
//                tvNavOrders.setTextColor(getResources().getColor(R.color.text_primary));
//                ivNavOrders.setColorFilter(getResources().getColor(R.color.text_primary));
//                break;
//            case 2: // Messages
//                tvNavMessages.setTextColor(getResources().getColor(R.color.text_primary));
//                ivNavMessages.setColorFilter(getResources().getColor(R.color.text_primary));
//                break;
            case 3: // Profile
                tvNavProfile.setTextColor(getResources().getColor(R.color.text_primary));
                ivNavProfile.setColorFilter(getResources().getColor(R.color.text_primary));
                break;
        }
    }


    private void resetBottomNavItems() {
        int defaultColor = getResources().getColor(R.color.text_secondary);

//        tvNavHome.setTextColor(defaultColor);
//        tvNavOrders.setTextColor(defaultColor);
//        tvNavMessages.setTextColor(defaultColor);
//        tvNavProfile.setTextColor(defaultColor);
//
//        ivNavHome.setColorFilter(defaultColor);
//        ivNavOrders.setColorFilter(defaultColor);
//        ivNavMessages.setColorFilter(defaultColor);
//        ivNavProfile.setColorFilter(defaultColor);
//
//        navHome.setBackgroundResource(android.R.color.transparent);
//        navOrders.setBackgroundResource(android.R.color.transparent);
//        navMessages.setBackgroundResource(android.R.color.transparent);
        navProfile.setBackgroundResource(android.R.color.transparent);
    }

}