package com.example.homehelperfinder.ui.putBooking

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.request.BookingUpdateRequest
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.data.remote.helper.HelperApiService
import com.example.homehelperfinder.databinding.ActivityBookHelperBinding
import com.example.homehelperfinder.databinding.ActivityUpdateBookingBinding
import com.example.homehelperfinder.ui.MakePaymentActivity
import com.example.homehelperfinder.ui.bookHelper.BookHelperActivity
import com.example.homehelperfinder.ui.postRequest.adapter.ServiceAdapter
import com.example.homehelperfinder.utils.DateUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateBookingActivity : AppCompatActivity() {
    private lateinit var bookingService: BookingApiService
    private lateinit var helperService: HelperApiService
    private lateinit var binding: ActivityUpdateBookingBinding
    private lateinit var viewModel: UpdateBookingViewModel
    private val serviceList: MutableList<ServiceResponse> = ArrayList<ServiceResponse>()
    private lateinit var serviceAdapter: ServiceAdapter
    private var helperId: Int = 0
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        id = intent.getIntExtra("bookingId", 0)
//        id = 4

        bookingService = BookingApiService()
        helperService = HelperApiService()

        binding = ActivityUpdateBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[UpdateBookingViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        fetchBooking()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView() {
        setupDatePicker()
        setupServiceRVAdapters()
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnUpdateBooking.setOnClickListener {
            onSubmit()
        }
        binding.btnPayment.setOnClickListener {

            var intent = Intent(this, MakePaymentActivity::class.java)
            intent.putExtra("BOOKING_ID", id)
            intent.putExtra("USER_ID", viewModel.userId)
            startActivity(intent)
        }
    }

    fun onSubmit() {
        val request = BookingUpdateRequest(
            viewModel.bookingId,
            viewModel.userId,
            viewModel.serviceId,
            viewModel.requestId,
            viewModel.helperId,
            DateUtils.formatDateTimeForApi(viewModel.startDate.value),
            DateUtils.formatDateTimeForApi(viewModel.endDate.value),
            null,
            null,
            viewModel.status,
            null,
            null,
            null,
            null,
            0.0,
            0.0
        )

        try {
            bookingService.updateBooking(
                this,
                request,
                object : BaseApiService.ApiCallback<BookingDetailResponse> {
                    override fun onSuccess(data: BookingDetailResponse?) {
                        runOnUiThread(Runnable {
                            runOnUiThread {
                                Toast.makeText(
                                    this@UpdateBookingActivity,
                                    "update booking Successfully" + data.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            setResult(Activity.RESULT_OK)
                            finish()
                        })
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@UpdateBookingActivity,
                                "Failed to update Booking: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }

                }
            )
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@UpdateBookingActivity,
                    "Failed to update Booking: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun setupServiceRVAdapters() {
        val flexBoxLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        serviceAdapter = ServiceAdapter(serviceList) { service ->
            viewModel.serviceId = service.getServiceId()
            serviceAdapter.setSelected(viewModel.serviceId)
        }

        binding.rvServices.adapter = serviceAdapter
        binding.rvServices.layoutManager = flexBoxLayoutManager
    }

    fun fetchBooking() {
        try {
            bookingService.getBooking(
                this,
                id,
                object : BaseApiService.ApiCallback<BookingDetailResponse> {
                    override fun onSuccess(data: BookingDetailResponse?) {
                        runOnUiThread(Runnable {
                            runOnUiThread {
                                Toast.makeText(
                                    this@UpdateBookingActivity,
                                    "fetch booking Successfully" + data.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            data?.let {
                                helperId = data.helperId
                                viewModel.endDate.value =
                                    DateUtils.formatDateTimeForDisplay(data.scheduledEndTime)
                                viewModel.startDate.value =
                                    DateUtils.formatDateTimeForDisplay(data.scheduledStartTime)
                                viewModel.serviceId = data.serviceId
                                viewModel.bookingId = data.bookingId
                                viewModel.userId = data.userId
                                viewModel.helperId = data.helperId
                                viewModel.requestId = data.requestId ?: 0
                                viewModel.status = data.status
                            }
                            fetchServices()
                        })
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@UpdateBookingActivity,
                                "Failed to fetch Booking: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }

                }
            )
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@UpdateBookingActivity,
                    "Failed to fetch Booking: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    private fun fetchServices() {
        try {
            helperService.getHelperServices(
                this,
                helperId,
                object : BaseApiService.ApiCallback<List<ServiceResponse>> {
                    override fun onSuccess(services: List<ServiceResponse>) {
                        runOnUiThread(Runnable {
                            serviceList.clear()
                            serviceList.addAll(services)
                            serviceAdapter.notifyDataSetChanged()
                        })
                        if (serviceList.isNotEmpty()) {
                            serviceAdapter.setSelected(viewModel.serviceId)
                        }
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@UpdateBookingActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@UpdateBookingActivity,
                    "Error fetching services: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun setupDatePicker() {
        binding.etStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->

                    val selectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, selectedDay)
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                    }

                    if (selectedCalendar.timeInMillis < System.currentTimeMillis()) {
                        Toast.makeText(this, "Cannot select past time", Toast.LENGTH_SHORT).show()
                        return@TimePickerDialog
                    }

                    if (viewModel.endDate.value != null) {
                        val endDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .parse(viewModel.endDate.value!!)
                        if (selectedCalendar.time.after(endDate)) {
                            Toast.makeText(
                                this,
                                "Start time must be before end time",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@TimePickerDialog
                        }
                    }

                    val formatted = String.format(
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear,
                        selectedHour, selectedMinute
                    )
                    viewModel.startDate.value = formatted

                }, hour, minute, true)

                timePicker.show()

            }, year, month, day)

            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }

        binding.etEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->

                    val selectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, selectedDay)
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                    }

                    if (selectedCalendar.timeInMillis < System.currentTimeMillis()) {
                        Toast.makeText(this, "Cannot select past time", Toast.LENGTH_SHORT).show()
                        return@TimePickerDialog
                    }

                    if (viewModel.startDate.value != null) {
                        val startDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .parse(viewModel.startDate.value!!)
                        if (selectedCalendar.time.before(startDate)) {
                            Toast.makeText(
                                this,
                                "End time must be after start time",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@TimePickerDialog
                        }
                    }

                    val formatted = String.format(
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear,
                        selectedHour, selectedMinute
                    )
                    viewModel.endDate.value = formatted

                }, hour, minute, true)

                timePicker.show()

            }, year, month, day)

            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }

    }
}

class UpdateBookingViewModel() : ViewModel(){
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()
    var serviceId : Int = 0
    var bookingId : Int = 0
    var userId : Int = 0
    var requestId : Int = 0
    var helperId : Int = 0
    var status : String = ""
}