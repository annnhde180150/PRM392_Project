package com.example.homehelperfinder.ui.cancelBooking

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.request.BookingCancelRequest
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.databinding.ActivityCancelBookingBinding
import com.example.homehelperfinder.ui.putRequest.EditRequestActivity
import com.example.homehelperfinder.utils.UserManager

class CancelBookingActivity : AppCompatActivity() {
    private lateinit var bookingApi : BookingApiService
    private lateinit var binding : ActivityCancelBookingBinding
    private lateinit var userMan : UserManager
    private var bookingId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookingApi = BookingApiService()

        userMan = UserManager.getInstance(this)
        bookingId = intent.getIntExtra("bookingId", 10)

        binding = ActivityCancelBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.btnConfirmCancel.setOnClickListener {
            var request = BookingCancelRequest(
                bookingId = bookingId,
                cancellationReason = binding.etCancelReason.text.toString(),
                cancelledBy = userMan.currentUserType ?: "User"
            )
            try{
                bookingApi.cancelBooking(
                    this,
                    request,
                    object : BaseApiService.ApiCallback<String> {
                        override fun onSuccess(request: String) {
                            Toast.makeText(
                                this@CancelBookingActivity,
                                "cancel booking Successfully ",
                                Toast.LENGTH_LONG
                            ).show()
//                            finish()
                        }

                        override fun onError(errorMessage: String?, throwable: Throwable?) {
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@CancelBookingActivity,
                                    "Failed to cancel booking: " + errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }
                    }
                )
            }catch (ex : Exception){
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@CancelBookingActivity,
                        "Error cancel booking: " + ex.message,
                        Toast.LENGTH_LONG
                    ).show()
                })
                ex.printStackTrace()
            }
        }
    }
}