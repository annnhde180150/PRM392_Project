package com.example.homehelperfinder.ui.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperSearchResult;
import com.example.homehelperfinder.data.model.response.UserSearchResult;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for displaying search results (users and helpers) in chat search
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {
    
    private static final int TYPE_USER = 0;
    private static final int TYPE_HELPER = 1;
    
    private Context context;
    private List<Object> searchResults;
    private OnSearchResultClickListener listener;
    
    public interface OnSearchResultClickListener {
        void onUserClick(UserSearchResult user);
        void onHelperClick(HelperSearchResult helper);
        void onStartChatClick(Object result);
    }
    
    public SearchResultsAdapter(Context context) {
        this.context = context;
        this.searchResults = new ArrayList<>();
    }
    
    public void setOnSearchResultClickListener(OnSearchResultClickListener listener) {
        this.listener = listener;
    }
    
    public void updateResults(List<UserSearchResult> users, List<HelperSearchResult> helpers) {
        searchResults.clear();
        if (users != null) {
            searchResults.addAll(users);
        }
        if (helpers != null) {
            searchResults.addAll(helpers);
        }
        notifyDataSetChanged();
    }
    
    public void clearResults() {
        searchResults.clear();
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemViewType(int position) {
        Object item = searchResults.get(position);
        if (item instanceof UserSearchResult) {
            return TYPE_USER;
        } else if (item instanceof HelperSearchResult) {
            return TYPE_HELPER;
        }
        return TYPE_USER;
    }
    
    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Object item = searchResults.get(position);
        
        if (item instanceof UserSearchResult) {
            bindUserResult(holder, (UserSearchResult) item);
        } else if (item instanceof HelperSearchResult) {
            bindHelperResult(holder, (HelperSearchResult) item);
        }
    }
    
    private void bindUserResult(SearchResultViewHolder holder, UserSearchResult user) {
        // Basic info
        holder.tvName.setText(user.getDisplayName());
        holder.tvEmail.setText(user.getEmail());
        
        // User type badge
        holder.tvUserType.setText("User");
        holder.tvUserType.setBackgroundResource(R.drawable.bg_user_badge);
        
        // Profile picture
        loadProfilePicture(holder.ivProfilePicture, user.getProfilePictureUrl());
        
        // Hide helper-specific info
        holder.layoutHelperInfo.setVisibility(View.GONE);
        holder.tvSkills.setVisibility(View.GONE);
        
        // Show conversation status if exists
        if (user.hasConversation()) {
            holder.layoutConversationStatus.setVisibility(View.VISIBLE);
        } else {
            holder.layoutConversationStatus.setVisibility(View.GONE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
        
        holder.btnStartChat.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStartChatClick(user);
            }
        });
    }
    
    private void bindHelperResult(SearchResultViewHolder holder, HelperSearchResult helper) {
        // Basic info
        holder.tvName.setText(helper.getDisplayName());
        holder.tvEmail.setText(helper.getEmail());
        
        // Helper type badge
        holder.tvUserType.setText("Helper");
        holder.tvUserType.setBackgroundResource(R.drawable.bg_helper_badge);
        
        // Profile picture
        loadProfilePicture(holder.ivProfilePicture, helper.getProfilePictureUrl());
        
        // Show helper-specific info
        holder.layoutHelperInfo.setVisibility(View.VISIBLE);
        
        // Rating
        holder.tvRating.setText(helper.getFormattedRating());
        
        // Availability status
        holder.tvAvailabilityStatus.setText(helper.getAvailabilityStatusText());
        setAvailabilityStatusColor(holder.tvAvailabilityStatus, helper.getAvailableStatus());
        
        // Skills
        if (helper.getSkills() != null && !helper.getSkills().isEmpty()) {
            holder.tvSkills.setVisibility(View.VISIBLE);
            holder.tvSkills.setText(helper.getSkillsText());
        } else {
            holder.tvSkills.setVisibility(View.GONE);
        }
        
        // Show conversation status if exists
        if (helper.hasConversation()) {
            holder.layoutConversationStatus.setVisibility(View.VISIBLE);
        } else {
            holder.layoutConversationStatus.setVisibility(View.GONE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHelperClick(helper);
            }
        });
        
        holder.btnStartChat.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStartChatClick(helper);
            }
        });
    }
    
    private void loadProfilePicture(CircleImageView imageView, String profilePictureUrl) {
        if (profilePictureUrl != null && !profilePictureUrl.trim().isEmpty()) {
            Glide.with(context)
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }
    }
    
    private void setAvailabilityStatusColor(TextView textView, Integer status) {
        int colorRes;
        int drawableRes = R.drawable.ic_circle;
        
        if (status == null) {
            colorRes = R.color.text_hint;
        } else {
            switch (status) {
                case 0: // Available
                    colorRes = R.color.status_available;
                    break;
                case 1: // Busy
                    colorRes = R.color.status_busy;
                    break;
                case 2: // Offline
                    colorRes = R.color.status_offline;
                    break;
                default:
                    colorRes = R.color.text_hint;
                    break;
            }
        }
        
        textView.setTextColor(ContextCompat.getColor(context, colorRes));
        textView.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
        textView.getCompoundDrawables()[0].setTint(ContextCompat.getColor(context, colorRes));
    }
    
    @Override
    public int getItemCount() {
        return searchResults.size();
    }
    
    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivProfilePicture;
        TextView tvName;
        TextView tvUserType;
        TextView tvEmail;
        LinearLayout layoutHelperInfo;
        TextView tvRating;
        TextView tvAvailabilityStatus;
        TextView tvSkills;
        LinearLayout layoutConversationStatus;
        MaterialButton btnStartChat;
        
        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserType = itemView.findViewById(R.id.tvUserType);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            layoutHelperInfo = itemView.findViewById(R.id.layoutHelperInfo);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvAvailabilityStatus = itemView.findViewById(R.id.tvAvailabilityStatus);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            layoutConversationStatus = itemView.findViewById(R.id.layoutConversationStatus);
            btnStartChat = itemView.findViewById(R.id.btnStartChat);
        }
    }
}
