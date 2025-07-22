package com.example.homehelperfinder.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperViewIncomeResponse;
import com.example.homehelperfinder.data.remote.helper.HelperApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class HelperWalletActivity extends BaseActivity {
    private TextView tv_username;
    private SharedPrefsHelper sharedPrefsHelper;
    private HelperApiService helperApiService;
    private TextView tv_balance_amount;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helper_wallet);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        viewInit();
        setupClickListeners();
        getIncome();
        setupMenuNavigation();
        getSupportActionBar();
        setupToolbar();
    }
    private void viewInit(){
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(sharedPrefsHelper.getUserName());
        tv_balance_amount = findViewById(R.id.tv_balance_amount);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        // Keep the toolbar setup for compatibility but it's now hidden
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("View Wallet");
        }
    }

    private void setupClickListeners(){

    }
    private void getIncome(){
        helperApiService = new HelperApiService();
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        String helperIdStr = sharedPrefsHelper.getUserId();
        int helperId = Integer.parseInt(helperIdStr);
        helperApiService.getIncome(this,helperId, new HelperApiService.ApiCallback<HelperViewIncomeResponse>() {
            @Override
            public void onSuccess(HelperViewIncomeResponse data) {
                double balance = data.getBalance();
                NumberFormat vnFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                String formatted = vnFormat.format(balance);
                tv_balance_amount.setText(formatted + " " + data.getCurrencyCode());
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}