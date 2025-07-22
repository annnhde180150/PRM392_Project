package com.example.homehelperfinder.ui.postRequest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.databinding.ItemServicesBinding

class ServiceAdapter(
    private val services: List<ServiceResponse>,
    private val onItemClick: (ServiceResponse) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private var selectedServiceId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.binding.name = service.serviceName

        val tintColor = if (service.serviceId == selectedServiceId)
            android.graphics.Color.GRAY
        else
            android.graphics.Color.WHITE

        holder.binding.root.setBackgroundColor(tintColor)

        holder.bind(service)
    }

    override fun getItemCount(): Int = services.size

    fun setSelected(id: Int) {
        selectedServiceId = id
        notifyDataSetChanged() // Rebind all to update backgrounds
    }

    inner class ServiceViewHolder(val binding: ItemServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServiceResponse) {
            binding.root.setOnClickListener {
                onItemClick(service)
                setSelected(service.serviceId) // Optional: auto-select on click
            }
        }
    }
}
