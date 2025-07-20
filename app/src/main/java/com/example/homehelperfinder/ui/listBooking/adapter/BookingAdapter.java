package com.example.homehelperfinder.ui.listBooking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ListBookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<ListBookingModel> bookingList;
    private OnBookingActionListener actionListener;
    
    public interface OnBookingActionListener {
        void onAcceptBooking(int requestId,int bookingId);
        void onDeclineBooking(int requestId,int bookingId);
    }

    public BookingAdapter(List<ListBookingModel> bookingList) {
        this.bookingList = bookingList;
    }
    
    public void setOnBookingActionListener(OnBookingActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_card, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        ListBookingModel item = bookingList.get(position);
        holder.tvServiceName.setText(item.getServiceName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvAddress.setText(item.getAddress());
        holder.tvStartTime.setText("Start: " + item.getStartTime());
        holder.tvEndTime.setText(" - End: " + item.getEndTime());
        holder.tvCustomerName.setText(item.getCustomerName());

        // Handle button clicks
        holder.btnAccept.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAcceptBooking(item.getRequestId(),item.getBookingId());
            }
        });
        
        holder.btnDecline.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeclineBooking(item.getRequestId(),item.getBookingId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView tvServiceName, tvPrice, tvAddress, tvStartTime, tvEndTime, tvCustomerName;
        Button btnAccept, btnDecline;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }

    public void updateBookings(List<ListBookingModel> newBookings) {
        this.bookingList.clear();
        if (newBookings != null) {
            this.bookingList.addAll(newBookings);
        }
        notifyDataSetChanged();
    }
}
