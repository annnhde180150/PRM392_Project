package com.example.homehelperfinder.ui.viewRequests

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
import com.example.homehelperfinder.data.remote.booking.BookingApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.data.remote.serviceRequest.ServiceRequestApiService
import com.example.homehelperfinder.databinding.ActivityViewAvailableRequestBinding
import com.example.homehelperfinder.ui.acceptRequest.AcceptRequestActivity
import com.example.homehelperfinder.ui.postRequest.PostRequestActivity
import com.example.homehelperfinder.ui.viewRequests.adapter.RequestAdapter

class ViewAvailableRequestActivity : AppCompatActivity() {
    private lateinit var binding : ActivityViewAvailableRequestBinding
    private lateinit var serviceApi : ServiceApiService
    private lateinit var bookingApi : BookingApiService
    private lateinit var requestApi : ServiceRequestApiService
    private lateinit var requestAdapter : RequestAdapter
    private val serviceList : MutableList<ServiceResponse> = ArrayList<ServiceResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewAvailableRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceApi = ServiceApiService()
        bookingApi = BookingApiService()
        requestApi = ServiceRequestApiService(this)

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
            requestApi.getAvailableRequests(
                this,
                object : BaseApiService.ApiCallback<List<RequestDetailResponse>>{
                    override fun onSuccess(data: List<RequestDetailResponse>?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewAvailableRequestActivity,
                                "Fetch Requests Successfully" + data.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        })
                        if(data == null){
                            Toast.makeText(
                                this@ViewAvailableRequestActivity,
                                "Failed to fetch requests",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        data?.let{ nonNullData ->
                            requestAdapter = RequestAdapter(nonNullData, serviceList){ response ->
                                val intent = Intent(this@ViewAvailableRequestActivity,
                                    AcceptRequestActivity::class.java)
                                intent.putExtra("requestId", response.requestId)
                                startActivity(intent)
                            }
                            binding.recyclerViewAvailableRequests.adapter = requestAdapter
                            binding.recyclerViewAvailableRequests.layoutManager =
                                LinearLayoutManager(this@ViewAvailableRequestActivity)
                            fetchServices()
                        }
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewAvailableRequestActivity,
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
                    this@ViewAvailableRequestActivity,
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
                                this@ViewAvailableRequestActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@ViewAvailableRequestActivity,
                    "Error fetching services: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }
}