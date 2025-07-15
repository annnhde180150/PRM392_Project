package com.example.homehelperfinder.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
    }
    private void viewInit(){
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(sharedPrefsHelper.getUserName());
        tv_balance_amount = findViewById(R.id.tv_balance_amount);
    }

    private void setupClickListeners(){

    }
    private void getIncome(){
        helperApiService = new HelperApiService();
        sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
        String helperIdStr = sharedPrefsHelper.getUserId();
        int helperId = 1; //Hardcode to test
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
}