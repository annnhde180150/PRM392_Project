package com.example.homehelperfinder.ui.activeBookings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.BookingDetailResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.booking.BookingApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.DateUtils;
import com.example.homehelperfinder.utils.UserManager;


public class BookingDetailActivity extends BaseActivity {
    private UserManager userManager;
    private int currentUserId;
    private static final int REQUEST_UPDATE_STATUS = 100;

    private TextView tvBookingId, tvServiceName, tvStatus;
    private TextView tvScheduledStart, tvScheduledEnd;
    private TextView tvActualStart, tvActualEnd;
    private TextView tvAddress, tvEstimatedPrice, tvFinalPrice;
    private LinearLayout layoutActualStart, layoutActualEnd, layoutFinalPrice;
    private Button btnUpdateStatus;
    private ProgressBar progressBar;

    private BookingApiService bookingApiService;
    private int bookingId;
    private boolean isHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // Initialize views
        tvBookingId = findViewById(R.id.tv_booking_id);
        tvServiceName = findViewById(R.id.tv_service_name);
        tvStatus = findViewById(R.id.tv_status);
        tvScheduledStart = findViewById(R.id.tv_scheduled_start);
        tvScheduledEnd = findViewById(R.id.tv_scheduled_end);
        tvActualStart = findViewById(R.id.tv_actual_start);
        tvActualEnd = findViewById(R.id.tv_actual_end);
        tvAddress = findViewById(R.id.tv_address);
        tvEstimatedPrice = findViewById(R.id.tv_estimated_price);
        tvFinalPrice = findViewById(R.id.tv_final_price);
        layoutActualStart = findViewById(R.id.layout_actual_start);
        layoutActualEnd = findViewById(R.id.layout_actual_end);
        layoutFinalPrice = findViewById(R.id.layout_final_price);
        btnUpdateStatus = findViewById(R.id.btn_update_status);
        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize services
        bookingApiService = new BookingApiService();

        // Get booking ID from intent
        bookingId = getIntent().getIntExtra("bookingId", -1);
        if (bookingId == -1) {
            Toast.makeText(this, "Invalid booking ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check if user is a helper
        userManager = UserManager.getInstance(this);
        currentUserId = userManager.getCurrentUserId();
        isHelper = userManager.isHelper();

        // Set click listeners
        btnUpdateStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateBookingStatusActivity.class);
            intent.putExtra("bookingId", bookingId);
            startActivityForResult(intent, REQUEST_UPDATE_STATUS);
        });

        // Load booking details
        loadBookingDetails();
    }

    private void loadBookingDetails() {
        progressBar.setVisibility(View.VISIBLE);

        bookingApiService.getBooking(this, bookingId, new BaseApiService.ApiCallback<BookingDetailResponse>() {
            @Override
            public void onSuccess(BookingDetailResponse data) {
                progressBar.setVisibility(View.GONE);
                if (data != null) {
                    displayBookingDetails(data);
                } else {
                    showError("Booking details not found");
                }
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                showError(errorMessage);
            }
        });
    }

    private void displayBookingDetails(BookingDetailResponse booking) {
        // Basic details
        tvBookingId.setText(String.valueOf(booking.getBookingId()));
        tvServiceName.setText(String.valueOf(booking.getServiceId())); // Should be service name
        tvStatus.setText(booking.getStatus());
        
        // Scheduled times
        String formattedStartTime = "";
        String formattedEndTime = "";
        
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.ENGLISH);
                java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());

                java.time.LocalDateTime startDateTime = java.time.LocalDateTime.parse(booking.getScheduledStartTime(), inputFormatter);
                java.time.LocalDateTime endDateTime = java.time.LocalDateTime.parse(booking.getScheduledEndTime(), inputFormatter);

                formattedStartTime = startDateTime.format(outputFormatter);
                formattedEndTime = endDateTime.format(outputFormatter);
            } else {
                // Fallback for older Android versions
                formattedStartTime = booking.getScheduledStartTime();
                formattedEndTime = booking.getScheduledEndTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
            formattedStartTime = booking.getScheduledStartTime();
            formattedEndTime = booking.getScheduledEndTime();
        }
        
        tvScheduledStart.setText(formattedStartTime);
        tvScheduledEnd.setText(formattedEndTime);
        
        // Actual times (if available)
        if (booking.getActualStartTime() != null && !booking.getActualStartTime().isEmpty()) {
            layoutActualStart.setVisibility(View.VISIBLE);
            
            String formattedActualStart = "";
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.ENGLISH);
                    java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
                    java.time.LocalDateTime actualStartDateTime = java.time.LocalDateTime.parse(booking.getActualStartTime(), inputFormatter);
                    formattedActualStart = actualStartDateTime.format(outputFormatter);
                } else {
                    formattedActualStart = booking.getActualStartTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
                formattedActualStart = booking.getActualStartTime();
            }
            
            tvActualStart.setText(formattedActualStart);
        } else {
            layoutActualStart.setVisibility(View.GONE);
        }
        
        if (booking.getActualEndTime() != null && !booking.getActualEndTime().isEmpty()) {
            layoutActualEnd.setVisibility(View.VISIBLE);
            
            String formattedActualEnd = "";
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.ENGLISH);
                    java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
                    java.time.LocalDateTime actualEndDateTime = java.time.LocalDateTime.parse(booking.getActualEndTime(), inputFormatter);
                    formattedActualEnd = actualEndDateTime.format(outputFormatter);
                } else {
                    formattedActualEnd = booking.getActualEndTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
                formattedActualEnd = booking.getActualEndTime();
            }
            
            tvActualEnd.setText(formattedActualEnd);
        } else {
            layoutActualEnd.setVisibility(View.GONE);
        }
        
        // Price details
        tvEstimatedPrice.setText(String.format("$%.2f", booking.getEstimatedPrice()));
        
        if (booking.getFinalPrice() != null) {
            layoutFinalPrice.setVisibility(View.VISIBLE);
            tvFinalPrice.setText(String.format("$%.2f", booking.getFinalPrice()));
        } else {
            layoutFinalPrice.setVisibility(View.GONE);
        }
        
        // Show update status button for helpers if booking is in progress or pending
        if (isHelper && ("InProgress".equals(booking.getStatus()) || "Pending".equals(booking.getStatus()))) {
            btnUpdateStatus.setVisibility(View.VISIBLE);
        } else {
            btnUpdateStatus.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_STATUS && resultCode == RESULT_OK) {
            // Refresh booking details
            loadBookingDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 