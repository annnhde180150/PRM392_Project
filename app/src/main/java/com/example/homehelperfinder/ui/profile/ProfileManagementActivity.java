package com.example.homehelperfinder.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
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

    // Bulk action UI components
    private Toolbar toolbar;
    private LinearLayout layoutBulkActions;
    private Button buttonSelectAll;
    private Button buttonBulkBan;
    private Button buttonBulkUnban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupTabLayout();
        setupBulkActions();
        loadActiveProfiles(); // Load active profiles by default
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        recyclerView = findViewById(R.id.recyclerViewProfiles);
        layoutBulkActions = findViewById(R.id.layoutBulkActions);
        buttonSelectAll = findViewById(R.id.buttonSelectAll);
        buttonBulkBan = findViewById(R.id.buttonBulkBan);
        buttonBulkUnban = findViewById(R.id.buttonBulkUnban);
        repository = new ProfileRepository();
        allProfiles = new ArrayList<>();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile Management");
        }
    }

    private void setupRecyclerView() {
        adapter = new ProfileAdapter(allProfiles);
        adapter.setOnProfileActionListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Add long click listener to enable selection mode
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, android.view.MotionEvent e) {
                return false;
            }
        });
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

    private void setupBulkActions() {
        buttonSelectAll.setOnClickListener(v -> {
            if (adapter.getSelectedCount() == allProfiles.size()) {
                adapter.clearSelection();
                buttonSelectAll.setText("Select All");
            } else {
                adapter.selectAll();
                buttonSelectAll.setText("Clear All");
            }
        });

        buttonBulkBan.setOnClickListener(v -> {
            List<ProfileModel> selectedProfiles = adapter.getSelectedProfiles();
            if (selectedProfiles.isEmpty()) {
                showToast("Please select profiles to ban");
                return;
            }
            showBulkBanDialog(selectedProfiles);
        });

        buttonBulkUnban.setOnClickListener(v -> {
            List<ProfileModel> selectedProfiles = adapter.getSelectedProfiles();
            if (selectedProfiles.isEmpty()) {
                showToast("Please select profiles to unban");
                return;
            }
            showBulkUnbanDialog(selectedProfiles);
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

    @Override
    public void onSelectionChanged(int selectedCount) {
        if (selectedCount > 0) {
            layoutBulkActions.setVisibility(android.view.View.VISIBLE);
            buttonSelectAll.setText(selectedCount == allProfiles.size() ? "Clear All" : "Select All");
        } else {
            layoutBulkActions.setVisibility(android.view.View.GONE);
            adapter.setSelectionMode(false);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select_mode) {
            toggleSelectionMode();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSelectionMode() {
        boolean newSelectionMode = !adapter.isSelectionMode();
        adapter.setSelectionMode(newSelectionMode);

        if (newSelectionMode) {
            layoutBulkActions.setVisibility(android.view.View.VISIBLE);
            showToast("Selection mode enabled. Tap checkboxes to select profiles.");
        } else {
            layoutBulkActions.setVisibility(android.view.View.GONE);
            showToast("Selection mode disabled.");
        }
    }

    private void showBulkBanDialog(List<ProfileModel> profiles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bulk Ban Profiles");
        builder.setMessage("Are you sure you want to ban " + profiles.size() + " profiles?");

        final EditText reasonEditText = new EditText(this);
        reasonEditText.setHint("Enter reason for banning...");
        builder.setView(reasonEditText);

        builder.setPositiveButton("Ban All", (dialog, which) -> {
            String reason = reasonEditText.getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                showToast("Please enter a reason for banning");
                return;
            }
            performBulkBanProfiles(profiles, reason);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showBulkUnbanDialog(List<ProfileModel> profiles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bulk Unban Profiles");
        builder.setMessage("Are you sure you want to unban " + profiles.size() + " profiles?");

        final EditText reasonEditText = new EditText(this);
        reasonEditText.setHint("Enter reason for unbanning...");
        builder.setView(reasonEditText);

        builder.setPositiveButton("Unban All", (dialog, which) -> {
            String reason = reasonEditText.getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                showToast("Please enter a reason for unbanning");
                return;
            }
            performBulkUnbanProfiles(profiles, reason);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void performBulkBanProfiles(List<ProfileModel> profiles, String reason) {
        showProgressDialog("Banning " + profiles.size() + " profiles...");

        List<BanUnbanRequest> requests = new ArrayList<>();
        for (ProfileModel profile : profiles) {
            requests.add(new BanUnbanRequest(
                    profile.getProfileId(),
                    profile.getProfileType(),
                    reason
            ));
        }

        repository.bulkBanProfiles(this, requests, new BaseApiService.ApiCallback<List<ProfileModel>>() {
            @Override
            public void onSuccess(List<ProfileModel> updatedProfiles) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showToast("Successfully banned " + updatedProfiles.size() + " profiles");
                    adapter.setSelectionMode(false);
                    layoutBulkActions.setVisibility(android.view.View.GONE);
                    refreshCurrentTab();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to ban profiles: " + errorMessage);
                    logError("Failed to bulk ban profiles", throwable);
                });
            }
        });
    }

    private void performBulkUnbanProfiles(List<ProfileModel> profiles, String reason) {
        showProgressDialog("Unbanning " + profiles.size() + " profiles...");

        List<BanUnbanRequest> requests = new ArrayList<>();
        for (ProfileModel profile : profiles) {
            requests.add(new BanUnbanRequest(
                    profile.getProfileId(),
                    profile.getProfileType(),
                    reason
            ));
        }


        repository.bulkUnbanProfiles(this, requests, new BaseApiService.ApiCallback<List<ProfileModel>>() {
            @Override
            public void onSuccess(List<ProfileModel> updatedProfiles) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showToast("Successfully unbanned " + updatedProfiles.size() + " profiles");
                    adapter.setSelectionMode(false);
                    layoutBulkActions.setVisibility(android.view.View.GONE);
                    refreshCurrentTab();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Failed to unban profiles: " + errorMessage);
                    logError("Failed to bulk unban profiles", throwable);
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