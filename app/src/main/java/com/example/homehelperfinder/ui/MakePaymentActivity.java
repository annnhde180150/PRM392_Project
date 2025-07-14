package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.homehelperfinder.data.model.request.GetPaymentRequest;
import com.example.homehelperfinder.data.model.request.UpdatePaymentRequest;
import com.example.homehelperfinder.data.model.response.GetPaymentResponse;
import com.example.homehelperfinder.data.remote.Payment.PaymentApiService;
import com.vnpay.authentication.VNP_AuthenticationActivity;
import com.vnpay.authentication.VNP_SdkCompletedCallback;
import com.example.homehelperfinder.R;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;

public class MakePaymentActivity extends AppCompatActivity {
    private static final String TAG = "MakePaymentActivity";
    private static final String KEY_PAYMENT_INITIATED = "payment_initiated";
    private static final String KEY_PAYMENT_COMPLETED = "payment_completed";

    private boolean isPaymentInitiated = false;
    private boolean isPaymentCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khôi phục trạng thái nếu có
        if (savedInstanceState != null) {
            isPaymentInitiated = savedInstanceState.getBoolean(KEY_PAYMENT_INITIATED, false);
            isPaymentCompleted = savedInstanceState.getBoolean(KEY_PAYMENT_COMPLETED, false);
        }

        // Kiểm tra xem có phải từ deep link không
        if (handleDeepLink()) {
            return;
        }

        // Chỉ khởi tạo thanh toán nếu chưa được khởi tạo và chưa hoàn thành
        if (!isPaymentInitiated && !isPaymentCompleted) {
            int currentUserId = getIntent().getIntExtra("USER_ID", 1);
            int currentBookingId = getIntent().getIntExtra("BOOKING_ID", 3);

            if (currentUserId != -1 && currentBookingId != -1) {
                Log.e(TAG, "Call payment api");
                isPaymentInitiated = true;
                fetchPaymentDetailsFromServer(currentUserId, currentBookingId);
            } else {
                Log.e(TAG, "User ID hoặc Booking ID không hợp lệ.");
                Toast.makeText(this, "Không thể tải thông tin thanh toán.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_PAYMENT_INITIATED, isPaymentInitiated);
        outState.putBoolean(KEY_PAYMENT_COMPLETED, isPaymentCompleted);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleDeepLink();
    }

    private boolean handleDeepLink() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            String scheme = intent.getData().getScheme();
            if ("makepaymentactivity".equals(scheme)) {
                Log.d(TAG, "Received deep link from VNPay: " + intent.getData().toString());

                // Xử lý kết quả thanh toán từ deep link
                String responseCode = intent.getData().getQueryParameter("vnp_ResponseCode");
                if ("00".equals(responseCode)) {
                    // Thanh toán thành công
                    handlePaymentSuccess();
                } else {
                    // Thanh toán thất bại
                    handlePaymentFailure();
                }
                return true;
            }
        }
        return false;
    }

    private void handlePaymentSuccess() {
        isPaymentCompleted = true;
        PaymentApiService paymentApiService = new PaymentApiService(this);
        UpdatePaymentRequest updatePaymentRequest = new UpdatePaymentRequest();
        SharedPreferences sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE);
        int paymentId = sharedPreferences.getInt("payment_id", -1);
        long paymentDateMillis = sharedPreferences.getLong("payment_date", -1);
        String paymentDateRequest = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochMilli(paymentDateMillis);
            paymentDateRequest= DateTimeFormatter.ISO_INSTANT.format(instant);
        }
        updatePaymentRequest.setPaymentDate(paymentDateRequest);
        updatePaymentRequest.setPaymentId(paymentId);
        updatePaymentRequest.setAction("Success");
        paymentApiService.updatePaymentStatus(this,updatePaymentRequest, new PaymentApiService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {

            }
        });
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        // Chuyển về màn hình chính
        Intent intent = new Intent(MakePaymentActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void handlePaymentFailure() {
        isPaymentCompleted = true;
        PaymentApiService paymentApiService = new PaymentApiService(this);
        UpdatePaymentRequest updatePaymentRequest = new UpdatePaymentRequest();
        SharedPreferences sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE);
        int paymentId = sharedPreferences.getInt("payment_id", -1);
        long paymentDateMillis = sharedPreferences.getLong("payment_date", -1);
        String paymentDateRequest = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochMilli(paymentDateMillis);
            paymentDateRequest= DateTimeFormatter.ISO_INSTANT.format(instant);
        }
        updatePaymentRequest.setPaymentDate(paymentDateRequest);
        updatePaymentRequest.setPaymentId(paymentId);
        updatePaymentRequest.setAction("Cancelled");
        paymentApiService.updatePaymentStatus(this,updatePaymentRequest, new PaymentApiService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {

            }
        });

        Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
        // Chuyển về màn hình chính
        Intent intent = new Intent(MakePaymentActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void fetchPaymentDetailsFromServer(int userId, int bookingId) {
        Log.d(TAG, "Đang gọi API getPaymentDetails với userId: " + userId + ", bookingId: " + bookingId);
        PaymentApiService paymentApiService = new PaymentApiService(this);
        GetPaymentRequest getPaymentRequest = new GetPaymentRequest();
        getPaymentRequest.setUserId(userId);
        getPaymentRequest.setBookingId(bookingId);
        paymentApiService.getPaymentDetails(this, getPaymentRequest, new PaymentApiService.ApiCallback<GetPaymentResponse>() {
            @Override
            public void onSuccess(GetPaymentResponse data) {
                handlerSuccess(data);
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Log.e(TAG, "Error getting payment details: " + errorMessage);
                Toast.makeText(MakePaymentActivity.this, "Lỗi khi tải thông tin thanh toán", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void handlerSuccess(GetPaymentResponse data) {
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        int paymentId = data.getPaymentId();
        String hashSecret = ai.metaData.getString("HASH_SECRET");
        Log.i(TAG, "Lấy thông tin thanh toán thành công: " + data.toString());

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh+toan+don+hang";
        String orderType = "other";
        String vnp_TxnRef = generateRandom6DigitNumber();
        String vnp_IpAddr = "13.67.12.10";
        String vnp_TmnCode = "XL9GZ0FP";
        int amount = data.getAmount() * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "vnd");
        String bank_code = "NCB";
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", "makepaymentactivity://vnpay_return");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        //Save paymentId and createDate to SharePreference
        SharedPreferences sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("payment_id", paymentId);
        editor.putLong("payment_date", cld.getTimeInMillis()); // Calendar to millis

        editor.apply();

        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    // Encode fieldValue và fieldName
                    String encodedFieldName = URLEncoder.encode(fieldName, "US-ASCII");
                    String encodedFieldValue = URLEncoder.encode(fieldValue, "US-ASCII");

                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(encodedFieldValue);

                    // Build query
                    query.append(encodedFieldName);
                    query.append('=');
                    query.append(encodedFieldValue);

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(hashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        openSdk(queryUrl,vnp_CreateDate,paymentId);
    }

    public void openSdk(String url,String paymentDate,int paymentId) {
        // Set callback TRƯỚC khi start activity
        VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
            @Override
            public void sdkAction(String action) {
                Log.wtf(TAG, "SDK Action received: " + action);

                // Chạy trên UI thread để đảm bảo an toàn
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (action) {
                            case "SuccessBackAction":
                                Log.d(TAG, "Payment successful via SDK callback");
                                handlePaymentSuccess();
                                break;

                            case "FaildBackAction":
                                Log.d(TAG, "Payment failed via SDK callback");
                                handlePaymentFailure();
                                break;

                            case "CallMobileBankingApp":
                                Log.d(TAG, "User switched to mobile banking app");
                                // Lưu trạng thái đang chờ thanh toán
                                break;

                            case "AppBackAction":
                                Log.d(TAG, "User pressed back from SDK");
                                // Có thể người dùng hủy thanh toán
                                Toast.makeText(MakePaymentActivity.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            case "WebBackAction":
                                Log.d(TAG, "User pressed back from web payment");
                                // Kiểm tra nếu URL chứa thông tin thành công
                                finish();
                                break;

                            default:
                                Log.d(TAG, "Unknown action: " + action);
                                break;
                        }
                    }
                });
            }
        });

        Intent intent = new Intent(this, VNP_AuthenticationActivity.class);
        intent.putExtra("url", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + url);
        intent.putExtra("tmn_code", "XL9GZ0FP");
        intent.putExtra("scheme", "makepaymentactivity");
        intent.putExtra("is_sandbox", true);

        startActivity(intent);
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static String generateRandom6DigitNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(9) + 1;
            sb.append(randomNumber);
        }
        return sb.toString();
    }
}