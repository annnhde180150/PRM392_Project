package com.example.homehelperfinder.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for displaying profile items
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<ProfileModel> profiles;

    public ProfileAdapter(List<ProfileModel> profiles) {
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        ProfileModel profile = profiles.get(position);
        holder.bind(profile);
    }

    @Override
    public int getItemCount() {
        return profiles != null ? profiles.size() : 0;
    }

    public void updateProfiles(List<ProfileModel> newProfiles) {
        this.profiles = newProfiles;
        notifyDataSetChanged();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewProfileId;
        private final TextView textViewProfileType;
        private final TextView textViewFullName;
        private final TextView textViewEmail;
        private final TextView textViewPhoneNumber;
        private final TextView textViewStatus;
        private final TextView textViewRegistrationDate;
        private final TextView textViewBanReason;
        private final TextView textViewBannedAt;
        private final TextView textViewBanDuration;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProfileId = itemView.findViewById(R.id.textViewProfileId);
            textViewProfileType = itemView.findViewById(R.id.textViewProfileType);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewRegistrationDate = itemView.findViewById(R.id.textViewRegistrationDate);
            textViewBanReason = itemView.findViewById(R.id.textViewBanReason);
            textViewBannedAt = itemView.findViewById(R.id.textViewBannedAt);
            textViewBanDuration = itemView.findViewById(R.id.textViewBanDuration);
        }

        public void bind(ProfileModel profile) {
            textViewProfileId.setText("ID: " + profile.getProfileId());
            textViewProfileType.setText("Type: " + profile.getProfileType());
            textViewFullName.setText("Name: " + (profile.getFullName() != null ? profile.getFullName() : "N/A"));
            textViewEmail.setText("Email: " + (profile.getEmail() != null ? profile.getEmail() : "N/A"));
            textViewPhoneNumber.setText("Phone: " + (profile.getPhoneNumber() != null ? profile.getPhoneNumber() : "N/A"));
            
            // Determine status based on isActive or isBanned
            String status = profile.isActive() ? "Active" : "Inactive";
            if (profile.getIsBanned()) {
                status = "Banned";
            }
            textViewStatus.setText("Status: " + status);
            
            textViewRegistrationDate.setText("Registered: " + formatDate(profile.getRegistrationDate()));

            // Show ban details only if profile is banned
            if (profile.getIsBanned()) {
                textViewBanReason.setVisibility(View.VISIBLE);
                textViewBannedAt.setVisibility(View.VISIBLE);
                textViewBanDuration.setVisibility(View.VISIBLE);

                textViewBanReason.setText("Ban Reason: " + (profile.getBanReason() != null ? profile.getBanReason() : "N/A"));
                textViewBannedAt.setText("Banned At: " + formatDate(profile.getBannedAt()));
                textViewBanDuration.setText("Ban Duration: " + (profile.getBanDuration() != null ? profile.getBanDuration() + " days" : "N/A"));
            } else {
                textViewBanReason.setVisibility(View.GONE);
                textViewBannedAt.setVisibility(View.GONE);
                textViewBanDuration.setVisibility(View.GONE);
            }
        }

        private String formatDate(String dateString) {
            if (dateString == null) return "N/A";
            try {
                // Handle both ISO format with milliseconds and without
                SimpleDateFormat inputFormat;
                if (dateString.contains(".")) {
                    // Format: 2025-06-20T16:33:01.85
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
                } else {
                    // Format: 2024-01-15T10:30:00Z
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                }
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return date != null ? outputFormat.format(date) : "N/A";
            } catch (Exception e) {
                return dateString; // Return original string if parsing fails
            }
        }
    }
} 