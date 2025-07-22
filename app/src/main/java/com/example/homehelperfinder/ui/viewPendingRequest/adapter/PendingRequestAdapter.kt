package com.example.homehelperfinder.ui.viewPendingRequest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.databinding.ItemAvailableRequestBinding
import com.example.homehelperfinder.databinding.ItemPendingUserRequestBinding
import com.example.homehelperfinder.utils.DateUtils

class PendingRequestAdapter(
    private val requestList: List<RequestDetailResponse>,
    private val serviceList: List<ServiceResponse>,
    private val onEditClick: (RequestDetailResponse) -> Unit,
    private val onCancelClick: (RequestDetailResponse) -> Unit
) : RecyclerView.Adapter<PendingRequestAdapter.PendingRequestViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingRequestViewHolder {
        val binding = ItemPendingUserRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingRequestViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PendingRequestViewHolder,
        position: Int
    ) {
        val binding = holder.binding
        val request = requestList[position]
        val serviceName = serviceList.find { it.serviceId == request.serviceId }?.serviceName

        binding.tvServices.text = serviceName
        binding.tvScheduledTime.text = DateUtils.formatDateTimeForDisplay(request.requestedStartTime)
        binding.tvDuration.text = request.requestedDurationHours.toString()
        binding.tvCreationTime.text = DateUtils.getRelativeTimeSpan(request.requestCreationTime)

        binding.btnEdit.setOnClickListener {
            onEditClick(request)
        }
        binding.btnCancel.setOnClickListener {
            onCancelClick(request)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    inner class PendingRequestViewHolder(val binding: ItemPendingUserRequestBinding) :
        RecyclerView.ViewHolder(binding.root)
}