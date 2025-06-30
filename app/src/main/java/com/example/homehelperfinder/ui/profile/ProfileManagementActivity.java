package com.example.homehelperfinder.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ProfileModel;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.repository.ProfileRepository;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for managing and displaying profiles (active and banned)
 */
public class ProfileManagementActivity extends BaseActivity {
    private static final String TAG = "ProfileManagement";
    
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ProfileRepository repository;
    private List<ProfileModel> allProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        initViews();
        setupRecyclerView();
        setupTabLayout();
        loadActiveProfiles(); // Load active profiles by default
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        recyclerView = findViewById(R.id.recyclerViewProfiles);
        repository = new ProfileRepository();
        allProfiles = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new ProfileAdapter(allProfiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Active Profiles"));
        tabLayout.addTab(tabLayout.newTab().setText("Banned Profiles"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    loadActiveProfiles();
                } else if (position == 1) {
                    loadBannedProfiles();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadActiveProfiles() {
        showProgressDialog("Loading active profiles...");

        repository.getActiveProfiles(this, new BaseApiService.ApiCallback<List<ProfileModel>>() {
            @Override
            public void onSuccess(List<ProfileModel> profiles) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    if (profiles != null) {
                        allProfiles.clear();
                        allProfiles.addAll(profiles);
                        adapter.updateProfiles(allProfiles);
                        logInfo("Loaded " + profiles.size() + " active profiles");
                    }
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to load active profiles: " + errorMessage);
                    logError("Failed to load active profiles", throwable);
                });
            }
        });
    }

    private void loadBannedProfiles() {
        showProgressDialog("Loading banned profiles...");

        repository.getBannedProfiles(this, new BaseApiService.ApiCallback<List<ProfileModel>>() {
            @Override
            public void onSuccess(List<ProfileModel> profiles) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    if (profiles != null) {
                        allProfiles.clear();
                        allProfiles.addAll(profiles);
                        adapter.updateProfiles(allProfiles);
                        logInfo("Loaded " + profiles.size() + " banned profiles");
                    }
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to load banned profiles: " + errorMessage);
                    logError("Failed to load banned profiles", throwable);
                });
            }
        });
    }
} 