package com.example.homehelperfinder.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.BanUnbanRequest;
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
public class ProfileManagementActivity extends BaseActivity implements ProfileAdapter.OnProfileActionListener {
    private static final String TAG = "ProfileManagement";

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ProfileRepository repository;
    private List<ProfileModel> allProfiles;
    private int currentTabPosition = 0;

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
        adapter.setOnProfileActionListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Active Profiles"));
        tabLayout.addTab(tabLayout.newTab().setText("Banned Profiles"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                if (currentTabPosition == 0) {
                    loadActiveProfiles();
                } else if (currentTabPosition == 1) {
                    loadBannedProfiles();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
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

    @Override
    public void onBanProfile(ProfileModel profile) {
        showBanDialog(profile);
    }

    @Override
    public void onUnbanProfile(ProfileModel profile) {
        showUnbanDialog(profile);
    }

    private void showBanDialog(ProfileModel profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ban Profile");
        builder.setMessage("Are you sure you want to ban " + profile.getFullName() + "?");

        // Create EditText for reason
        final EditText reasonEditText = new EditText(this);
        reasonEditText.setHint("Enter reason for banning...");
        builder.setView(reasonEditText);

        builder.setPositiveButton("Ban", (dialog, which) -> {
            String reason = reasonEditText.getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                showToast("Please enter a reason for banning");
                return;
            }
            performBanProfile(profile, reason);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showUnbanDialog(ProfileModel profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unban Profile");
        builder.setMessage("Are you sure you want to unban " + profile.getFullName() + "?");

        // Create EditText for reason
        final EditText reasonEditText = new EditText(this);
        reasonEditText.setHint("Enter reason for unbanning...");
        builder.setView(reasonEditText);

        builder.setPositiveButton("Unban", (dialog, which) -> {
            String reason = reasonEditText.getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                showToast("Please enter a reason for unbanning");
                return;
            }
            performUnbanProfile(profile, reason);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void performBanProfile(ProfileModel profile, String reason) {
        showProgressDialog("Banning profile...");
        
        BanUnbanRequest request = new BanUnbanRequest(
                profile.getProfileId(),
                profile.getProfileType(),
                reason
        );

        repository.banProfile(this, request, new BaseApiService.ApiCallback<ProfileModel>() {
            @Override
            public void onSuccess(ProfileModel updatedProfile) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showToast("Profile banned successfully");
                    refreshCurrentTab();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to ban profile: " + errorMessage);
                    logError("Failed to ban profile", throwable);
                });
            }
        });
    }

    private void performUnbanProfile(ProfileModel profile, String reason) {
        showProgressDialog("Unbanning profile...");
        BanUnbanRequest request = new BanUnbanRequest(
                profile.getProfileId(),
                profile.getProfileType(),
                reason
        );

        repository.unbanProfile(this, request, new BaseApiService.ApiCallback<ProfileModel>() {
            @Override
            public void onSuccess(ProfileModel updatedProfile) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showToast("Profile unbanned successfully");
                    refreshCurrentTab();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to unban profile: " + errorMessage);
                    logError("Failed to unban profile", throwable);
                });
            }
        });
    }

    private void refreshCurrentTab() {
        if (currentTabPosition == 0) {
            loadActiveProfiles();
        } else if (currentTabPosition == 1) {
            loadBannedProfiles();
        }
    }
}