package com.example.homehelperfinder.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ProfileModel;
import com.example.homehelperfinder.data.repository.ProfileRepository;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for managing and displaying profiles (active and banned)
 */
public class ProfileManagementActivity extends AppCompatActivity {
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
        repository.getActiveProfiles()
                .thenAccept(profiles -> runOnUiThread(() -> {
                    if (profiles != null) {
                        allProfiles.clear();
                        allProfiles.addAll(profiles);
                        adapter.updateProfiles(allProfiles);
                        Log.d(TAG, "Loaded " + profiles.size() + " active profiles");
                    }
                }))
                .exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        String errorMessage = "Failed to load active profiles: " + throwable.getMessage();
                        Toast.makeText(ProfileManagementActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, errorMessage, throwable);
                    });
                    return null;
                });
    }

    private void loadBannedProfiles() {
        repository.getBannedProfiles()
                .thenAccept(profiles -> runOnUiThread(() -> {
                    if (profiles != null) {
                        allProfiles.clear();
                        allProfiles.addAll(profiles);
                        adapter.updateProfiles(allProfiles);
                        Log.d(TAG, "Loaded " + profiles.size() + " banned profiles");
                    }
                }))
                .exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        String errorMessage = "Failed to load banned profiles: " + throwable.getMessage();
                        Toast.makeText(ProfileManagementActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, errorMessage, throwable);
                    });
                    return null;
                });
    }
} 