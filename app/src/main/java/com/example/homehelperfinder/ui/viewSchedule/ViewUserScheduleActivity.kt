package com.example.homehelperfinder.ui.viewSchedule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.databinding.ActivityViewPendingBookingBinding
import com.example.homehelperfinder.databinding.ActivityViewUserScheduleBinding
import com.example.homehelperfinder.ui.cancelBooking.CancelBookingActivity
import com.example.homehelperfinder.ui.putBooking.UpdateBookingActivity
import com.example.homehelperfinder.ui.viewPendingBooking.ViewPendingBookingActivity
import com.example.homehelperfinder.ui.viewPendingBooking.adapter.PendingBookingAdapter
import com.example.homehelperfinder.utils.UserManager

class ViewUserScheduleActivity : AppCompatActivity(), PendingBookingAdapter.OnPendingBookingActionListener {
    private lateinit var binding: ActivityViewUserScheduleBinding
    private lateinit var bookingApi : BookingApiService
    private lateinit var userMan : UserManager
    private lateinit var bookingAdapter : PendingBookingAdapter
    private var bookingList : MutableList<BookingDetailResponse> = ArrayList<BookingDetailResponse>()
    private var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookingApi = BookingApiService()
        userMan = UserManager.getInstance(this)
//        userId = userMan.currentUserId
        userId = 1

        binding = ActivityViewUserScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchPendingBookings()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView(){
        binding.btnBack.setOnClickListener {
            finish()
        }
        setUpRecyclerView()
    }

    fun setUpRecyclerView(){
        bookingAdapter = PendingBookingAdapter(
            bookingList,
            this
        )

        binding.rvPendingBookings.adapter = bookingAdapter
        binding.rvPendingBookings.layoutManager = LinearLayoutManager(this)
    }

    fun fetchPendingBookings(){
        try{
            bookingApi.getUserSchedule(
                this,
                userId,
                object : BaseApiService.ApiCallback<List<BookingDetailResponse>>{
                    override fun onSuccess(data: List<BookingDetailResponse>?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewUserScheduleActivity,
                                "fetch Successfully" + data.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        })
                        bookingList.clear()
                        data?.let {
                            bookingList.addAll(it)
                        }
                        bookingAdapter.notifyDataSetChanged()
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewUserScheduleActivity,
                                "Failed to fetc bookings: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }

                }
            )
        }catch (ex : Exception){
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@ViewUserScheduleActivity,
                    "Error fetching bookings: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    override fun onEditBooking(booking: BookingDetailResponse) {
        val intent = Intent(this, UpdateBookingActivity::class.java)
        intent.putExtra("bookingId", booking.bookingId)
        startActivity(intent)
    }

    override fun onCancelBooking(booking: BookingDetailResponse) {
        val intent = Intent(this, CancelBookingActivity::class.java)
        intent.putExtra("bookingId", booking.bookingId)
        startActivity(intent)
    }
}