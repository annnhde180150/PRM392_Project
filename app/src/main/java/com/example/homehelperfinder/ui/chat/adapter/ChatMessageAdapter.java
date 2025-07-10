package com.example.homehelperfinder.ui.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ChatMessageResponse;
import com.example.homehelperfinder.utils.ChatUtils;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying chat messages in RecyclerView
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<ChatMessageResponse> messages;
    private SharedPrefsHelper prefsHelper;

    public ChatMessageAdapter(Context context) {
        this.context = context;
        this.messages = new ArrayList<>();
        this.prefsHelper = SharedPrefsHelper.getInstance(context);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageResponse message = messages.get(position);
        String currentUserId = prefsHelper.getUserId();
        String currentUserType = prefsHelper.getUserType();

        if (ChatUtils.isMessageFromCurrentUser(
                message.getSenderUserId(),
                message.getSenderHelperId(),
                currentUserType,
                currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View view = inflater.inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageResponse message = messages.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<ChatMessageResponse> messages) {
        this.messages.clear();
        if (messages != null) {
            this.messages.addAll(messages);
        }
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessageResponse message) {
        if (message != null) {
            messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }
    }

    public void addMessages(List<ChatMessageResponse> newMessages) {
        if (newMessages != null && !newMessages.isEmpty()) {
            int startPosition = messages.size();
            messages.addAll(newMessages);
            notifyItemRangeInserted(startPosition, newMessages.size());
        }
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }

    // ViewHolder for sent messages
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTimestamp;
        TextView tvReadStatus;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvReadStatus = itemView.findViewById(R.id.tvReadStatus);
        }

        void bind(ChatMessageResponse message) {
            tvMessage.setText(message.getMessageContent());
            tvTimestamp.setText(ChatUtils.formatTimestampForDisplay(message.getTimestamp()));
            
            if (tvReadStatus != null) {
                if (message.getIsReadByReceiver() != null && message.getIsReadByReceiver()) {
                    tvReadStatus.setText("Read");
                    tvReadStatus.setVisibility(View.VISIBLE);
                } else {
                    tvReadStatus.setText("Sent");
                    tvReadStatus.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // ViewHolder for received messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTimestamp;
        TextView tvSenderName;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
        }

        void bind(ChatMessageResponse message) {
            tvMessage.setText(message.getMessageContent());
            tvTimestamp.setText(ChatUtils.formatTimestampForDisplay(message.getTimestamp()));
            
            if (tvSenderName != null && message.getSenderName() != null) {
                tvSenderName.setText(message.getSenderName());
                tvSenderName.setVisibility(View.VISIBLE);
            } else if (tvSenderName != null) {
                tvSenderName.setVisibility(View.GONE);
            }
        }
    }
}
