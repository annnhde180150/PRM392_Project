package com.example.homehelperfinder.ui.editProfile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;

import java.util.List;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.AddressViewHolder> {
    private List<UserAddressResponse> addresses;
    private int defaultIndex = -1;
    private OnAddressActionListener listener;
    private boolean showDefaultSelector = false;
    public int defaultAddressId = -1;
    private boolean showEditDeleteButtons = false;

    public interface OnAddressActionListener {
        void onEdit(int position, UserAddressResponse address);
        void onDelete(int position, UserAddressResponse address);
        void onDefaultSelected(int position, UserAddressResponse address);
    }

    public UserAddressAdapter(List<UserAddressResponse> addresses, OnAddressActionListener listener) {
        this.addresses = addresses;
        this.listener = listener;
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).isDefault()) {
                defaultIndex = i;
                break;
            }
        }
    }

    public void setShowDefaultSelector(boolean show) {
        this.showDefaultSelector = show;
        notifyDataSetChanged();
    }

    public void setDefaultAddressId(int defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
        notifyDataSetChanged();
    }

    public void setShowEditDeleteButtons(boolean show) {
        this.showEditDeleteButtons = show;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddressResponse address = addresses.get(position);
        holder.tvFullAddress.setText(address.getFullAddress());
        holder.rbDefault.setChecked(address.getAddressId() == defaultAddressId);
        holder.layoutDefaultSelector.setVisibility(showDefaultSelector ? View.VISIBLE : View.GONE);
        if (holder.tvDefaultIndicator != null) {
            holder.tvDefaultIndicator.setVisibility((address.getAddressId() == defaultAddressId && !showDefaultSelector) ? View.VISIBLE : View.GONE);
        }
        holder.btnEdit.setVisibility(showEditDeleteButtons ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(showEditDeleteButtons ? View.VISIBLE : View.GONE);
        holder.rbDefault.setOnClickListener(v -> {
            listener.onDefaultSelected(position, address);
        });
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position, address));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position, address));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbDefault;
        TextView tvFullAddress;
        ImageButton btnEdit, btnDelete;
        View layoutDefaultSelector;
        TextView tvDefaultIndicator;
        AddressViewHolder(View itemView) {
            super(itemView);
            rbDefault = itemView.findViewById(R.id.rbDefault);
            tvFullAddress = itemView.findViewById(R.id.tvFullAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutDefaultSelector = itemView.findViewById(R.id.layoutDefaultSelector);
            tvDefaultIndicator = itemView.findViewById(R.id.tvDefaultIndicator);
        }
    }
} 