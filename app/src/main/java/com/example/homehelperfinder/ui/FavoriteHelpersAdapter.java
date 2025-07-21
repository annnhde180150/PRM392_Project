package com.example.homehelperfinder.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.FavoriteHelperDetailsDto;
import java.util.List;

public class FavoriteHelpersAdapter extends RecyclerView.Adapter<FavoriteHelpersAdapter.FavoriteHelperViewHolder> {
    private List<FavoriteHelperDetailsDto> favorites;
    private final OnRemoveFavoriteListener removeListener;

    public interface OnRemoveFavoriteListener {
        void onRemove(FavoriteHelperDetailsDto favorite);
    }

    public FavoriteHelpersAdapter(List<FavoriteHelperDetailsDto> favorites, OnRemoveFavoriteListener removeListener) {
        this.favorites = favorites;
        this.removeListener = removeListener;
    }

    public void updateFavorites(List<FavoriteHelperDetailsDto> newFavorites) {
        this.favorites = newFavorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteHelperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_helper, parent, false);
        return new FavoriteHelperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHelperViewHolder holder, int position) {
        FavoriteHelperDetailsDto favorite = favorites.get(position);
        holder.tvName.setText(favorite.getFullName());
        holder.tvEmail.setText(favorite.getEmail());
        if (!TextUtils.isEmpty(favorite.getProfilePictureUrl())) {
            Glide.with(holder.itemView.getContext())
                .load(favorite.getProfilePictureUrl())
                .placeholder(R.drawable.ic_person_placeholder)
                .error(R.drawable.ic_person_placeholder)
                .into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_person_placeholder);
        }
        holder.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites != null ? favorites.size() : 0;
    }

    static class FavoriteHelperViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvEmail;
        Button btnRemove;
        public FavoriteHelperViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivFavoriteHelperAvatar);
            tvName = itemView.findViewById(R.id.tvFavoriteHelperName);
            tvEmail = itemView.findViewById(R.id.tvFavoriteHelperEmail);
            btnRemove = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
} 