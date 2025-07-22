package com.example.homehelperfinder.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ChatConversationResponse;
import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.remote.chat.ChatApiService;
import com.example.homehelperfinder.data.remote.signalr.SignalRCallback;
import com.example.homehelperfinder.data.remote.signalr.SignalRService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.chat.adapter.ConversationsAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.signalr.SignalRHelper;

import java.util.List;

/**
 * Activity for displaying list of conversations
 */
public class ConversationsActivity extends BaseActivity implements ConversationsAdapter.OnConversationClickListener {
    private static final String TAG = "ConversationsActivity";

    // UI Components
    private RecyclerView rvConversations;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvEmptyState;
    private ImageButton btnBack;
    private LinearLayout searchBar;

    // Data
    private ConversationsAdapter adapter;
    private ChatApiService chatApiService;

    // SignalR
    private SignalRService signalRService;
    private SignalRCallback signalRCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        initViews();
        initData();
        setupRecyclerView();
        setupSwipeRefresh();
        setupMenuNavigation();
        setupSignalR();
        loadConversations();
    }

    private void initViews() {
        rvConversations = findViewById(R.id.rvConversations);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnBack = findViewById(R.id.btnBack);
        searchBar = findViewById(R.id.searchBar);

        // Setup search bar click listener
        if (searchBar != null) {
            searchBar.setOnClickListener(v -> openSearchChat());
        }
    }

    private void initData() {
        chatApiService = new ChatApiService(this);
    }

    private void setupRecyclerView() {
        adapter = new ConversationsAdapter(this);
        adapter.setOnConversationClickListener(this);
        rvConversations.setLayoutManager(new LinearLayoutManager(this));
        rvConversations.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadConversations);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent
        );

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadConversations() {
        if (!swipeRefreshLayout.isRefreshing()) {
            showLoading("Loading conversations...");
        }

        chatApiService.getConversations(
                this,
                ApiHelper.callbackWithContext(
                        this,
                        this::onConversationsLoaded,
                        this::onError
                )
        );
    }

    private void onConversationsLoaded(List<ChatConversationResponse> conversations) {
        hideLoading();
        swipeRefreshLayout.setRefreshing(false);

        adapter.setConversations(conversations);

        if (conversations == null || conversations.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
        }
    }

    private void onError(String errorMessage, Throwable throwable) {
        hideLoading();
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Logger.e(TAG, "Error loading conversations: " + errorMessage, throwable);
        
        // Show empty state if no conversations are loaded
        if (adapter.getItemCount() == 0) {
            showEmptyState();
        }
    }

    private void showEmptyState() {
        tvEmptyState.setVisibility(View.VISIBLE);
        rvConversations.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        tvEmptyState.setVisibility(View.GONE);
        rvConversations.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConversationClick(ChatConversationResponse conversation) {
        Intent intent = new Intent(this, ChatActivity.class);
        
        // Pass conversation data to ChatActivity
        if (conversation.getBookingId() != null) {
            intent.putExtra(Constants.INTENT_BOOKING_ID, conversation.getBookingId());
        }
        
        if (conversation.getParticipantUserId() != null) {
            intent.putExtra(Constants.INTENT_OTHER_USER_ID, conversation.getParticipantUserId());
        }
        
        if (conversation.getParticipantHelperId() != null) {
            intent.putExtra(Constants.INTENT_OTHER_HELPER_ID, conversation.getParticipantHelperId());
        }
        
        intent.putExtra("participant_name", conversation.getParticipantName());
        intent.putExtra(Constants.INTENT_CONVERSATION_ID, conversation.getConversationId());
        
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh conversations when returning to this activity
        loadConversations();
    }

    /**
     * Setup SignalR for real-time conversation updates
     */
    private void setupSignalR() {
        try {
            signalRService = SignalRHelper.getService(this);

            // Create SignalR callback for conversations
            signalRCallback = new SignalRCallback.SimpleCallback() {
                @Override
                public void onNewMessage(ChatMessageDto message) {
                    // Refresh conversations when new message arrives
                    runOnUiThread(() -> {
                        Logger.d(TAG, "New message received, refreshing conversations");
                        loadConversations();
                    });
                }

                @Override
                public void onNewNotification(NotificationDetailsDto notification) {
                    // Refresh conversations for chat-related notifications
                    if (notification.isChatMessage()) {
                        runOnUiThread(() -> {
                            Logger.d(TAG, "Chat notification received, refreshing conversations");
                            loadConversations();
                        });
                    }
                }

                @Override
                public void onConnected(String connectionId) {
                    Logger.d(TAG, "SignalR connected in conversations: " + connectionId);
                }

                @Override
                public void onDisconnected(String reason) {
                    Logger.w(TAG, "SignalR disconnected in conversations: " + reason);
                }

                @Override
                public void onError(String error, Exception exception) {
                    Logger.e(TAG, "SignalR error in conversations: " + error, exception);
                }
            };

            // Add callback to SignalR service
            signalRService.addCallback(signalRCallback);

            Logger.d(TAG, "SignalR setup completed for conversations");

        } catch (Exception e) {
            Logger.e(TAG, "Error setting up SignalR", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleanup SignalR
        if (signalRService != null && signalRCallback != null) {
            signalRService.removeCallback(signalRCallback);
            Logger.d(TAG, "SignalR callback removed from conversations");
        }
    }

    /**
     * Open search chat activity
     */
    private void openSearchChat() {
        Intent intent = new Intent(this, SearchChatActivity.class);
        startActivity(intent);
    }
}
