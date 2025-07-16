package com.example.homehelperfinder.ui.listBooking;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ListBookingModel;
import com.example.homehelperfinder.ui.listBooking.adapter.BookingAdapter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HelperBookingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helper_booking_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        List<ListBookingModel> bookingList = new ArrayList<>();
        bookingList.add(new ListBookingModel(
                R.drawable.ic_launcher_background,
                "Floor Cleaning",
                "#123",
                "120.000 ₫",
                "4517 Washington Ave.",
                "02 February, 2022 At 8:30 AM",
                "Arlene McCoy"
        ));
        bookingList.add(new ListBookingModel(
                R.drawable.ic_launcher_background,
                "Window Washing",
                "#124",
                "150.000 ₫",
                "1234 Elm Street",
                "05 March, 2022 At 9:00 AM",
                "Jacob Smith"
        ));
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BookingAdapter adapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);
    }
}