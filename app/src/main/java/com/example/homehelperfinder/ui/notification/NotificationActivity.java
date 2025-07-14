package com.example.homehelperfinder.ui.notification;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.NotificationModel;
import com.example.homehelperfinder.data.model.request.NotificationCreateRequest;
import com.example.homehelperfinder.data.remote.notification.NotificationApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.notification.adapter.NotificationAdapter;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.signalr.NotificationSignalRHelper;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing notifications
 */
public class NotificationActivity extends BaseActivity implements
        NotificationAdapter.OnNotificationClickListener,
        NotificationSignalRHelper.NotificationListener {

    private static final String TAG = "NotificationActivity";

    // UI Components
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewNotifications;
    private LinearLayout emptyStateLayout;
    private LinearLayout loadingLayout;
    private LinearLayout errorLayout;
    private TextView textViewError;
    private Button buttonRetry;
    private Button buttonMarkAllRead;
    private Button buttonRefresh;

    // Data and Adapters
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notifications;
    private NotificationApiService notificationApiService;
    private NotificationSignalRHelper signalRHelper;

    // User Info
    private Integer currentUserId;
    private Integer currentHelperId;
    private boolean isHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        setupClickListeners();
        loadUserInfo();
        initializeApiService();
        initializeSignalRHelper();
        loadNotifications();
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        errorLayout = findViewById(R.id.errorLayout);
        textViewError = findViewById(R.id.textViewError);
        buttonRetry = findViewById(R.id.buttonRetry);
        buttonMarkAllRead = findViewById(R.id.buttonMarkAllRead);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        notifications = new ArrayList<>();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Notifications");
        }
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        notificationAdapter = new NotificationAdapter(this, notifications, this);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(notificationAdapter);
    }

    /**
     * Setup SwipeRefreshLayout
     */
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshNotifications);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary_color,
                R.color.secondary_color,
                R.color.accent_color
        );
    }

    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        buttonRetry.setOnClickListener(v -> loadNotifications());
        buttonRefresh.setOnClickListener(v -> refreshNotifications());
        buttonMarkAllRead.setOnClickListener(v -> markAllNotificationsAsRead());

        // Add long click listener for testing
        buttonRefresh.setOnLongClickListener(v -> {
            createTestNotification();
            return true;
        });
    }

    /**
     * Load user information from SharedPreferences
     */
    private void loadUserInfo() {
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(this);

        // Get user ID as string and convert to Integer
        String userIdStr = prefsHelper.getUserId();
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                currentUserId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                Logger.e(TAG, "Failed to parse user ID: " + userIdStr, e);
                currentUserId = null;
            }
        }

        // Determine if user is helper and get helper ID
        String userType = prefsHelper.getUserType();
        isHelper = Constants.USER_TYPE_HELPER.equals(userType);

        if (isHelper && currentUserId != null) {
            currentHelperId = currentUserId; // For helpers, helper ID is same as user ID
        } else {
            currentHelperId = null;
        }

        Logger.d(TAG, "User info loaded - UserId: " + currentUserId +
                ", HelperId: " + currentHelperId + ", IsHelper: " + isHelper +
                ", UserType: " + userType);
    }

    /**
     * Initialize API service
     */
    private void initializeApiService() {
        notificationApiService = new NotificationApiService(this);
    }

    /**
     * Initialize SignalR helper for real-time notifications
     */
    private void initializeSignalRHelper() {
        signalRHelper = NotificationSignalRHelper.getInstance(this);
        signalRHelper.addNotificationListener(this);
        Logger.d(TAG, "SignalR helper initialized for real-time notifications");
    }

    /**
     * Load notifications from API
     */
    private void loadNotifications() {
        showLoadingState();

        if (isHelper && currentHelperId != null) {
            notificationApiService.getNotificationsByHelperId(currentHelperId, 
                    new NotificationApiService.NotificationListCallback() {
                @Override
                public void onSuccess(List<NotificationModel> notificationList) {
                    runOnUiThread(() -> handleNotificationsLoaded(notificationList));
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> handleLoadError(error));
                }
            });
        } else if (currentUserId != null) {
            notificationApiService.getNotificationsByUserId(currentUserId, 
                    new NotificationApiService.NotificationListCallback() {
                @Override
                public void onSuccess(List<NotificationModel> notificationList) {
                    runOnUiThread(() -> handleNotificationsLoaded(notificationList));
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> handleLoadError(error));
                }
            });
        } else {
            handleLoadError("User information not available");
        }
    }

    /**
     * Refresh notifications
     */
    private void refreshNotifications() {
        swipeRefreshLayout.setRefreshing(true);
        loadNotifications();
    }

    /**
     * Handle successful notifications loading
     */
    private void handleNotificationsLoaded(List<NotificationModel> notificationList) {
        swipeRefreshLayout.setRefreshing(false);
        
        notifications.clear();
        notifications.addAll(notificationList);
        notificationAdapter.notifyDataSetChanged();

        if (notifications.isEmpty()) {
            showEmptyState();
        } else {
            showContentState();
        }

        Logger.d(TAG, "Loaded " + notifications.size() + " notifications");
    }

    /**
     * Handle loading error
     */
    private void handleLoadError(String error) {
        swipeRefreshLayout.setRefreshing(false);
        showErrorState(error);
        Logger.e(TAG, "Failed to load notifications: " + error);
    }

    /**
     * Mark all notifications as read
     */
    private void markAllNotificationsAsRead() {
        if (isHelper && currentHelperId != null) {
            notificationApiService.markAllHelperNotificationsAsRead(currentHelperId, 
                    new NotificationApiService.SimpleCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(NotificationActivity.this, 
                                "All notifications marked as read", Toast.LENGTH_SHORT).show();
                        refreshNotifications();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(NotificationActivity.this, 
                                "Failed to mark notifications as read: " + error, 
                                Toast.LENGTH_LONG).show();
                    });
                }
            });
        } else if (currentUserId != null) {
            notificationApiService.markAllUserNotificationsAsRead(currentUserId, 
                    new NotificationApiService.SimpleCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(NotificationActivity.this, 
                                "All notifications marked as read", Toast.LENGTH_SHORT).show();
                        refreshNotifications();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(NotificationActivity.this, 
                                "Failed to mark notifications as read: " + error, 
                                Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    /**
     * Show loading state
     */
    private void showLoadingState() {
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerViewNotifications.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * Show content state
     */
    private void showContentState() {
        loadingLayout.setVisibility(View.GONE);
        recyclerViewNotifications.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * Show empty state
     */
    private void showEmptyState() {
        loadingLayout.setVisibility(View.GONE);
        recyclerViewNotifications.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * Show error state
     */
    private void showErrorState(String error) {
        loadingLayout.setVisibility(View.GONE);
        recyclerViewNotifications.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        textViewError.setText(error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // NotificationAdapter.OnNotificationClickListener implementation
    @Override
    public void onNotificationClick(NotificationModel notification) {
        // Mark notification as read if it's unread
        if (notification.isUnread()) {
            markNotificationAsRead(notification);
        }
        
        // Handle notification click based on type
        handleNotificationAction(notification);
    }

    @Override
    public void onNotificationLongClick(NotificationModel notification) {
        // Show context menu for notification actions
        showNotificationContextMenu(notification);
    }

    /**
     * Mark single notification as read
     */
    private void markNotificationAsRead(NotificationModel notification) {
        notificationApiService.markNotificationAsRead(notification.getNotificationId(), 
                new NotificationApiService.SimpleCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    notification.setIsRead(true);
                    notificationAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String error) {
                Logger.e(TAG, "Failed to mark notification as read: " + error);
            }
        });
    }

    /**
     * Handle notification action based on type
     */
    private void handleNotificationAction(NotificationModel notification) {
        // TODO: Implement navigation based on notification type
        // For now, just show a toast
        Toast.makeText(this, "Notification clicked: " + notification.getTitle(), 
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Show context menu for notification
     */
    private void showNotificationContextMenu(NotificationModel notification) {
        // TODO: Implement context menu with options like delete, mark as read/unread
        Toast.makeText(this, "Long clicked: " + notification.getTitle(),
                Toast.LENGTH_SHORT).show();
    }

    // NotificationSignalRHelper.NotificationListener implementation
    @Override
    public void onNotificationReceived(NotificationDetailsDto notification) {
        runOnUiThread(() -> {
            Logger.d(TAG, "Received real-time notification: " + notification.getTitle());

            // Convert SignalR notification to NotificationModel
            NotificationModel notificationModel = convertToNotificationModel(notification);
            if (notificationModel != null) {
                // Add to the top of the list
                notificationAdapter.addNotification(notificationModel);

                // Show toast for new notification
                Toast.makeText(this, "New notification: " + notification.getTitle(),
                        Toast.LENGTH_SHORT).show();

                // Update UI state if we were showing empty state
                if (notifications.size() == 1) {
                    showContentState();
                }
            }
        });
    }

    /**
     * Convert SignalR NotificationDetailsDto to NotificationModel
     */
    private NotificationModel convertToNotificationModel(NotificationDetailsDto dto) {
        if (dto == null) return null;

        NotificationModel model = new NotificationModel();
        model.setTitle(dto.getTitle());
        model.setMessage(dto.getMessage());
        model.setNotificationType(dto.getNotificationType());
        model.setReferenceId(dto.getReferenceId());
        model.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        model.setCreationTime(dto.getTimestamp());
        model.setSentTime(dto.getTimestamp());
        model.setRecipientUserId(dto.getUserId());
        model.setRecipientHelperId(dto.getHelperId());

        return model;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleanup SignalR helper
        if (signalRHelper != null) {
            signalRHelper.removeNotificationListener(this);
        }

        Logger.d(TAG, "NotificationActivity destroyed and cleaned up");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "NotificationActivity paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(TAG, "NotificationActivity resumed");

        // Refresh notifications when returning to the activity
        refreshNotifications();
    }

    /**
     * Create a test notification for testing purposes
     */
    private void createTestNotification() {
        if (currentUserId == null && currentHelperId == null) {
            Toast.makeText(this, "User information not available for testing", Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationCreateRequest request = new NotificationCreateRequest();
        request.setTitle("Test Notification");
        request.setMessage("This is a test notification created from the app");
        request.setNotificationType(NotificationModel.TYPE_SYSTEM);
        request.setReferenceId("test-" + System.currentTimeMillis());

        if (isHelper && currentHelperId != null) {
            request.setRecipientHelperId(currentHelperId);
        } else if (currentUserId != null) {
            request.setRecipientUserId(currentUserId);
        }

        notificationApiService.createNotification(request, new NotificationApiService.NotificationCallback() {
            @Override
            public void onSuccess(NotificationModel notification) {
                runOnUiThread(() -> {
                    Toast.makeText(NotificationActivity.this,
                            "Test notification created successfully", Toast.LENGTH_SHORT).show();
                    refreshNotifications();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(NotificationActivity.this,
                            "Failed to create test notification: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
