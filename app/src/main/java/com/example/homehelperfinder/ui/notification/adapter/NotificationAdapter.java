package com.example.homehelperfinder.ui.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.NotificationModel;
import com.example.homehelperfinder.utils.DateUtils;

import java.util.List;

/**
 * RecyclerView adapter for displaying notifications
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<NotificationModel> notifications;
    private final OnNotificationClickListener listener;

    /**
     * Interface for handling notification clicks
     */
    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationModel notification);
        void onNotificationLongClick(NotificationModel notification);
    }

    public NotificationAdapter(Context context, List<NotificationModel> notifications, 
                             OnNotificationClickListener listener) {
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * ViewHolder for notification items
     */
    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewNotificationIcon;
        private final TextView textViewTitle;
        private final TextView textViewMessage;
        private final TextView textViewType;
        private final TextView textViewTime;
        private final View unreadIndicator;
        private final ImageView imageViewMenu;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewNotificationIcon = itemView.findViewById(R.id.imageViewNotificationIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNotificationClick(notifications.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNotificationLongClick(notifications.get(position));
                    return true;
                }
                return false;
            });

            imageViewMenu.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNotificationLongClick(notifications.get(position));
                }
            });
        }

        /**
         * Bind notification data to views
         */
        public void bind(NotificationModel notification) {
            // Set title
            textViewTitle.setText(notification.getTitle());
            
            // Set message
            textViewMessage.setText(notification.getMessage());
            
            // Set notification type
            textViewType.setText(notification.getNotificationType());
            setNotificationTypeStyle(notification.getNotificationType());
            
            // Set time
            String timeText = DateUtils.getRelativeTimeSpan(notification.getCreationTime());
            textViewTime.setText(timeText);
            
            // Set unread indicator
            if (notification.isUnread()) {
                unreadIndicator.setVisibility(View.VISIBLE);
                // Make unread notifications more prominent
                textViewTitle.setTextColor(context.getResources().getColor(R.color.text_primary, null));
                itemView.setAlpha(1.0f);
            } else {
                unreadIndicator.setVisibility(View.GONE);
                // Make read notifications less prominent
                textViewTitle.setTextColor(context.getResources().getColor(R.color.text_secondary, null));
                itemView.setAlpha(0.7f);
            }
            
            // Set notification icon based on type
            setNotificationIcon(notification.getNotificationType());
        }

        /**
         * Set notification icon based on type
         */
        private void setNotificationIcon(String notificationType) {
            int iconRes;
            int backgroundTint;
            
            switch (notificationType) {
                case NotificationModel.TYPE_BOOKING:
                    iconRes = R.drawable.ic_calendar_today;
                    backgroundTint = R.color.primary_color;
                    break;
                case NotificationModel.TYPE_MESSAGE:
                    iconRes = R.drawable.ic_message;
                    backgroundTint = R.color.secondary_color;
                    break;
                case NotificationModel.TYPE_PAYMENT:
                    iconRes = R.drawable.ic_payment;
                    backgroundTint = R.color.accent_color;
                    break;
                case NotificationModel.TYPE_SYSTEM:
                    iconRes = R.drawable.ic_settings;
                    backgroundTint = R.color.text_secondary;
                    break;
                case NotificationModel.TYPE_REMINDER:
                    iconRes = R.drawable.ic_alarm;
                    backgroundTint = R.color.warning_color;
                    break;
                case NotificationModel.TYPE_UPDATE:
                    iconRes = R.drawable.ic_update;
                    backgroundTint = R.color.info_color;
                    break;
                default:
                    iconRes = R.drawable.ic_notification;
                    backgroundTint = R.color.primary_color;
                    break;
            }
            
            imageViewNotificationIcon.setImageResource(iconRes);
            imageViewNotificationIcon.setBackgroundTintList(
                    context.getResources().getColorStateList(backgroundTint, null));
        }

        /**
         * Set notification type badge style
         */
        private void setNotificationTypeStyle(String notificationType) {
            int backgroundTint;
            
            switch (notificationType) {
                case NotificationModel.TYPE_BOOKING:
                    backgroundTint = R.color.primary_color;
                    break;
                case NotificationModel.TYPE_MESSAGE:
                    backgroundTint = R.color.secondary_color;
                    break;
                case NotificationModel.TYPE_PAYMENT:
                    backgroundTint = R.color.accent_color;
                    break;
                case NotificationModel.TYPE_SYSTEM:
                    backgroundTint = R.color.text_secondary;
                    break;
                case NotificationModel.TYPE_REMINDER:
                    backgroundTint = R.color.warning_color;
                    break;
                case NotificationModel.TYPE_UPDATE:
                    backgroundTint = R.color.info_color;
                    break;
                default:
                    backgroundTint = R.color.primary_color;
                    break;
            }
            
            textViewType.setBackgroundTintList(
                    context.getResources().getColorStateList(backgroundTint, null));
        }
    }

    /**
     * Update notifications list
     */
    public void updateNotifications(List<NotificationModel> newNotifications) {
        notifications.clear();
        notifications.addAll(newNotifications);
        notifyDataSetChanged();
    }

    /**
     * Add new notification to the top of the list
     */
    public void addNotification(NotificationModel notification) {
        notifications.add(0, notification);
        notifyItemInserted(0);
    }

    /**
     * Remove notification from list
     */
    public void removeNotification(int position) {
        if (position >= 0 && position < notifications.size()) {
            notifications.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Update specific notification
     */
    public void updateNotification(NotificationModel updatedNotification) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getNotificationId().equals(updatedNotification.getNotificationId())) {
                notifications.set(i, updatedNotification);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * Get notification at position
     */
    public NotificationModel getNotificationAt(int position) {
        if (position >= 0 && position < notifications.size()) {
            return notifications.get(position);
        }
        return null;
    }
}
