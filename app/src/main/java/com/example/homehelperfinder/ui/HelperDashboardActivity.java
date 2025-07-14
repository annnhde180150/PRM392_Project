package com.example.homehelperfinder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.HelperAvailableRequest;
import com.example.homehelperfinder.data.remote.Helper.HelperAvailableStatusApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

public class HelperDashboardActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchActiveStatus;
    private SharedPrefsHelper sharedPrefsHelper;


    private ImageButton btnNotification;

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

    private void setupClickListener() {
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
    }

    private void initViews() {
        btnNotification = findViewById(R.id.btn_notification);
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        switchActiveStatus = findViewById(R.id.switch_active_status);
    }

    private void setupClickListeners() {
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HelperDashboardActivity.this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
        });
    }
}