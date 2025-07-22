package com.example.homehelperfinder.ui.viewPendingRequest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityViewUserPendingRequetsBinding
import com.example.homehelperfinder.ui.deleteRequest.DeleteRequestActivity
import com.example.homehelperfinder.ui.putRequest.EditRequestActivity
import com.example.homehelperfinder.ui.viewPendingRequest.adapter.PendingRequestAdapter
import com.example.homehelperfinder.utils.ActivityResultHandler
import com.example.homehelperfinder.utils.UserManager

class ViewUserPendingRequetsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityViewUserPendingRequetsBinding
    private lateinit var requestApi : ServiceRequestApiService
    private lateinit var serviceApi : ServiceApiService
    private lateinit var requestAdapter : PendingRequestAdapter
    private lateinit var userMan : UserManager
    private lateinit var resultHandler: ActivityResultHandler
    private val serviceList : MutableList<ServiceResponse> = ArrayList<ServiceResponse>()
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestApi = ServiceRequestApiService(this)
        serviceApi = ServiceApiService()
        userMan = UserManager.getInstance(this)
        resultHandler = ActivityResultHandler(this){
            fetchRequests()
        }
        resultHandler.register()
        id = userMan.currentUserId

        binding = ActivityViewUserPendingRequetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchRequests()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView(){
        binding.btnClose.setOnClickListener {
            finish()
        }

    }

    fun fetchRequests(){
        try{
            requestApi.getUserPending(
                this,
                id,
                object : BaseApiService.ApiCallback<List<RequestDetailResponse>>{
                    override fun onSuccess(data: List<RequestDetailResponse>?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewUserPendingRequetsActivity,
                                "Fetch Requests Successfully" + data.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        })
                        if(data == null){
                            Toast.makeText(
                                this@ViewUserPendingRequetsActivity,
                                "Failed to fetch requests",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        data?.let{ nonNullData ->
                            requestAdapter = PendingRequestAdapter(nonNullData, serviceList,
                                onEditClick = { response ->
                                    val intent = Intent(this@ViewUserPendingRequetsActivity, EditRequestActivity::class.java)
                                    intent.putExtra("requestId", response.requestId)
                                    resultHandler.launch(intent)
                                },
                                onCancelClick = { response ->
                                    val intent = Intent(this@ViewUserPendingRequetsActivity, DeleteRequestActivity::class.java)
                                    intent.putExtra("requestId", response.requestId)
                                    resultHandler.launch(intent)
                                })
                            binding.recyclerViewAvailableRequests.adapter = requestAdapter
                            binding.recyclerViewAvailableRequests.layoutManager =
                                LinearLayoutManager(this@ViewUserPendingRequetsActivity)
                            fetchServices()
                        }
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewUserPendingRequetsActivity,
                                "Failed to fetch requests: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }

                }
            )
        }catch (ex : Exception){
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@ViewUserPendingRequetsActivity,
                    "Error fetching requests: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    private fun fetchServices() {
        try {
            serviceApi.getActiveServices(
                this,
                object : BaseApiService.ApiCallback<List<ServiceResponse>> {
                    override fun onSuccess(services: List<ServiceResponse>) {
                        runOnUiThread(Runnable {
                            serviceList.clear()
                            serviceList.addAll(services)
                            requestAdapter.notifyDataSetChanged()
                        })
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewUserPendingRequetsActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@ViewUserPendingRequetsActivity,
                    "Error fetching services: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}