package com.example.homehelperfinder.ui.viewPendingBooking

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
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.databinding.ActivityViewPendingBookingBinding
import com.example.homehelperfinder.ui.cancelBooking.CancelBookingActivity
import com.example.homehelperfinder.ui.postRequest.PostRequestActivity
import com.example.homehelperfinder.ui.putBooking.UpdateBookingActivity
import com.example.homehelperfinder.ui.viewPendingBooking.adapter.PendingBookingAdapter
import com.example.homehelperfinder.utils.ActivityResultHandler
import com.example.homehelperfinder.utils.UserManager

class ViewPendingBookingActivity : AppCompatActivity(), PendingBookingAdapter.OnPendingBookingActionListener {
    private lateinit var binding: ActivityViewPendingBookingBinding
    private lateinit var bookingApi : BookingApiService
    private lateinit var userMan : UserManager
    private lateinit var bookingAdapter : PendingBookingAdapter
    private lateinit var resultHandler: ActivityResultHandler
    private var bookingList : MutableList<BookingDetailResponse> = ArrayList<BookingDetailResponse>()
    private var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookingApi = BookingApiService()
        userMan = UserManager.getInstance(this)
        resultHandler = ActivityResultHandler(this){
            fetchPendingBookings()
        }
        resultHandler.register()
        userId = userMan.currentUserId
//        userId = 1

        binding = ActivityViewPendingBookingBinding.inflate(layoutInflater)
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
            bookingApi.getBookingByUser(
                this,
                userId,
                object : BaseApiService.ApiCallback<List<BookingDetailResponse>>{
                    override fun onSuccess(data: List<BookingDetailResponse>?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewPendingBookingActivity,
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
                                this@ViewPendingBookingActivity,
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
                    this@ViewPendingBookingActivity,
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
        resultHandler.launch(intent)
    }

    override fun onCancelBooking(booking: BookingDetailResponse) {
        val intent = Intent(this, CancelBookingActivity::class.java)
        intent.putExtra("bookingId", booking.bookingId)
        resultHandler.launch(intent)
    }
}