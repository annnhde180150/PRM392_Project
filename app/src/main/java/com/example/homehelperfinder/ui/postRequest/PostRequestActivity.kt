package com.example.homehelperfinder.ui.postRequest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.model.response.UserAddressResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.address.AddressApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityPostRequestBinding
import com.example.homehelperfinder.ui.postRequest.adapter.AddressAdapter
import com.example.homehelperfinder.ui.postRequest.adapter.ServiceAdapter
import com.example.homehelperfinder.utils.DateUtils
import com.example.homehelperfinder.utils.SharedPrefsHelper
import com.example.homehelperfinder.utils.UserManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.Calendar


class PostRequestActivity() : AppCompatActivity() {
    private lateinit var serviceService : ServiceApiService
    private lateinit var addressService : AddressApiService
    private lateinit var serviceRequestService: ServiceRequestApiService
    private lateinit var binding : ActivityPostRequestBinding
    private lateinit var serviceAdapter : ServiceAdapter
    private lateinit var addressAdapter : ArrayAdapter<UserAddressResponse>
    private lateinit var userManager: UserManager
    private lateinit var pref : SharedPrefsHelper
    private var userId : Int = 0
    private val viewModel : PostRequestViewModel by viewModels()
    private val serviceList: MutableList<ServiceResponse> = ArrayList<ServiceResponse>()
    private val addressList: MutableList<UserAddressResponse> = ArrayList<UserAddressResponse>()


    lateinit var spAddress: Spinner
    lateinit var rvServices : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceService = ServiceApiService()
        addressService = AddressApiService()
        serviceRequestService = ServiceRequestApiService(this)
        pref = SharedPrefsHelper.getInstance(this)

        userManager = UserManager.getInstance(this)
        userId = userManager.currentUserId

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
        fetchAddress()
        initView()
    }

    fun initView(){
        rvServices = findViewById<RecyclerView>(R.id.rvServices)
        spAddress = findViewById<Spinner>(R.id.spAddress)
        binding.btnSubmit.setOnClickListener {
            onSubmit()
        }

        binding.btnClose.setOnClickListener {
            onSubmit()
        }

        setupDatePicker()
        setupServiceRVAdapters()
        setupAddressSpAdapters()
    }

    fun onSubmit(){
        if (viewModel.serviceId < 1 || viewModel.addressId < 1 || viewModel.startTime.value.isNullOrEmpty() || viewModel.duration.value.isNullOrEmpty()){
            Toast.makeText(
                this,
                "Please fill data in form",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val request = NewRequestRequest(
            userId,
            viewModel.serviceId,
            viewModel.addressId,
            DateUtils.formatDateTimeForApi(viewModel.startTime.value),
            viewModel.duration.value.toDouble(),
            viewModel.specialNote.value)


        try{
            serviceRequestService.createRequest(
                this,
                request,
                object : BaseApiService.ApiCallback<RequestDetailResponse>{
                    override fun onSuccess(data: RequestDetailResponse?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@PostRequestActivity,
                                "Insert Successfully" + data.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        })
//                        pref.putInt("requestId", data!!.requestId)

                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@PostRequestActivity,
                                "Failed to create service Request: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }

                }
            )
        }catch (ex : Exception){
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

                    val formatted = String.format(
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear,
                        selectedHour, selectedMinute
                    )
                    viewModel.startTime.value = formatted

                }, hour, minute, true)

                timePicker.show()

            }, year, month, day)

            datePicker.datePicker.minDate = System.currentTimeMillis()

            datePicker.show()
        }
    }

    fun setupServiceRVAdapters(){
        val flexBoxLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        serviceAdapter = ServiceAdapter(serviceList){ service ->
            viewModel.serviceId = service.getServiceId()
            serviceAdapter.setSelected(viewModel.serviceId)
        }

        rvServices.adapter = serviceAdapter
        rvServices.layoutManager = flexBoxLayoutManager
    }

    fun setupAddressSpAdapters(){
        addressAdapter = AddressAdapter(this, addressList)
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spAddress.adapter = addressAdapter

        spAddress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = parent?.getItemAtPosition(position) as UserAddressResponse
                viewModel.addressId = selected.addressId
                viewModel.address.value = selected.fullAddress
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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
                        viewModel.serviceId = serviceList[0].serviceId
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

    private fun fetchAddress() {
        try {
            addressService.getUserAddressesByUserId(
                this,
//                userManager.currentUserId,'
                1,
                object : BaseApiService.ApiCallback<List<UserAddressResponse>> {
                    override fun onSuccess(addresses: List<UserAddressResponse>) {
                        runOnUiThread(Runnable {
                            addressList.clear()
                            addressList.addAll(addresses)
                            addressAdapter.notifyDataSetChanged()
                        })
                        viewModel.addressId = addressList[0].addressId
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@PostRequestActivity,
                                "Failed to load Addresses: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@PostRequestActivity,
                    "Error fetching Addresses: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}

class PostRequestViewModel() : ViewModel(){
    var serviceId: Int = 0
    var addressId: Int = 0
    var startTime: MutableLiveData<String> = MutableLiveData()
    var duration: MutableLiveData<String> = MutableLiveData()
    var specialNote: MutableLiveData<String> = MutableLiveData()
    var address: MutableLiveData<String> = MutableLiveData()
}