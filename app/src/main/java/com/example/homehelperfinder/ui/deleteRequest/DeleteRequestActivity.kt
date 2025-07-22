package com.example.homehelperfinder.ui.deleteRequest

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
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityDeleteRequestBinding
import com.example.homehelperfinder.databinding.ActivityEditRequestBinding
import com.example.homehelperfinder.ui.putRequest.EditRequestViewModel
import com.example.homehelperfinder.utils.DateUtils
import com.example.homehelperfinder.utils.SharedPrefsHelper

class DeleteRequestActivity : AppCompatActivity() {
    private lateinit var serviceRequestService: ServiceRequestApiService
    private lateinit var serviceService : ServiceApiService
    private lateinit var binding : ActivityDeleteRequestBinding
    private lateinit var viewModel : DeleteRequestViewModel
    private lateinit var pref : SharedPrefsHelper
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        pref = SharedPrefsHelper.getInstance(this)
        serviceRequestService = ServiceRequestApiService(this)
        serviceService = ServiceApiService()

//        id = pref.getInt("requestId")
        id = intent.getIntExtra("requestId", 0)

        binding = ActivityDeleteRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DeleteRequestViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        fetchRequest()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    fun onSubmit(){
        try{
            serviceRequestService.deleteRequest(
                this,
                id,
                object : BaseApiService.ApiCallback<String> {
                    override fun onSuccess(data : String) {
                        Toast.makeText(
                            this@DeleteRequestActivity,
                            "Delete request Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@DeleteRequestActivity,
                                "Failed to delete request: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                }
            )
        }catch (ex : Exception){
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@DeleteRequestActivity,
                    "Error fetching request: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun initView(){
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            onSubmit()
        }
    }

    fun fetchService(id : Int){
        try{
            serviceService.getService(
                this,
                id,
                object : BaseApiService.ApiCallback<ServiceResponse> {
                    override fun onSuccess(request: ServiceResponse) {
                        viewModel.serviceName.value = request.serviceName
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@DeleteRequestActivity,
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
                    this@DeleteRequestActivity,
                    "Error fetching request: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
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
                        viewModel.duration.value = request.requestedDurationHours.toString()
                        viewModel.serviceId.value = request.serviceId.toString()
                        fetchService(request.serviceId)
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@DeleteRequestActivity,
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
                    this@DeleteRequestActivity,
                    "Error fetching request: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}

class DeleteRequestViewModel() : ViewModel(){
    val startTime = MutableLiveData<String>()
    val duration = MutableLiveData<String>()
    val serviceId = MutableLiveData<String>()
    val serviceName = MutableLiveData<String>()
    var requestId : Int = 0
}