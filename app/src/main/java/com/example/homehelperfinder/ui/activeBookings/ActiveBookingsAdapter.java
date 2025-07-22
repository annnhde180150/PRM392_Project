package com.example.homehelperfinder.ui.activeBookings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse;
import java.util.List;

public class ActiveBookingsAdapter extends RecyclerView.Adapter<ActiveBookingsAdapter.BookingViewHolder> {
    private final List<GetAllServiceRequestResponse> bookings;
    public ActiveBookingsAdapter(List<GetAllServiceRequestResponse> bookings) {
        this.bookings = bookings;
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int bookingId);
    }
    private OnUpdateClickListener updateClickListener;
    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_booking, parent, false);
        return new BookingViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        GetAllServiceRequestResponse booking = bookings.get(position);
        holder.tvServiceName.setText(booking.getServiceName());
        holder.tvStatus.setText(booking.getStatus());
        holder.tvScheduledTime.setText(booking.getScheduledStartTime() + " - " + booking.getScheduledEndTime());
        holder.tvAddress.setText(booking.getFullAddress());
        holder.tvFullName.setText(booking.getFullName());
        holder.tvPrice.setText(String.valueOf(booking.getEstimatedPrice()));
        holder.btnUpdateStatus.setOnClickListener(v -> {
            if (updateClickListener != null) {
                updateClickListener.onUpdateClick(booking.getBookingId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return bookings.size();
    }
    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvStatus, tvScheduledTime, tvAddress, tvFullName, tvPrice;
        Button btnUpdateStatus;
        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvScheduledTime = itemView.findViewById(R.id.tvScheduledTime);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
} 