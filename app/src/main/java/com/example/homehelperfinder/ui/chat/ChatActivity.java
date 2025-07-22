package com.example.homehelperfinder.ui.chat;

import android.content.Intent;
import java.util.ArrayList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.MarkAsReadRequest;
import com.example.homehelperfinder.data.model.request.SendMessageRequest;
import com.example.homehelperfinder.data.model.response.ChatMessageResponse;
import com.example.homehelperfinder.data.model.response.MarkAsReadResponse;
import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.chat.ChatApiService;
import com.example.homehelperfinder.data.remote.signalr.SignalRCallback;
import com.example.homehelperfinder.data.remote.signalr.SignalRService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.chat.adapter.ChatMessageAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.ChatUtils;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.signalr.SignalRHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Activity for displaying and managing chat conversation
 */
public class ChatActivity extends BaseActivity {
    private static final String TAG = "ChatActivity";

    // UI Components
    private TextView tvParticipantName;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private ImageButton btnBack;
    private LinearLayout layoutEmptyState;

    // Data
    private ChatMessageAdapter adapter;
    private ChatApiService chatApiService;
    private SharedPrefsHelper prefsHelper;
    private String conversationId;
    private Integer bookingId;
    private Integer otherUserId;
    private Integer otherHelperId;
    private String participantName;

    // SignalR
    private SignalRService signalRService;
    private SignalRCallback signalRCallback;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();
        setupMenuNavigation();
        setupSignalR();
        loadConversation();
    }

    private void initViews() {
        tvParticipantName = findViewById(R.id.tvParticipantName);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
    }

    private void initData() {
        chatApiService = new ChatApiService(this);
        prefsHelper = SharedPrefsHelper.getInstance(this);

        // Get data from intent
        Intent intent = getIntent();
        conversationId = intent.getStringExtra(Constants.INTENT_CONVERSATION_ID);
        bookingId = intent.hasExtra(Constants.INTENT_BOOKING_ID) ?
                intent.getIntExtra(Constants.INTENT_BOOKING_ID, -1) : null;
        otherUserId = intent.hasExtra(Constants.INTENT_OTHER_USER_ID) ?
                intent.getIntExtra(Constants.INTENT_OTHER_USER_ID, -1) : null;
        otherHelperId = intent.hasExtra(Constants.INTENT_OTHER_HELPER_ID) ?
                intent.getIntExtra(Constants.INTENT_OTHER_HELPER_ID, -1) : null;
        participantName = intent.getStringExtra("participant_name");

        // Validate that we have either otherUserId or otherHelperId
        if (otherUserId == null && otherHelperId == null) {
            Logger.e(TAG, "No recipient specified - otherUserId: " + otherUserId + ", otherHelperId: " + otherHelperId);

            // Check if we have the old intent keys for backward compatibility
            if (intent.hasExtra("otherUserId")) {
                otherUserId = intent.getIntExtra("otherUserId", -1);
                if (otherUserId == -1) otherUserId = null;
                Logger.d(TAG, "Found otherUserId from legacy key: " + otherUserId);
            }

            if (intent.hasExtra("otherHelperId")) {
                otherHelperId = intent.getIntExtra("otherHelperId", -1);
                if (otherHelperId == -1) otherHelperId = null;
                Logger.d(TAG, "Found otherHelperId from legacy key: " + otherHelperId);
            }

            // Check participant name from legacy key
            if (participantName == null && intent.hasExtra("otherUserName")) {
                participantName = intent.getStringExtra("otherUserName");
                Logger.d(TAG, "Found participant name from legacy key: " + participantName);
            }

            // Final validation
            if (otherUserId == null && otherHelperId == null) {
                Toast.makeText(this, "Error: No recipient specified", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // Convert -1 to null for proper API calls
        if (conversationId != null && conversationId.equals("-1")) conversationId = null;
        if (bookingId != null && bookingId == -1) bookingId = null;
        if (otherUserId != null && otherUserId == -1) otherUserId = null;
        if (otherHelperId != null && otherHelperId == -1) otherHelperId = null;

        // Update participant name after processing legacy keys
        if (participantName != null && !participantName.isEmpty()) {
            tvParticipantName.setText(participantName);
        } else {
            tvParticipantName.setText("Chat");
        }
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> sendMessage());

        btnBack.setOnClickListener(v -> finish());

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setEnabled(ChatUtils.isValidMessageContent(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Initially disable send button
        btnSend.setEnabled(false);
    }

    private void loadConversation() {
        showLoading("Loading messages...");

        chatApiService.getConversationMessages(
                this,
                bookingId,
                otherUserId,
                otherHelperId,
                ApiHelper.callbackWithContext(
                        this,
                        this::onMessagesLoaded,
                        this::onError
                )
        );
    }

    private void onMessagesLoaded(List<ChatMessageResponse> messages) {
        hideLoading();

        if (messages != null) {
            adapter.setMessages(messages);

            if (!messages.isEmpty()) {
                rvMessages.scrollToPosition(messages.size() - 1);
                markMessagesAsRead(messages);
                hideEmptyState();
            } else {
                showEmptyState();
            }
        } else {
            // Handle null messages list
            adapter.setMessages(new ArrayList<>());
            showEmptyState();
        }
    }

    private void markMessagesAsRead(List<ChatMessageResponse> messages) {
        List<Long> unreadMessageIds = new ArrayList<>();
        String currentUserId = prefsHelper.getUserId();
        String currentUserType = prefsHelper.getUserType();

        for (ChatMessageResponse message : messages) {
            // Only mark messages as read if they're not from current user and not already read
            if (!ChatUtils.isMessageFromCurrentUser(
                    message.getSenderUserId(),
                    message.getSenderHelperId(),
                    currentUserType,
                    currentUserId) &&
                    (message.getIsReadByReceiver() == null || !message.getIsReadByReceiver())) {
                unreadMessageIds.add(message.getChatId());
            }
        }

        if (!unreadMessageIds.isEmpty()) {
            MarkAsReadRequest request = new MarkAsReadRequest(unreadMessageIds);
            chatApiService.markMessagesAsRead(this, request, new BaseApiService.ApiCallback<MarkAsReadResponse>() {
                @Override
                public void onSuccess(MarkAsReadResponse data) {
                    Logger.d(TAG, "Messages marked as read successfully");
                }

                @Override
                public void onError(String errorMessage, Throwable throwable) {
                    Logger.e(TAG, "Failed to mark messages as read: " + errorMessage);
                }
            });
        }
    }

    private void sendMessage() {
        String messageContent = etMessage.getText().toString().trim();

        if (!ChatUtils.isValidMessageContent(messageContent)) {
            Toast.makeText(this, "Please enter a valid message", Toast.LENGTH_SHORT).show();
            return;
        }

        SendMessageRequest request = new SendMessageRequest();
        request.setBookingId(bookingId);
        request.setReceiverUserId(otherUserId);
        request.setReceiverHelperId(otherHelperId);
        request.setMessageContent(messageContent);

        // Clear the input immediately for better UX
        etMessage.setText("");
        btnSend.setEnabled(false);

        chatApiService.sendMessage(
                this,
                request,
                ApiHelper.callbackWithContext(
                        this,
                        this::onMessageSent,
                        this::onSendError
                )
        );
    }

    private void onMessageSent(ChatMessageResponse message) {
        adapter.addMessage(message);
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
        hideEmptyState(); // Hide empty state when first message is sent
        Logger.d(TAG, "Message sent successfully");
    }

    private void onSendError(String errorMessage, Throwable throwable) {
        Toast.makeText(this, "Failed to send message: " + errorMessage, Toast.LENGTH_SHORT).show();
        Logger.e(TAG, "Failed to send message: " + errorMessage, throwable);
    }

    private void onError(String errorMessage, Throwable throwable) {
        hideLoading();
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Logger.e(TAG, "Error: " + errorMessage, throwable);
    }

    private void showEmptyState() {
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyState() {
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.GONE);
        }
    }

    /**
     * Setup SignalR for real-time messaging
     */
    private void setupSignalR() {
        try {
            signalRService = SignalRHelper.getService(this);

            // Create SignalR callback for this chat
            signalRCallback = new SignalRService.ChatCallback() {
                @Override
                public void onNewMessage(ChatMessageDto message) {
                    handleRealTimeMessage(message);
                }

                @Override
                public void onConnected(String connectionId) {
                    Logger.d(TAG, "SignalR connected in chat: " + connectionId);
                    joinConversation();
                }

                @Override
                public void onDisconnected(String reason) {
                    Logger.w(TAG, "SignalR disconnected in chat: " + reason);
                }

                @Override
                public void onError(String error, Exception exception) {
                    Logger.e(TAG, "SignalR error in chat: " + error, exception);
                }
            };

            // Add callback to SignalR service
            signalRService.addCallback(signalRCallback);

            // Connect SignalR if not connected. The onConnected callback will handle joining the conversation.
            if (signalRService.isConnected()) {
                Logger.d(TAG, "SignalR is already connected. Joining conversation.");
                joinConversation();
            } else {
                Logger.d(TAG, "SignalR is not connected. Initializing connection...");
                // Initialize and connect SignalR. The callback's onConnected will be triggered.
                Disposable disposable = SignalRHelper.initialize(this)
                        .subscribe(
                                () -> Logger.d(TAG, "SignalR initialization process started."),
                                throwable -> Logger.e(TAG, "Failed to initialize SignalR in chat", throwable)
                        );
                disposables.add(disposable);
            }

            Logger.d(TAG, "SignalR setup initiated for chat");

        } catch (Exception e) {
            Logger.e(TAG, "Error setting up SignalR", e);
        }
    }

    /**
     * Join the conversation for real-time updates
     */
    private void joinConversation() {
        try {
            Disposable disposable = SignalRHelper.joinConversation(this, conversationId)
                    .subscribe(
                            () -> Logger.d(TAG, "Successfully joined conversation"),
                            throwable -> Logger.e(TAG, "Failed to join conversation", throwable)
                    );
            disposables.add(disposable);
        } catch (Exception e) {
            Logger.e(TAG, "Error joining conversation", e);
        }
    }

    /**
     * Leave the conversation
     */
    private void leaveConversation() {
        try {
            Disposable disposable = SignalRHelper.leaveConversation(this, conversationId)
                    .subscribe(
                            () -> Logger.d(TAG, "Successfully left conversation"),
                            throwable -> Logger.e(TAG, "Failed to leave conversation", throwable)
                    );
            disposables.add(disposable);
        } catch (Exception e) {
            Logger.e(TAG, "Error leaving conversation", e);
        }
    }

    /**
     * Handle real-time message received via SignalR
     */
    private void handleRealTimeMessage(ChatMessageDto message) {
        try {
            // Check if message is for this conversation
            boolean isForThisConversation = false;

            if (bookingId != null && bookingId.equals(message.getBookingId())) {
                isForThisConversation = true;
            } else if (otherUserId != null && otherUserId.equals(message.getSenderUserId())) {
                isForThisConversation = true;
            } else if (otherHelperId != null && otherHelperId.equals(message.getSenderHelperId())) {
                isForThisConversation = true;
            }

            if (isForThisConversation) {
                // Convert SignalR DTO to ChatMessageResponse
                ChatMessageResponse chatMessage = convertSignalRMessageToResponse(message);

                // Add message to adapter on UI thread
                runOnUiThread(() -> {
                    adapter.addMessage(chatMessage);
                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                    hideEmptyState(); // Hide empty state when real-time message is received
                    Logger.d(TAG, "Real-time message added to chat");
                });

                // Mark message as read
                markMessageAsRead(message.getChatId());
            }

        } catch (Exception e) {
            Logger.e(TAG, "Error handling real-time message", e);
        }
    }

    /**
     * Convert SignalR ChatMessageDto to ChatMessageResponse
     */
    private ChatMessageResponse convertSignalRMessageToResponse(ChatMessageDto dto) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setChatId(dto.getChatId());
        response.setBookingId(dto.getBookingId());
        response.setSenderUserId(dto.getSenderUserId());
        response.setSenderHelperId(dto.getSenderHelperId());
        response.setReceiverUserId(dto.getReceiverUserId());
        response.setReceiverHelperId(dto.getReceiverHelperId());
        response.setMessageContent(dto.getMessageContent());
        response.setTimestamp(dto.getTimestamp());
        response.setIsReadByReceiver(dto.getIsReadByReceiver());
        response.setReadTimestamp(dto.getReadTimestamp());
        response.setIsModerated(dto.getIsModerated());
        response.setModeratorAdminId(dto.getModeratorAdminId());
        response.setSenderName(dto.getSenderName());
        response.setSenderProfilePicture(dto.getSenderProfilePicture());
        response.setSenderType(dto.getSenderType());
        return response;
    }

    /**
     * Mark a single message as read
     */
    private void markMessageAsRead(Long chatId) {
        try {
            List<Long> chatIds = new ArrayList<>();
            chatIds.add(chatId);

            MarkAsReadRequest request = new MarkAsReadRequest();
            request.setChatIds(chatIds);

            chatApiService.markMessagesAsRead(this, request, new BaseApiService.ApiCallback<MarkAsReadResponse>() {
                @Override
                public void onSuccess(MarkAsReadResponse data) {
                    Logger.d(TAG, "Message marked as read: " + chatId);
                }

                @Override
                public void onError(String errorMessage, Throwable throwable) {
                    Logger.e(TAG, "Failed to mark message as read: " + errorMessage, throwable);
                }
            });

        } catch (Exception e) {
            Logger.e(TAG, "Error marking message as read", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Leave conversation and cleanup SignalR
        if (signalRService != null && signalRCallback != null) {
            leaveConversation();
            signalRService.removeCallback(signalRCallback);
            Logger.d(TAG, "SignalR callback removed and conversation left");
        }
        disposables.clear();
    }
}
