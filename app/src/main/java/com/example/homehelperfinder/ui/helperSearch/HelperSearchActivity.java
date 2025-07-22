package com.example.homehelperfinder.ui.helperSearch;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperSearchResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.helper.HelperApiService;
import com.example.homehelperfinder.data.remote.service.ServiceApiService;
import com.example.homehelperfinder.data.remote.favorite.FavoriteHelperApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.bookHelper.BookHelperActivity;
import com.example.homehelperfinder.ui.helperSearch.adapter.HelperSearchAdapter;
import com.example.homehelperfinder.ui.helperSearch.adapter.ServiceSelectionAdapter;
import com.example.homehelperfinder.utils.LocationUtils;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.UserManager;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class HelperSearchActivity extends BaseActivity implements ServiceSelectionAdapter.OnServiceClickListener, HelperSearchAdapter.OnHelperClickListener {
    
    private static final String TAG = "HelperSearchActivity";
    
    // UI Components
    private Toolbar toolbar;
    private RecyclerView rvServices;
    private RecyclerView rvHelpers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialTextView tvEmptyState;
    private MaterialTextView tvSelectedService;
    
    // Adapters
    private ServiceSelectionAdapter serviceAdapter;
    private HelperSearchAdapter helperAdapter;
    
    // Services
    private ServiceApiService serviceApiService;
    private HelperApiService helperApiService;
    private FavoriteHelperApiService favoriteHelperApiService;
    
    // Data
    private List<ServiceResponse> serviceList = new ArrayList<>();
    private List<HelperSearchResponse> helperList = new ArrayList<>();
    private ServiceResponse selectedService;
    
    // Location
    private Double currentLatitude;
    private Double currentLongitude;
    private static final double DEFAULT_RADIUS = 10.0; // 10km radius
    
    // Pagination
    private int currentPage = 1;
    private static final int PAGE_SIZE = 20;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_search);
        
        initViews();
        setupToolbar();
        setupRecyclerViews();
        setupSwipeRefresh();
        setupServices();
        
        // Get current location
        getCurrentLocation();
        
        // Load services
        loadServices();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvServices = findViewById(R.id.rvServices);
        rvHelpers = findViewById(R.id.rvHelpers);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvSelectedService = findViewById(R.id.tvSelectedService);
        
        serviceApiService = new ServiceApiService();
        helperApiService = new HelperApiService();
        favoriteHelperApiService = new FavoriteHelperApiService();
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Find Helpers");
        }
    }
    
    private void setupRecyclerViews() {
        // Setup services recycler view
        serviceAdapter = new ServiceSelectionAdapter(serviceList, this);
        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setAdapter(serviceAdapter);
        
        // Setup helpers recycler view
        helperAdapter = new HelperSearchAdapter(helperList, this);
        rvHelpers.setLayoutManager(new LinearLayoutManager(this));
        rvHelpers.setAdapter(helperAdapter);
    }
    
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (selectedService != null) {
                refreshHelpers();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    
    private void setupServices() {
        // This will be called after services are loaded
    }
    
    private void getCurrentLocation() {
        LocationUtils.getCurrentLocation(this, new LocationUtils.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                Logger.d(TAG, "Location received: " + latitude + ", " + longitude);
            }
            
            @Override
            public void onLocationError(String error) {
                Logger.e(TAG, "Location error: " + error);
                // Use default location (can be set to a default city)
                currentLatitude = 16.0544; // Da Nang default
                currentLongitude = 108.2022;
            }
        });
    }
    
    private void loadServices() {
        showLoading("Loading services...");
        
        serviceApiService.getActiveServices(this, new BaseApiService.ApiCallback<List<ServiceResponse>>() {
            @Override
            public void onSuccess(List<ServiceResponse> services) {
                runOnUiThread(() -> {
                    hideLoading();
                    serviceList.clear();
                    serviceList.addAll(services);
                    serviceAdapter.notifyDataSetChanged();
                    
                    // Auto-select first service if available
                    if (!serviceList.isEmpty()) {
                        onServiceSelected(serviceList.get(0));
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(HelperSearchActivity.this, 
                            "Failed to load services: " + errorMessage, 
                            Toast.LENGTH_LONG).show();
                    Logger.e(TAG, "Error loading services", throwable);
                });
            }
        });
    }
    
    private void loadHelpers() {
        if (selectedService == null || currentLatitude == null || currentLongitude == null) {
            return;
        }
        
        if (isLoading || !hasMoreData) {
            return;
        }
        
        isLoading = true;
        
        helperApiService.searchHelpers(
                this,
                selectedService.getServiceId(),
                currentPage,
                PAGE_SIZE,
                new BaseApiService.ApiCallback<List<HelperSearchResponse>>() {
                    @Override
                    public void onSuccess(List<HelperSearchResponse> helpers) {
                        runOnUiThread(() -> {
                            isLoading = false;
                            swipeRefreshLayout.setRefreshing(false);
                            
                            if (currentPage == 1) {
                                helperList.clear();
                            }
                            
                            if (helpers != null && !helpers.isEmpty()) {
                                helperList.addAll(helpers);
                                hasMoreData = helpers.size() == PAGE_SIZE;
                                currentPage++;
                            } else {
                                hasMoreData = false;
                            }
                            
                            helperAdapter.notifyDataSetChanged();
                            updateEmptyState();
                        });
                    }
                    
                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        runOnUiThread(() -> {
                            isLoading = false;
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(HelperSearchActivity.this, 
                                    "Failed to load helpers: " + errorMessage, 
                                    Toast.LENGTH_LONG).show();
                            Logger.e(TAG, "Error loading helpers", throwable);
                            updateEmptyState();
                        });
                    }
                }
        );
    }
    
    private void refreshHelpers() {
        currentPage = 1;
        hasMoreData = true;
        loadHelpers();
    }
    
    private void updateEmptyState() {
        if (helperList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvHelpers.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvHelpers.setVisibility(View.VISIBLE);
        }
    }
    
    // ServiceSelectionAdapter.OnServiceClickListener
    @Override
    public void onServiceSelected(ServiceResponse service) {
        selectedService = service;
        tvSelectedService.setText("Selected: " + service.getServiceName());
        
        // Reset pagination and load helpers
        currentPage = 1;
        hasMoreData = true;
        helperList.clear();
        helperAdapter.notifyDataSetChanged();
        
        loadHelpers();
    }
    
    // HelperSearchAdapter.OnHelperClickListener
    @Override
    public void onHelperSelected(HelperSearchResponse helper) {

    }
    
    @Override
    public void onHelperContact(HelperSearchResponse helper) {
        // Navigate to chat or contact helper
        // You can implement chat functionality here
        Toast.makeText(this, "Contacting " + helper.getHelperName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BookHelperActivity.class);
        intent.putExtra("helperId", helper.getHelperId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onAddFavorite(HelperSearchResponse helper) {
        try {
            int userId = UserManager.getInstance(this).getCurrentUserId();
            int helperId = helper.getHelperId();
            if (userId == -1 || helperId == 0) {
                Toast.makeText(this, "Invalid user or helper ID", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject json = new JSONObject();
            json.put("userId", userId);
            json.put("helperId", helperId);
            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            favoriteHelperApiService.addFavorite(this, body, new BaseApiService.ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Toast.makeText(HelperSearchActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(String error, Throwable throwable) {
                    Toast.makeText(HelperSearchActivity.this, "Failed to add favorite: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error preparing request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
} 