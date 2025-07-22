package com.example.homehelperfinder.ui.viewRequests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.databinding.ItemAvailableRequestBinding
import com.example.homehelperfinder.utils.DateUtils

class RequestAdapter(
    private val requestList : List<RequestDetailResponse>,
    private val serviceList : List<ServiceResponse>,
    private val onAccept : (RequestDetailResponse) -> Unit
) : RecyclerView.Adapter<RequestAdapter.AvailableRequestViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailableRequestViewHolder {
        val binding = ItemAvailableRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AvailableRequestViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AvailableRequestViewHolder,
        position: Int
    ) {
        val binding = holder.binding
        val request = requestList[position]
        val serviceName = serviceList.find { it.serviceId == request.serviceId}?.serviceName

        binding.tvServices.text = serviceName
        binding.tvScheduledTime.text = DateUtils.formatDateTimeForDisplay(request.requestedStartTime)
        binding.tvDuration.text = request.requestedDurationHours.toString()
        binding.tvCreationTime.text = DateUtils.getRelativeTimeSpan(request.requestCreationTime)

        binding.root.setOnClickListener {
            onAccept(request)
        }

        binding.btnAccept.setOnClickListener {
            onAccept(request)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    inner class AvailableRequestViewHolder(val binding : ItemAvailableRequestBinding) :
    RecyclerView.ViewHolder(binding.root){

    }
}