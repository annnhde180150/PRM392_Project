package com.example.homehelperfinder.ui.activeBookings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.serviceRequest.GetAllServiceRequestService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class ActiveBookingsActivity extends BaseActivity {
    public static final String EXTRA_MODE = "mode"; // "user" or "helper"
    public static final String EXTRA_ID = "id"; // userId or helperId
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ActiveBookingsAdapter adapter;
    private List<GetAllServiceRequestResponse> bookingList = new ArrayList<>();
    private GetAllServiceRequestService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_bookings);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ActiveBookingsAdapter(bookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        service = new GetAllServiceRequestService(this);

        adapter.setOnUpdateClickListener(bookingId -> {
            Intent intent1 = new Intent(ActiveBookingsActivity.this, UpdateBookingStatusActivity.class);
            intent1.putExtra("bookingId", bookingId);
            startActivity(intent1);
        });
        setupToolbar();
        Intent intent = getIntent();
        String mode = intent.getStringExtra(EXTRA_MODE);
        SharedPrefsHelper sharedPreferences = SharedPrefsHelper.getInstance(this);
        String id = sharedPreferences.getUserId();
        if (mode == null) {
            Toast.makeText(this, "Invalid parameters", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadBookings(mode, Integer.parseInt(id));
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Update booking status");
        }
    }

    private void loadBookings(String mode, int id) {
        BaseApiService.ApiCallback<List<GetAllServiceRequestResponse>> callback = new BaseApiService.ApiCallback<List<GetAllServiceRequestResponse>>() {
            @Override
            public void onSuccess(List<GetAllServiceRequestResponse> data) {
                bookingList.clear();
                if (data != null && !data.isEmpty()) {
                    bookingList.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                }
            }
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Toast.makeText(ActiveBookingsActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        };
        if (mode.equals("user")) {
            service.getActiveBookingsByUserId(this, id, callback);
        } else if (mode.equals("helper")) {
            service.getActiveBookingsByHelperId(this, id, callback);
        }
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