package com.example.homehelperfinder.ui.admin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.HelperApplication;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for helper applications RecyclerView
 */
public class HelperApplicationAdapter extends RecyclerView.Adapter<HelperApplicationAdapter.ApplicationViewHolder> {
    
    private final Context context;
    private List<HelperApplication> applications;
    private OnApplicationClickListener clickListener;
    
    public interface OnApplicationClickListener {
        void onApplicationClick(HelperApplication application, int position);
    }
    
    public HelperApplicationAdapter(Context context, List<HelperApplication> applications) {
        this.context = context;
        this.applications = applications;
    }
    
    public void setOnApplicationClickListener(OnApplicationClickListener listener) {
        this.clickListener = listener;
    }
    
    public void updateApplications(List<HelperApplication> newApplications) {
        this.applications = newApplications;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_helper_application, parent, false);
        return new ApplicationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        HelperApplication application = applications.get(position);
        holder.bind(application, position);
    }
    
    @Override
    public int getItemCount() {
        return applications != null ? applications.size() : 0;
    }
    
    class ApplicationViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        private final TextView tvHelperName;
        private final TextView tvEmail;
        private final TextView tvPhoneNumber;
        private final TextView tvRegistrationDate;
        private final TextView tvPrimaryService;
        private final TextView tvDocumentCount;
        private final TextView tvSkillCount;
        private final TextView tvWorkAreaCount;
        private final Chip chipStatus;
        
        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_application);
            tvHelperName = itemView.findViewById(R.id.tv_helper_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvRegistrationDate = itemView.findViewById(R.id.tv_registration_date);
            tvPrimaryService = itemView.findViewById(R.id.tv_primary_service);
            tvDocumentCount = itemView.findViewById(R.id.tv_document_count);
            tvSkillCount = itemView.findViewById(R.id.tv_skill_count);
            tvWorkAreaCount = itemView.findViewById(R.id.tv_work_area_count);
            chipStatus = itemView.findViewById(R.id.chip_status);
        }
        
        public void bind(HelperApplication application, int position) {
            // Set basic info
            tvHelperName.setText(application.getFullName());
            tvEmail.setText(application.getEmail());
            tvPhoneNumber.setText(application.getPhoneNumber());
            tvPrimaryService.setText(application.getPrimaryService());
            
            // Format registration date
            try {
                // Assuming the date format from API is ISO 8601
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = inputFormat.parse(application.getRegistrationDate());
                tvRegistrationDate.setText(date != null ? outputFormat.format(date) : application.getRegistrationDate());
            } catch (Exception e) {
                tvRegistrationDate.setText(application.getRegistrationDate());
            }
            
            // Set counts
            tvDocumentCount.setText(String.valueOf(application.getDocumentCount()));
            tvSkillCount.setText(String.valueOf(application.getSkillCount()));
            tvWorkAreaCount.setText(String.valueOf(application.getWorkAreaCount()));
            
            // Set status chip
            setupStatusChip(application);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onApplicationClick(application, position);
                }
            });
        }
        
        private void setupStatusChip(HelperApplication application) {
            chipStatus.setText(application.getFormattedApprovalStatus());
            
            // Set chip colors based on status
            int chipBackgroundColor;
            int chipTextColor;
            
            if (application.isPending()) {
                chipBackgroundColor = ContextCompat.getColor(context, R.color.status_pending_bg);
                chipTextColor = ContextCompat.getColor(context, R.color.status_pending_text);
            } else if (application.isApproved()) {
                chipBackgroundColor = ContextCompat.getColor(context, R.color.status_approved_bg);
                chipTextColor = ContextCompat.getColor(context, R.color.status_approved_text);
            } else if (application.isRejected()) {
                chipBackgroundColor = ContextCompat.getColor(context, R.color.status_rejected_bg);
                chipTextColor = ContextCompat.getColor(context, R.color.status_rejected_text);
            } else if (application.isRevisionRequested()) {
                chipBackgroundColor = ContextCompat.getColor(context, R.color.status_revision_bg);
                chipTextColor = ContextCompat.getColor(context, R.color.status_revision_text);
            } else {
                chipBackgroundColor = ContextCompat.getColor(context, R.color.status_unknown_bg);
                chipTextColor = ContextCompat.getColor(context, R.color.status_unknown_text);
            }
            
            chipStatus.setChipBackgroundColor(ColorStateList.valueOf(chipBackgroundColor));
            chipStatus.setTextColor(chipTextColor);
        }
    }
}
