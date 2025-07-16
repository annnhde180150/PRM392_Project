package com.example.homehelperfinder.ui.listBooking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ListBookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<ListBookingModel> bookingList;

    public BookingAdapter(List<ListBookingModel> bookingList) {
        this.bookingList = bookingList;
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
        holder.imgService.setImageResource(item.getImageResId());
        holder.tvServiceName.setText(item.getServiceName());
        holder.tvBookingId.setText(item.getBookingId());
        holder.tvPrice.setText(item.getPrice());
        holder.tvAddress.setText(item.getAddress());
        holder.tvDateTime.setText(item.getDateTime());
        holder.tvCustomerName.setText(item.getCustomerName());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView tvServiceName, tvBookingId, tvPrice, tvDiscount, tvAddress, tvDateTime, tvCustomerName;
        Button btnAccept, btnDecline;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }
}
