package com.example.homehelperfinder.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<ProfileModel> profiles;
    private OnProfileActionListener listener;

    public interface OnProfileActionListener {
        void onBanProfile(ProfileModel profile);
        void onUnbanProfile(ProfileModel profile);
    }

    public ProfileAdapter(List<ProfileModel> profiles) {
        this.profiles = profiles;
    }

    public void setOnProfileActionListener(OnProfileActionListener listener) {
        this.listener = listener;
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
        holder.bind(profile, listener);
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
        private final TextView textViewLastLoginDate;
        private final Button buttonBan;
        private final Button buttonUnban;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProfileId = itemView.findViewById(R.id.textViewProfileId);
            textViewProfileType = itemView.findViewById(R.id.textViewProfileType);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewRegistrationDate = itemView.findViewById(R.id.textViewRegistrationDate);
            textViewLastLoginDate = itemView.findViewById(R.id.textViewLastLoginDate);
            buttonBan = itemView.findViewById(R.id.buttonBan);
            buttonUnban = itemView.findViewById(R.id.buttonUnban);
        }

        public void bind(ProfileModel profile, OnProfileActionListener listener) {
            textViewProfileId.setText("ID: " + profile.getProfileId());
            textViewProfileType.setText("Type: " + (profile.getProfileType() != null ? profile.getProfileType() : "N/A"));
            textViewFullName.setText("Name: " + (profile.getFullName() != null ? profile.getFullName() : "N/A"));
            textViewEmail.setText("Email: " + (profile.getEmail() != null ? profile.getEmail() : "N/A"));
            textViewPhoneNumber.setText("Phone: " + (profile.getPhoneNumber() != null ? profile.getPhoneNumber() : "N/A"));

            // Determine status based on isActive
            Boolean isActive = profile.getIsActive();
            String status = (isActive != null && isActive) ? "Active" : "Inactive";
            textViewStatus.setText("Status: " + status);

            textViewRegistrationDate.setText("Registered: " + formatDate(profile.getRegistrationDate()));
            textViewLastLoginDate.setText("Last Login: " + formatDate(profile.getLastLoginDate()));

            // Show/hide buttons based on profile status
            if (isActive != null && isActive) {
                // Profile is active - show Ban button
                buttonBan.setVisibility(View.VISIBLE);
                buttonUnban.setVisibility(View.GONE);
            } else {
                // Profile is inactive/banned - show Unban button
                buttonBan.setVisibility(View.GONE);
                buttonUnban.setVisibility(View.VISIBLE);
            }

            // Set click listeners
            buttonBan.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBanProfile(profile);
                }
            });

            buttonUnban.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUnbanProfile(profile);
                }
            });
        }

        private String formatDate(String dateString) {
            if (dateString == null || dateString.trim().isEmpty()) return "N/A";
            try {
                // Handle the API date format: 2025-06-20T16:33:01.85
                SimpleDateFormat inputFormat;
                if (dateString.contains(".")) {
                    // Format with milliseconds: 2025-06-20T16:33:01.85
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
                } else if (dateString.contains("T")) {
                    // Format without milliseconds: 2025-06-20T16:33:01
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                } else {
                    // Simple date format: 2025-06-20
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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