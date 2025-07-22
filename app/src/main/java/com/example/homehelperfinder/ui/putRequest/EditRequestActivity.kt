package com.example.homehelperfinder.ui.putRequest

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.request.UpdateRequestRequest
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.model.response.UserAddressResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.address.AddressApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityEditRequestBinding
import com.example.homehelperfinder.ui.postRequest.PostRequestActivity
import com.example.homehelperfinder.ui.postRequest.adapter.AddressAdapter
import com.example.homehelperfinder.ui.postRequest.adapter.ServiceAdapter
import com.example.homehelperfinder.ui.viewPendingRequest.ViewUserPendingRequetsActivity
import com.example.homehelperfinder.utils.DateUtils
import com.example.homehelperfinder.utils.SharedPrefsHelper
import com.example.homehelperfinder.utils.UserManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.Calendar
import kotlin.getValue

class EditRequestActivity : AppCompatActivity() {
    private lateinit var serviceService : ServiceApiService
    private lateinit var addressService : AddressApiService
    private lateinit var serviceRequestService: ServiceRequestApiService
    private lateinit var binding : ActivityEditRequestBinding
    private lateinit var serviceAdapter : ServiceAdapter
    private lateinit var addressAdapter : AddressAdapter
    private lateinit var userManager: UserManager
    private lateinit var pref : SharedPrefsHelper
    private lateinit var viewModel : EditRequestViewModel
    private var id : Int = 0
    private val serviceList: MutableList<ServiceResponse> = ArrayList<ServiceResponse>()
    private val addressList: MutableList<UserAddressResponse> = ArrayList<UserAddressResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pref = SharedPrefsHelper.getInstance(this)
        id = intent.getIntExtra("requestId", 0)
//        id = pref.getInt("requestId")


        serviceRequestService = ServiceRequestApiService(this)
        addressService = AddressApiService()
        serviceService = ServiceApiService()


        fetchRequest()
        fetchAddress()
        fetchServices()

        binding = ActivityEditRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[EditRequestViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView(){
        setupDatePicker()
        setupAddressSpAdapters()
        setupServiceRVAdapters()
        binding.tvServiceId.text = id.toString()
        val intent = Intent(this, ViewUserPendingRequetsActivity::class.java);

        binding.btnConfirm.setOnClickListener {
            onSubmit()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
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

        var request = UpdateRequestRequest(
            viewModel.requestId,
//            userManager.currentUserId,
            1,
            viewModel.serviceId,
            viewModel.addressId,
            DateUtils.formatDateTimeForApi(viewModel.startTime.value.toString()),
            viewModel.duration.value.toDouble(),
            viewModel.specialNote.value.toString(),
            viewModel.status
        )

        try{
            serviceRequestService.updateRequest(
                this,
                request,
                object : BaseApiService.ApiCallback<RequestDetailResponse> {
                    override fun onSuccess(request: RequestDetailResponse) {
                        Toast.makeText(
                            this@EditRequestActivity,
                            "Update request Successfully ",
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@EditRequestActivity,
                                "Failed to update request: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                }
            )
        }catch (ex : Exception){
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@EditRequestActivity,
                    "Error fetching request: " + ex.message,
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

        binding.rvServices.adapter = serviceAdapter
        binding.rvServices.layoutManager = flexBoxLayoutManager
    }

    fun setupAddressSpAdapters(){
        addressAdapter = AddressAdapter(this, addressList)
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAddress.adapter = addressAdapter

        binding.spAddress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

    fun fetchRequest(){
        try{
            serviceRequestService.getRequest(
                this,
                id,
                object : BaseApiService.ApiCallback<RequestDetailResponse> {
                    override fun onSuccess(request: RequestDetailResponse) {
                        viewModel.startTime.value = DateUtils.formatDateTimeForDisplay(request.requestedStartTime)
                        viewModel.requestId = request.requestId
                        viewModel.serviceId = request.serviceId
                        viewModel.addressId = request.addressId
                        viewModel.duration.value = request.requestedDurationHours.toString()
                        viewModel.specialNote.value = request.specialNotes
                        viewModel.status = request.status
                        if(serviceList.size > 0)
                            serviceAdapter.setSelected(request.serviceId)
                        if(addressList.size > 0)
                            setSpinnerSelection(request.addressId)
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@EditRequestActivity,
                                "Failed to load request: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                }
            )
        }catch (ex : Exception){
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@EditRequestActivity,
                    "Error fetching request: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

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
                            if(viewModel.serviceId > 0)
                                serviceAdapter.setSelected(viewModel.serviceId)
                        })
                        viewModel.serviceId = serviceList[0].serviceId
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@EditRequestActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@EditRequestActivity,
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
                            if(viewModel.addressId > 0)
                                setSpinnerSelection(viewModel.addressId)
                        })
                        viewModel.addressId = addressList[0].addressId
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@EditRequestActivity,
                                "Failed to load Addresses: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@EditRequestActivity,
                    "Error fetching Addresses: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun setSpinnerSelection(id : Int){
        var position = addressAdapter.getPositionById(id)
        if(position > 0)
            binding.spAddress.setSelection(position)
    }
}