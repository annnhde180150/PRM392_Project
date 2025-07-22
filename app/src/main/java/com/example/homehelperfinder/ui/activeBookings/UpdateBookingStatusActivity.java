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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateBookingStatusActivity extends BaseActivity {
    private UserManager userManager;
    private int currentUserId;
    private RadioGroup radioGroupStatus;
    private RadioButton radioInProgress, radioCompleted, radioCancelled;
    private TextView tvStartTime, tvEndTime;
    private Button btnPickStartTime, btnPickEndTime, btnUpdate;
    private ProgressBar progressBar;
    
    private BookingApiService bookingApiService;
    private int bookingId;
    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking_status);

        // Initialize views
        radioGroupStatus = findViewById(R.id.radio_group_status);
        radioInProgress = findViewById(R.id.radio_in_progress);
        radioCompleted = findViewById(R.id.radio_completed);
        radioCancelled = findViewById(R.id.radio_cancelled);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        btnPickStartTime = findViewById(R.id.btn_pick_start_time);
        btnPickEndTime = findViewById(R.id.btn_pick_end_time);
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
        startDate = calendar.getTime();
        endDate = calendar.getTime();
        updateTimeDisplays();

        // Set click listeners
        btnPickStartTime.setOnClickListener(v -> showDateTimePicker(true));
        btnPickEndTime.setOnClickListener(v -> showDateTimePicker(false));
        btnUpdate.setOnClickListener(v -> updateBookingStatus());
        //
        userManager = UserManager.getInstance(this);
        currentUserId = userManager.getCurrentUserId();
    }

    private void showDateTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        if (isStartTime && startDate != null) {
            calendar.setTime(startDate);
        } else if (!isStartTime && endDate != null) {
            calendar.setTime(endDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(Calendar.YEAR, selectedYear);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, selectedHour, selectedMinute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);

                if (isStartTime) {
                    startDate = calendar.getTime();
                } else {
                    endDate = calendar.getTime();
                }

                updateTimeDisplays();
            }, hour, minute, true);

            timePickerDialog.show();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateTimeDisplays() {


        if (startDate != null) {
            tvStartTime.setText(DateUtils.formatDateTime(startDate));
        }

        if (endDate != null) {
            tvEndTime.setText(DateUtils.formatDateTime(endDate));
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
        request.setActualStartTime(DateUtils.formatDateTimeForApi(startDate));
        request.setActualEndTime(DateUtils.formatDateTimeForApi(endDate));

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