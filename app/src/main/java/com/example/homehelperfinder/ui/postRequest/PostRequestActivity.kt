package com.example.homehelperfinder.ui.postRequest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.databinding.ActivityPostRequestBinding
import com.example.homehelperfinder.ui.postRequest.adapter.ServiceAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.Calendar


class PostRequestActivity() : AppCompatActivity() {
    private lateinit var serviceService : ServiceApiService
    private lateinit var binding : ActivityPostRequestBinding
    private lateinit var serviceAdapter : ServiceAdapter
    private val viewModel : PostRequestViewModel by viewModels()
    private val serviceList: MutableList<ServiceResponse> = ArrayList<ServiceResponse>()


    lateinit var rvServices : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceService = ServiceApiService()

        binding = ActivityPostRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.model = viewModel
        binding.lifecycleOwner = this
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchServices()
        initView()
    }

    fun initView(){
        rvServices = findViewById<RecyclerView>(R.id.rvServices)

        setupDatePicker()
        setupAdapters()
    }

    fun onSubmit(){

    }

    fun setupDatePicker(){
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Date Picker
            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // After picking date, show Time Picker
                val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val formatted = String.format(
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear,
                        selectedHour, selectedMinute
                    )
                    viewModel.startTime.value = formatted
                }, hour, minute, true)

                timePicker.show()

            }, year, month, day)

            datePicker.show()
        }
    }

    fun setupAdapters(){
        val flexBoxLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        serviceAdapter = ServiceAdapter(serviceList){ service ->
            viewModel.serviceId.value = service.getServiceId()
        }

        rvServices.adapter = serviceAdapter
        rvServices.layoutManager = flexBoxLayoutManager
    }

    //region for Setup
    private fun fetchServices() {
        try {
            serviceService.getActiveServices(
                this,
                object : BaseApiService.ApiCallback<List<ServiceResponse>> {
                    override fun onSuccess(services: List<ServiceResponse>) {
                        runOnUiThread(Runnable {
                            serviceList.clear()
                            serviceList.addAll(services)
                            serviceAdapter.notifyDataSetChanged()
                        })
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@PostRequestActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@PostRequestActivity,
                    "Error fetching services: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}

class PostRequestViewModel() : ViewModel(){
    var serviceId: MutableLiveData<Int> = MutableLiveData()
    var addressId: MutableLiveData<Int> = MutableLiveData()
    var startTime: MutableLiveData<String> = MutableLiveData()
    var duration: MutableLiveData<String> = MutableLiveData()
    var specialNote: MutableLiveData<String> = MutableLiveData()
}