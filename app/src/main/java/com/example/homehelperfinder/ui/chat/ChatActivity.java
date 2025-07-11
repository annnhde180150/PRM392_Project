package com.example.homehelperfinder.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.MarkAsReadRequest;
import com.example.homehelperfinder.data.model.request.SendMessageRequest;
import com.example.homehelperfinder.data.model.response.ChatMessageResponse;
import com.example.homehelperfinder.data.model.response.MarkAsReadResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.chat.ChatApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.chat.adapter.ChatMessageAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.ChatUtils;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

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

    // Data
    private ChatMessageAdapter adapter;
    private ChatApiService chatApiService;
    private SharedPrefsHelper prefsHelper;
    private Integer bookingId;
    private Integer otherUserId;
    private Integer otherHelperId;
    private String participantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();
        setupMenuNavigation();
        loadConversation();
    }

    private void initViews() {
        tvParticipantName = findViewById(R.id.tvParticipantName);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initData() {
        chatApiService = new ChatApiService(this);
        prefsHelper = SharedPrefsHelper.getInstance(this);

        // Get data from intent
        Intent intent = getIntent();
        bookingId = intent.hasExtra(Constants.INTENT_BOOKING_ID) ? 
                intent.getIntExtra(Constants.INTENT_BOOKING_ID, -1) : null;
        otherUserId = intent.hasExtra(Constants.INTENT_OTHER_USER_ID) ? 
                intent.getIntExtra(Constants.INTENT_OTHER_USER_ID, -1) : null;
        otherHelperId = intent.hasExtra(Constants.INTENT_OTHER_HELPER_ID) ? 
                intent.getIntExtra(Constants.INTENT_OTHER_HELPER_ID, -1) : null;
        participantName = intent.getStringExtra("participant_name");

        // Set participant name
        if (participantName != null && !participantName.isEmpty()) {
            tvParticipantName.setText(participantName);
        } else {
            tvParticipantName.setText("Chat");
        }

        // Validate that we have either otherUserId or otherHelperId
        if (otherUserId == null && otherHelperId == null) {
            Logger.e(TAG, "No recipient specified");
            Toast.makeText(this, "Error: No recipient specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Convert -1 to null for proper API calls
        if (bookingId != null && bookingId == -1) bookingId = null;
        if (otherUserId != null && otherUserId == -1) otherUserId = null;
        if (otherHelperId != null && otherHelperId == -1) otherHelperId = null;
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setEnabled(ChatUtils.isValidMessageContent(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
        adapter.setMessages(messages);
        
        if (messages != null && !messages.isEmpty()) {
            rvMessages.scrollToPosition(messages.size() - 1);
            markMessagesAsRead(messages);
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
}
