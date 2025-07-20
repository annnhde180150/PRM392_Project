package com.example.homehelperfinder.ui.helperSearch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ServiceResponse;

import java.util.List;

public class ServiceSelectionAdapter extends RecyclerView.Adapter<ServiceSelectionAdapter.ServiceViewHolder> {
    
    private List<ServiceResponse> serviceList;
    private OnServiceClickListener listener;
    private int selectedPosition = -1;
    
    public interface OnServiceClickListener {
        void onServiceSelected(ServiceResponse service);
    }
    
    public ServiceSelectionAdapter(List<ServiceResponse> serviceList, OnServiceClickListener listener) {
        this.serviceList = serviceList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_selection, parent, false);
        return new ServiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceResponse service = serviceList.get(position);
        holder.bind(service, position == selectedPosition);
    }
    
    @Override
    public int getItemCount() {
        return serviceList.size();
    }
    
    public void setSelectedService(int position) {
        int previousSelected = selectedPosition;
        selectedPosition = position;
        
        if (previousSelected != -1) {
            notifyItemChanged(previousSelected);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }
    
    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private ImageView ivServiceIcon;
        private TextView tvServiceName;
        
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            ivServiceIcon = itemView.findViewById(R.id.ivServiceIcon);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            
            rootView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedService(position);
                    if (listener != null) {
                        listener.onServiceSelected(serviceList.get(position));
                    }
                }
            });
        }
        
        public void bind(ServiceResponse service, boolean isSelected) {
            tvServiceName.setText(service.getServiceName());
            
            // Load service icon if available
            if (service.getIconUrl() != null && !service.getIconUrl().isEmpty()) {
                Glide.with(ivServiceIcon.getContext())
                        .load(service.getIconUrl())
                        .placeholder(R.drawable.ic_helper)
                        .error(R.drawable.ic_helper)
                        .into(ivServiceIcon);
            } else {
                ivServiceIcon.setImageResource(R.drawable.ic_helper);
            }
            
            // Update selection state
            rootView.setSelected(isSelected);
            if (isSelected) {
                rootView.setBackgroundResource(R.drawable.bg_service_selected);
            } else {
                rootView.setBackgroundResource(R.drawable.bg_service_unselected);
            }
        }
    }
} 