package com.example.homehelperfinder.ui.profileManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ProfileResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProfileManagementAdapter extends RecyclerView.Adapter<ProfileManagementAdapter.ProfileViewHolder> {
    private List<ProfileResponse> profiles;
    private OnProfileActionListener listener;
    private final Set<Integer> selectedProfiles;
    private boolean isSelectionMode = false;
    private boolean isUpdatingSelection = false;

    public ProfileManagementAdapter(List<ProfileResponse> profiles) {
        this.profiles = profiles;
        this.selectedProfiles = new HashSet<>();
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
        ProfileResponse profile = profiles.get(position);
        boolean isSelected = selectedProfiles.contains(profile.getProfileId());
        holder.bind(profile, listener, isSelectionMode, isSelected, this);
    }

    @Override
    public int getItemCount() {
        return profiles != null ? profiles.size() : 0;
    }

    public void updateProfiles(List<ProfileResponse> newProfiles) {
        this.profiles = newProfiles;
        selectedProfiles.clear();
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    // Selection methods
    public void setSelectionMode(boolean selectionMode) {
        this.isSelectionMode = selectionMode;
        if (!selectionMode) {
            selectedProfiles.clear();
        }
        // Use post to defer notifyDataSetChanged until after layout
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            notifyDataSetChanged();
        });
    }

    public void toggleSelection(int profileId) {
        if (isUpdatingSelection) {
            return; // Prevent recursive calls
        }

        if (selectedProfiles.contains(profileId)) {
            selectedProfiles.remove(profileId);
        } else {
            selectedProfiles.add(profileId);
        }

        // Post the notification to avoid calling during layout
        if (listener != null) {
            listener.onSelectionChanged(getSelectedCount());
        }

        // Use post to defer notifyDataSetChanged until after layout
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            if (!isUpdatingSelection) {
                notifyDataSetChanged();
            }
        });
    }

    public void selectAll() {
        selectedProfiles.clear();
        for (ProfileResponse profile : profiles) {
            selectedProfiles.add(profile.getProfileId());
        }
        if (listener != null) {
            listener.onSelectionChanged(getSelectedCount());
        }
        // Use post to defer notifyDataSetChanged until after layout
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            notifyDataSetChanged();
        });
    }

    public void clearSelection() {
        selectedProfiles.clear();
        if (listener != null) {
            listener.onSelectionChanged(getSelectedCount());
        }
        // Use post to defer notifyDataSetChanged until after layout
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            notifyDataSetChanged();
        });
    }

    public List<ProfileResponse> getSelectedProfiles() {
        List<ProfileResponse> selected = new ArrayList<>();
        for (ProfileResponse profile : profiles) {
            if (selectedProfiles.contains(profile.getProfileId())) {
                selected.add(profile);
            }
        }
        return selected;
    }

    public int getSelectedCount() {
        return selectedProfiles.size();
    }

    public interface OnProfileActionListener {
        void onBanProfile(ProfileResponse profile);

        void onUnbanProfile(ProfileResponse profile);

        void onSelectionChanged(int selectedCount);
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxSelect;
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
            checkBoxSelect = itemView.findViewById(R.id.checkBoxSelect);
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

        public void bind(ProfileResponse profile, OnProfileActionListener listener, boolean isSelectionMode, boolean isSelected, ProfileManagementAdapter adapter) {
            // Handle checkbox visibility and state
            checkBoxSelect.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);

            // Clear previous listener to prevent unwanted triggers
            checkBoxSelect.setOnCheckedChangeListener(null);

            // Set the checked state without triggering listener
            adapter.isUpdatingSelection = true;
            checkBoxSelect.setChecked(isSelected);
            adapter.isUpdatingSelection = false;

            // Set the listener after updating the state
            checkBoxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!adapter.isUpdatingSelection) {
                    adapter.toggleSelection(profile.getProfileId());
                }
            });
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