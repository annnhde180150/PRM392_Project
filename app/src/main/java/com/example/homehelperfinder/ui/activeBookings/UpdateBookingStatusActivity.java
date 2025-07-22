package com.example.homehelperfinder.ui.activeBookings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.BookingStatusUpdateRequest;
import com.example.homehelperfinder.data.model.response.BookingDetailResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.booking.BookingApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.DateUtils;
import com.example.homehelperfinder.utils.UserManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateBookingStatusActivity extends BaseActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private Toolbar toolbar;
    private UserManager userManager;
    private int currentUserId;
    private RadioGroup radioGroupStatus;
    private RadioButton radioInProgress, radioCompleted, radioCancelled;
    private TextView tvStartTime, tvEndTime;
    private Button btnPickStartTime, btnPickEndTime, btnUpdate;
    private ProgressBar progressBar;
    
    private BookingApiService bookingApiService;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking_status);

        // Initialize views
        radioGroupStatus = findViewById(R.id.radio_group_status);
        radioInProgress = findViewById(R.id.radio_in_progress);
        radioCompleted = findViewById(R.id.radio_completed);
        radioCancelled = findViewById(R.id.radio_cancelled);
        btnUpdate = findViewById(R.id.btn_update);
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

        // Set current time as default
        Calendar calendar = Calendar.getInstance();


        // Set click listeners
        btnUpdate.setOnClickListener(v -> updateBookingStatus());
        //
        userManager = UserManager.getInstance(this);
        currentUserId = userManager.getCurrentUserId();
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Update booking status");
        }
    }

    private void updateBookingStatus() {
        // Validate inputs
        if (radioGroupStatus.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a status", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected status
        String status;
        if (radioInProgress.isChecked()) {
            status = "InProgress";
        } else if (radioCompleted.isChecked()) {
            status = "Completed";
        } else {
            status = "Cancelled";
        }

        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        btnUpdate.setEnabled(false);

        // Create request
        BookingStatusUpdateRequest request = new BookingStatusUpdateRequest();
        request.setBookingId(bookingId);
        request.setHelperId(currentUserId);
        request.setStatus(status);
        // Send API request
        bookingApiService.updateBookingStatus(this, bookingId, request, new BaseApiService.ApiCallback<BookingDetailResponse>() {
            @Override
            public void onSuccess(BookingDetailResponse data) {
                progressBar.setVisibility(View.GONE);
                btnUpdate.setEnabled(true);
                Toast.makeText(UpdateBookingStatusActivity.this, "Booking status updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                btnUpdate.setEnabled(true);
                Toast.makeText(UpdateBookingStatusActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
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