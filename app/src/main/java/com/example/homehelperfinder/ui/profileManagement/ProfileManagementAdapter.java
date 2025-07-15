package com.example.homehelperfinder.ui.profileManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
        private final ImageView imageViewMenu;
        private final ImageView imageViewProfile;
        private final TextView textViewFullName;
        private final TextView textViewEmail;
        private final TextView textViewAddress;
        private final TextView textViewPhoneNumber;
        private final androidx.cardview.widget.CardView cardViewActivate;
        private final androidx.cardview.widget.CardView cardViewDeactivate;
        private final Button buttonActivate;
        private final Button buttonDeactivate;

        // Legacy elements (hidden but kept for compatibility)
        private final TextView textViewProfileId;
        private final TextView textViewProfileType;
        private final TextView textViewStatus;
        private final TextView textViewRegistrationDate;
        private final TextView textViewLastLoginDate;
        private final Button buttonBan;
        private final Button buttonUnban;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            // New design elements
            checkBoxSelect = itemView.findViewById(R.id.checkBoxSelect);
            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            cardViewActivate = itemView.findViewById(R.id.cardViewActivate);
            cardViewDeactivate = itemView.findViewById(R.id.cardViewDeactivate);
            buttonActivate = itemView.findViewById(R.id.buttonActivate);
            buttonDeactivate = itemView.findViewById(R.id.buttonDeactivate);

            // Legacy elements (hidden but kept for compatibility)
            textViewProfileId = itemView.findViewById(R.id.textViewProfileId);
            textViewProfileType = itemView.findViewById(R.id.textViewProfileType);
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

            // Bind data to new design elements
            textViewFullName.setText(profile.getFullName() != null ? profile.getFullName() : "N/A");
            textViewEmail.setText(profile.getEmail() != null ? profile.getEmail() : "N/A");
            textViewPhoneNumber.setText(profile.getPhoneNumber() != null ? profile.getPhoneNumber() : "N/A");

            // For address, we'll use a placeholder since it's not in the ProfileResponse
            // In a real implementation, you might want to add address to the API response
            textViewAddress.setText("Address not available");

            // Set profile picture placeholder (in a real app, you'd load from URL)
            imageViewProfile.setImageResource(R.drawable.ic_person_placeholder);

            // Determine status and show appropriate buttons based on active state
            Boolean isActive = profile.getIsActive();

            // Logic: If profile is active -> show only Deactivate button
            //        If profile is inactive/null -> show only Activate button
            if (isActive != null && isActive) {
                // Profile is ACTIVE - user can only DEACTIVATE
                // Hide the entire CardView container to completely remove from layout
                cardViewActivate.setVisibility(View.GONE);
                cardViewDeactivate.setVisibility(View.VISIBLE);
            } else {
                // Profile is INACTIVE or status unknown - user can only ACTIVATE
                // Hide the entire CardView container to completely remove from layout
                cardViewActivate.setVisibility(View.VISIBLE);
                cardViewDeactivate.setVisibility(View.GONE);
            }

            // Set click listeners for action buttons
            // Activate button: Changes inactive profile to active (Unban)
            buttonActivate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUnbanProfile(profile); // Activate = Unban
                }
            });

            // Deactivate button: Changes active profile to inactive (Ban)
            buttonDeactivate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBanProfile(profile); // Deactivate = Ban
                }
            });

            // Three-dot menu click listener (for future functionality)
            imageViewMenu.setOnClickListener(v -> {
                // TODO: Show popup menu with additional options
                // For now, we'll just show a simple toast
                v.getContext().getApplicationContext();
            });

            // Update legacy elements (hidden but still functional for compatibility)
            textViewProfileId.setText("ID: " + profile.getProfileId());
            textViewProfileType.setText("Type: " + (profile.getProfileType() != null ? profile.getProfileType() : "N/A"));
            String status = (isActive != null && isActive) ? "Active" : "Inactive";
            textViewStatus.setText("Status: " + status);
            textViewRegistrationDate.setText("Registered: " + formatDate(profile.getRegistrationDate()));
            textViewLastLoginDate.setText("Last Login: " + formatDate(profile.getLastLoginDate()));

            // Legacy button listeners (hidden but functional)
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