package com.example.homehelperfinder.ui.viewFeedBack

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.ReviewResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.review.ReviewApiService
import com.example.homehelperfinder.data.remote.service.ServiceApiService
import com.example.homehelperfinder.ui.viewFeedBack.adapter.FeedBackAdapter
import com.example.homehelperfinder.ui.viewRequests.ViewAvailableRequestActivity
import com.example.homehelperfinder.utils.UserManager

class ViewFeedbacckActivity : AppCompatActivity() {
    private lateinit var userMan : UserManager
    private lateinit var reviewApi : ReviewApiService
    private lateinit var serviceApi : ServiceApiService
    private var helperId : Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeedBackAdapter
    private var serviceList : MutableList<ServiceResponse> = ArrayList<ServiceResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        serviceApi = ServiceApiService()
        reviewApi = ReviewApiService()
        userMan = UserManager.getInstance(this)
//        helperId = userMan.currentUserId ?: 0
        helperId = 8

        setContentView(R.layout.activity_view_feedbacck)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        adapter = FeedBackAdapter(emptyList(), serviceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchReviews()
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
                            adapter.notifyDataSetChanged()
                        })
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@ViewFeedbacckActivity,
                                "Failed to load services: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    }
                })
        } catch (ex: Exception) {
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@ViewFeedbacckActivity,
                    "Error fetching services: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            })
            ex.printStackTrace()
        }
    }

    fun fetchReviews(){
        try{
            reviewApi.getHelperReviews(
                this,
                helperId,
                object : BaseApiService.ApiCallback<List<ReviewResponse>>{
                    override fun onSuccess(data: List<ReviewResponse>?) {
                        runOnUiThread {
                            if (data != null) {
                                adapter.updateReviews(data)
                            }
                            Toast.makeText(
                                this@ViewFeedbacckActivity,
                                "fetch Successfully" + data.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        fetchServices()
                    }

                    override fun onError(errorMessage: String?, throwable: Throwable?) {
                        runOnUiThread {
                            Toast.makeText(
                                this@ViewFeedbacckActivity,
                                "Failed to fetch reviews: " + errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            )
        }catch (ex : Exception){
            runOnUiThread {
                Toast.makeText(
                    this@ViewFeedbacckActivity,
                    "Error fetching reviews: " + ex.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            ex.printStackTrace()
        }
    }
}