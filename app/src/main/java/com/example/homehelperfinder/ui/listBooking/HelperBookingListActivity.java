package com.example.homehelperfinder.ui.listBooking;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ListBookingModel;
import com.example.homehelperfinder.data.model.request.ServiceRequestUpdateStatusRequest;
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.serviceRequest.GetAllServiceRequestService;
import com.example.homehelperfinder.ui.listBooking.adapter.BookingAdapter;
import com.example.homehelperfinder.utils.DateUtils;
import com.example.homehelperfinder.utils.UserManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class HelperBookingListActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<ListBookingModel> bookingList;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GetAllServiceRequestService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helper_booking_list);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        loadBookings();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewBookings);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        bookingList = new ArrayList<>();
        service = new GetAllServiceRequestService(this);
        
        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadBookings);
    }

    private void setupRecyclerView() {
        adapter = new BookingAdapter(bookingList);
        adapter.setOnBookingActionListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadBookings() {
        if (!swipeRefreshLayout.isRefreshing()) {
            showLoading(true);
        }
        
        UserManager userManager = UserManager.getInstance(this);
//        int helperId = userManager.getCurrentUserId();
        int helperId = 1; //implement for test
        if (helperId == -1) {
            showLoading(false);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_LONG).show();
            return;
        }
        
        service.getAllBookingsByHelperId(this, helperId, new BaseApiService.ApiCallback<List<GetAllServiceRequestResponse>>() {
            @Override
            public void onSuccess(List<GetAllServiceRequestResponse> dataList) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                bookingList.clear();

                if (dataList != null && !dataList.isEmpty()) {
                    for (GetAllServiceRequestResponse data : dataList) {
                        // Format the start and end date for display
                        String formattedStartTime = data.getScheduledStartTime();
                        String formattedEndTime = data.getScheduledEndTime();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.ENGLISH);
                                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault());

                                LocalDateTime startDateTime = LocalDateTime.parse(data.getScheduledStartTime(), inputFormatter);
                                LocalDateTime endDateTime = LocalDateTime.parse(data.getScheduledEndTime(), inputFormatter);

                                formattedStartTime = startDateTime.format(outputFormatter);
                                formattedEndTime = endDateTime.format(outputFormatter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            formattedStartTime = "Invalid date";
                            formattedEndTime = "Invalid date";
                        }

                        ListBookingModel booking = new ListBookingModel(
                                data.getBookingId(),
                                data.getRequestId(),
                                data.getServiceName(),
                                data.getEstimatedPrice() != null ? data.getEstimatedPrice().toString() : "0",
                                data.getFullAddress(),
                                data.getFullName(),
                                formattedStartTime,
                                formattedEndTime

                        );
                        bookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                    showEmptyState(false);
                } else {
                    showEmptyState(true);
                }
                
                Log.d("HelperBookingList", "Loaded " + bookingList.size() + " bookings");
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                showEmptyState(true);
                Toast.makeText(HelperBookingListActivity.this, 
                    "Lỗi tải danh sách: " + errorMessage, Toast.LENGTH_LONG).show();
                Log.e("HelperBookingList", "Error loading bookings: " + errorMessage, throwable);
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmptyState(boolean show) {
        if (tvEmptyState != null) {
            tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    @Override
    public void onAcceptBooking(int requestId,int bookingId) {

        // Refresh the list after accepting
        ServiceRequestUpdateStatusRequest request = new ServiceRequestUpdateStatusRequest();
        request.setBookingId(bookingId);
        request.setRequestId(requestId);
        request.setAction("Accept");
        service.updateRequestStatus(this, request, new BaseApiService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                loadBookings();
                Toast.makeText(HelperBookingListActivity.this, "Đã chấp nhận đơn hàng #" + requestId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Toast.makeText(HelperBookingListActivity.this, "Lỗi không thể cập nhật trạng thái" + requestId, Toast.LENGTH_SHORT).show();
            }
        });
        loadBookings();
        Toast.makeText(HelperBookingListActivity.this, "Đã chấp nhận đơn hàng #" + requestId, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDeclineBooking(int requestId,int bookingId) {
        // Refresh the list after declining
        ServiceRequestUpdateStatusRequest request = new ServiceRequestUpdateStatusRequest();
        request.setBookingId(bookingId);
        request.setRequestId(requestId);
        request.setAction("Cancel");
        service.updateRequestStatus(this, request, new BaseApiService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Toast.makeText(HelperBookingListActivity.this, "Lỗi không thể cập nhật trạng thái" + requestId, Toast.LENGTH_SHORT).show();
            }
        });
        loadBookings();
        Toast.makeText(HelperBookingListActivity.this, "Đã hủy đơn hàng #" + requestId, Toast.LENGTH_SHORT).show();
    }
}