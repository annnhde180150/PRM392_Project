package com.example.homehelperfinder.ui.postRequest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.databinding.ItemServicesBinding

class ServiceAdapter(
    private val services : List<ServiceResponse>,
    private val onItemClick: (ServiceResponse) -> Unit) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder {
        val binding = ItemServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ServiceViewHolder,
        position: Int
    ) {
        holder.binding.name = services[position].serviceName
        holder.bind(services[position])
    }

    override fun getItemCount(): Int {
        return services.size
    }

    inner class ServiceViewHolder(val binding : ItemServicesBinding) :
            RecyclerView.ViewHolder(binding.root){
        fun bind(service : ServiceResponse) {
            binding.root.setOnClickListener {
                onItemClick(service)
            }
        }
            }
}