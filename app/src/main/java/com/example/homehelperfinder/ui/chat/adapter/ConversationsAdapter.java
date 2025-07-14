package com.example.homehelperfinder.ui.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ChatConversationResponse;
import com.example.homehelperfinder.utils.ChatUtils;
import com.example.homehelperfinder.utils.CircularImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying conversations list in RecyclerView
 */
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {
    private Context context;
    private List<ChatConversationResponse> conversations;
    private OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(ChatConversationResponse conversation);
    }

    public ConversationsAdapter(Context context) {
        this.context = context;
        this.conversations = new ArrayList<>();
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ChatConversationResponse conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public void setConversations(List<ChatConversationResponse> conversations) {
        this.conversations.clear();
        if (conversations != null) {
            this.conversations.addAll(conversations);
        }
        notifyDataSetChanged();
    }

    public void addConversation(ChatConversationResponse conversation) {
        if (conversation != null) {
            conversations.add(0, conversation); // Add to top
            notifyItemInserted(0);
        }
    }

    public void updateConversation(ChatConversationResponse updatedConversation) {
        if (updatedConversation == null) return;

        for (int i = 0; i < conversations.size(); i++) {
            ChatConversationResponse conversation = conversations.get(i);
            if (conversation.getConversationId().equals(updatedConversation.getConversationId())) {
                conversations.set(i, updatedConversation);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void clearConversations() {
        conversations.clear();
        notifyDataSetChanged();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePicture;
        TextView tvParticipantName;
        TextView tvLastMessage;
        TextView tvLastActivity;
        TextView tvUnreadCount;

        ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvParticipantName = itemView.findViewById(R.id.tvParticipantName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastActivity = itemView.findViewById(R.id.tvLastActivity);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onConversationClick(conversations.get(position));
                }
            });
        }

        void bind(ChatConversationResponse conversation) {
            // Set participant name
            String displayName = ChatUtils.getParticipantDisplayName(
                    conversation.getParticipantName(),
                    conversation.getParticipantType()
            );
            tvParticipantName.setText(displayName);

            // Set last message
            if (conversation.getLastMessage() != null) {
                String lastMessageContent = ChatUtils.truncateMessage(
                        conversation.getLastMessage().getMessageContent(), 50
                );
                tvLastMessage.setText(lastMessageContent);
                tvLastMessage.setVisibility(View.VISIBLE);
            } else {
                tvLastMessage.setText("No messages yet");
                tvLastMessage.setVisibility(View.VISIBLE);
            }

            // Set last activity time
            String relativeTime = ChatUtils.getRelativeTimeString(conversation.getLastActivity());
            tvLastActivity.setText(relativeTime);

            // Set unread count
            if (conversation.getUnreadCount() > 0) {
                tvUnreadCount.setText(String.valueOf(conversation.getUnreadCount()));
                tvUnreadCount.setVisibility(View.VISIBLE);
            } else {
                tvUnreadCount.setVisibility(View.GONE);
            }

            // Load profile picture with circular transformation
            if (ivProfilePicture != null) {
                String profilePictureUrl = conversation.getParticipantProfilePicture();
                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                    CircularImageUtils.loadCircularImage(context, profilePictureUrl, ivProfilePicture, R.drawable.ic_guest_icon);
                } else {
                    ivProfilePicture.setImageResource(R.drawable.ic_guest_icon);
                }
            }
        }
    }
}
