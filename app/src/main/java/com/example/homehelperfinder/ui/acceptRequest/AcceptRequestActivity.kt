package com.example.homehelperfinder.ui.acceptRequest

import android.app.Activity
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
import com.example.homehelperfinder.data.model.request.AcceptRequestRequest
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.model.response.UserAddressResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.address.AddressApiService
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityAcceptRequestBinding
import com.example.homehelperfinder.ui.postRequest.PostRequestActivity
import com.example.homehelperfinder.ui.putRequest.EditRequestActivity
import com.example.homehelperfinder.utils.DateUtils
import com.example.homehelperfinder.utils.SharedPrefsHelper
import com.example.homehelperfinder.utils.UserManager

class AcceptRequestActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAcceptRequestBinding
    private lateinit var viewModel : AcceptRequestViewModel
    private lateinit var serviceApi : ServiceApiService
    private lateinit var addressApi : AddressApiService
    private lateinit var requestApi : ServiceRequestApiService
    private lateinit var bookingApi : BookingApiService
    private lateinit var userManager : UserManager
    private lateinit var pref : SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pref = SharedPrefsHelper.getInstance(this)
        serviceApi = ServiceApiService()
        addressApi = AddressApiService()
        requestApi = ServiceRequestApiService(this)
        bookingApi = BookingApiService()
        userManager = UserManager.getInstance(this)

        binding = ActivityAcceptRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AcceptRequestViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val id = intent.getIntExtra("requestId", 0)
        fetchRequest(id)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView(){
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            val request = AcceptRequestRequest(
                viewModel.requestId,
                userManager.currentUserId
//                1
            )

            try{
                bookingApi.acceptRequest(
                    this,
                    request,
                    object : BaseApiService.ApiCallback<BookingDetailResponse> {
                        override fun onSuccess(request: BookingDetailResponse) {
                            Toast.makeText(
                                this@AcceptRequestActivity,
                                "Acccept Succesfully",
                                Toast.LENGTH_LONG
                            ).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onError(errorMessage: String?, throwable: Throwable?) {
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@AcceptRequestActivity,
                                    "Failed to Acccept : " + errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }
                    }
                )
            }catch (ex : Exception){
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@AcceptRequestActivity,
                        "Error trying Acccept: " + ex.message,
                        Toast.LENGTH_LONG
                    ).show()
                })
                ex.printStackTrace()
            }
        }
    }

    fun fetchRequest(id : Int){
        try{
            requestApi.getRequest(
                this,
                id,
                object : BaseApiService.ApiCallback<RequestDetailResponse> {
                    override fun onSuccess(request: RequestDetailResponse) {
                        viewModel.startTime.value = DateUtils.formatDateTimeForDisplay(request.requestedStartTime)
                        viewModel.duration.value = request.requestedDurationHours.toString()
                        viewModel.specialNote.value = request.specialNotes
                        viewModel.requestId = request.requestId
                        fetchService(request.serviceId)
                        fetchAddress(request.addressId)
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@AcceptRequestActivity,
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
                    this@AcceptRequestActivity,
                    "Error fetching request: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun fetchService(id : Int){
        try {
            serviceApi.getService(
                this,
                id,
                object : BaseApiService.ApiCallback<ServiceResponse> {
                    override fun onSuccess(services: ServiceResponse) {
                        viewModel.service.value = services.serviceName
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@AcceptRequestActivity,
                                "Failed to load service: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@AcceptRequestActivity,
                    "Error fetching service: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun fetchAddress(id : Int){
        try {
            addressApi.getAddress(
                this,
                id,
                object : BaseApiService.ApiCallback<UserAddressResponse> {
                    override fun onSuccess(address: UserAddressResponse) {
                        viewModel.address.value = address.fullAddress
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@AcceptRequestActivity,
                                "Failed to load address: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@AcceptRequestActivity,
                    "Error fetching address: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}

class AcceptRequestViewModel() : ViewModel(){
    var service = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var startTime = MutableLiveData<String>()
    var duration = MutableLiveData<String>()
    var specialNote = MutableLiveData<String>()
    var requestId : Int = 0
}