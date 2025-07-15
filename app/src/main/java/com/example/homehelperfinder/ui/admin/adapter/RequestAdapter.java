package com.example.homehelperfinder.ui.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.RequestItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying admin requests in RecyclerView
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    
    private final List<RequestItem> requestList;
    private final Context context;
    private OnRequestClickListener clickListener;
    
    // Date formatters
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final SimpleDateFormat creationDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public RequestAdapter(Context context, List<RequestItem> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    public void setOnRequestClickListener(OnRequestClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestItem request = requestList.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    /**
     * Update the request list with new data
     */
    public void updateRequests(List<RequestItem> newRequests) {
        requestList.clear();
        requestList.addAll(newRequests);
        notifyDataSetChanged();
    }

    /**
     * Add more requests to the list (for pagination)
     */
    public void addRequests(List<RequestItem> newRequests) {
        int startPosition = requestList.size();
        requestList.addAll(newRequests);
        notifyItemRangeInserted(startPosition, newRequests.size());
    }

    /**
     * Clear all requests
     */
    public void clearRequests() {
        requestList.clear();
        notifyDataSetChanged();
    }

    /**
     * Format date string from API format to display format
     */
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "N/A";
        }
        
        try {
            Date date = inputDateFormat.parse(dateString);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            return dateString; // Return original if parsing fails
        }
    }

    /**
     * Get status background color based on status
     */
    private int getStatusBackgroundColor(String status) {
        if (status == null) return R.drawable.bg_status_default;
        
        switch (status.toLowerCase()) {
            case "pending":
                return R.drawable.bg_status_pending;
            case "confirmed":
            case "accepted":
                return R.drawable.bg_status_confirmed;
            case "in_progress":
            case "ongoing":
                return R.drawable.bg_status_in_progress;
            case "completed":
                return R.drawable.bg_status_completed;
            case "cancelled":
                return R.drawable.bg_status_cancelled;
            case "rejected":
                return R.drawable.bg_status_rejected;
            default:
                return R.drawable.bg_status_default;
        }
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRequestId;
        private final TextView tvStatus;
        private final TextView tvUserName;
        private final TextView tvUserEmail;
        private final LinearLayout layoutHelper;
        private final TextView tvHelperName;
        private final TextView tvServices;
        private final TextView tvScheduledTime;
        private final TextView tvDuration;
        private final TextView tvLocation;
        private final LinearLayout layoutSpecialNotes;
        private final TextView tvSpecialNotes;
        private final TextView tvCreationTime;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvRequestId = itemView.findViewById(R.id.tv_request_id);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            layoutHelper = itemView.findViewById(R.id.layout_helper);
            tvHelperName = itemView.findViewById(R.id.tv_helper_name);
            tvServices = itemView.findViewById(R.id.tv_services);
            tvScheduledTime = itemView.findViewById(R.id.tv_scheduled_time);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvLocation = itemView.findViewById(R.id.tv_location);
            layoutSpecialNotes = itemView.findViewById(R.id.layout_special_notes);
            tvSpecialNotes = itemView.findViewById(R.id.tv_special_notes);
            tvCreationTime = itemView.findViewById(R.id.tv_creation_time);

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onRequestClick(requestList.get(position), position);
                    }
                }
            });
        }

        public void bind(RequestItem request) {
            // Request ID
            tvRequestId.setText("Request #" + request.getRequestId());
            
            // Status
            tvStatus.setText(request.getFormattedStatus());
            tvStatus.setBackgroundResource(getStatusBackgroundColor(request.getStatus()));
            
            // User info
            if (request.getUser() != null) {
                tvUserName.setText(request.getUser().getName());
                tvUserEmail.setText(request.getUser().getEmail());
            } else {
                tvUserName.setText("Unknown User");
                tvUserEmail.setText("");
            }
            
            // Helper info
            if (request.hasHelper()) {
                layoutHelper.setVisibility(View.VISIBLE);
                tvHelperName.setText(request.getHelper().getName());
            } else {
                layoutHelper.setVisibility(View.GONE);
            }
            
            // Services
            tvServices.setText(request.getServicesAsString());
            
            // Scheduled time
            tvScheduledTime.setText(formatDate(request.getScheduledTime()));
            
            // Duration
            tvDuration.setText(String.format(Locale.getDefault(), "%.1fh", request.getRequestedDurationHours()));
            
            // Location
            tvLocation.setText(request.getLocation());
            
            // Special notes
            if (request.getSpecialNotes() != null && !request.getSpecialNotes().trim().isEmpty()) {
                layoutSpecialNotes.setVisibility(View.VISIBLE);
                tvSpecialNotes.setText(request.getSpecialNotes());
            } else {
                layoutSpecialNotes.setVisibility(View.GONE);
            }
            
            // Creation time
            tvCreationTime.setText("Tạo: " + formatDate(request.getRequestCreationTime()));
        }
    }

    /**
     * Interface for handling request item clicks
     */
    public interface OnRequestClickListener {
        void onRequestClick(RequestItem request, int position);
    }
}
